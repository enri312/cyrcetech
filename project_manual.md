# Manual del Proyecto Cyrcetech v2.3.0

## 1. Introducción

Cyrcetech es un sistema integral de **Gestión de Taller de Reparación (SaaS / On-Premise)** diseñado para administrar órdenes de servicio, clientes, inventario y facturación. El sistema utiliza una arquitectura híbrida con un Backend centralizado (Spring Boot) y clientes Frontend (JavaFX Desktop y React Web).

### Tecnologías Clave
- **Backend**: Java 25, Spring Boot 3.4.0, Spring Security (JWT)
- **Frontend Desktop**: JavaFX 21, Modular, Estilos CSS modernos
- **Frontend Web**: React 19 (Vite)
- **Base de Datos**: PostgreSQL 18
- **Integraciones**: CyrcePDF (PDF), Apache POI (Excel), n8n (Automatización), Docker

---

## 2. Diagrama de Contexto

Representa al sistema como una caja negra mostrando las interacciones con actores externos.

```mermaid
graph TB
    subgraph Actores Externos
        Admin["👨‍💼 Administrador"]
        Tech["👨‍🔧 Técnico"]
        User["👤 Usuario"]
        Customer["🧑‍💼 Cliente"]
        N8N["🔄 n8n Automation"]
    end

    subgraph Sistema Cyrcetech
        SYS["🔧 Sistema de Gestión<br/>de Taller"]
    end

    Admin -->|"Gestionar Usuarios<br/>Ver Auditoría<br/>Generar Reportes"| SYS
    Tech -->|"Crear/Actualizar Tickets<br/>Ver Equipos<br/>Registrar Diagnóstico"| SYS
    User -->|"Crear Tickets<br/>Ver Historial<br/>Consultar Estado"| SYS
    Customer -->|"Recibir Documentos<br/>Consultar Estado"| SYS
    
    SYS -->|"Reportes Excel/PDF<br/>Logs de Auditoría"| Admin
    SYS -->|"Orden de Servicio PDF<br/>Facturas"| Customer
    SYS -->|"Webhooks<br/>Eventos de Tickets"| N8N
    N8N -->|"Emails Automáticos<br/>Notificaciones"| Customer
```

### Entradas del Sistema
| Entrada | Actor | Descripción |
|---------|-------|-------------|
| Datos de Cliente | Técnico/Admin | Nombre, teléfono, dirección, RUC/DNI |
| Datos de Equipo | Técnico | Marca, modelo, tipo, condición física |
| Orden de Servicio | Técnico/Usuario | Descripción del problema, costo estimado |
| Diagnóstico AI | Sistema | Análisis automático del problema |
| Pagos | Admin | Registro de pagos parciales/totales |

### Salidas del Sistema
| Salida | Destino | Formato |
|--------|---------|---------|
| Orden de Servicio | Cliente | PDF |
| Factura | Cliente | PDF |
| Reporte de Tickets | Admin | Excel (.xlsx) |
| Reporte de Clientes | Admin | PDF (con antigüedad) |
| Logs de Auditoría | Admin | JSON/Tabla |
| Webhooks | n8n | HTTP POST JSON |

---

## 3. Diagrama de Flujo de Datos (DFD Nivel 1)

```mermaid
flowchart TB
    subgraph Externos
        E1["👨‍🔧 Técnico"]
        E2["👨‍💼 Administrador"]
        E3["👤 Usuario"]
        E4["🔄 n8n"]
    end

    subgraph "1.0 Gestión de Clientes"
        P1["Registrar Cliente"]
        P2["Consultar Cliente"]
        P3["Exportar PDF"]
    end

    subgraph "2.0 Gestión de Equipos"
        P4["Registrar Equipo"]
        P5["Asociar a Cliente"]
    end

    subgraph "3.0 Gestión de Tickets"
        P6["Crear Ticket"]
        P7["Actualizar Estado"]
        P8["Exportar Excel"]
    end

    subgraph "4.0 Facturación"
        P9["Generar Factura"]
        P10["Registrar Pago"]
    end

    subgraph "5.0 Auditoría"
        P11["Registrar Acción"]
        P12["Consultar Logs"]
    end

    subgraph Almacenes
        D1[("customers")]
        D2[("equipment")]
        D3[("tickets")]
        D4[("invoices")]
        D5[("audit_logs")]
    end

    E1 --> P1 --> D1
    E1 --> P4 --> D2
    E1 --> P6 --> D3
    E3 --> P6
    P6 --> E4
    
    E2 --> P9 --> D4
    E2 --> P12 --> D5
    E2 --> P3
    E2 --> P8
    
    P1 & P2 & P4 & P6 & P7 & P9 & P10 --> P11 --> D5
```

---

## 4. Diagrama de Casos de Uso

```mermaid
graph LR
    subgraph Actores
        Tech["👨‍🔧 Técnico"]
        Admin["👨‍💼 Admin"]
        User["👤 Usuario"]
    end

    subgraph "Casos de Uso - Clientes"
        UC1["Registrar Cliente"]
        UC2["Buscar Cliente"]
        UC3["Editar Cliente"]
        UC4["Exportar Clientes PDF"]
    end

    subgraph "Casos de Uso - Equipos"
        UC5["Registrar Equipo"]
        UC6["Asociar a Cliente"]
        UC7["Buscar por Tipo"]
    end

    subgraph "Casos de Uso - Tickets"
        UC8["Crear Ticket"]
        UC9["Actualizar Estado"]
        UC10["Ver Historial"]
        UC11["Exportar Tickets Excel"]
    end

    subgraph "Casos de Uso - Admin"
        UC12["Ver Auditoría"]
        UC13["Gestionar Usuarios"]
        UC14["Generar Facturas"]
    end

    Tech --> UC1 & UC2 & UC5 & UC6 & UC8 & UC9 & UC10
    Admin --> UC1 & UC2 & UC3 & UC4 & UC5 & UC8 & UC11 & UC12 & UC13 & UC14
    User --> UC8 & UC10
```

### Descripción de Casos de Uso Principales

| ID | Caso de Uso | Actor Principal | Descripción |
|----|-------------|-----------------|-------------|
| UC1 | Registrar Cliente | Técnico/Admin | Crear nuevo cliente con datos de contacto |
| UC4 | Exportar Clientes PDF | Admin | Generar PDF con lista de clientes y antigüedad |
| UC8 | Crear Ticket | Técnico/Usuario | Registrar nueva orden de servicio |
| UC9 | Actualizar Estado | Técnico | Cambiar estado (PENDING → DIAGNOSING → READY) |
| UC11 | Exportar Tickets Excel | Admin | Descargar todos los tickets en formato Excel |
| UC12 | Ver Auditoría | Admin | Consultar logs de acciones del sistema |

### Matriz de Permisos por Rol

| Acción | Usuario | Técnico | Admin |
|--------|:-------:|:-------:|:-----:|
| Ver sus propios datos | ✅ | ✅ | ✅ |
| Editar su perfil | ✅ | ✅ | ✅ |
| Crear tickets | ✅ | ✅ | ✅ |
| Ver todos los tickets | ❌ | ✅ | ✅ |
| Tomar/gestionar tickets | ❌ | ✅ | ✅ |
| Crear usuarios | ❌ | ❌ | ✅ |
| Cambiar roles | ❌ | ❌ | ✅ |
| Ver reportes completos | ❌ | ❌ | ✅ |
| Configurar el sistema | ❌ | ❌ | ✅ |
| Ver auditoría | ❌ | ❌ | ✅ |
| Exportar Excel/PDF | ❌ | ✅ | ✅ |

---

## 5. Diagrama de Clases (Dominio)

```mermaid
classDiagram
    class Customer {
        -String id
        -String name
        -String taxId
        -String address
        -String phone
        -LocalDate registrationDate
        -CustomerCategory category
        +getFormattedPhone()
        +getSeniorityDays()
        +getFormattedSeniority()
        +updateCategory()
    }

    class CustomerCategory {
        <<enumeration>>
        NUEVO
        REGULAR
        VIP
        ESPECIAL
        +getDisplayName()
        +fromDays(long days)
    }

    class Equipment {
        -String id
        -String brand
        -String model
        -DeviceType deviceType
        -String serialNumber
        -String physicalCondition
        -Customer customer
        +getSummary()
    }

    class DeviceType {
        <<enumeration>>
        NOTEBOOK
        SMARTPHONE
        TABLET
        MONITOR
        CONSOLE
        PRINTER
        OTHER
    }

    class Ticket {
        -String id
        -Customer customer
        -Equipment equipment
        -String problemDescription
        -TicketStatus status
        -double estimatedCost
        -double amountPaid
        -String aiDiagnosis
        -LocalDate dateCreated
        +getRemainingBalance()
        +isFullyPaid()
    }

    class TicketStatus {
        <<enumeration>>
        PENDING
        DIAGNOSING
        IN_PROGRESS
        WAITING_PARTS
        READY
        DELIVERED
        CANCELLED
    }

    class AuditLog {
        -String id
        -String userId
        -String username
        -String userRole
        -AuditAction action
        -String entityType
        -String entityId
        -LocalDateTime timestamp
        -String details
    }

    class AuditAction {
        <<enumeration>>
        LIST
        VIEW
        SEARCH
        CREATE
        UPDATE
        DELETE
        EXPORT_PDF
        EXPORT_EXCEL
        LOGIN
        LOGOUT
    }

    Customer "1" --> "*" Equipment : owns
    Customer "1" --> "*" Ticket : places
    Customer --> CustomerCategory
    Equipment --> DeviceType
    Equipment "1" --> "*" Ticket : subject of
    Ticket --> TicketStatus
    AuditLog --> AuditAction
```

---

## 6. Diseño del Sistema

### 6.1 Arquitectura

El sistema implementa **Clean Architecture** con separación en capas:

```mermaid
graph TB
    subgraph "Presentation Layer"
        JFX["JavaFX Desktop"]
        React["React Web"]
        Swagger["Swagger UI"]
    end

    subgraph "API Layer (Controllers)"
        Auth["AuthController"]
        Cust["CustomerController"]
        Equip["EquipmentController"]
        Tick["TicketController"]
        Audit["AuditLogController"]
    end

    subgraph "Business Layer (Services)"
        AuthS["AuthService"]
        CustS["CustomerService"]
        TickS["TicketService"]
        AuditS["AuditLogService"]
        ExcelS["ExcelExportService"]
        PdfS["CustomerPdfExportService"]
    end

    subgraph "Data Layer (Repositories)"
        CustR["CustomerRepository"]
        EquipR["EquipmentRepository"]
        TickR["TicketRepository"]
        AuditR["AuditLogRepository"]
    end

    subgraph External
        DB[("PostgreSQL")]
        N8N["n8n Webhooks"]
    end

    JFX & React --> Auth & Cust & Equip & Tick
    Audit --> AuditS --> AuditR --> DB
    Cust --> CustS --> CustR --> DB
    CustS --> PdfS
    Tick --> TickS --> TickR --> DB
    TickS --> ExcelS
    TickS --> N8N
```

### 6.2 Diseño de Base de Datos (ERD)

```mermaid
erDiagram
    users ||--o{ audit_logs : generates
    customers ||--o{ equipment : owns
    customers ||--o{ tickets : places
    equipment ||--o{ tickets : "is subject of"
    tickets ||--o{ invoices : generates

    users {
        uuid id PK
        varchar username UK
        varchar email UK
        varchar password_hash
        varchar role "ADMIN|TECHNICIAN|USER"
        timestamp created_at
    }

    customers {
        uuid id PK
        varchar name
        varchar tax_id UK
        varchar address
        varchar phone
        date registration_date
        varchar category "NUEVO|REGULAR|VIP|ESPECIAL"
        timestamp created_at
        timestamp updated_at
    }

    equipment {
        uuid id PK
        varchar brand
        varchar model
        varchar device_type
        varchar serial_number UK
        varchar physical_condition
        uuid customer_id FK
        timestamp created_at
    }

    tickets {
        uuid id PK
        uuid customer_id FK
        uuid equipment_id FK
        text problem_description
        text observations
        varchar status
        decimal estimated_cost
        decimal amount_paid
        text ai_diagnosis
        date date_created
        timestamp updated_at
    }

    invoices {
        uuid id PK
        varchar invoice_number UK
        uuid ticket_id FK
        uuid customer_id FK
        decimal total_amount
        varchar payment_status
        varchar payment_method
        text notes
        date issue_date
        date due_date
    }

    audit_logs {
        uuid id PK
        uuid user_id
        varchar username
        varchar user_role
        varchar action
        varchar entity_type
        varchar entity_id
        text details
        varchar ip_address
        timestamp timestamp
    }
```

### 6.3 APIs / Módulos Internos

| Módulo | Función | Entradas | Salidas | Dependencias |
|--------|---------|----------|---------|--------------|
| **AuthService** | Autenticación JWT | email, password | JWT Token, UserInfo | UserRepository, JwtService |
| **CustomerService** | CRUD Clientes | CustomerRequest | CustomerResponse | CustomerRepository, PdfExportService |
| **TicketService** | Gestión Tickets | TicketRequest | TicketResponse | TicketRepo, WebhookService, ExcelExportService |
| **AuditLogService** | Registro de acciones | action, entity | void | AuditLogRepository, SecurityContext |
| **ExcelExportService** | Exportar a Excel | List&lt;Ticket&gt; | byte[] (xlsx) | Apache POI |
| **CustomerPdfExportService** | Exportar a PDF | List&lt;Customer&gt; | byte[] (pdf) | CyrcePDF |
| **WebhookService** | Notificar eventos | TicketResponse | HTTP Response | HttpClient |

---

## 7. Implementación

### 7.1 Lenguaje y Librerías

| Componente | Tecnología | Versión |
|------------|------------|---------|
| **Lenguaje** | Java | 25 |
| **Framework Backend** | Spring Boot | 3.4.0 |
| **ORM** | Spring Data JPA / Hibernate | 6.x |
| **Seguridad** | Spring Security + JWT | 6.x |
| **PDF** | CyrcePDF | 1.0.0 |
| **Excel** | Apache POI | 5.2.5 |
| **Base de Datos** | PostgreSQL | 18.1 |
| **Frontend Desktop** | JavaFX | 21 |
| **Frontend Web** | React + Vite | 19 |
| **Automatización** | n8n | Docker |

### 7.2 Requisitos del Sistema

| Requisito | Mínimo | Recomendado |
|-----------|--------|-------------|
| **JDK** | 21 | 25 |
| **RAM** | 4 GB | 8 GB |
| **Disco** | 500 MB | 2 GB |
| **Docker** | 20.x | 24.x |
| **PostgreSQL** | 14 | 18 |

### 7.3 Estructura del Proyecto

```
cyrcetech/
├── backend/                          # Spring Boot Backend
│   ├── src/main/java/com/cyrcetech/backend/
│   │   ├── config/                   # Configuraciones (Security, CORS, OpenAPI)
│   │   ├── controller/               # REST Controllers
│   │   ├── domain/entity/            # Entidades JPA + Enums
│   │   ├── dto/                      # Request/Response DTOs
│   │   ├── exception/                # Manejo de excepciones
│   │   ├── repository/               # Spring Data Repositories
│   │   ├── security/                 # JWT, UserDetails
│   │   └── service/                  # Lógica de negocio
│   └── build.gradle
│
├── src/main/java/com/cyrcetech/      # JavaFX Frontend
│   ├── app/                          # Aplicación principal
│   ├── entity/                       # Modelos locales
│   ├── infrastructure/api/           # Clientes REST
│   └── interface_adapter/controller/ # Controllers FXML
│
├── frontend-web/                     # React Frontend
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   └── services/
│   └── package.json
│
├── docker-compose.yml                # PostgreSQL + n8n
└── README.md
```

### 7.4 Fragmentos de Código Relevantes

#### Categorización Automática de Clientes
```java
// CustomerCategory.java
public static CustomerCategory fromDays(long daysSinceRegistration) {
    if (daysSinceRegistration <= 30) return NUEVO;
    if (daysSinceRegistration <= 180) return REGULAR;
    if (daysSinceRegistration <= 365) return VIP;
    return ESPECIAL;
}
```

#### Registro de Auditoría
```java
// AuditLogService.java
public void logAction(AuditAction action, String entityType, String entityId, String details) {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    AuditLog log = new AuditLog();
    log.setUserId(getUserId(auth));
    log.setUsername(auth.getName());
    log.setAction(action);
    log.setEntityType(entityType);
    log.setTimestamp(LocalDateTime.now());
    auditLogRepository.save(log);
}
```

#### Exportación a Excel
```java
// ExcelExportService.java
public byte[] exportTicketsToExcel(List<Ticket> tickets) throws IOException {
    try (Workbook workbook = new XSSFWorkbook()) {
        Sheet sheet = workbook.createSheet("Tickets");
        // Headers + Data rows
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        return out.toByteArray();
    }
}
```

---

## 8. Nuevas Funcionalidades v2.3.0

### Categoría de Clientes
| Categoría | Rango | Descripción |
|-----------|-------|-------------|
| NUEVO | 0-30 días | Cliente reciente |
| REGULAR | 1-6 meses | Cliente establecido |
| VIP | 6-12 meses | Cliente fiel |
| ESPECIAL | 1+ año | Cliente preferencial |

### Sistema de Auditoría
- Registro automático de todas las acciones (LIST, VIEW, CREATE, UPDATE, DELETE)
- Filtros por usuario, rol, entidad y fecha
- Solo accesible por usuarios ADMIN

### Exportaciones
- **Excel (Tickets)**: `GET /api/tickets/export/excel`
- **PDF (Clientes)**: `GET /api/customers/export/pdf` (incluye antigüedad y categoría)

---

*Generado automáticamente por Antigravity AI - Diciembre 2025*
