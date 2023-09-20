package task3;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoordsPair {

  private int key;
  private int value;

  public CoordsPair(int key, int value) {
    this.key = key;
    this.value = value;
  }

  @Override
  public int hashCode() {
    return key + value;
  }

  @Override
  public boolean equals(Object obj){
    return obj.getClass() == this.getClass() && ((CoordsPair) obj).getKey() == key && ((CoordsPair) obj).getValue() == value;
  }

  @Override
  public String toString() {
    return String.format("X: %d, Y: %d", key, value);
  }

}
