import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PruebasPhantomjsIT {
  private static WebDriver driver = null;

  @Test
  public void tituloIndexTest() {
    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setJavascriptEnabled(true);
    caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/usr/bin/phantomjs");
    caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,
        new String[] { "--web-security=no", "--ignore-ssl-errors=yes" });
    driver = new PhantomJSDriver(caps);
    driver.navigate().to("http://localhost:8080/Baloncesto/");
    assertEquals("Votacion mejor jugador liga ACB", driver.getTitle(),
        "El titulo no es correcto");
    System.out.println(driver.getTitle());
    driver.close();
    driver.quit();
  }

  @Test
  public void votosACeroTest() {
    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setJavascriptEnabled(true);
    caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/usr/bin/phantomjs");
    caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,
        new String[] { "--web-security=no", "--ignore-ssl-errors=yes" });
    driver = new PhantomJSDriver(caps);
    driver.navigate().to("http://localhost:8080/Baloncesto/");
    driver.findElement(By.name("B3")).click(); // Boton de poner votos a cero
    driver.navigate().to("http://localhost:8080/Baloncesto/");
    driver.findElement(By.name("B4")).click(); // Boton de ver votos
    List<WebElement> votes = driver.findElements(By.tagName("li"));

    for (int i = 0; i < votes.size(); i++) {
      assertEquals("0", votes.get(i).getText().split(": ")[1],
          "El numero de votos no es correcto");
    }
    driver.close();
    driver.quit();
  }

  @Test
  public void votarOtroTest() {
    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setJavascriptEnabled(true);
    caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "/usr/bin/phantomjs");
    caps.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,
        new String[] { "--web-security=no", "--ignore-ssl-errors=yes" });
    driver = new PhantomJSDriver(caps);
    driver.navigate().to("http://localhost:8080/Baloncesto/");
    driver.findElement(By.name("R1")).click(); // Selector de votar a otro
    driver.findElement(By.name("txtOtros")).sendKeys("Ruben");
    driver.findElement(By.name("B1")).click(); // Boton de enviar voto
    driver.navigate().to("http://localhost:8080/Baloncesto/");
    List<WebElement> votes = driver.findElements(By.tagName("li"));

    for (int i = 0; i < votes.size(); i++) {
      String liText = votes.get(i).getText();
      String name = liText.split(": ")[0];
      String value = liText.split(": ")[1];

      if (name.equals("Ruben")) {
        assertEquals("1", value, "El numero de votos no es correcto");
      } else {
        assertEquals("0", value, "El numero de votos no es correcto");
      }
    }
    driver.close();
    driver.quit();
  }
}