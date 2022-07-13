package lab11.graphs;

/**
 *  @author Josh Hug
 */
public class MazeCycles extends MazeExplorer {
    /* Inherits public fields:
    public int[] distTo;
    public int[] edgeTo;
    public boolean[] marked;
    */

    private Maze maze;
    private int[] back;
    private boolean hasCycle;

    public MazeCycles(Maze m) {
        super(m);
        maze = m;
        back = new int[maze.V()];
    }

    private void drawCycle(int v, int u) {
        edgeTo[u] = v;
        while (v != u) {
            edgeTo[v] = back[v];
            v = back[v];
        }
        announce();
    }

    private void detectCycle(int v) {
        marked[v] = true;
        announce();

        for (int u : maze.adj(v)) {
            if (hasCycle) {
                return;
            } else if (marked[u]) {
                if (back[v] != u) {
                    hasCycle = true;
                    drawCycle(v, u);
                }
            } else {
                back[u] = v;
                detectCycle(u);
            }
        }
    }

    @Override
    public void solve() {
        detectCycle(0);
    }
}

