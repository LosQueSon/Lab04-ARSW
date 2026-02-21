# Modelo ArchiMate 3.2 - Blueprints REST API

## Guía para Modelado en ArchiMate 3.2

Este documento proporciona la estructura y elementos necesarios para crear el modelo arquitectónico del sistema Blueprints REST API usando ArchiMate 3.2.

---

## 1. Capas de ArchiMate a Utilizar

### **Application Layer** (Capa de Aplicación)
- Componentes principales del sistema
- Servicios expuestos
- Interfaces de aplicación

### **Technology Layer** (Capa de Tecnología)
- Infraestructura (Docker, PostgreSQL)
- Plataformas (Spring Boot, Java)
- Redes y comunicaciones

---

## 2. Elementos ArchiMate por Capa

### 📱 **CAPA DE APLICACIÓN (Application Layer)**

#### **Application Components** (Componentes de Aplicación)

| ID | Elemento | Tipo | Nombre | Descripción |
|----|----------|------|--------|-------------|
| AC-01 | Application Component | Component | **Blueprints API Controller** | Controlador REST que expone los endpoints HTTP |
| AC-02 | Application Component | Component | **Blueprints Services** | Servicio de lógica de negocio |
| AC-03 | Application Component | Component | **Blueprints Filter** | Componente de filtrado de puntos |
| AC-04 | Application Component | Component | **Blueprints Persistence** | Componente de acceso a datos |
| AC-05 | Application Component | Component | **OpenAPI Documentation** | Documentación interactiva Swagger |

#### **Application Services** (Servicios de Aplicación)

| ID | Elemento | Tipo | Nombre | Descripción |
|----|----------|------|--------|-------------|
| AS-01 | Application Service | Service | **GET All Blueprints** | Obtener todos los blueprints |
| AS-02 | Application Service | Service | **GET Blueprints by Author** | Obtener blueprints por autor |
| AS-03 | Application Service | Service | **GET Blueprint** | Obtener blueprint específico |
| AS-04 | Application Service | Service | **POST Blueprint** | Crear nuevo blueprint |
| AS-05 | Application Service | Service | **PUT Blueprint** | Actualizar blueprint |
| AS-06 | Application Service | Service | **DELETE Blueprint** | Eliminar blueprint |
| AS-07 | Application Service | Service | **POST Add Point** | Agregar punto a blueprint |

#### **Application Interfaces** (Interfaces de Aplicación)

| ID | Elemento | Tipo | Nombre | Descripción |
|----|----------|------|--------|-------------|
| AI-01 | Application Interface | Interface | **REST API v1** | Interfaz REST JSON HTTP |
| AI-02 | Application Interface | Interface | **Swagger UI** | Interfaz web de documentación |

#### **Data Objects** (Objetos de Datos)

| ID | Elemento | Tipo | Nombre | Descripción |
|----|----------|------|--------|-------------|
| DO-01 | Data Object | Object | **Blueprint** | Entidad Blueprint (author, name, points) |
| DO-02 | Data Object | Object | **Point** | Entidad Point (x, y) |
| DO-03 | Data Object | Object | **ApiResponse** | DTO de respuesta estándar |

---

### 🖥️ **CAPA DE TECNOLOGÍA (Technology Layer)**

#### **Technology Services** (Servicios de Tecnología)

| ID | Elemento | Tipo | Nombre | Descripción |
|----|----------|------|--------|-------------|
| TS-01 | Technology Service | Service | **HTTP Server** | Servidor HTTP/HTTPS |
| TS-02 | Technology Service | Service | **Database Service** | Servicio de base de datos PostgreSQL |
| TS-03 | Technology Service | Service | **JDBC Connection Pool** | Pool de conexiones JDBC |

#### **System Software** (Software de Sistema)

| ID | Elemento | Tipo | Nombre | Versión |
|----|----------|------|--------|---------|
| SS-01 | System Software | Software | **Spring Boot** | 3.3.9 |
| SS-02 | System Software | Software | **Java Runtime** | 21 |
| SS-03 | System Software | Software | **PostgreSQL** | 16 |
| SS-04 | System Software | Software | **Docker Engine** | Latest |
| SS-05 | System Software | Software | **Maven** | 3.x |

#### **Technology Collaboration** (Colaboración Tecnológica)

| ID | Elemento | Tipo | Nombre | Protocolo |
|----|----------|------|--------|-----------|
| TC-01 | Technology Collaboration | Collaboration | **REST Communication** | HTTP/HTTPS |
| TC-02 | Technology Collaboration | Collaboration | **JDBC Communication** | JDBC |

#### **Artifacts** (Artefactos)

| ID | Elemento | Tipo | Nombre | Descripción |
|----|----------|------|--------|-------------|
| AR-01 | Artifact | Artifact | **blueprints.jar** | JAR ejecutable Spring Boot |
| AR-02 | Artifact | Artifact | **PostgreSQL Container** | Contenedor Docker de BD |
| AR-03 | Artifact | Artifact | **application.properties** | Archivo de configuración |
| AR-04 | Artifact | Artifact | **docker-compose.yml** | Orquestación de contenedores |

#### **Nodes** (Nodos)

| ID | Elemento | Tipo | Nombre | Descripción |
|----|----------|------|--------|-------------|
| ND-01 | Node | Device/Container | **Application Container** | Contenedor Spring Boot |
| ND-02 | Node | Device/Container | **Database Container** | Contenedor PostgreSQL |

---

## 3. Relaciones ArchiMate

### **Relaciones de Composición (Composition)**
```
Spring Boot Application
├── Blueprints API Controller (AC-01)
├── Blueprints Services (AC-02)
├── Blueprints Filter (AC-03)
├── Blueprints Persistence (AC-04)
└── OpenAPI Documentation (AC-05)
```

### **Relaciones de Servicio (Serving)**
```
AC-01 (Controller) ──serves──> AS-01, AS-02, AS-03, AS-04, AS-05, AS-06, AS-07
AC-02 (Services) ──serves──> AC-01
AC-03 (Filter) ──serves──> AC-02
AC-04 (Persistence) ──serves──> AC-02
SS-03 (PostgreSQL) ──serves──> AC-04
```

### **Relaciones de Realización (Realization)**
```
AC-01 ──realizes──> AI-01 (REST API v1)
AC-05 ──realizes──> AI-02 (Swagger UI)
AR-01 ──realizes──> AC-01, AC-02, AC-03, AC-04
SS-01 ──realizes──> TS-01 (HTTP Server)
SS-03 ──realizes──> TS-02 (Database Service)
```

### **Relaciones de Asignación (Assignment)**
```
AR-01 (blueprints.jar) ──assigned to──> ND-01 (Application Container)
AR-02 (PostgreSQL Container) ──assigned to──> ND-02 (Database Container)
```

### **Relaciones de Acceso (Access)**
```
AC-04 (Persistence) ──access (read/write)──> DO-01 (Blueprint)
AC-04 (Persistence) ──access (read/write)──> DO-02 (Point)
AC-01 (Controller) ──access (read/write)──> DO-03 (ApiResponse)
```

### **Relaciones de Flujo (Flow)**
```
AI-01 (REST API) ──flow──> AC-01 (Controller)
AC-01 ──flow──> AC-02 (Services)
AC-02 ──flow──> AC-03 (Filter)
AC-02 ──flow──> AC-04 (Persistence)
AC-04 ──flow (via JDBC)──> SS-03 (PostgreSQL)
```

---

## 4. Vistas ArchiMate Recomendadas

### **Vista 1: Application Structure View** (Vista de Estructura de Aplicación)

**Propósito:** Mostrar la estructura interna de la aplicación Spring Boot

**Elementos a incluir:**
- Application Components: AC-01, AC-02, AC-03, AC-04, AC-05
- Application Interfaces: AI-01, AI-02
- Data Objects: DO-01, DO-02, DO-03
- Relaciones: Composition, Serving, Access

**Representación:**
```
┌─────────────────────────────────────────────────────────────┐
│           Spring Boot Application (Grouping)                │
│                                                              │
│  ┌────────────────┐                                         │
│  │ REST API v1    │ (AI-01)                                │
│  │  <<interface>> │                                         │
│  └───────┬────────┘                                         │
│          │ realizes                                         │
│  ┌───────▼──────────────┐                                   │
│  │ BlueprintsAPI        │ (AC-01)                          │
│  │ Controller           │                                   │
│  └──────┬───────────────┘                                   │
│         │ uses                                              │
│  ┌──────▼───────────────┐      ┌──────────────────┐        │
│  │ BlueprintsServices   │──────► BlueprintsFilter │ (AC-03)│
│  │     (AC-02)          │ uses │     (AC-03)      │        │
│  └──────┬───────────────┘      └──────────────────┘        │
│         │ uses                                              │
│  ┌──────▼────────────────┐                                 │
│  │ Blueprints            │                                  │
│  │ Persistence (AC-04)   │                                  │
│  └───────────────────────┘                                  │
│                                                              │
│  Data Objects:                                              │
│  [Blueprint] [Point] [ApiResponse]                          │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

---

### **Vista 2: Technology View** (Vista de Tecnología)

**Propósito:** Mostrar la infraestructura y plataformas tecnológicas

**Elementos a incluir:**
- Nodes: ND-01, ND-02
- System Software: SS-01, SS-02, SS-03, SS-04
- Artifacts: AR-01, AR-02, AR-03, AR-04
- Technology Services: TS-01, TS-02, TS-03

**Representación:**
```
┌────────────────────────────────────────────────────────────┐
│              Docker Compose Environment                     │
│                                                             │
│  ┌──────────────────────────┐  ┌───────────────────────┐  │
│  │  Application Container   │  │  Database Container   │  │
│  │      (ND-01)             │  │      (ND-02)          │  │
│  │                          │  │                       │  │
│  │  ┌────────────────────┐  │  │  ┌─────────────────┐ │  │
│  │  │ Java Runtime (21)  │  │  │  │ PostgreSQL 16   │ │  │
│  │  │     (SS-02)        │  │  │  │    (SS-03)      │ │  │
│  │  └────────┬───────────┘  │  │  └────────┬────────┘ │  │
│  │           │              │  │           │          │  │
│  │  ┌────────▼───────────┐  │  │  ┌────────▼────────┐ │  │
│  │  │ Spring Boot 3.3.9  │  │  │  │ Database Service│ │  │
│  │  │     (SS-01)        │  │  │  │     (TS-02)     │ │  │
│  │  └────────┬───────────┘  │  │  └─────────────────┘ │  │
│  │           │              │  │                       │  │
│  │  ┌────────▼───────────┐  │  │  Volume:              │  │
│  │  │ blueprints.jar     │  │  │  blueprints_pgdata    │  │
│  │  │     (AR-01)        │  │  │                       │  │
│  │  └────────────────────┘  │  │                       │  │
│  │                          │  │                       │  │
│  │  Port: 8080              │  │  Port: 5432           │  │
│  └──────────┬───────────────┘  └───────────┬───────────┘  │
│             │                              │              │
│             └──────────JDBC─────────────────┘              │
│                    (TC-02)                                 │
│                                                             │
└─────────────────────────────────────────────────────────────┘
         │
         │ HTTP/HTTPS (TC-01)
         ▼
   [External Clients]
```

---

### **Vista 3: Layered View** (Vista de Capas)

**Propósito:** Mostrar la arquitectura en capas del sistema

**Elementos a incluir:**
- Todas las capas lógicas
- Flujo de dependencias

**Representación:**
```
┌──────────────────────────────────────────────────────────┐
│                  PRESENTATION LAYER                       │
│  ┌────────────────────────────────────────────────────┐  │
│  │  BlueprintsAPIController (AC-01)                   │  │
│  │  • REST API v1 Interface (AI-01)                   │  │
│  │  • Swagger UI (AC-05, AI-02)                       │  │
│  └────────────────────────────────────────────────────┘  │
└─────────────────────┬────────────────────────────────────┘
                      │ uses
┌─────────────────────▼────────────────────────────────────┐
│                  BUSINESS LAYER                           │
│  ┌────────────────────────────────────────────────────┐  │
│  │  BlueprintsServices (AC-02)                        │  │
│  │  • Business Logic                                  │  │
│  │  • Transaction Management                          │  │
│  └────────────────────────────────────────────────────┘  │
└─────────┬───────────────────────┬────────────────────────┘
          │ uses                  │ uses
┌─────────▼─────────────┐  ┌──────▼────────────────────────┐
│   FILTER LAYER        │  │   PERSISTENCE LAYER           │
│  ┌─────────────────┐  │  │  ┌─────────────────────────┐ │
│  │ BlueprintsFilter│  │  │  │ BlueprintsPersistence   │ │
│  │    (AC-03)      │  │  │  │      (AC-04)            │ │
│  │                 │  │  │  │                         │ │
│  │ • Redundancy    │  │  │  │ • PostgresPersistence   │ │
│  │ • Undersampling │  │  │  │ • JDBC Operations       │ │
│  │ • Identity      │  │  │  │                         │ │
│  └─────────────────┘  │  │  └────────┬────────────────┘ │
└───────────────────────┘  └───────────┼──────────────────┘
                                       │ JDBC (TC-02)
                          ┌────────────▼──────────────────┐
                          │   TECHNOLOGY LAYER            │
                          │  ┌─────────────────────────┐  │
                          │  │ PostgreSQL Database     │  │
                          │  │      (SS-03)            │  │
                          │  │                         │  │
                          │  │ Tables:                 │  │
                          │  │ • blueprints            │  │
                          │  │ • blueprint_points      │  │
                          │  └─────────────────────────┘  │
                          └───────────────────────────────┘
```

---

### **Vista 4: Deployment View** (Vista de Despliegue)

**Propósito:** Mostrar cómo se despliegan los componentes

**Elementos a incluir:**
- Nodes (contenedores Docker)
- Artifacts
- Deployment relationships

**Representación:**
```
┌──────────────────────────────────────────────────────────────┐
│                     Docker Host                               │
│                                                               │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │           Docker Compose Orchestration                   │ │
│  │              (AR-04: docker-compose.yml)                 │ │
│  └─────────────────────────────────────────────────────────┘ │
│                                                               │
│  ┌──────────────────────────┐   ┌──────────────────────────┐│
│  │ blueprints-app           │   │ blueprints-postgres      ││
│  │ (ND-01: Container)       │   │ (ND-02: Container)       ││
│  │                          │   │                          ││
│  │ ┌──────────────────────┐ │   │ ┌──────────────────────┐││
│  │ │ Java 21              │ │   │ │ PostgreSQL 16        │││
│  │ │ (SS-02)              │ │   │ │ (SS-03)              │││
│  │ └──────────────────────┘ │   │ └──────────────────────┘││
│  │                          │   │                          ││
│  │ ┌──────────────────────┐ │   │ Database: blueprints     ││
│  │ │ Spring Boot 3.3.9    │ │   │ User: blueprints_user    ││
│  │ │ (SS-01)              │ │   │                          ││
│  │ └──────────────────────┘ │   │ Volume:                  ││
│  │                          │   │ blueprints_pgdata        ││
│  │ ┌──────────────────────┐ │   │                          ││
│  │ │ AR-01:               │ │   │ Port: 5432               ││
│  │ │ blueprints.jar       │ │   │                          ││
│  │ └──────────────────────┘ │   └──────────────────────────┘│
│  │                          │                                │
│  │ ┌──────────────────────┐ │                                │
│  │ │ AR-03:               │ │                                │
│  │ │ application.props    │ │                                │
│  │ └──────────────────────┘ │                                │
│  │                          │                                │
│  │ Port Mapping: 8080:8080  │                                │
│  │                          │                                │
│  └────────────┬─────────────┘                                │
│               │                                               │
└───────────────┼───────────────────────────────────────────────┘
                │
                │ Network Bridge
                │
         ┌──────▼─────────┐
         │   Internet     │
         │   Clients      │
         └────────────────┘
```

---

### **Vista 5: Service Realization View** (Vista de Realización de Servicios)

**Propósito:** Mostrar cómo los componentes realizan los servicios expuestos

**Elementos a incluir:**
- Application Services (AS-01 a AS-07)
- Application Components que los realizan
- Realization relationships

**Representación:**
```
┌───────────────────────────────────────────────────────────────┐
│               REST API Services (AI-01)                        │
├───────────────────────────────────────────────────────────────┤
│                                                                │
│  Application Services:                                         │
│                                                                │
│  [AS-01: GET All Blueprints]                                  │
│  [AS-02: GET Blueprints by Author]                            │
│  [AS-03: GET Blueprint]                                       │
│  [AS-04: POST Blueprint]                                      │
│  [AS-05: PUT Blueprint]                                       │
│  [AS-06: DELETE Blueprint]                                    │
│  [AS-07: POST Add Point]                                      │
│                                                                │
└────────────────────┬──────────────────────────────────────────┘
                     │ realized by
┌────────────────────▼──────────────────────────────────────────┐
│          BlueprintsAPIController (AC-01)                       │
│                                                                │
│  Endpoints:                                                    │
│  • GET    /api/v1/blueprints          → AS-01                │
│  • GET    /api/v1/blueprints/{author} → AS-02                │
│  • GET    /api/v1/blueprints/{author}/{name} → AS-03         │
│  • POST   /api/v1/blueprints          → AS-04                │
│  • PUT    /api/v1/blueprints/{author}/{name} → AS-05         │
│  • DELETE /api/v1/blueprints/{author}/{name} → AS-06         │
│  • POST   /api/v1/blueprints/{author}/{name}/points → AS-07  │
│                                                                │
└────────────────────┬──────────────────────────────────────────┘
                     │ uses
┌────────────────────▼──────────────────────────────────────────┐
│          BlueprintsServices (AC-02)                            │
│                                                                │
│  Business Operations:                                          │
│  • addNewBlueprint()                                          │
│  • getAllBlueprints()                                         │
│  • getBlueprintsByAuthor()                                    │
│  • getBlueprint() ─┐                                          │
│  • addPoint()      │                                          │
│                    │                                           │
└────────────────────┼───────────────────────────────────────────┘
                     │ uses
         ┌───────────┴──────────┐
         │                      │
┌────────▼────────┐   ┌─────────▼──────────┐
│ Filter (AC-03)  │   │ Persistence (AC-04)│
│                 │   │                     │
│ • Redundancy    │   │ • CRUD Operations   │
│ • Undersampling │   │ • JDBC Queries      │
│ • Identity      │   │                     │
└─────────────────┘   └─────────┬───────────┘
                                │ JDBC
                      ┌─────────▼──────────┐
                      │ PostgreSQL (SS-03) │
                      │                    │
                      │ • blueprints       │
                      │ • blueprint_points │
                      └────────────────────┘
```

---

## 5. Propiedades de Elementos (Properties)

### **Application Component: BlueprintsAPIController (AC-01)**
- **Name:** BlueprintsAPIController
- **Type:** REST Controller
- **Technology:** Spring Web MVC
- **Stereotype:** @RestController
- **Base Path:** /api/v1/blueprints
- **Documentation:** Swagger/OpenAPI enabled

### **Application Component: BlueprintsServices (AC-02)**
- **Name:** BlueprintsServices
- **Type:** Business Service
- **Stereotype:** @Service
- **Responsibilities:** 
  - Business logic orchestration
  - Transaction management
  - Filter application
  - Persistence delegation

### **Application Component: BlueprintsFilter (AC-03)**
- **Name:** BlueprintsFilter
- **Type:** Strategy Component
- **Pattern:** Strategy Pattern
- **Implementations:**
  - RedundancyFilter (@Profile("redundancy"))
  - UndersamplingFilter (@Profile("undersampling"))
  - IdentityFilter (@Profile("identity"))

### **Application Component: BlueprintsPersistence (AC-04)**
- **Name:** PostgresBlueprintPersistence
- **Type:** Repository
- **Stereotype:** @Repository
- **Pattern:** Repository Pattern
- **Technology:** Spring JDBC (JdbcTemplate)
- **Transactions:** @Transactional support

### **System Software: Spring Boot (SS-01)**
- **Name:** Spring Boot
- **Version:** 3.3.9
- **Type:** Application Framework
- **Modules:**
  - spring-boot-starter-web
  - spring-boot-starter-jdbc
  - spring-boot-starter-validation
  - spring-boot-docker-compose

### **System Software: PostgreSQL (SS-03)**
- **Name:** PostgreSQL
- **Version:** 16
- **Type:** Relational Database
- **Port:** 5432
- **Database:** blueprints
- **Schema:**
  - Table: blueprints (author, name)
  - Table: blueprint_points (author, name, idx, x, y)

---

## 6. Patrones de Diseño en ArchiMate

### **Pattern 1: Layered Architecture**
```
Relationship: Layered Pattern
- Presentation Layer (AC-01, AC-05)
- Business Layer (AC-02)
- Filter Layer (AC-03)
- Persistence Layer (AC-04)
- Technology Layer (SS-03)
```

### **Pattern 2: Strategy Pattern** (Filter Layer)
```
<<interface>> BlueprintsFilter (AC-03)
    ├── realizes: RedundancyFilter
    ├── realizes: UndersamplingFilter
    └── realizes: IdentityFilter
    
Selection Mechanism: Spring Profiles
```

### **Pattern 3: Repository Pattern**
```
<<interface>> BlueprintPersistence
    └── realizes: PostgresBlueprintPersistence (AC-04)
    
Encapsulation: Data access logic
```

---

## 7. Cómo Crear el Modelo en Archi (ArchiMate Tool)

### **Paso 1: Crear el Modelo Base**
1. Abrir Archi (herramienta open-source para ArchiMate)
2. File → New → Model
3. Nombrar: "Blueprints REST API Architecture"

### **Paso 2: Crear Folders (Carpetas Organizativas)**
```
Model/
├── Application Layer/
│   ├── Components/
│   ├── Services/
│   ├── Interfaces/
│   └── Data Objects/
├── Technology Layer/
│   ├── Nodes/
│   ├── System Software/
│   ├── Artifacts/
│   └── Technology Services/
└── Views/
    ├── Application Structure View
    ├── Technology View
    ├── Layered View
    ├── Deployment View
    └── Service Realization View
```

### **Paso 3: Crear Elementos**
- Usar la paleta de ArchiMate Application Layer
- Arrastrar elementos según la tabla del punto 2
- Configurar propiedades de cada elemento

### **Paso 4: Crear Relaciones**
- Usar tipos de relación apropiados:
  - **Composition:** para estructura interna
  - **Serving:** para servicios
  - **Realization:** para implementaciones
  - **Assignment:** para despliegue
  - **Access:** para datos
  - **Flow:** para flujo de información

### **Paso 5: Crear Vistas**
- Crear cada vista del punto 4
- Arrastrar elementos relevantes
- Aplicar layout automático o manual
- Agregar colores y estilos

### **Paso 6: Documentar**
- Agregar descripciones a elementos
- Agregar properties
- Generar reporte HTML desde Archi

---

## 8. Herramientas Recomendadas

### **Archi (Open Source)**
- **Website:** https://www.archimatetool.com/
- **Características:**
  - Soporte completo ArchiMate 3.2
  - Generación de reportes HTML
  - Export a PNG, SVG, PDF
  - Gratuito y open source

### **Otras Opciones:**
- **Visual Paradigm** (comercial, soporte ArchiMate)
- **Sparx Enterprise Architect** (comercial)
- **BiZZdesign Enterprise Studio** (comercial)
- **draw.io** (limitado, con extensión ArchiMate)

---

## 9. Export y Formato de Intercambio

### **Formato Open Exchange XML**
Archi puede exportar a formato `.archimate` (XML) para intercambio.

### **Generación de Reportes**
Desde Archi: File → Export Model → HTML Report

---

## 10. Convenciones de Nomenclatura

| Prefijo | Tipo de Elemento |
|---------|------------------|
| AC-XX | Application Component |
| AS-XX | Application Service |
| AI-XX | Application Interface |
| DO-XX | Data Object |
| TS-XX | Technology Service |
| SS-XX | System Software |
| TC-XX | Technology Collaboration |
| AR-XX | Artifact |
| ND-XX | Node |

---

## 11. Colores Recomendados (ArchiMate Standard)

| Layer | Color (Hex) |
|-------|-------------|
| Application Layer | #C9E7B7 (Verde claro) |
| Technology Layer | #B5E3C4 (Verde agua) |
| Motivation Layer | #EDD1C2 (Beige) |
| Strategy Layer | #FFE4B5 (Amarillo suave) |

---

## 12. Checklist de Completitud del Modelo

- [ ] Todos los componentes de aplicación definidos
- [ ] Todos los servicios REST mapeados
- [ ] Interfaces de aplicación documentadas
- [ ] Objetos de datos identificados
- [ ] Infraestructura tecnológica modelada
- [ ] Contenedores Docker representados
- [ ] Relaciones de dependencia establecidas
- [ ] 5 vistas principales creadas
- [ ] Propiedades de elementos completadas
- [ ] Patrones de diseño documentados
- [ ] Modelo validado (sin errores en Archi)

---

*Guía creada para Lab04-ARSW - Spring Boot REST API Blueprints*
*Compatible con ArchiMate 3.2 Specification*

