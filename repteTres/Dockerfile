# ==================================
# 1. ETAPA DE CONSTRUCCIÓN (BUILD STAGE)
# Utiliza un JDK (Java Development Kit) para compilar la aplicación.
# Usamos Eclipse Temurin con Java 21 para asegurar la compatibilidad.
# ==================================
FROM eclipse-temurin:21-jdk-jammy AS build

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia los archivos de configuración de Maven/Gradle y el código fuente
# Esto aprovecha el caching de Docker: si el pom.xml/build.gradle no cambian, no vuelve a descargar dependencias.
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline -B

# Copia el código fuente restante
COPY src ./src

# Compila y empaqueta la aplicación en un JAR
# El JAR final estará en /app/target/
RUN ./mvnw clean package -DskipTests

# ==================================
# 2. ETAPA DE EJECUCIÓN (RUNTIME STAGE)
# Utiliza un JRE (Java Runtime Environment) ligero y optimizado para ejecutar la aplicación.
# ==================================
FROM eclipse-temurin:21-jre-jammy

# Expone el puerto por defecto de Spring Boot (8080).
# Render automáticamente remapea el tráfico externo al puerto que tu app usa internamente.
EXPOSE 8080

# Establece el directorio de trabajo
WORKDIR /app

# Copia el JAR compilado desde la etapa de 'build'
COPY --from=build /app/target/*.jar app.jar

# Comando para ejecutar la aplicación Spring Boot.
# Usa $PORT para que Spring Boot tome el puerto que Render le asigne.
ENTRYPOINT ["java", "-jar", "app.jar"]

# Si necesitas argumentos de inicio adicionales (como opciones de memoria),
# puedes usar CMD o añadir variables de entorno en Render.
# Por ejemplo: ENTRYPOINT ["java", "-Xmx256m", "-jar", "app.jar"]