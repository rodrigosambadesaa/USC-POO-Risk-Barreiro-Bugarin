package gal.sdc.usc.risk.domain.valores;

public enum SubEquipamientos {
  GRANADERO(Equipamientos.INFANTERIA, "Granadero", 1),
  FUSILERO(Equipamientos.INFANTERIA, "Fusilero", 2),
  DECABALLO(Equipamientos.CABALLERIA, "DeCaballo", 3),
  DECAMELLO(Equipamientos.CABALLERIA, "DeCamello", 2),
  DECAMPANHA(Equipamientos.ARTILLERIA, "DeCampanha", 4),
  ANTIAEREA(Equipamientos.ARTILLERIA, "Antiaerea", 3);

  private final Equipamientos equipamiento;
  private final String nombre;
  private final Integer ejercitos;

  SubEquipamientos(Equipamientos equipamiento, String nombre, Integer ejercitos) {
    this.equipamiento = equipamiento;
    this.nombre = nombre;
    this.ejercitos = ejercitos;
  }

  public static SubEquipamientos toSubEquipamientos(String subEquipamiento) {
    if (subEquipamiento == null) {
      return null;
    }
    String valor = subEquipamiento.strip();
    for (SubEquipamientos equipamientos : SubEquipamientos.values()) {
      if (valor.equalsIgnoreCase(equipamientos.getNombre())) {
        return equipamientos;
      }
    }
    return null;
  }

  public Equipamientos getEquipamiento() {
    return this.equipamiento;
  }

  public String getNombre() {
    return this.nombre;
  }

  public Integer getEjercitos() {
    return this.ejercitos;
  }
}
