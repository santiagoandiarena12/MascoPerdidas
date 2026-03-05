# 📚 Documentación Completa de API REST - Buscar Pet

## Resumen General

Este documento proporciona una guía completa de todos los endpoints disponibles en la API REST de **Buscar Pet**, incluyendo operaciones CRUD para **Usuarios** y **Mascotas**.

---

## 🗂️ Índice de Documentación

1. [Visión General](#visión-general)
2. [Estructura de la API](#estructura-de-la-api)
3. [JWT y Autenticación](#jwt-y-autenticación)
4. [Sistema de Roles](#sistema-de-roles)
5. [Validaciones y Seguridad](#validaciones-y-seguridad)
6. [Usuarios - Endpoints](#usuarios---endpoints)
7. [Mascotas - Endpoints](#mascotas---endpoints)
8. [Publicaciones - Endpoints](#publicaciones---endpoints)
9. [Códigos de Estado HTTP](#códigos-de-estado-http)
10. [Ejemplos Prácticos](#ejemplos-prácticos)

---

## 🎯 Visión General

**Buscar Pet** es una plataforma para:
- 📋 Gestión de **usuarios** (registro, actualización, cambio de contraseña)
- 🐾 Gestión de **mascotas** (registro de mascotas por usuario)
- 🔍 Búsqueda de mascotas perdidas/encontradas
- 📍 Sistema de alertas por ubicación geográfica

---

## 🏗️ Estructura de la API

```
API Base: http://localhost:8080/api

├── /usuarios
│   ├── POST   /registro                    → Registrar nuevo usuario
│   ├── GET    /                            → Listar todos los usuarios
│   ├── GET    /{id}                        → Obtener usuario por ID
│   ├── GET    /email/{email}               → Obtener usuario por email
│   ├── GET    /buscar?nombre={nombre}      → Buscar usuarios por nombre
│   ├── PUT    /{usuarioId}                 → Actualizar perfil
│   ├── POST   /{usuarioId}/cambiar-password → Cambiar contraseña
│   ├── PATCH  /{usuarioId}/rol            → Cambiar rol (ADMIN/USER)
│   └── DELETE /{usuarioId}                 → Eliminar usuario
│
├── /mascotas
│   ├── POST   /usuario/{usuarioId}                     → Crear mascota
│   ├── GET    /                                        → Listar todas
│   ├── GET    /{id}                                    → Obtener por ID
│   ├── GET    /usuario/{usuarioId}                     → Mascotas de un usuario
│   ├── GET    /buscar?nombre={nombre}                  → Búsqueda general
│   ├── GET    /usuario/{usuarioId}/buscar?nombre={...} → Búsqueda por usuario
│   ├── PUT    /{mascotaId}/usuario/{usuarioId}         → Actualizar (seguro)
│   └── DELETE /{mascotaId}/usuario/{usuarioId}         → Eliminar (seguro)
│
└── /publicaciones
    ├── POST   /?usuarioId={id}&mascotaId={id}          → Crear publicación
    ├── GET    /                                        → Listar todas
    ├── GET    /{id}                                    → Obtener por ID
    ├── GET    /tipo/{tipo}                             → Obtener por tipo (PERDIDO/ENCONTRADO)
    ├── GET    /autor/{usuarioId}                       → Obtener por autor
    ├── GET    /cercanas?latitud={lat}&longitud={lon}&radioKm={km} → Buscar cercanas
    ├── PUT    /{id}                                    → Actualizar publicación
    └── DELETE /{id}                                    → Eliminar publicación
```

---

## 🔐 JWT y Autenticación

### ¿Qué es JWT?
JWT (JSON Web Token) es un sistema de autenticación basado en tokens que permite autenticar usuarios de forma segura y sin mantener sesiones en el servidor.

### Flujo de Autenticación

#### 1. Login - Obtener Token
```
POST /api/auth/login
Content-Type: application/json

{
  "email": "usuario@example.com",
  "password": "password123"
}

Respuesta: 200 OK
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c3VhcmlvQGV4YW1wbGUuY29tIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3MDk1ODAwMDAsImV4cCI6MTcwOTY2NjQwMH0...",
  "role": "USER",
  "email": "usuario@example.com"
}
```

#### 2. Usar el Token en Peticiones Protegidas
Para cualquier endpoint protegido, incluye el token en el header:

```
Authorization: Bearer {tu_token}
```

**Ejemplo con cURL:**
```bash
curl -X POST http://localhost:8080/api/mascotas/usuario/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..." \
  -H "Content-Type: application/json" \
  -d '{"nombre": "Firulais", "especie": "Perro"}'
```

**Ejemplo con Postman:**
1. Ve a la pestaña "Authorization"
2. Selecciona "Bearer Token"
3. Pega tu token en el campo

### Endpoints Públicos (NO requieren token)
- ✅ `POST /api/auth/login` - Login
- ✅ `POST /api/usuarios/registro` - Registro de nuevo usuario
- ✅ `GET /api/publicaciones/mapa` - Ver el mapa de mascotas
- ✅ `GET /api/mascotas/**` - Ver todas las mascotas (cualquier GET)

### Endpoints Protegidos (SÍ requieren token)
- 🔒 Todos los `POST`, `PUT`, `PATCH`, `DELETE` de cualquier recurso
- 🔒 `GET /api/usuarios/**` - Consultar usuarios
- 🔒 `POST /api/publicaciones` - Crear publicaciones

### Duración del Token
- **Validez:** 24 horas
- Después de ese tiempo, deberás hacer login nuevamente

---

## 👥 Sistema de Roles

### Roles Disponibles

#### 🙋 USER (Usuario Normal)
- **Asignación:** Automática al registrarse
- **Permisos actuales:** Puede realizar todas las operaciones
- **Permisos futuros:** Se restringirán algunas operaciones administrativas

#### 👑 ADMIN (Administrador)
- **Asignación:** Manual (ver sección de migración)
- **Permisos actuales:** Puede realizar todas las operaciones
- **Permisos futuros:** Acceso total sin restricciones

### Permisos Actuales (Temporales)
```
┌─────────────────────────┬──────┬────────┐
│ Operación               │ USER │ ADMIN  │
├─────────────────────────┼──────┼────────┤
│ Ver mascotas públicas   │  ✅  │   ✅   │
│ Registrarse             │  ✅  │   ✅   │
│ Login                   │  ✅  │   ✅   │
│ Crear mascotas          │  ✅  │   ✅   │
│ Actualizar sus mascotas │  ✅  │   ✅   │
│ Crear publicaciones     │  ✅  │   ✅   │
│ Ver todos los usuarios  │  ✅  │   ✅   │
│ Cambiar rol de usuarios │  ✅  │   ✅   │
│ Eliminar cualquier dato │  ✅  │   ✅   │
└─────────────────────────┴──────┴────────┘
```

### Permisos Futuros (Planeados)
```
┌─────────────────────────┬──────┬────────┐
│ Operación               │ USER │ ADMIN  │
├─────────────────────────┼──────┼────────┤
│ Ver mascotas públicas   │  ✅  │   ✅   │
│ Registrarse             │  ✅  │   ✅   │
│ Login                   │  ✅  │   ✅   │
│ Crear mascotas          │  ✅  │   ✅   │
│ Actualizar sus mascotas │  ✅  │   ✅   │
│ Crear publicaciones     │  ✅  │   ✅   │
│ Ver todos los usuarios  │  ❌  │   ✅   │
│ Cambiar rol de usuarios │  ❌  │   ✅   │
│ Eliminar otros usuarios │  ❌  │   ✅   │
│ Eliminar publicaciones  │  ❌  │   ✅   │
│ Moderación de contenido │  ❌  │   ✅   │
└─────────────────────────┴──────┴────────┘
```

### Cómo Crear un Usuario ADMIN

**Opción 1: Desde la Base de Datos**
```sql
-- Actualizar usuario existente
UPDATE usuario SET role = 'ADMIN' WHERE email = 'admin@example.com';

-- O crear uno nuevo
INSERT INTO usuario (nombre, email, password, role) 
VALUES ('Administrador', 'admin@example.com', 'admin123', 'ADMIN');
```

**Opción 2: Usar el Endpoint (requiere estar logueado)**
```bash
PATCH /api/usuarios/{usuarioId}/rol?nuevoRol=ADMIN

# Con token de cualquier usuario (por ahora)
curl -X PATCH "http://localhost:8080/api/usuarios/1/rol?nuevoRol=ADMIN" \
  -H "Authorization: Bearer {tu_token}"
```

---

## 🔒 Validaciones y Seguridad

#### 👤 En Usuarios:
- ✅ **Email único** - No se permite duplicados
- ✅ **Email válido** - Validación de formato
- ✅ **Contraseña fuerte** - Mínimo 6 caracteres
- ✅ **Protección de datos** - No se puede cambiar email por PUT
- ✅ **Verificación de identidad** - Password actual requerida para cambio
- ✅ **Protección de orfandad** - No eliminar si tiene mascotas

#### 🐾 En Mascotas:
- ✅ **Verificación de propiedad** - Solo el dueño puede actualizar/eliminar
- ✅ **Validación de usuario** - Debe existir en la BD
- ✅ **Datos obligatorios** - Nombre y especie requeridos
- ✅ **Límites de caracteres** - Nombre máx 50 caracteres

#### 📢 En Publicaciones:
- ✅ **Validación de usuario y mascota** - Ambos deben existir
- ✅ **Tipos válidos** - Solo PERDIDO o ENCONTRADO
- ✅ **Sistema de alertas** - Notifica vecinos cercanos automáticamente
- ✅ **Coordenadas obligatorias** - Latitud y longitud del reporte
- ✅ **Búsqueda geográfica** - Encuentra mascotas por radio de distancia

---

## 👤 USUARIOS - Endpoints

### 1. Registrar Nuevo Usuario
```
POST /api/usuarios/registro
Content-Type: application/json

{
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "password": "miPassword123",
  "telefono": "+34 612 345 678",
  "latitudCasa": -34.6037,
  "longitudCasa": -58.3816
}

Respuesta: 201 CREATED
```

### 2. Listar Todos los Usuarios
```
GET /api/usuarios

Respuesta: 200 OK
Array de usuarios
```

### 3. Obtener Usuario por ID
```
GET /api/usuarios/{id}

Ejemplo: GET /api/usuarios/1
Respuesta: 200 OK
```

### 4. Obtener Usuario por Email
```
GET /api/usuarios/email/{email}

Ejemplo: GET /api/usuarios/email/juan@example.com
Respuesta: 200 OK
```

### 5. Buscar Usuarios por Nombre
```
GET /api/usuarios/buscar?nombre={nombre}

Ejemplo: GET /api/usuarios/buscar?nombre=Juan
Respuesta: 200 OK (Array de usuarios que coinciden)
```

### 6. Actualizar Perfil de Usuario
```
PUT /api/usuarios/{usuarioId}
Content-Type: application/json

{
  "nombre": "Juan Pérez Actualizado",
  "telefono": "+34 612 999 999",
  "latitudCasa": -34.5997,
  "longitudCasa": -58.3756
}

Respuesta: 200 OK
Restricciones:
  ❌ No puedes cambiar email
  ❌ No puedes cambiar password (usa cambiar-password)
```

### 7. Cambiar Contraseña
```
POST /api/usuarios/{usuarioId}/cambiar-password
Content-Type: application/json

{
  "passwordActual": "miPassword123",
  "passwordNueva": "nuevoPassword456"
}

Respuesta: 204 NO CONTENT
Requiere: Contraseña actual correcta
```

### 8. Eliminar Usuario
```
DELETE /api/usuarios/{usuarioId}

Respuesta: 204 NO CONTENT
Restricción: No puede tener mascotas registradas
```

### 9. Cambiar Rol de Usuario
```
PATCH /api/usuarios/{usuarioId}/rol?nuevoRol={ADMIN|USER}

Ejemplo: PATCH /api/usuarios/5/rol?nuevoRol=ADMIN

Respuesta: 200 OK
{
  "id": 5,
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "role": "ADMIN",
  ...
}

Nota: A futuro este endpoint estará restringido solo para ADMIN
Valores válidos: USER, ADMIN
```

---

## 🐾 MASCOTAS - Endpoints

### 1. Crear Mascota
```
POST /api/mascotas/usuario/{usuarioId}
Content-Type: application/json

{
  "nombre": "Firulais",
  "especie": "Perro",
  "raza": "Labrador",
  "fotoUrl": "https://ejemplo.com/foto.jpg"
}

Respuesta: 201 CREATED
Requiere: Usuario debe existir
```

### 2. Listar Todas las Mascotas
```
GET /api/mascotas

Respuesta: 200 OK
Array de todas las mascotas
```

### 3. Obtener Mascota por ID
```
GET /api/mascotas/{id}

Ejemplo: GET /api/mascotas/1
Respuesta: 200 OK
```

### 4. Obtener Mascotas de un Usuario
```
GET /api/mascotas/usuario/{usuarioId}

Ejemplo: GET /api/mascotas/usuario/1
Respuesta: 200 OK (Array de mascotas del usuario)
```

### 5. Búsqueda General de Mascotas
```
GET /api/mascotas/buscar?nombre={nombre}

Ejemplo: GET /api/mascotas/buscar?nombre=Firu
Respuesta: 200 OK (Array de mascotas que coinciden)
```

### 6. Búsqueda de Mascotas por Usuario
```
GET /api/mascotas/usuario/{usuarioId}/buscar?nombre={nombre}

Ejemplo: GET /api/mascotas/usuario/1/buscar?nombre=Firu
Respuesta: 200 OK
```

### 7. Actualizar Mascota
```
PUT /api/mascotas/{mascotaId}/usuario/{usuarioId}
Content-Type: application/json

{
  "nombre": "Firulais Actualizado",
  "especie": "Perro",
  "raza": "Golden Retriever",
  "fotoUrl": "https://ejemplo.com/nueva-foto.jpg"
}

Respuesta: 200 OK
Restricción: Solo el dueño puede actualizar
```

### 8. Eliminar Mascota
```
DELETE /api/mascotas/{mascotaId}/usuario/{usuarioId}

Respuesta: 204 NO CONTENT
Restricción: Solo el dueño puede eliminar
```

---

## 📢 PUBLICACIONES - Endpoints

### 1. Crear Publicación
```
POST /api/publicaciones?usuarioId={usuarioId}&mascotaId={mascotaId}
Content-Type: application/json

{
  "titulo": "Perro perdido en el parque",
  "descripcion": "Golden Retriever de 3 años, muy amigable",
  "tipo": "PERDIDO",
  "latitudReporte": -34.6037,
  "longitudReporte": -58.3816
}

Respuesta: 201 CREATED
Tipos válidos: "PERDIDO" o "ENCONTRADO"
Nota: Si es PERDIDO, notifica automáticamente a vecinos en radio de 2km
```

### 2. Listar Todas las Publicaciones
```
GET /api/publicaciones

Respuesta: 200 OK
Array de todas las publicaciones
```

### 3. Obtener Publicación por ID
```
GET /api/publicaciones/{id}

Ejemplo: GET /api/publicaciones/1
Respuesta: 200 OK
```

### 4. Obtener Publicaciones por Tipo
```
GET /api/publicaciones/tipo/{tipo}

Ejemplo: GET /api/publicaciones/tipo/PERDIDO
Ejemplo: GET /api/publicaciones/tipo/ENCONTRADO

Respuesta: 200 OK (Array de publicaciones del tipo especificado)
Tipos válidos: PERDIDO, ENCONTRADO
```

### 5. Obtener Publicaciones por Autor
```
GET /api/publicaciones/autor/{usuarioId}

Ejemplo: GET /api/publicaciones/autor/1
Respuesta: 200 OK (Array de publicaciones del usuario)
```

### 6. Buscar Publicaciones Cercanas (Búsqueda Geográfica)
```
GET /api/publicaciones/cercanas?latitud={lat}&longitud={lon}&radioKm={km}

Ejemplo: GET /api/publicaciones/cercanas?latitud=-34.6037&longitud=-58.3816&radioKm=5

Respuesta: 200 OK
Retorna: Array de publicaciones dentro del radio especificado
Incluye: Nombre mascota, especie, descripción, coordenadas y distancia
```

### 7. Actualizar Publicación
```
PUT /api/publicaciones/{id}
Content-Type: application/json

{
  "titulo": "Título actualizado",
  "descripcion": "Nueva descripción",
  "tipo": "ENCONTRADO",
  "latitudReporte": -34.6037,
  "longitudReporte": -58.3816
}

Respuesta: 200 OK
```

### 8. Eliminar Publicación
```
DELETE /api/publicaciones/{id}

Respuesta: 204 NO CONTENT
```

---

## 📊 Códigos de Estado HTTP

| Código | Significado | Uso |
|--------|-------------|-----|
| 200 | OK | Operación GET/PUT exitosa |
| 201 | Created | Recurso creado exitosamente |
| 204 | No Content | Operación DELETE/POST sin respuesta exitosa |
| 400 | Bad Request | Datos inválidos o validación fallida |
| 401 | Unauthorized | Autenticación fallida (password incorrecto) |
| 403 | Forbidden | Sin permisos (no es el dueño) |
| 404 | Not Found | Recurso no encontrado |
| 409 | Conflict | Conflicto de datos (usuario con mascotas) |

---

## 💡 Ejemplos Prácticos

### Flujo Completo: Registrar Usuario, Mascota y Publicación

#### 1. Registrar usuario
```bash
curl -X POST http://localhost:8080/api/usuarios/registro \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan Pérez",
    "email": "juan@example.com",
    "password": "miPassword123",
    "latitudCasa": -34.6037,
    "longitudCasa": -58.3816
  }'

# Respuesta: { "id": 1, "nombre": "Juan Pérez", "role": "USER", ... }
```

#### 2. Hacer login para obtener token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "juan@example.com",
    "password": "miPassword123"
  }'

# Respuesta: 
# {
#   "token": "eyJhbGciOiJIUzUxMiJ9...",
#   "role": "USER",
#   "email": "juan@example.com"
# }
# ⚠️ GUARDA EL TOKEN - Lo necesitarás para las siguientes peticiones
```

#### 3. Registrar mascota del usuario (con token)
```bash
curl -X POST http://localhost:8080/api/mascotas/usuario/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Firulais",
    "especie": "Perro",
    "raza": "Labrador"
  }'

# Respuesta: { "id": 1, "nombre": "Firulais", "duenio": { "id": 1, ... } }
```

#### 4. Crear publicación de mascota perdida (con token)
```bash
curl -X POST "http://localhost:8080/api/publicaciones?usuarioId=1&mascotaId=1" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Firulais perdido en el parque",
    "descripcion": "Perro Labrador dorado, muy amigable",
    "tipo": "PERDIDO",
    "latitudReporte": -34.6037,
    "longitudReporte": -58.3816
  }'

# Respuesta: { "id": 1, ... }
# Nota: Se envían automáticamente emails a vecinos en radio de 2km
```

#### 5. Buscar mascotas perdidas cercanas (público, no requiere token)
```bash
curl "http://localhost:8080/api/publicaciones/cercanas?latitud=-34.6037&longitud=-58.3816&radioKm=5"

# Respuesta: Array con publicaciones cercanas y distancia
```

#### 5. Buscar la mascota
```bash
curl http://localhost:8080/api/mascotas/usuario/1/buscar?nombre=Firu

# Respuesta: Array con mascotas que coinciden
```

#### 6. Actualizar publicación cuando se encuentra (con token)
```bash
curl -X PUT http://localhost:8080/api/publicaciones/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Firulais encontrado!",
    "descripcion": "Mascota encontrada sana y salva",
    "tipo": "ENCONTRADO",
    "latitudReporte": -34.6037,
    "longitudReporte": -58.3816
  }'

# Respuesta: Publicación actualizada
```

#### 7. Cambiar contraseña del usuario (con token)
```bash
curl -X POST http://localhost:8080/api/usuarios/1/cambiar-password \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..." \
  -H "Content-Type: application/json" \
  -d '{
    "passwordActual": "miPassword123",
    "passwordNueva": "nuevoPassword456"
  }'

# Respuesta: 204 No Content
```

---

### Operación Fallida: Intento de Modificación No Autorizada

```bash
# Usuario 2 intenta actualizar mascota del usuario 1 (con token de usuario 2)
curl -X PUT http://localhost:8080/api/mascotas/1/usuario/2 \
  -H "Authorization: Bearer {token_usuario_2}" \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Intento de hackeo"
  }'

# Respuesta: 403 FORBIDDEN
# Mensaje: "No tienes permiso para actualizar esta mascota"
```

---

### Ejemplo: Cambiar Usuario a ADMIN

```bash
# 1. Login como cualquier usuario (por ahora)
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "juan@example.com", "password": "miPassword123"}'

# Respuesta: { "token": "...", "role": "USER", ... }

# 2. Cambiar rol a ADMIN
curl -X PATCH "http://localhost:8080/api/usuarios/1/rol?nuevoRol=ADMIN" \
  -H "Authorization: Bearer {tu_token}"

# Respuesta: { "id": 1, "nombre": "Juan Pérez", "role": "ADMIN", ... }

# 3. Hacer login nuevamente para obtener token con rol ADMIN
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "juan@example.com", "password": "miPassword123"}'

# Respuesta: { "token": "...", "role": "ADMIN", ... }
# Ahora el token incluye el rol ADMIN
```

---

## 📖 Documentación Detallada

Para información más completa, consulta:

### Usuarios:
- **Archivo:** `API_USUARIOS_DOCUMENTACION.md`
- **Contenido:** Documentación detallada de todos los endpoints de usuarios
- **Incluye:** Validaciones, ejemplos, errores comunes

### Mascotas:
- **Archivo:** `API_MASCOTAS_DOCUMENTACION.md`
- **Contenido:** Documentación detallada de todos los endpoints de mascotas
- **Incluye:** Validaciones de seguridad, ejemplos, casos de uso

### Pruebas:
- **Archivo:** `PRUEBAS_API_USUARIOS.md`
- **Contenido:** Casos de prueba para usuarios
- **Incluye:** Casos exitosos, errores esperados, scripts PowerShell

- **Archivo:** `PRUEBAS_API_MASCOTAS.md`
- **Contenido:** Casos de prueba para mascotas
- **Incluye:** Casos exitosos, errores esperados, scripts PowerShell

---

## 🚀 Inicio Rápido

### 1. **Iniciar el servidor**
```bash
docker-compose up
```

### 2. **Registrar un usuario**
```bash
curl -X POST http://localhost:8080/api/usuarios/registro \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Tu Nombre",
    "email": "tu@email.com",
    "password": "password123",
    "latitudCasa": -34.6037,
    "longitudCasa": -58.3816
  }'
```

### 3. **Obtener el ID del usuario** (de la respuesta anterior)

### 4. **Registrar una mascota**
```bash
curl -X POST http://localhost:8080/api/mascotas/usuario/{usuarioId} \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Mi Mascota",
    "especie": "Perro",
    "raza": "Labrador"
  }'
```

### 5. **Crear publicación de mascota perdida**
```bash
curl -X POST "http://localhost:8080/api/publicaciones?usuarioId={usuarioId}&mascotaId={mascotaId}" \
  -H "Content-Type: application/json" \
  -d '{
    "titulo": "Mascota perdida",
    "descripcion": "Se perdió en el parque",
    "tipo": "PERDIDO",
    "latitudReporte": -34.6037,
    "longitudReporte": -58.3816
  }'
```

### 6. **Buscar mascotas cercanas**
```bash
curl "http://localhost:8080/api/publicaciones/cercanas?latitud=-34.6037&longitud=-58.3816&radioKm=5"
```

### 7. **Probar otros endpoints**

---

## 🔧 Herramientas Recomendadas para Pruebas

1. **Postman** - GUI interactivo para pruebas API
2. **cURL** - Cliente HTTP desde línea de comandos
3. **PowerShell** - Scripts de prueba automatizadas
4. **Thunder Client** - Extensión de VS Code
5. **REST Client** - Extensión de VS Code

---

## 📋 Checklist de Verificación

- [ ] Servidor Docker iniciado correctamente
- [ ] Conexión a base de datos verificada
- [ ] Registro de usuario funciona
- [ ] Búsqueda de usuarios funciona
- [ ] Actualización de perfil funciona
- [ ] Cambio de contraseña funciona
- [ ] Creación de mascotas funciona
- [ ] Búsqueda de mascotas funciona
- [ ] Actualización de mascotas funciona (solo dueño)
- [ ] Eliminación de mascotas funciona (solo dueño)
- [ ] Creación de publicaciones funciona
- [ ] Búsqueda de publicaciones por tipo funciona
- [ ] Búsqueda geográfica de publicaciones funciona
- [ ] Sistema de alertas por email funciona (radio 2km)
- [ ] Actualización de publicaciones funciona
- [ ] Eliminación de publicaciones funciona
- [ ] Eliminación de usuario (sin mascotas) funciona
- [ ] Códigos HTTP son correctos

---

## ⚠️ Notas Importantes

1. **CORS:** Está habilitado con `@CrossOrigin(origins = "*")`. En producción, deberías restringir los orígenes permitidos.

2. **Contraseñas:** Actualmente se almacenan en texto plano. **En producción, debes usar BCrypt u otro método de hash seguro.**

3. **Autenticación JWT:** ✅ Sistema JWT implementado. Los tokens tienen validez de 24 horas e incluyen el rol del usuario.

4. **Roles:** Sistema de roles ADMIN/USER implementado. Por ahora ambos tienen los mismos permisos, pero la infraestructura está lista para restricciones futuras.

5. **Validación:** Se usa Bean Validation con `@Valid` y anotaciones para validar datos en controladores.

6. **Logging:** Se recomienda agregar logging para auditoría y debugging.

7. **Migración de BD:** Después de actualizar el código, debes agregar la columna `role` a la tabla usuario. Ver archivo `MIGRACION_ROLES.md`.

---

## 📞 Soporte y Contacto

Para problemas, reportes de bugs o sugerencias:
- Consulta la documentación específica de cada recurso
- Revisa los casos de prueba para ejemplos
- Verifica los logs del servidor para mensajes de error

---

## 📝 Historial de Cambios

| Versión | Fecha | Cambios |
|---------|-------|---------|
| 1.0 | 2026-02-24 | Documentación completa inicial |
| 1.1 | 2026-03-04 | Sistema JWT implementado. Sistema de roles ADMIN/USER agregado |

---

**Última actualización:** 2026-03-04  
**Estado:** ✅ Sistema JWT y Roles implementados

