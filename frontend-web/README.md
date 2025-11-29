# Frontend Web - CyrceTech

Versión web del sistema de gestión de tickets CyrceTech, desarrollada con React + Vite.

## Descripción

Este es el frontend web original del sistema CyrceTech, que fue posteriormente convertido a una aplicación JavaFX de escritorio. Se conserva como referencia y para posible uso futuro como interfaz web.

## Características

- **Framework**: React 19 + TypeScript
- **Build Tool**: Vite
- **Styling**: TailwindCSS (CDN)
- **UI**: Diseño glassmorphism con tema oscuro y acentos neón
- **Funcionalidades**:
  - Sistema de login
  - Dashboard con estadísticas
  - Gestión de tickets de reparación
  - Administración de clientes
  - Inventario de repuestos
  - Diagnóstico asistido por IA (Deepseek/Ollama)
  - Internacionalización (ES/EN)

## Ejecutar Localmente

### Requisitos
- Node.js 16+
- npm o yarn

### Instalación

```bash
# Instalar dependencias
npm install

# Configurar API Key (si se usa IA)
# Editar .env.local y agregar GEMINI_API_KEY

# Iniciar servidor de desarrollo
npm run dev

# La aplicación estará disponible en http://localhost:3000
```

### Comandos Disponibles

```bash
npm run dev      # Servidor de desarrollo
npm run build    # Build de producción
npm run preview  # Preview del build
```

## Estructura

```
frontend-web/
├── components/          # Componentes UI reutilizables
│   └── ui.tsx          # GlassCard, Input, Button, etc.
├── services/           # Servicios externos
│   └── geminiService.ts # Integración con IA
├── App.tsx             # Componente principal
├── index.tsx           # Entry point
├── index.html          # HTML base
├── types.ts            # Definiciones TypeScript
├── vite.config.ts      # Configuración Vite
├── tsconfig.json       # Configuración TypeScript
└── package.json        # Dependencias
```

## Relación con JavaFX

Esta versión web fue la base para el desarrollo de la aplicación JavaFX de escritorio. Ambas comparten:
- La misma lógica de negocio
- Modelos de datos similares (Ticket, Customer, SparePart)
- Flujos de trabajo idénticos

## Notas

- **Estado**: Archivado/Referencia
- **Uso actual**: La aplicación principal es la versión JavaFX
- **Propósito**: Conservado para referencia histórica y posible uso futuro como interfaz web
