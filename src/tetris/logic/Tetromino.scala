package tetris.logic

abstract class Tetromino(val anchor: Point, val body: List[Point], val shape: Int) {
  def rotateLeft() : Tetromino
  def rotateRight() : Tetromino
}

class StandardTetromino(anchor: Point, tetromino: List[Point], shape: Int) extends Tetromino(anchor, tetromino, shape)
{
  def rotateLeft(): StandardTetromino = {
    val newTetromino = tetromino.map{case Point(x, y) => Point(anchor.x + y - anchor.y, anchor.y - x + anchor.x)}
    new StandardTetromino(anchor, newTetromino, shape)
  }

  def rotateRight(): StandardTetromino = {
    val newTetromino = tetromino.map{case Point(x, y) => Point(anchor.x - y + anchor.y, anchor.y + x - anchor.x)}
    new StandardTetromino(anchor, newTetromino, shape)
  }
}

class OTetromino(anchor: Point, tetromino: List[Point], shape: Int) extends Tetromino(anchor, tetromino, shape) {

  def rotateLeft(): Tetromino = this
  def rotateRight(): Tetromino = this
}

class ITetromino(anchor: Point, tetromino: List[Point], shape: Int) extends Tetromino(anchor, tetromino, shape) {

  def rotateLeft(): Tetromino = {
    val newTetromino = tetromino.map{case Point(x, y) => Point(anchor.x + y - anchor.y, anchor.y - x + anchor.x + 1)}
    new ITetromino(anchor, newTetromino, shape)
  }
  def rotateRight(): Tetromino = {
    val newTetromino = tetromino.map{case Point(x, y) => Point(anchor.x - y + anchor.y + 1, anchor.y + x - anchor.x)}
    new ITetromino(anchor, newTetromino, shape)
  }
}
