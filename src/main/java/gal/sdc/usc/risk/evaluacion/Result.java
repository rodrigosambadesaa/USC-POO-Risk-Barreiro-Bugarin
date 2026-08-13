package gal.sdc.usc.risk.evaluacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Result {
  private final ArrayList<String> textsOfCommands;
  private final ArrayList<String> textsOfAnswer;
  private final HashMap<String, ArrayList<Command>> commands;

  public Result(String file) {
    this();
    this.readCommandsAndResults(file);
    this.generateCommandRepresentation();
  }

  private Result() {
    textsOfCommands = new ArrayList<>();
    textsOfAnswer = new ArrayList<>();
    commands = new HashMap<>();
  }

  public static Result from(BufferedReader reader) {
    Result result = new Result();
    result.readCommandsAndResults(reader);
    result.generateCommandRepresentation();
    return result;
  }

  private void generateCommandRepresentation() {
    for (int i = 0; i < this.textsOfAnswer.size(); i++) {
      String answer = this.textsOfAnswer.get(i);
      Command command = new Command("[" + i + "] " + this.textsOfCommands.get(i));
      Scanner scan = new Scanner(answer);
      ArrayList<Answer> listOfAnswers = new ArrayList<>();
      while (scan.hasNextLine()) {
        if (scan.nextLine().replaceAll("\\s+", "").trim().equals("{")) {
          StringBuilder result = new StringBuilder();
          String linea = scan.nextLine();
          while (!linea.replaceAll("\\s+", "").trim().equals("}")) {
            result.append(linea).append("\n");
            linea = scan.nextLine();
          }
          listOfAnswers.add(new Answer(result.toString()));
        }
      }
      command.setAnswers(listOfAnswers);
      if (this.commands.get(this.generalCommand(this.textsOfCommands.get(i))) != null) {
        this.commands.get(this.generalCommand(this.textsOfCommands.get(i))).add(command);
      } else {
        ArrayList<Command> pCommands = new ArrayList<>();
        pCommands.add(command);
        this.commands.put(this.generalCommand(this.textsOfCommands.get(i)), pCommands);
      }
    }
  }

  private String generalCommand(String command) {
    String gCommand = null;
    if (command.contains(GenCommands.CREAR_MAPA)) gCommand = GenCommands.CREAR_MAPA;
    else if (command.contains(GenCommands.CREAR_JUGADORES)) gCommand = GenCommands.CREAR_JUGADORES;
    else if (command.contains("crear")) gCommand = GenCommands.CREAR_JUGADOR;
    else if (command.contains(GenCommands.OBTENER_FRONTERA))
      gCommand = GenCommands.OBTENER_FRONTERA;
    else if (command.contains(GenCommands.OBTENER_CONTINENTE))
      gCommand = GenCommands.OBTENER_CONTINENTE;
    else if (command.contains(GenCommands.OBTENER_COLOR)) gCommand = GenCommands.OBTENER_COLOR;
    else if (command.contains(GenCommands.OBTENER_PAISES)) gCommand = GenCommands.OBTENER_PAISES;
    else if (command.contains(GenCommands.ASIGNAR_MISIONES))
      gCommand = GenCommands.ASIGNAR_MISIONES;
    else if (command.contains(GenCommands.ASIGNAR_MISION)) gCommand = GenCommands.ASIGNAR_MISION;
    else if (command.contains(GenCommands.ASIGNAR_PAISES)) gCommand = GenCommands.ASIGNAR_PAISES;
    else if (command.contains(GenCommands.ASIGNAR_PAIS)) gCommand = GenCommands.ASIGNAR_PAIS;
    else if (command.contains(GenCommands.REPARTIR_EJERCITOS))
      gCommand = GenCommands.REPARTIR_EJERCITOS;
    else if (command.contains(GenCommands.ACABAR_TURNO)) gCommand = GenCommands.ACABAR_TURNO;
    else if (command.contains(GenCommands.CAMBIAR_CARTAS)) gCommand = GenCommands.CAMBIAR_CARTAS;
    else if (command.contains(GenCommands.DESCRIBIR_JUGADOR))
      gCommand = GenCommands.DESCRIBIR_JUGADOR;
    else if (command.contains(GenCommands.JUGADOR)) gCommand = GenCommands.JUGADOR;
    else if (command.contains(GenCommands.DESCRIBIR_PAIS)) gCommand = GenCommands.DESCRIBIR_PAIS;
    else if (command.contains(GenCommands.DESCRIBIR_CONTINENTE))
      gCommand = GenCommands.DESCRIBIR_CONTINENTE;
    else if (command.contains(GenCommands.ATACAR)) gCommand = GenCommands.ATACAR;
    else if (command.contains(GenCommands.REARMAR)) gCommand = GenCommands.REARMAR;
    else if (command.contains(GenCommands.ASIGNAR_CARTA)) gCommand = GenCommands.ASIGNAR_CARTA;
    //
    return gCommand;
  }

  private void readCommandsAndResults(String file) {
    try (BufferedReader br = Files.newBufferedReader(Path.of(file), StandardCharsets.UTF_8)) {
      readCommandsAndResults(br);
    } catch (IOException excep) {
      System.out.println(excep.getMessage());
    }
  }

  private void readCommandsAndResults(BufferedReader reader) {
    StringBuilder resultsOfCommands = new StringBuilder();
    String command = null;
    try {
      String line;
      while ((line = reader.readLine()) != null) {
        if (line.contains("$>") || line.contains("EOF")) {
          if (command != null) {
            this.textsOfCommands.add(command.toLowerCase());
            this.textsOfAnswer.add(resultsOfCommands.toString().toLowerCase());
            resultsOfCommands = new StringBuilder();
          }
          if (line.contains("EOF")) {
            break;
          }
          command = line.substring(line.indexOf("$>")).trim();
        } else {
          resultsOfCommands.append(line).append("\n");
        }
      }
    } catch (IOException exception) {
      throw new IllegalStateException("No se pudo leer la evaluación", exception);
    }
  }

  public HashMap<String, ArrayList<Command>> getCommands() {
    return commands;
  }
}
