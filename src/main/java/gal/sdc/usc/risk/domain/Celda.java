package gal.sdc.usc.risk.domain;

import java.util.Objects;

public class Celda {
  private static final int MAX_X = Mapa.MAX_PAISES_X - 1;
  private static final int MAX_Y = Mapa.MAX_PAISES_Y - 1;

  private final int x;
  private final int y;

  private Celda(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public Celda getOeste() {
    if (this.x <= 0) {
      return null;
    }
    return new Celda(this.x - 1, this.y);
  }

  public Celda getEste() {
    if (this.x >= Celda.MAX_X) {
      return null;
    }
    return new Celda(this.x + 1, this.y);
  }

  public Celda getNorte() {
    if (this.y <= 0) {
      return null;
    }
    return new Celda(this.x, this.y - 1);
  }

  public Celda getSur() {
    if (this.y >= Celda.MAX_Y) {
      return null;
    }
    return new Celda(this.x, this.y + 1);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Celda celda = (Celda) o;
    return x == celda.x && y == celda.y;
  }

  @Override
  public int hashCode() {
    return Objects.hash(x, y);
  }

  @Override
  public String toString() {
    return "Celda{" + "x=" + x + ", y=" + y + '}';
  }

  public static class Builder {
    private Integer x;
    private Integer y;

    public Builder() {}

    public Builder withX(int x) {
      this.x = x;
      return this;
    }

    public Builder withY(int y) {
      this.y = y;
      return this;
    }

    public Celda build() {
      Objects.requireNonNull(x, "La coordenada x es obligatoria");
      Objects.requireNonNull(y, "La coordenada y es obligatoria");
      if (x < 0 || x > MAX_X || y < 0 || y > MAX_Y) {
        throw new IllegalArgumentException("Coordenadas fuera del tablero: " + x + "," + y);
      }
      return new Celda(x, y);
    }
  }
}
