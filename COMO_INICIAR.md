# Cómo Iniciar Cyrcetech

## 🚀 Inicio Rápido

### Ejecuta la aplicación con UN SOLO COMANDO:

```bash
INICIAR.bat
```

**Eso es todo.** Este script:
1. ✅ Verifica que Ollama esté corriendo (para IA local)
2. ✅ Compila el proyecto automáticamente
3. ✅ Inicia la aplicación JavaFX

---

## 🤖 Configuración de IA Local (Ollama)

Para usar el diagnóstico de IA, **antes de ejecutar `INICIAR.bat`**:

1. **Inicia Ollama** (en otra terminal):
   ```bash
   ollama serve
   ```

2. **Verifica que el modelo esté disponible**:
   ```bash
   ollama pull deepseek-r1:8b
   ```

> **Nota**: Si Ollama no está corriendo, la aplicación funcionará igual pero sin diagnóstico de IA.

---

## 📋 Requisitos

- **Java**: JDK 25 o superior
- **Gradle**: 9.2.1 (incluido via wrapper)
- **Ollama**: Opcional, solo para diagnóstico de IA

---

## 🛠️ Comandos Avanzados (Opcional)

Si necesitas más control, puedes usar Gradle directamente:

### Solo compilar
```bash
gradlew.bat build
```

### Solo ejecutar (sin compilar)
```bash
gradlew.bat run
```

### Limpiar y recompilar
```bash
gradlew.bat clean build
```

---

## ❓ Solución de Problemas

### La aplicación no inicia
1. Verifica que Java esté instalado: `java --version`
2. Revisa los errores en la consola
3. Intenta limpiar: `gradlew.bat clean build`

### Error de compilación
- Asegúrate de tener Java 25 instalado
- Cierra el IDE y vuelve a ejecutar `INICIAR.bat`

### IA no funciona
- Verifica que Ollama esté corriendo: `ollama serve`
- Verifica que el modelo esté instalado: `ollama list`
