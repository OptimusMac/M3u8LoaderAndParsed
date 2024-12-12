package generics.impl;

import generics.DefaultService;
import model.Argument;
import model.DefaultValue;

public class DefaultServiceImpl extends Value implements DefaultService {

  @DefaultValue(value = "10", type = Integer.class)
  @Override
  public String randomName(int value, @Argument int iter) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < iter; i++) {
      double rand = Math.random();
      char c = (char) ('a' + (int) (rand * 26));
      stringBuilder.append(c);
    }
    return stringBuilder.toString();
  }

  @Override
  public String randomName() {
    return randomName(15, value());
  }

  public static void main(String[] args) {
    System.out.println(new DefaultServiceImpl().randomName());
  }

}
