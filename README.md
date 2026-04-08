# 🚀 Nave Espacial

## 🎮 Descripción

**Nave Espacial** es un videojuego multijugador desarrollado en Java, inspirado en el clásico *Asteroids*, donde hasta **4 jugadores** pueden enfrentarse en combates espaciales en tiempo real.

Los jugadores controlan naves capaces de moverse, rotar y disparar, mientras enfrentan amenazas como **meteoritos destructibles** y **enemigos tipo UFO** que también pueden atacar.

💥 La supervivencia depende de tus reflejos: cualquier colisión puede significar la destrucción.

## 🎯 Características

### 🎮 Jugabilidad
- Movimiento y rotación de la nave
- Sistema de disparo con control de velocidad
- Física basada en vectores (inercia y aceleración)

### 🌌 Entorno
- Generación dinámica de meteoritos
- División de meteoritos en tamaños más pequeños
- Enemigos tipo UFO con comportamiento ofensivo

### 💥 Interacciones
- Sistema de colisiones entre objetos
- Destrucción de entidades al impactar
- Animaciones de explosiones

### 🧠 Sistema
- Sistema de puntaje
- Arquitectura modular del juego
- Base para implementación de multijugador (hasta 4 jugadores)

## 🎮 Controles

JUGADOR 1
- ⬆️ Flecha arriba → Acelerar
- ⬅️ Flecha izquierda → Girar a la izquierda
- ➡️ Flecha derecha → Girar a la derecha
- 🔫 P → Disparar

JUGADOR 2
- W Letra W → Acelerar
- A Letra A → Girar a la izquierda
- S Letra S → Girar a la derecha
- 🔫 R → Disparar

## 🛠️ Tecnologías utilizadas

| Tecnología   | Descripción |
|-------------|------------|
| ☕ Java      | Lenguaje principal del proyecto |
| 🖼️ Swing/AWT | Renderizado gráfico y manejo de interfaz |
| 📦 Maven     | Gestión de dependencias y build del proyecto |
| 🌿 Git Flow  | Control de versiones y organización del desarrollo |

## ▶️ Cómo ejecutar el proyecto

### 🔹 Requisitos

- Java 17 o superior
- Maven instalado
- IDE recomendado: IntelliJ IDEA

---

### 🔹 Ejecución con Maven

- Clonar el repositorio:

```bash
git clone <https://github.com/Teoqm/NaveEpacial_INGII.git>

🔹 Ejecución desde IntelliJ IDEA
Abrir el proyecto en IntelliJ
Esperar a que Maven cargue las dependencias
Ejecutar la clase Main
¡Disfrutar el juego! 🚀

## 📂 Estructura del proyecto

src/
└── autonoma.nave_epacial/
├── gameObjects # Objetos del juego (Player, Meteor, UFO, Laser, etc.)
├── graphics # Manejo de recursos (Assets, Loader)
├── gui # Ventana principal y renderizado
├── input # Manejo de teclado
├── math # Cálculos matemáticos (Vector2D)
├── network # Base para multijugador
├── states # Estados del juego (GameState, etc.)
├── ui # Elementos de interfaz
└── main # Punto de entrada del programa

resources/
├── effects
├── explosion
├── lasers
├── meteors
├── ships
├── sounds
└── ui

El proyecto está organizado en paquetes siguiendo una arquitectura modular:

- **gameObjects** → Contiene las entidades del juego (jugador, meteoritos, enemigos, disparos)
- **graphics** → Manejo de imágenes y recursos
- **gui** → Ventana principal y ciclo del juego
- **input** → Captura de entradas del usuario (teclado)
- **math** → Clases matemáticas como vectores
- **network** → Base para implementación de multijugador
- **states** → Manejo de estados del juego
- **ui** → Elementos de interfaz gráfica
- **main** → Clase principal para ejecutar el juego

## 🧠 Mecánicas del juego

- 🚀 **Movimiento con inercia**:  
  La nave utiliza un sistema basado en vectores, lo que genera un movimiento realista con aceleración y desaceleración.

- 🔄 **Rotación angular**:  
  La orientación de la nave se controla mediante un ángulo, permitiendo girar en cualquier dirección.

- 🔫 **Sistema de disparo**:  
  Los proyectiles se generan en la dirección de la nave y utilizan un control de frecuencia de disparo.

- ☄️ **Meteoritos dinámicos**:  
  Los meteoritos se generan aleatoriamente y se desplazan por la pantalla.

- 💥 **División de meteoritos**:  
  Al destruir un meteorito, este puede dividirse en otros más pequeños.

- 👾 **Enemigos (UFO)**:  
  Los enemigos tienen comportamiento propio y pueden disparar contra el jugador.

- 💣 **Sistema de colisiones**:  
  Se detectan colisiones entre objetos mediante cálculos de distancia, destruyendo entidades al impactar.

- 🎇 **Explosiones animadas**:  
  Al destruir objetos se generan animaciones que mejoran la experiencia visual.

- 🧮 **Sistema de puntaje**:  
  Se registran los puntos obtenidos por el jugador al destruir enemigos o meteoritos.

## 👨‍💻 Autores

- Mateo Quintero  
- Juan Jacobo Cañas  
- Juan Hernández  
- Juan José Morales  

---

## 📌 Notas

Este proyecto fue desarrollado como parte del curso de Ingeniería de Software, aplicando conceptos de:

- Programación orientada a objetos (POO)
- Manejo de eventos
- Renderizado gráfico en Java
- Arquitectura modular
- Sockets
- Control de versiones con Git y Git Flow

El desarrollo del proyecto se realizó siguiendo buenas prácticas de organización de código y trabajo en equipo.
