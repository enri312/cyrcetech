# Cyrcetech - Sistema de Gestión de Tickets

Sistema de gestión de tickets para reparación de dispositivos electrónicos, desarrollado con JavaFX.

## Características

- 🎫 Gestión de tickets de reparación
- 👥 Administración de clientes
- 🔧 Registro de repuestos
- 🤖 Diagnóstico asistido por IA
- 💾 Persistencia de datos

## Inicio Rápido

### Requisitos
- Java JDK 17 o superior
- Gradle 9.2.1 (incluido via wrapper)

### Ejecutar la Aplicación

```bash
# Opción 1: Script principal (limpia, compila e inicia)
scripts\iniciar.bat

# Opción 2: Solo ejecutar (si ya está compilado)
scripts\run.bat
```

Para más información, consulta [COMO_INICIAR.md](COMO_INICIAR.md)

## Estructura del Proyecto

```
cyrcetech/
├── scripts/              # Scripts de utilidad
├── frontend-web/         # Frontend web React (histórico/referencia)
├── src/main/
│   ├── java/            # Código fuente Java
│   └── resources/       # Recursos (FXML, CSS, imágenes)
├── build.gradle         # Configuración de Gradle
└── gradlew.bat          # Gradle wrapper
```

## Tecnologías

- **JavaFX** - Framework de interfaz gráfica
- **Gradle** - Sistema de construcción
- **Java Records** - Modelos de datos inmutables

