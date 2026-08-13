package gal.sdc.usc.risk.domain.ejercito;

import gal.sdc.usc.risk.domain.Ejercito;

public class EjercitoNuevo extends Ejercito {
  public EjercitoNuevo() {
    super(0);
  }

  public EjercitoNuevo(int cantidad) {
    super(cantidad);
  }

  @Override
  public int[] ataque(int[] valores) {
    return valores;
  }
}
