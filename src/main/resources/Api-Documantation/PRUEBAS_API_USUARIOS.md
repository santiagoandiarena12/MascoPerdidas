# Pruebas de API - CRUD de Usuarios

## Casos de Prueba Detallados

Este documento contiene casos de prueba para todos los endpoints de la API de Usuarios, incluyendo casos exitosos y casos de error.

---

## 1. CREATE - Registro de Nuevo Usuario

### ✅ Caso Exitoso - Registro válido

**Request:**
```bash
POST http://localhost:8080/api/usuarios/registro
Content-Type: application/json

{
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "password": "miPassword123",
  "telefono": "+34 612 345 678",
  "latitudCasa": -34.6037,
  "longitudCasa": -58.3816
}
```

**Respuesta esperada:** `201 CREATED`
```json
{
  "id": 1,
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "password": "miPassword123",
  "telefono": "+34 612 345 678",
  "latitudCasa": -34.6037,
  "longitudCasa": -58.3816,
  "mascotas": []
}
```

---

### ❌ Caso de Error - Email duplicado

**Request:**
```bash
POST http://localhost:8080/api/usuarios/registro
Content-Type: application/json

{
  "nombre": "Otro Juan",
  "email": "juan@example.com",
  "password": "password123"
}
```

**Respuesta esperada:** `400 BAD REQUEST`
**Mensaje:** "El email ya está registrado: juan@example.com"

---

### ❌ Caso de Error - Email inválido

**Request:**
```bash
POST http://localhost:8080/api/usuarios/registro
Content-Type: application/json

{
  "nombre": "María García",
  "email": "email-invalido",
  "password": "password123"
}
```

**Respuesta esperada:** `400 BAD REQUEST`
**Mensaje:** "El formato del email es inválido"

---

### ❌ Caso de Error - Contraseña débil

**Request:**
```bash
POST http://localhost:8080/api/usuarios/registro
Content-Type: application/json

{
  "nombre": "Carlos López",
  "email": "carlos@example.com",
  "password": "123"
}
```

**Respuesta esperada:** `400 BAD REQUEST`
**Mensaje:** "La contraseña debe tener al menos 6 caracteres"

---

### ❌ Caso de Error - Nombre vacío

**Request:**
```bash
POST http://localhost:8080/api/usuarios/registro
Content-Type: application/json

{
  "nombre": "",
  "email": "usuario@example.com",
  "password": "password123"
}
```

**Respuesta esperada:** `400 BAD REQUEST`
**Mensaje:** "El nombre del usuario es obligatorio"

---

### ❌ Caso de Error - Email vacío

**Request:**
```bash
POST http://localhost:8080/api/usuarios/registro
Content-Type: application/json

{
  "nombre": "Usuario Nuevo",
  "email": "",
  "password": "password123"
}
```

**Respuesta esperada:** `400 BAD REQUEST`
**Mensaje:** "El email del usuario es obligatorio"

---

## 2. READ - Obtener Todos los Usuarios

### ✅ Caso Exitoso

**Request:**
```bash
GET http://localhost:8080/api/usuarios
```

**Respuesta esperada:** `200 OK`
```json
[
  {
    "id": 1,
    "nombre": "Juan Pérez",
    "email": "juan@example.com",
    "password": "miPassword123",
    "telefono": "+34 612 345 678",
    "latitudCasa": -34.6037,
    "longitudCasa": -58.3816,
    "mascotas": []
  },
  {
    "id": 2,
    "nombre": "María García",
    "email": "maria@example.com",
    "password": "pass123456",
    "telefono": "+34 612 111 111",
    "latitudCasa": -34.6037,
    "longitudCasa": -58.3816,
    "mascotas": []
  }
]
```

---

## 3. READ - Obtener Usuario por ID

### ✅ Caso Exitoso

**Request:**
```bash
GET http://localhost:8080/api/usuarios/1
```

**Respuesta esperada:** `200 OK`
```json
{
  "id": 1,
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "password": "miPassword123",
  "telefono": "+34 612 345 678",
  "latitudCasa": -34.6037,
  "longitudCasa": -58.3816,
  "mascotas": []
}
```

---

### ❌ Caso de Error - Usuario no encontrado

**Request:**
```bash
GET http://localhost:8080/api/usuarios/999
```

**Respuesta esperada:** `404 NOT FOUND`

---

### ❌ Caso de Error - ID inválido

**Request:**
```bash
GET http://localhost:8080/api/usuarios/-1
```

**Respuesta esperada:** `400 BAD REQUEST`
**Mensaje:** "ID de usuario inválido"

---

## 4. READ - Obtener Usuario por Email

### ✅ Caso Exitoso

**Request:**
```bash
GET http://localhost:8080/api/usuarios/email/juan@example.com
```

**Respuesta esperada:** `200 OK`
```json
{
  "id": 1,
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "password": "miPassword123",
  "telefono": "+34 612 345 678",
  "latitudCasa": -34.6037,
  "longitudCasa": -58.3816,
  "mascotas": []
}
```

---

### ❌ Caso de Error - Email no encontrado

**Request:**
```bash
GET http://localhost:8080/api/usuarios/email/noexiste@example.com
```

**Respuesta esperada:** `404 NOT FOUND`

---

## 5. SEARCH - Buscar Usuarios por Nombre

### ✅ Caso Exitoso - Búsqueda exacta

**Request:**
```bash
GET http://localhost:8080/api/usuarios/buscar?nombre=Juan
```

**Respuesta esperada:** `200 OK`
```json
[
  {
    "id": 1,
    "nombre": "Juan Pérez",
    "email": "juan@example.com",
    "password": "miPassword123",
    "telefono": "+34 612 345 678",
    "latitudCasa": -34.6037,
    "longitudCasa": -58.3816,
    "mascotas": []
  }
]
```

---

### ✅ Caso Exitoso - Búsqueda parcial (case-insensitive)

**Request:**
```bash
GET http://localhost:8080/api/usuarios/buscar?nombre=pérez
```

**Respuesta esperada:** `200 OK` - Encuentra a "Juan Pérez"

---

### ✅ Caso Exitoso - Múltiples resultados

**Request:**
```bash
GET http://localhost:8080/api/usuarios/buscar?nombre=a
```

**Respuesta esperada:** `200 OK` - Retorna todos los usuarios con 'a' en el nombre

---

### ❌ Caso de Error - Nombre vacío

**Request:**
```bash
GET http://localhost:8080/api/usuarios/buscar?nombre=
```

**Respuesta esperada:** `400 BAD REQUEST`
**Mensaje:** "El nombre de búsqueda no puede estar vacío"

---

## 6. UPDATE - Actualizar Perfil del Usuario

### ✅ Caso Exitoso - Actualizar nombre

**Request:**
```bash
PUT http://localhost:8080/api/usuarios/1
Content-Type: application/json

{
  "nombre": "Juan Pérez Actualizado"
}
```

**Respuesta esperada:** `200 OK`
```json
{
  "id": 1,
  "nombre": "Juan Pérez Actualizado",
  "email": "juan@example.com",
  "password": "miPassword123",
  "telefono": "+34 612 345 678",
  "latitudCasa": -34.6037,
  "longitudCasa": -58.3816,
  "mascotas": []
}
```

---

### ✅ Caso Exitoso - Actualizar ubicación

**Request:**
```bash
PUT http://localhost:8080/api/usuarios/1
Content-Type: application/json

{
  "latitudCasa": -34.5997,
  "longitudCasa": -58.3756
}
```

**Respuesta esperada:** `200 OK`

---

### ✅ Caso Exitoso - Actualizar múltiples campos

**Request:**
```bash
PUT http://localhost:8080/api/usuarios/1
Content-Type: application/json

{
  "nombre": "Juan Pérez Actualizado",
  "telefono": "+34 612 999 999",
  "latitudCasa": -34.5997,
  "longitudCasa": -58.3756
}
```

**Respuesta esperada:** `200 OK`

---

### ❌ Caso de Error - Intento de cambiar email (PROTECCIÓN DE SEGURIDAD)

**Request:**
```bash
PUT http://localhost:8080/api/usuarios/1
Content-Type: application/json

{
  "nombre": "Juan",
  "email": "nuevo@example.com"
}
```

**Respuesta esperada:** `400 BAD REQUEST`
**Mensaje:** "No puedes cambiar tu email"

---

### ❌ Caso de Error - Intento de cambiar password (PROTECCIÓN DE SEGURIDAD)

**Request:**
```bash
PUT http://localhost:8080/api/usuarios/1
Content-Type: application/json

{
  "nombre": "Juan",
  "password": "nuevoPassword123"
}
```

**Respuesta esperada:** `400 BAD REQUEST`
**Mensaje:** "Usa el endpoint de cambiar contraseña"

---

### ❌ Caso de Error - Nombre exceede límite de caracteres

**Request:**
```bash
PUT http://localhost:8080/api/usuarios/1
Content-Type: application/json

{
  "nombre": "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
}
```

**Respuesta esperada:** `400 BAD REQUEST`
**Mensaje:** "El nombre no puede exceder 100 caracteres"

---

### ❌ Caso de Error - Usuario no encontrado

**Request:**
```bash
PUT http://localhost:8080/api/usuarios/999
Content-Type: application/json

{
  "nombre": "Juan"
}
```

**Respuesta esperada:** `404 NOT FOUND`

---

## 7. UPDATE - Cambiar Contraseña

### ✅ Caso Exitoso

**Request:**
```bash
POST http://localhost:8080/api/usuarios/1/cambiar-password
Content-Type: application/json

{
  "passwordActual": "miPassword123",
  "passwordNueva": "nuevoPassword456"
}
```

**Respuesta esperada:** `204 NO CONTENT` (sin body)

---

### ❌ Caso de Error - Contraseña actual incorrecta

**Request:**
```bash
POST http://localhost:8080/api/usuarios/1/cambiar-password
Content-Type: application/json

{
  "passwordActual": "passwordIncorrecto",
  "passwordNueva": "nuevoPassword456"
}
```

**Respuesta esperada:** `401 UNAUTHORIZED`
**Mensaje:** "La contraseña actual es incorrecta"

---

### ❌ Caso de Error - Contraseña nueva muy débil

**Request:**
```bash
POST http://localhost:8080/api/usuarios/1/cambiar-password
Content-Type: application/json

{
  "passwordActual": "miPassword123",
  "passwordNueva": "123"
}
```

**Respuesta esperada:** `400 BAD REQUEST`
**Mensaje:** "La contraseña debe tener al menos 6 caracteres"

---

### ❌ Caso de Error - Campos vacíos

**Request:**
```bash
POST http://localhost:8080/api/usuarios/1/cambiar-password
Content-Type: application/json

{
  "passwordActual": "miPassword123",
  "passwordNueva": null
}
```

**Respuesta esperada:** `400 BAD REQUEST`

---

### ❌ Caso de Error - Usuario no encontrado

**Request:**
```bash
POST http://localhost:8080/api/usuarios/999/cambiar-password
Content-Type: application/json

{
  "passwordActual": "password",
  "passwordNueva": "newpassword"
}
```

**Respuesta esperada:** `404 NOT FOUND`

---

## 8. DELETE - Eliminar Usuario

### ✅ Caso Exitoso - Eliminar usuario sin mascotas

**Request:**
```bash
DELETE http://localhost:8080/api/usuarios/1
```

**Respuesta esperada:** `204 NO CONTENT` (sin body)

---

### ❌ Caso de Error - Usuario tiene mascotas (PROTECCIÓN DE DATOS)

**Situación:** El usuario 1 tiene mascotas registradas

**Request:**
```bash
DELETE http://localhost:8080/api/usuarios/1
```

**Respuesta esperada:** `409 CONFLICT`
**Mensaje:** "No puedes eliminar tu cuenta mientras tengas mascotas registradas"

**Solución:** Primero elimina todas las mascotas del usuario

---

### ❌ Caso de Error - Usuario no encontrado

**Request:**
```bash
DELETE http://localhost:8080/api/usuarios/999
```

**Respuesta esperada:** `404 NOT FOUND`

---

## Script de PowerShell para Pruebas Automatizadas

```powershell
# Variables
$baseUrl = "http://localhost:8080/api/usuarios"
$contador = 0

function Test-Endpoint {
    param(
        [string]$nombre,
        [string]$metodo,
        [string]$uri,
        [object]$body,
        [int]$codigoEsperado
    )
    
    $contador++
    Write-Host "`n[$contador] Probando: $nombre" -ForegroundColor Cyan
    
    try {
        if ($body) {
            $response = Invoke-RestMethod -Uri $uri -Method $metodo -Body ($body | ConvertTo-Json) -ContentType "application/json"
        } else {
            $response = Invoke-RestMethod -Uri $uri -Method $metodo
        }
        
        Write-Host "✅ Exitoso - Respuesta:" -ForegroundColor Green
        Write-Host ($response | ConvertTo-Json -Depth 2)
    } catch {
        $statusCode = $_.Exception.Response.StatusCode.Value__
        Write-Host "⚠️  Error $statusCode" -ForegroundColor Yellow
        Write-Host $_.Exception.Message
    }
}

# 1. Registrar usuario
$usuarioNuevo = @{
    nombre = "Juan Pérez"
    email = "juan@example.com"
    password = "miPassword123"
    telefono = "+34 612 345 678"
    latitudCasa = -34.6037
    longitudCasa = -58.3816
}
Test-Endpoint -nombre "Registrar nuevo usuario" -metodo "Post" `
    -uri "$baseUrl/registro" -body $usuarioNuevo -codigoEsperado 201

# 2. Obtener todos
Test-Endpoint -nombre "Obtener todos los usuarios" -metodo "Get" `
    -uri $baseUrl -codigoEsperado 200

# 3. Obtener por ID
Test-Endpoint -nombre "Obtener usuario por ID" -metodo "Get" `
    -uri "$baseUrl/1" -codigoEsperado 200

# 4. Obtener por email
Test-Endpoint -nombre "Obtener usuario por email" -metodo "Get" `
    -uri "$baseUrl/email/juan@example.com" -codigoEsperado 200

# 5. Buscar por nombre
Test-Endpoint -nombre "Buscar usuario por nombre" -metodo "Get" `
    -uri "$baseUrl/buscar?nombre=Juan" -codigoEsperado 200

# 6. Actualizar perfil
$actualizacion = @{
    nombre = "Juan Pérez Actualizado"
    telefono = "+34 612 999 999"
}
Test-Endpoint -nombre "Actualizar perfil del usuario" -metodo "Put" `
    -uri "$baseUrl/1" -body $actualizacion -codigoEsperado 200

# 7. Cambiar contraseña
$cambioPassword = @{
    passwordActual = "miPassword123"
    passwordNueva = "nuevoPassword456"
}
Test-Endpoint -nombre "Cambiar contraseña" -metodo "Post" `
    -uri "$baseUrl/1/cambiar-password" -body $cambioPassword -codigoEsperado 204

# 8. Intentar eliminar con mascotas (debe fallar)
Write-Host "`n[$contador+1] Intentando eliminar usuario con mascotas..." -ForegroundColor Red
try {
    Invoke-RestMethod -Uri "$baseUrl/1" -Method Delete
} catch {
    Write-Host "⚠️  Error esperado (usuario tiene mascotas)" -ForegroundColor Yellow
}

Write-Host "`n✅ Todas las pruebas completadas!" -ForegroundColor Green
```

---

## Checklist de Verificación

- [ ] Registro con datos válidos funciona
- [ ] No se puede registrar email duplicado
- [ ] Email con formato inválido rechazado
- [ ] Contraseña débil rechazada
- [ ] Se obtienen todos los usuarios
- [ ] Se obtiene usuario por ID
- [ ] Se obtiene usuario por email
- [ ] Búsqueda por nombre funciona (case-insensitive)
- [ ] Se actualiza perfil correctamente
- [ ] No se puede cambiar email por actualización
- [ ] No se puede cambiar password por actualización
- [ ] Cambio de password funciona con contraseña correcta
- [ ] Cambio de password rechaza contraseña incorrecta
- [ ] Se puede eliminar usuario sin mascotas
- [ ] No se puede eliminar usuario con mascotas
- [ ] Códigos de estado HTTP son correctos

---

**Versión:** 1.0  
**Última actualización:** 2026-02-24

