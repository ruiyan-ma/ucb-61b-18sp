import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Graph for storing all the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {

    private static class Node {
        Node(long id, double lon, double lat) {
            this.id = id;
            this.lon = lon;
            this.lat = lat;
            edges = new LinkedHashMap<>();
        }

        String name;
        long id;
        double lon;
        double lat;

        /**
         * A map contains (adjacent node id, way) pairs.
         */
        Map<Long, Way> edges;
    }

    private static class Way {
        Way(long id, String name, String maxSpeed, List<Long> nodeIds) {
            this.id = id;
            this.name = name;
            this.maxSpeed = maxSpeed;
            this.nodeIds = nodeIds;
        }

        long id;
        String name;
        String maxSpeed;
        List<Long> nodeIds;
    }

    /**
     * Add a new node to the graph.
     *
     * @param id:  the node id.
     * @param lon: the longitude.
     * @param lat: the latitude.
     */
    void addNode(long id, double lon, double lat) {
        vertices.put(id, new Node(id, lon, lat));
    }

    /**
     * Set name for a location.
     *
     * @param id:   the node id.
     * @param name: the node name.
     */
    void setNodeName(long id, String name) {
        vertices.get(id).name = name;
    }

    /**
     * Add a new edge to the graph.
     *
     * @param id:      the edge id.
     * @param nodeIDs: the list of node ids on this edge.
     */
    void addWay(long id, String name, String maxSpeed, List<Long> nodeIDs) {
        Way way = new Way(id, name, maxSpeed, nodeIDs);
        for (int i = 0; i < nodeIDs.size() - 1; ++i) {
            long id1 = nodeIDs.get(i);
            long id2 = nodeIDs.get(i + 1);
            Node n1 = vertices.get(id1);
            Node n2 = vertices.get(id2);
            n1.edges.put(id2, way);
            n2.edges.put(id1, way);
        }
    }

    /**
     * Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc.
     * <p>
     * A map contains (node id, node) pairs.
     */
    private Map<Long, Node> vertices = new LinkedHashMap<>();

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     *
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();
    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     *
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     * Remove nodes with no connections from the graph.
     * While this does not guarantee that any two nodes in the remaining graph are connected,
     * we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        // Note: should not modify a collection in its for-each loop
        Map<Long, Node> cleanVertices = new LinkedHashMap<>();
        for (Node node : vertices.values()) {
            if (node.edges.size() > 0) {
                cleanVertices.put(node.id, node);
            }
        }
        vertices = cleanVertices;
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     *
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        return vertices.keySet();
    }

    /**
     * Returns ids of all vertices adjacent to v.
     *
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        return vertices.get(v).edges.keySet();
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     *
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dPhi = Math.toRadians(latW - latV);
        double dLambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dPhi / 2.0) * Math.sin(dPhi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dLambda / 2.0) * Math.sin(dLambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     *
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     *
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        double minDist = Double.MAX_VALUE;
        long result = vertices.keySet().iterator().next();
        for (Node node : vertices.values()) {
            double dist = distance(lon, lat, node.lon, node.lat);
            if (dist < minDist) {
                minDist = dist;
                result = node.id;
            }
        }

        return result;
    }

    /**
     * Gets the longitude of a vertex.
     *
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return vertices.get(v).lon;
    }

    /**
     * Gets the latitude of a vertex.
     *
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return vertices.get(v).lat;
    }
}
