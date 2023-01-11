import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */

public class Rasterer {
    public static final int MAX_DEPTH = 7;
    public static final double ROOT_MIN_LON = MapServer.ROOT_ULLON;
    public static final double ROOT_MAX_LON = MapServer.ROOT_LRLON;
    public static final double ROOT_MIN_LAT = MapServer.ROOT_LRLAT;
    public static final double ROOT_MAX_LAT = MapServer.ROOT_ULLAT;
//    public static final double OFFSET = 0.03;

    public Rasterer() {
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
     * The grid of images must obey the following properties, where image in the
     * grid is referred to as a "tile".
     * <ul>
     *     <li>The tiles collected must cover the most longitudinal distance per pixel
     *     (LonDPP) possible, while still covering less than or equal to the amount of
     *     longitudinal distance per pixel in the query box for the user viewport size. </li>
     *     <li>Contains all tiles that intersect the query bounding box that fulfill the
     *     above condition.</li>
     *     <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     * </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     * forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        Map<String, Object> results = new HashMap<>();

        double queryMinLon = params.get("ullon");
        double queryMaxLon = params.get("lrlon");
        double queryMinLat = params.get("lrlat");
        double queryMaxLat = params.get("ullat");
        double queryWidth = params.get("w");

        // if the query box is not in bound, return false
        if (!checkQuery(queryMinLon, queryMaxLon, queryMinLat, queryMaxLat)) {
            results.put("query_success", false);
            return results;
        }

        double queryLonDPP = computeDPP(queryMinLon, queryMaxLon, (int) queryWidth);
        double lonDPP = computeDPP(ROOT_MIN_LON, ROOT_MAX_LON, MapServer.TILE_SIZE);
        int depth = 0;

        // find the greatest longitude DPP which is smaller than or equal to query DPP
        while (depth < MAX_DEPTH && lonDPP > queryLonDPP) {
            lonDPP /= 2;
            depth += 1;
        }

        // DPT: distance per tile
        double lonDPT = (ROOT_MAX_LON - ROOT_MIN_LON) / Math.pow(2, depth);
        double latDPT = (ROOT_MAX_LAT - ROOT_MIN_LAT) / Math.pow(2, depth);

        // find indices of tiles covered the longitudes and latitudes of query box
        int x1 = (int) ((queryMinLon - ROOT_MIN_LON) / lonDPT);
        int x2 = (int) ((queryMaxLon - ROOT_MIN_LON) / lonDPT);
        int y1 = (int) ((ROOT_MAX_LAT - queryMaxLat) / latDPT);
        int y2 = (int) ((ROOT_MAX_LAT - queryMinLat) / latDPT);

        x2 = (int) Math.min(Math.pow(2, depth) - 1, x2);
        y2 = (int) Math.min(Math.pow(2, depth) - 1, y2);

        String[][] grid = new String[y2 - y1 + 1][x2 - x1 + 1];
        for (int i = 0; y1 + i <= y2; ++i) {
            for (int j = 0; x1 + j <= x2; ++j) {
                grid[i][j] = String.format("d%s_x%s_y%s.png", depth, x1 + j, y1 + i);
            }
        }

        results.put("render_grid", grid);
        results.put("raster_ul_lon", ROOT_MIN_LON + x1 * lonDPT);
        results.put("raster_ul_lat", ROOT_MAX_LAT - y1 * latDPT);
        results.put("raster_lr_lon", ROOT_MIN_LON + (x2 + 1) * lonDPT);
        results.put("raster_lr_lat", ROOT_MAX_LAT - (y2 + 1) * latDPT);
        results.put("depth", depth);
        results.put("query_success", true);
        return results;
    }

    /**
     * Check whether the query box is valid and inside the map.
     *
     * @param minLon: the min (left) longitude.
     * @param maxLon: the max (right) longitude.
     * @param minLat: the min (lower) latitude.
     * @param maxLat: the max (upper) latitude.
     * @return true if the query box is inside the map.
     */
    private boolean checkQuery(double minLon, double maxLon, double minLat, double maxLat) {
        return minLon < maxLon && minLat < maxLat
//                && minLon >= ROOT_MIN_LON - OFFSET && maxLon <= ROOT_MAX_LON + OFFSET
//                && minLat >= ROOT_MIN_LAT - OFFSET && maxLat <= ROOT_MAX_LAT + OFFSET;
                && minLon < ROOT_MAX_LON && maxLon >= ROOT_MIN_LON
                && minLat < ROOT_MAX_LAT && maxLat >= ROOT_MIN_LAT;
    }

    /**
     * Compute the longitudinal (or latitudinal) distance per pixel.
     *
     * @param v1     :    the first longitude (or latitude) value.
     * @param v2     :    the second longitude (or latitude) value.
     * @param width: the width of pixels.
     * @return the DPP value.
     */
    private double computeDPP(double v1, double v2, int width) {
        return Math.abs(v1 - v2) / width;
    }
}
