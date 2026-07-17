
-------------Controller Layer-----------------
Controllers is like handlers in go 

In springboot  you use annotation to  map your path 

e.g @RestController
@RequestMapping("/api/auth")
public class AuthController 


Now   e.g.    example/api/auth calls this class 


you can differentiate them even more to
@PostMapping("/login")

so for example if you hit api/auth/login it will call the login method in the AuthController class.

you can have for example multiple methods in the same controller class to handle different endpoints.
and diffrent HTTP methods like GET, POST, PUT, DELETE etc.

if you have the same name for example ("/login") method
if you have different http methods like GET and POST you can differentiate them by using the appropriate annotation
 like 
 @GetMapping and @PostMapping.


----Record Class-----------------
to Create a class with getters and setters you can use record
example  

public record User(String Username, String Password) {
    // You can add additional methods if needed

    // but you get the getters and setters automatically generated for you
}


so you can call   
user.getUsername() and user.getPassword() to access the values of the fields.


-----------DTO Layer-------------

Dto  layer is used to Transffer data between different layers of the application, 
especially between the controller and service layers.


example in this application 
we have LoginRequest and LoginResponse classes in the dto package.

LoginRequest class is used to encapsulate the data sent from the client to the server in json format 
when a user attempts to log in

LoginResponse does th opposite , and it send a token back to the client after successful authentication.





----------------REPOSITORY LAYER-------------------
Repository Layer is used to interact with the Database ,

note springBoot use JPA (Java Persistence API) to handle database operations, 
and you can create repository interfaces that extend JpaRepository or CrudRepository
 to perform CRUD operations on your entities.

you can also define custom query methods in your repository interfaces using Spring Data JPA's 
method naming conventions or by using the @Query annotation.

method naming conventions allow you to define query methods based on the names of your entity's fields,

example naming convention is findByUsername(String username) 
which will generate a query

Find user
where Username = username ;
and many others see 

Naming conventions www.spring.io/guides/gs/accessing-data-jpa/






 