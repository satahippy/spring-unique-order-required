# unique-order-required

## Configuration

If you're using Spring Boot, then no additional configuration is required (auto-configuration will do everything for you).

Otherwise you need to register `UniqueOrderRequiredBeanPostProcessor`:

```java
@Configuration
class Config {

    @Bean
    public static UniqueOrderRequiredBeanPostProcessor postProcessor() {
        return new UniqueOrderRequiredBeanPostProcessor();
    }
}
```

## Usage

Just place `@OrderRequired` on iterable field where you need to check unique ordering of items:

```java
@Component
class SomeService {

    @OrderRequired
    @Autowired
    Collection<Bean> beans;
}
```