# 🔧 Cyrcetech - Sistema de Gestión de Taller de Reparación

Sistema integral de gestión para talleres de reparación de dispositivos electrónicos, con arquitectura híbrida (JavaFX Desktop + Spring Boot REST API + React Web).

## 📋 Descripción

Cyrcetech es una solución completa que permite gestionar:
- 👥 **Clientes** - Información de contacto y historial
- 💻 **Equipos** - Dispositivos en reparación (notebooks, smartphones, tablets, etc.)
- 🎫 **Órdenes de Reparación** - Seguimiento del estado de reparaciones
- 📦 **Inventario de Repuestos** - Control de stock y proveedores
- 💰 **Facturación** - Generación de facturas y seguimiento de pagos

## 🏗️ Arquitectura

### Arquitectura Híbrida (3 Capas)

```
┌─────────────────────────────────────────────────────────┐
│                    FRONTEND LAYER                        │
├──────────────────────┬──────────────────────────────────┤
│   JavaFX Desktop     │      React Web App               │
│   (Aplicación Local) │   (Aplicación Web Moderna)       │
└──────────────────────┴──────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────┐
│                    BACKEND LAYER                         │
│              Spring Boot REST API                        │
│                  (Puerto 8080)                           │
└─────────────────────────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────┐
│                   DATABASE LAYER                         │
│         PostgreSQL 18.1 (Docker - Puerto 5433)          │
└─────────────────────────────────────────────────────────┘
```

## ✅ Estado del Proyecto

### Etapa 1: Backend API ✅ **COMPLETADO**
- ✅ Spring Boot 3.4.0 configurado
- ✅ PostgreSQL conectado y funcionando
- ✅ 5 entidades implementadas (Customer, Equipment, Ticket, SparePart, Invoice)
- ✅ 44 endpoints REST funcionando
- ✅ Documentación Swagger/OpenAPI
- ✅ Manejo global de excepciones
- ✅ CORS configurado

### Etapa 2: Integración JavaFX ⏳ **EN PROGRESO**
- [ ] Conectar app desktop al backend REST API
- [ ] Reemplazar gestión local con llamadas HTTP
- [ ] Actualizar controllers para consumir endpoints

### Etapa 3: Frontend React 📋 **PLANIFICADO**
- [ ] Crear aplicación web React
- [ ] Diseño responsive moderno
- [ ] Consumir API REST del backend

### Etapa 4: Seguridad 📋 **PLANIFICADO**
- [ ] Autenticación JWT
- [ ] Autorización basada en roles
- [ ] Endpoints seguros

## 🚀 Inicio Rápido

### Requisitos Previos

- **Java JDK 21** o superior
- **PostgreSQL** (Docker recomendado)
- **Gradle 9.2.1**
- **Node.js 18+** (para frontend React en el futuro)

### 1. Iniciar Base de Datos

```bash
# Iniciar contenedor PostgreSQL
docker start cyrcetech_db

# O con docker-compose (si tienes el archivo)
docker-compose up -d
```

### 2. Iniciar Backend API

```bash
cd backend
.\gradlew.bat bootRun --no-daemon
```

El servidor estará disponible en `http://localhost:8080`

### 3. Iniciar Aplicación JavaFX

```bash
# Desde el directorio raíz
.\gradlew.bat run
```

## 📡 API Endpoints

### Customers (6 endpoints)
```
GET    /api/customers              - Listar clientes
GET    /api/customers/{id}         - Obtener cliente
POST   /api/customers              - Crear cliente
PUT    /api/customers/{id}         - Actualizar cliente
DELETE /api/customers/{id}         - Eliminar cliente
GET    /api/customers/search?q=... - Buscar clientes
```

### Equipment (8 endpoints)
```
GET    /api/equipment                      - Listar equipos
GET    /api/equipment/{id}                 - Obtener equipo
GET    /api/equipment/customer/{id}        - Equipos de cliente
GET    /api/equipment/type/{deviceType}    - Equipos por tipo
POST   /api/equipment                      - Crear equipo
PUT    /api/equipment/{id}                 - Actualizar equipo
DELETE /api/equipment/{id}                 - Eliminar equipo
GET    /api/equipment/search?q=...         - Buscar equipos
```

### Tickets (10 endpoints)
```
GET    /api/tickets                        - Listar tickets
GET    /api/tickets/{id}                   - Obtener ticket
GET    /api/tickets/customer/{id}          - Tickets de cliente
GET    /api/tickets/equipment/{id}         - Tickets de equipo
GET    /api/tickets/status/{status}        - Tickets por estado
GET    /api/tickets/active                 - Tickets activos
POST   /api/tickets                        - Crear ticket
PUT    /api/tickets/{id}                   - Actualizar ticket
DELETE /api/tickets/{id}                   - Eliminar ticket
GET    /api/tickets/search?q=...           - Buscar tickets
```

### Spare Parts (9 endpoints)
```
GET    /api/spare-parts                    - Listar repuestos
GET    /api/spare-parts/{id}               - Obtener repuesto
GET    /api/spare-parts/low-stock          - Stock bajo
GET    /api/spare-parts/out-of-stock       - Sin stock
GET    /api/spare-parts/in-stock           - Con stock
POST   /api/spare-parts                    - Crear repuesto
PUT    /api/spare-parts/{id}               - Actualizar repuesto
DELETE /api/spare-parts/{id}               - Eliminar repuesto
GET    /api/spare-parts/search?q=...       - Buscar repuestos
```

### Invoices (11 endpoints)
```
GET    /api/invoices                       - Listar facturas
GET    /api/invoices/{id}                  - Obtener factura
GET    /api/invoices/ticket/{id}           - Factura de ticket
GET    /api/invoices/number/{number}       - Buscar por número
GET    /api/invoices/status/{status}       - Por estado de pago
GET    /api/invoices/overdue               - Facturas vencidas
GET    /api/invoices/paid                  - Facturas pagadas
GET    /api/invoices/pending               - Facturas pendientes
POST   /api/invoices                       - Crear factura
PUT    /api/invoices/{id}                  - Actualizar factura
DELETE /api/invoices/{id}                  - Eliminar factura
```

**Total: 44 endpoints REST**

## 🛠️ Tecnologías

### Backend
- **Spring Boot 3.4.0** - Framework principal
- **Spring Data JPA** - ORM y persistencia
- **PostgreSQL 18.1** - Base de datos
- **Swagger/OpenAPI 2.3.0** - Documentación de API
- **Java 21** - Lenguaje

### Frontend Desktop
- **JavaFX 21** - Framework UI
- **FXML** - Diseño de interfaces
- **CSS** - Estilos personalizados

### Frontend Web (Futuro)
- **React 18** - Framework UI
- **TypeScript** - Lenguaje tipado
- **Tailwind CSS** - Framework de estilos

## 📁 Estructura del Proyecto

```
Cyrcetech/
├── backend/                          # Spring Boot REST API
│   ├── src/main/java/
│   │   └── com/cyrcetech/backend/
│   │       ├── controller/           # REST Controllers (5)
│   │       ├── service/              # Business Logic (5)
│   │       ├── repository/           # JPA Repositories (5)
│   │       ├── domain/entity/        # JPA Entities (5)
│   │       ├── dto/                  # DTOs (15)
│   │       ├── exception/            # Exception Handling
│   │       └── config/               # Configuraciones
│   ├── src/main/resources/
│   │   └── application.yml           # Configuración
│   ├── build.gradle                  # Dependencias backend
│   └── README.md                     # Documentación backend
│
├── src/main/java/com/cyrcetech/      # JavaFX Desktop App
│   ├── entity/                       # Entidades (legacy)
│   ├── interface_adapter/
│   │   └── controller/               # Controllers JavaFX
│   ├── service/                      # Services (legacy)
│   └── CyrcetechApplication.java     # Main JavaFX
│
├── src/main/resources/
│   └── com/cyrcetech/app/view/       # FXML Views
│
├── build.gradle                      # Dependencias JavaFX
└── README.md                         # Este archivo
```

## 🧪 Testing

### Backend API
```bash
cd backend

# Compilar
.\gradlew.bat build

# Tests
.\gradlew.bat test

# Ejecutar
.\gradlew.bat bootRun --no-daemon
```

### Testing con Thunder Client
Ver guías en:
- `backend/THUNDER_CLIENT_GUIDE.md` - Customer API
- `backend/EQUIPMENT_TEST_GUIDE.md` - Equipment API

## 📊 Estadísticas

- **Entidades JPA**: 5
- **Enums**: 4 (DeviceType, TicketStatus, PaymentStatus, PaymentMethod)
- **Repositories**: 5
- **Services**: 5
- **Controllers**: 5
- **DTOs**: 15 (10 Request + 5 Response)
- **Endpoints REST**: 44
- **Líneas de código**: ~3,500+ (backend)

## 🗺️ Roadmap

### ✅ Completado
- [x] Aplicación JavaFX desktop funcional
- [x] Backend REST API completo
- [x] 5 entidades con relaciones JPA
- [x] 44 endpoints REST
- [x] Documentación Swagger
- [x] Manejo de excepciones
- [x] CORS configurado

### 🚧 En Progreso
- [ ] Integración JavaFX con backend REST API

### 📋 Planificado
- [ ] Frontend React web
- [ ] Autenticación JWT
- [ ] Tests unitarios e integración
- [ ] Dockerización completa
- [ ] CI/CD Pipeline
- [ ] Reportes PDF mejorados
- [ ] Dashboard con estadísticas

## 🔐 Configuración

### Variables de Entorno

```bash
# Backend
DB_PASSWORD=password  # Contraseña PostgreSQL (default: password)
```

### application.yml (Backend)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/cyrcetech
    username: admin
    password: ${DB_PASSWORD:password}
  jpa:
    hibernate:
      ddl-auto: update
```

## 📄 Licencia

Proyecto privado - Todos los derechos reservados

---

**Versión**: 1.0.0  
**Estado**: Backend API ✅ Completado | JavaFX Integration ⏳ En Progreso  
**Última actualización**: 2025-12-04
