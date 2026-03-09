package edu.unc.comp301;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UNCBuildingAPIImpl implements UNCBuildingAPI {

  private final Map<String, Building> buildings = new HashMap<>();

  @Override
  public Building getBuilding(String name) {
    if (name == null) {
      return null;
    }
    return buildings.get(name.toLowerCase());
  }

  @Override
  public List<String> getAllBuildingNames() {
    return new ArrayList<>(buildings.keySet());
  }

  @Override
  public void getDirections(String startBuilding, String endBuilding) {
    // Intentionally left blank per assignment instructions.
  }

  public void add(String name, String description) {
    if (name == null) {
      return;
    }
    buildings.put(name.toLowerCase(), new BuildingImpl(name, description));
  }
}

