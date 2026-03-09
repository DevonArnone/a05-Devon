package edu.unc.comp301;

import java.util.List;

public class WalkingDirectionsServiceAdapter implements UNCBuildingAPI {

  private final UNCBuildingAPIImpl baseApi;
  private final WalkingDirectionsService walkingService;

  public WalkingDirectionsServiceAdapter(
      UNCBuildingAPIImpl baseApi, WalkingDirectionsService walkingService) {
    this.baseApi = baseApi;
    this.walkingService = walkingService;
  }

  @Override
  public Building getBuilding(String name) {
    return baseApi.getBuilding(name);
  }

  @Override
  public List<String> getAllBuildingNames() {
    return baseApi.getAllBuildingNames();
  }

  @Override
  public void getDirections(String startBuilding, String endBuilding) {
    Building start = baseApi.getBuilding(startBuilding);
    Building end = baseApi.getBuilding(endBuilding);
    if (start == null || end == null) {
      System.out.println("One or both buildings are not in the UNCBuildingAPI.");
      return;
    }

    double startLat = start.getLatitude();
    double startLon = start.getLongitude();
    double endLat = end.getLatitude();
    double endLon = end.getLongitude();

    walkingService.getWalkingDirections(startLat, startLon, endLat, endLon);
  }
}

