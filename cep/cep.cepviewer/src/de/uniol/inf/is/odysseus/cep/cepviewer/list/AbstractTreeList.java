package de.uniol.inf.is.odysseus.cep.cepviewer.list;

import java.util.ArrayList;

import javax.swing.event.EventListenerList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import de.uniol.inf.is.odysseus.cep.cepviewer.event.CEPViewAgent;
import de.uniol.inf.is.odysseus.cep.cepviewer.event.CEPViewEvent;
import de.uniol.inf.is.odysseus.cep.cepviewer.event.ICEPViewListener;
import de.uniol.inf.is.odysseus.cep.epa.StateMachineInstance;
//import de.uniol.inf.is.odysseus.cep.cepviewer.testdata.StateMachineInstance;

/**
 * This abstract Class defines a tree list.
 * 
 * @author Christian
 */
public abstract class AbstractTreeList extends Composite {

	// the tree which should be represented
	private Tree tree;
	
	private EventListenerList listenerList = new EventListenerList();
	
	private ArrayList<StateMachineInstance> machineList;

	// the images that show the status of a tree item
	private final Image running = new Image(getDisplay(), this.getClass()
			.getResourceAsStream("imageA.jpg"));
	private final Image finished = new Image(getDisplay(), this.getClass()
			.getResourceAsStream("imageB.jpg"));
	@SuppressWarnings("unused")
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
	public AbstractTreeList(final Composite parent, int style) {
		super(parent, SWT.NONE);
		this.machineList = new ArrayList<StateMachineInstance>();
		this.setLayout(new FillLayout());
		this.tree = new Tree(this, style | SWT.SINGLE);
		this.tree.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {	
				TreeItem item = tree.getSelection()[0];
				StateMachineInstance instance = machineList.get(tree.indexOf(item));
				CEPViewAgent.getInstance().fireCEPEvent(CEPViewEvent.ITEM_SELECTED, instance);
			}
		});
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
	
	public void addCEPEventListener(ICEPViewListener listener) {
		this.listenerList.add(ICEPViewListener.class, listener);
	}
	
	public void removeCEPEventListener(ICEPViewListener listener) {
		this.listenerList.remove(ICEPViewListener.class, listener);
	}
	
	public void fireCEPEvent(int type) {
		 Object[] listeners = listenerList.getListenerList();
		 ICEPViewListener listener = null;
		 for (int i=0; i<listeners.length; i+=2) {
			 if (listeners[i]==ICEPViewListener.class){
				 listener = (ICEPViewListener)listeners[i+1];
				 CEPViewEvent event = new CEPViewEvent(listener, type, "Event: " + type);
				 listener.cepEventOccurred(event);
			 };
		 };
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
	
	/**
	 * This method returns the list of statemachineinstances 
	 * 
	 * @return the list of statemachineinstances
	 */
	public ArrayList<StateMachineInstance> getMachineList() {
		return this.machineList;
	}
}
