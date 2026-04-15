package autonoma.nave_epacial.gameObjects;

/**
 * La clase Chronometer proporciona una funcionalidad de temporizador simple
 * para controlar eventos basados en el tiempo dentro del juego.
 * Permite ejecutar una cuenta regresiva o esperar un intervalo específico
 * antes de detenerse automáticamente.
 * @version 1.0
 */
public class Chronometer {
    /** Tiempo acumulado desde el inicio del cronómetro en milisegundos. */
    private double delta;
    /** Marca de tiempo del sistema en la última actualización. */
    private double lastTime;
    /** Duración total del intervalo de tiempo a cronometrar. */
    private long time;
    /** Indica si el cronómetro se encuentra actualmente activo. */
    private boolean running;

    /**
     * Construye un nuevo Chronometer inicializando los valores por defecto.
     * El cronómetro comienza en estado detenido.
     */
    public Chronometer() {
        this.delta = 0;
        this.lastTime = System.currentTimeMillis();
        this.running = false;
    }

    /**
     * Inicia el cronómetro con un tiempo de duración específico.
     * * @param time La duración en milisegundos que el cronómetro debe correr.
     */
    public void run (long time)
    {
        this.running = true;
        this.time = time;
    }

    /**
     * Actualiza el estado del cronómetro. Calcula el tiempo transcurrido
     * desde la última actualización y verifica si se ha alcanzado el tiempo
     * objetivo para detener la ejecución.
     */
    public void update () {
        if  (this.running)
            this.delta += System.currentTimeMillis() - this.lastTime;

        if (this.delta >=this.time) {
            this.running = false;
            this.delta = 0;
        }
        this.lastTime = System.currentTimeMillis();
    }

    /**
     * Verifica si el cronómetro está actualmente en ejecución.
     * * @return {@code true} si el cronómetro está corriendo, {@code false} en caso contrario.
     */
    public boolean isRunning ()
    {
        return this.running;
    }
}