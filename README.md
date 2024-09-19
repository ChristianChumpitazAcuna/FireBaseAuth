---

## 😎 Environments
Ayudan a gestionar diferentes configuraciones para variables como bases de datos, credenciales, servicios externos, y otras propiedades que pueden variar según el entorno.

<details>
  <summary><strong>Uso de Variables de Entorno</strong></summary>
  
Las variables de entorno se definen utilizando la sintaxis **${VARIABLE_NAME:default_value}**.
  
- Usar valores definidos en el entorno de ejecución.
- Proporcionar valores por defecto para desarrollo local.
</details>

<details>
  <summary><strong>Configuración de Entornos</strong></summary>
  
Utilizamos archivos de propiedades o YML para definir configuraciones de cada entorno. 
Por ejemplo, **application.yml**

```yaml
spring:
  application:
    name: vg.ms.enrollment_detail
  data:
    mongodb:
      uri: ${MONGODB_URI:mongodb://localhost:27017/defaultdb}

server:
  port: ${SERVER_PORT:8091}

services:
  student:
    url: ${STUDENT_SERVICE_URL:http://localhost:8082/student}
```
</details>

<details>
  <summary><strong>Consumo de Propiedades con @Value</strong></summary>
  
Para utilizar estas propiedades en tu código, puedes usar la anotación **@Value** de Spring. Ejemplos:

```Java
@Value("${services.student.url}")
private String studentServiceUrl;
```
</details>

---
---
