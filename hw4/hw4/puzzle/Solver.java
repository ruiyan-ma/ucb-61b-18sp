package hw4.puzzle;

import edu.princeton.cs.algs4.MinPQ;

import java.util.*;

public class Solver {
    /**
     * Solve the puzzle using the A* algorithm.
     */
    public Solver(WorldState initial) {
        MinPQ<SearchNode> queue = new MinPQ<>();
        SearchNode start = new SearchNode(initial, 0, null);
        queue.insert(start);
        totalNum = 1;

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
                if (node.parent == null || !node.parent.worldState.equals(neighbor)) {
                    queue.insert(new SearchNode(neighbor, node.moves + 1, node));
                    totalNum += 1;
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

    int totalNum;
}
