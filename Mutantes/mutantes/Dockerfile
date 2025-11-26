# Etapa de construcción
FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

COPY . .

# Dar permisos de ejecución al instalador de gradle
RUN chmod +x gradlew
# Crear el ejecutable (saltando los tests para ir rápido)
RUN ./gradlew bootJar -x test

# Etapa de ejecución (La imagen final)
FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
# Copiar el jar desde la etapa anterior
COPY --from=build /workspace/app/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]