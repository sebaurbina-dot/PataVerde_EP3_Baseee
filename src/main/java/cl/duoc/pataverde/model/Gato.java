package cl.duoc.pataverde.model;

/**
 * Gato atendido en la clínica.
 * <p>
 * Costo de consulta: base de $12.000; si vive en exterior, se incrementa en un 15%.
 */
public class Gato extends Mascota {

    public static final String TIPO = "Gato";
    private static final double COSTO_BASE = 12000;
    private static final double RECARGO_EXTERIOR = 0.15;

    private boolean viveExterior;

    public Gato(String nombre, int edad, double peso, boolean viveExterior) {
        super(nombre, edad, peso);
        this.viveExterior = viveExterior;
    }

    public boolean isViveExterior() {
        return viveExterior;
    }

    public void setViveExterior(boolean viveExterior) {
        this.viveExterior = viveExterior;
    }

    @Override
    public String getTipo() {
        return TIPO;
    }

    @Override
    public double calcularCostoConsulta() {
        double costo = COSTO_BASE;
        if (viveExterior) {
            costo += COSTO_BASE * RECARGO_EXTERIOR;
        }
        return costo;
    }
}
