import java.util.HashMap;
import java.util.Map;

/**
 * Location class represents a location with a name.
 */
public class Location {
    Location(long id, String name, double lon, double lat) {
        this.id = id;
        this.name = name;
        this.lon = lon;
        this.lat = lat;
    }

    Map<String, Object> getInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("id", id);
        info.put("name", name);
        info.put("lon", lon);
        info.put("lat", lat);
        return info;
    }

    long id;
    String name;
    double lon;
    double lat;
}
