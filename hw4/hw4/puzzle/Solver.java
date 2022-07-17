package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.*;

public class Solver {
    /**
     * Solve the puzzle using the A* algorithm.
     */
    public Solver(WorldState initial) {
        MinPQ<SearchNode> queue = new MinPQ<>();
        Set<WorldState> visited = new HashSet<>();
        SearchNode start = new SearchNode(initial, 0, null);
        queue.insert(start);

        while (!queue.isEmpty()) {
            SearchNode node = queue.delMin();
            visited.add(start.worldState);

            if (node.worldState.isGoal()) {
                solution = new LinkedList<>();
                solution.add(node.worldState);
                while (node.parent != null) {
                    node = node.parent;
                    solution.add(0, node.worldState);
                }
                return;
            }

            for (WorldState neighbor : node.worldState.neighbors()) {
                if (!visited.contains(neighbor)) {
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

    int minNumMove;

    List<WorldState> solution;
}
