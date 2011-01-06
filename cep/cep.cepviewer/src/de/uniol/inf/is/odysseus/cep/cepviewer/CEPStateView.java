package de.uniol.inf.is.odysseus.cep.cepviewer;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.cep.cepviewer.model.AbstractState;
import de.uniol.inf.is.odysseus.cep.cepviewer.model.AbstractTransition;
import de.uniol.inf.is.odysseus.cep.cepviewer.util.StringConst;
import de.uniol.inf.is.odysseus.cep.metamodel.Transition;

/**
 * This class defines the state view which should hold the information of the
 * selected state.
 * 
 * @author Christian
 */
public class CEPStateView extends ViewPart {

	// the widget which holds the informations
	private Table table;
	private String[] stateRowLabels;

	/**
	 * This is the constructor.
	 */
	public CEPStateView() {
		super();
	}

	/**
	 * This method creates the state view.
	 * 
	 * @param parent
	 *            is the widget which contains the state view.
	 */
	public void createPartControl(Composite parent) {
		this.table = new Table(parent, SWT.SINGLE | SWT.BORDER
				| SWT.HIDE_SELECTION);
		this.stateRowLabels = new String[] { StringConst.STATE_VIEW_ROW_A,
				StringConst.STATE_VIEW_ROW_B, StringConst.STATE_VIEW_ROW_C,
				StringConst.STATE_VIEW_ROW_D };
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		this.table.setLayoutData(data);
		for (int i = 0; i < 2; i++) {
			new TableColumn(this.table, SWT.NONE);
		}
		for (String row : this.stateRowLabels) {
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
	 * This method displays the information of the given state in the view.
	 * 
	 * @param state
	 *            is a state of an automata
	 */
	public void setContent(AbstractState state) {
		this.table.getItem(0).setText(state.getName());
		this.table.getItem(1).setText(
				Boolean.toString(state.getState().isAccepting()));
		this.table.getItem(2).setText(Boolean.toString(state.isActive()));
		List<Transition> list = state.getState().getTransitions();
		if (!list.isEmpty()) {
			Transition first = list.remove(0);
			this.table.getItem(3).setText(
					StringConst.STATE_VIEW_ROW_D_STATE + first.getNextState()
							+ StringConst.STATE_VIEW_ROW_D_CONDITION
							+ first.getCondition()
							+ StringConst.TRANSITION_LABEL_ACTION
							+ first.getAction());
			for (Transition next : list) {
				TableItem item = new TableItem(this.table, SWT.NONE);
				item.setText(
						1,
						"to: " + next.getNextState() + " if: "
								+ next.getCondition() + "/" + next.getAction());
			}
		}
		for (int i = 0; i < 2; i++) {
			this.table.getColumn(i).pack();
		}
	}

	/**
	 * This method displays the information of the given transition in the view.
	 * 
	 * @param transition
	 *            is a transition of an automata
	 * @deprecated: not used
	 */
	public void setContent(AbstractTransition transition) {

	}

}
