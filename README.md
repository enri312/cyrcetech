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

```
┌─────────────────────────────────────────────────────────┐
│                    FRONTEND LAYER                        │
├──────────────────────┬──────────────────────────────────┤
│   JavaFX Desktop     │      React Web App               │
│   (HTTP Client)      │    (Planificado)                 │
└──────────────────────┴──────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────┐
│              Spring Boot REST API                        │
│                 (Puerto 8080)                            │
│               44 Endpoints REST                          │
└─────────────────────────────────────────────────────────┘
                           │
                           ▼
┌─────────────────────────────────────────────────────────┐
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

### Etapa 2: Integración JavaFX ✅ **COMPLETADO**
- ✅ Cliente HTTP configurado (java.net.http.HttpClient)
- ✅ 5 API Services creados (Customer, Equipment, Ticket, SparePart, Invoice)
- ✅ Controllers actualizados para consumir REST API
- ✅ Removidas dependencias PostgreSQL del frontend
- ✅ Dashboard dinámico con datos del API
- ✅ UI mejorada (TablesViews, formularios responsive)

### Etapa 3: Frontend React 📋 **PLANIFICADO**
- [ ] Crear aplicación web React
- [ ] Diseño responsive moderno
- [ ] Consumir API REST del backend

### Etapa 4: Seguridad 📋 **PLANIFICADO**
- [ ] Autenticación JWT
- [ ] Autorización basada en roles
- [ ] Endpoints seguros

### Etapa 5: Integraciones 📋 **PLANIFICADO**
- [ ] n8n para automatizaciones
- [ ] Notificaciones WhatsApp/Email
- [ ] Reportes PDF avanzados

## 🚀 Inicio Rápido

### Requisitos Previos
- **Java JDK 21** o superior
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

## 📡 API Endpoints (44 Total)

| Entidad | Endpoints | Ruta Base |
|---------|-----------|-----------|
| Customers | 6 | `/api/customers` |
| Equipment | 8 | `/api/equipment` |
| Tickets | 10 | `/api/tickets` |
| Spare Parts | 9 | `/api/spare-parts` |
| Invoices | 11 | `/api/invoices` |

Documentación completa: `http://localhost:8080/swagger-ui.html`

## 🛠️ Tecnologías

| Capa | Tecnología |
|------|------------|
| Backend | Spring Boot 3.4.0, Spring Data JPA |
| Database | PostgreSQL 18.1 (Docker) |
| Frontend Desktop | JavaFX 21, Gson |
| Frontend Web | React 18 (planificado) |

## 📊 Estadísticas

- **Entidades JPA**: 5
- **Enums**: 4 (DeviceType, TicketStatus, PaymentStatus, PaymentMethod)
- **Endpoints REST**: 44
- **API Services**: 5
- **Controllers JavaFX**: 13

## 🗺️ Roadmap

### ✅ Completado
- [x] Backend REST API completo
- [x] Integración JavaFX con API
- [x] Dashboard dinámico
- [x] CRUD completo para 5 entidades

### 📋 Próximos Pasos
- [ ] Frontend React web
- [ ] Autenticación JWT
- [ ] Integración n8n
- [ ] Dockerización completa
- [ ] CI/CD Pipeline

---

**Versión**: 2.0.0  
**Estado**: Backend ✅ | JavaFX ✅ | React 📋  
**Última actualización**: 2025-12-05
