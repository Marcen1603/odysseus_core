package de.uniol.inf.is.odysseus.cep.cepviewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;
import de.uniol.inf.is.odysseus.cep.epa.StateMachineInstance;
import de.uniol.inf.is.odysseus.cep.metamodel.State;

/**
 * This class defines the query view which should hold the information of the
 * selected query.
 * 
 * @author Christian
 */
public class CEPQueryView extends ViewPart {

	// the widget which holds the informations
	private Table table;
	private String[] rowLabels;

	/**
	 * This is the constructor.
	 */
	public CEPQueryView() {
		super();
	}

	/**
	 * This method creates the query view.
	 * 
	 * @param parent
	 *            is the widget which contains the query view.
	 */
	public void createPartControl(Composite parent) {
		this.table = new Table(parent, SWT.SINGLE | SWT.BORDER
				| SWT.HIDE_SELECTION);
		this.rowLabels = new String[] { StringConst.QUERY_VIEW_ROW_A, StringConst.QUERY_VIEW_ROW_B, StringConst.QUERY_VIEW_ROW_C, StringConst.QUERY_VIEW_ROW_D, StringConst.QUERY_VIEW_ROW_E, StringConst.QUERY_VIEW_ROW_F, StringConst.QUERY_VIEW_ROW_G, StringConst.QUERY_VIEW_ROW_H};
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		this.table.setLayoutData(data);
		for(int i = 0; i < 2; i++) {
			new TableColumn(this.table, SWT.NONE);
		}
		for (String row : this.rowLabels) {
			TableItem item = new TableItem(this.table, SWT.NONE);
			item.setText(0, row);
		}
		for(int i = 0; i < 2; i++) {
			this.table.getColumn(i).pack();
		}
	}

	/**
	 * This method is called to set the focus to this view.
	 */
	public void setFocus() {
	}

	/**
	 * This method displays the information of the given instance in the view.
	 * 
	 * @param instance
	 *            is a StateMachineInstance
	 */
	public void setContent(StateMachineInstance<?> instance) {
		String string = new String();
		this.table.getItem(0).setText(
				Integer.toString(instance.getStateMachine().hashCode()));
		this.table.getItem(1).setText(Integer.toString(instance.hashCode()));
		for (State next : instance.getStateMachine().getStates()) {
			string += StringConst.STATE_LABEL_PREFIX + next.getId();
		}
		this.table.getItem(2).setText(string);
		this.table.getItem(3).setText(
				StringConst.STATE_LABEL_PREFIX
						+ instance.getStateMachine().getInitialState().getId());
		for (State next : instance.getStateMachine().getFinalStates()) {
			string += StringConst.STATE_LABEL_PREFIX + next.getId();
		}
		this.table.getItem(4).setText(string);
		this.table.getItem(5).setText(
				Long.toString(instance.getStateMachine().getWindowSize()));
		this.table.getItem(6).setText(
				StringConst.STATE_LABEL_PREFIX + instance.getCurrentState());
		this.table.getItem(7).setText(
				Long.toString(instance.getStartTimestamp()));
		for (int i = 0; i < 2; i++) {
			this.table.getColumn(i).pack();
		}
	}
}
