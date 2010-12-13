package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.cep.cepviewer.CEPAutomataView;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.AbstractState;
import de.uniol.inf.is.odysseus.cep.epa.CepOperator;
import de.uniol.inf.is.odysseus.cep.epa.StateMachineInstance;

/**
 * This abstract Class defines a tree list.
 * 
 * @author Christian
 */
public abstract class AbstractTreeList extends Composite {

	// the tree which should be represented
	private TreeViewer tree;

	private CEPTreeItem root;

	// the images that show the status of a tree item
	private final Image running = new Image(getDisplay(), this.getClass()
			.getResourceAsStream("imageA.jpg"));
	private final Image finished = new Image(getDisplay(), this.getClass()
			.getResourceAsStream("imageB.jpg"));
	@SuppressWarnings("unused")
	private final Image aborted = new Image(getDisplay(), this.getClass()
			.getResourceAsStream("imageC.jpg"));

	private static final String AUTOMATA_VIEW_ID = "de.uniol.inf.is.odysseus.cep.cepviewer.automataview";

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
		this.root = new CEPTreeItem();
		this.tree = new TreeViewer(this, style | SWT.SINGLE);
		this.tree.setContentProvider(new MyTreeContentProvider());
		this.tree.setLabelProvider(new MyTreeLabelProvider());
		this.tree.addSelectionChangedListener(new ISelectionChangedListener() {
			@SuppressWarnings("unchecked")
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection select = (IStructuredSelection) event
						.getSelection();
				if (((CEPTreeItem) select.getFirstElement()).getContent() instanceof StateMachineInstance) {
					StateMachineInstance instance = ((StateMachineInstance) ((CEPTreeItem) select
							.getFirstElement()).getContent());
					for (IViewReference a : PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage()
							.getViewReferences()) {
						if (a.getId().equals(AUTOMATA_VIEW_ID)) {
							if (event.getSource() instanceof AbstractState) {
								((CEPAutomataView) a.getView(false))
										.showAutomata(instance);
							}
						}
					}
				}
			}
		});
		this.createContextMenu();
	}

	public void createContextMenu() {
		Menu menu = new Menu(this.tree.getTree());
		MenuItem removeItem = new MenuItem(menu, SWT.PUSH);
		removeItem.setText("Remove");
		removeItem.addListener(SWT.Selection, new Listener() {
			@SuppressWarnings("unchecked")
			public void handleEvent(Event event) {
				IStructuredSelection select = (IStructuredSelection) tree
						.getSelection();
				if (!select.isEmpty()) {
					Object object = ((CEPTreeItem) select.getFirstElement())
							.getContent();
					if (object instanceof StateMachineInstance
							|| object instanceof CepOperator) {
						tree.getTree().getSelection()[0].dispose();
					} else {
						System.out.println("must not be disposed");
					}
				} else {
					System.out.println("Nothing to dispose");
				}
			}
		});
		menu.setVisible(false);
		this.tree.getTree().setMenu(menu);
	}

	/**
	 * This method assigns an image to a tree item.
	 * 
	 * @param item
	 *            is a tree item
	 */
	@SuppressWarnings("unchecked")
	// TODO in den LabelProvider einbauen
	public void setStatusImage(TreeItem item) {
		if (((StateMachineInstance) item.getData("Instance")).getCurrentState()
				.isAccepting()) {
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
	public TreeViewer getTree() {
		return this.tree;
	}

	public CEPTreeItem getRoot() {
		return this.root;
	}

	/**
	 * This method should handle the addition of a tree item form a state
	 * machine instance.
	 * 
	 * @param instance
	 *            is an state machine instance
	 */
	@SuppressWarnings("unchecked")
	public abstract boolean addToTree(StateMachineInstance instance);

	/**
	 * This method should handle the addition of a tree item form a state
	 * machine instance.
	 * 
	 * @param instance
	 *            is an state machine instance
	 */
	@SuppressWarnings("unchecked")
	public abstract boolean addToTree(CepOperator operator);

}
