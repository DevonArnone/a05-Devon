package edu.unc.comp301;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

public class WalkingDirectionsService {

  private final String apiKey;
  private final OkHttpClient client = new OkHttpClient();

  public WalkingDirectionsService(String apiKey) {
    this.apiKey = apiKey;
  }

  public String createURL(double startLat, double startLon, double endLat, double endLon) {
    String start = startLon + "," + startLat;
    String end = endLon + "," + endLat;

    HttpUrl url =
            HttpUrl.parse("https://api.openrouteservice.org/v2/directions/foot-walking")
                    .newBuilder()
                    .addQueryParameter("api_key", apiKey)
                    .addQueryParameter("start", start)
                    .addQueryParameter("end", end)
                    .addQueryParameter("steps", "true")
                    .build();

    return url.toString();
  }

  public Request buildRequest(String url) {
    return new Request.Builder().url(url).get().build();
  }

  public String sendRequest(Request request) throws IOException {
    try (Response resp = client.newCall(request).execute()) {
      if (resp == null || !resp.isSuccessful() || resp.body() == null) {
        throw new IOException("Unsuccessful response from OpenRouteService");
      }
      return resp.body().string();
    }
  }

  public DirectionsResult parseDirections(String json) {
    JSONObject obj = new JSONObject(json);
    JSONArray features = obj.getJSONArray("features");
    JSONObject feature = features.getJSONObject(0);
    JSONObject properties = feature.getJSONObject("properties");
    JSONArray segments = properties.getJSONArray("segments");

    List<Step> steps = new ArrayList<>();
    double totalDistance = 0.0;
    double totalDuration = 0.0;

    for (int i = 0; i < segments.length(); i++) {
      JSONObject segment = segments.getJSONObject(i);
      JSONArray segmentSteps = segment.getJSONArray("steps");

      for (int j = 0; j < segmentSteps.length(); j++) {
        JSONObject stepObj = segmentSteps.getJSONObject(j);

        String instruction = stepObj.getString("instruction");
        double distance = stepObj.getDouble("distance");
        double duration = stepObj.getDouble("duration");

        steps.add(new Step(instruction, distance, duration));
        totalDistance += distance;
        totalDuration += duration;
      }
    }

    return new DirectionsResult(steps, totalDistance, totalDuration);
  }

  public void getWalkingDirections(
          double startLat, double startLon, double endLat, double endLon) {
    try {
      String url = createURL(startLat, startLon, endLat, endLon);
      Request request = buildRequest(url);
      String json = sendRequest(request);
      DirectionsResult result = parseDirections(json);

      List<Step> steps = result.getSteps();
      for (int i = 0; i < steps.size(); i++) {
        Step step = steps.get(i);
        System.out.println(
                (i + 1)
                        + ". "
                        + step.getInstruction()
                        + " - ("
                        + step.getDistance()
                        + " meters, "
                        + step.getDuration()
                        + " seconds)");
      }

      System.out.println("Total distance: " + result.getTotalDistance() + " m");
      System.out.println("Total time: " + result.getTotalDuration());
    } catch (Exception e) {
      System.out.println(e);
    }
  }
}