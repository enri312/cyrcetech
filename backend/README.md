# 🚀 Cyrcetech Backend API

REST API backend para el sistema de gestión de taller de reparación Cyrcetech.

## 📋 Descripción

Backend API construido con Spring Boot que expone endpoints REST para gestionar clientes, equipos, órdenes de reparación, repuestos y facturación.

## 🛠️ Tecnologías

- **Spring Boot 3.4.0** - Framework principal
- **Spring Data JPA** - ORM y persistencia
- **PostgreSQL 18.1** - Base de datos
- **Swagger/OpenAPI 2.3.0** - Documentación de API
- **Java 21** - Lenguaje

## 🚀 Inicio Rápido

### Requisitos Previos

- Java JDK 21 o superior
- PostgreSQL corriendo en Docker (puerto 5432)
- Gradle 9.2.1

### Configuración

1. **Iniciar PostgreSQL con Docker**:
   ```bash
   docker start cyrcetech_db
   ```

2. **Compilar el proyecto**:
   ```bash
   .\gradlew.bat build
   ```

3. **Ejecutar la aplicación**:
   ```bash
   .\gradlew.bat bootRun --no-daemon
   ```

El servidor se iniciará en `http://localhost:8080`

### Documentación de API

- **Thunder Client** (Recomendado) - Ver `THUNDER_CLIENT_GUIDE.md` y `EQUIPMENT_TEST_GUIDE.md`
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html (en desarrollo)

## 📡 Endpoints Disponibles

### 👥 Customers API

```
GET    /api/customers              - Listar todos los clientes
GET    /api/customers/{id}         - Obtener cliente por ID
POST   /api/customers              - Crear nuevo cliente
PUT    /api/customers/{id}         - Actualizar cliente
DELETE /api/customers/{id}         - Eliminar cliente
GET    /api/customers/search?q=... - Buscar clientes
```

**Ejemplo de Request (Crear Cliente)**:
```json
POST /api/customers
Content-Type: application/json

{
  "name": "Juan Pérez",
  "taxId": "20-12345678-9",
  "address": "Av. Corrientes 1234, CABA",
  "phone": "1122334455"
}
```

**Ejemplo de Response**:
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Juan Pérez",
  "taxId": "20-12345678-9",
  "address": "Av. Corrientes 1234, CABA",
  "phone": "1122334455",
  "formattedPhone": "(112) 233-4455"
}
```

### 💻 Equipment API

```
GET    /api/equipment                      - Listar todos los equipos
GET    /api/equipment/{id}                 - Obtener equipo por ID
GET    /api/equipment/customer/{customerId} - Equipos de un cliente
GET    /api/equipment/type/{deviceType}    - Equipos por tipo
POST   /api/equipment                      - Crear nuevo equipo
PUT    /api/equipment/{id}                 - Actualizar equipo
DELETE /api/equipment/{id}                 - Eliminar equipo
GET    /api/equipment/search?q=...         - Buscar equipos
```

**Device Types**: NOTEBOOK, SMARTPHONE, MONITOR, TABLET, CONSOLE, PRINTER, OTHER

**Ejemplo de Request (Crear Equipo)**:
```json
POST /api/equipment
Content-Type: application/json

{
  "brand": "Dell",
  "model": "Latitude 5420",
  "deviceType": "NOTEBOOK",
  "serialNumber": "SN123456789",
  "physicalCondition": "Buen estado",
  "customerId": "550e8400-e29b-41d4-a716-446655440000"
}
```

### 🎫 Tickets API

```
GET    /api/tickets                        - Listar todos los tickets
GET    /api/tickets/{id}                   - Obtener ticket por ID
GET    /api/tickets/customer/{customerId}  - Tickets de un cliente
GET    /api/tickets/equipment/{equipmentId} - Tickets de un equipo
GET    /api/tickets/status/{status}        - Tickets por estado
GET    /api/tickets/active                 - Tickets activos
POST   /api/tickets                        - Crear nuevo ticket
PUT    /api/tickets/{id}                   - Actualizar ticket
DELETE /api/tickets/{id}                   - Eliminar ticket
GET    /api/tickets/search?q=...           - Buscar tickets
```

**Ticket Status**: PENDING, DIAGNOSING, IN_PROGRESS, WAITING_PARTS, READY, DELIVERED, CANCELLED

**Ejemplo de Request (Crear Ticket)**:
```json
POST /api/tickets
Content-Type: application/json

{
  "customerId": "550e8400-e29b-41d4-a716-446655440000",
  "equipmentId": "660e8400-e29b-41d4-a716-446655440000",
  "problemDescription": "Pantalla no enciende",
  "observations": "Cliente reporta que dejó de funcionar ayer",
  "estimatedCost": 15000.00
}
```

## 🗂️ Estructura del Proyecto

```
backend/
├── src/main/java/com/cyrcetech/backend/
│   ├── CyrcetechBackendApplication.java
│   ├── config/
│   │   └── CorsConfig.java
│   ├── controller/
│   │   ├── CustomerController.java
│   │   ├── EquipmentController.java
│   │   └── TicketController.java
│   ├── domain/entity/
│   │   ├── Customer.java
│   │   ├── Equipment.java
│   │   ├── Ticket.java
│   │   ├── DeviceType.java
│   │   └── TicketStatus.java
│   ├── dto/
│   │   ├── request/
│   │   │   ├── CreateCustomerRequest.java
│   │   │   ├── UpdateCustomerRequest.java
│   │   │   ├── CreateEquipmentRequest.java
│   │   │   ├── UpdateEquipmentRequest.java
│   │   │   ├── CreateTicketRequest.java
│   │   │   └── UpdateTicketRequest.java
│   │   └── response/
│   │       ├── CustomerResponse.java
│   │       ├── EquipmentResponse.java
│   │       └── TicketResponse.java
│   ├── exception/
│   │   ├── GlobalExceptionHandler.java
│   │   ├── ResourceNotFoundException.java
│   │   └── ErrorResponse.java
│   ├── repository/
│   │   ├── CustomerRepository.java
│   │   ├── EquipmentRepository.java
│   │   └── TicketRepository.java
│   └── service/
│       ├── CustomerService.java
│       ├── EquipmentService.java
│       └── TicketService.java
└── src/main/resources/
    └── application.yml
```

## ⚙️ Configuración

### Variables de Entorno

```bash
DB_PASSWORD=password  # Contraseña de PostgreSQL (default: password)
```

### application.yml

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/cyrcetech
    username: admin
    password: ${DB_PASSWORD:password}
  jpa:
    hibernate:
      ddl-auto: update  # Crea/actualiza tablas automáticamente
```

## 🧪 Testing

Ver guías de testing:
- `THUNDER_CLIENT_GUIDE.md` - Guía para Customer API
- `EQUIPMENT_TEST_GUIDE.md` - Guía para Equipment API

```bash
# Ejecutar tests
.\gradlew.bat test

# Build completo
.\gradlew.bat clean build
```

## 📦 Build

```bash
# Build del proyecto
.\gradlew.bat build

# Crear JAR ejecutable
.\gradlew.bat bootJar

# El JAR se genera en: build/libs/cyrcetech-backend-1.0.0.jar
```

## 🗺️ Roadmap

### Completado ✅
- [x] Configuración inicial de Spring Boot
- [x] Customer API (CRUD completo + búsqueda)
- [x] Equipment API (CRUD completo + filtros por customer/tipo)
- [x] Ticket API (CRUD completo + filtros por customer/equipment/status)
- [x] SparePart API (CRUD completo + control de stock)
- [x] Invoice API (Facturación completa)
- [x] Documentación con Swagger
- [x] Manejo global de excepciones
- [x] Configuración CORS
- [x] Tests unitarios y de integración

### En Progreso ⏳
- [ ] Dockerización completa (opcional)

### Planificado 📋
- [ ] Autenticación JWT
- [ ] CI/CD Pipeline

## 📊 Progreso de Implementación

**Entidades Completadas**: 5/5 (100%)
- ✅ Customer
- ✅ Equipment
- ✅ Ticket
- ✅ SparePart
- ✅ Invoice

## 📄 Licencia

Proyecto privado - Todos los derechos reservados

---

**Versión**: 1.0.0  
**Puerto**: 8080  
**Base de Datos**: PostgreSQL 18.1 (Docker puerto 5432)  
**Java**: 21
