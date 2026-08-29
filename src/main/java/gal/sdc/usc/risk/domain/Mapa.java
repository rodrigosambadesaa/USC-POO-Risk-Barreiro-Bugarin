package gal.sdc.usc.risk.domain;

import gal.sdc.usc.risk.domain.valores.EnlacesMaritimos;
import gal.sdc.usc.risk.util.Colores;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class Mapa {
  public static final int MAX_PAISES_X = 11;
  public static final int MAX_PAISES_Y = 8;
  public static final int MAX_ENLACES_MARITIMOS = 6;

  public static final int CONTINENTES_CON_4_PAISES = 2;
  public static final int CONTINENTES_CON_6_PAISES = 1;
  public static final int CONTINENTES_CON_7_PAISES = 1;
  public static final int CONTINENTES_CON_9_PAISES = 1;
  public static final int CONTINENTES_CON_12_PAISES = 1;

  private final HashMap<String, Continente> continentes;
  private final HashMap<Celda, Pais> paises;

  private boolean tieneFronteras = false;

  private Mapa(HashMap<String, Continente> continentes, HashMap<Celda, Pais> paises) {
    this.continentes = new LinkedHashMap<>(continentes);
    this.paises = new LinkedHashMap<>(paises);
  }

  public HashMap<String, Continente> getContinentes() {
    return new LinkedHashMap<>(continentes);
  }

  public HashMap<Celda, Pais> getPaisesPorCeldas() {
    return new LinkedHashMap<>(paises);
  }

  public Pais getPaisPorNombre(String nombre) {
    if (nombre == null) {
      return null;
    }

    return this.paises.values().stream()
        .filter(
            pais ->
                pais.getAbreviatura().equalsIgnoreCase(nombre)
                    || pais.getNombre().equalsIgnoreCase(nombre))
        .findAny()
        .orElse(null);
  }

  public Continente getContinentePorNombre(String nombre) {
    if (nombre == null) {
      return null;
    }

    return this.continentes.values().stream()
        .filter(
            continente ->
                continente.getAbreviatura().equalsIgnoreCase(nombre)
                    || continente.getNombre().equalsIgnoreCase(nombre))
        .findAny()
        .orElse(null);
  }

  public List<Continente> getContinentesPorJugador(Jugador jugador) {
    List<Continente> resultado = new ArrayList<>();
    for (Continente continente : this.continentes.values()) {
      if (continente.getJugador() != null && continente.getJugador().equals(jugador)) {
        resultado.add(continente);
      }
    }
    return resultado;
  }

  public List<Pais> getPaisesPorJugador(Jugador jugador) {
    if (jugador == null) {
      return List.of();
    }
    return this.paises.values().stream()
        .filter(pais -> jugador.equals(pais.getJugador()))
        .toList();
  }

  private void asignarFronteras() {
    if (this.tieneFronteras) {
      return;
    }

    Celda celda;
    Pais pais, aux;
    for (int i = 0; i < Mapa.MAX_PAISES_Y; i++) {
      for (int j = 0; j < Mapa.MAX_PAISES_X; j++) {
        celda = new Celda.Builder().withX(j).withY(i).build();
        pais = this.paises.get(celda);
        if (pais == null) {
          continue;
        }

        Fronteras.Builder preFronteras = new Fronteras.Builder();

        aux = this.paises.get(celda.getNorte());
        if (aux != null) {
          preFronteras.withNorte(aux);
        }
        aux = this.paises.get(celda.getSur());
        if (aux != null) {
          preFronteras.withSur(aux);
        }
        aux = this.paises.get(celda.getEste());
        if (aux != null) {
          preFronteras.withEste(aux);
        }
        aux = this.paises.get(celda.getOeste());
        if (aux != null) {
          preFronteras.withOeste(aux);
        }

        for (EnlacesMaritimos enlace : EnlacesMaritimos.values()) {
          if (enlace.getPais1().getNombre().equals(pais.getNombre())) {
            preFronteras.withMaritima(this.getPaisPorNombre(enlace.getPais2().getNombre()));
          } else if (enlace.getPais2().getNombre().equals(pais.getNombre())) {
            preFronteras.withMaritima(this.getPaisPorNombre(enlace.getPais1().getNombre()));
          }
        }

        if (!pais.setFronteras(preFronteras.build())) {
          throw new IllegalStateException(
              "El país " + pais.getNombre() + " ya tenía fronteras asignadas");
        }
      }
    }

    this.tieneFronteras = true;
  }

  private String linea(int i) {
    StringBuilder out = new StringBuilder();
    for (int j = 0; j < Mapa.MAX_PAISES_X; j++) {
      if (i == 0) {
        if (j == 0) {
          out.append("╔");
        }
        out.append("════════════");
        if ((j + 1) == Mapa.MAX_PAISES_X) {
          out.append("╗");
        } else {
          out.append("╤");
        }
      } else if (i == Mapa.MAX_PAISES_Y) {
        if (j == 0) {
          out.append("╚");
        }
        out.append("════════════");
        if ((j + 1) == Mapa.MAX_PAISES_X) {
          out.append("╝");
        } else {
          out.append("╧");
        }
      } else {
        if (j == 0) {
          out.append("╟");
        }
        out.append("──────");
        if (((i == 3 || i == 4) && (j == 5 || j == 6)) || ((i == 5 || i == 6) && j == 9)) {
          out.append(new Colores("┃", Colores.Color.ROJO));
        } else {
          out.append("─");
        }
        out.append("─────");
        if ((j + 1) == Mapa.MAX_PAISES_X) {
          out.append("╢");
        } else {
          if (i == 5 && j == 3) {
            out.append(new Colores("┃", Colores.Color.ROJO));
          } else {
            out.append("┼");
          }
        }
      }
    }
    out.append("\n");
    return out.toString();
  }

  @Override
  public String toString() {
    StringBuilder out = new StringBuilder();
    Celda celda;
    Pais pais;
    String texto;
    StringBuilder nombreTemporal;

    for (int i = 0; i < Mapa.MAX_PAISES_Y; i++) {
      // Imprimir paises con sus colores
      for (int j = 0; j < Mapa.MAX_PAISES_X; j++) {
        if (j == 0) {
          out.append(this.linea(i));
        }

        celda = new Celda.Builder().withX(j).withY(i).build();
        pais = this.paises.get(celda);

        if ((i == 0 && (j == 0 || j == 3 || j == 4 || j == 9 || j == 10))
            || (i == 4 && j == 5)
            || (i == 5 && j == 3)) {
          texto = new Colores("━", Colores.Color.ROJO).toString();
        } else if ((i == 4 && j == 4)) {
          texto = new Colores("┏", Colores.Color.ROJO).toString();
        } else if ((i == 5 && j == 4)) {
          texto = new Colores("┛", Colores.Color.ROJO).toString();
        } else if (j == 0) {
          texto = "║";
        } else {
          texto = "│";
        }

        if (pais == null) {
          if ((i == 0 && (j == 3 || j == 9 || j == 10))
              || (i == 4 && j == 4)
              || (i == 5 && j == 3)) {
            texto += new Colores("━━━━━━━━━━━━", Colores.Color.ROJO);
          } else if ((i == 3 && (j == 5 || j == 6)) || (i == 5 && j == 9)) {
            texto += new Colores("      ┃     ", Colores.Color.ROJO);
          } else {
            texto += String.format(" %-18s ", new Colores(""));
          }
        } else {
          nombreTemporal = new StringBuilder(pais.getAbreviatura());
          while (nombreTemporal.length() < Pais.MAX_LENGTH_NOMBRE) {
            nombreTemporal.append(" ");
          }
          texto +=
              String.format(
                  " %-20s ", new Colores(nombreTemporal.toString(), null, pais.getColor()));
        }

        if ((j + 1) == Mapa.MAX_PAISES_X) {
          if (i == 0) {
            texto += new Colores("━", Colores.Color.ROJO).toString() + "\n";
          } else {
            texto += "║\n";
          }
        }

        out.append(texto);
      }

      for (int j = 0; j < Mapa.MAX_PAISES_X; j++) {
        celda = new Celda.Builder().withX(j).withY(i).build();
        pais = this.paises.get(celda);

        if ((i == 4 && j == 4)) {
          texto = new Colores("┃", Colores.Color.ROJO).toString();
        } else if (j == 0) {
          texto = "║";
        } else {
          texto = "│";
        }

        if (pais == null || pais.getJugador() == null) {
          if ((i == 3 && (j == 5 || j == 6)) || (i == 5 && j == 9)) {
            texto += new Colores("      ┃     ", Colores.Color.ROJO);
          } else {
            texto += String.format(" %-18s ", new Colores(""));
          }
        } else {
          nombreTemporal =
              new StringBuilder(
                  pais.getEjercito() == null ? "0" : pais.getEjercito().toInt().toString());
          while (nombreTemporal.length() < Pais.MAX_LENGTH_NOMBRE) {
            nombreTemporal.append(" ");
          }
          texto +=
              String.format(
                  " %-18s ", new Colores(nombreTemporal.toString(), pais.getJugador().getColor()));
        }

        if ((j + 1) == Mapa.MAX_PAISES_X) {
          texto += "║\n";
        }

        out.append(texto);
      }
    }
    out.append(this.linea(Mapa.MAX_PAISES_Y));
    return out.toString();
  }

  public static class Builder {
    private final HashMap<String, Continente> continentes;
    private final HashMap<Celda, Pais> paises;

    public Builder() {
      continentes = new LinkedHashMap<>();
      paises = new LinkedHashMap<>();
    }

    public Builder withContinente(Continente continente) {
      Continente valor = Objects.requireNonNull(continente, "El continente es obligatorio");
      Continente anterior = this.continentes.putIfAbsent(valor.getNombre(), valor);
      if (anterior != null && anterior != valor) {
        throw new IllegalArgumentException("Continente duplicado: " + valor.getNombre());
      }
      return this;
    }

    public Builder withPais(Pais pais) {
      Pais valor = Objects.requireNonNull(pais, "El país es obligatorio");
      Pais anterior = this.paises.putIfAbsent(valor.getCelda(), valor);
      if (anterior != null && anterior != valor) {
        throw new IllegalArgumentException("Celda de país duplicada: " + valor.getCelda());
      }
      return this;
    }

    public Mapa build() {
      return this.build(false);
    }

    public Mapa build(boolean manual) {
      Mapa mapa = new Mapa(continentes, paises);
      if (!manual) {
        mapa.asignarFronteras();
      }
      return mapa;
    }
  }
}
