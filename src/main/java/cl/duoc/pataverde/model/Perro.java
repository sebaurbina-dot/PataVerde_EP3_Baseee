package cl.duoc.pataverde.model;

/**
 * Perro atendido en la clínica.
 * <p>
 * Costo de consulta: base de $15.000; si no está vacunado, se incrementa en un 30%.
 */
public class Perro extends Mascota implements Adoptable {

    public static final String TIPO = "Perro";
    private static final double COSTO_BASE = 15000;
    private static final double RECARGO_SIN_VACUNA = 0.30;

    private String raza;
    private boolean vacunado;
    private boolean disponibleAdopcion;

    public Perro(String nombre, int edad, double peso, String raza,
                 boolean vacunado, boolean disponibleAdopcion) {
        super(nombre, edad, peso);
        setRaza(raza);
        this.vacunado = vacunado;
        this.disponibleAdopcion = disponibleAdopcion;
    }

    public String getRaza() {
        return raza;
    }

    /** @param raza no puede ser nula ni vacía. */
    public void setRaza(String raza) {
        if (raza == null || raza.isBlank()) {
            throw new IllegalArgumentException("La raza no puede estar vacía.");
        }
        this.raza = raza.trim();
    }

    public boolean isVacunado() {
        return vacunado;
    }

    public void setVacunado(boolean vacunado) {
        this.vacunado = vacunado;
    }

    @Override
    public boolean isDisponibleAdopcion() {
        return disponibleAdopcion;
    }

    @Override
    public void setDisponibleAdopcion(boolean disponibleAdopcion) {
        this.disponibleAdopcion = disponibleAdopcion;
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public double calcularCostoConsulta() {
        double costo = COSTO_BASE;
        if (!vacunado) {
            costo += COSTO_BASE * RECARGO_SIN_VACUNA;
        }
        return costo;
    }
}
