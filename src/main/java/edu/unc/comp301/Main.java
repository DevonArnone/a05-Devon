package edu.unc.comp301;

import java.io.InputStream;
import java.util.Properties;

public class Main {
  public static void main(String[] args) {

    Properties cfg = new Properties();
    try (InputStream in = Main.class.getClassLoader().getResourceAsStream("config.properties")) {
      if (in == null) {
        System.out.println("config.properties not found on classpath.");
        return;
      }
      cfg.load(in);
    } catch (Exception e) {
      System.out.println(e);
      return;
    }

    String apiKey = cfg.getProperty("ors.api.key");
    if (apiKey == null || apiKey.isEmpty()) {
      System.out.println("ors.api.key not found in config.properties.");
      return;
    }

    UNCBuildingApiImpl unc = new UNCBuildingApiImpl();
    unc.add("sitterson hall", "computer science");
    unc.add("hamilton hall", "classrooms");

    WalkingDirectionsService wds = new WalkingDirectionsService(apiKey);
    UNCBuildingApi adapter = new WalkingDirectionsServiceAdapter(wds, unc);

    adapter.getDirections("sitterson hall", "hamilton hall");
  }
}