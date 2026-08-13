package gal.sdc.usc.risk.domain.carta.artilleria;

import gal.sdc.usc.risk.domain.Pais;
import gal.sdc.usc.risk.domain.carta.Artilleria;
import gal.sdc.usc.risk.domain.valores.SubEquipamientos;

public class DeCampanha extends Artilleria {
  private static final SubEquipamientos SUBEQUIPAMIENTO = SubEquipamientos.DECAMPANHA;

  public DeCampanha(Pais pais) {
    super(pais, DeCampanha.SUBEQUIPAMIENTO);
  }

  @Override
  public int obtenerRearme() {
    return DeCampanha.SUBEQUIPAMIENTO.getEjercitos();
  }
}
