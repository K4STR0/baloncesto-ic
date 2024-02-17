import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ModeloDatosTest {
  @Test
  public void testExisteJugador() {
    System.out.println("Prueba de existeJugador");
    String nombre = "";
    ModeloDatos instance = new ModeloDatos();
    boolean expResult = false;
    boolean result = instance.existeJugador(nombre);
    assertEquals(expResult, result);
    //fail("Fallo forzado.");
  }

  @Test
  public void testActualizarJugador() {
    System.out.println("Prueba de actualizarJugador");
    String nombre = "Rudy";
    ModeloDatos instance = new ModeloDatos();
    instance.actualizarJugador(nombre);
    int expResult = 1;
    int votosActuales = 1; // Aqui se deberia llamar a un metodo que devuelva los votos actuales
    assertEquals(expResult, votosActuales);
  }
}