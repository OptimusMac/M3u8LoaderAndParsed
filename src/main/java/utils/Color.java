package utils;

import lombok.Getter;
import org.slf4j.Logger;

public class Color {

  @Getter
  public enum TextColor {
    BLACK("\033[30m"),
    RED("\033[31m"),
    GREEN("\033[32m"),
    YELLOW("\033[33m"),
    BLUE("\033[34m"),
    MAGENTA("\033[35m"),
    CYAN("\033[36m"),
    WHITE("\033[37m");

    private final String code;

    TextColor(String code) {
      this.code = code;
    }

  }

  @Getter
  public enum BackgroundColor {
    BLACK("\033[40m"),
    RED("\033[41m"),
    GREEN("\033[42m"),
    YELLOW("\033[43m"),
    BLUE("\033[44m"),
    MAGENTA("\033[45m"),
    CYAN("\033[46m"),
    WHITE("\033[47m");

    private final String code;

    BackgroundColor(String code) {
      this.code = code;
    }

  }

  @Getter
  public enum TextStyle {
    BOLD("\033[1m"),
    UNDERLINED("\033[4m"),
    REVERSED("\033[7m"),
    RESET("\033[0m");

    private final String code;

    TextStyle(String code) {
      this.code = code;
    }

  }

  public static void printColoredText(String text, TextColor textColor) {
    System.out.println(textColor.getCode() + text + TextStyle.RESET.getCode());
  }

  public static void printStyledText(String text, TextColor textColor, BackgroundColor bgColor, TextStyle style) {
    System.out.println(style.getCode() + textColor.getCode() + bgColor.getCode() + text + TextStyle.RESET.getCode());
  }

  public static void printStyledLog(String text, TextColor textColor, BackgroundColor bgColor, TextStyle style, Object... obj){
    System.out.printf(
        style.getCode() + textColor.getCode() + bgColor.getCode() + text + TextStyle.RESET.getCode()
            + "%n", obj);
  }

}
