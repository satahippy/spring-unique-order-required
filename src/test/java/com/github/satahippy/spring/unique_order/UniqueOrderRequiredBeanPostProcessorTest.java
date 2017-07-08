package com.github.satahippy.spring.unique_order;

import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Collection;

public class UniqueOrderRequiredBeanPostProcessorTest {

    interface TestBean {}

    @Order(1)
    static class BeanFirst implements TestBean {}

    @Order(2)
    static class BeanSecond implements TestBean {}

    static class BeanNone implements TestBean {}

    @Configuration
    static class Config {
        @Bean
        static UniqueOrderRequiredBeanPostProcessor postProcessor() {
            return new UniqueOrderRequiredBeanPostProcessor();
        }
    }

    @Test(expected = BeanCreationException.class)
    public void onlyIterable() {
        new AnnotationConfigApplicationContext(OnlyIterableBeanConfig.class);
    }

    @Configuration
    static class OnlyIterableBeanConfig extends Config {
        @OrderRequired
        int field;
    }

    @Test(expected = BeanCreationException.class)
    public void orderShouldBePresented() {
        new AnnotationConfigApplicationContext(OrderShouldBePresentedConfig.class);
    }

    @Configuration
    static class OrderShouldBePresentedConfig extends Config {
        @OrderRequired
        @Autowired
        Collection<TestBean> beans;

        @Bean
        static TestBean bean1() {
            return new BeanFirst();
        }

        @Bean
        static TestBean bean2() {
            return new BeanNone();
        }
    }

    @Test(expected = BeanCreationException.class)
    public void orderShouldBeUnique() {
        new AnnotationConfigApplicationContext(OrderShouldBeUniqueConfig.class);
    }

    @Configuration
    static class OrderShouldBeUniqueConfig extends Config {
        @OrderRequired
        @Autowired
        Collection<TestBean> beans;

        @Bean
        static TestBean bean1() {
            return new BeanFirst();
        }

        @Bean
        static TestBean bean2() {
            return new BeanFirst();
        }
    }

    @Test
    public void success() {
        new AnnotationConfigApplicationContext(SuccessConfig.class);
    }

    @Configuration
    static class SuccessConfig extends Config {
        @OrderRequired
        @Autowired
        Collection<TestBean> beans;

        @Bean
        static TestBean bean1() {
            return new BeanFirst();
        }

        @Bean
        static TestBean bean2() {
            return new BeanSecond();
        }
    }
}