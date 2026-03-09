package edu.unc.comp301;

import java.io.IOException;

public class BuildingImpl implements Building {

  private final String name;
  private final String description;
  private Location loc;
  private final NominatimGeocoder geocoder =
      new NominatimGeocoder("UNCGeocoder (your_email@unc.edu)");

  public BuildingImpl(String name, String description) {
    this.name = name;
    this.description = description;
    String address = name + ", Chapel Hill, North Carolina";
    try {
      this.loc = geocoder.geocode(address);
    } catch (IOException e) {
      this.loc = null;
    }
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public double getLatitude() {
    if (loc == null) {
      return Double.NaN;
    }
    return loc.getLatitude();
  }

  @Override
  public double getLongitude() {
    if (loc == null) {
      return Double.NaN;
    }
    return loc.getLongitude();
  }
}
