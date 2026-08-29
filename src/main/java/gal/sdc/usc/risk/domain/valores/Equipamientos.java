package gal.sdc.usc.risk.domain.valores;

public enum Equipamientos {
  CABALLERIA("Caballería", 8),
  INFANTERIA("Infantería", 6),
  ARTILLERIA("Artillería", 10);

  private final String nombre;
  private final Integer ejercitos;

  Equipamientos(String nombre, Integer ejercitos) {
    this.nombre = nombre;
    this.ejercitos = ejercitos;
  }

  public static Equipamientos toEquipamientos(String equipamiento) {
    if (equipamiento == null) {
      return null;
    }
    String valor = equipamiento.strip();
    for (Equipamientos equipamientos : Equipamientos.values()) {
      if (valor.equalsIgnoreCase(equipamientos.getNombre())) {
        return equipamientos;
      }
    }
    return null;
  }

  public String getNombre() {
    return this.nombre;
  }

  public Integer getEjercitos() {
    return this.ejercitos;
  }
}
