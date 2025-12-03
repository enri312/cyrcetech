# 🔧 CyrceTech - Sistema de Gestión para Taller de Reparación

Sistema completo de gestión para talleres de reparación de computadoras y notebooks, con arquitectura híbrida (JavaFX + Web) y diagnóstico asistido por IA local.

## 🎯 Arquitectura Híbrida

Este proyecto está en transición hacia una **arquitectura híbrida moderna**:

### Etapa Actual: JavaFX Desktop ✅
- ✅ Aplicación de escritorio completamente funcional
- ✅ Interfaz moderna con Neon Dark Mode
- ✅ CRUD completo para Clientes, Equipos e Historial Técnico
- ✅ Integración con PostgreSQL
- ✅ Diagnóstico con IA (Ollama)

### Próximas Etapas 🚀
1. **Etapa 1 - Backend API (Spring Boot)** ⏳ En Planificación
   - REST API para compartir lógica de negocio
   - Spring Boot + PostgreSQL + JPA
   - Documentación con Swagger/OpenAPI

2. **Etapa 2 - Conectar JavaFX al Backend**
   - Migrar JavaFX para consumir API REST
   - Mantener funcionalidad actual

3. **Etapa 3 - Frontend Web (React + TypeScript)**
   - Interfaz web moderna
   - Acceso desde navegador
   - Mismo backend que JavaFX

4. **Etapa 4 - Docker Compose Unificado**
   - Deployment completo dockerizado
   - Backend + Frontend + PostgreSQL

5. **Etapa 5 - Preparación SaaS** (Futuro)
   - Multi-tenancy
   - Sistema de suscripciones

## ✨ Características Actuales

- 🎨 **Interfaz Moderna** - Diseño Neon Dark Mode con Glassmorphism e iconos SVG
- 📊 **Dashboard Interactivo** - Contadores en tiempo real y accesos directos
- 🌐 **Multilenguaje** - Soporte completo para Español e Inglés
- 👥 **Gestión de Clientes** - CRUD completo con búsqueda
- 💻 **Gestión de Equipos** - CRUD completo con filtros
- 📋 **Órdenes de Reparación** - Seguimiento completo del ciclo
- 📦 **Control de Repuestos** - Inventario de piezas
- 📜 **Historial Técnico** - Vista completa de todas las órdenes con búsqueda
- 🤖 **Diagnóstico con IA** - Asistencia inteligente usando Ollama (local)
- 📊 **Reportes PDF** - Generación de informes
- 💾 **Base de Datos PostgreSQL** - Persistencia robusta con Docker

## 🚀 Inicio Rápido

### Requisitos Previos

- **Java JDK 25** o superior
- **Docker Desktop** (para PostgreSQL)
- **Ollama** (opcional, para diagnóstico IA)

### Instalación y Ejecución

1. **Iniciar Base de Datos**:
   ```bash
   docker-compose up -d
   ```

2. **Ejecutar Aplicación**:
   ```bash
   INICIAR.bat
   ```
   O manualmente:
   ```bash
   ./gradlew run
   ```

La base de datos se inicializa automáticamente al arrancar la aplicación.

## 🤖 Configuración de IA (Opcional)

Para usar el diagnóstico asistido por IA:

1. **Instalar Ollama**: [ollama.ai](https://ollama.ai)

2. **Iniciar servicio**:
   ```bash
   ollama serve
   ```

3. **Descargar modelo**:
   ```bash
   ollama pull deepseek-r1:8b
   ```

## 📁 Estructura del Proyecto

```
cyrcetech/
├── src/main/java/com/cyrcetech/
│   ├── app/                    # Aplicación principal JavaFX
│   ├── entity/                 # Entidades de dominio
│   ├── infrastructure/         # DAOs y DB
│   ├── usecase/               # Servicios de negocio
│   └── interface_adapter/     # Controladores UI
├── src/main/resources/
│   ├── com/cyrcetech/app/view/ # Vistas FXML
│   ├── messages_*.properties   # Archivos de localización
│   └── schema.sql             # Esquema de base de datos
├── frontend-web/              # Frontend Web (React/TypeScript) - En desarrollo
├── cyrcetech-backend/         # Backend API (Spring Boot) - Planificado
├── scripts/                   # Scripts de utilidad
├── docker-compose.yml         # Configuración PostgreSQL
└── INICIAR.bat               # Script de inicio rápido
```

## 🛠️ Tecnologías

### Stack Actual (JavaFX)
- **Java 25** - Lenguaje principal
- **JavaFX 25** - Interfaz gráfica moderna
- **PostgreSQL 18.1** - Base de datos (via Docker)
- **Gradle 9.2.1** - Gestión de dependencias
- **Ollama** - IA local para diagnósticos
- **Clean Architecture** - Arquitectura por capas

### Stack Futuro (Híbrido)
- **Spring Boot 3.2** - Backend REST API
- **React 18 + TypeScript** - Frontend Web
- **Spring Data JPA** - ORM
- **Swagger/OpenAPI** - Documentación API
- **Docker Compose** - Orquestación de servicios

## 📖 Módulos del Sistema

### 1. Gestión de Clientes ✅
- Registro de clientes con RUC/CI
- Datos de contacto completos
- Historial de equipos por cliente
- **CRUD Completo**: Crear, Editar, Eliminar, Buscar

### 2. Gestión de Equipos ✅
- Registro de dispositivos (PC, Notebooks, etc.)
- Asociación con clientes
- Información técnica (marca, modelo, serie)
- **CRUD Completo**: Crear, Editar, Eliminar, Filtrar

### 3. Órdenes de Reparación ✅
- Estados: Pendiente → Diagnóstico → En Reparación → Listo → Entregado
- Descripción de problemas
- Diagnóstico técnico y de IA
- Control de costos

### 4. Historial Técnico ✅
- Vista completa de todas las órdenes
- Búsqueda en tiempo real
- Filtrado por cliente, equipo, problema
- Actualización de datos

### 5. Repuestos
- Control de inventario
- Precios y proveedores
- Stock disponible

### 6. Reportes ✅
- Resumen de órdenes
- Generación de PDF
- Listado de clientes
- Estado de inventario

## 🔐 Credenciales por Defecto

- **Usuario**: `admin`
- **Contraseña**: `admin`

## 📚 Documentación Adicional

- [COMO_INICIAR.md](COMO_INICIAR.md) - Guía detallada de inicio
- [Implementation Plan](/.gemini/antigravity/brain/*/implementation_plan.md) - Plan técnico de migración

## 🐛 Solución de Problemas

### La aplicación no inicia
- Verifica que Docker Desktop esté ejecutándose
- Asegúrate de que el puerto 5432 no esté en uso

### Error de conexión a base de datos
```bash
docker-compose down
docker-compose up -d
```

### Problemas con Gradle
```bash
./gradlew clean build
```

## 🗺️ Roadmap

### Completado ✅
- [x] CRUD completo para Clientes
- [x] CRUD completo para Equipos
- [x] Historial Técnico con búsqueda
- [x] Generación de reportes PDF
- [x] Integración con IA (Ollama)
- [x] Interfaz moderna con Dark Mode

### En Progreso ⏳
- [ ] **Etapa 1**: Backend API con Spring Boot
- [ ] Migración de entidades a JPA
- [ ] Documentación API con Swagger

### Planificado 📋
- [ ] **Etapa 2**: Conectar JavaFX al Backend REST
- [ ] **Etapa 3**: Frontend Web con React + TypeScript
- [ ] **Etapa 4**: Docker Compose unificado
- [ ] Módulo de facturación completo
- [ ] Búsqueda y filtros avanzados
- [ ] Copias de seguridad automáticas
- [ ] **Etapa 5**: Preparación para SaaS

## 📄 Licencia

Proyecto privado - Todos los derechos reservados

---

**Versión**: 3.0.0-hybrid  
**Última actualización**: Diciembre 2025  
**Estado**: En transición a arquitectura híbrida
