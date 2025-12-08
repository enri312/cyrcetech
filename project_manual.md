# Manual del Proyecto Cyrcetech v2.3.0

## 1. Introducción
Cyrcetech es un sistema integral de **Gestión de Taller de Reparación (SaaS / On-Premise)** diseñado para administrar ordenes de servicio, clientes, inventario y facturación. El sistema utiliza una arquitectura híbrida con un Backend centralizado (Spring Boot) y clientes Frontend (JavaFX Desktop y React Web).

### Tecnologías Clave
- **Backend**: Java 25, Spring Boot 3.4.0, Spring Security (JWT).
- **Frontend Desktop**: JavaFX 21, Modular, Estilos CSS modernos.
- **Frontend Web**: React (Vite).
- **Base de Datos**: PostgreSQL 18.
- **Integraciones**: CyrcePDF (Generación de Reportes), Apache POI (Excel), n8n (Automatización), Docker.

---

## 2. Arquitectura del Sistema
El sistema sigue una **Clean Architecture** (Arquitectura Limpia) adaptada, separando capas de responsabilidad.

```mermaid
graph TD
    Client["Cliente JavaFX / Web"] -->|REST API + JWT| Controller["Controladores (API Layer)"]
    Controller -->|DTOs| Service["Servicios (Business Layer)"]
    Service -->|Entities| Repo["Repositorios (Data Layer)"]
    Repo -->|SQL| DB[("PostgreSQL")]
    
    Service -->|Event| Webhook[WebhookService]
    Webhook -->|HTTP POST| n8n["n8n Automation"]
    
    Service -->|Library| PDF["CyrcePDF Lib"]
    PDF -->|Byte Array| Controller
```

---

## 3. Modelo de Datos (ERD)
Diagrama de Entidad-Relación que representa la estructura de la base de datos.

```mermaid
erDiagram
    USER ||--o{ TICKET : manages
    CUSTOMER ||--o{ EQUIPMENT : owns
    CUSTOMER ||--o{ TICKET : places
    EQUIPMENT ||--o{ TICKET : "is subject of"
    TICKET ||--o{ INVOICE : generates

    USER {
        string id PK
        string username
        string role "ADMIN, TECHNICIAN, USER"
    }

    CUSTOMER {
        string id PK
        string name
        string email
        string phone
        string tax_id
        date registration_date
        string category "NUEVO, REGULAR, VIP, ESPECIAL"
    }

    EQUIPMENT {
        string id PK
        string brand
        string model
        string serial_number
        string device_type
    }

    TICKET {
        string id PK
        string status "PENDING, DIAGNOSING, READY..."
        string problem_description
        double estimated_cost
        double amount_paid
        string ai_diagnosis
        date date_created
    }

    INVOICE {
        string id PK
        string invoice_number
        double total_amount
        string payment_status "PAID, PENDING"
        string payment_method "CASH, CARD"
    }

    AUDIT_LOG {
        string id PK
        string user_id
        string username
        string action "LIST, VIEW, CREATE, UPDATE, DELETE"
        string entity_type
        timestamp timestamp
    }
```

---

## 4. Diagrama de Clases (Dominio Principal)
Vista simplificada de las clases principales del Backend.

```mermaid
classDiagram
    class Ticket {
        -String id
        -TicketStatus status
        -double estimatedCost
        -String problemDescription
        +getRemainingBalance()
        +isFullyPaid()
    }

    class Customer {
        -String id
        -String name
        -String email
        +getInitials()
    }

    class Equipment {
        -String id
        -String brand
        -String model
        +getSummary()
    }

    class TicketService {
        +createTicket(CreateTicketRequest)
        +updateTicket(id, UpdateTicketRequest)
        +getTicketsByStatus(status)
    }

    TicketService --> Ticket : manages
    TicketService --> WebhookService : notifies
    CustomerService --> CustomerPdfExportService : exports PDF
    TicketService --> ExcelExportService : exports Excel
    Ticket "*" --> "1" Customer : belongs to
    Ticket "*" --> "1" Equipment : belongs to
```

---

## 5. Diagrama de Secuencia (Creación de Ticket)
Flujo de interacción cuando un técnico crea una nueva orden de servicio.

```mermaid
sequenceDiagram
    actor Tech as Técnico (JavaFX)
    participant API as ApiClient
    participant Ctrl as TicketController
    participant Svc as TicketService
    participant Repo as TicketRepository
    participant DB as PostgreSQL
    participant WH as WebhookService
    participant N8N as n8n (External)

    Tech->>API: POST /api/tickets (JSON)
    API->>Ctrl: createTicket(request)
    Ctrl->>Svc: createTicket(request)
    
    Svc->>Svc: Validar Cliente/Equipo
    
    Svc->>Repo: save(ticket)
    Repo->>DB: INSERT INTO tickets...
    DB-->>Repo: (Saved Entity)
    Repo-->>Svc: (Saved Entity)

    par Async Notification
        Svc->>WH: notifyTicketCreated(response)
        WH->>N8N: POST /webhook/ticket-created
    end

    Svc-->>Ctrl: TicketResponse
    Ctrl-->>API: 201 Created (TicketResponse)
    API-->>Tech: Mostrar éxito
```

---

## 6. Guía de Instalación y Uso

### Requisitos Previos
- Docker Desktop instalado.
- Java JDK 25 (para desarrollo).

### Pasos para Ejecutar
1. **Despliegue con Docker**:
   Ejecuta el siguiente comando en la raíz del proyecto para iniciar Base de Datos y n8n:
   ```bash
   docker-compose up -d
   ```

2. **Iniciar Backend**:
   Desde la carpeta `/backend` o raíz:
   ```bash
   ./gradlew :backend:bootRun
   ```

3. **Iniciar Cliente Desktop (JavaFX)**:
   Desde la raíz:
   ```bash
   ./gradlew :run
   ```

### Credenciales por Defecto (Base de Datos)
- **Login**: `postgres`
- **Password**: `password`
- **Host**: `localhost:5432 / db`

### Integración n8n
- Acceder a: `http://localhost:5678`
- Configurar el Workflow para escuchar `POST /webhook/ticket-created`.

---

## 7. Nuevas Funcionalidades v2.3.0

### Categoría de Clientes
| Categoría | Rango | Descripción |
|---|---|---|
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
*Generado automáticamente por Antigravity AI - 2025*
