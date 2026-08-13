package gal.sdc.usc.risk.domain.carta.caballeria;

import gal.sdc.usc.risk.domain.Pais;
import gal.sdc.usc.risk.domain.carta.Caballeria;
import gal.sdc.usc.risk.domain.valores.SubEquipamientos;

public class DeCaballo extends Caballeria {
  private static final SubEquipamientos SUBEQUIPAMIENTO = SubEquipamientos.DECABALLO;

  public DeCaballo(Pais pais) {
    super(pais, DeCaballo.SUBEQUIPAMIENTO);
  }

  @Override
  public int obtenerRearme() {
    return DeCaballo.SUBEQUIPAMIENTO.getEjercitos();
  }
}
