package hw4.puzzle;

public class SearchNode implements Comparable<SearchNode> {
    SearchNode(WorldState worldState, int moves, SearchNode parent) {
        this.worldState = worldState;
        this.moves = moves;
        this.parent = parent;
        computeDist();
    }

    /**
     * Compute the total distance of a search node.
     */
    public void computeDist() {
        dist = moves + worldState.estimatedDistanceToGoal();
    }

    WorldState worldState;
    int moves;
    int dist;
    SearchNode parent;

    @Override
    public int compareTo(SearchNode o) {
        return dist - o.dist;
    }
}
