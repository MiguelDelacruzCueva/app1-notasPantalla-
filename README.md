# Aplicación JavaFX To-Do List

Una aplicación moderna de lista de tareas con temporizador integrado, vista compacta/expandible y personalización completa.

## Características Principales

### 📝 Gestión de Tareas
- Agregar, eliminar y marcar tareas como completadas
- Asignar colores personalizados a cada tarea (6 colores disponibles)
- Vista de lista con indicadores visuales de estado
- Selección de tareas para seguimiento en vista compacta

### ⏱️ Sistema de Temporizador
- **Cronómetro**: Mide el tiempo dedicado a cada tarea
- **Cuenta Regresiva**: Establece tiempo límite para tareas (con alarma suave)
- Controles de pausa/reanudar/detener
- Visualización en formato HH:MM:SS


### ⚙️ Personalización
- Establecer apodo de usuario
- Ajustar transparencia de la ventana
- Las preferencias se guardan automáticamente

## Requisitos del Sistema

- Java 17 o superior
- Maven 3.6 o superior
- JavaFX 20.0.1 (incluido en las dependencias)


## Estructura del Proyecto

```
javafx-todo-app/
├── src/
│   └── main/
│       ├── java/com/todoapp/
│       │   ├── TodoApp.java           # Aplicación principal
│       │   ├── Task.java              # Modelo de tarea
│       │   ├── TimerController.java   # Controlador del temporizador
│       │   └── UserPreferences.java   # Gestor de preferencias
│       └── resources/com/todoapp/
│           ├── styles.css             # Estilos CSS
│           └── alarm.wav              # Sonido de alarma
├── pom.xml
└── README.md
```


## Licencia

Este proyecto es de código abierto y está disponible para uso personal y educativo.
