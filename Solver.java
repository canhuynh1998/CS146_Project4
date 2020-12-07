import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinkedStack;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

// A solver based on the A* algorithm for the 8-puzzle and its generalizations.
public class Solver {
    private LinkedStack<Board> solution;
    private int moves;

    // Helper search node class.
    private class SearchNode {
        private Board board;
        private int moves;
        private SearchNode prev;

        SearchNode(Board board, int moves, SearchNode previous) {
            this.board = board;
            this.moves = moves;
            this.prev = previous;
        }

    }

    // Find a solution to the initial board (using the A* algorithm).
    public Solver(Board initial) {
        if (initial == null) {
            throw new NullPointerException("It is null");
        }
        if (!initial.isSolvable()) {
            throw new IllegalArgumentException("Cannot solve this table");
        }
        this.solution = new LinkedStack<Board>();

        //System.out.println(initial.size());
        SearchNode intialNode = new SearchNode(initial, 0, null);

        MinPQ<SearchNode> pq = new MinPQ<>(new ManhattanOrder());   // dequeue the PQ in the order of Manhattan number.
        pq.insert(intialNode);


        //A* Algorithm.
        while (!pq.isEmpty()) {
            SearchNode current = pq.delMin();
            Board currentboard = current.board;

            if (currentboard.isGoal()) {
                //Found a solution
                //Put all the previous board that leads to this solution into the stack.
                for (SearchNode node = current; node != null; node = node.prev) {
                    solution.push(node.board);
                }
                break;
            } else {
                for (Board node : currentboard.neighbors()) {
                    //Put all the possible board into the PQ.
                    if (!node.equals(current.board)) {
                        //Avoid duplicated board
                        SearchNode newNode = new SearchNode(node, this.moves, current);

                        pq.insert(newNode);
                    }
                }
                this.moves++;
            }
        }
    }

    // The minimum number of moves to solve the initial board.
    public int moves() {
        return this.moves;
    }

    // Sequence of boards in a shortest solution.
    public Iterable<Board> solution() {
        return this.solution;
    }

    // Helper hamming priority function comparator.
    private static class HammingOrder implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b) {
            int avalue = a.board.hamming() + a.moves;
            int bvalue = b.board.hamming() + b.moves;

            return Integer.compare(avalue, bvalue);
        }
    }

    // Helper manhattan priority function comparator.
    private static class ManhattanOrder implements Comparator<SearchNode> {
        public int compare(SearchNode a, SearchNode b) {
            int avalue = a.board.manhattan() + a.moves;
            int bvalue = b.board.manhattan() + b.moves;

            return Integer.compare(avalue, bvalue);

        }
    }

    // Test client. [DO NOT EDIT]
    public static void main(String[] args) {
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] tiles = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                tiles[i][j] = in.readInt();
            }
        }
        Board initial = new Board(tiles);
        if (initial.isSolvable()) {
            Solver solver = new Solver(initial);
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) {
                StdOut.println(board);
            }
        } else {
            StdOut.println("Unsolvable puzzle");
        }
    }
}
