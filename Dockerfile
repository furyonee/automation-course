FROM maven:3.9.6-eclipse-temurin-17

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src ./src

RUN mvn exec:java \
-Dexec.mainClass="com.microsoft.playwright.CLI" \
-Dexec.args="install chromium"

CMD ["mvn", "test", "-Dtest=StatusCodeCombinedTest", "-Denv=dev"]