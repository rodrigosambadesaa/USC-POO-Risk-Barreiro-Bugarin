package gal.sdc.usc.risk.domain.carta.artilleria;

import gal.sdc.usc.risk.domain.Pais;
import gal.sdc.usc.risk.domain.carta.Artilleria;
import gal.sdc.usc.risk.domain.valores.SubEquipamientos;

public class Antiaerea extends Artilleria {
  private static final SubEquipamientos SUBEQUIPAMIENTO = SubEquipamientos.ANTIAEREA;

  public Antiaerea(Pais pais) {
    super(pais, Antiaerea.SUBEQUIPAMIENTO);
  }

  @Override
  public int obtenerRearme() {
    return Antiaerea.SUBEQUIPAMIENTO.getEjercitos();
  }
}
