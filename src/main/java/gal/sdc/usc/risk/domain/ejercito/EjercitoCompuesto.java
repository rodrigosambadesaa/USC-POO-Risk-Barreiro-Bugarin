package gal.sdc.usc.risk.domain.ejercito;

import gal.sdc.usc.risk.domain.Ejercito;
import gal.sdc.usc.risk.util.Colores;

public abstract class EjercitoCompuesto extends Ejercito {
  public EjercitoCompuesto(Colores.Color color) {
    super(0, color);
  }

  public EjercitoCompuesto(int cantidad, Colores.Color color) {
    super(cantidad, color);
  }
}
