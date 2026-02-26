# 🔐 Configuración de Variables de Entorno

## 📌 Para Desarrolladores

Este proyecto utiliza **variables de entorno** para proteger credenciales sensibles como las de Mailtrap.

### ⚙️ Configuración Inicial

1. **Copia el archivo de ejemplo:**
   ```bash
   cp .env.example .env
   ```

2. **Edita el archivo `.env` con tus credenciales:**
   ```env
   MAIL_HOST=sandbox.smtp.mailtrap.io
   MAIL_PORT=2525
   MAIL_USERNAME=tu_username_de_mailtrap
   MAIL_PASSWORD=tu_password_de_mailtrap
   ```

3. **Obtén tus credenciales de Mailtrap:**
   - Ve a [https://mailtrap.io/](https://mailtrap.io/)
   - Regístrate o inicia sesión
   - Ve a tu inbox de prueba
   - Copia el username y password de SMTP Settings

### 🐳 Ejecutar con Docker Compose

```bash
docker-compose up --build
```

Docker Compose automáticamente leerá las variables del archivo `.env` y las inyectará en el contenedor.

### 🚀 Ejecutar en Local (sin Docker)

Configura las variables de entorno en tu IDE o en la terminal:

**Windows (PowerShell):**
```powershell
$env:MAIL_HOST="sandbox.smtp.mailtrap.io"
$env:MAIL_PORT="2525"
$env:MAIL_USERNAME="tu_username"
$env:MAIL_PASSWORD="tu_password"
```

**Linux/Mac (Bash):**
```bash
export MAIL_HOST=sandbox.smtp.mailtrap.io
export MAIL_PORT=2525
export MAIL_USERNAME=tu_username
export MAIL_PASSWORD=tu_password
```

Luego ejecuta:
```bash
./mvnw spring-boot:run
```

### ⚠️ IMPORTANTE

- **NUNCA** subas el archivo `.env` a GitHub
- **SIEMPRE** usa `.env.example` como plantilla
- El archivo `.env` está en `.gitignore` para protegerte

### 🔍 Verificar que funciona

Si todo está configurado correctamente, al crear una publicación PERDIDA, deberías ver en los logs:

```
!! EMAIL ENVIADO A: [nombre del vecino]
```

---

## 📁 Archivos Importantes

- `.env.example` → Plantilla pública (se sube a GitHub)
- `.env` → Credenciales reales (NO se sube a GitHub)
- `.gitignore` → Contiene `.env` para protegerlo
- `application.properties` → Lee las variables de entorno
- `docker-compose.yml` → Inyecta las variables al contenedor

