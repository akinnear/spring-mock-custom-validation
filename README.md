# spring-mock-custom-validation
This a simple example of how to write a unit test which creates an injectable mock for a custom validator.

To start create an annotation for your custom validation, in my case it was ```ValidLogin```.

Next create a constraint validator, in my case it was ```ValidLoginValidator```.

Code the validator and include any dependencies you need, in my case 
I created a simplistic interface,  ```IUserRepository```, to tell me if a user name is valid.

Next add an instance of ```LocalValidatorFactoryBean``` to your context
```java
@Bean
public LocalValidatorFactoryBean validator() {
	return new LocalValidatorFactoryBean();
}
```
Create an instance of the validation annotation
```java
@ValidLogin
private String login;
```

Next create a test, auto wire in the ```Validator``` and add a mockito ```@MockBean``` containing the dependencies the validator requires
```java
@Autowired
private Validator validator;

@MockBean
private IUserRepository userRepo;
```

Ensure your validator is in the configuration of the test
```java
@ContextConfiguration(classes= {ValidationConfiguration.class})
```

Write your test while mocking the validators dependencies! 