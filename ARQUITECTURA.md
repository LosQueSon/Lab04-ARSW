# Diagrama de Arquitectura - Blueprints REST API

## Arquitectura General del Sistema

```
┌─────────────────────────────────────────────────────────────────────────┐
│                           CAPA DE CLIENTE                                │
│                                                                          │
│  ┌──────────────┐  ┌──────────────┐  ┌─────────────────────────────┐  │
│  │   Browser    │  │  Postman/    │  │   Aplicaciones Externas     │  │
│  │  (Swagger)   │  │   cURL       │  │   (REST Clients)            │  │
│  └──────┬───────┘  └──────┬───────┘  └─────────────┬───────────────┘  │
│         │                 │                         │                   │
└─────────┼─────────────────┼─────────────────────────┼──────────────────┘
          │                 │                         │
          └─────────────────┴─────────────────────────┘
                            │
                    HTTP/REST (JSON)
                            │
┌───────────────────────────▼─────────────────────────────────────────────┐
│                      SPRING BOOT APPLICATION                             │
│                    (Port: 8080 by default)                              │
│                                                                          │
│  ┌────────────────────────────────────────────────────────────────────┐ │
│  │                    PRESENTATION LAYER                              │ │
│  │  ┌──────────────────────────────────────────────────────────────┐ │ │
│  │  │         BlueprintsAPIController                              │ │ │
│  │  │  @RestController  @RequestMapping("/api/v1/blueprints")      │ │ │
│  │  │                                                               │ │ │
│  │  │  Endpoints:                                                   │ │ │
│  │  │  • GET    /api/v1/blueprints                                 │ │ │
│  │  │  • GET    /api/v1/blueprints/{author}                        │ │ │
│  │  │  • GET    /api/v1/blueprints/{author}/{bpname}               │ │ │
│  │  │  • POST   /api/v1/blueprints                                 │ │ │
│  │  │  • POST   /api/v1/blueprints/{author}/{bpname}/points        │ │ │
│  │  │  • PUT    /api/v1/blueprints/{author}/{bpname}               │ │ │
│  │  │  • DELETE /api/v1/blueprints/{author}/{bpname}               │ │ │
│  │  └──────────────────────┬───────────────────────────────────────┘ │ │
│  └─────────────────────────┼─────────────────────────────────────────┘ │
│                            │                                            │
│  ┌────────────────────────▼───────────────────────────────────────────┐ │
│  │                      BUSINESS LOGIC LAYER                          │ │
│  │  ┌──────────────────────────────────────────────────────────────┐ │ │
│  │  │            BlueprintsServices (@Service)                     │ │ │
│  │  │                                                               │ │ │
│  │  │  • addNewBlueprint(Blueprint)                                │ │ │
│  │  │  • getAllBlueprints() : Set<Blueprint>                       │ │ │
│  │  │  • getBlueprintsByAuthor(author) : Set<Blueprint>            │ │ │
│  │  │  • getBlueprint(author, name) : Blueprint                    │ │ │
│  │  │  • addPoint(author, name, x, y)                              │ │ │
│  │  └─────────┬────────────────────────────┬───────────────────────┘ │ │
│  └────────────┼────────────────────────────┼─────────────────────────┘ │
│               │                            │                            │
│               │                            │                            │
│  ┌────────────▼────────────────┐  ┌────────▼──────────────────────────┐ │
│  │    FILTER LAYER             │  │    PERSISTENCE LAYER              │ │
│  │  ┌─────────────────────┐    │  │  ┌──────────────────────────────┐│ │
│  │  │ BlueprintsFilter    │    │  │  │  BlueprintPersistence        ││ │
│  │  │   <<interface>>     │    │  │  │    <<interface>>             ││ │
│  │  └──────┬──────────────┘    │  │  └──────────┬───────────────────┘│ │
│  │         │                    │  │             │                    │ │
│  │  ┌──────┴──────────────┐    │  │  ┌──────────▼───────────────────┐│ │
│  │  │ Implementaciones:   │    │  │  │ PostgresBlueprintPersistence ││ │
│  │  │                     │    │  │  │      (@Repository)           ││ │
│  │  │ • RedundancyFilter  │    │  │  │                              ││ │
│  │  │   @Profile("red..")│    │  │  │  • saveBlueprint()           ││ │
│  │  │   Elimina puntos   │    │  │  │  • getBlueprint()            ││ │
│  │  │   consecutivos     │    │  │  │  • getBlueprintsByAuthor()   ││ │
│  │  │   duplicados       │    │  │  │  • getAllBlueprints()        ││ │
│  │  │                     │    │  │  │  • addPoint()                ││ │
│  │  │ • UndersamplingFlt  │    │  │  └──────────┬───────────────────┘│ │
│  │  │   @Profile("under")│    │  │             │                    │ │
│  │  │   Conserva 1 de    │    │  │   ┌─────────▼──────────────┐    │ │
│  │  │   cada 2 puntos    │    │  │   │   JdbcTemplate         │    │ │
│  │  │                     │    │  │   │ (Spring JDBC)          │    │ │
│  │  │ • IdentityFilter    │    │  │   └────────────────────────┘    │ │
│  │  │   Sin filtrado      │    │  │                                  │ │
│  │  └─────────────────────┘    │  └──────────────────────────────────┘ │
│  └─────────────────────────────┘                 │                     │
│                                                   │                     │
│  ┌────────────────────────────────────────────────────────────────────┐ │
│  │                         CONFIGURATION                              │ │
│  │  • OpenApiConfig (Swagger/OpenAPI 3.0)                             │ │
│  │  • application.properties (DataSource, Profiles)                   │ │
│  └────────────────────────────────────────────────────────────────────┘ │
│                                                                          │
└──────────────────────────────────┬───────────────────────────────────────┘
                                   │
                          JDBC Connection
                                   │
┌──────────────────────────────────▼───────────────────────────────────────┐
│                          DATABASE LAYER                                  │
│                                                                           │
│  ┌─────────────────────────────────────────────────────────────────┐    │
│  │                     PostgreSQL 16                                │    │
│  │                 (Docker Container)                               │    │
│  │                                                                   │    │
│  │  Tablas:                                                          │    │
│  │  ┌─────────────────────────────────────────────┐                 │    │
│  │  │  blueprints                                  │                 │    │
│  │  │  • author (VARCHAR) PK                       │                 │    │
│  │  │  • name (VARCHAR) PK                         │                 │    │
│  │  └─────────────────────────────────────────────┘                 │    │
│  │                                                                   │    │
│  │  ┌─────────────────────────────────────────────┐                 │    │
│  │  │  blueprint_points                            │                 │    │
│  │  │  • author (VARCHAR) FK                       │                 │    │
│  │  │  • name (VARCHAR) FK                         │                 │    │
│  │  │  • idx (INTEGER) PK                          │                 │    │
│  │  │  • x (INTEGER)                               │                 │    │
│  │  │  • y (INTEGER)                               │                 │    │
│  │  └─────────────────────────────────────────────┘                 │    │
│  │                                                                   │    │
│  │  Puerto: 5432                                                     │    │
│  │  Volume: blueprints_pgdata                                        │    │
│  └─────────────────────────────────────────────────────────────────┘    │
│                                                                           │
└───────────────────────────────────────────────────────────────────────────┘
```

---

## Modelo de Dominio

```
┌─────────────────────────────────────────────────────────────────┐
│                         Blueprint                                │
│─────────────────────────────────────────────────────────────────│
│ - author: String                                                 │
│ - name: String                                                   │
│ - points: List<Point>                                            │
│─────────────────────────────────────────────────────────────────│
│ + getAuthor(): String                                            │
│ + getName(): String                                              │
│ + getPoints(): List<Point>                                       │
└────────────────────────┬────────────────────────────────────────┘
                         │ 1
                         │
                         │ *
                         │
                    ┌────▼─────────────────────────────────┐
                    │          Point                       │
                    │──────────────────────────────────────│
                    │ - x: int                             │
                    │ - y: int                             │
                    │──────────────────────────────────────│
                    │ + x(): int                           │
                    │ + y(): int                           │
                    └──────────────────────────────────────┘
```

---

## Flujo de Datos - GET Blueprint

```
Cliente                Controller           Service          Filter           Persistence        DB
  │                        │                   │                │                 │             │
  │  GET /api/v1/         │                   │                │                 │             │
  │  blueprints/{author}/│                   │                │                 │             │
  │  {name}               │                   │                │                 │             │
  ├───────────────────────►                   │                │                 │             │
  │                        │                   │                │                 │             │
  │                        │ getBlueprint()    │                │                 │             │
  │                        ├──────────────────►│                │                 │             │
  │                        │                   │                │                 │             │
  │                        │                   │ getBlueprint() │                 │             │
  │                        │                   ├────────────────┼────────────────►│             │
  │                        │                   │                │                 │             │
  │                        │                   │                │                 │ SELECT ...  │
  │                        │                   │                │                 ├────────────►│
  │                        │                   │                │                 │             │
  │                        │                   │                │                 │ Rows        │
  │                        │                   │                │                 ◄─────────────┤
  │                        │                   │                │                 │             │
  │                        │                   │                │   Blueprint     │             │
  │                        │                   │◄───────────────┼─────────────────┤             │
  │                        │                   │                │                 │             │
  │                        │                   │ apply(bp)      │                 │             │
  │                        │                   ├───────────────►│                 │             │
  │                        │                   │                │                 │             │
  │                        │                   │  Filtered BP   │                 │             │
  │                        │                   │◄───────────────┤                 │             │
  │                        │                   │                │                 │             │
  │                        │  Blueprint        │                │                 │             │
  │                        │◄──────────────────┤                │                 │             │
  │                        │                   │                │                 │             │
  │  200 OK                │                   │                │                 │             │
  │  ApiResponse<Blueprint>│                   │                │                 │             │
  │◄───────────────────────┤                   │                │                 │             │
  │                        │                   │                │                 │             │
```

---

## Patrón de Inyección de Dependencias

```
                    ┌─────────────────────────────┐
                    │  Spring IoC Container       │
                    │                              │
                    │  • Component Scanning        │
                    │  • Autowiring                │
                    │  • Profile Management        │
                    └──┬──────────────┬────────────┘
                       │              │
         ┌─────────────┘              └──────────────┐
         │                                           │
         ▼                                           ▼
┌────────────────────┐                    ┌──────────────────────┐
│  @RestController   │                    │    @Service          │
│                    │                    │                      │
│ BlueprintsAPI      │                    │ BlueprintsServices   │
│ Controller         │◄───depends on─────┤                      │
└────────────────────┘                    └───┬──────────────┬───┘
                                              │              │
                                   depends on │              │ depends on
                                              │              │
                              ┌───────────────▼──┐    ┌──────▼──────────────┐
                              │  @Repository     │    │  @Component         │
                              │                  │    │  @Profile(...)      │
                              │ PostgresBP...    │    │                     │
                              │ Persistence      │    │ BlueprintsFilter    │
                              └──────────────────┘    │ (RedundancyFilter,  │
                                                      │  UndersamplingFlt,  │
                                                      │  IdentityFilter)    │
                                                      └─────────────────────┘
```

---

## Configuración de Perfiles (Profiles)

```
application.properties:
spring.profiles.active=redundancy, undersampling

┌──────────────────────────────────────────────────────────────────┐
│                    Profile Configuration                          │
├──────────────────────────────────────────────────────────────────┤
│                                                                   │
│  Profile: "redundancy"                                            │
│  ┌────────────────────────────────────────────────────────────┐  │
│  │  @Component @Profile("redundancy")                         │  │
│  │  RedundancyFilter                                          │  │
│  │  • Elimina puntos consecutivos con mismas coordenadas     │  │
│  │  • Ejemplo: [(0,0), (0,0), (1,1)] → [(0,0), (1,1)]       │  │
│  └────────────────────────────────────────────────────────────┘  │
│                                                                   │
│  Profile: "undersampling"                                         │
│  ┌────────────────────────────────────────────────────────────┐  │
│  │  @Component @Profile("undersampling")                      │  │
│  │  UndersamplingFilter                                       │  │
│  │  • Conserva solo puntos en índices pares (0, 2, 4, ...)   │  │
│  │  • Reduce densidad de puntos a la mitad                   │  │
│  └────────────────────────────────────────────────────────────┘  │
│                                                                   │
│  Profile: "identity" (default si no hay perfil activo)           │
│  ┌────────────────────────────────────────────────────────────┐  │
│  │  @Component @Profile("identity")                           │  │
│  │  IdentityFilter                                            │  │
│  │  • No aplica ningún filtrado                              │  │
│  │  • Retorna el blueprint original sin cambios              │  │
│  └────────────────────────────────────────────────────────────┘  │
│                                                                   │
│  NOTA: Múltiples perfiles activos se aplican en cascada         │
│        si hay múltiples implementaciones de BlueprintsFilter     │
└──────────────────────────────────────────────────────────────────┘
```

---

## Tecnologías y Frameworks

| Capa | Tecnología | Versión | Propósito |
|------|-----------|---------|-----------|
| **Backend Framework** | Spring Boot | 3.3.9 | Framework principal |
| **Language** | Java | 21 | Lenguaje de programación |
| **Web Layer** | Spring Web MVC | 3.3.9 | REST API |
| **Database** | PostgreSQL | 16 | Base de datos relacional |
| **Data Access** | Spring JDBC | 3.3.9 | Acceso a base de datos |
| **Documentation** | SpringDoc OpenAPI | 2.6.0 | Documentación API (Swagger) |
| **Containerization** | Docker Compose | - | Orquestación de contenedores |
| **Build Tool** | Maven | 3.x | Gestión de dependencias y build |
| **Testing** | Spring Boot Test | 3.3.9 | Testing framework |

---

## Patrones de Diseño Utilizados

### 1. **Dependency Injection (DI)**
   - Spring gestiona todas las dependencias entre componentes
   - Constructor-based injection en todos los componentes

### 2. **Repository Pattern**
   - `PostgresBlueprintPersistence` encapsula lógica de acceso a datos
   - Abstracción con interfaz `BlueprintPersistence`

### 3. **Strategy Pattern**
   - Interfaz `BlueprintsFilter` con múltiples implementaciones
   - Permite cambiar algoritmos de filtrado dinámicamente vía profiles

### 4. **Service Layer Pattern**
   - `BlueprintsServices` centraliza lógica de negocio
   - Desacopla controllers de persistencia

### 5. **DTO Pattern**
   - `ApiResponse<T>` para respuestas consistentes
   - `NewBlueprintRequest` para validación de entrada

### 6. **RESTful API Design**
   - Recursos orientados a REST
   - Verbos HTTP semánticos (GET, POST, PUT, DELETE)
   - Status codes apropiados

---

## Endpoints de la API

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/v1/blueprints` | Obtener todos los blueprints |
| GET | `/api/v1/blueprints/{author}` | Obtener blueprints por autor |
| GET | `/api/v1/blueprints/{author}/{name}` | Obtener blueprint específico (con filtrado) |
| POST | `/api/v1/blueprints` | Crear nuevo blueprint |
| POST | `/api/v1/blueprints/{author}/{name}/points` | Agregar punto a blueprint |
| PUT | `/api/v1/blueprints/{author}/{name}` | Actualizar blueprint |
| DELETE | `/api/v1/blueprints/{author}/{name}` | Eliminar blueprint |

**Documentación interactiva:** `http://localhost:8080/swagger-ui.html`

---

## Variables de Configuración

```properties
# Pathing Strategy (para Swagger)
spring.mvc.pathmatch.matching-strategy=ant_path_matcher

# Perfiles Activos (Filters)
spring.profiles.active=redundancy, undersampling

# Configuración de DataSource
spring.datasource.url=jdbc:postgresql://localhost:5432/blueprints
spring.datasource.username=blueprints_user
spring.datasource.password=blueprints_pass
spring.datasource.driver-class-name=org.postgresql.Driver

# Inicialización de Base de Datos
spring.sql.init.mode=always

# Documentación OpenAPI
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.api-docs.path=/v3/api-docs
```

---

## Deployment con Docker Compose

```
┌─────────────────────────────────────────────────────┐
│             Docker Compose Network                   │
│                                                      │
│  ┌────────────────────┐      ┌──────────────────┐  │
│  │ Spring Boot App    │      │   PostgreSQL 16  │  │
│  │                    │      │                  │  │
│  │ Port: 8080         ├──────► Port: 5432       │  │
│  │                    │ JDBC │                  │  │
│  │ Depends on:        │      │ Volume:          │  │
│  │ - postgres         │      │ blueprints_pgdata│  │
│  └────────────────────┘      └──────────────────┘  │
│                                                      │
└─────────────────────────────────────────────────────┘
```

### Comandos:
```bash
# Iniciar servicios
docker-compose up -d

# Ver logs
docker-compose logs -f

# Detener servicios
docker-compose down

# Detener y eliminar volúmenes
docker-compose down -v
```

---

## Manejo de Errores

```
Exception Hierarchy:

BlueprintPersistenceException
    └─ Cuando falla persistencia (e.g., blueprint duplicado)
    └─ HTTP 500 Internal Server Error

BlueprintNotFoundException
    └─ Cuando no se encuentra blueprint/autor
    └─ HTTP 404 Not Found

Validation Errors
    └─ @Valid en DTOs
    └─ HTTP 400 Bad Request
```

---

## Respuesta API Estándar

```json
{
  "status": 200,
  "message": "execute ok",
  "data": {
    "author": "john_doe",
    "name": "house_blueprint",
    "points": [
      {"x": 0, "y": 0},
      {"x": 10, "y": 10},
      {"x": 20, "y": 5}
    ]
  }
}
```

---

## Seguridad de Datos

- **Transacciones:** Métodos anotados con `@Transactional`
- **Validación:** DTOs validados con Bean Validation (`@Valid`)
- **SQL Injection:** Protegido mediante PreparedStatements (JdbcTemplate)
- **Excepciones:** Manejo centralizado con respuestas consistentes

---

*Generado para Lab04-ARSW - Spring Boot REST API Blueprints*

