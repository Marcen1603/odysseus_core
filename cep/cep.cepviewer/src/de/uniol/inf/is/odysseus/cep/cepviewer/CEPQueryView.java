package de.uniol.inf.is.odysseus.cep.cepviewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.cep.cepviewer.automata.AbstractState;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.CEPInstance;
import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;

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
	private CEPInstance instance;

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
		this.rowLabels = new String[] { StringConst.QUERY_VIEW_ROW_A,
				StringConst.QUERY_VIEW_ROW_B, StringConst.QUERY_VIEW_ROW_C,
				StringConst.QUERY_VIEW_ROW_D, StringConst.QUERY_VIEW_ROW_E,
				StringConst.QUERY_VIEW_ROW_F, StringConst.QUERY_VIEW_ROW_G,
				StringConst.QUERY_VIEW_ROW_H };
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		this.table.setLayoutData(data);
		for (int i = 0; i < 2; i++) {
			new TableColumn(this.table, SWT.NONE);
		}
		for (String row : this.rowLabels) {
			TableItem item = new TableItem(this.table, SWT.NONE);
			item.setText(0, row);
		}
		for (int i = 0; i < 2; i++) {
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
	public void setContent(CEPInstance instance) {
		this.clear();
		this.instance = instance;
		this.getSite().getShell().getDisplay().syncExec(new Runnable() {
			public void run() {
				String string = new String();
				CEPQueryView.this.table.getItem(0).setText(
						1,
						Integer.toString(CEPQueryView.this.instance
								.getStateMachine().hashCode()));
				CEPQueryView.this.table.getItem(1)
						.setText(
								1,
								Integer.toString(CEPQueryView.this.instance
										.hashCode()));
				for (AbstractState state : CEPQueryView.this.instance
						.getStateList()) {
					string += state.getName() + " ";
				}
				CEPQueryView.this.table.getItem(2).setText(1, string);
				CEPQueryView.this.table.getItem(3).setText(1,
						CEPQueryView.this.instance.getCurrentState().getName());
				string = new String();
				for (AbstractState state : CEPQueryView.this.instance
						.getFinalStateList()) {
					string += state.getName() + " ";
				}
				CEPQueryView.this.table.getItem(4).setText(1, string);
				CEPQueryView.this.table.getItem(5).setText(
						1,
						Long.toString(CEPQueryView.this.instance
								.getStateMachine().getWindowSize()));
				CEPQueryView.this.table.getItem(6).setText(1,
						CEPQueryView.this.instance.getCurrentState().getName());
				CEPQueryView.this.table.getItem(7).setText(
						1,
						Long.toString(CEPQueryView.this.instance
								.getStartTimestamp()));
				for (int i = 0; i < 2; i++) {
					CEPQueryView.this.table.getColumn(i).pack();
				}
			}
		});
	}

	public void clear() {
		this.table.removeAll();
		for (String row : this.rowLabels) {
			TableItem item = new TableItem(this.table, SWT.NONE);
			item.setText(0, row);
		}
		for (int i = 0; i < 2; i++) {
			this.table.getColumn(i).pack();
		}
	}
}
