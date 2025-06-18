package tetris.logic

import engine.random.RandomGenerator

case class GameState(tetromino: Tetromino, randomGen: RandomGenerator, gridDims: Dimensions,
                     currentBoard: Seq[Seq[CellType]]) {

  private val initialAnchor : Point = if(gridDims.width % 2 == 0)
    Point((gridDims.width / 2) - 1, 1)
  else Point(gridDims.width / 2, 1)

  def rotateLeft(): GameState = {
    val newTetromino = tetromino.rotateLeft()
    if (!isColliding(newTetromino))
      copy(tetromino = newTetromino)
    else this
  }

  def rotateRight(): GameState = {
    val newTetromino = tetromino.rotateRight()
    if(!isColliding(newTetromino))
      copy(tetromino = newTetromino)
    else this
  }

  def moveLeft(): GameState = {
    val newTetromino = moveTetromino(-1, 0)
    if (!isColliding(newTetromino))
      copy(tetromino = newTetromino)
    else this
  }

  def moveRight(): GameState = {
    val newTetromino = moveTetromino(1, 0)
    if (!isColliding(newTetromino))
      copy(tetromino = newTetromino)
    else this
  }

  def moveDown(): GameState = {
    val newTetromino = moveTetromino(0, 1)

    if (isColliding(newTetromino)) {
      val newBoard = updateBoard(tetromino)
      val clearedBoard = clearFullLines(newBoard)
      val nextTetromino = generateTetromino(initialAnchor, randomGen.randomInt(7))
      copy(tetromino = nextTetromino, currentBoard = clearedBoard)
    } else {
      copy(tetromino = newTetromino)
    }
  }

  def drop(): GameState = {
    val distance = dropDistance(tetromino, 0)
    val droppedTetromino = moveTetromino(0, distance)
    val newBoard = updateBoard(droppedTetromino)
    val clearedBoard = clearFullLines(newBoard)
    val newTetromino = generateTetromino(initialAnchor, randomGen.randomInt(7))

    copy(tetromino = newTetromino, currentBoard = clearedBoard)
  }

  def gameOver(): Boolean = {
    val newTetromino = generateTetromino(initialAnchor, randomGen.randomInt(7))
    newTetromino.body.exists {p => p.y >= 0 && p.y < gridDims.height && p.x >= 0 && p.x < gridDims.width &&
      currentBoard(p.y)(p.x) != Empty}
  }

  def getCellType(point: Point, tetromino: Tetromino): CellType = {
    if(tetromino.body.contains(point)) {
      tetromino.shape match {
        case 0 => ICell
        case 1 => JCell
        case 2 => LCell
        case 3 => OCell
        case 4 => SCell
        case 5 => TCell
        case 6 => ZCell
      }
    }
    else currentBoard(point.y)(point.x)
  }

  private def moveTetromino(x: Int, y: Int): Tetromino = {
    tetromino match {
      case i: ITetromino => new ITetromino(Point(i.anchor.x + x, i.anchor.y + y),
        i.body.map(p => Point(p.x + x, p.y + y)), i.shape)
      case t: StandardTetromino => new StandardTetromino(Point(t.anchor.x + x, t.anchor.y + y),
        t.body.map(p => Point(p.x + x, p.y + y)), t.shape)
      case o: OTetromino => new OTetromino(Point(o.anchor.x + x, o.anchor.y + y),
        o.body.map(p => Point(p.x + x, p.y + y)), o.shape)
    }
  }

  private def dropDistance(currentTetromino: Tetromino, distance: Int): Int = {
    val nextTetromino = moveTetromino(0, distance + 1)
    if (isColliding(nextTetromino))
      distance
    else dropDistance(currentTetromino, distance + 1)
  }

  private def isColliding(tetromino: Tetromino): Boolean = {
    tetromino.body.exists { point =>
      point.x < 0 || point.x >= gridDims.width || point.y >= gridDims.height ||
        point.y < 0 || currentBoard(point.y)(point.x) != Empty
    }
  }

  private def updateBoard(currentTetromino: Tetromino): Seq[Seq[CellType]] = {
    val newBoard = currentBoard.indices.map(rowIndex => {
      currentBoard(rowIndex).indices.map(colIndex => {
        if (currentTetromino.body.contains(Point(colIndex, rowIndex))) {
          getCellType(Point(colIndex, rowIndex), currentTetromino)
        } else {
          currentBoard(rowIndex)(colIndex)
        }
      })
    })
    newBoard
  }

  private def clearFullLines(board: Seq[Seq[CellType]]): Seq[Seq[CellType]] = {
    val clearedBoard = board.filterNot(row => row.forall(cell => cell != Empty))
    val newLines = Seq.fill(gridDims.height - clearedBoard.length)(Seq.fill(gridDims.width)(Empty))
    newLines ++ clearedBoard
  }

  def generateTetromino(anchor : Point, randomNum : Int): Tetromino = {

    randomNum match {
      case 0 => new ITetromino(anchor, List(Point(anchor.x - 1, anchor.y), anchor, Point(anchor.x + 1, anchor.y),
        Point(anchor.x + 2, anchor.y)), 0)
      case 1 => new StandardTetromino(anchor, List(Point(anchor.x - 1, anchor.y - 1), Point(anchor.x - 1, anchor.y),
        anchor, Point(anchor.x + 1, anchor.y)), 1)
      case 2 => new StandardTetromino(anchor, List(Point(anchor.x - 1, anchor.y), anchor, Point(anchor.x + 1, anchor.y),
        Point(anchor.x + 1, anchor.y - 1)), 2)
      case 3 => new OTetromino(anchor, List(anchor, Point(anchor.x, anchor.y - 1), Point(anchor.x + 1, anchor.y - 1),
        Point(anchor.x + 1, anchor.y)), 3)
      case 4 => new StandardTetromino(anchor, List(Point(anchor.x - 1, anchor.y), anchor, Point(anchor.x, anchor.y - 1),
        Point(anchor.x + 1, anchor.y - 1)), 4)
      case 5 => new StandardTetromino(anchor, List(Point(anchor.x - 1, anchor.y), Point(anchor.x, anchor.y - 1),
        anchor, Point(anchor.x + 1, anchor.y)), 5)
      case 6 => new StandardTetromino(anchor, List(Point(anchor.x - 1, anchor.y - 1), Point(anchor.x, anchor.y - 1),
        anchor, Point(anchor.x + 1, anchor.y)), 6)
    }
  }
}
