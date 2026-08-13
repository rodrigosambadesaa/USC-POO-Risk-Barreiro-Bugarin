package gal.sdc.usc.risk.infrastructure.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/** Adaptador UTF-8 para recursos de classpath, escenarios externos y ficheros de salida. */
public final class ResourceStore {
  private ResourceStore() {}

  public static BufferedReader reader(String name) throws IOException {
    String directory = System.getProperty("risk.resources.dir");
    if (directory != null && !directory.isBlank()) {
      return Files.newBufferedReader(Path.of(directory).resolve(name), StandardCharsets.UTF_8);
    }
    InputStream stream = ResourceStore.class.getClassLoader().getResourceAsStream(name);
    if (stream == null) {
      throw new IOException("Recurso no encontrado: " + name);
    }
    return new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
  }

  public static Path output(String name) {
    return Path.of(System.getProperty("risk.output.dir", ".")).resolve(name).normalize();
  }

  public static File outputFile(String name) {
    return output(name).toFile();
  }
}
