package edu.neu.ccs.cs5004.eschaton.model.map;

import java.awt.*;

import edu.neu.ccs.cs5004.eschaton.config.Config;
import edu.neu.ccs.cs5004.eschaton.model.map.cell.Cell;
import edu.neu.ccs.cs5004.eschaton.model.map.cell.cellposition.CellPosition;
import edu.neu.ccs.cs5004.eschaton.model.map.cell.celltypes.EschatonCell;
import edu.neu.ccs.cs5004.eschaton.model.map.cell.celltypes.Forest;
import edu.neu.ccs.cs5004.eschaton.model.map.cell.celltypes.Hills;
import edu.neu.ccs.cs5004.eschaton.model.map.cell.celltypes.Mountain;
import edu.neu.ccs.cs5004.eschaton.model.map.cell.celltypes.Plains;

import static edu.neu.ccs.cs5004.eschaton.config.Config.NUMBER_OF_BLOCKS;
import static edu.neu.ccs.cs5004.eschaton.config.Config.X_STEP;
import static edu.neu.ccs.cs5004.eschaton.config.Config.Y_STEP_ONE;
import static edu.neu.ccs.cs5004.eschaton.config.Config.Y_STEP_TWO;
import static edu.neu.ccs.cs5004.eschaton.config.Config.getRandomNumber;

public class Map implements MapInterface {

  private Cell[][] mapCells = new Cell[100][100];
  private Cell[][][] cellGrid = new Cell[20][20][20];
  private Config config;
  private Point origin;
//  private Integer[] startIndexes = new Integer[config.getNumberOfPlayers()];
//  private Point[] startPoints = new Point[config.getNumberOfPlayers()];

  /**
   * The map constructor takes the configuration as a parameter and builds out the game
   * map. The size of the map and the origin are pulled from the config class.
   *
   * There will be a variety of map options that are set in the configuration eventually.
   */
  public Map(Config config) {
    this.mapCells = new Cell[100][100];
    this.cellGrid= new Cell[20][20][20];
    this.config = config;
    this.origin = config.getOrigin();
    generateMap();
  }

  /**
   * This method generates a hexagonal map. It creates a cell at each node and supplies that cell
   * with the array indices organizing the map and the Points used to place them on the map window.
   *
   * The Cell types and contents are also generated at this time,
   * @return Cell[][][] hexagonal grid of Cells.
   */
  private void generateMap(){
    int[] blockStepX = {X_STEP, 0, -X_STEP, -X_STEP, 0, X_STEP};
    int[] blockStepY = {Y_STEP_ONE, Y_STEP_TWO, Y_STEP_ONE, -Y_STEP_ONE, -Y_STEP_TWO, -Y_STEP_ONE};
    int blockSpecialValue = 0;
    int cellSpecial = 0;
    int count = 0;

    Cell tempCell = new EschatonCell(new CellPosition(0, 0, 0),
        new Point(origin.x, origin.y), blockSpecialValue);

    mapCells[origin.x/10][origin.y/10] = tempCell;

    cellGrid[0][0][0] = tempCell;

    for (int distanceFromOrigin = 0; distanceFromOrigin < config.getSizeOfMap();
         distanceFromOrigin++){

      int[] blockXVals = {origin.x,
                          origin.x + X_STEP * distanceFromOrigin,
                          origin.x + X_STEP * distanceFromOrigin,
                          origin.x,
                          origin.x - X_STEP * distanceFromOrigin,
                          origin.x - X_STEP * distanceFromOrigin};

      int[] blockYVals = {origin.y - Y_STEP_TWO * distanceFromOrigin,
                          origin.y - Y_STEP_ONE * distanceFromOrigin,
                          origin.y + Y_STEP_ONE * distanceFromOrigin,
                          origin.y + Y_STEP_TWO * distanceFromOrigin,
                          origin.y + Y_STEP_ONE * distanceFromOrigin,
                          origin.y - Y_STEP_ONE * distanceFromOrigin};

      blockSpecialValue = getRandomNumber(distanceFromOrigin, 0, 0);

      for (int block = 0; block < NUMBER_OF_BLOCKS; block ++){

        int blockXOrdinal = blockXVals[block];
        int blockYOrdinal = blockYVals[block];

        for (int blockSize = 0; blockSize < distanceFromOrigin; blockSize++){
          if(blockSpecialValue == blockSize){
            cellSpecial = getRandomNumber(2, 1, 1);
          }else {
            cellSpecial = 0;
          }

          tempCell = makeNewCell(
              new CellPosition(distanceFromOrigin,block+1, blockSize+1),
              new Point(blockXOrdinal + blockStepX[block] * (blockSize + 1),
                  blockYOrdinal + blockStepY[block] * (blockSize + 1)), cellSpecial);

          cellGrid [distanceFromOrigin][block+1][blockSize+1] = tempCell;
          mapCells[(blockXOrdinal + blockStepX[block] * (blockSize + 1))/10]
              [(blockYOrdinal + blockStepY[block] * (blockSize + 1))/10] = tempCell;
          System.out.println(count++);
        }
      }
    }
  }

  private Cell makeNewCell(CellPosition cellPosition, Point point, Integer special) {
    int randomCell = getRandomNumber(8, 1, 1);
    return  (cellPosition.getCircle() < 1) ?
        new EschatonCell(cellPosition, point, special) : (randomCell == 1) ?
        new Mountain(cellPosition, point, special) : (randomCell < 3 ) ?
        new Hills(cellPosition, point, special) :  (randomCell < 5) ?
        new Forest(cellPosition, point, special) :
        new Plains(cellPosition, point,special);
  }

  /**
   * Returns a cell at the position in the 3d array of the map.
   *
   * @param position Position representing which block to find the cell including block, circle
   *                 and clockwise
   * @return Cell at the requested location incl
   */
  @Override
  public Cell getCellAtPosition(CellPosition position) {
    return cellGrid[position.getCircle()][position.getBlock()][position.getClockwise()];
  }

  public Cell getCellAtPoint(Point point) {
    return mapCells[point.x/10][point.y/10];
  }

  public Cell[][][] getCellGrid() {
    return cellGrid;
  }

//  public Point[] getStartPoints() {
//    return startPoints;
//  }
}
