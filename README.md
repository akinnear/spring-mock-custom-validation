# spring-mock-custom-validation
This a simple example of how to write a unit test which creates an injectable mock for a custom validator.
It also mocks a custom validator faking that validation occurred.

## To inject a mock into a custom validator
Create an annotation for your custom validation

Create a constraint validator

Code the validator and include any dependencies you need

Next add an instance of ```LocalValidatorFactoryBean``` to your context
```java
@Bean
public LocalValidatorFactoryBean validator() {
	return new LocalValidatorFactoryBean();
}
```
Create an instance of the validation annotation
```java
@ValidUserName
private String userName;
```

Next create a test, auto wire in the ```Validator``` and add a mockito ```@MockBean``` containing the dependencies the validator requires
```java
@Autowired
private Validator validator;

@MockBean
private IUserUtils userUtils;
```

Ensure your validator is in the configuration of the test
```java
@ContextConfiguration(classes= {ValidationConfiguration.class})
```

Write your test while mocking the validators dependencies! 
