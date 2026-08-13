package gal.sdc.usc.risk.domain.carta;

import gal.sdc.usc.risk.domain.Carta;
import gal.sdc.usc.risk.domain.Pais;
import gal.sdc.usc.risk.domain.valores.Equipamientos;
import gal.sdc.usc.risk.domain.valores.SubEquipamientos;

public abstract class Artilleria extends Carta {
  private static final Equipamientos EQUIPAMIENTO = Equipamientos.ARTILLERIA;

  protected Artilleria(Pais pais, SubEquipamientos subEquipamiento) {
    super(pais, subEquipamiento, Artilleria.EQUIPAMIENTO);
  }
}
