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

### 🎨 Interfaz Personalizable
- **Vista Compacta**: Muestra solo la tarea seleccionada y el temporizador
- **Vista Expandida**: Muestra todas las tareas y controles completos
- Ventana arrastrable por toda la pantalla
- Siempre visible encima de otras ventanas
- Transparencia ajustable
- Diseño moderno con colores suaves (azul oscuro/gris)

### ⚙️ Personalización
- Establecer apodo de usuario
- Ajustar transparencia de la ventana
- Las preferencias se guardan automáticamente

## Requisitos del Sistema

- Java 17 o superior
- Maven 3.6 o superior
- JavaFX 20.0.1 (incluido en las dependencias)

## Instalación y Ejecución

### 1. Compilar el proyecto
```bash
mvn clean compile
```

### 2. Ejecutar la aplicación
```bash
mvn javafx:run
```

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

## Uso de la Aplicación

### Vista Compacta
1. Al iniciar, verás una ventana pequeña con:
    - Tu apodo personalizado
    - La tarea seleccionada actualmente
    - El temporizador activo
    - Botón de configuración (⚙)

2. Arrastra la ventana a cualquier posición de tu pantalla

### Vista Expandida
1. Haz clic en el botón ⚙ para expandir
2. En esta vista puedes:
    - Ver todas tus tareas
    - Agregar nuevas tareas con colores
    - Marcar tareas como completadas (checkbox)
    - Eliminar tareas (botón ×)
    - Seleccionar una tarea haciendo clic en ella
    - Acceder al panel de temporizador
    - Modificar ajustes

### Usando el Temporizador
1. Haz clic en "⏱ Timer" en la vista expandida
2. Para cronómetro: Haz clic en "Iniciar Cronómetro"
3. Para cuenta regresiva:
    - Ingresa los minutos deseados
    - Haz clic en "Iniciar"
    - La alarma sonará cuando termine

### Personalización
1. Haz clic en "⚙ Ajustes"
2. Modifica tu apodo
3. Ajusta la transparencia con el slider
4. Haz clic en "OK" para guardar

## Colores de Tareas Disponibles

- **Azul**: #4A90E2
- **Verde**: #50C878
- **Naranja**: #F5A623
- **Rojo**: #E94B3C
- **Morado**: #9B59B6
- **Turquesa**: #1ABC9C

## Características Técnicas

- **Persistencia**: Las preferencias se guardan en `~/.todoapp_prefs.json`
- **Transparencia**: Rango de 0.3 a 1.0
- **Ventana**: Siempre visible encima de otras aplicaciones
- **Arrastre**: Funciona en cualquier área de la ventana
- **Diseño**: Responsive con animaciones suaves

## Atajos y Consejos

- Presiona Enter en el campo de texto para agregar una tarea rápidamente
- Haz clic en una tarea para seleccionarla y verla en vista compacta
- El temporizador sigue corriendo incluso en vista compacta
- Las tareas completadas se muestran tachadas y semitransparentes
- La ventana se puede mover a cualquier monitor en configuraciones multi-pantalla

## Notas Importantes

- La alarma requiere el archivo `alarm.wav` en los recursos (actualmente es un placeholder)
- Para agregar un sonido de alarma real, reemplaza `src/main/resources/com/todoapp/alarm.wav` con tu archivo .wav preferido
- Las tareas no persisten entre sesiones (se pueden agregar en futuras versiones)

## Solución de Problemas

### La aplicación no inicia
- Verifica que tienes Java 17 o superior: `java -version`
- Asegúrate de tener Maven instalado: `mvn -version`

### Error de JavaFX
- El plugin de Maven debería descargar JavaFX automáticamente
- Si hay problemas, ejecuta: `mvn clean install`

### La alarma no suena
- Verifica que el archivo alarm.wav existe y es un archivo WAV válido
- Ajusta el volumen del sistema

## Licencia

Este proyecto es de código abierto y está disponible para uso personal y educativo.
