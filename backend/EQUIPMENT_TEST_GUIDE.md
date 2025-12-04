# Testing Equipment API with Thunder Client

## Prerequisites
- Server running on `http://localhost:8080`
- Customer created (use Customer ID from previous tests)

## Test Sequence

### 1. List All Equipment (Empty)
```
Method: GET
URL: http://localhost:8080/api/equipment

Expected: []
```

### 2. Create Equipment
```
Method: POST
URL: http://localhost:8080/api/equipment
Headers:
  Content-Type: application/json

Body:
{
  "brand": "Dell",
  "model": "Latitude 5420",
  "deviceType": "NOTEBOOK",
  "serialNumber": "SN123456789",
  "physicalCondition": "Buen estado, pantalla con rayón menor",
  "customerId": "4ef1a10c-b48b-4857-ada4-c6ac281e2cf8"
}

Expected: 201 Created
Response includes: id, brand, model, deviceType, serialNumber, physicalCondition, customerId, customerName, summary
```

### 3. List All Equipment
```
Method: GET
URL: http://localhost:8080/api/equipment

Expected: Array with 1 equipment
```

### 4. Get Equipment by ID
```
Method: GET
URL: http://localhost:8080/api/equipment/{id}

Replace {id} with the ID from step 2
Expected: 200 OK with equipment details
```

### 5. Get Equipment by Customer
```
Method: GET
URL: http://localhost:8080/api/equipment/customer/4ef1a10c-b48b-4857-ada4-c6ac281e2cf8

Expected: Array with customer's equipment
```

### 6. Get Equipment by Device Type
```
Method: GET
URL: http://localhost:8080/api/equipment/type/NOTEBOOK

Expected: Array with notebooks
```

### 7. Search Equipment
```
Method: GET
URL: http://localhost:8080/api/equipment/search?q=Dell

Expected: Array with matching equipment
```

### 8. Update Equipment
```
Method: PUT
URL: http://localhost:8080/api/equipment/{id}
Headers:
  Content-Type: application/json

Body:
{
  "brand": "Dell",
  "model": "Latitude 5420",
  "deviceType": "NOTEBOOK",
  "serialNumber": "SN123456789",
  "physicalCondition": "Reparado, como nuevo"
}

Expected: 200 OK with updated equipment
```

### 9. Delete Equipment
```
Method: DELETE
URL: http://localhost:8080/api/equipment/{id}

Expected: 204 No Content
```

### 10. Verify Deletion
```
Method: GET
URL: http://localhost:8080/api/equipment

Expected: []
```

## Device Types Available
- NOTEBOOK
- SMARTPHONE
- MONITOR
- TABLET
- CONSOLE
- PRINTER
- OTHER

## Notes
- Equipment requires a valid customerId
- The `summary` field is auto-generated (e.g., "💻 Notebook Dell Latitude 5420")
- The `customerName` is populated from the related Customer entity
