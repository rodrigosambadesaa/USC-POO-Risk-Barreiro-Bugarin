package gal.sdc.usc.risk.evaluacion;

import java.util.List;
import java.util.Map;

public class GenCommands {
  //
  public static final String CREAR_MAPA = "crear mapa";
  public static final String OBTENER_FRONTERA = "obtener frontera";
  public static final String OBTENER_CONTINENTE = "obtener continente";
  public static final String OBTENER_COLOR = "obtener color";
  public static final String OBTENER_PAISES = "obtener paises";
  public static final String CREAR_JUGADOR = "crear jugador";
  public static final String CREAR_JUGADORES = "crear jugadores";
  public static final String ASIGNAR_MISION = "asignar mision";
  public static final String ASIGNAR_MISIONES = "asignar misiones";
  public static final String ASIGNAR_PAIS = "asignar pais";
  public static final String ASIGNAR_PAISES = "asignar paises";
  public static final String REPARTIR_EJERCITOS = "repartir ejercitos";
  // public final static String REPARTIR_EJERCITOS=   "repartir ejercitos";
  public static final String CAMBIAR_CARTAS = "cambiar cartas";
  // public final static String CAMBIAR_CARTAS_TODAS= "cambiar cartas todas";
  public static final String ACABAR_TURNO = "acabar turno";
  public static final String JUGADOR = "jugador";
  public static final String DESCRIBIR_JUGADOR = "describir jugador";
  public static final String DESCRIBIR_PAIS = "describir pais";
  public static final String DESCRIBIR_CONTINENTE = "describir continente";
  // public final static String VER_MAPA=             "ver mapa";
  public static final String ATACAR = "atacar";
  public static final String REARMAR = "rearmar";
  public static final String ASIGNAR_CARTA = "asignar carta";

  //
  public static final List<String> COMANDS =
      List.of(
          CREAR_MAPA,
          OBTENER_FRONTERA,
          OBTENER_CONTINENTE,
          OBTENER_COLOR,
          OBTENER_PAISES,
          CREAR_JUGADOR,
          CREAR_JUGADORES,
          ASIGNAR_MISION,
          ASIGNAR_MISIONES,
          ASIGNAR_PAIS,
          ASIGNAR_PAISES,
          REPARTIR_EJERCITOS,
          // REPARTIR_EJERCITOS,
          CAMBIAR_CARTAS,
          // CAMBIAR_CARTAS_TODAS,
          ACABAR_TURNO,
          JUGADOR,
          DESCRIBIR_JUGADOR,
          DESCRIBIR_PAIS,
          DESCRIBIR_CONTINENTE,
          // VER_MAPA,
          ATACAR,
          REARMAR,
          ASIGNAR_CARTA);
  //
  public static final Map<String, Float> COMMAND_MARKS =
      Map.ofEntries(
          Map.entry(CREAR_MAPA, 6.0f),
          Map.entry(OBTENER_FRONTERA, 1.5f),
          Map.entry(OBTENER_CONTINENTE, 1.5f),
          Map.entry(OBTENER_COLOR, 1.5f),
          Map.entry(OBTENER_PAISES, 1.5f),
          Map.entry(CREAR_JUGADOR, 1.5f),
          Map.entry(CREAR_JUGADORES, 1.0f),
          Map.entry(ASIGNAR_MISION, 1.5f),
          Map.entry(ASIGNAR_MISIONES, 1.0f),
          Map.entry(ASIGNAR_PAIS, 1.5f),
          Map.entry(ASIGNAR_PAISES, 1.0f),
          Map.entry(REPARTIR_EJERCITOS, 1.5f),
          Map.entry(CAMBIAR_CARTAS, 1.5f),
          Map.entry(ACABAR_TURNO, 1.0f),
          Map.entry(JUGADOR, 0.5f),
          Map.entry(DESCRIBIR_JUGADOR, 1.5f),
          Map.entry(DESCRIBIR_PAIS, 1.5f),
          Map.entry(DESCRIBIR_CONTINENTE, 1.5f),
          Map.entry(ATACAR, 4.5f),
          Map.entry(REARMAR, 1.5f),
          Map.entry(ASIGNAR_CARTA, 1.5f));
}
