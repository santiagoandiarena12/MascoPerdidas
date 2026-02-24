# API REST - CRUD de Usuarios

## Documentación Completa de Endpoints

Esta API permite gestionar usuarios con verificaciones de seguridad para proteger datos sensibles y mantener la integridad de la base de datos.

---

## 📋 Tabla de Contenidos

1. [Endpoints de Registro y Lectura](#endpoints-de-registro-y-lectura)
2. [Endpoints de Actualización](#endpoints-de-actualización)
3. [Endpoints de Eliminación](#endpoints-de-eliminación)
4. [Códigos de Estado HTTP](#códigos-de-estado-http)
5. [Validaciones de Seguridad](#validaciones-de-seguridad)
6. [Ejemplos de Uso](#ejemplos-de-uso)
7. [Errores Comunes](#errores-comunes)

---

## Endpoints de Registro y Lectura

### 1. **POST /api/usuarios/registro** - Registrar nuevo usuario

**Descripción:** Crea un nuevo usuario en el sistema. Requiere email único y contraseña segura.

**Método:** `POST`  
**URL:** `http://localhost:8080/api/usuarios/registro`

**Headers requeridos:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "password": "miPassword123",
  "telefono": "+34 612 345 678",
  "latitudCasa": -34.6037,
  "longitudCasa": -58.3816
}
```

**Campos:**
| Campo | Tipo | Obligatorio | Restricciones |
|-------|------|-------------|---------------|
| nombre | String | Sí | Max 100 caracteres, no vacío |
| email | String | Sí | Email válido, único, max 100 caracteres |
| password | String | Sí | Min 6 caracteres, max 100 caracteres |
| telefono | String | No | - |
| latitudCasa | Double | No | Coordenada válida |
| longitudCasa | Double | No | Coordenada válida |

**Respuesta exitosa (201 CREATED):**
```json
{
  "id": 5,
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "password": "miPassword123",
  "telefono": "+34 612 345 678",
  "latitudCasa": -34.6037,
  "longitudCasa": -58.3816,
  "mascotas": []
}
```

**Respuestas de error:**
- `400 Bad Request` - Datos inválidos (email duplicado, formato inválido, contraseña débil, etc.)

**Validaciones:**
- ✅ Email no puede estar vacío
- ✅ Email debe ser válido (formato: usuario@dominio.com)
- ✅ Email debe ser único en la base de datos
- ✅ Nombre obligatorio, máximo 100 caracteres
- ✅ Contraseña mínimo 6 caracteres, máximo 100
- ✅ No puede haber campos vacíos en datos obligatorios

---

### 2. **GET /api/usuarios** - Obtener todos los usuarios

**Descripción:** Retorna una lista con todos los usuarios registrados en el sistema.

**Método:** `GET`  
**URL:** `http://localhost:8080/api/usuarios`

**Respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
    "nombre": "María García",
    "email": "maria@example.com",
    "password": "pass123456",
    "telefono": "+34 612 111 111",
    "latitudCasa": -34.6037,
    "longitudCasa": -58.3816,
    "mascotas": []
  },
  {
    "id": 2,
    "nombre": "Carlos López",
    "email": "carlos@example.com",
    "password": "pass654321",
    "telefono": "+34 612 222 222",
    "latitudCasa": -34.6037,
    "longitudCasa": -58.3816,
    "mascotas": []
  }
]
```

---

### 3. **GET /api/usuarios/{id}** - Obtener usuario por ID

**Descripción:** Retorna un usuario específico según su ID.

**Método:** `GET`  
**URL:** `http://localhost:8080/api/usuarios/1`

**Parámetros:**
| Parámetro | Tipo | Ubicación | Descripción |
|-----------|------|-----------|-------------|
| id | Long | Path | ID del usuario (debe ser > 0) |

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "nombre": "María García",
  "email": "maria@example.com",
  "password": "pass123456",
  "telefono": "+34 612 111 111",
  "latitudCasa": -34.6037,
  "longitudCasa": -58.3816,
  "mascotas": []
}
```

**Respuestas de error:**
- `400 Bad Request` - ID inválido (≤ 0)
- `404 Not Found` - Usuario no encontrado

---

### 4. **GET /api/usuarios/email/{email}** - Obtener usuario por email

**Descripción:** Busca y retorna un usuario específico por su email.

**Método:** `GET`  
**URL:** `http://localhost:8080/api/usuarios/email/maria@example.com`

**Parámetros:**
| Parámetro | Tipo | Ubicación | Descripción |
|-----------|------|-----------|-------------|
| email | String | Path | Email del usuario a buscar |

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "nombre": "María García",
  "email": "maria@example.com",
  "password": "pass123456",
  "telefono": "+34 612 111 111",
  "latitudCasa": -34.6037,
  "longitudCasa": -58.3816,
  "mascotas": []
}
```

**Respuestas de error:**
- `404 Not Found` - Usuario con ese email no existe

---

### 5. **GET /api/usuarios/buscar?nombre={nombre}** - Buscar usuarios por nombre

**Descripción:** Busca usuarios cuyos nombres contengan el texto proporcionado (case-insensitive).

**Método:** `GET`  
**URL:** `http://localhost:8080/api/usuarios/buscar?nombre=María`

**Parámetros:**
| Parámetro | Tipo | Ubicación | Descripción |
|-----------|------|-----------|-------------|
| nombre | String | Query | Nombre o parte del nombre a buscar |

**Respuesta exitosa (200 OK):**
```json
[
  {
    "id": 1,
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

**Respuestas de error:**
- `400 Bad Request` - Nombre vacío o nulo

---

## Endpoints de Actualización

### 6. **PUT /api/usuarios/{usuarioId}** - Actualizar perfil del usuario

**Descripción:** Actualiza los datos del usuario. Solo se pueden cambiar: nombre, teléfono y coordenadas. Email y contraseña tienen endpoints separados.

**Método:** `PUT`  
**URL:** `http://localhost:8080/api/usuarios/1`

**Parámetros Path:**
| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| usuarioId | Long | ID del usuario a actualizar |

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "nombre": "María García Actualizado",
  "telefono": "+34 612 999 999",
  "latitudCasa": -34.5997,
  "longitudCasa": -58.3756
}
```

**Campos opcionales:**
| Campo | Tipo | Restricciones |
|-------|------|---------------|
| nombre | String | Max 100 caracteres |
| telefono | String | - |
| latitudCasa | Double | - |
| longitudCasa | Double | - |

**Respuesta exitosa (200 OK):**
```json
{
  "id": 1,
  "nombre": "María García Actualizado",
  "email": "maria@example.com",
  "password": "pass123456",
  "telefono": "+34 612 999 999",
  "latitudCasa": -34.5997,
  "longitudCasa": -58.3756,
  "mascotas": []
}
```

**Respuestas de error:**
- `400 Bad Request` - Datos inválidos (intento de cambiar email/password, nombre > 100 caracteres)
- `404 Not Found` - Usuario no encontrado

**Restricciones de seguridad:**
- ❌ No puedes cambiar el email por este endpoint
- ❌ No puedes cambiar la contraseña por este endpoint (usa el endpoint de cambiar password)

---

### 7. **POST /api/usuarios/{usuarioId}/cambiar-password** - Cambiar contraseña

**Descripción:** Cambia la contraseña del usuario. Requiere la contraseña actual para verificación.

**Método:** `POST`  
**URL:** `http://localhost:8080/api/usuarios/1/cambiar-password`

**Parámetros Path:**
| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| usuarioId | Long | ID del usuario |

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "passwordActual": "miPassword123",
  "passwordNueva": "nuevoPassword456"
}
```

**Campos requeridos:**
| Campo | Tipo | Restricciones |
|-------|------|---------------|
| passwordActual | String | Debe coincidir con la contraseña actual |
| passwordNueva | String | Min 6 caracteres, max 100 caracteres |

**Respuesta exitosa (204 NO CONTENT):**
```
(sin body)
```

**Respuestas de error:**
- `400 Bad Request` - Contraseña nueva no válida (< 6 caracteres, > 100 caracteres, campos nulos)
- `401 Unauthorized` - La contraseña actual es incorrecta
- `404 Not Found` - Usuario no encontrado

**Validaciones:**
- ✅ La contraseña actual debe ser correcta
- ✅ La contraseña nueva debe tener mínimo 6 caracteres
- ✅ La contraseña nueva no puede exceder 100 caracteres
- ✅ Ambos campos son obligatorios

---

## Endpoints de Eliminación

### 8. **DELETE /api/usuarios/{usuarioId}** - Eliminar usuario

**Descripción:** Elimina un usuario del sistema. No se puede eliminar si tiene mascotas registradas.

**Método:** `DELETE`  
**URL:** `http://localhost:8080/api/usuarios/1`

**Parámetros Path:**
| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| usuarioId | Long | ID del usuario a eliminar |

**Respuesta exitosa (204 NO CONTENT):**
```
(sin body)
```

**Respuestas de error:**
- `404 Not Found` - Usuario no encontrado
- `409 Conflict` - El usuario tiene mascotas registradas y no puede ser eliminado

**Restricciones de seguridad:**
- ❌ No se puede eliminar si tiene mascotas activas
- ℹ️ Primero debes eliminar todas las mascotas del usuario

---

## Códigos de Estado HTTP

| Código | Significado | Situación |
|--------|------------|-----------|
| 200 | OK | Operación GET/PUT exitosa |
| 201 | Created | Registro de nuevo usuario exitoso |
| 204 | No Content | Operación DELETE/cambio de password exitosa |
| 400 | Bad Request | Datos inválidos, validaciones fallidas |
| 401 | Unauthorized | Contraseña actual incorrecta en cambio de password |
| 404 | Not Found | Recurso no encontrado |
| 409 | Conflict | Usuario tiene mascotas, no puede eliminarse |

---

## Validaciones de Seguridad

### 🔒 Validaciones en Registro

- ✅ **Email único**: No puede haber dos usuarios con el mismo email
- ✅ **Email válido**: Debe seguir formato usuario@dominio.com
- ✅ **Contraseña fuerte**: Mínimo 6 caracteres
- ✅ **Nombre obligatorio**: No puede estar vacío
- ✅ **Límites de caracteres**: 
  - Nombre: máximo 100
  - Email: máximo 100
  - Contraseña: máximo 100

### 🔒 Validaciones en Actualización

- ✅ **Email protegido**: No se puede cambiar por este endpoint
- ✅ **Contraseña protegida**: Solo se puede cambiar por endpoint dedicado
- ✅ **Verificación de existencia**: El usuario debe existir

### 🔒 Validaciones en Cambio de Contraseña

- ✅ **Verificación de identidad**: Requiere contraseña actual correcta
- ✅ **Contraseña nueva fuerte**: Mínimo 6 caracteres
- ✅ **Campos obligatorios**: Ambas contraseñas necesarias

### 🔒 Validaciones en Eliminación

- ✅ **Protección de datos**: No eliminar si tiene mascotas (evita orfandad)
- ✅ **Verificación de existencia**: El usuario debe existir

---

## Ejemplos de Uso

### Con cURL

#### Registrar nuevo usuario
```bash
curl -X POST http://localhost:8080/api/usuarios/registro \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan Pérez",
    "email": "juan@example.com",
    "password": "miPassword123",
    "telefono": "+34 612 345 678",
    "latitudCasa": -34.6037,
    "longitudCasa": -58.3816
  }'
```

#### Obtener todos los usuarios
```bash
curl http://localhost:8080/api/usuarios
```

#### Obtener usuario por ID
```bash
curl http://localhost:8080/api/usuarios/1
```

#### Buscar usuario por email
```bash
curl http://localhost:8080/api/usuarios/email/juan@example.com
```

#### Buscar usuarios por nombre
```bash
curl http://localhost:8080/api/usuarios/buscar?nombre=Juan
```

#### Actualizar perfil
```bash
curl -X PUT http://localhost:8080/api/usuarios/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan Pérez Actualizado",
    "telefono": "+34 612 999 999",
    "latitudCasa": -34.5997,
    "longitudCasa": -58.3756
  }'
```

#### Cambiar contraseña
```bash
curl -X POST http://localhost:8080/api/usuarios/1/cambiar-password \
  -H "Content-Type: application/json" \
  -d '{
    "passwordActual": "miPassword123",
    "passwordNueva": "nuevoPassword456"
  }'
```

#### Eliminar usuario
```bash
curl -X DELETE http://localhost:8080/api/usuarios/1
```

---

### Con PowerShell

#### Registrar nuevo usuario
```powershell
$body = @{
    nombre = "Juan Pérez"
    email = "juan@example.com"
    password = "miPassword123"
    telefono = "+34 612 345 678"
    latitudCasa = -34.6037
    longitudCasa = -58.3816
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/usuarios/registro" `
  -Method Post `
  -Body $body `
  -ContentType "application/json"
```

#### Obtener usuario por ID
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/usuarios/1" -Method Get
```

#### Actualizar perfil
```powershell
$body = @{
    nombre = "Juan Pérez Actualizado"
    telefono = "+34 612 999 999"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/usuarios/1" `
  -Method Put `
  -Body $body `
  -ContentType "application/json"
```

#### Cambiar contraseña
```powershell
$body = @{
    passwordActual = "miPassword123"
    passwordNueva = "nuevoPassword456"
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/api/usuarios/1/cambiar-password" `
  -Method Post `
  -Body $body `
  -ContentType "application/json"
```

#### Eliminar usuario
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/usuarios/1" -Method Delete
```

---

## Errores Comunes

### ❌ Error 1: Email duplicado
```
Status: 400 Bad Request
Mensaje: "El email ya está registrado: juan@example.com"
```
**Solución:** Usa un email diferente o verifica si ya existe una cuenta con ese email.

### ❌ Error 2: Email con formato inválido
```
Status: 400 Bad Request
Mensaje: "El formato del email es inválido"
```
**Solución:** Usa un email válido con formato: usuario@dominio.com

### ❌ Error 3: Contraseña muy débil
```
Status: 400 Bad Request
Mensaje: "La contraseña debe tener al menos 6 caracteres"
```
**Solución:** Usa una contraseña con mínimo 6 caracteres.

### ❌ Error 4: Contraseña actual incorrecta
```
Status: 401 Unauthorized
Mensaje: "La contraseña actual es incorrecta"
```
**Solución:** Verifica que introduzcas la contraseña actual correctamente.

### ❌ Error 5: Usuario tiene mascotas
```
Status: 409 Conflict
Mensaje: "No puedes eliminar tu cuenta mientras tengas mascotas registradas"
```
**Solución:** Primero elimina todas las mascotas del usuario antes de eliminar la cuenta.

### ❌ Error 6: Usuario no encontrado
```
Status: 404 Not Found
```
**Solución:** Verifica que el ID o email del usuario sea correcto.

### ❌ Error 7: Intento de cambiar email
```
Status: 400 Bad Request
Mensaje: "No puedes cambiar tu email"
```
**Solución:** El email no puede modificarse. Usa otro email si es necesario crear nueva cuenta.

### ❌ Error 8: Intento de cambiar password en PUT
```
Status: 400 Bad Request
Mensaje: "Usa el endpoint de cambiar contraseña"
```
**Solución:** Usa el endpoint `/cambiar-password` para cambiar la contraseña, no el PUT de actualización.

---

## Resumen de Endpoints

| Método | Endpoint | Descripción | Código Éxito |
|--------|----------|-------------|--------------|
| POST | /api/usuarios/registro | Registrar nuevo usuario | 201 |
| GET | /api/usuarios | Listar todos los usuarios | 200 |
| GET | /api/usuarios/{id} | Obtener usuario por ID | 200 |
| GET | /api/usuarios/email/{email} | Obtener usuario por email | 200 |
| GET | /api/usuarios/buscar?nombre={nombre} | Buscar por nombre | 200 |
| PUT | /api/usuarios/{usuarioId} | Actualizar perfil | 200 |
| POST | /api/usuarios/{usuarioId}/cambiar-password | Cambiar contraseña | 204 |
| DELETE | /api/usuarios/{usuarioId} | Eliminar usuario | 204 |

---

## 📞 Soporte

Para más información o reportar problemas, consulta la documentación adicional:
- `API_MASCOTAS_DOCUMENTACION.md` - Documentación de endpoints de mascotas
- `PRUEBAS_API_MASCOTAS.md` - Casos de prueba para mascotas

---

**Última actualización:** 2026-02-24  
**Versión:** 1.0

