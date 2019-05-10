package me.ialistannen.treeviewer.view;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.text.Font;
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
    this(ast, Font.getDefault());
  }

  /**
   * @param ast The root node of the AST
   */
  public TreePane(Tree ast, Font font) {
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
        Text text = new Text(tree.getToken().getTokenText());
        text.setFont(font);
        return text.getLayoutBounds().getWidth() + 30;
      }

      @Override
      public double getHeight(Tree tree) {
        Text text = new Text(tree.getToken().getTokenText());
        text.setFont(font);
        return text.getLayoutBounds().getHeight() + 30;
      }
    };

    TreeLayout<Tree> treeLayout = new TreeLayout<>(
        layout, nodeExtentProvider, defaultConfiguration
    );

    TreeComponent treeComponent = new TreeComponent(treeLayout, font);

    setPadding(new Insets(20));
    setMaxWidth(Double.MAX_VALUE);
    setMaxHeight(Double.MAX_VALUE);

    setContent(treeComponent);
  }
}
