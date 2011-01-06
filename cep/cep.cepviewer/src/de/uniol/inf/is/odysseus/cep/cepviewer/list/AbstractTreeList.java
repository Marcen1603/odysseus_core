package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.cep.cepviewer.CEPAutomataView;
import de.uniol.inf.is.odysseus.cep.cepviewer.CEPListView;
import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;
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
		this.setLayout(new FillLayout());
		this.tree = new TreeViewer(this, style | SWT.SINGLE);
		this.tree.setContentProvider(new TreeContentProvider());
		this.tree.setLabelProvider(new TreeLabelProvider());
		this.tree.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection select = (IStructuredSelection) event
						.getSelection();
				if (select.getFirstElement() instanceof InstanceTreeItem) {
					StateMachineInstance<?> instance = ((InstanceTreeItem) select
							.getFirstElement()).getContent();
					for (IViewReference a : PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage()
							.getViewReferences()) {
						if (a.getId().equals(StringConst.AUTOMATA_VIEW_ID)) {
							CEPAutomataView view = (CEPAutomataView) a
									.getView(false);
							view.clearView();
							view.showAutomata(instance);
						}
					}
				}
			}
		});
		this.root = new LabelTreeItem(null, "Root");
		this.tree.setInput(this.root.getChildren());
	}

	public void createContextMenu(CEPListView view) {
		MenuManager menuManager = new MenuManager();
		Menu contextMenu = menuManager.createContextMenu(this.tree.getTree());
		// Set the MenuManager
		this.tree.getTree().setMenu(contextMenu);
		view.getSite().registerContextMenu(menuManager, this.tree);
	}

	public void changeStatus(StateMachineInstance<?> instance) {
		// TODO: change status?
	}

	public void update() {
		this.getDisplay().asyncExec(new Runnable() {
			public void run() {
				TreeItem selected = tree.getTree().getSelection()[0];
				tree.getTree().select(selected);
			}
		});
	}

	/**
	 * This method should add an object to the tree, if the objectis an instance
	 * of the right class.
	 * 
	 * @param object
	 *            is an object
	 */
	public abstract boolean addToTree(Object object);
	
	public abstract void removeAll();
	
	public abstract boolean remove(InstanceTreeItem item);

	public TreeViewer getTree() {
		return tree;
	}

	
}
