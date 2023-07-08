### How to use this spring-boot project

- Install packages with `mvn package`
- Run `mvn spring-boot:run` for starting the application (or use your IDE)

Application (with the embedded H2 database) is ready to be used ! You can access the url below for testing it :

- Swagger UI : http://localhost:8080/swagger-ui.html
- H2 UI : http://localhost:8080/h2-console

> Don't forget to set the `JDBC URL` value as `jdbc:h2:mem:testdb` for H2 UI.



### Instructions

- download the zip file of this project
- create a repository in your own github named 'java-challenge'
- clone your repository in a folder on your machine
- extract the zip file in this folder
- commit and push

- Enhance the code in any ways you can see, you are free! Some possibilities:
  - Add tests
  - Change syntax
  - Protect controller end points
  - Add caching logic for database calls
  - Improve doc and comments
  - Fix any bug you might find
- Edit readme.md and add any comments. It can be about what you did, what you would have done if you had more time, etc.
- Send us the link of your repository.

#### Restrictions
- use java 8


#### What we will look for
- Readability of your code
- Documentation
- Comments in your code 
- Appropriate usage of spring boot
- Appropriate usage of packages
- Is the application running as expected
- No performance issues

#### My experience in Java

I have 5 years experience in Java and I started learning Spring Boot this year.

#### Summary of my changes
- Updated lombok version to fix maven package issue
- Set constraint on the Employee name field so that it cannot be null
  My reasoning was that everyone should have a name, even if they may not yet have a department or salary
- Fixed updateEmployee() flow so that it sets the correct ID on the employee before saving it to the database
  Otherwise, it would create a new user instead of overriding the old one
- Updated getEmployee() in EmployeeServiceImpl so that it returns null if the employee doesn't exist
  This way, the controller can handle empty values by throwing custom exception
- Added RestExceptionHandler controller advice to return meaningful error messages with appropriate status codes for exceptions
- Replaced print statements with logs and added additional log statements
  This allows us to categorize and filter logs based on severity, and eventually send logs to external systems if desired
- Changed return value of api calls to be ResponseEntity objects, so that it is easier to set status codes and return messages
- Created ResponseMessage class to hold the String messages we want to return in api response
- Added tests for controller, repository, and service, and added smoke test for app
- Created separate config file to enable Swagger
  This helped resolve issues that arose when trying to test different slices of the application without loading the whole Spring context
- Added caching using Ehcache

#### What I would like to do with more time
- Add automated tests to verify caching functionality. For now, I tested the caching functionality manually
  by setting the logging level to TRACE and performing various operations through the Swagger UI and H2 UI
- Research and implement protection of REST endpoints. Spring Security seems to be a promising option to explore,
  as it allows us to quickly configure authentication and authorization while providing useful built-in functionality
- Since the user is not supposed to set the employee ID themselves when sending a POST or PUT operation through the Swagger UI
  (even if they do, it gets ignored!), I would like to prevent confusion by perhaps hiding that field for those operations
- Continue adding more test cases

Thank you for checking out my project!