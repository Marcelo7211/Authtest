# Etapa de Build
FROM openjdk:17.0.1-jdk-oracle as build
WORKDIR /workspace/app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN chmod +x mvnw
RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Etapa de Execução
FROM openjdk:17.0.1-jdk-oracle
WORKDIR /app
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Ajuste do ENTRYPOINT
ENTRYPOINT ["java","-cp","/app:/app/lib/*","com.ead.authuser.AuthuserApplication"]
