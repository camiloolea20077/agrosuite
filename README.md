# 🌾 AgroSuit

**AgroSuit** es una plataforma integral para la gestión agropecuaria, que incluye control de nacimientos, ganado, empleados, usuarios, y más.

Este proyecto está dividido en dos partes principales:

- **Frontend:** Angular 16 + PrimeNG
- **Backend:** Java (Spring Boot)
- **Base de datos:** PostgreSQL

---

## 📁 Estructura del proyecto


---

## ⚙️ Requisitos

Asegúrate de tener lo siguiente instalado en tu máquina:

- [Node.js](https://nodejs.org/) 18 o superior
- [Angular CLI](https://angular.io/cli)
- [Java JDK](https://adoptium.net/) 17 o superior
- [Apache Maven](https://maven.apache.org/)
- [PostgreSQL](https://www.postgresql.org/) 13 o superior

---

## 🛠️ Configuración del proyecto

### 📌 Backend

1. Ve a la carpeta del backend:

```bash
cd backend

DB_HOST=localhost
DB_PORT=5432
DB_NAME=agrosuit
DB_USER=tu_usuario
DB_PASSWORD=tu_contraseña


./mvnw spring-boot:run
mvn spring-boot:run


🌐 Frontend
cd frontend
npm install
ng serve


🚀 Build de producción

ng build --configuration=production
dist/frontend-erp/
