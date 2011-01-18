package de.uniol.inf.is.odysseus.cep.cepviewer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.cep.cepviewer.automatamodel.AbstractState;
import de.uniol.inf.is.odysseus.cep.cepviewer.automatamodel.AbstractTransition;
import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;
import de.uniol.inf.is.odysseus.cep.metamodel.Transition;

/**
 * This class defines the state view which should hold the information of the
 * selected state.
 * 
 * @author Christian
 */
public class CEPStateView extends ViewPart {

	// the ID of this view
	public static final String ID = "de.uniol.inf.is.odysseus.cep.cepviewer.stateview";
	// the row labels
	private String[] rowLabels;
	// holds the currently shown state
	private AbstractState state;
	// the widget which holds the informations
	private Table table;

	/**
	 * This is the constructor.
	 */
	public CEPStateView() {
		super();
		// create the Strings for the row labels
		this.rowLabels = new String[] { StringConst.STATE_VIEW_ROW_A,
				StringConst.STATE_VIEW_ROW_B, StringConst.STATE_VIEW_ROW_C,
				StringConst.STATE_VIEW_ROW_D };
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
		// resize the columns
		for (int i = 0; i < this.table.getColumnCount(); i++) {
			this.table.getColumn(i).pack();
		}
	}

	/**
	 * This method creates the CEPStateView by creating the Table widget and
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
	 * This method creates an array containing all Transitions of a state as
	 * String representation.
	 * 
	 * @param state
	 *            is the selected State of the diagram
	 * @param transList
	 *            is the list of all transitions in the diagram
	 * @return an array with the representations of all transitions
	 */
	private String[] createTransList(AbstractState state,
			ArrayList<AbstractTransition> transList) {
		this.state = state;
		List<String> output = new ArrayList<String>();
		for (Transition transition : state.getState().getTransitions()) {
			for (AbstractTransition trans : transList) {
				if (transition.equals(trans.getTransition())) {
					// get the condition and action of the transition
					output.add(StringConst.STATE_VIEW_ROW_D_Label
							.replaceFirst(StringConst.WILDCARD,
									trans.getNextState().getName())
							.replaceFirst(StringConst.WILDCARD,
									transition.getCondition().toString())
							.replaceFirst(StringConst.WILDCARD,
									transition.getAction().toString()));
					break;
				}
			}
		}
		return output.toArray(new String[output.size()]);
	}

	/**
	 * This method displays the information of the given state in the view.
	 * 
	 * @param state
	 *            is a state of an automata
	 * @param transitions
	 *            is a list with all transitions in the diagram
	 */
	public void setContent(AbstractState state,
			ArrayList<AbstractTransition> transList) {
		this.clear();
		// create an array with all informations
		final String[] newContent = { state.getName(),
				Boolean.toString(state.getState().isAccepting()),
				Boolean.toString(state.isActive()) };
		final String[] transContent = this.createTransList(state, transList);
		this.getSite().getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				// set the state information
				for (int i = 0; i < newContent.length; i++) {
					table.getItem(i).setText(1, newContent[i]);
				}
				// set the informations for all transitions of this state
				if (transContent[0].isEmpty()) {
					table.getItem(3).setText(1, StringConst.STATE_VIEW_NONE);
				}
				table.getItem(3).setText(1, transContent[0]);
				for (int i = 1; i < transContent.length; i++) {
					TableItem newItem = new TableItem(CEPStateView.this.table,
							SWT.NONE);
					newItem.setText(1, transContent[i]);
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

	/**
	 * This method is used to update the entry for the current state of the
	 * shown instance.
	 */
	public void update() {
		if (this.state != null) {
			table.getItem(2)
					.setText(1, Boolean.toString(this.state.isActive()));
		}
	}

}
