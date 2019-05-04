package me.ialistannen.treeviewer.view;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Text;
import me.ialistannen.treeviewer.model.Tree;
import org.abego.treelayout.NodeExtentProvider;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.AbstractTreeForTreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;

/**
 * A {@link javafx.scene.layout.Pane} displaying an AST
 */
public class TreePane extends ScrollPane {

  /**
   * @param ast The root node of the AST
   */
  public TreePane(Tree ast) {
    AbstractTreeForTreeLayout<Tree> layout = new AbstractTreeForTreeLayout<Tree>(ast) {
      @Override
      public Tree getParent(Tree tree) {
        return tree.getParent();
      }

      @Override
      public List<Tree> getChildrenList(Tree tree) {
        return new ArrayList<>(tree.getChildren());
      }
    };

    DefaultConfiguration<Tree> defaultConfiguration = new DefaultConfiguration<>(
        20, 20
    );

    NodeExtentProvider<Tree> nodeExtentProvider = new NodeExtentProvider<Tree>() {
      @Override
      public double getWidth(Tree tree) {
        return new Text(tree.getToken().getTokenText()).getLayoutBounds().getWidth() + 30;
//        return 70;
      }

      @Override
      public double getHeight(Tree tree) {
//        return 70;
        return new Text(tree.getToken().getTokenText()).getLayoutBounds().getHeight() + 30;
      }
    };

    TreeLayout<Tree> treeLayout = new TreeLayout<>(
        layout, nodeExtentProvider, defaultConfiguration
    );

    TreeComponent treeComponent = new TreeComponent(treeLayout);

    setPadding(new Insets(20));
    setMaxWidth(Double.MAX_VALUE);
    setMaxHeight(Double.MAX_VALUE);

    setContent(treeComponent);
  }
}
