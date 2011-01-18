package de.uniol.inf.is.odysseus.cep.cepviewer;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.cep.cepviewer.list.AbstractTreeList;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.CEPEventListener;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.NormalTreeList;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.QueryTreeList;
import de.uniol.inf.is.odysseus.cep.cepviewer.list.StatusTreeList;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.CEPInstance;
import de.uniol.inf.is.odysseus.cep.cepviewer.listmodel.InstanceTreeItem;
import de.uniol.inf.is.odysseus.cep.cepviewer.util.IntConst;
import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;

import org.eclipse.jface.viewers.IStructuredSelection;
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

	// the ID of this view
	public static final String ID = "de.uniol.inf.is.odysseus.cep.cepviewer.listview";
	private Label infoLabel;
	// the event listener
	private CEPEventListener listener;
	private NormalTreeList normalList;
	private TabItem normalListItem;
	// the list of CepOperators handled by the CEPViewer
	private ArrayList<CepOperator<?, ?>> operators;
	private QueryTreeList queryList;
	private TabItem queryListItem;
	private StatusTreeList statusList;
	private TabItem statusListItem;
	// the widgets for the list view.
	private TabFolder tabMenu;

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
	 * This method returns the list currently shown in the CEPViewer.
	 * 
	 * @return
	 */
	public AbstractTreeList getActiveList() {
		TabItem tab = this.tabMenu.getItem(this.tabMenu.getSelectionIndex());
		if (tab != null && tab.getControl() instanceof AbstractTreeList) {
			return (AbstractTreeList) tab.getControl();
		}
		return null;
	}



	/**
	 * This method is called, if the state of an instance changed. It checks if
	 * the changed instance is the same instance as the one selected in the
	 * currently shown list. If this is the case, the listener of the active
	 * list is informed to refresh all views with the new data of the instance.
	 * 
	 * @param instance
	 *            is the instance that has been changed.
	 */
	public void selectionChanged(StateMachineInstance<?> instance) {
		AbstractTreeList list = this.getActiveList();
		IStructuredSelection select = (IStructuredSelection) list.getTree()
				.getSelection();
		if (select.getFirstElement() instanceof InstanceTreeItem) {
			// if the selected item is a CEPInstance show it's data
			CEPInstance cepInstance = ((InstanceTreeItem) select.getFirstElement())
					.getContent();
			if (cepInstance.getInstance().equals(cepInstance)) {
				list.getListener().select(cepInstance);
			}
		}

	}

	/**
	 * This method is called to set the focus to this view.
	 */
	public void setFocus() {
		this.tabMenu.setFocus();
	}

	/**
	 * This method updates the info label with the current numbers of the
	 * inherited StateMachineInstances.
	 */
	public void setInfoData() {
		this.getSite().getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				String infotext = StringConst.INFO_ALL
						+ CEPListView.this.normalList.getItemCount()
						+ StringConst.WHITESPACE;
				if (CEPListView.this.normalList.getItemCount() >= IntConst.MAX_LIST_ENTRIES) {
					// if the list can not add more entries...
					infotext = infotext
							.concat(StringConst.INFO_MAXIMAL_ENTRIES);
				} else {
					infotext = infotext.concat(StringConst.WHITESPACE);
				}
				infotext = infotext.concat(StringConst.STATUS_RUNNING
						+ CEPListView.this.statusList.getNumberOfRunning()
						+ StringConst.WHITESPACE + StringConst.STATUS_FINISHED
						+ CEPListView.this.statusList.getNumberOfFinished()
						+ StringConst.WHITESPACE + StringConst.STATUS_ABORTED
						+ CEPListView.this.statusList.getNumberOfAborted());
				CEPListView.this.infoLabel.setText(infotext);
			}
		});
	}
	
	/**
	 * This is the getter for the event listener.
	 * 
	 * @return the event listener
	 */
	public CEPEventListener getListener() {
		return listener;
	}

	/**
	 * This is the getter for the NormalTreeList.
	 * 
	 * @return the NormalTreeList
	 */
	public NormalTreeList getNormalList() {
		return normalList;
	}

	/**
	 * This is the getter for the list of handled CepOperators.
	 * 
	 * @return the list of handled CepOperators
	 */
	public ArrayList<CepOperator<?, ?>> getOperators() {
		return operators;
	}

	/**
	 * This is the getter for the QueryTreeList.
	 * 
	 * @return the QueryTreeList
	 */
	public QueryTreeList getQueryList() {
		return queryList;
	}

	/**
	 * This is the getter for the StatusTreeList.
	 * 
	 * @return the StatusTreeList
	 */
	public StatusTreeList getStatusList() {
		return statusList;
	}

}
