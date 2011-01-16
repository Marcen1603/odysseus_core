package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

import de.uniol.inf.is.odysseus.cep.cepviewer.CEPListView;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.AbstractTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.InstanceTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.LabelTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.MachineTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.TreeContentProvider;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.TreeLabelProvider;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.CEPInstance;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.CEPStatus;
import de.uniol.inf.is.odysseus.cep.epa.StateMachineInstance;

/**
 * This abstract Class defines a tree list.
 * 
 * @author Christian
 */
public abstract class AbstractTreeList extends Composite {

	// the tree which should be represented
	protected TreeViewer tree;
	// the root element of the tree
	protected AbstractTreeItem root;
	// the listener
	protected CEPTreeListListener listener;

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

	public void createContextMenu(CEPListView view) {
		MenuManager menuManager = new MenuManager();
		Menu contextMenu = menuManager.createContextMenu(this.tree.getTree());
		// Set the MenuManager
		this.tree.getTree().setMenu(contextMenu);
		view.getSite().registerContextMenu(menuManager, this.tree);
	}

	/**
	 * This method should add an object to the tree, if the objectis an instance
	 * of the right class.
	 * 
	 * @param object
	 *            is an object
	 */
	public abstract void addToTree(CEPInstance object);

	public abstract void removeAll();

	public abstract boolean remove(InstanceTreeItem item);

	public abstract boolean remove(MachineTreeItem item);

	public abstract void stateChanged(StateMachineInstance<?> instance);

	public abstract void statusChanged(StateMachineInstance<?> instance,
			CEPStatus status);

	public TreeViewer getTree() {
		return tree;
	}

	public CEPTreeListListener getListener() {
		return listener;
	}
	
	
}
