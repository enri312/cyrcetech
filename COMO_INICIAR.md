# Cómo Iniciar Cyrcetech

## Inicio Rápido

### Opción 1: Script Principal (Recomendado)
Ejecuta el script principal que limpia, compila e inicia la aplicación:
```bash
scripts\iniciar.bat
```

### Opción 2: Inicio Rápido
Si ya compilaste el proyecto y solo quieres ejecutarlo:
```bash
scripts\run.bat
```

## Comandos Gradle Manuales

### Limpiar y Compilar
```bash
gradlew.bat clean build
```

### Ejecutar Aplicación
```bash
gradlew.bat run
```

### Limpiar Archivos Temporales
```bash
scripts\cleanup.bat
```

## Requisitos

- **Java**: JDK 17 o superior
- **Gradle**: 9.2.1 (incluido via wrapper)

## Estructura del Proyecto

```
cyrcetech/
├── scripts/              # Scripts de utilidad
│   ├── iniciar.bat      # Script principal (clean + build + run)
│   ├── run.bat          # Inicio rápido (solo run)
│   └── cleanup.bat      # Limpieza de archivos temporales
├── src/                 # Código fuente Java/JavaFX
├── build.gradle         # Configuración de Gradle
└── gradlew.bat          # Gradle wrapper
```

## Solución de Problemas

### Error: "cannot find symbol: class Ticket"
El proyecto necesita compilarse primero. Ejecuta:
```bash
gradlew.bat clean build
```

### Error: "Java no está instalado"
Verifica que Java esté instalado y en el PATH:
```bash
java --version
```

### La aplicación no inicia
1. Verifica que no haya errores de compilación
2. Revisa los logs en la consola
3. Asegúrate de que todos los archivos FXML estén en `src/main/resources`

