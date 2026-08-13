package gal.sdc.usc.risk.domain;

import gal.sdc.usc.risk.domain.valores.Misiones;
import java.util.Objects;

public class Mision {
  private final Misiones identificador;
  private final String codigo;
  private final String descripcion;

  public Mision(Misiones identificador, String codigo, String descripcion) {
    this.identificador = identificador;
    this.codigo = codigo;
    this.descripcion = descripcion;
  }

  public Misiones getIdentificador() {
    return this.identificador;
  }

  public String getCodigo() {
    return codigo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  @Override
  public String toString() {
    return "Mision{"
        + "identificador="
        + identificador
        + ", codigo='"
        + codigo
        + '\''
        + ", descripcion='"
        + descripcion
        + '\''
        + '}';
  }

  public static class Builder {
    private final Misiones mision;

    public Builder(Misiones mision) {
      this.mision = mision;
    }

    public Mision build() {
      Objects.requireNonNull(mision, "La misión es obligatoria");
      return new Mision(mision, mision.getId(), mision.getNombre());
    }
  }

  @Override
  public boolean equals(Object obj) {
    return obj instanceof Mision other && identificador == other.identificador;
  }

  @Override
  public int hashCode() {
    return identificador.hashCode();
  }
}
