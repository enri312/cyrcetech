# Guía de Prueba de Endpoints con Thunder Client

## 📋 Requisitos Previos
- Thunder Client instalado en VS Code
- Servidor Spring Boot corriendo en `http://localhost:8080`

## 🔧 Configuración Inicial

### 1. Abrir Thunder Client
- Click en el ícono del rayo ⚡ en la barra lateral de VS Code
- O presiona `Ctrl+Shift+P` y busca "Thunder Client"

## 🧪 Tests de la API de Customers

### Test 1: Crear un Cliente (POST)
```
Method: POST
URL: http://localhost:8080/api/customers
Headers:
  Content-Type: application/json

Body (JSON):
{
  "name": "Juan Pérez",
  "taxId": "20-12345678-9",
  "address": "Av. Corrientes 1234, CABA",
  "phone": "1122334455"
}

Expected Response: 201 Created
```

### Test 2: Listar Todos los Clientes (GET)
```
Method: GET
URL: http://localhost:8080/api/customers

Expected Response: 200 OK
Response: Array de clientes
```

### Test 3: Buscar Cliente por ID (GET)
```
Method: GET
URL: http://localhost:8080/api/customers/{id}

Nota: Reemplaza {id} con el ID obtenido del Test 1

Expected Response: 200 OK
```

### Test 4: Buscar Clientes (GET)
```
Method: GET
URL: http://localhost:8080/api/customers/search?q=Juan

Expected Response: 200 OK
Response: Array de clientes que coinciden con "Juan"
```

### Test 5: Actualizar Cliente (PUT)
```
Method: PUT
URL: http://localhost:8080/api/customers/{id}
Headers:
  Content-Type: application/json

Body (JSON):
{
  "name": "Juan Pérez Actualizado",
  "taxId": "20-12345678-9",
  "address": "Av. Corrientes 5678, CABA",
  "phone": "1155667788"
}

Expected Response: 200 OK
```

### Test 6: Eliminar Cliente (DELETE)
```
Method: DELETE
URL: http://localhost:8080/api/customers/{id}

Expected Response: 204 No Content
```

## 📊 Swagger UI (Alternativa)
También puedes probar los endpoints desde Swagger UI:
```
http://localhost:8080/swagger-ui/index.html
```

## 🎯 Orden Recomendado de Pruebas
1. POST - Crear cliente
2. GET - Listar todos
3. GET - Buscar por ID (usa el ID del paso 1)
4. GET - Buscar por término
5. PUT - Actualizar (usa el ID del paso 1)
6. GET - Verificar actualización
7. DELETE - Eliminar (usa el ID del paso 1)
8. GET - Verificar eliminación

## ⚠️ Notas Importantes
- Guarda el `id` del cliente creado en el Test 1 para usarlo en los tests posteriores
- Los teléfonos se normalizan automáticamente (se eliminan espacios y guiones)
- El campo `formattedPhone` en la respuesta muestra el teléfono formateado
