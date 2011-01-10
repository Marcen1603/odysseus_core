package de.uniol.inf.is.odysseus.cep.cepviewer;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.cep.cepviewer.list.AbstractTreeList;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.CEPEventListener;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.NormalTreeList;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.QueryTreeList;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.StatusTreeList;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.CEPInstance;
import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.cep.epa.CepOperator;
import de.uniol.inf.is.odysseus.cep.epa.StateMachineInstance;

/**
 * This class defines the list view.
 * 
 * @author Christian
 */
public class CEPListView extends ViewPart {

	// These are widgets for the list view.
	private TabFolder tabMenu;
	private NormalTreeList normalList;
	private QueryTreeList queryList;
	private StatusTreeList statusList;
	private TabItem normalListItem;
	private TabItem queryListItem;
	private TabItem statusListItem;
	private Label infoLabel;
	private CEPEventListener listener;
	private ArrayList<CepOperator<?, ?>> operators;

	/**
	 * This is the constructor.
	 */
	public CEPListView() {
		super();
		this.operators = new ArrayList<CepOperator<?, ?>>();
		this.listener = new CEPEventListener(this);
	}

	/**
	 * This method creates the list view. It instantiate three TreeList which
	 * show the StateMachineInstance that were added to the CEPViewer.
	 * 
	 * @param parent
	 *            is the widget which contains the list view.
	 */
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
		this.normalList.createContextMenu(this);
		this.normalListItem = new TabItem(this.tabMenu, SWT.NONE);
		this.normalListItem.setText(StringConst.NORMAL_LIST_TAB_NAME);
		this.normalListItem.setToolTipText(StringConst.NORMAL_LIST_TOOTIP);
		this.normalListItem.setControl(this.normalList);
		// create tab for the categorized List
		this.queryList = new QueryTreeList(this.tabMenu, SWT.V_SCROLL);
		this.queryList.createContextMenu(this);
		this.queryListItem = new TabItem(this.tabMenu, SWT.NONE);
		this.queryListItem.setText(StringConst.QUERY_LIST_TAB_NAME);
		this.queryListItem.setToolTipText(StringConst.QUERY_LIST_TOOLTIP);
		this.queryListItem.setControl(this.queryList);
		// create tab for the status List
		this.statusList = new StatusTreeList(this.tabMenu, SWT.V_SCROLL);
		this.statusList.createContextMenu(this);
		this.statusListItem = new TabItem(this.tabMenu, SWT.NONE);
		this.statusListItem.setText(StringConst.STATUS_LIST_TAB_NAME);
		this.statusListItem.setToolTipText(StringConst.STATUS_LIST_TOOLTIP);
		this.statusListItem.setControl(this.statusList);
		// create label for the infos
		this.infoLabel = new Label(parent, SWT.CENTER);
		this.infoLabel.setLayoutData(new GridData(SWT.FILL, SWT.END, true,
				false));
		this.setInfoData();
	}

	/**
	 * This method is called, if a new instance of the class CepOperator should
	 * be added to the CEPViewer. It implements the ICEPEventListener which
	 * manages the communication with the operator an the CEPViewer. After this
	 * it adds the instances to the lists
	 * 
	 * @param operator
	 *            is an CepOperator which holds all instances of an StateMachine
	 *            and manages the CEP
	 */
	@SuppressWarnings("rawtypes")
	public void addStateMaschine(CepOperator operator) {
		// initialize ICEPEventListener
		this.operators.add(operator);
		operator.getCEPEventAgent().addCEPEventListener(this.listener);
		// add the instances of the operator
		for (Object instance : operator.getInstances()) {
			CEPInstance newInstance = new CEPInstance(
					(StateMachineInstance<?>) instance);
			this.normalList.addToTree(newInstance);
			this.queryList.addToTree(newInstance);
			this.statusList.addToTree(newInstance);
		}
		this.setInfoData();
	}

	/**
	 * This method updates the info label with the current numbers of the
	 * inherited StateMachineInstances.
	 */
	public void setInfoData() {
		this.getSite().getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				String infotext = StringConst.INFO_ALL + " "
						+ CEPListView.this.normalList.getItemCount() + " "
						+ StringConst.STATUS_RUNNING + " "
						+ CEPListView.this.statusList.getNumberOfRunning()
						+ " " + StringConst.STATUS_FINISHED + " "
						+ CEPListView.this.statusList.getNumberOfFinished()
						+ " " + StringConst.STATUS_ABORTED + " "
						+ CEPListView.this.statusList.getNumberOfAborted();
				CEPListView.this.infoLabel.setText(infotext);
			}
		});
	}

	public void refresh() {
		this.getSite().getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				CEPListView.this.normalList.getTree().refresh();
				CEPListView.this.queryList.getTree().refresh();
				CEPListView.this.statusList.getTree().refresh();
			}
		});
	}

	public AbstractTreeList getActiveList() {
		TabItem tab = this.tabMenu.getItem(this.tabMenu.getSelectionIndex());
		if (tab != null && tab.getControl() instanceof AbstractTreeList) {
			return (AbstractTreeList) tab.getControl();
		}
		return null;
	}

	/**
	 * This method is called to set the focus to this view.
	 */
	public void setFocus() {
		this.tabMenu.setFocus();
	}

	public NormalTreeList getNormalList() {
		return normalList;
	}

	public QueryTreeList getQueryList() {
		return queryList;
	}

	public StatusTreeList getStatusList() {
		return statusList;
	}

	public ArrayList<CepOperator<?, ?>> getOperators() {
		return operators;
	}

	public CEPEventListener getListener() {
		return listener;
	}

}
