## Graduation project

#### REST API implementation using Hibernate / Spring / SpringMVC without an external interface, with authorization and role-based access rights using the most popular Java tools and technologies: Maven, Spring MVC, Security, JPA (Hibernate), REST (Jackson), JUnit5, Java 8 Stream and Time API and saving to inMemory HSQLDB database.

The task is:

Build a voting system for deciding where to have lunch.

 * 2 types of users: admin and regular users
 * Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
 * Menu changes each day (admins do the updates)
 * Users can vote on which restaurant they want to have lunch at
 * Only one vote counted per user
 * If user votes again the same day:
    - If it is before 11:00 we asume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides new menu each day.

As a result, provide a link to github repository. It should contain the code, README.md with API documentation and couple curl commands to test it.

### ![error](https://cloud.githubusercontent.com/assets/13649199/13672935/ef09ec1e-e6e7-11e5-9f79-d1641c05cbe6.png) Implementation Features

-  ### application deployed in application context `mvs` (rootpath: http://localhost:8080/mvs). 
curl samples and HTTP requests are at the ./config/

#### [DishCurl](./config/DishCurl.http)
#### [RestaurantCurl](./config/RestaurantCurl.http)
#### [UserCurl](./config/UserCurl.http)
#### [VoteCurl](./config/VoteCurl.http)


-  ### DataBase HSQLDB(inMemory)
-  ### WebContainer Tomcat
-  ### LOGGING 
    logging configured to output to the console and file in the ./src/main/resources/logback.xml
-  ####Sequence Strategies: Sequence objects @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq") initialValue = 100_000

-  ### Entity: User, Restaurant, Dish, Vote
- User, Restaurant used Hibernate Second Level Cache Strategy: nonstrict read/write (the application only occasionally needs to update data (i.e. if it is extremely unlikely that two transactions would try to update the same item simultaneously, and strict transaction isolation is not required)
- Vote and Dish do not use used Hibernate Second Level Cache , i.e. ones can modify often. 
#### User
 * has two roles (ROLE_ADMIN, ROLE_USER) with different access levels
 * user can have two roles at the same time
 * user with only ROLE_ADMIN:
     - does not have access to vote control others users
     - have full CRUD control users, restaurants and dishes
 * user with ROLE_USER:
      - has read-only access to dishes and restaurants.
      - user has full access to his votes, but, according to task, cannot have more than one vote per day and cannot change his voice after timeBorder 11-00 of the current day (the timeBorder is set in the file **\MenuVotingSystem\src\main\java\ru\cherniak\menuvotingsystem\util\DateTimeUtil.java). For GET requests used vote transfer object (VoteTo - see below)
      - has full CRUD control of UserTo (user transfer object - see below) fields (cannot set and change his date registration and ROLE)
 * UserTo - transfer object using for read and write operations only with USER_ROLE, has equal fields with Entity:
      - "id", "name", "email", "password".     
 

