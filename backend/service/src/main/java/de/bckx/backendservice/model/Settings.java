package de.bckx.backendservice.model;

/**
 * Settings
 */
public class Settings {

  String id;
  String applicationName;

  public Settings() {}

  public String getId() {
    return this.id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getApplicationName() {
    return this.applicationName;
  }

  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }
}
