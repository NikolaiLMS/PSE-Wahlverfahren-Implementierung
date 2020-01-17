package edu.pse.beast.highlevel.javafx;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.pse.beast.datatypes.propertydescription.PreAndPostConditionsDescription;
import edu.pse.beast.highlevel.javafx.resultpresenter.ResultPresenterNEW;
import edu.pse.beast.propertychecker.Result;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 * The Class ChildTreeItem.
 */
public abstract class ChildTreeItem extends CustomTreeItem {

    /** The results. */
    private ArrayList<ResultTreeItem> results =
            new ArrayList<ResultTreeItem>();

    /** The prop name. */
    private Label propName;

    /** The check box. */
    private CheckBox checkBox = new CheckBox();

    /** The parent. */
    private final ParentTreeItem parent;

    /** The disabled. */
    // private ImageView statusIcon = new ImageView();
    private boolean disabled = false;

    /** The result tree items. */
    private final List<TreeItem<CustomTreeItem>> resultTreeItems =
            new ArrayList<TreeItem<CustomTreeItem>>();

    /**
     * The constructor.
     *
     * @param values
     *            the values
     * @param parentItem
     *            the parent item
     * @param treeItemReference
     *            the tree item reference
     */
    ChildTreeItem(final ChildTreeItemValues values,
                  final ParentTreeItem parentItem,
                  final TreeItem<CustomTreeItem> treeItemReference) {
        this.parent = parentItem;
        this.checkBox.setSelected(values.getCheckBoxStatus());
        this.propName = new Label(values.getPropertyName());
        this.disabled = values.isDisabled();
        this.setTreeItemReference(treeItemReference);
        treeItemReference.setValue(this);
        for (Iterator<Result> iterator =
                values.getResults().iterator();
                iterator.hasNext();) {
            Result result = (Result) iterator.next();
            addResult(result);
        }
        init();
    }

    /**
     * Instantiates a new child tree item.
     *
     * @param name
     *            the name
     * @param parentItem
     *            the parent item
     * @param treeItemReference
     *            the tree item reference
     */
    ChildTreeItem(final String name,
                  final ParentTreeItem parentItem,
                  final TreeItem<CustomTreeItem> treeItemReference) {
        this.parent = parentItem;
        propName = new Label(name);
        this.setTreeItemReference(treeItemReference);
        treeItemReference.setValue(this);
        init();
    }

    /**
     * Inits the.
     */
    private void init() {
        this.setAlignment(Pos.CENTER_LEFT);
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(final ObservableValue<? extends Boolean> ov,
                                final Boolean oldValue, final Boolean newValue) {
                if (!disabled) {
                    parent.childCheckboxChanged();
                }
            }
        });
        this.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(final MouseEvent event) {
                wasClicked();
            }
        });
        this.getChildren().add(checkBox);
        this.getChildren().add(new Separator(Orientation.VERTICAL));
        this.getChildren().add(propName);
    }

    /**
     * Checks if is selected.
     *
     * @return true, if is selected
     */
    public boolean isSelected() {
        return checkBox.isSelected();
    }

    /**
     * Was clicked.
     */
    private void wasClicked() {
        parent.wasClicked(false);
        if (results.size() > 0) {
            results.get(results.size() - 1).wasClicked();
        }
    }

    /**
     * Sets the value of the checkbox, while disabling the listener for the
     * checkbox beforehand, and re-enabling it afterwards.
     *
     * @param state
     *            new checkbox value
     */
    public void setSelected(final boolean state) {
        this.disabled = true;
        checkBox.setSelected(state);
        this.disabled = false;
    }

    /**
     * Adds the result.
     *
     * @param result
     *            the result
     */
    public void addResult(final Result result) {
        ResultTreeItem resultItem = new ResultTreeItem(result, this);
        results.add(resultItem);
        TreeItem<CustomTreeItem> reference =
                new TreeItem<CustomTreeItem>(resultItem);
        this.getTreeItemReference().getChildren().add(reference);
        // this.getChildren().add(resultItem);
        addChildrenToStage();
        this.update();
    }
    //
    // public void setStatus(AnalysisStatus status) {
    // this.status = status;
    // checkBox.setText(status.toString());
    // }

    // public Result getResult() {
    // return result;
    // }

    /**
     * Notifies the child object that its result object changed, so it has to
     * update its gui.
     */
    public void update() {
        // TODO ?
    }

    /**
     * Reset result.
     *
     * @param result
     *            the result
     */
    public void resetResult(final Result result) {
        this.addResult(result);
    }

    /**
     * Gets the analysis type.
     *
     * @return the analysis type
     */
    public abstract AnalysisType getAnalysisType();

    /**
     * Gets the pre and post properties.
     *
     * @return the pre and post properties
     */
    public PreAndPostConditionsDescription getPreAndPostProperties() {
        return parent.getPreAndPostProperties();
    }

    // public void setPresentable() {
    //
    // if (result != null && result.isFinished()) {
    // if (!result.isValid()) {
    // this.setBackground(new Background(new BackgroundFill(Color.YELLOW,
    // CornerRadii.EMPTY, Insets.EMPTY)));
    // } else {
    // if (result.isSuccess()) {
    // this.setBackground(
    // new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY,
    // Insets.EMPTY)));
    // } else {
    // this.setBackground(new Background(new BackgroundFill(Color.RED,
    // CornerRadii.EMPTY, Insets.EMPTY)));
    // }
    // }
    // }
    //
    // }
    //
    /**
     * Reset presentable.
     */
    public void resetPresentable() {
        this.setBackground(
                new Background(
                        new BackgroundFill(Color.WHITE, CornerRadii.EMPTY,
                                           Insets.EMPTY)
                        )
        );
    }

    /**
     * Gets the values.
     *
     * @return the values
     */
    public ChildTreeItemValues getValues() {
        ArrayList<Result> tmpList = new ArrayList<Result>();
        for (Iterator<ResultTreeItem> iterator = results.iterator();
                iterator.hasNext();) {
            ResultTreeItem result = (ResultTreeItem) iterator.next();
            tmpList.add(result.getResult());
        }
        return new ChildTreeItemValues(propName.getText(),
                                       checkBox.isSelected(),
                                       false, tmpList);
    }

    /**
     * Adds the children to stage.
     */
    public void addChildrenToStage() {
        resultTreeItems.clear();
        TreeItem<CustomTreeItem> item2 = this.getTreeItemReference();
        item2.isExpanded();
        ObservableList<TreeItem<CustomTreeItem>> children2 =
                item2.getChildren();
        children2.size();
        this.getTreeItemReference().getChildren().clear();
        for (Iterator<ResultTreeItem> iterator = results.iterator();
                iterator.hasNext();) {
            ResultTreeItem item = (ResultTreeItem) iterator.next();
            item.setPresentable();
            resultTreeItems.add(new TreeItem<CustomTreeItem>(item));
        }
        this.getTreeItemReference().getChildren().addAll(resultTreeItems);
    }

    /**
     * Delete result.
     *
     * @param resultTreeItem
     *            the result tree item
     */
    public void deleteResult(final ResultTreeItem resultTreeItem) {
        results.remove(resultTreeItem);
        addChildrenToStage();
    }

    /**
     * Shows the result depending on the current presentation type.
     *
     * @param result
     *            the result
     */
    public void showResult(final Result result) {
        ResultPresenterNEW.getInstance().setResult(result);
    }
}
