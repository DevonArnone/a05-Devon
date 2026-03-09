package edu.unc.comp301;

import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

public class NominatimGeocoder {

  private final String baseURL = "https://nominatim.openstreetmap.org/search";
  private final OkHttpClient client = new OkHttpClient();
  private final String userAgent;

  public NominatimGeocoder(String userAgent) {
    this.userAgent = userAgent;
  }

  public String createURL(String address) {
    HttpUrl url =
        HttpUrl.parse(baseURL)
            .newBuilder()
            .addQueryParameter("q", address)
            .addQueryParameter("format", "json")
            .addQueryParameter("limit", "1")
            .build();
    return url.toString();
  }

  public Request buildRequest(String url) {
    return new Request.Builder().url(url).header("User-Agent", userAgent).get().build();
  }

  public String sendRequest(Request request) throws IOException {
    try (Response resp = client.newCall(request).execute()) {
      if (resp == null || !resp.isSuccessful() || resp.body() == null) {
        throw new IOException("Unsuccessful response from Nominatim");
      }
      return resp.body().string();
    }
  }

  public Location parseLocation(String json) throws IOException {
    JSONArray results = new JSONArray(json);
    if (results.isEmpty()) {
      return null;
    }
    JSONObject object = results.getJSONObject(0);
    double latitude = object.getDouble("lat");
    double longitude = object.getDouble("lon");
    return new Location(latitude, longitude);
  }

  public Location geocode(String address) throws IOException {
    String url = createURL(address);
    Request request = buildRequest(url);
    String json = sendRequest(request);
    return parseLocation(json);
  }
}

