package lab11.graphs;

import java.util.PriorityQueue;

/**
 *  @author Josh Hug
 */
public class MazeAStarPath extends MazeExplorer {
    private int source;
    private int target;
    private boolean targetFound = false;
    private Maze maze;

    public MazeAStarPath(Maze m, int sourceX, int sourceY, int targetX, int targetY) {
        super(m);
        maze = m;
        source = maze.xyTo1D(sourceX, sourceY);
        target = maze.xyTo1D(targetX, targetY);
        distTo[source] = 0;
        edgeTo[source] = source;
    }

    /** Estimate of the distance from v to the target. */
    private int h(int v) {
        int dx = Math.abs(maze.toX(target) - maze.toX(v));
        int dy = Math.abs(maze.toY(target) - maze.toY(v));
        return Math.max(dx, dy);
    }

    /** Return the total distance. */
    private int getDist(int v) {
        return distTo[v] + h(v);
    }

    /** Performs an A star search from vertex s. */
    private void aStar(int s) {
        PriorityQueue<Integer> queue = new PriorityQueue<>((o1, o2) -> getDist(o1) - getDist(o2));
        queue.add(s);
        marked[s] = true;

        while (!queue.isEmpty()) {
            int v = queue.poll();
            announce();

            if (v == target) {
                return;
            }

            for (int w : maze.adj(v)) {
                if (!marked[w]) {
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                    queue.add(w);
                    marked[w] = true;
                }
            }
        }
    }

    @Override
    public void solve() {
        aStar(source);
    }

}

