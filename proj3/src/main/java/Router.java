import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     *
     * @param g       The graph to use.
     * @param stlon   The longitude of the start location.
     * @param stlat   The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        long start = g.closest(stlon, stlat);
        long end = g.closest(destlon, destlat);

        Map<Long, Double> distTo = new HashMap<>();
        for (long id : g.vertices()) distTo.put(id, Double.MAX_VALUE / 2);
        distTo.put(start, 0.0);

        Map<Long, Long> edgeTo = new HashMap<>();
        edgeTo.put(start, null);

        class NodeComparator implements Comparator<Long> {
            @Override
            public int compare(Long id1, Long id2) {
                double d1 = distTo.get(id1) + g.distance(id1, end);
                double d2 = distTo.get(id2) + g.distance(id2, end);
                return Double.compare(d1, d2);
            }
        }

        PriorityQueue<Long> queue = new PriorityQueue<>(new NodeComparator());
        queue.add(start);

        while (!queue.isEmpty()) {
            long id = queue.poll();

            if (id == end) {
                break;
            }

            for (long next : g.adjacent(id)) {
                double dist = distTo.get(id) + g.distance(id, next);
                if (dist < distTo.get(next)) {
                    distTo.put(next, dist);
                    edgeTo.put(next, id);
                    queue.remove(next);
                    queue.add(next);
                }
            }
        }

        List<Long> path = new ArrayList<>();
        Long nodeId = end;
        while (nodeId != null) {
            path.add(nodeId);
            nodeId = edgeTo.get(nodeId);
        }

        Collections.reverse(path);
        return path;
    }

    /**
     * Create the list of directions corresponding to a route on the graph.
     *
     * @param g     The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        List<NavigationDirection> result = new ArrayList<>();

        if (route.size() < 2) {
            return result;
        }

        long firstNodeId = route.get(0);
        long secondNodeId = route.get(1);

        double distance = 0;
        double prevBearing = g.bearing(firstNodeId, secondNodeId);
        int direction = NavigationDirection.START;
        String prevWayName = g.getWayName(firstNodeId, secondNodeId);

        for (int i = 0; i < route.size() - 1; ++i) {
            long n1 = route.get(i);
            long n2 = route.get(i + 1);
            double currBearing = g.bearing(n1, n2);
            String currWayName = g.getWayName(n1, n2);

            if (currWayName.equals(prevWayName)) {
                distance += g.distance(n1, n2);
            } else {
                NavigationDirection navigation = new NavigationDirection();
                navigation.direction = direction;
                navigation.distance = distance;
                navigation.way = prevWayName;
                result.add(navigation);

                direction = getDirection(prevBearing, currBearing);
                distance = g.distance(n1, n2);
                prevWayName = g.getWayName(n1, n2);
            }

            // update the bearing
            prevBearing = currBearing;
        }

        // add the last navigation direction
        NavigationDirection navigation = new NavigationDirection();
        navigation.direction = direction;
        navigation.distance = distance;
        navigation.way = prevWayName;
        result.add(navigation);

        return result;
    }

    /**
     * Get the next direction based on previous bearing angle and current bearing angle.
     * - Between -15 and 15 degrees the direction should be “Continue straight”.
     * - Beyond -15 and 15 degrees but between -30 and 30 degrees the direction should be “Slight left/right”.
     * - Beyond -30 and 30 degrees but between -100 and 100 degrees the direction should be “Turn left/right”.
     * - Beyond -100 and 100 degrees the direction should be “Sharp left/right”.
     *
     * @param prevBearing: the previous bearing angle.
     * @param currBearing: the current bearing angle.
     * @return the next direction.
     */
    private static int getDirection(double prevBearing, double currBearing) {
        double diff = round(currBearing - prevBearing);
        double absDiff = Math.abs(diff);

        if (absDiff < 15) {
            return NavigationDirection.STRAIGHT;
        } else if (absDiff < 30) {
            return diff > 0 ? NavigationDirection.SLIGHT_RIGHT : NavigationDirection.SLIGHT_LEFT;
        } else if (absDiff < 100) {
            return diff > 0 ? NavigationDirection.RIGHT : NavigationDirection.LEFT;
        } else {
            return diff > 0 ? NavigationDirection.SHARP_RIGHT : NavigationDirection.SHARP_LEFT;
        }
    }

    /**
     * Round the angle in [-180, 180].
     *
     * @param angle: the angle.
     * @return the angle after rounding.
     */
    private static double round(double angle) {
        while (angle < -180) {
            angle += 360;
        }

        while (angle > 180) {
            angle -= 360;
        }

        return angle;
    }


    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /**
         * Integer constants representing directions.
         */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /**
         * Number of directions supported.
         */
        public static final int NUM_DIRECTIONS = 8;

        /**
         * A mapping of integer values to directions.
         */
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /**
         * Default name for an unknown way.
         */
        public static final String UNKNOWN_ROAD = "unknown road";

        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /**
         * The direction a given NavigationDirection represents.
         */
        int direction;
        /**
         * The name of the way I represent.
         */
        String way;
        /**
         * The distance along this way I represent.
         */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         *
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                        && way.equals(((NavigationDirection) o).way)
                        && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
