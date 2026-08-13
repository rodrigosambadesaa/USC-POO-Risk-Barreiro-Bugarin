package gal.sdc.usc.risk.domain.carta.infanteria;

import gal.sdc.usc.risk.domain.Pais;
import gal.sdc.usc.risk.domain.carta.Infanteria;
import gal.sdc.usc.risk.domain.valores.SubEquipamientos;

public class Fusilero extends Infanteria {
  private static final SubEquipamientos SUBEQUIPAMIENTO = SubEquipamientos.FUSILERO;

  public Fusilero(Pais pais) {
    super(pais, Fusilero.SUBEQUIPAMIENTO);
  }

  @Override
  public int obtenerRearme() {
    return Fusilero.SUBEQUIPAMIENTO.getEjercitos();
  }
}
