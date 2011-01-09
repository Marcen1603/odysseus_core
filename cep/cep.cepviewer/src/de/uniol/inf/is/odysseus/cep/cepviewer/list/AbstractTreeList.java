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
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.cep.cepviewer.CEPAutomataView;
import de.uniol.inf.is.odysseus.cep.cepviewer.CEPListView;
import de.uniol.inf.is.odysseus.cep.cepviewer.CEPQueryView;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.AbstractTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.InstanceTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.entry.LabelTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.CEPInstance;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.CEPStatus;
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
		this.root = new LabelTreeItem(null, "Root");
		this.tree.setInput(this.root.getChildren());
		this.tree.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection select = (IStructuredSelection) event
						.getSelection();
				if (select.getFirstElement() instanceof InstanceTreeItem) {
					CEPInstance instance = ((InstanceTreeItem) select
							.getFirstElement()).getContent();
					for (IViewReference a : PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage()
							.getViewReferences()) {
						if (a.getId().equals(StringConst.AUTOMATA_VIEW_ID)) {
							CEPAutomataView view = (CEPAutomataView) a
									.getView(false);
							view.clearView();
							System.out.println("bevor showAutomata");
							try{
							view.showAutomata(instance);
							}catch(Exception e) {
								e.printStackTrace();
							}
							System.out.println("nach showAutomata");
						} else if (a.getId().equals(StringConst.QUERY_VIEW_ID)) {
							CEPQueryView view = (CEPQueryView) a
									.getView(false);
							view.setContent(instance);
						}
					}
				} else {
					System.out.println("keine Instanz");
				}
			}
		});
	}

	public void createContextMenu(CEPListView view) {
		MenuManager menuManager = new MenuManager();
		Menu contextMenu = menuManager.createContextMenu(this.tree.getTree());
		// Set the MenuManager
		this.tree.getTree().setMenu(contextMenu);
		view.getSite().registerContextMenu(menuManager, this.tree);
	}

//	public void update() {
//		this.getDisplay().asyncExec(new Runnable() {
//			public void run() {
//				TreeItem selected = tree.getTree().getSelection()[0];
//				tree.getTree().select(selected);
//			}
//		});
//	}

	/**
	 * This method should add an object to the tree, if the objectis an instance
	 * of the right class.
	 * 
	 * @param object
	 *            is an object
	 */
	public abstract void addToTree(CEPInstance object);
	
	public abstract void removeAll();
	
	public abstract boolean remove(AbstractTreeItem item);

	public abstract void stateChanged(StateMachineInstance<?> instance);
	
	public abstract void statusChanged(StateMachineInstance<?> instance, CEPStatus status);
	
	public TreeViewer getTree() {
		return tree;
	}

	
}
