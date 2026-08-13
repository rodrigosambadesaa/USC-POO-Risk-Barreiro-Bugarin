package gal.sdc.usc.risk.domain.ejercito;

import gal.sdc.usc.risk.domain.Ejercito;
import gal.sdc.usc.risk.util.Colores;

public abstract class EjercitoBase extends Ejercito {
  public EjercitoBase(Colores.Color color) {
    super(0, color);
  }

  public EjercitoBase(int cantidad, Colores.Color color) {
    super(cantidad, color);
  }
}
