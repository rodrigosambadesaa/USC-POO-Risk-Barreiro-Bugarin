package gal.sdc.usc.risk.domain;

import gal.sdc.usc.risk.domain.carta.artilleria.Antiaerea;
import gal.sdc.usc.risk.domain.carta.artilleria.DeCampanha;
import gal.sdc.usc.risk.domain.carta.caballeria.DeCaballo;
import gal.sdc.usc.risk.domain.carta.caballeria.DeCamello;
import gal.sdc.usc.risk.domain.carta.infanteria.Fusilero;
import gal.sdc.usc.risk.domain.carta.infanteria.Granadero;
import gal.sdc.usc.risk.domain.valores.Equipamientos;
import gal.sdc.usc.risk.domain.valores.SubEquipamientos;
import java.util.Objects;

public abstract class Carta {
  private final SubEquipamientos subEquipamiento;
  private final Equipamientos equipamiento;
  private final Pais pais;

  protected Carta(Pais pais, SubEquipamientos subEquipamiento, Equipamientos equipamiento) {
    this.pais = pais;
    this.subEquipamiento = subEquipamiento;
    this.equipamiento = equipamiento;
  }

  public Equipamientos getEquipamiento() {
    return this.equipamiento;
  }

  public SubEquipamientos getSubEquipamiento() {
    return subEquipamiento;
  }

  public Pais getPais() {
    return pais;
  }

  public String getNombre() {
    return this.subEquipamiento.getNombre() + "&" + pais.getAbreviatura();
  }

  public abstract int obtenerRearme();

  @Override
  public String toString() {
    return "Carta{" + "subEquipamiento=" + subEquipamiento + ", pais=" + pais + '}';
  }

  public static class Builder {
    private SubEquipamientos subEquipamiento;
    private Pais pais;

    public Builder() {}

    public Builder withSubEquipamiento(SubEquipamientos subEquipamiento) {
      this.subEquipamiento = subEquipamiento;
      return this;
    }

    public Builder withPais(Pais pais) {
      this.pais = pais;
      return this;
    }

    public Carta build() {
      Objects.requireNonNull(subEquipamiento, "El equipamiento de la carta es obligatorio");
      Objects.requireNonNull(pais, "El país de la carta es obligatorio");
      return switch (this.subEquipamiento) {
        case FUSILERO:
          yield new Fusilero(this.pais);
        case GRANADERO:
          yield new Granadero(this.pais);
        case ANTIAEREA:
          yield new Antiaerea(this.pais);
        case DECABALLO:
          yield new DeCaballo(this.pais);
        case DECAMELLO:
          yield new DeCamello(this.pais);
        case DECAMPANHA:
          yield new DeCampanha(this.pais);
      };
    }
  }
}
