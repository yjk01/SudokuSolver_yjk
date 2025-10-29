// Name: Jun Kim
// Date: 2/14/2025
// Class: CS 467
// Instructor: Dr. A

import java.util.*;

public class SudokuSolver {
    public String solve(String State) {
        String ans = "";
        int[][] grid = new int[9][9];

        // fill sudoku board based on state
        for (int i = 0; i < State.length(); i++) {
            char c = State.charAt(i);
            if (c == '_') {
                grid[i / 9][i % 9] = 0;
            } else {
                grid[i / 9][i % 9] = c - '0';
            }
        }

        // row, column, 3x3 box
        int[][][] domains = arcConsistency(grid);

        // if arc and dfs is performed, fill sudoku board
        if (domains != null && dfsSudoku(grid, domains)) {
            for (int i = 0; i < 9; i++)
                for (int j = 0; j < 9; j++)
                    ans += grid[i][j];
        }

        return ans;
    }

    private int[][][] arcConsistency(int[][] grid) {
        int[][][] domains = new int[9][9][10];

        // fill domain with possible values
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                domains[r][c] = getDomain(grid, r, c);
            }
        }

        boolean update;

        // loop until no more changes can be made
        do {
            update = false;

            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    // check if cell is empty
                    if (grid[r][c] == 0) {
                        int[] domain = domains[r][c];
                        int cnt = 0;
                        int val = 0;

                        // count number of possible values
                        for (int x = 1; x <= 9; x++) {
                            if (domain[x] == 1) {
                                cnt++;
                                val = x;
                            }
                        }

                        // if only one value is possible, fill in cell
                        if (cnt == 1) {
                            grid[r][c] = val;

                            // clean domain
                            for (int i = 0; i < domains[r][c].length; i++)
                                domains[r][c][i] = 0;

                            update = true;

                            // update domain
                            for (int i = 0; i < 9; i++) {
                                domains[r][i][val] = 0;
                                domains[i][c][val] = 0;
                                domains[r - r % 3 + i / 3][c - c % 3 + i % 3][val] = 0;
                            }
                        }
                    }
                }
            }
        } while (update);

        return domains;
    }

    private int[] getDomain(int[][] grid, int r, int c) {
        int[] domain = new int[10];

        // set as 1 to indicate all values are possible
        for (int i = 0; i < domain.length; i++) {
            domain[i] = 1;
        }

        // ser as 0 if number is already used
        for (int i = 0; i < 9; i++) {
            domain[grid[r][i]] = 0; // row
            domain[grid[i][c]] = 0; // column
            domain[grid[r - r % 3 + i / 3][c - c % 3 + i % 3]] = 0; // 3x3 box
        }

        return domain;
    }

    private boolean dfsSudoku(int[][] grid, int[][][] domains) {
        int[] nothing = null;

        // find cell with no value
        for (int i = 0; i < 9 && nothing == null; i++) {
            for (int j = 0; j < 9 && nothing == null; j++) {
                if (grid[i][j] == 0) {
                    nothing = new int[] { i, j };
                }
            }
        }
        // no more cell to fill so end game
        if (nothing == null) {
            return true;
        }
        int r = nothing[0];
        int c = nothing[1];
        int[] domain = domains[r][c];

        // Try all possible values from the domain
        for (int x = 1; x <= 9; x++) {
            if (domain[x] == 1) {
                boolean valid = true;

                // check if number is valid
                for (int i = 0; i < 9; i++) {
                    if (grid[r][i] == x || grid[i][c] == x
                            || grid[r - r % 3 + i / 3][c - c % 3 + i % 3] == x) {
                        valid = false;
                        break;
                    }
                }

                // if valid, place numver and recurse
                if (valid) {
                    grid[r][c] = x;
                    if (dfsSudoku(grid, domains))
                        return true;

                    grid[r][c] = 0; // reset if not valid
                }
            }
        }

        return false;
    }
}