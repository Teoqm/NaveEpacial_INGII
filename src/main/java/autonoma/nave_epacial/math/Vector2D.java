package autonoma.nave_epacial.math;

/**
 * La clase Vector2D representa un vector en un espacio bidimensional (x, y).
 * Proporciona métodos esenciales para el cálculo de físicas en el juego,
 * incluyendo suma de vectores, normalización, cálculo de magnitudes y
 * manipulación de direcciones mediante ángulos.
 * @version 1.0
 */
public class Vector2D {
	/** Componente horizontal del vector. */
	private double x;
	/** Componente vertical del vector. */
	private double y;

	/**
	 * Construye un nuevo vector con las coordenadas especificadas.
	 * @param x Componente X.
	 * @param y Componente Y.
	 */
	public Vector2D(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * Construye un vector nulo (0, 0).
	 */
	public Vector2D()
	{
		x = 0;
		y = 0;
	}

	/**
	 * Calcula el ángulo del vector en radianes utilizando el arcoseno.
	 * @return El ángulo del vector.
	 */
	public double getAngle(){
		return Math.asin(y/getMagnitude());
	}

	/**
	 * Suma este vector con otro vector dado.
	 * @param v El vector a sumar.
	 * @return Un nuevo {@link Vector2D} con el resultado de la suma.
	 */
	public Vector2D add(Vector2D v)
	{
		return new Vector2D(x + v.getX(), y + v.getY());
	}

	/**
	 * Resta un vector dado a este vector.
	 * @param v El vector a restar.
	 * @return Un nuevo {@link Vector2D} con el resultado de la resta.
	 */
	public Vector2D subtract(Vector2D v)
	{
		return new Vector2D(x - v.getX(), y - v.getY());
	}

	/**
	 * Escala el vector multiplicando sus componentes por un valor escalar.
	 * @param value El valor por el cual multiplicar.
	 * @return Un nuevo {@link Vector2D} escalado.
	 */
	public Vector2D scale(double value)
	{
		return new Vector2D(x*value, y*value);
	}

	/**
	 * Limita la magnitud del vector a un valor máximo especificado.
	 * Si la magnitud actual supera el límite, el vector se normaliza y se escala al límite.
	 * @param value La magnitud máxima permitida.
	 * @return El vector limitado (ya sea el original o uno nuevo escalado).
	 */
	public Vector2D limit(double value)
	{
		if(getMagnitude() > value){
			return this.normalize().scale(value);
		}
		return this;
	}

	/**
	 * Normaliza el vector para que su magnitud sea igual a 1 (vector unitario).
	 * @return Un nuevo {@link Vector2D} normalizado.
	 */
	public Vector2D normalize()
	{
		double magnitude = getMagnitude();
		return new Vector2D(x / magnitude, y / magnitude);
	}

	/**
	 * Calcula la magnitud (longitud) del vector utilizando el teorema de Pitágoras.
	 * @return La magnitud del vector.
	 */
	public double getMagnitude()
	{
		return Math.sqrt(x*x + y*y);
	}

	/**
	 * Cambia la dirección del vector manteniendo su magnitud actual.
	 * @param angle El nuevo ángulo en radianes.
	 * @return Un nuevo {@link Vector2D} orientado hacia el ángulo dado.
	 */
	public Vector2D setDirection(double angle)
	{
		double magnitude = getMagnitude();
		return new Vector2D(Math.cos(angle)*magnitude, Math.sin(angle)*magnitude);
	}

	/** @return La componente X. */
	public double getX() {
		return x;
	}

	/** @param x Nueva componente X. */
	public void setX(double x) {
		this.x = x;
	}

	/** @return La componente Y. */
	public double getY() {
		return y;
	}

	/** @param y Nueva componente Y. */
	public void setY(double y) {
		this.y = y;
	}
}