package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

import de.uniol.inf.is.odysseus.cep.cepviewer.testdata.StateMachineInstance;

/**
 * This abstract Class defines a tree list.
 * 
 * @author Christian
 */
public abstract class AbstractTreeList extends Composite {

	// the tree which should be represented
	private Tree tree;

	// the images that show the status of a tree item
	private final Image running = new Image(getDisplay(), this.getClass()
			.getResourceAsStream("imageA.jpg"));
	private final Image finished = new Image(getDisplay(), this.getClass()
			.getResourceAsStream("imageB.jpg"));
	private final Image aborted = new Image(getDisplay(), this.getClass()
			.getResourceAsStream("imageC.jpg"));

	/**
	 * This is the constructor.
	 * 
	 * @param parent
	 *            is the widget that inherits this tree list.
	 * @param style
	 *            contains the style of this tree list.
	 */
	public AbstractTreeList(Composite parent, int style) {
		super(parent, SWT.NONE);
		this.setLayout(new FillLayout());
		this.tree = new Tree(this, style | SWT.SINGLE);
	}

	/**
	 * This method assigns an image to a tree item.
	 * 
	 * @param item
	 *            is a tree item
	 */
	public void setStatusImage(StateTreeItem item) {
		if (item.getStateMachineInstance().getCurrentState().isAccepting()) {
			item.setImage(this.finished);
		} else {
			item.setImage(this.running);
		}
	}

	/**
	 * This method returns the tree.
	 * 
	 * @return the tree.
	 */
	public Tree getTree() {
		return this.tree;
	}

	/**
	 * This method should handle the addition of a tree item form a state
	 * machine instance.
	 * 
	 * @param stateMachineInstance
	 *            is an state machine instance
	 */
	public abstract void addStateMachineInstance(
			StateMachineInstance stateMachineInstance);
}
