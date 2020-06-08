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
curl samples and HTTP requests are at the **/MenuVotingSystem/config/
- DishCurl.http
- RestaurantCurl.http
- UserCurl.http
- VoteCurl.http

-  ### DataBase HSQLDB(inMemory)
-  ### WebContainer Tomcat
-  ### LOGGING 
logging configured to output to the console. To save the logs to a file, uncomment the code in the file logback.xml and indicate your path to the yourLogFile.log
-  ###Sequence Strategies: Sequence objects @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq") initialValue = 100_000

-  ### Entity: User, Restaurant, Dish, Vote
- User, Restaurant, Dish used Hibernate Second Level Cache Strategy: nonstrict read/write (the application only occasionally needs to update data (i.e. if it is extremely unlikely that two transactions would try to update the same item simultaneously, and strict transaction isolation is not required)
- Vote used Hibernate Second Level Cache Strategy: read only (upgrade implemented how delete old and insert new, i.e. application needs to read, but not modify, instances of a persistent class. This is the simplest and optimal performing strategy. ) 
#### User
 * has two roles (ROLE_ADMIN, ROLE_USER) with different access levels
 * has unique email and unique pair id-role 
 * has a one-to-many relationship with votes (fetch = FetchType.LAZY)
 * user can have two roles at the same time
 * user with ROLE_ADMIN:
     - does not have access to vote control (if has only ROLE_ADMIN).
     - have full CRUD control users, restaurants and dishes
 * user with ROLE_USER:
      - has read-only access to dishes of restaurant.
      - has read-only access to restaurant transfer object (restaurantTO - see below).
      - user has full access to his votes, but, according to task, cannot have more than one vote per day and cannot change his voice after timeBorder 11-00 of the current day (the timeBorder is set in the file **\MenuVotingSystem\src\main\java\ru\cherniak\menuvotingsystem\util\DateTimeUtil.java). For GET requests used vote transfer object (VoteTo - see below)
      - has full CRUD control of UserTo (user transfer object - see below) fields (cannot set and change his date registration and ROLE)
 * UserTo - transfer object using for read and write operations only with USER_ROLE, has equal fields with Entity:
      - "id", "name", "email", "password".     
 * UserService:
      - GET List methods sorted by name, then by email.
      - getAll method has @Cacheable("users") - the most frequent requests and rarely changed data 
 * UserControllers:
      - ROLE_ADMIN : rootpath REST url http://localhost:8080/mvs/rest/admin/users
      - ROLE_USER : rootpath REST url http://localhost:8080/mvs/rest/profile     
   
 #### Restaurant
  * has unique field: name 
  * has a one-to-many relationship with dishes (fetch = FetchType.LAZY)
  * RestaurantTo - transfer object using read only for users with USER_ROLE :
       - has equal fields with Entity: "id", "name", "type", "address", "phone", "url"
       - has an additional field: "countOfVotes" - total number of votes for the entire period from the moment of registration in the database
  * RestaurantService:
       - GET List methods sorted by name
       - getAll method has @Cacheable("restaurants") - the most frequent requests and rarely changed data
  * RestaurantControllers:
       - ROLE_ADMIN : rootpath REST url http://localhost:8080/mvs/rest/admin/restaurants
       - ROLE_USER : rootpath REST url http://localhost:8080/mvs/rest/user/restaurants            
 #### Dish
  * has unique pair date_name by one restaurant 
  * has a many-to-one relationship with restaurant (fetch = FetchType.LAZY)
  * DishService:
       - GET List methods sorted by "date"(desc), then "restaurant.id"(asc), then "name" (asc)
       - no @Cacheable - the most frequent changed data
  * DishControllers:
       - ROLE_ADMIN : rootpath REST url http://localhost:8080/mvs//rest/admin/dishes
       - ROLE_USER : rootpath REST url http://localhost:8080/mvs/rest/user/dishes
#### Vote
  * DB has unique pair date_userId 
  * has a many-to-one relationship with restaurant (fetch = FetchType.LAZY) and user (fetch = FetchType.LAZY)
  * VoteTo - transfer object using read only for users with USER_ROLE :
       - has equal fields with Entity: "id", "date"
       - has an additional field: ""name" = restaurantName with the restaurantId from the Entity
  * VoteService:
       - GET List methods sorted by "date"(desc)
       - no @Cacheable - the most frequent changed data
  * VoteControllers:
       - ROLE_ADMIN : not exist REST url
       - ROLE_USER : rootpath REST url http://localhost:8080/mvs/rest/user/votes


