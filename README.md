# 🧬 Mutant Detector API

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3-green)
![Status](https://img.shields.io/badge/Status-Terminado-success)

¡Bienvenido al sistema de reclutamiento de Magneto!
Este proyecto es una **API REST** diseñada para detectar si un humano es un mutante basándose en su secuencia de ADN.

El programa analiza una matriz de `NxN` buscando secuencias de **4 letras iguales** (A, T, C, G) en direcciones horizontal, vertical u oblicua.

---

## 🚀 Tecnologías Usadas

El proyecto fue construido siguiendo una arquitectura profesional en capas:

* **Java 17**: Lenguaje principal.
* **Spring Boot 3**: Framework para la API REST.
* **H2 Database**: Base de datos en memoria (SQL) para guardar los análisis.
* **JPA / Hibernate**: Para la comunicación con la base de datos.
* **Lombok**: Para reducir el código repetitivo.
* **Gradle**: Gestor de construcción y dependencias.
* **JUnit 5**: Pruebas unitarias.
* **Swagger (OpenAPI)**: Documentación automática.

---

## 🛠️ Instrucciones de Ejecución

Sigue estos pasos para correr el proyecto en tu computadora:

1.  **Clonar el repositorio** (o descargar el código):
    ```bash
    git clone <TU_LINK_DEL_REPO>
    cd Mutantes
    ```

2.  **Ejecutar el proyecto**:
    * **En Windows:**
        ```powershell
        .\gradlew bootRun
        ```
    * **En Mac/Linux:**
        ```bash
        ./gradlew bootRun
        ```

3.  **Confirmación**:
    Verás un mensaje en la consola indicando que el servidor inició en el puerto 8080.
    > `Tomcat started on port 8080`

---

## 📡 Cómo Usar la API

Una vez que el servidor esté corriendo, puedes probarlo de las siguientes formas:

### 📄 Documentación Automática (Swagger UI)
Puedes probar los botones directamente desde tu navegador entrando a:
👉 [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### 1. Detectar Mutante (POST)
Envía una secuencia de ADN para analizar.

* **URL:** `http://localhost:8080/mutant`
* **Método:** `POST`
* **Body (JSON):**

```json
{
    "dna": [
        "ATGCGA",
        "CAGTGC",
        "TTATGT",
        "AGAAGG",
        "CCCCTA",
        "TCACTG"
    ]
}
```

* **Respuestas Posibles:**
    * 🟢 **200 OK**: Es un **Mutante**.
    * 🔴 **403 Forbidden**: Es un **Humano**.
    * 🟠 **400 Bad Request**: ADN inválido.

### 2. Ver Estadísticas (GET)
Consulta las estadísticas de las verificaciones de ADN.

* **URL:** `http://localhost:8080/stats`
* **Método:** `GET`
* **Respuesta Esperada:**

```json
{
    "count_mutant_dna": 40,
    "count_human_dna": 100,
    "ratio": 0.4
}
```

---

## 🏛️ Arquitectura y Diagramas

El sistema utiliza un **Controlador** para recibir peticiones, un **Servicio** para la lógica de negocio y validaciones, y un **Repositorio** para guardar los resultados (evitando re-analizar ADNs ya procesados).

### Diagrama de Secuencia: Análisis de Mutante (POST)

```mermaid
sequenceDiagram
    autonumber
    actor Cliente
    participant Controller
    participant Service
    participant Detector
    participant Repo
    participant DB

    Cliente->>Controller: POST /mutant (ADN)
    activate Controller
    
    Controller->>Service: analyze(dna)
    activate Service
    
    Service->>Service: Calcular Hash del ADN
    
    Service->>Repo: findByDnaHash(hash)
    activate Repo
    Repo-->>Service: null (No existe)
    deactivate Repo
    
    Note right of Service: Si es nuevo, analizamos
    
    Service->>Detector: isMutant(dna)
    activate Detector
    Detector-->>Service: true (Es Mutante)
    deactivate Detector
    
    Service->>Repo: save(resultado)
    activate Repo
    Repo->>DB: INSERT INTO dna_records
    Repo-->>Service: Guardado
    deactivate Repo
    
    Service-->>Controller: true
    deactivate Service
    
    Controller-->>Cliente: 200 OK
    deactivate Controller
```

### Diagrama de Secuencia: Estadísticas (GET)

```mermaid
sequenceDiagram
    autonumber
    actor Cliente
    participant Controller
    participant Service
    participant Repo
    participant DB

    Cliente->>Controller: GET /stats
    activate Controller
    Controller->>Service: getStats()
    activate Service
    
    Service->>Repo: countByIsMutant(true)
    activate Repo
    Repo->>DB: SELECT COUNT...
    DB-->>Service: Cantidad Mutantes
    deactivate Repo
    
    Service->>Repo: countByIsMutant(false)
    activate Repo
    Repo->>DB: SELECT COUNT...
    DB-->>Service: Cantidad Humanos
    deactivate Repo
    
    Service->>Service: Calcular Ratio
    
    Service-->>Controller: StatsResponse (JSON)
    deactivate Service
    Controller-->>Cliente: 200 OK
    deactivate Controller
```

---

## 🧪 Testing

El proyecto incluye tests unitarios con **JUnit 5** y **Mockito**.
Para ejecutarlos desde la terminal:

```bash
./gradlew test
```

---
Hecho por **Moyano Elena** - 2024
