package autonoma.nave_epacial.Ui;

/**
 * Interfaz que define una acción ejecutable.
 *
 * Se utiliza para encapsular comportamientos que pueden ser
 * ejecutados, por ejemplo, en botones o elementos de la interfaz gráfica.
 *
 * Implementa el principio de encapsulamiento de acciones,
 * permitiendo mayor flexibilidad y reutilización.
 *
 * @author Mateo Quintero
 * @author Juan Jacobo Cañas
 * @author Juan Hernández
 * @author Juan José Morales
 *  * * @version 1.0
 */
public interface Action {

    /**
     * Método que ejecuta la acción definida.
     *
     * Las clases que implementen esta interfaz deben
     * proporcionar la lógica específica de la acción.
     */
    public abstract void doAction();

}