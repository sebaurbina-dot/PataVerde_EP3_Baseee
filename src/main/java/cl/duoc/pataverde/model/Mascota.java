package cl.duoc.pataverde.model;

/**
 * Datos comunes de todas las mascotas de la clínica.
 * <p>
 * El identificador (id) vale 0 mientras la mascota no ha sido almacenada.
 * Los métodos set rechazan los datos inválidos con una IllegalArgumentException
 * cuyo mensaje explica el motivo.
 */
public abstract class Mascota {

    public static final int EDAD_MINIMA = 0;
    public static final int EDAD_MAXIMA = 30;

    private int id;
    private String nombre;
    private int edad;
    private double peso;

    protected Mascota(String nombre, int edad, double peso) {
        setNombre(nombre);
        setEdad(edad);
        setPeso(peso);
    }

    /** @return identificador de la mascota (0 si aún no ha sido almacenada). */
    public int getId() {
        return id;
    }

    /** @param id identificador asignado al almacenar la mascota (no negativo). */
    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("El identificador no puede ser negativo.");
        }
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    /** @param nombre no puede ser nulo ni vacío. */
    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        this.nombre = nombre.trim();
    }

    public int getEdad() {
        return edad;
    }

    /** @param edad en años, entre 0 y 30. */
    public void setEdad(int edad) {
        if (edad < EDAD_MINIMA || edad > EDAD_MAXIMA) {
            throw new IllegalArgumentException("La edad debe estar entre 0 y 30 años.");
        }
        this.edad = edad;
    }

    public double getPeso() {
        return peso;
    }

    /** @param peso en kilogramos, mayor que cero. */
    public void setPeso(double peso) {
        if (peso <= 0) {
            throw new IllegalArgumentException("El peso debe ser mayor que cero.");
        }
        this.peso = peso;
    }

    /** @return tipo de la mascota: "Perro" o "Gato". */
    public abstract String getTipo();

    /** @return costo de la consulta veterinaria, en pesos. */
    public abstract double calcularCostoConsulta();
}
