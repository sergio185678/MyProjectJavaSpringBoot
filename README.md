# Proyecto Java SpringBoot: Guía para Desarrollo de API REST

Este proyecto sirve como base y guía para la creación de otros proyectos similares, implementando los conceptos básicos de una API REST utilizando Java y Spring Boot.

Nota: El backend desplegado actualmente no admite la gestión de archivos debido a las limitaciones de la capa gratuita de Render con Docker, así como a mi falta de experiencia en Docker. Además, debido a estas limitaciones, la inicialización del backend puede ser bastante lenta, lo que podría resultar en que la aplicación no cargue de inmediato. Si esto sucede, te recomiendo dejarla cargando; eventualmente, se inicializará y la aplicación funcionará correctamente.

Usuario con cargo administrador: 
  - Email: sergio1@gmail.com
  - Contraseña: 123
  - 
Link desplegado del backend: [https://myprojectjavaspringboot.onrender.com](https://myprojectjavaspringboot.onrender.com/swagger-ui/index.html)
Creditos: https://render.com/

![image](https://github.com/sergio185678/MyProjectJavaSpringBoot/assets/67492035/27da0391-b845-40c6-9250-3a5306e9934a)

Link del repositorio del frontend: https://github.com/sergio185678/FrontendBaseAngularWithJava
## Estructura del Proyecto:

- **Controller**: Gestiona todas las solicitudes HTTP. Se integra con Service, Utils y Middleware mediante inyección de dependencias.
  
- **Middleware**: Encargado de la autenticación, trabajando en conjunto con Utils.

- **Modelo**:
  - **Dao o Repositorio**: Implementa las operaciones relacionadas con la base de datos utilizando CrudRepository y PagingAndSortingRepository para facilitar el trabajo.
  - **Dto**: Representa las entidades del sistema, especialmente diseñadas para trabajar con las peticiones HTTP entrantes y salientes.
  - **Entidad**: Representa las tablas de la base de datos.
  - **Payload**: Define la estructura de respuesta de las peticiones, que incluye un mensaje y un objeto.

- **Service**: Contiene la lógica de negocio, trabajando en conjunto con el Dao o Repositorio.
  - **Interfaces**: Separación de las interfaces de los servicios para una mejor organización.

- **Utils**: Contiene el JWTUtil con la configuración completa del JWT.

Este proyecto utiliza una base de datos simple que incluye entidades de Usuario, Documento y Cargo.

## Características Adicionales:

Aparte de las funcionalidades básicas de una API CRUD simple, se implementaron las siguientes características:

- Se integró la dependencia de Hibernate para trabajar con ORM, permitiendo la relación entre las tablas mediante anotaciones como @OneToMany y @ManyToOne.

- Capa de seguridad:
  - Encriptación de las contraseñas de los usuarios.
  - Implementación de JWT, configurando los claims, la clave y el sujeto, y proporcionando funciones para facilitar la manipulación de los valores dentro del JWT.
  - Asignación de permisos específicos a cada rol de usuario.
  
- Paginación en el Repositorio utilizando PagingAndSortingRepository.

- Implementación de un buscador que utiliza un dato de entrada para buscar y devolver la lista de usuarios que cumplan con los criterios.

- Administración completa de archivos, permitiendo la carga y descarga de documentos, almacenándolos en la carpeta "uploads".
