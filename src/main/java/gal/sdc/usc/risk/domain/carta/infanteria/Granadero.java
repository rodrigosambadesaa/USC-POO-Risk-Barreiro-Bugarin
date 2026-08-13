package gal.sdc.usc.risk.domain.carta.infanteria;

import gal.sdc.usc.risk.domain.Pais;
import gal.sdc.usc.risk.domain.carta.Infanteria;
import gal.sdc.usc.risk.domain.valores.SubEquipamientos;

public class Granadero extends Infanteria {
  private static final SubEquipamientos SUBEQUIPAMIENTO = SubEquipamientos.GRANADERO;

  public Granadero(Pais pais) {
    super(pais, Granadero.SUBEQUIPAMIENTO);
  }

  @Override
  public int obtenerRearme() {
    return Granadero.SUBEQUIPAMIENTO.getEjercitos();
  }
}
