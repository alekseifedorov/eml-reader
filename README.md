1. This is a cli application based on the following stack:
   - JDK 11
   - Spring Boot 
   - javax.mail
   - java.util.zip
   
2. How to build

    Type 'gradlew clean build' to build eml-reader-1.0.jar.

    The server port is specified in application.properties (server.port=8080)

3. How to run

    Type either of the following commands:
      -  gradlew clean build && java -jar build/libs/eml-reader-1.0.jar filename [-t zip|eml],
            e.g. java -jar build/libs/eml-reader-1.0.jar archive.zip -t zip 
                  or
                 java -jar build/libs/eml-reader-1.0.jar "Email 2.eml" -t eml 
                  or
                 java -jar build/libs/eml-reader-1.0.jar archive.zip 
      -  gradlew bootRun

