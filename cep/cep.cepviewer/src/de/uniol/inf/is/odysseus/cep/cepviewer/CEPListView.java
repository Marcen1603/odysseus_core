package de.uniol.inf.is.odysseus.cep.cepviewer;

import de.uniol.inf.is.odysseus.cep.cepviewer.list.NormalTreeList;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.QueryTreeList;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.StatusTreeList;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.cep.epa.CepOperator;
//import de.uniol.inf.is.odysseus.cep.epa.event.CEPEvent;
//import de.uniol.inf.is.odysseus.cep.epa.event.CEPEventAgent;
//import de.uniol.inf.is.odysseus.cep.epa.event.ICEPEventListener;

/**
 * This class defines the list view.
 * 
 * @author Christian
 */
public class CEPListView extends ViewPart {

	// The id of this view.
	public static final String ID = "de.uniol.inf.is.odysseus.cep.cepviewer.listview";
	
	// These are widgets for the list view.
	private TabFolder tabMenu;
	private NormalTreeList normalList;
	private QueryTreeList queryList;
	private StatusTreeList statusList;
	private TabItem normalListItem;
	private TabItem queryListItem;
	private TabItem statusListItem;
	private Label infoLabel;

	/**
	 * This is the constructor.
	 */
	public CEPListView() {
		super();
	}

	/**
	 * This method creates the list view. There will be 20 automata instances
	 * added to test this view.
	 * 
	 * @param parent
	 *            is the widget which contains the list view.
	 */
	@Override
	public void createPartControl(Composite parent) {

		// set the layout
		GridLayout layout = new GridLayout(1, true);
		layout.marginWidth = layout.marginHeight = 0;
		parent.setLayout(layout);

		// create the tab menu of this view
		this.tabMenu = new TabFolder(parent, SWT.NONE);
		this.tabMenu
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		// create tab for the unsorted List
		this.normalList = new NormalTreeList(this.tabMenu, SWT.V_SCROLL);
		this.normalListItem = new TabItem(this.tabMenu, SWT.NONE);
		this.normalListItem.setText("Normal-List");
		this.normalListItem.setToolTipText("This is the unsorted List");
		this.normalListItem.setControl(this.normalList);

		// create tab for the categorized List
		this.queryList = new QueryTreeList(this.tabMenu, SWT.V_SCROLL);
		this.queryListItem = new TabItem(this.tabMenu, SWT.NONE);
		this.queryListItem.setText("Query-List");
		this.queryListItem.setToolTipText("This is the categorized List");
		this.queryListItem.setControl(this.queryList);

		// create tab for the status List
		this.statusList = new StatusTreeList(this.tabMenu, SWT.V_SCROLL);
		this.statusListItem = new TabItem(this.tabMenu, SWT.NONE);
		this.statusListItem.setText("Status-List");
		this.statusListItem.setToolTipText("This is the status List");
		this.statusListItem.setControl(this.statusList);

		// create label for the infos
		this.infoLabel = new Label(parent, SWT.CENTER);
		this.infoLabel.setLayoutData(new GridData(SWT.FILL, SWT.END, true,
				false));
		setInfoData();
	}

	/**
	 * This method adds the state machines
	 */
	@SuppressWarnings("unchecked")
	public void addStateMaschine(CepOperator operator) {
//		CEPEventAgent.getInstance().addCEPEventListener(new ICEPEventListener() {
//			public void cepEventOccurred(CEPEvent event) {
//				// TODO event handling
//			}
//		});
		normalList.addToTree(operator);
		queryList.addToTree(operator);
		statusList.addToTree(operator);
		setInfoData();
	}

	/**
	 * This method should update the infoLabel.
	 */
	public void setInfoData() {
		String infotext = this.statusList.getNumberOfRunning() + " running ; "
				+ this.statusList.getNumberOfFinished() + " finished ; "
				+ this.statusList.getNumberOfAborted() + " aborted";
		this.infoLabel.setText(infotext);
		this.infoLabel.pack();
	}

	/**
	 * @deprecated for testing purpose
	 */
	public void setInfoData(String string) {
		this.infoLabel.setText(string);
		this.infoLabel.pack();
	}
	
	@SuppressWarnings("unchecked")
	public void removeMachine(CepOperator operator) {
		// TODO
	}
	
	/**
	 * This method is called to set the focus to this view.
	 */
	@Override
	public void setFocus() {
		this.tabMenu.setFocus();
	}
	
}
