import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

// Models a board in the 8-puzzle game or its generalization.
public class Board {
    private int[][] tiles;
    private int N;
    private int hamming;
    private int manhattan;

    // Construct a board from an N-by-N array of tiles, where
    // tiles[i][j] = tile at row i and column j, and 0 represents the blank
    // square.
    public Board(int[][] tiles) {
        this.N = tiles.length;
        this.tiles = new int[this.N][this.N];
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                this.tiles[i][j] = tiles[i][j]; //initialize the board 2D array

                // Calculate the manhattan and hamming numbers.
                int expectValue = j + 1 + i * this.N;
                int actualValue = this.tiles[i][j];
                int expectRow = (int) (actualValue - 1) / this.N;   // to calculate the manhattan number
                int expectCol = (int) (actualValue - 1) % this.N;  // to calculate the manhattan number
                if (actualValue != 0 && actualValue != expectValue) {
                    this.hamming++;
                    this.manhattan += Math.abs(i - expectRow) + Math.abs(j - expectCol);
                }
            }
        }
    }

    // Tile at row i and column j.
    public int tileAt(int i, int j) {
        return this.tiles[i][j];
    }

    // Size of this board.
    public int size() {
        return this.N;
    }

    // Number of tiles out of place.
    public int hamming() {
        return this.hamming;
    }

    // Sum of Manhattan distances between tiles and goal.
    public int manhattan() {
        return this.manhattan;
    }

    // Is this board the goal board?
    public boolean isGoal() {
//        for (int row = 0; row < this.size(); row++) {
//            for (int col = 0; col < this.size(); col++) {
//                int correct = (col + 1) + row * this.N;
//                if (this.tiles[row][col] != correct) {
//                    return false;
//                }
//            }
//        }
//        return true;
        return manhattan() == 0;
    }

    // Is this board solvable?
    public boolean isSolvable() {
        int inversion = inversions();
        int sum = -1;
        for (int row = 0; row < this.N; row++) {
            for (int col = 0; col < this.N; col++) {
                if (this.tiles[row][col] == 0) {
                    sum = row;
                    break;
                }
            }
        }

        if (this.N % 2 == 0) {
            return (inversion + sum) % 2 != 0;
        } else {
            return inversion % 2 == 0;
        }

    }

    // Does this board equal that?
    public boolean equals(Board that) {
        if (that.size() != this.size()) {
            return false;
        }
        for (int row = 0; row < this.size(); row++) {
            for (int col = 0; col < this.size(); col++) {
                if (this.tiles[row][col] != that.tiles[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    // All neighboring boards.
    public Iterable<Board> neighbors() {
        Queue<Board> queue = new Queue<Board>();
        int zeroPos = blankPos();
        int zeroCol = zeroPos % this.N;             // collumn of zero
        int zeroRow = (zeroPos - zeroCol) / this.N; // row of zero

        int[][] clone = cloneTiles();
        for (int row = 0; row < clone.length; row++) {
            for (int col = 0; col < clone[0].length; col++) {
                if (Math.abs(row - zeroRow) + Math.abs(col - zeroCol) == 1) {
                    clone[zeroRow][zeroCol] = clone[row][col];
                    clone[row][col] = 0;
                    Board neighbor = new Board(clone);
                    queue.enqueue(neighbor);
                    clone[row][col] = clone[zeroRow][zeroCol];
                    clone[zeroRow][zeroCol] = 0;
                }
            }
        }
        return queue;

    }

    // String representation of this board.
    public String toString() {
        String s = N + "\n";
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s += String.format("%2d", tiles[i][j]);
                if (j < N - 1) {
                    s += " ";
                }
            }
            if (i < N - 1) {
                s += "\n";
            }
        }
        return s;
    }

    // Helper method that returns the position (in row-major order) of the
    // blank (zero) tile.
    private int blankPos() {
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                if (this.tiles[i][j] == 0) {
                    return j + i * this.N;
                }
            }
        }
        return -1;
    }

    // Helper method that returns the number of inversions.
    private int inversions() {
        int inversions = 0;
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                for (int k = 0; k < this.N; k++) {
                    for (int l = 0; l < this.N; l++) {
                        if ((k * this.N + l) > (i * this.N + j)
                                && this.tiles[i][j] != 0 && this.tiles[k][l] != 0
                                && this.tiles[i][j] > this.tiles[k][l]) {
                            inversions++;
                        }

                    }
                }
            }
        }
        return inversions;
    }

    // Helper method that clones the tiles[][] array in this board and
    // returns it.
    private int[][] cloneTiles() {
        int[][] cloneArr = new int[this.N][this.N];
        for (int row = 0; row < cloneArr.length; row++) {
            for (int col = 0; col < cloneArr[0].length; col++) {
                cloneArr[row][col] = this.tiles[row][col];
            }
        }
        return cloneArr;
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
        Board board = new Board(tiles);
        StdOut.println(board.hamming());
        StdOut.println(board.manhattan());
        StdOut.println(board.isGoal());
        StdOut.println(board.isSolvable());
        for (Board neighbor : board.neighbors()) {
            StdOut.println(neighbor);
        }
    }
}
