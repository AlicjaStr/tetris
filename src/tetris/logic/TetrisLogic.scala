package tetris.logic

import engine.random.{RandomGenerator, ScalaRandomGen}
import tetris.logic.TetrisLogic._

class TetrisLogic(val randomGen: RandomGenerator,
                  val gridDims : Dimensions,
                  val initialBoard: Seq[Seq[CellType]]) {

  def this(random: RandomGenerator, gridDims : Dimensions) =
    this(random, gridDims, makeEmptyBoard(gridDims))

  def this() =
    this(new ScalaRandomGen(), DefaultDims, makeEmptyBoard(DefaultDims))

  private var currentState : GameState = GameState(null, randomGen,
    gridDims, initialBoard)

  currentState = currentState.copy(tetromino = generateTetromino(if(gridDims.width % 2 == 0)
    Point((gridDims.width / 2) - 1, 1) else Point(gridDims.width / 2, 1), randomGen.randomInt(7)))

  def generateTetromino(anchor: Point, randomNum: Int): Tetromino = {
   currentState.generateTetromino(anchor, randomNum)
  }

  def rotateLeft(): Unit = {
   currentState = currentState.rotateLeft()
  }

  def rotateRight(): Unit = {
    currentState = currentState.rotateRight()
  }

  def moveLeft(): Unit = {
    currentState = currentState.moveLeft()
  }

  def moveRight(): Unit = {
    currentState = currentState.moveRight()
  }

  def moveDown(): Unit = {
    currentState = currentState.moveDown()
  }

  def doHardDrop(): Unit = {
  currentState = currentState.drop()
  }

  def isGameOver: Boolean = {
    currentState.gameOver()
  }

  def getCellType(p : Point): CellType = {
    currentState.getCellType(p, currentState.tetromino)
  }
}

object TetrisLogic {

  val FramesPerSecond: Int = 5 // change this to speed up or slow down the game

  val DrawSizeFactor = 1.0 // increase this to make the game bigger (for high-res screens)
  // or decrease to make game smaller



  def makeEmptyBoard(gridDims : Dimensions): Seq[Seq[CellType]] = {
    val emptyLine = Seq.fill(gridDims.width)(Empty)
    Seq.fill(gridDims.height)(emptyLine)
  }


  // These are the dimensions used when playing the game.
  // When testing the game, other dimensions are passed to
  // the constructor of GameLogic.
  //
  // DO NOT USE the variable DefaultGridDims in your code!
  //
  // Doing so will cause tests which have different dimensions to FAIL!
  //
  // In your code only use gridDims.width and gridDims.height
  // do NOT use DefaultDims.width and DefaultDims.height


  val DefaultWidth: Int = 10
  val NrTopInvisibleLines: Int = 4
  val DefaultVisibleHeight: Int = 20
  val DefaultHeight: Int = DefaultVisibleHeight + NrTopInvisibleLines
  val DefaultDims : Dimensions = Dimensions(width = DefaultWidth, height = DefaultHeight)


  def apply() = new TetrisLogic(new ScalaRandomGen(),
    DefaultDims,
    makeEmptyBoard(DefaultDims))

}