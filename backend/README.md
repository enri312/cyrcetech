# 🚀 Cyrcetech Backend API

REST API backend para el sistema de gestión de taller de reparación Cyrcetech.

## 📋 Descripción

Backend API construido con Spring Boot que expone endpoints REST para gestionar clientes, equipos, órdenes de reparación, repuestos y facturación.

## 🛠️ Tecnologías

- **Spring Boot 3.2.0** - Framework principal
- **Spring Data JPA** - ORM y persistencia
- **PostgreSQL** - Base de datos
- **Lombok** - Reducción de boilerplate
- **Swagger/OpenAPI** - Documentación de API
- **Java 21** - Lenguaje

## 🚀 Inicio Rápido

### Requisitos Previos

- Java JDK 21 o superior
- PostgreSQL corriendo (puerto 5432)
- Gradle 8.x

### Configuración

1. **Asegúrate de que PostgreSQL esté corriendo**:
   ```bash
   docker-compose up -d
   ```
   (Desde el directorio raíz de Cyrcetech)

2. **Compilar el proyecto**:
   ```bash
   ./gradlew build
   ```

3. **Ejecutar la aplicación**:
   ```bash
   ./gradlew bootRun
   ```

El servidor se iniciará en `http://localhost:8080`

### Documentación de API

Una vez iniciado el servidor, accede a:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI Docs**: http://localhost:8080/api-docs

## 📡 Endpoints Disponibles

### Customers API

```
GET    /api/customers              - Listar todos los clientes
GET    /api/customers/{id}         - Obtener cliente por ID
POST   /api/customers              - Crear nuevo cliente
PUT    /api/customers/{id}         - Actualizar cliente
DELETE /api/customers/{id}         - Eliminar cliente
GET    /api/customers/search?q=... - Buscar clientes
```

### Ejemplo de Request (Crear Cliente)

```json
POST /api/customers
Content-Type: application/json

{
  "name": "Juan Pérez",
  "taxId": "4.555.666",
  "address": "Av. Siempre Viva 123",
  "phone": "0981-555-0101"
}
```

### Ejemplo de Response

```json
{
  "id": "550e8400-e29b-41d4-a716-446655440000",
  "name": "Juan Pérez",
  "taxId": "4.555.666",
  "address": "Av. Siempre Viva 123",
  "phone": "0981555010",
  "formattedPhone": "(098) 155-50101"
}
```

## 🗂️ Estructura del Proyecto

```
backend/
├── src/main/java/com/cyrcetech/backend/
│   ├── CyrcetechBackendApplication.java  # Clase principal
│   ├── config/                           # Configuraciones
│   │   └── CorsConfig.java
│   ├── controller/                       # REST Controllers
│   │   └── CustomerController.java
│   ├── domain/entity/                    # Entidades JPA
│   │   └── Customer.java
│   ├── dto/                              # Data Transfer Objects
│   │   ├── request/
│   │   │   ├── CreateCustomerRequest.java
│   │   │   └── UpdateCustomerRequest.java
│   │   └── response/
│   │       └── CustomerResponse.java
│   ├── exception/                        # Manejo de excepciones
│   │   ├── GlobalExceptionHandler.java
│   │   ├── ResourceNotFoundException.java
│   │   └── ErrorResponse.java
│   ├── repository/                       # Repositorios JPA
│   │   └── CustomerRepository.java
│   └── service/                          # Lógica de negocio
│       └── CustomerService.java
└── src/main/resources/
    └── application.yml                   # Configuración
```

## ⚙️ Configuración

### Variables de Entorno

```bash
DB_PASSWORD=postgres  # Contraseña de PostgreSQL (default: postgres)
```

### application.yml

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/cyrcetech
    username: postgres
    password: ${DB_PASSWORD:postgres}
  jpa:
    hibernate:
      ddl-auto: update  # Crea/actualiza tablas automáticamente
```

## 🧪 Testing

```bash
# Ejecutar tests
./gradlew test

# Ejecutar tests con coverage
./gradlew test jacocoTestReport
```

## 📦 Build

```bash
# Build del proyecto
./gradlew build

# Crear JAR ejecutable
./gradlew bootJar

# El JAR se genera en: build/libs/cyrcetech-backend-1.0.0.jar
```

## 🐳 Docker (Próximamente)

```bash
# Build de imagen Docker
docker build -t cyrcetech-backend .

# Ejecutar contenedor
docker run -p 8080:8080 cyrcetech-backend
```

## 🗺️ Roadmap

### Completado ✅
- [x] Configuración inicial de Spring Boot
- [x] Customer API (CRUD completo)
- [x] Documentación con Swagger
- [x] Manejo global de excepciones
- [x] Configuración CORS

### En Progreso ⏳
- [ ] Equipment API
- [ ] Ticket API
- [ ] SparePart API
- [ ] Invoice API

### Planificado 📋
- [ ] Autenticación JWT
- [ ] Tests unitarios y de integración
- [ ] Dockerización
- [ ] CI/CD Pipeline

## 📄 Licencia

Proyecto privado - Todos los derechos reservados

---

**Versión**: 1.0.0  
**Puerto**: 8080  
**Base de Datos**: PostgreSQL 18.1
