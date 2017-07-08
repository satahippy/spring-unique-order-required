package com.github.satahippy.spring.unique_order;

import java.lang.reflect.Field;
import java.util.*;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
public class UniqueOrderRequiredBeanPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		for (Field field : getAllFields(bean.getClass())) {
			if (field.isAnnotationPresent(OrderRequired.class)) {
				checkField(bean, field);
			}
		}
		return bean;
	}

	private static List<Field> getAllFields(Class<?> type) {
		List<Field> fields = new LinkedList<>();
		fields.addAll(Arrays.asList(type.getDeclaredFields()));

		if (type.getSuperclass() != null) {
			fields.addAll(getAllFields(type.getSuperclass()));
		}

		return fields;
	}

	private static void checkField(Object bean, Field field) {
		if (!Iterable.class.isAssignableFrom(field.getType())) {
			throw new RuntimeException("OrderRequired can be used only on Iterable, " + prepareFieldForExceptionReport(field));
		}

		boolean originalAccessible = field.isAccessible();
		field.setAccessible(true);

		Set<Integer> orders = new HashSet<>();
		try {
			Iterable<?> items = (Iterable<?>) field.get(bean);
			for (Object item : items) {
				Class<?> itemClazz = item.getClass();
				if (!itemClazz.isAnnotationPresent(Order.class)) {
					throw new RuntimeException("Order annotation should be presented in OrderRequired items, " + prepareFieldForExceptionReport(field));
				}
				Order order = itemClazz.getAnnotation(Order.class);
				if (!orders.add(order.value())) {
					throw new RuntimeException("Order value should be unique in OrderRequired items, " + prepareFieldForExceptionReport(field));
				}
			}
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

		field.setAccessible(originalAccessible);
	}

	private static String prepareFieldForExceptionReport(Field field) {
		return String.format("used on field [%s]", field);
	}
}
