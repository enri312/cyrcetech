# 🔧 CyrceTech - Sistema de Gestión para Taller de Reparación

Sistema completo de gestión para talleres de reparación de computadoras y notebooks, con diagnóstico asistido por IA local.

## ✨ Características

- 👥 **Gestión de Clientes** - Registro completo de clientes con datos de contacto
- 💻 **Gestión de Equipos** - Control de dispositivos por cliente
- 📋 **Órdenes de Reparación** - Seguimiento completo del ciclo de reparación
- 📦 **Control de Repuestos** - Inventario de piezas y componentes
- 🤖 **Diagnóstico con IA** - Asistencia inteligente usando Ollama (local)
- 📊 **Reportes** - Generación de informes del taller
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
│   ├── app/                    # Aplicación principal
│   ├── entity/                 # Entidades de dominio
│   ├── infrastructure/         # DAOs y DB
│   ├── usecase/               # Servicios de negocio
│   └── interface_adapter/     # Controladores UI
├── src/main/resources/
│   ├── com/cyrcetech/app/view/ # Vistas FXML
│   └── schema.sql             # Esquema de base de datos
├── docker-compose.yml         # Configuración PostgreSQL
└── INICIAR.bat               # Script de inicio rápido
```

## 🛠️ Tecnologías

- **Java 25** - Lenguaje principal
- **JavaFX** - Interfaz gráfica moderna
- **PostgreSQL 18.1** - Base de datos (via Docker)
- **Gradle 9.2.1** - Gestión de dependencias
- **Ollama** - IA local para diagnósticos
- **Clean Architecture** - Arquitectura por capas

## 📖 Módulos del Sistema

### 1. Gestión de Clientes
- Registro de clientes con RUC/CI
- Datos de contacto completos
- Historial de equipos por cliente

### 2. Gestión de Equipos
- Registro de dispositivos (PC, Notebooks, etc.)
- Asociación con clientes
- Información técnica (marca, modelo, serie)

### 3. Órdenes de Reparación
- Estados: Pendiente → Diagnóstico → En Reparación → Listo → Entregado
- Descripción de problemas
- Diagnóstico técnico y de IA
- Control de costos

### 4. Repuestos
- Control de inventario
- Precios y proveedores
- Stock disponible

### 5. Reportes
- Resumen de órdenes
- Ingresos por período
- Listado de clientes
- Estado de inventario

## 🔐 Credenciales por Defecto

- **Usuario**: `admin`
- **Contraseña**: `admin`

## 📚 Documentación Adicional

- [COMO_INICIAR.md](COMO_INICIAR.md) - Guía detallada de inicio
- [Walkthrough](https://github.com/user/cyrcetech/wiki) - Documentación técnica

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

## 🤝 Contribución

Este es un proyecto en desarrollo activo. Próximas funcionalidades:
- [ ] CRUD completo para todas las entidades
- [ ] Generación de reportes PDF
- [ ] Módulo de facturación
- [ ] Búsqueda y filtros avanzados
- [ ] Copias de seguridad automáticas

## 📄 Licencia

Proyecto privado - Todos los derechos reservados

---

**Versión**: 2.0.0  
**Última actualización**: Noviembre 2025
