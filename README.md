# Sudoku Solver (SudokuSolver.java) — CSP / Search

## Solver as a CSP

- Variables: 81 cells (r,c) with domain {1..9} for unknown cells and singleton domains for given cells.
- Constraints: all-different constraints on each row, column, and 3×3 box.
- Goal: assign every variable a value that satisfies all constraints.

## Implementation

1. Representation

   - Domains are stored in a 3D int array: `int[][][] domains = new int[9][9][10]`. A value of `1` at `domains[r][c][v]` means value `v` is currently allowed for cell `(r,c)`.

2. Initial domain computation (`getDomain`)

   - For each cell, the domain is initialized by eliminating values present in the same row, column, and 3×3 box.

3. Constraint propagation (`arcConsistency` function)

   - Deterministic singleton-propagation loop: repeatedly scan cells, and whenever a cell's domain has exactly one possible value, assign that value and remove it from neighboring domains (row, column, box). This loop continues until no further singletons are found.

4. Search (`dfsSudoku`)

   - After propagation, the solver performs depth-first backtracking search. It locates an unassigned cell (current code picks the first empty cell), iterates allowed values from the domain, checks local validity (row/column/box) and recurses.

5. Output
   - `solve(String State)` accepts an 81-character string (rows concatenated), with unknowns represented as `_`. If a solution is found, the method returns an 81-character string of digits; otherwise it returns an empty string.

## Design trade-offs and limitations

- Propagation vs. search: The implemented singleton-propagation step is cheap and often solves easy/medium puzzles entirely. Harder puzzles rely on backtracking.
- State management in recursion: The solver performs no restorative updates to `domains` per recursive call. That reduces copying overhead but can limit correctness or performance in harder search trees. A safer approach is to implement incremental domain updates with backtracking or to copy domain state on recursion.
- Heuristics: The current DFS selects the first empty cell. This is simple but suboptimal. Heuristics such as Minimum Remaining Values (MRV) for variable selection and Least Constraining Value (LCV) for value ordering dramatically reduce search branching.
- Consistency algorithms: The current propagation is singleton-focused. Implementing AC-3 (arc-consistency) or forward-checking would strengthen pruning before search.

## How to Run

Requirements

- Java JDK 8+ (ensure `javac` and `java` are on your PATH).

Compile and run the UI (if you want a quick visual runner):

```bash
cd SudokuClass
javac *.java
java SudokuUI
```
