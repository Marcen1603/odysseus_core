package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

import de.uniol.inf.is.odysseus.cep.cepviewer.CEPListView;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.AbstractTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.CEPInstance;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.InstanceTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.LabelTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.MachineTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.TreeContentProvider;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.TreeLabelProvider;

/**
 * This abstract Class defines a tree list.
 * 
 * @author Christian
 */
public abstract class AbstractTreeList extends Composite {

	// the listener
	protected CEPTreeListListener listener;
	// the root element of the tree
	protected AbstractTreeItem root;
	// the tree which should be represented
	protected TreeViewer tree;

	/**
	 * This is the constructor.
	 * 
	 * @param parent
	 *            is the widget that inherits this tree list.
	 * @param style
	 *            contains the style of this tree list.
	 */
	public AbstractTreeList(final Composite parent, int style) {
		super(parent, SWT.NONE);
		this.listener = new CEPTreeListListener();
		this.setLayout(new FillLayout());
		this.tree = new TreeViewer(this, style | SWT.SINGLE);
		this.tree.setContentProvider(new TreeContentProvider());
		this.tree.setLabelProvider(new TreeLabelProvider());
		this.root = new LabelTreeItem(null, "Root");
		this.tree.setInput(this.root.getChildren());
		this.tree.addSelectionChangedListener(listener);
	}

	/**
	 * This method should add an object to the tree, if the objectis an instance
	 * of the right class.
	 * 
	 * @param object
	 *            is an object of the class CEPInstance
	 */
	public abstract void addToTree(CEPInstance object);

	/**
	 * This method creates the context menu for the TreeViewer.
	 * 
	 * @param view
	 *            is the view which holds the TreeViewer widget.
	 */
	public void createContextMenu(CEPListView view) {
		MenuManager menuManager = new MenuManager();
		Menu contextMenu = menuManager.createContextMenu(this.tree.getTree());
		// Set the MenuManager
		this.tree.getTree().setMenu(contextMenu);
		view.getSite().registerContextMenu(menuManager, this.tree);
	}

	/**
	 * This method should remove an InstanceTreeItem from the tree within the
	 * TreeViewer.
	 * 
	 * @param item
	 *            is the item which should be removed
	 */
	public abstract boolean remove(InstanceTreeItem item);

	/**
	 * This method should remove a MachineTreeItem and all InstanceTreeItems
	 * which are children of the MachineTreeItem from the tree within the
	 * TreeViewer.
	 * 
	 * @param item
	 *            is the item which should be removed
	 */
	public abstract boolean remove(MachineTreeItem item);

	/**
	 * This method should remove all enties from the tree within the TreeViewer.
	 * 
	 * @param object
	 *            is an object of the class CEPInstance
	 */
	public abstract void removeAll();

	/**
	 * This is the getter for the CEPTreeListListener.
	 * 
	 * @return the listener
	 */
	public CEPTreeListListener getListener() {
		return listener;
	}

	/**
	 * This is the getter for the TreeViewer.
	 * 
	 * @return the insatcne of the TreeViewer
	 */
	public TreeViewer getTree() {
		return tree;
	}

}
