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

 ### Uso de Logging Estructurado
El logging estructurado permite registrar eventos del sistema de forma organizada, utilizando diferentes niveles de severidad. Esto facilita la depuración y el monitoreo los microservicios.
<details>
  <summary><strong>Tipos de log</strong></summary>

- **INFO:** Para registrar eventos importantes en el flujo normal de la aplicación, como el inicio de procesos o la obtención de datos.
- **DEBUG:** Para incluir detalles adicionales que son útiles durante el desarrollo o depuración, pero no deberían estar activos en producción.
- **WARN:** Para advertir sobre situaciones inesperadas que no interrumpen la ejecución, pero podrían necesitar atención.
- **ERROR:** Para registrar errores críticos que pueden causar fallos en la aplicación y requieren una intervención inmediata.
</details>

<details>
  <summary><strong>Requisitos</strong></summary>
Tener Lombok agregado en el proyecto

```yaml
<dependency>
  <groupId>org.projectlombok</groupId>
  <artifactId>lombok</artifactId>
  <optional>true</optional>
</dependency>
```

Usar la anotacion `@Slf4j`

```Java
@Service
@Slf4j
public class UserService {
  ...
}
```
</details>

<details>
  <summary><strong>Ejemplo</strong></summary>
  Aquí se muestra cómo realizar logs en un servicio, registrando información adicional sobre el procesamiento de datos.

```Java
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    public void procesarUsuario(Long userId) {
        // Nivel INFO: Evento significativo en el flujo
        log.info("Procesando usuario con ID: {}", userId);

        try {
            // Simulación de procesamiento
            log.debug("Buscando datos del usuario con ID: {}", userId);
            // Procesamiento exitoso
            log.info("Usuario con ID {} procesado exitosamente", userId);

        } catch (Exception e) {
            // Nivel ERROR: Excepción en el procesamiento
            log.error("Error al procesar el usuario con ID {}: {}", userId, e.getMessage(), e);
            throw e; // Relanzar la excepción si es necesario
        }
    }
}
```
</details>
