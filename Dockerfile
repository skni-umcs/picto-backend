FROM amazoncorretto:20 as development
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN ./mvnw dependency:go-offline

COPY src ./src

#COPY --from=builder /app/target/*.jar ./app.jar
CMD ["./mvnw", "spring-boot:run"]

FROM amazoncorretto:20 as builder
WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./

RUN ./mvnw dependency:go-offline

COPY src ./src

RUN ./mvnw package -DskipTests

FROM amazoncorretto:20 as production

WORKDIR /app

COPY --from=builder /app/target/*.jar ./app.jar

CMD ["java", "-jar", "app.jar"]
