package edu.unc.comp301;

import java.util.List;

public interface UNCBuildingAPI {

  Building getBuilding(String name);

  List<String> getAllBuildingNames();

  void getDirections(String startBuilding, String endBuilding);
}

