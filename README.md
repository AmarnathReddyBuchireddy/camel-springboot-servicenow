# Camel Spring Boot ServiceNow

To build this project use

```
$ mvn install
```

To run the project you can execute the following Maven goal

```
$ mvn spring-boot:run
```

You will also need an instance of ServiceNow. Create a developer instance and replace the appropriate values in `src/main/resources/application.yml`.

To test

```
$ curl -X POST -H 'Content-Type: application/json' -d '{ "level": 4, "description": "I need more cowbell!" }' 'http://localhost:8080/camel/incidents/'
```