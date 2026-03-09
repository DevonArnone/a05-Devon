package edu.unc.comp301;

import java.util.List;

public class WalkingDirectionsServiceAdapter implements UNCBuildingApi {

  private final WalkingDirectionsService walkingService;
  private final UNCBuildingApi baseApi;

  public WalkingDirectionsServiceAdapter(
          WalkingDirectionsService walkingService, UNCBuildingApi baseApi) {
    this.walkingService = walkingService;
    this.baseApi = baseApi;
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
      System.out.println("One or both buildings are not in the UNCBuildingApi.");
      return;
    }

    walkingService.getWalkingDirections(
            start.getLatitude(),
            start.getLongitude(),
            end.getLatitude(),
            end.getLongitude());
  }
}