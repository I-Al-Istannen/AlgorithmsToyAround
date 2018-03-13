package me.ialistannen.pathfinding.visualize.algorithms.jps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.ialistannen.pathfinding.visualize.algorithms.astar.AStarAlgorithm;
import me.ialistannen.pathfinding.visualize.algorithms.distance.DistanceFunction;
import me.ialistannen.pathfinding.visualize.grid.DefaultGridState;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate;
import me.ialistannen.pathfinding.visualize.grid.GridCoordinate.Direction;

public class JumpPointSearchAlgorithm extends AStarAlgorithm {

  public JumpPointSearchAlgorithm(DistanceFunction distanceFunction) {
    super(distanceFunction, new ArrayList<>(Arrays.asList(Direction.values())));
  }

  @Override
  protected void expand(AStarNode node) {
    markAsClosed(node.getCoordinate());

    List<GridCoordinate> neighbours = new ArrayList<>();

    if (node.getParent() != null) {
      neighbours.addAll(findNeighbours(
          node.getCoordinate(), directionToParent(node)
      ));
    } else {
      for (Direction direction : directions) {
        neighbours.addAll(findNeighbours(node.getCoordinate(), direction));
      }
    }

    List<AStarNode> successors = new ArrayList<>();

    for (GridCoordinate coordinate : neighbours) {
      if (isClosed(coordinate)) {
        continue;
      }

      Direction direction = directionBetween(node.getCoordinate(), coordinate);

      AStarNode jump = jump(node, node.getCoordinate(), direction);

      if (jump != null) {
        addStep(coordinate, DefaultGridState.OPEN_SET);
        successors.add(jump);
      }
    }

    for (AStarNode successor : successors) {
      markNodeForExamination(successor);
    }
  }

  private List<GridCoordinate> findNeighbours(GridCoordinate start, Direction direction) {
    List<GridCoordinate> neighbours = new ArrayList<>();

    switch (direction) {
      case WEST:
      case EAST:
      case NORTH:
      case SOUTH:
        // BASE CASES
        neighbours.add(start.getNeighbour(direction));

        neighbours.addAll(getStraightForcedNeighbours(start, direction));
        break;
      case NORTH_EAST:
      case NORTH_WEST:
      case SOUTH_EAST:
      case SOUTH_WEST:
        // BASE CASES
        neighbours.add(start.getNeighbour(direction));
        neighbours.add(start.add(direction.getxMod(), 0));
        neighbours.add(start.add(0, direction.getyMod()));

        neighbours.addAll(getDiagonalForcedNeighbours(start, direction));
        break;
    }

    return neighbours;
  }

  private List<GridCoordinate> getStraightForcedNeighbours(GridCoordinate start,
      Direction currentDirection) {

    List<GridCoordinate> neighbours = new ArrayList<>();

    GridCoordinate next = start.getNeighbour(currentDirection);

    if (currentDirection.getyMod() == 0) {
      if (isBlocked(start.add(0, 1))) {
        neighbours.add(next.add(0, 1));
      }
      if (isBlocked(start.add(0, -1))) {
        neighbours.add(next.add(0, -1));
      }
    } else {
      if (isBlocked(start.add(1, 0))) {
        neighbours.add(next.add(1, 0));
      }
      if (isBlocked(start.add(-1, 0))) {
        neighbours.add(next.add(-1, 0));
      }
    }

    return neighbours;
  }

  private List<GridCoordinate> getDiagonalForcedNeighbours(GridCoordinate start,
      Direction currentDirection) {

    List<GridCoordinate> neighbours = new ArrayList<>();

    GridCoordinate next = start.getNeighbour(currentDirection);

    if (isBlocked(start.add(-currentDirection.getxMod(), 0))) {
      neighbours.add(next.add(currentDirection.getxMod() * -2, 0));
    }
    if (isBlocked(start.add(0, -currentDirection.getyMod()))) {
      neighbours.add(next.add(0, currentDirection.getyMod() * -2));
    }

    return neighbours;
  }

  private AStarNode jump(AStarNode parent, GridCoordinate coordinate, Direction direction) {
    GridCoordinate newCoordinate = coordinate.getNeighbour(direction);

    if (!canMoveTo(coordinate, newCoordinate)) {
      return null;
    }

    AStarNode nextNode = createNode(parent, newCoordinate, direction);

    addStep(nextNode.getCoordinate(), DefaultGridState.OTHER);

    if (grid.getStateAt(newCoordinate).isEnd()) {
      return nextNode;
    }

    if (direction.isDiagonal()) {
      // if we have a forced neighbour, we are important
      if (!getDiagonalForcedNeighbours(newCoordinate, direction).isEmpty()) {
        return nextNode;
      }

      // horizontal / vertical check
      if (jump(nextNode, newCoordinate, Direction.forMod(0, direction.getyMod())) != null
          || jump(nextNode, newCoordinate, Direction.forMod(direction.getxMod(), 0)) != null) {
        return nextNode;
      }
    } else {
      // if we have a forced neighbour, we are important
      if (!getStraightForcedNeighbours(newCoordinate, direction).isEmpty()) {
        return nextNode;
      }
    }

    return jump(nextNode, newCoordinate, direction);
  }

  private boolean isBlocked(GridCoordinate coordinate) {
    return grid.isBlocked(coordinate);
  }

  private AStarNode createNode(AStarNode parent, GridCoordinate coordinate, Direction direction) {
    double newDistance = parent.getDistanceToStart() + direction.getCost();
    AStarNode node = getCachedOrCreate(
        coordinate,
        new AStarNode(
            newDistance,
            coordinate, parent.getTarget(), parent
        )
    );

    if (newDistance < node.getDistanceToStart()) {
      node.setParent(parent);
      node.setDistanceToStart(newDistance);
    }

    return node;
  }

  private Direction directionToParent(AStarNode child) {
    GridCoordinate parentCoordinate = child.getParent().getCoordinate();
    GridCoordinate childCoordinate = child.getCoordinate();

    return directionBetween(parentCoordinate, childCoordinate);
  }

  private Direction directionBetween(GridCoordinate parent, GridCoordinate child) {
    return Direction.forMod(
        child.getColumn() - parent.getColumn(),
        child.getRow() - parent.getRow()
    );
  }
}
