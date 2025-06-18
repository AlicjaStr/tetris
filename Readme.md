#The game of Tetris centers around **tetrominoes**: geometric shapes of 4 orthogonally
connected squares.

You are making a slightly simplified version of this game:
* No “ghost” of where the current tetromino will end up.
* No scoring.
* No difficulty curve (speeding up).
* No hold functionality.
* No box showing the next tetrominoes.
* In the official Tetris, if a tetromino is rotated but the target spot is occupied or out of bounds, the tetromino may slightly be moved to a space that is free via a “wall-kick"
system. We do not implement this, we simply do not rotate if the target spot is occupied or out of bounds.


The drawing and event code, `TetrisGame` interacts with the game logic `TetrisLogic` as follows:

* When drawing the game, `TetrisGame` asks `TetrisLogic` for each cell in the grid what the type is. The cell types are
defined `tetris.logic.CellType` and are either `Empty` or an `<X>Cell` where X is the letter corresponding to the tetromino
that the block originated from. See below for a list of tetrominoes. The drawing code
also calls `isGameOver` and draws a game over text if this is true.
* When the user presses the left, right or down arrow key, then `moveLeft`, `moveRight` or `moveDown` is called
correspondingly.
* When the user presses the `s` key or the up arrow, then `rotateRight` is called. When the user pressed `a` then
`rotateLeft` is called.
* When the user presses the space bar, then `doHardDrop` is called. This immediately drops the current tetromino
to where it is supported by the floor or already placed blocks.
* Every 5 seconds (by default) the method `moveDown` is called to advance the game.
You can adjust the speed of the game by increasing or decreasing the value `TetrisLogic.FramesPerSecond`.



Code style is judged as described in the readable code lectures and the
 [code style grading guideline](https://canvas.vu.nl/courses/78072/pages/code-style). The maximum style grade you can get depends on how much of the previous 8 points you got. For example, if you get 5 points from the tests, did not make your gamestate immutable (0/1), did get the points for map/filter & exists (0.75), and did get the points for rotation using subclasses (0.75). then your maximum style points is (6.5/8) * 2 = 1,625 point.

