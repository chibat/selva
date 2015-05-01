package io.github.chibat.selva;

public class Config {

  private static final Config instance = new Config();
  private boolean welcomePageFlag = true;
  private String requestCharacterEncoding = "UTF-8";

  private Config() {
  }

  public static Config getInstance() {
    return instance;
  }

  public boolean isWelcomePageFlag() {
    return welcomePageFlag;
  }

  public void setWelcomePageFlag(boolean welcomePageFlag) {
    this.welcomePageFlag = welcomePageFlag;
  }

  public String getRequestCharacterEncoding() {
    return requestCharacterEncoding;
  }

  public void setRequestCharacterEncoding(String requestCharacterEncoding) {
    this.requestCharacterEncoding = requestCharacterEncoding;
  }
}
