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
	 * @param transitions
	 */
	public void setContent(AbstractState state,
			ArrayList<AbstractTransition> transList) {
		this.clear();
		final String[] newContent = { state.getName(),
				Boolean.toString(state.getState().isAccepting()),
				Boolean.toString(state.isActive()) };
		final String[] transContent = this.createTransList(state, transList);
		this.getSite().getShell().getDisplay().asyncExec(new Runnable() {
			public void run() {
				for (int i = 0; i < newContent.length; i++) {
					table.getItem(i).setText(1, newContent[i]);
				}
				table.getItem(2).setText(1, transContent[0]);
				for (int i = 1; i < transContent.length; i++) {
					TableItem newItem = new TableItem(CEPStateView.this.table,
							SWT.NONE);
					newItem.setText(1, transContent[i]);
				}
				for (int i = 0; i < table.getColumnCount(); i++) {
					table.getColumn(i).pack();
				}
			}
		});

	}

	private String[] createTransList(AbstractState state,
			ArrayList<AbstractTransition> transList) {
		List<String> output = new ArrayList<String>();
		for (Transition transition : state.getState().getTransitions()) {
			for (AbstractTransition trans : transList) {
				if (transition.equals(trans.getTransition())) {
					output.add(StringConst.STATE_VIEW_ROW_D_STATE
							+ trans.getNextState().getName()
							+ StringConst.STATE_VIEW_ROW_D_CONDITION
							+ transition.getCondition()
							+ StringConst.TRANSITION_LABEL_ACTION
							+ transition.getAction());
					break;
				}
			}
		}
		return output.toArray(new String[output.size()]);
	}

	public void clear() {
		this.table.removeAll();
		for (String row : this.stateRowLabels) {
			TableItem item = new TableItem(this.table, SWT.NONE);
			item.setText(0, row);
		}
		for (int i = 0; i < 2; i++) {
			this.table.getColumn(i).pack();
		}
	}

}
