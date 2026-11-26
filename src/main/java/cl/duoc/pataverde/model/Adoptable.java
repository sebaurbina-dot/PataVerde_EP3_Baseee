package cl.duoc.pataverde.model;

/**
 * Comportamiento de las mascotas que pueden ofrecerse en adopción.
 */
public interface Adoptable {

    /** @return true si la mascota está disponible para adopción. */
    boolean isDisponibleAdopcion();

    /** @param disponibleAdopcion true si la mascota queda disponible para adopción. */
    void setDisponibleAdopcion(boolean disponibleAdopcion);
}
