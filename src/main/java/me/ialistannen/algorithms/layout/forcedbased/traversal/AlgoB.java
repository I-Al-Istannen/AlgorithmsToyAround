package me.ialistannen.algorithms.layout.forcedbased.traversal;

import java.util.ArrayList;
import java.util.Arrays;
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
 * <br>PathTree bauen: n log n? << n^3
 * <br>PathTree reduce: n
 * </p>
 */
public class AlgoB implements Traversal {

  @Override
  public <T> List<NodeChangeAction<T>> run(List<Node<T>> nodes) {
    List<NodeChangeAction<T>> actions = new ArrayList<>();

    Node<T> tNode = nodes.get(0);

    List<Path<T>> paths = new ArrayList<>();

    for (Node<T> target : nodes) {
      System.out.println("Path to " + target);
      paths.addAll(Path.deduplicate(
          getPaths(tNode, target, new HashSet<>()).stream()
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

    public Path() {
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

    Path<T> dropFirst() {
      if (length() == 0) {
        return this;
      }
      Path<T> path = new Path<>();
      path.nodes = nodes.subList(1, nodes.size());
      return path;
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
      System.out.println();
      System.out.println(">>> Dedupe: " + paths);
//      PathTree<T> pathTree = new PathTree<>(paths.get(0).nodes.get(0));
//
//      for (Path<T> path : paths) {
//        pathTree.insertPath(path.dropFirst());
//      }
//
////      System.out.println(pathTree);
//      System.out.println(pathTree.getValues(new HashSet<>()));
//      System.out.println(pathTree.countNodes(new HashSet<>()));
//
//      System.out.println(pathTree.reduce());
//      System.out.println(pathTree);
//      System.out.println(pathTree.getValues(new HashSet<>()));
//      System.out.println(pathTree.countNodes(new HashSet<>()));
//      System.out.println();
//
//      System.out.println(pathTree.toPaths());
//      System.out.println();
//
//      System.out.println("<<<");
//      System.out.println();
//      return pathTree.toPaths();
      Set<Node<T>> nodes = paths.stream()
          .map(it -> (Set<Node<T>>) new HashSet<>(it.nodes))
          .reduce(AlgoB::intersection)
          .orElseThrow(RuntimeException::new);

      return Arrays.asList(new Path<T>().addNodes(nodes));
    }
  }

  private static <T> Set<T> intersection(Set<T> first, Set<T> second) {
    Set<T> result = new HashSet<>(first);
    result.retainAll(second);

    return result;
  }

  private static class PathTree<T> {

    private Node<T> value;
    private List<PathTree<T>> children;

    public PathTree(Node<T> value) {
      this.value = value;
      this.children = new ArrayList<>();
    }

    public void addChild(PathTree<T> child) {
      if (children.stream().anyMatch(it -> it.value.equals(child.value))) {
        return;
      }
      children.add(child);
    }

    public void addChild(Node<T> child) {
      addChild(new PathTree<>(child));
    }

    public void insertPath(Path<T> path) {
      if (path.length() == 0) {
        return;
      }
      PathTree<T> subtree = find(path.nodes.get(0));

      if (subtree == null) {
        addChild(path.nodes.get(0));
        subtree = find(path.nodes.get(0));
      }

      if (path.length() == 1) {
        return;
      }
      subtree.addChild(
          find(path.nodes.get(1)) == null ? new PathTree<>(path.nodes.get(1))
              : find(path.nodes.get(1))
      );

      insertPath(path.dropFirst());
    }

    private PathTree<T> find(Node<T> node) {
      return find(node, new HashSet<>());
    }

    private PathTree<T> find(Node<T> node, Set<PathTree<T>> visited) {
      if (value.equals(node)) {
        return this;
      }
      if (!visited.add(this)) {
        return null;
      }
      for (PathTree<T> child : children) {
        PathTree<T> childPath = child.find(node, visited);
        if (childPath != null) {
          return childPath;
        }
      }
      return null;
    }

    private List<T> getValues(Set<Node<T>> visited) {
      List<T> result = new ArrayList<>();
      if (visited.contains(value)) {
        return result;
      }

      visited.add(value);
      result.add(value.getValue());

      for (PathTree<T> child : children) {
        result.addAll(child.getValues(visited));
      }
      return result;
    }

    int countNodes(Set<Node<T>> visited) {
      return children.stream()
          .filter(tree -> visited.add(tree.value))
          .map(tree -> tree.countNodes(visited))
          .reduce(Integer::sum)
          .map(it -> it + 1)
          .orElse(1);
    }

    List<Path<T>> toPaths() {
      Path<T> me = new Path<T>().addNode(value);
      List<Path<T>> paths = new ArrayList<>();

      for (PathTree<T> child : children) {
        List<Path<T>> childPaths = child.toPaths();
        for (Path<T> childPath : childPaths) {
          paths.add(me.addNodes(childPath.nodes));
        }
      }

      if (paths.isEmpty()) {
        paths.add(me);
      }

      return paths
          .stream()
          .min(Comparator.comparing(Path::length))
          .map(Collections::singletonList)
          .orElse(Collections.emptyList());
    }

    public int getChildCount() {
      return children.size();
    }

    public boolean reduce() {
      System.out.println(
          "Reducing: " + getValues(new HashSet<>()));
      if (getChildCount() <= 1) {
        return false;
      }
      if (children.stream().allMatch(it -> it.getChildCount() <= 1)) {
        return false;
      }
      this.children = children.stream()
          .flatMap(tree -> tree.getChildrenOrSelf().stream())
          .distinct()
          .collect(Collectors.toList());

      System.out.println("Recursing");
      reduce();
      System.out.println(
          "Final: " + getValues(new HashSet<>()));

      return true;
    }

    public List<PathTree<T>> getChildrenOrSelf() {
      return children.isEmpty() ? Collections.singletonList(this) : children;
    }

    @Override
    public String toString() {
      return "PathTree{" +
          "value=" + value +
          ", children=" + children +
          '}';
    }
  }
}
