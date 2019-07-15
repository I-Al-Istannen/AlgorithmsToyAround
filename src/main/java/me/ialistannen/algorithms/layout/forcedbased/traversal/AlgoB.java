package me.ialistannen.algorithms.layout.forcedbased.traversal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import me.ialistannen.algorithms.layout.forcedbased.tree.Node;

/**
 * Solves the algorithm assignment B.
 *
 * <p><br>Runtime:
 * <br>Alle Pfade berechnen: n * Semi-DFS ==> n * (|V| + |E|) ==> n^2? n^3?
 * <br>Min: n
 * </p>
 */
public class AlgoB implements Traversal {

  @Override
  public <T> List<NodeChangeAction<T>> run(List<Node<T>> nodes) {
    List<NodeChangeAction<T>> actions = new ArrayList<>();

    Node<T> start = nodes.get(0);

    List<Path<T>> paths = new ArrayList<>();

    for (Node<T> target : nodes) {
      System.out.println("Path to " + target);
      paths.addAll(Path.deduplicate(
          getPaths(start, target, new HashSet<>()).stream()
              .map(Path::reversed)
              .collect(Collectors.toList())
          )
      );
    }
    System.out.println();
    System.out.println("Final paths!");
    for (Path<T> path : paths) {
      System.out.println(path);
    }

    System.out.println();
    paths.stream()
        .max(Comparator.comparing(Path::length))
        .ifPresent(it -> {
          System.out.println("Picked " + it);
          for (Node<T> node : it.nodes) {
            actions.add(
                NodeChangeAction.builder(node)
                    .withHighlight(true)
                    .withLeftText("✓")
                    .build()
            );
          }
        });

    return actions;
  }

  private <T> List<Path<T>> getPaths(Node<T> start, Node<T> target, Set<Node<T>> closedSet) {
    List<Path<T>> paths = new ArrayList<>();

    if (closedSet.contains(start)) {
      return paths;
    }

    closedSet.add(start);

    for (Node<T> neighbour : start.getNeighbours()) {
      List<Path<T>> subPaths = getPaths(neighbour, target, closedSet)
          .stream()
          .filter(tPath -> tPath.contains(target))
          .collect(Collectors.toList());

      paths.addAll(subPaths);
    }
    closedSet.remove(start);

    if (paths.isEmpty()) {
      paths.add(new Path<>());
    }

    return paths.stream()
        .map(tPath -> tPath.addNode(start))
        .collect(Collectors.toList());
  }

  private static class Path<T> {

    private List<Node<T>> nodes;

    Path() {
      this.nodes = new ArrayList<>();
    }

    /**
     * Ads the given node to the path.
     *
     * @param node the node to add
     * @return the new path
     */
    Path<T> addNode(Node<T> node) {
      Path<T> tPath = new Path<>();
      tPath.nodes = new ArrayList<>(nodes);
      tPath.nodes.add(node);
      return tPath;
    }

    /**
     * Adds the given nodes to the path.
     *
     * @param nodes the nodes to add
     * @return the new path
     */
    Path<T> addNodes(Collection<Node<T>> nodes) {
      Path<T> tPath = new Path<>();
      tPath.nodes = new ArrayList<>(this.nodes);
      tPath.nodes.addAll(nodes);
      return tPath;
    }

    int length() {
      return nodes.size();
    }

    boolean contains(Node<T> node) {
      return nodes.contains(node);
    }

    Path<T> reversed() {
      Path<T> tPath = new Path<>();
      tPath.nodes.addAll(nodes);
      Collections.reverse(tPath.nodes);
      return tPath;
    }

    @Override
    public String toString() {
      return "Path{"
          + nodes.stream()
          .map(Node::getValue)
          .map(Objects::toString)
          .collect(Collectors.joining("->"))
          + '}';
    }

    static <T> List<Path<T>> deduplicate(List<Path<T>> paths) {
      Set<Node<T>> nodes = paths.stream()
          .map(it -> (Set<Node<T>>) new HashSet<>(it.nodes))
          .reduce(AlgoB::intersection)
          .orElseThrow(RuntimeException::new);

      return Collections.singletonList(new Path<T>().addNodes(nodes));
    }
  }

  private static <T> Set<T> intersection(Set<T> first, Set<T> second) {
    Set<T> result = new HashSet<>(first);
    result.retainAll(second);

    return result;
  }
}
