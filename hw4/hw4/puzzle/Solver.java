package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.LinkedList;
import java.util.List;


public class Solver {
    /**
     * Solve the puzzle using the A* algorithm.
     */
    public Solver(WorldState initial) {
        MinPQ<SearchNode> queue = new MinPQ<>();
        SearchNode start = new SearchNode(initial, 0, null);
        queue.insert(start);

        while (!queue.isEmpty()) {
            SearchNode node = queue.delMin();

            if (node.worldState.isGoal()) {
                minNumMove = node.moves;
                solution = new LinkedList<>();
                solution.add(node.worldState);
                while (node.parent != null) {
                    node = node.parent;
                    solution.add(0, node.worldState);
                }
                return;
            }

            for (WorldState neighbor : node.worldState.neighbors()) {
                // don’t enqueue a neighbor if its world state is the same as parent node
                if (node.parent == null || !node.parent.worldState.equals(neighbor)) {
                    queue.insert(new SearchNode(neighbor, node.moves + 1, node));
                }
            }
        }
    }

    /**
     * Return the minimum number of moves to solve the puzzle
     * starting at the initial WorldState.
     */
    public int moves() {
        return minNumMove;
    }

    /**
     * Returns a sequence of WorldStates from the initial WorldState
     * to the solution.
     */
    public Iterable<WorldState> solution() {
        return solution;
    }

    private int minNumMove;

    private List<WorldState> solution;
}
