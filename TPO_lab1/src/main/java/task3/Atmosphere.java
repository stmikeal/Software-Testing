package task3;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Atmosphere {
  private Weather curWeather;
  private double density;   // плотность
  private boolean isRarefied; // разреженность
  private double humidity;  // влажность
  private boolean moonVisible;

  public Atmosphere(Weather curWeather, double density, double humidity, boolean moonVisible) {
    if (density < 0 || humidity < 0) {
      System.out.println("Кажется мы находимся в другой вселенной...");
    }
    this.curWeather = curWeather;
    this.density = density;
    this.humidity = humidity;
    this.isRarefied = density < 1.293;
    this.moonVisible = moonVisible;
  }

  @Override
  public String toString() {
    return (density < 0 || humidity < 0) ? "Atmosphere of different universe"
            : String.format("Typical Atmosphere. Humidity: %f. Density: %f.", humidity, density);
  }

  public enum Weather{
    RAINY,
    SUNNY,
    CLOUDY,
    STORM,
    SNOW
  }

  public boolean isRarefied() {
    return isRarefied;
  }

  public void setDensity(double density) {
    this.density = density;
    this.isRarefied = density < 1.293;
  }
}
