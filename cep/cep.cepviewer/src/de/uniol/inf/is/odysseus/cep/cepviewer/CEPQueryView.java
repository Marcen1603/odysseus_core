package de.uniol.inf.is.odysseus.cep.cepviewer;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.cep.cepviewer.automatamodel.AbstractState;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.CEPInstance;
import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;

/**
 * This class defines the query view which should hold the information of the
 * selected query.
 * 
 * @author Christian
 */
public class CEPQueryView extends ViewPart {

	// the ID of this view
	public static final String ID = "de.uniol.inf.is.odysseus.cep.cepviewer.queryview";

	// the widget which holds the informations
	private Table table;
	// the row labels
	private String[] rowLabels;

	/**
	 * This is the constructor.
	 */
	public CEPQueryView() {
		super();
		// create the Strings for the row labels
		this.rowLabels = new String[] { StringConst.QUERY_VIEW_ROW_A,
				StringConst.QUERY_VIEW_ROW_B, StringConst.QUERY_VIEW_ROW_C,
				StringConst.QUERY_VIEW_ROW_D, StringConst.QUERY_VIEW_ROW_E,
				StringConst.QUERY_VIEW_ROW_F, StringConst.QUERY_VIEW_ROW_G,
				StringConst.QUERY_VIEW_ROW_H };
	}

	/**
	 * This method clears the view.
	 */
	public void clear() {
		this.table.removeAll();
		for (String row : this.rowLabels) {
			TableItem item = new TableItem(this.table, SWT.NONE);
			item.setText(0, row);
		}
		for (int i = 0; i < this.table.getColumnCount(); i++) {
			this.table.getColumn(i).pack();
		}
	}

	/**
	 * This method creates the CEPQueryView by creating the Table widget and
	 * setting it's labels.
	 * 
	 * @param parent
	 *            is the widget which contains the query view.
	 */
	public void createPartControl(Composite parent) {
		// create the widget and set the layout
		this.table = new Table(parent, SWT.SINGLE | SWT.BORDER
				| SWT.HIDE_SELECTION);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		this.table.setLayoutData(data);
		// create the columns
		for (int i = 0; i < 2; i++) {
			new TableColumn(this.table, SWT.NONE);
		}
		// create the rows and set the labels
		for (String row : this.rowLabels) {
			TableItem item = new TableItem(this.table, SWT.NONE);
			item.setText(0, row);
		}
		// resize the columns
		for (int i = 0; i < this.table.getColumnCount(); i++) {
			this.table.getColumn(i).pack();
		}
	}

	/**
	 * This method creates a String of all states in a list of states, divided
	 * by a comma.
	 * 
	 * @param stateList
	 * @return
	 */
	private String createStateLabel(ArrayList<AbstractState> stateList) {
		String output = "";
		for (AbstractState state : stateList) {
			output = output.concat(state.getName()).concat(
					StringConst.SEPERATOR);
		}
		return output.substring(0, output.length() - 1);
	}

	/**
	 * This method displays the information of the given instance in the view.
	 * 
	 * @param instance
	 *            is a StateMachineInstance
	 */
	public void setContent(CEPInstance instance) {
		// create an array with all informations
		final String[] newContent = {
				Integer.toString(instance.getStateMachine().hashCode()),
				Integer.toString(instance.getInstance().hashCode()),
				this.createStateLabel(instance.getStateList()),
				instance.getStateList().get(0).getName(),
				this.createStateLabel(instance.getFinalStateList()),
				Long.toString(instance.getStateMachine().getWindowSize()),
				instance.getCurrentState().getName(),
				Long.toString(instance.getStartTimestamp()) };
		this.getSite().getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				// set the information
				for (int i = 0; i < newContent.length; i++) {
					table.getItem(i).setText(1, newContent[i]);
				}
				// resize the columns
				for (int i = 0; i < table.getColumnCount(); i++) {
					table.getColumn(i).pack();
				}
			}
		});
	}

	/**
	 * This method is called to set the focus to this view.
	 */
	public void setFocus() {
		// do nothing
	}

}
