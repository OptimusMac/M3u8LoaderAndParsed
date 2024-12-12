import java.util.*;
import java.util.stream.Collectors;
import org.openqa.selenium.chrome.ChromeOptions;

public class FlagManager {

  private final Map<String, String> flags = new HashMap<>();

  public void parseArgs(String[] args) {
    for (String arg : args) {
      if (arg.startsWith("--")) {
        String[] parts = arg.substring(2).split("=", 2);
        if (parts.length == 2) {
          flags.put(parts[0], parts[1]);
        } else {
          flags.put(parts[0], "");
        }
      }
    }
  }

  public void addFlagsToOptions(ChromeOptions options) {
    for (Map.Entry<String, String> entry : flags.entrySet()) {
      String flag = entry.getKey();
      String value = entry.getValue();
      if (value.isEmpty()) {
        options.addArguments("--" + flag);
      } else {
        options.addArguments("--" + flag + "=" + value);
      }
    }
    options.addArguments("--headless");
    options.addArguments("--disable-gpu");
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-extensions");
    options.addArguments("--disable-dev-shm-usage");
    options.addArguments("--disable-plugins");
    options.addArguments("--disable-plugins-discovery");
    options.addArguments("--disable-accelerated-2d-canvas");
    options.addArguments("--disable-software-rasterizer");
    options.addArguments("--no-proxy-server");
    options.addArguments("--disable-images");
    options.addArguments("--disable-background-tasks");
    options.addArguments("--window-size=1280x1024");

    options.addArguments("--remote-debugging-port=9222");
    options.addArguments("--headers");
  }

  public Optional<String> getFlagValue(String flagName) {
    return Optional.ofNullable(flags.get(flagName));
  }

  public List<String> getFlagNames() {
    return new ArrayList<>(flags.keySet());
  }

  public boolean hasFlag(String flagName) {
    return flags.containsKey(flagName);
  }
}
