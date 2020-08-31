# Import Sales Utility

### Installation
1. Execute Maven goals:
`mvn clean install`
2. Build Docker image: 
`mvn -DskipTests=true spring-boot:build-image -f pom.xml`
3. Start Docker app stack:
`docker-compose -f .\src\main\resources\docker\stack.yml`

### How to run
The application listens new *.dat files created on %HOMEPATH% "in" directory. The utility app performs the importing process and produces a report on %HOMEPATH% "data\\out" directory. 
