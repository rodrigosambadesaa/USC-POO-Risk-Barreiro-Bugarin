package gal.sdc.usc.risk.domain.carta;

import gal.sdc.usc.risk.domain.Carta;
import gal.sdc.usc.risk.domain.Pais;
import gal.sdc.usc.risk.domain.valores.Equipamientos;
import gal.sdc.usc.risk.domain.valores.SubEquipamientos;

public abstract class Caballeria extends Carta {
  private static final Equipamientos EQUIPAMIENTO = Equipamientos.CABALLERIA;

  protected Caballeria(Pais pais, SubEquipamientos subEquipamiento) {
    super(pais, subEquipamiento, Caballeria.EQUIPAMIENTO);
  }
}
