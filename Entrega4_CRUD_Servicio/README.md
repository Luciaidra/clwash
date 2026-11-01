# C&L Wash – Sistema de gestión para lavadero de autos

Desarrollo de Sistemas Orientado a Objetos · Lucía Mangioni, Catalina Baviello

Java 17+ · Swing · MySQL 8 · JDBC · MVC + DAO + casos de uso

---

## Qué va en cada entrega

### Entrega 1 – Prototipos de interfaz (Vehículo y Servicio)
| Archivo | Qué es |
|---|---|
| `vista/VehiculoView.java` | Pantalla de vehículos: búsqueda arriba, tabla al centro, formulario a la derecha |
| `vista/ServicioView.java` | Pantalla del catálogo de servicios, misma estructura |
| `vista/prototipos/PrototiposMain.java` | Abre las dos pantallas con datos de ejemplo, sin base ni controlador |

Datos que se piden:
- **Vehículo:** patente, marca, modelo, tipo (combo: Auto, Camioneta, SUV, Utilitario, Moto).
- **Servicio:** nombre, descripción, precio, duración en minutos. El id no se pide porque lo genera la base.

Cómo se listan: en un `JTable` de solo lectura, con un campo de búsqueda. El precio se muestra en pesos (`$ 15.000,00`) y la duración como `60 min`.

### Entrega 2 – Entidad + DAO (Vehículo, con JDBC)
Corresponde a los **"archivos deseados"** de la consigna:
| Archivo pedido | Archivo del proyecto |
|---|---|
| Entidad | `modelo/entidades/Vehiculo.java` (+ `TipoVehiculo.java`) |
| Interfaz DAO con genéricos | `modelo/dao/DAO.java` → `DAO<T, K>` |
| Interfaz DAO de la entidad | `modelo/dao/VehiculoDAO.java` → `extends DAO<Vehiculo, String>` |
| Implementación JDBC | `modelo/dao/impl/VehiculoDAOJDBC.java` |
| Apoyo | `modelo/dao/ConexionBD.java`, `modelo/excepciones/*`, `sql/clwash.sql` |

### Entrega 3 – CRUD completo de Vehículo con MVC
| Capa | Archivos |
|---|---|
| **Modelo** | entidades, DAO y **una clase por caso de uso** (el desafío opcional): `AltaVehiculo`, `ModificarVehiculo`, `EliminarVehiculo`, `BuscarVehiculo`, `ListarVehiculos` + `ValidadorVehiculo` |
| **Vista** | `VehiculoView` (la misma del prototipo, ahora conectada) |
| **Controlador** | `controlador/VehiculoController.java` |
| Arranque | `app/Main.java` |

Flujo (sirve también para el diagrama de secuencia "Registrar vehículo"):

```
Usuario → VehiculoView: clic en Guardar
VehiculoView → VehiculoController: guardar()
VehiculoController → VehiculoView: getPatente(), getMarca(), getModelo(), getTipo()
VehiculoController → AltaVehiculo: ejecutar(vehiculo, dao)
AltaVehiculo → ValidadorVehiculo: validar(vehiculo)
AltaVehiculo → VehiculoDAOJDBC: existePatente(patente)
AltaVehiculo → VehiculoDAOJDBC: insertar(vehiculo)
VehiculoDAOJDBC → MySQL: INSERT INTO vehiculo ...
VehiculoController → VehiculoView: mostrarInfo(), limpiarFormulario(), mostrarVehiculos(lista)
```

### Entrega 4 – CRUD completo de Servicio + menú principal
Misma arquitectura que Vehículo, aplicada a la segunda entidad:
| Capa | Archivos |
|---|---|
| **Modelo** | `dao/ServicioDAO.java` (`extends DAO<Servicio, Integer>`), `dao/impl/ServicioDAOJDBC.java`, casos de uso en `casosdeuso/servicio/`: `AltaServicio`, `ModificarServicio`, `EliminarServicio`, `BuscarServicio`, `ListarServicios` + `ValidadorServicio` |
| **Vista** | `ServicioView` (el prototipo de la entrega 1, ahora conectado) y `MenuPrincipalView` |
| **Controlador** | `ServicioController` y `MenuController` |
| Arranque | `app/Main.java` ahora abre el menú principal |

Reglas de Servicio: nombre obligatorio y sin repetir (sin distinguir mayúsculas), precio mayor que cero, duración entre 5 y 600 minutos, y no se puede borrar un servicio usado en órdenes de lavado.

---

## Manejo de excepciones
Jerarquía propia en `modelo/excepciones`:

```
Exception
├── ValidacionException          → dato mal cargado (patente inválida, campo vacío)
└── DAOException                 → error técnico de acceso a datos (envuelve SQLException)
    ├── EntidadDuplicadaException     → patente ya registrada
    └── EntidadNoEncontradaException  → se busca/modifica/borra algo que no existe
```

- El **DAO** traduce `SQLException` a estas excepciones (las capas de arriba no conocen JDBC).
- Los **casos de uso** lanzan `ValidacionException` según las reglas de negocio.
- El **controlador** las *resuelve*: errores del usuario → advertencia (y no se borra lo que escribió); errores técnicos → mensaje de error + detalle en consola. El programa nunca se cae.
- Casos cubiertos: MySQL apagado, patente duplicada, formato de patente inválido, campos vacíos o muy largos, eliminar un vehículo que tiene órdenes (clave foránea), registro borrado mientras se editaba.

---

## Cómo ejecutarlo
1. Ejecutar `sql/clwash.sql` en MySQL (crea la base `clwash`, las 3 tablas y datos de prueba).
2. Ajustar usuario y clave en `modelo/dao/ConexionBD.java`.
3. Agregar el driver **MySQL Connector/J** al proyecto:
   - NetBeans/IntelliJ: agregar el `.jar` a las librerías, o
   - Maven: `com.mysql:mysql-connector-j:8.4.0`
4. Ejecutar `com.clwash.app.Main` (menú principal → Vehículos / Servicios) o `com.clwash.vista.prototipos.PrototiposMain` (prototipos, no necesita base).

Javadoc: `javadoc -encoding UTF-8 -d doc -sourcepath src -subpackages com.clwash`

## Estructura
```
src/com/clwash/
├── app/Main.java
├── controlador/  VehiculoController, ServicioController, MenuController
├── modelo/
│   ├── entidades/   Vehiculo, TipoVehiculo, Servicio, OrdenLavado, EstadoOrden
│   ├── dao/         DAO<T,K>, VehiculoDAO, ServicioDAO, ConexionBD
│   │   └── impl/    VehiculoDAOJDBC, ServicioDAOJDBC
│   ├── casosdeuso/vehiculo/  Alta, Modificar, Eliminar, Buscar, Listar, Validador
│   ├── casosdeuso/servicio/  Alta, Modificar, Eliminar, Buscar, Listar, Validador
│   └── excepciones/ DAOException, ValidacionException, EntidadDuplicada…, EntidadNoEncontrada…
└── vista/           MenuPrincipalView, VehiculoView, ServicioView
    └── prototipos/  PrototiposMain
sql/clwash.sql
```
