# 🧪 GUÍA COMPLETA DE PRUEBAS EN POSTMAN

## 📋 Índice
1. [Configuración Inicial](#configuración-inicial)
2. [Pruebas de Usuarios](#1-pruebas-de-usuarios)
3. [Pruebas de Mascotas](#2-pruebas-de-mascotas)
4. [Pruebas de Publicaciones](#3-pruebas-de-publicaciones)
5. [Validación de Bean Validation](#4-validación-de-bean-validation)
6. [Pruebas de Seguridad](#5-pruebas-de-seguridad)

---

## Configuración Inicial

### Base URL
```
http://localhost:8080
```

### Headers comunes
```
Content-Type: application/json
```

---

## 1. PRUEBAS DE USUARIOS

### ✅ 1.1 Crear Usuario (Registro)
**Endpoint:** `POST /api/usuarios/registro`

**Body (JSON):**
```json
{
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "password": "password123",
  "telefono": "2494123456",
  "latitudCasa": -37.3217,
  "longitudCasa": -59.1331
}
```

**Respuesta esperada:** `201 Created`
```json
{
  "id": 1,
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "telefono": "2494123456",
  "latitudCasa": -37.3217,
  "longitudCasa": -59.1331,
  "mascotas": []
}
```

**Nota:** Guarda el `id` del usuario creado para las siguientes pruebas.

---

### ✅ 1.2 Listar Todos los Usuarios
**Endpoint:** `GET /api/usuarios`

**Respuesta esperada:** `200 OK`
```json
[
  {
    "id": 1,
    "nombre": "Juan Pérez",
    "email": "juan@example.com",
    "telefono": "2494123456",
    "latitudCasa": -37.3217,
    "longitudCasa": -59.1331,
    "mascotas": []
  }
]
```

---

### ✅ 1.3 Obtener Usuario por ID
**Endpoint:** `GET /api/usuarios/{id}`

**Ejemplo:** `GET /api/usuarios/1`

**Respuesta esperada:** `200 OK`
```json
{
  "id": 1,
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "telefono": "2494123456",
  "latitudCasa": -37.3217,
  "longitudCasa": -59.1331,
  "mascotas": []
}
```

---

### ✅ 1.4 Obtener Usuario por Email
**Endpoint:** `GET /api/usuarios/email/{email}`

**Ejemplo:** `GET /api/usuarios/email/juan@example.com`

**Respuesta esperada:** `200 OK`

---

### ✅ 1.5 Buscar Usuarios por Nombre
**Endpoint:** `GET /api/usuarios/buscar?nombre={nombre}`

**Ejemplo:** `GET /api/usuarios/buscar?nombre=Juan`

**Respuesta esperada:** `200 OK` con lista de usuarios que coincidan.

---

### ✅ 1.6 Actualizar Usuario
**Endpoint:** `PUT /api/usuarios/{usuarioId}`

**Ejemplo:** `PUT /api/usuarios/1`

**Body (JSON):**
```json
{
  "nombre": "Juan Pérez Actualizado",
  "telefono": "2494999999",
  "latitudCasa": -37.3200,
  "longitudCasa": -59.1300
}
```

**Respuesta esperada:** `200 OK`

---

### ✅ 1.7 Cambiar Contraseña
**Endpoint:** `POST /api/usuarios/{usuarioId}/cambiar-password`

**Ejemplo:** `POST /api/usuarios/1/cambiar-password`

**Body (JSON):**
```json
{
  "passwordActual": "password123",
  "passwordNueva": "newPassword456"
}
```

**Respuesta esperada:** `204 No Content`

---

### ✅ 1.8 Eliminar Usuario (sin mascotas)
**Endpoint:** `DELETE /api/usuarios/{usuarioId}`

**Ejemplo:** `DELETE /api/usuarios/1`

**Respuesta esperada:** `204 No Content`

**Nota:** Solo funciona si el usuario NO tiene mascotas registradas.

---

## 2. PRUEBAS DE MASCOTAS

### ✅ 2.1 Crear Mascota
**Endpoint:** `POST /api/mascotas/usuario/{usuarioId}`

**Ejemplo:** `POST /api/mascotas/usuario/1`

**Body (JSON):**
```json
{
  "nombre": "Rex",
  "especie": "Perro",
  "raza": "Labrador",
  "fotoUrl": "https://example.com/rex.jpg"
}
```

**Respuesta esperada:** `201 Created`
```json
{
  "id": 1,
  "nombre": "Rex",
  "especie": "Perro",
  "raza": "Labrador",
  "fotoUrl": "https://example.com/rex.jpg",
  "duenio": {
    "id": 1
  },
  "historialPublicaciones": []
}
```

**Nota:** Guarda el `id` de la mascota para las siguientes pruebas.

---

### ✅ 2.2 Listar Todas las Mascotas
**Endpoint:** `GET /api/mascotas`

**Respuesta esperada:** `200 OK`

---

### ✅ 2.3 Obtener Mascota por ID
**Endpoint:** `GET /api/mascotas/{id}`

**Ejemplo:** `GET /api/mascotas/1`

**Respuesta esperada:** `200 OK`

---

### ✅ 2.4 Listar Mascotas de un Usuario
**Endpoint:** `GET /api/mascotas/usuario/{usuarioId}`

**Ejemplo:** `GET /api/mascotas/usuario/1`

**Respuesta esperada:** `200 OK` con lista de mascotas del usuario.

---

### ✅ 2.5 Buscar Mascotas por Nombre
**Endpoint:** `GET /api/mascotas/buscar?nombre={nombre}`

**Ejemplo:** `GET /api/mascotas/buscar?nombre=Rex`

**Respuesta esperada:** `200 OK`

---

### ✅ 2.6 Buscar Mascotas de Usuario por Nombre
**Endpoint:** `GET /api/mascotas/usuario/{usuarioId}/buscar?nombre={nombre}`

**Ejemplo:** `GET /api/mascotas/usuario/1/buscar?nombre=Rex`

**Respuesta esperada:** `200 OK`

---

### ✅ 2.7 Actualizar Mascota
**Endpoint:** `PUT /api/mascotas/{mascotaId}/usuario/{usuarioId}`

**Ejemplo:** `PUT /api/mascotas/1/usuario/1`

**Body (JSON):**
```json
{
  "nombre": "Rex Actualizado",
  "especie": "Perro",
  "raza": "Labrador Golden",
  "fotoUrl": "https://example.com/rex-new.jpg"
}
```

**Respuesta esperada:** `200 OK`

**Nota:** Solo el dueño puede actualizar su mascota.

---

### ✅ 2.8 Eliminar Mascota
**Endpoint:** `DELETE /api/mascotas/{mascotaId}/usuario/{usuarioId}`

**Ejemplo:** `DELETE /api/mascotas/1/usuario/1`

**Respuesta esperada:** `204 No Content`

**Nota:** Solo el dueño puede eliminar su mascota.

---

## 3. PRUEBAS DE PUBLICACIONES

### ✅ 3.1 Crear Publicación PERDIDA
**Endpoint:** `POST /api/publicaciones?usuarioId={usuarioId}&mascotaId={mascotaId}`

**Ejemplo:** `POST /api/publicaciones?usuarioId=1&mascotaId=1`

**Body (JSON):**
```json
{
  "titulo": "Se perdió Rex en zona centro",
  "descripcion": "Perro labrador color dorado, muy amigable. Responde al nombre de Rex. Última vez visto cerca del parque central.",
  "tipo": "PERDIDA",
  "latitudReporte": -37.3217,
  "longitudReporte": -59.1331
}
```

**Respuesta esperada:** `201 Created`
```json
{
  "id": 1,
  "titulo": "Se perdió Rex en zona centro",
  "descripcion": "Perro labrador color dorado, muy amigable. Responde al nombre de Rex. Última vez visto cerca del parque central.",
  "tipo": "PERDIDA",
  "latitudReporte": -37.3217,
  "longitudReporte": -59.1331,
  "fechaCarga": "2026-02-26T20:40:00"
}
```

**⚠️ IMPORTANTE:** Solo se puede tener UNA publicación PERDIDA activa por mascota.

---

### ✅ 3.2 Crear Publicación ENCONTRADA
**Endpoint:** `POST /api/publicaciones?usuarioId={usuarioId}&mascotaId={mascotaId}`

**Body (JSON):**
```json
{
  "titulo": "Mascota encontrada en el parque",
  "descripcion": "Encontré este perro labrador cerca del parque. Parece perdido y muy amigable.",
  "tipo": "ENCONTRADA",
  "latitudReporte": -37.3200,
  "longitudReporte": -59.1300
}
```

**Respuesta esperada:** `201 Created`

---

### ✅ 3.3 Listar Todas las Publicaciones
**Endpoint:** `GET /api/publicaciones`

**Respuesta esperada:** `200 OK`

---

### ✅ 3.4 Obtener Publicación por ID
**Endpoint:** `GET /api/publicaciones/{id}`

**Ejemplo:** `GET /api/publicaciones/1`

**Respuesta esperada:** `200 OK`

---

### ✅ 3.5 Obtener Publicaciones por Tipo
**Endpoint:** `GET /api/publicaciones/tipo/{tipo}`

**Ejemplos:**
- `GET /api/publicaciones/tipo/PERDIDA`
- `GET /api/publicaciones/tipo/ENCONTRADA`

**Respuesta esperada:** `200 OK`

---

### ✅ 3.6 Obtener Publicaciones de un Autor
**Endpoint:** `GET /api/publicaciones/autor/{usuarioId}`

**Ejemplo:** `GET /api/publicaciones/autor/1`

**Respuesta esperada:** `200 OK`

---

### ✅ 3.7 Buscar Mascotas Cercanas
**Endpoint:** `GET /api/publicaciones/cercanas?latitud={lat}&longitud={lon}&radioKm={radio}`

**Ejemplo:** `GET /api/publicaciones/cercanas?latitud=-37.3217&longitud=-59.1331&radioKm=5`

**Respuesta esperada:** `200 OK`
```json
[
  {
    "titulo": "Se perdió Rex en zona centro",
    "nombreMascota": "Rex",
    "especie": "Perro",
    "fotoUrl": "https://example.com/rex.jpg",
    "descripcion": "Perro labrador color dorado...",
    "latitud": -37.3217,
    "longitud": -59.1331,
    "distanciaKm": 0.5
  }
]
```

---

### ✅ 3.8 Obtener Mascotas en el Mapa (Tandil)
**Endpoint:** `GET /api/publicaciones/mapa`

**Respuesta esperada:** `200 OK` (busca en radio de 10km desde centro de Tandil)

---

### ✅ 3.9 Actualizar Publicación
**Endpoint:** `PUT /api/publicaciones/{id}`

**Ejemplo:** `PUT /api/publicaciones/1`

**Body (JSON):**
```json
{
  "titulo": "Actualización: Rex encontrado!",
  "descripcion": "Gracias a todos por su ayuda!",
  "tipo": "PERDIDA",
  "latitudReporte": -37.3217,
  "longitudReporte": -59.1331
}
```

**Respuesta esperada:** `200 OK`

---

### ✅ 3.10 Eliminar Publicación
**Endpoint:** `DELETE /api/publicaciones/{id}`

**Ejemplo:** `DELETE /api/publicaciones/1`

**Respuesta esperada:** `204 No Content`

---

## 4. VALIDACIÓN DE BEAN VALIDATION

### ❌ 4.1 Intentar Crear Usuario sin Email
**Endpoint:** `POST /api/usuarios/registro`

**Body (JSON):**
```json
{
  "nombre": "Usuario Sin Email",
  "password": "password123"
}
```

**Respuesta esperada:** `400 Bad Request`
```json
{
  "timestamp": "2026-02-26T20:40:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Errores de validacion: email: El email del usuario es obligatorio",
  "path": "/api/usuarios/registro"
}
```

---

### ❌ 4.2 Intentar Crear Usuario con Email Inválido
**Body (JSON):**
```json
{
  "nombre": "Usuario Test",
  "email": "email-invalido",
  "password": "password123"
}
```

**Respuesta esperada:** `400 Bad Request`
```json
{
  "message": "Errores de validacion: email: El formato del email es invalido"
}
```

---

### ❌ 4.3 Intentar Crear Usuario con Contraseña Corta
**Body (JSON):**
```json
{
  "nombre": "Usuario Test",
  "email": "test@example.com",
  "password": "123"
}
```

**Respuesta esperada:** `400 Bad Request`
```json
{
  "message": "Errores de validacion: password: La contrasena debe tener entre 6 y 100 caracteres"
}
```

---

### ❌ 4.4 Intentar Crear Mascota sin Nombre
**Endpoint:** `POST /api/mascotas/usuario/1`

**Body (JSON):**
```json
{
  "especie": "Perro",
  "raza": "Labrador"
}
```

**Respuesta esperada:** `400 Bad Request`
```json
{
  "message": "Errores de validacion: nombre: El nombre de la mascota es obligatorio"
}
```

---

### ❌ 4.5 Intentar Crear Publicación con Descripción Corta
**Endpoint:** `POST /api/publicaciones?usuarioId=1&mascotaId=1`

**Body (JSON):**
```json
{
  "titulo": "Test",
  "descripcion": "Corta",
  "tipo": "PERDIDA",
  "latitudReporte": -37.3217,
  "longitudReporte": -59.1331
}
```

**Respuesta esperada:** `400 Bad Request`
```json
{
  "message": "Errores de validacion: descripcion: La descripcion debe tener entre 10 y 1000 caracteres"
}
```

---

### ❌ 4.6 Intentar Crear Publicación con Coordenadas Inválidas
**Body (JSON):**
```json
{
  "titulo": "Mascota perdida",
  "descripcion": "Descripción larga suficiente para validar",
  "tipo": "PERDIDA",
  "latitudReporte": 999,
  "longitudReporte": -59.1331
}
```

**Respuesta esperada:** `400 Bad Request`
```json
{
  "message": "Errores de validacion: latitudReporte: Latitud fuera de rango"
}
```

---

## 5. PRUEBAS DE SEGURIDAD

### ❌ 5.1 Intentar Crear Usuario con Email Duplicado
**Endpoint:** `POST /api/usuarios/registro`

**Body (JSON):**
```json
{
  "nombre": "Otro Usuario",
  "email": "juan@example.com",
  "password": "password123"
}
```

**Respuesta esperada:** `409 Conflict`
```json
{
  "status": 409,
  "error": "Conflict",
  "message": "El email ya esta registrado: juan@example.com"
}
```

---

### ❌ 5.2 Intentar Actualizar Mascota de Otro Usuario
**Endpoint:** `PUT /api/mascotas/1/usuario/999`

**Body (JSON):**
```json
{
  "nombre": "Intento Hackeo",
  "especie": "Perro"
}
```

**Respuesta esperada:** `403 Forbidden`
```json
{
  "status": 403,
  "error": "Forbidden",
  "message": "No tienes permiso para actualizar esta mascota"
}
```

---

### ❌ 5.3 Intentar Crear Publicación con Mascota de Otro Usuario
**Endpoint:** `POST /api/publicaciones?usuarioId=999&mascotaId=1`

**Body (JSON):**
```json
{
  "titulo": "Intento de publicación fraudulenta",
  "descripcion": "Intentando publicar mascota que no me pertenece",
  "tipo": "PERDIDA",
  "latitudReporte": -37.3217,
  "longitudReporte": -59.1331
}
```

**Respuesta esperada:** `403 Forbidden`
```json
{
  "status": 403,
  "error": "Forbidden",
  "message": "La mascota no pertenece al usuario indicado"
}
```

---

### ❌ 5.4 Intentar Crear Segunda Publicación PERDIDA para la Misma Mascota
**Endpoint:** `POST /api/publicaciones?usuarioId=1&mascotaId=1`

**Body (JSON):**
```json
{
  "titulo": "Segunda publicación perdida",
  "descripcion": "Intentando duplicar publicación de mascota perdida",
  "tipo": "PERDIDA",
  "latitudReporte": -37.3217,
  "longitudReporte": -59.1331
}
```

**Respuesta esperada:** `409 Conflict`
```json
{
  "status": 409,
  "error": "Conflict",
  "message": "Ya existe una publicacion PERDIDA para esta mascota"
}
```

---

### ❌ 5.5 Intentar Obtener Usuario No Existente
**Endpoint:** `GET /api/usuarios/999999`

**Respuesta esperada:** `404 Not Found`
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Usuario no encontrado con ID: 999999"
}
```

---

### ❌ 5.6 Intentar Cambiar Contraseña con Contraseña Incorrecta
**Endpoint:** `POST /api/usuarios/1/cambiar-password`

**Body (JSON):**
```json
{
  "passwordActual": "passwordIncorrecta",
  "passwordNueva": "nuevaPassword123"
}
```

**Respuesta esperada:** `401 Unauthorized`
```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "La contraseña actual es incorrecta"
}
```

---

### ❌ 5.7 Intentar Eliminar Usuario con Mascotas
**Endpoint:** `DELETE /api/usuarios/1`

**Respuesta esperada:** `409 Conflict`
```json
{
  "status": 409,
  "error": "Conflict",
  "message": "No puedes eliminar tu cuenta mientras tengas mascotas registradas"
}
```

---

## 6. FLUJO DE PRUEBA COMPLETO RECOMENDADO

### Paso 1: Crear Usuarios
1. Crear Usuario 1 (dueño)
2. Crear Usuario 2 (vecino)

### Paso 2: Crear Mascotas
1. Usuario 1 crea Mascota 1
2. Usuario 1 crea Mascota 2

### Paso 3: Crear Publicaciones
1. Usuario 1 crea publicación PERDIDA para Mascota 1
2. Intentar crear otra publicación PERDIDA (debería fallar)
3. Usuario 2 crea publicación ENCONTRADA para Mascota 1

### Paso 4: Probar Búsquedas
1. Buscar mascotas cercanas
2. Buscar por tipo PERDIDA
3. Buscar publicaciones del Usuario 1

### Paso 5: Probar Actualizaciones
1. Usuario 1 actualiza sus datos
2. Usuario 1 actualiza Mascota 1
3. Usuario 1 actualiza Publicación 1

### Paso 6: Probar Seguridad
1. Usuario 2 intenta actualizar Mascota de Usuario 1 (debe fallar)
2. Usuario 2 intenta eliminar Mascota de Usuario 1 (debe fallar)
3. Intentar crear usuario con email duplicado (debe fallar)

### Paso 7: Limpiar
1. Eliminar publicaciones
2. Eliminar mascotas
3. Eliminar usuarios

---

## 📌 NOTAS IMPORTANTES

1. **Bean Validation**: Todos los campos obligatorios están validados automáticamente.
2. **GlobalExceptionHandler**: Todas las excepciones devuelven JSON estructurado.
3. **Seguridad**: 
   - Solo el dueño puede modificar/eliminar sus mascotas
   - Solo se puede publicar mascotas propias
   - Solo una publicación PERDIDA por mascota
4. **IDs**: Los IDs son auto-incrementales, guárdalos al crear recursos.

---

## 🎯 CÓDIGOS DE ESTADO HTTP

- **200 OK**: Operación exitosa (GET, PUT)
- **201 Created**: Recurso creado exitosamente (POST)
- **204 No Content**: Operación exitosa sin contenido (DELETE)
- **400 Bad Request**: Error de validación
- **401 Unauthorized**: Credenciales incorrectas
- **403 Forbidden**: Sin permisos
- **404 Not Found**: Recurso no encontrado
- **409 Conflict**: Conflicto (email duplicado, publicación duplicada, etc.)
- **500 Internal Server Error**: Error del servidor

---

## ✅ CHECKLIST DE PRUEBAS

### Usuarios
- [ ] Crear usuario válido
- [ ] Crear usuario con email inválido (debe fallar)
- [ ] Crear usuario con email duplicado (debe fallar)
- [ ] Listar usuarios
- [ ] Obtener usuario por ID
- [ ] Obtener usuario por email
- [ ] Actualizar usuario
- [ ] Cambiar contraseña
- [ ] Eliminar usuario sin mascotas
- [ ] Intentar eliminar usuario con mascotas (debe fallar)

### Mascotas
- [ ] Crear mascota válida
- [ ] Crear mascota sin nombre (debe fallar)
- [ ] Listar todas las mascotas
- [ ] Obtener mascota por ID
- [ ] Listar mascotas de un usuario
- [ ] Buscar mascotas por nombre
- [ ] Actualizar mascota propia
- [ ] Intentar actualizar mascota ajena (debe fallar)
- [ ] Eliminar mascota propia
- [ ] Intentar eliminar mascota ajena (debe fallar)

### Publicaciones
- [ ] Crear publicación PERDIDA válida
- [ ] Crear publicación ENCONTRADA válida
- [ ] Intentar crear segunda PERDIDA para misma mascota (debe fallar)
- [ ] Crear publicación con descripción corta (debe fallar)
- [ ] Intentar crear publicación con mascota ajena (debe fallar)
- [ ] Listar todas las publicaciones
- [ ] Obtener publicación por ID
- [ ] Filtrar por tipo PERDIDA
- [ ] Filtrar por tipo ENCONTRADA
- [ ] Obtener publicaciones de un autor
- [ ] Buscar mascotas cercanas
- [ ] Actualizar publicación
- [ ] Eliminar publicación

---

¡Listo! Con esta guía puedes probar toda la funcionalidad de tu API. 🚀

