# NequiChallenge - API de Gestión de Franquicias

## 📌 Descripción

Esta API permite gestionar una red de franquicias desarrollada para el reto técnico de Nequi. El sistema permite administrar franquicias, sucursales y productos, manteniendo un control del stock disponible.

---

## 🧩 Funcionalidades principales

- Gestión de franquicias
- Gestión de sucursales por franquicia
- Gestión de productos por sucursal con control de stock

---

## 🛠️ Tecnologías

- Java 21
- Spring WebFlux (Programación Reactiva)
- Gradle
- R2DBC con PostgreSQL
- Docker y Docker Compose
- OpenAPI / Swagger
- Terraform (Infraestructura como código)
- AWS (ECS, VPC, RDS, ECR)

---

## 🏗️ Estructura del Proyecto

El proyecto sigue una arquitectura limpia (Clean Architecture) con la siguiente estructura:

```plaintext
NequiChallenge/
├── applications/
│   └── app-service/           # Aplicación principal
├── domain/
│   ├── model/                 # Modelos de dominio
│   └── usecase/               # Casos de uso del dominio
├── infrastructure/
│   ├── driven-adapters/
│   │   └── r2dbc-postgresql/  # Adaptador para PostgreSQL reactivo
│   └── entry-points/
│       └── reactive-web/      # API REST reactiva
└── deployment/                # Configuraciones de despliegue
    ├── docker-compose.yml     # Para desarrollo local
    ├── docker-compose-prod.yml # Para producción
    └── terraform/              # IaC con Terraform
```

---

## ✅ Prerrequisitos

- Java 21 o superior
- Docker y Docker Compose
- Gradle 7.x o superior
- Terraform 1.x o superior
- Cuenta activa en AWS con credenciales configuradas (`aws configure`)

---

## 🔐 Variables de Entorno

| Variable        | Descripción                            | Valor por defecto |
|----------------|----------------------------------------|-------------------|
| DB_HOST         | Host de la base de datos PostgreSQL    | -                 |
| DB_PORT         | Puerto de PostgreSQL                   | -                 |
| DB_NAME         | Nombre de la base de datos             | -                 |
| DB_USER         | Usuario de la base de datos            | -                 |
| DB_PASSWORD     | Contraseña de la base de datos         | -                 |
| DB_SSL_MODE     | Modo SSL para la conexión              | disable           |

---

## 🚀 Instalación y Ejecución

### 🧪 Desarrollo Local

1. Clonar el repositorio:

```bash

https://github.com/camora-pragma/nequi-challenge-web-api
cd nequi-challenge-api
```

2. Iniciar la base de datos local:

```bash

cd deployment
docker-compose up -d
```

3. Compilar

```bash

./gradlew clean build
```

---

## 🧪 Ejecución de Tests

Para ejecutar las pruebas y generar los informes de cobertura:

```bash

./gradlew clean bootjar test jacocoMergedReport
```

El informe de cobertura estará disponible en:

```
build/reports/jacoco/jacocoMergedReport/html/index.html
```

---

## 🚢 Usar Imagen publica dockerhub

la imagen pública en Docker Hub:

```bash

cd deployment
docker-compose -f docker-compose-prod.yml up -d
```

## 📚 Documentación de la API

La documentación de la API está disponible mediante Swagger UI:

- Swagger UI: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- OpenAPI Docs: [http://localhost:8080/v1/api-docs](http://localhost:8080/v1/api-docs)


---

## 🌐 Despliegue con Infraestructura como Código (IaC)

La infraestructura necesaria para ejecutar la API también puede ser desplegada en AWS usando Terraform.

### 📁 Estructura del módulo Terraform

Los archivos de Terraform se encuentran en:

```
deployment/terraform/
```

### 📄 Configuración

Antes de ejecutar Terraform, crea un archivo llamado `terraform.tfvars` dentro del directorio `deployment/terraform` con el siguiente contenido:

```hcl
aws_region      = "us-east-1"
project_name    = "nequi-challenge"
vpc_cidr        = "10.0.0.0/16"
container_image = "YOUR/IMAGE"
container_port  = 8080
cpu             = 1024
memory          = 3072
desired_count   = 1
db_user         = "USER"
db_password     = "PASSWORD"
db_host         = "HOST"
db_name         = "DB_NAME"
db_ssl_mode     = "require"
```

⚠️ Nota: Asegúrate de reemplazar los valores como `YOUR/IMAGE`, `USER`, `PASSWORD`, etc., con los datos reales.
La imagen del contenedor debe estar publicada previamente en Amazon ECR.

---

### ⚙️ Requisitos

- Terraform instalado: https://developer.hashicorp.com/terraform/install
- Cuenta activa en AWS
- Acceso con credenciales configuradas (`aws configure`)

---

### 🚀 Pasos para el despliegue

1. Ir al directorio de Terraform:

```bash

cd deployment/terraform
```

2. Inicializar el proyecto:

```bash

terraform init
```

3. Revisar el plan de ejecución:

```bash

terraform plan
```

4. Aplicar el despliegue:

```bash

terraform apply
```

## 🚀 Endpoints de la API

A continuación se presentan los principales endpoints de la API para gestionar franquicias, sucursales y productos:

### **POST /api/v1/franchise**
Crea una nueva franquicia.

---

### **POST /api/v1/franchise/{franchiseId}/branch**
Crea una nueva sucursal para una franquicia específica.

---

### **GET /api/v1/franchise/{franchiseId}/branches/products/max-stock**
Obtiene el producto con el stock máximo en todas las sucursales de una franquicia.

---

### **POST /api/v1/branch/{branchId}/product**
Añade un nuevo producto a una sucursal específica.

---

### **DELETE /api/v1/branch/{branchId}/product/{productId}**
Elimina un producto de una sucursal específica.

---

### **PUT /api/v1/product/{productId}**
Actualiza la información de un producto específico.

---

### **URL de la aplicación en producción**

Puedes acceder a la aplicación en producción utilizando la siguiente URL:

[https://xeuoinl7g3.execute-api.us-east-1.amazonaws.com/api/v1/franchise/1/branches/products/max-stock](https://xeuoinl7g3.execute-api.us-east-1.amazonaws.com/api/v1/franchise/1/branches/products/max-stock)

---


