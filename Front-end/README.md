# Frontend Web - CyrceTech

Cliente web oficial del sistema de gestión CyrceTech, desarrollado con React + Vite y conectado al Backend Spring Boot.

## 📋 Descripción

Interfaz de usuario moderna y responsiva para la gestión integral del taller. Se conecta a la API REST para realizar operaciones CRUD sobre clientes, equipos, tickets, repuestos y facturas.

## 🚀 Características

- **Stack Moderno**: React 19 + TypeScript + Vite
- **Diseño Premium**: Interfaz "Glassmorphism" con Tailwind CSS
- **Conexión Real**: Integrado con Backend Spring Boot (`http://localhost:8080`)
- **Funcionalidades Completas**:
  - 📊 **Dashboard**: Métricas en tiempo real
  - 👥 **Clientes**: Gestión completa (CRUD)
  - 💻 **Equipos**: Registro de dispositivos por cliente
  - 🎫 **Tickets**: Flujo de reparación (Pendiente -> Entregado)
  - 🔧 **Repuestos**: Control de stock e inventario
  - 📄 **Facturación**: Generación de facturas calculadas

## 🛠️ Ejecución

### Requisitos
- Node.js 16+
- Backend Spring Boot corriendo en puerto 8080

### Pasos

1. **Instalar dependencias**:
   ```bash
   npm install
   ```

2. **Iniciar servidor de desarrollo**:
   ```bash
   npm run dev
   ```

3. **Acceder**:
   Abrir `http://localhost:5173` en el navegador.

## 📦 Build para Producción

Para generar los archivos estáticos listos para desplegar:

```bash
npm run build
# Los archivos se generarán en la carpeta /dist
```

## 📂 Estructura

```
frontend-web/
├── components/     # UI Kit (Botones, Inputs, Cards)
├── services/       # Cliente HTTP y llamadas API
├── views/          # Pantallas principales (Dashboard, Clientes, etc.)
├── App.tsx         # Router y Layout principal
└── types.ts        # Definiciones de tipos TypeScript
```
