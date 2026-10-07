# Decisiones de Diseño y Arquitectura

En este documento se detallan las respuestas y justificaciones técnicas sobre las decisiones tomadas en la configuración y arquitectura del proyecto Spring Boot.

---

## 1. ¿Por qué usaste `@ConfigurationProperties` en lugar de `@Value`?

Se optó por `@ConfigurationProperties` frente a `@Value` por las siguientes ventajas clave en mantenibilidad, robustez y diseño:

* **Type-Safety y Organización Jerárquica:**
  Permite agrupar todas las propiedades relacionadas bajo un prefijo común (`app.info.*`) dentro de una clase estructurada (`AppInfoProperties`). Con `@Value`, las propiedades se inyectan como cadenas dispersas (`@Value("${app.info.name}")`), lo que propicia errores tipográficos y dificulta la detección de inconsistencias.
* **Validación Declarativa con Fail-Fast:**
  Al combinar `@ConfigurationProperties` con `@Validated` y anotaciones de Jakarta Validation (`@NotBlank`, `@Email`), Spring Boot valida exhaustivamente todas las propiedades durante la fase de inicialización. Si falta un parámetro obligatorio o el formato de correo es incorrecto, la aplicación detiene el arranque de inmediato (*fail-fast*), previniendo errores en tiempo de ejecución. `@Value` no ofrece validación nativa de constraints de Jakarta Bean Validation sobre las variables inyectadas.
* **Inyección Limpia y Testabilidad:**
  El controlador (`InfoController`) depende de un objeto fuertemente tipado mediante inyección por constructor (`Constructor Injection`), evitando acoplar el controlador a la infraestructura de resolución de propiedades de Spring. Esto permite probar el controlador de forma aislada sin levantar el entorno completo de variables.
* **Soporte para Herramientas e IDEs:**
  Permite generar metadatos de configuración (`spring-boot-configuration-processor`), habilitando autocompletado, advertencias y documentación en los archivos `.properties` y `.yml` dentro del entorno de desarrollo.

---

## 2. ¿Cómo garantizas que la versión no se desincronice del `pom.xml`?

Para asegurar el principio de **Fuente Única de Verdad** (*Single Source of Truth*):

* **Configuración del Token en `application.properties`:**
  En el archivo de configuración se establece:
  ```properties
  app.info.version=@project.version@
  ```
* **Filtrado de Recursos de Maven (*Maven Resource Filtering*):**
  Spring Boot Starter Parent configura por defecto el delimitador `@` en el plugin `maven-resources-plugin`. Durante la fase `process-resources` de la compilación de Maven, dicho token es reemplazado automáticamente por el valor de la etiqueta `<version>` definida en el `pom.xml` (actualmente `0.0.1-SNAPSHOT`).
* **Sincronización Automática:**
  No se requiere editar manualmente la versión en los archivos de propiedades. Cualquier actualización de versión en el `pom.xml` (manual o mediante herramientas CI/CD como `mvn versions:set`) se refleja de forma transparente en la propiedad de configuración y, en consecuencia, en la respuesta de `/api/info`.

---

## 3. ¿Qué pasaría si quitas el `@Component` de `AppInfoProperties`? (Prueba y Documentación de Error)

### Comportamiento Teórico
La anotación `@ConfigurationProperties` se limita a definir el mapeo y enlace de propiedades externas hacia los campos de la clase, pero **no registra la clase como un Bean** en el contenedor de dependencias (`ApplicationContext`) de Spring. Para que Spring cree el Bean correspondiente, la clase debe:
1. Tener una anotación de componente de Spring (como `@Component`), o bien
2. Ser registrada explícitamente mediante `@EnableConfigurationProperties(AppInfoProperties.class)` o `@ConfigurationPropertiesScan` en la clase principal o de configuración.

Al retirar `@Component`, Spring Boot no instancia `AppInfoProperties` durante el escaneo de componentes. Cuando Spring intenta instanciar `InfoController`, encuentra que el parámetro 0 de su constructor requiere un Bean de tipo `AppInfoProperties` que no existe en el contexto, fallando el arranque de la aplicación.

### Prueba Realizada y Evidencia del Error
Se comentó la anotación `@Component` en `AppInfoProperties.java` y se ejecutó la prueba de arranque del contexto (`SegundaPracticaSprintApplicationTests`). La prueba falló arrojando el siguiente error exacto en la consola:

```text
Caused by: org.springframework.beans.factory.UnsatisfiedDependencyException: 
Error creating bean with name 'infoController' defined in file [...\controller\InfoController.class]: 
Unsatisfied dependency expressed through constructor parameter 0: 
No qualifying bean of type 'com.example.segunda_practica_sprint.config.AppInfoProperties' available: 
expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {}

Caused by: org.springframework.beans.factory.NoSuchBeanDefinitionException: 
No qualifying bean of type 'com.example.segunda_practica_sprint.config.AppInfoProperties' available: 
expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {}
```

> **Conclusión:** `@Component` es esencial para que Spring descubra y gestione `AppInfoProperties` como un Bean inyectable en `InfoController`.

---

## 4. ¿Qué diferencia hay entre tu `/info` y el `/actuator/info` de Spring?

| Criterio | `/api/info` (Endpoint Personalizado) | `/actuator/info` (Spring Boot Actuator) |
| :--- | :--- | :--- |
| **Audiencia y Propósito** | Diseñado para los consumidores de la aplicación (clientes frontend, integraciones externas, usuarios finales). Responde a requisitos funcionales del negocio. | Diseñado para operaciones, monitoreo, observabilidad y plataformas DevOps (ej. Kubernetes, Prometheus, Grafana). Responde a requerimientos operacionales. |
| **Estructura del Contrato** | Retorna un DTO fuertemente tipado (`AppInfoResponse`) con campos definidos de la aplicación (nombre, desarrollador, correo, rol, entorno). | Retorna un mapa extensible y genérico de información del sistema, compilación y entorno. |
| **Fuente de Datos** | Alimentado por la clase tipada `AppInfoProperties` mediante inyección de dependencias en `InfoController`. | Alimentado por múltiples componentes contribuyentes (`InfoContributor`), tales como `BuildInfoContributor`, `GitInfoContributor`, `EnvInfoContributor`, etc. |
| **Seguridad y Exposición** | Expuesto en la ruta base de la API `/api` con acceso público controlado explícitamente en `SecurityFilterChain`. | Pertenece al subsistema `/actuator`. Suele aislarse en redes privadas, puertos dedicados o requerir roles de administración en entornos productivos. |

---

## 5. Exploración de Propiedades (Investigación por Categoría)

A continuación se documentan las propiedades configuradas en `application.properties` (y perfiles asociados), junto con las respuestas a su comportamiento, verificación e impacto en caso de omisión.

### 5.1. Servidor
* **Propiedades:** `server.error.include-message=never` y `server.shutdown=graceful`.
* **¿Qué cambió en el comportamiento de tu app?**
  - `server.error.include-message=never`: En caso de producirse un error HTTP 4xx o 5xx, el JSON de error estándar de Spring suprime el mensaje descriptivo interno de la excepción.
  - `server.shutdown=graceful`: Al recibir una orden de parada (ej. SIGTERM o stop en la consola), el servidor Tomcat no finaliza abruptamente, sino que otorga un periodo de gracia para que las peticiones HTTP activas concluyan su procesamiento.
* **¿Cómo lo verificaste?**
  - Para `include-message`: Se realizó una petición errónea verificando que la clave `"message"` en el payload devuelto no revele stacktraces o detalles internos.
  - Para `shutdown`: En el log de arranque se confirma la inicialización de Tomcat en modo de apagado ordenado (`Graceful shutdown enabled`), y al detener el proceso se verifica el log de espera ordenada.
* **¿Qué pasaría si no las configuraras?**
  - Sin `include-message=never`, las respuestas de error podrían filtrar detalles de clases internas o excepciones a usuarios maliciosos (*Information Disclosure*).
  - Sin `shutdown=graceful`, el apagado es inmediato (`immediate`), interrumpiendo conexiones activas y generando errores de conexión o pérdida de datos en transacciones en vuelo.

### 5.2. Logging
* **Propiedades:** `logging.level.com.example` (definido por perfiles: `DEBUG` en `dev` y `WARN` en `prod`) y `logging.file.name=logs/app.log`.
* **¿Qué cambió en el comportamiento de tu app?**
  - El volumen de logs del paquete base cambia según el entorno: en desarrollo provee información exhaustiva (`DEBUG`) y en producción evita saturación de logs registrando únicamente advertencias y errores (`WARN`).
  - Además, los eventos de log se registran automáticamente en disco en el archivo `logs/app.log`, además de salir por consola.
* **¿Cómo lo verificaste?**
  - Se verificó en el sistema de archivos la creación automática de la carpeta `logs/` con el archivo `app.log` persistido.
  - Se comparó el log de arranque entre el perfil `dev` y `prod`, evidenciando la reducción drástica de verbosidad en `prod`.
* **¿Qué pasaría si no las configuraras?**
  - Sin `logging.level`, todo usaría el nivel por defecto (`INFO`), perdiendo visibilidad fina en desarrollo o saturando los logs en producción.
  - Sin `logging.file.name`, al cerrar la terminal o reiniciar el servidor, todo el historial de ejecución se perdería irremediablemente sin posibilidad de auditoría forense posterior.

### 5.3. Bases de Datos / JSON
* **Propiedades:** `spring.datasource.hikari.initialization-fail-timeout=-1` (con `spring.jpa.hibernate.ddl-auto=none`) y `spring.jackson.default-property-inclusion=non_null`.
* **¿Qué cambió en el comportamiento de tu app?**
  - Con `initialization-fail-timeout=-1`, el pool HikariCP no aborta el arranque de la aplicación aunque PostgreSQL se encuentre fuera de línea, permitiendo que Spring Boot inicie exitosamente en modo tolerante a fallos.
  - Con `non_null`, Jackson excluye automáticamente cualquier campo cuyo valor sea `null` al serializar respuestas JSON.
* **¿Cómo lo verificaste?**
  - Se inició la aplicación con PostgreSQL apagado: el servidor arrancó normalmente en el puerto 8080, `/api/info` respondió con éxito y `/actuator/health` reportó `db: DOWN` sin haber roto el inicio del servidor.
  - Se inspeccionó la salida JSON asegurando que ningún atributo nulo forme parte de los payloads.
* **¿Qué pasaría si no las configuraras?**
  - Sin `initialization-fail-timeout=-1`, HikariCP lanza una excepción fatal tras 1 segundo si no conecta a PostgreSQL, provocando que la aplicación completa se detenga y no pueda servir ningún endpoint.
  - Sin `non_null`, los JSON serializarían `"campo": null`, consumiendo ancho de banda innecesario y entregando payloads con atributos residuales.

### 5.4. Actuator
* **Propiedades:** `management.endpoints.web.exposure.include=health,info` y `management.endpoint.health.show-details=always`.
* **¿Qué cambió en el comportamiento de tu app?**
  - Expuso a través del protocolo web HTTP los endpoints de gestión `/actuator/health` y `/actuator/info`.
  - Provocó que `/actuator/health` muestre el desglose minucioso de cada subsistema (base de datos, espacio en disco, liveness, readiness, SSL, etc.).
* **¿Cómo lo verificaste?**
  - Se consultó en el navegador `http://localhost:8080/actuator/health`, observando el detalle completo de cada componente bajo la clave `"components"`.
* **¿Qué pasaría si no las configuraras?**
  - Sin `exposure.include`, `/actuator/info` estaría bloqueado por defecto devolviendo HTTP 404.
  - Sin `show-details=always`, `/actuator/health` únicamente retornaría un resumen plano como `{"status": "DOWN"}` sin indicar cuál componente específico está fallando.

### 5.5. Seguridad
* **Propiedades y Configuración:** `spring.security.user.name=admin`, `spring.security.user.password=admin123` y el `SecurityFilterChain` en `SecurityConfig.java`.
* **¿Qué cambió en el comportamiento de tu app?**
  - Estableció credenciales conocidas para autenticación HTTP Basic.
  - Configuró permisos para que `/api/info` y `/actuator/health` sean de libre acceso anónimo, mientras que el resto de rutas (como `/home`) requieran autenticación.
* **¿Cómo lo verificaste?**
  - Petición `curl.exe http://localhost:8080/api/info` sin credenciales: devuelve HTTP 200 exitosamente.
  - Petición a `/home` sin credenciales: rechazada con HTTP 401 Unauthorized; enviando credenciales (`-u admin:admin123`), responde HTTP 200.
* **¿Qué pasaría si no las configuraras?**
  - Sin `user.name` y `password`, Spring Security genera una contraseña efímera aleatoria en consola en cada arranque.
  - Sin el `SecurityFilterChain` personalizado, Spring Security por defecto bloquea **todos** los endpoints de la aplicación, haciendo imposible consumir `/api/info` sin credenciales previas.

