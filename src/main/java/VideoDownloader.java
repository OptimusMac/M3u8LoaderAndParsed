import generics.DefaultService;
import generics.impl.DefaultServiceImpl;
import io.github.bonigarcia.wdm.WebDriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v131.network.Network;
import org.slf4j.LoggerFactory;
import utils.Color;
import utils.Color.BackgroundColor;
import utils.Color.TextColor;
import utils.Color.TextStyle;

public class VideoDownloader {

  private static final List<String> m3u8urls = new ArrayList<>();
  private static final org.slf4j.Logger log = LoggerFactory.getLogger(VideoDownloader.class);

  public static void main(String[] args) {
    FlagManager flagManager = new FlagManager();
    flagManager.parseArgs(args);

    boolean stackTraceEnabled = flagManager.hasFlag("stacktrace");


    try {
      String browserVersion = flagManager.getFlagValue("browserVersion").orElse("");
      if (!browserVersion.isEmpty()) {
        WebDriverManager.chromedriver().browserVersion(browserVersion).setup();
      } else {
        WebDriverManager.chromedriver().setup();
      }

      ChromeOptions options = new ChromeOptions();

      flagManager.addFlagsToOptions(options);

      ChromeDriver driver = new ChromeDriver(options);

      DevTools devTools = driver.getDevTools();
      devTools.createSession();
      devTools.send(Network.enable(Optional.of(100000), Optional.of(100000), Optional.of(100000)));

      devTools.addListener(Network.requestWillBeSent(), request -> {
        if (stackTraceEnabled) {
          log.info("Request URL: " + request.getRequest().getUrl());
        }
      });

      devTools.addListener(Network.responseReceived(), response -> {
        String url = response.getResponse().getUrl();
        if (url.contains("m3u8")) {
          m3u8urls.add(url);
        }
      });

      String videoPageUrl = flagManager.getFlagValue("videoPageUrl").orElseGet(() -> {
        Color.printStyledText("Укажите ссылку на видео --videoPageUrl", TextColor.GREEN,
            BackgroundColor.RED, TextStyle.BOLD);
        driver.quit();
        return null;
      });
      if (videoPageUrl == null) {
        return;
      }

      driver.get(videoPageUrl);

      int timeout = Integer.parseInt(flagManager.getFlagValue("timeout").orElse("10"));
      try {
        Thread.sleep(timeout * 1000L);
      } catch (InterruptedException e) {
        log.info("Ошибка при ожидании: ", e);
      }
      driver.quit();
    } catch (Exception e) {
      if (stackTraceEnabled) {
        log.info("Произошла ошибка: ", e);
      } else {
        log.error("Произошла ошибка: " + e.getMessage());
      }
    }

    if (!m3u8urls.isEmpty()) {
      m3u8urls.forEach(string -> {
        int index = m3u8urls.indexOf(string);
        Color.printStyledLog("%s. %s", TextColor.CYAN, BackgroundColor.BLACK, TextStyle.BOLD, index, string);
      });
      return;
    }


  }
}
