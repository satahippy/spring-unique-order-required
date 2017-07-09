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

## Release

We're using Sonatype for releasing.

In order to make this work you need to specify some gradle properties in `~/.gradle/gradle.properties`.
You can find example of this file in resources.