# 🔧 Cyrcetech - Sistema de Gestión de Taller de Reparación

Sistema integral de gestión para talleres de reparación de dispositivos electrónicos, con arquitectura híbrida (JavaFX Desktop + Spring Boot REST API + React Web).

## 📋 Descripción

Cyrcetech es una solución completa que permite gestionar:

- 👥 **Clientes** - Información de contacto y historial
- 💻 **Equipos** - Dispositivos en reparación (notebooks, smartphones, tablets, etc.)
- 🎫 **Órdenes de Reparación** - Seguimiento del estado de reparaciones
- 🧰 **Inventario de Repuestos** - Control de stock y proveedores
- 💰 **Facturación** - Generación de facturas y seguimiento de pagos
- 🤖 **IA Local (Phi4-mini)** - Diagnóstico asistido por Ollama

---

## 🚀 Inicio Rápido

### Requisitos Previos
- **Java JDK 25** o superior
- **Docker** (para PostgreSQL)
- **Gradle 9.2.1**

### 1. Iniciar Base de Datos
```bash
docker start cyrcetech_db
```

### 2. Iniciar Backend API
```bash
cd backend
.\gradlew.bat bootRun --console=plain
```
El servidor estará en `http://localhost:8080`

### 3. Iniciar Aplicación JavaFX
```bash
.\gradlew.bat run --console=plain
```

## 📡 API Endpoints (50+ Total)

| Entidad | Endpoints | Ruta Base |
|---|---|---|
| Auth | 2 | `/api/auth` |
| Customers | 7 | `/api/customers` |
| Equipment | 8 | `/api/equipment` |
| Tickets | 11 | `/api/tickets` |
| Spare Parts | 9 | `/api/spare-parts` |
| Invoices | 11 | `/api/invoices` |
| Billing | 3 | `/api/billing` |
| Audit Logs | 5 | `/api/audit` |

Documentación completa: `http://localhost:8080/swagger-ui.html`

## 🛠️ Tecnologías

| Capa | Tecnología |
|---|---|
| Backend | Spring Boot 4.0.0, Spring Data JPA, **CyrcePDF 2.0.0** |
| Database | PostgreSQL 18.1 (Docker) |
| Frontend Desktop | JavaFX 21, Gson |
| Frontend Web | React 19 (Vite) [Carpeta: `Front-end`] |
| IA Local | Ollama + **Phi4-mini** (Microsoft) |

---

## 📋 Plan Híbrido - 5 Etapas

### Etapa 1: Backend API ✅ COMPLETADO
- [x] Spring Boot 3.4.0 configurado
- [x] PostgreSQL conectado y funcionando
- [x] 5 entidades implementadas (Customer, Equipment, Ticket, SparePart, Invoice)
- [x] 44 endpoints REST funcionando
- [x] Documentación Swagger/OpenAPI
- [x] Manejo global de excepciones

### Etapa 2: Integración JavaFX ✅ COMPLETADO
- [x] Cliente HTTP configurado (java.net.http.HttpClient)
- [x] 5 API Services creados (Customer, Equipment, Ticket, SparePart, Invoice)
- [x] Controllers actualizados para consumir REST API
- [x] Removidas dependencias PostgreSQL del frontend
- [x] Dashboard dinámico con datos del API
- [x] UI mejorada (TablesViews, formularios responsive)

### Etapa 3: Frontend Web React ✅ COMPLETADO
- [x] Proyecto React con Vite creado
- [x] Dockerfile configurado
- [x] Integración completa con API (5 servicios: Auth, Customers, Equipment, Tickets, SpareParts, Invoices)
- [x] Vistas implementadas (Clients, Equipment, Invoices)
- [x] Sistema de autenticación JWT integrado
- [x] Soporte multi-idioma (ES/EN)

### Etapa 4: Seguridad ✅ COMPLETADO
- [x] `Autenticación JWT implementada (backend)`
- [x] `Login/Register en frontend web`
- [x] `Autorización basada en roles`
- [x] `Autenticación en JavaFX Desktop`
- [x] `Renderizado condicional de Menú`

### Etapa 5: Integraciones ✅ COMPLETADO
- [x] `Librería propia CyrcePDF implementada`
- [x] `Dashboard Avanzado con JavaFX Charts`
- [x] `Generación de Reportes PDF (CyrcePDF)`
- [x] `Integración Local con n8n (Docker)`
- [x] `Automatización de flujos de trabajo`/Email

### Etapa 6: Orden de Modificación ✅ COMPLETADO (v2.3.0)
- [x] `Categoría de Clientes` (Nuevo, Regular, VIP, Especial)
- [x] `Sistema de Auditoría Completo` (USER, TECHNICIAN, ADMIN)
- [x] `Exportación a Excel` (Tickets con todos los estados)
- [x] `Exportación a PDF con antigüedad` (Clientes)

### Etapa 7: DevOps / CI/CD ✅ COMPLETADO
- [x] **GitHub Actions Workflow** para Backend (Java 25/Gradle)
- [x] **GitHub Actions Workflow** para Frontend (React/Vite)
- [x] Configuración de triggers automáticos por push/PR

---

## 📊 Estadísticas

- **Entidades JPA**: 6 (incluyendo AuditLog)
- **Enums**: 6 (DeviceType, TicketStatus, PaymentStatus, PaymentMethod, CustomerCategory, AuditAction)
- **Endpoints REST**: 50+
- **API Services JavaFX**: 5
- **API Services React**: 6 (Auth + 5 entidades)
- **Controllers JavaFX**: 14 (incluyendo Login)

## ✨ Nuevas Funcionalidades (v2.3.0)

| Funcionalidad | Descripción |
|---|---|
| **Categoría de Cliente** | Nuevo, Regular, VIP, Especial (basado en antigüedad) |
| **Sistema de Auditoría** | Logs completos para todos los roles |
| **Exportar a Excel** | `GET /api/tickets/export/excel` |
| **Exportar a PDF** | `GET /api/customers/export/pdf` |
| **CI/CD** | Pipelines automáticos para Backend y Frontend |
| **IA Local (Phi4-mini)** | Diagnóstico asistido con Ollama (tiempo de respuesta ~10-30s) |
| **Permisos por Rol** | Usuario puede ver/crear Clientes y Equipos |
| **Billing (v2.5.0)** | Reportes de facturación por día/mes/año |
| **Estilos Sincronizados** | Frontend React idéntico al JavaFX |

---

## 📝 Entregables Finales
- [x] Diseño de base de datos (modelo entidad-relación)
- [x] Diagrama UML de clases
- [x] Boceto visual de interfaz
- [x] Estructura del proyecto Java

---

**Versión**: 2.5.0
**Estado**: Backend ✅ | JavaFX ✅ | React ✅ | Seguridad ✅ | Integraciones ✅ | Auditoría ✅ | CI/CD ✅ | IA Local ✅ | Billing ✅
**Última actualización**: 2025-12-13
