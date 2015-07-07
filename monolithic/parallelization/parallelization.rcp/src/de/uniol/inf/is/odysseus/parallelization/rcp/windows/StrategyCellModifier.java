package de.uniol.inf.is.odysseus.parallelization.rcp.windows;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.TableItem;

public class StrategyCellModifier implements ICellModifier {

	private Viewer viewer;

	public StrategyCellModifier(Viewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public boolean canModify(Object element, String property) {
		StrategySelectionRow row = null;
		if (element instanceof StrategySelectionRow) {
			row = (StrategySelectionRow) element;
		}
		if (row != null) {
			switch (property) {
			case StrategySelectionTableViewer.END_OPERATOR_ID:
				return true;
			case StrategySelectionTableViewer.DEGREES:
				return true;
			}
		}
		return false;
	}

	@Override
	public Object getValue(Object element, String property) {
		StrategySelectionRow row = null;
		if (element instanceof StrategySelectionRow) {
			row = (StrategySelectionRow) element;
		}
		if (row != null) {
			switch (property) {
			case StrategySelectionTableViewer.END_OPERATOR_ID:
				return row.getEndOperatorId();
			case StrategySelectionTableViewer.DEGREES:
				return row.getCustomDegrees();
			}
		}
		return "";
	}

	@Override
	public void modify(Object element, String property, Object value) {
		StrategySelectionRow row = null;
		if (element instanceof TableItem) {
			TableItem tableItem = (TableItem) element;
			if (tableItem.getData() instanceof StrategySelectionRow) {
				row = (StrategySelectionRow) tableItem.getData();
			}
		}
		if (row != null) {
			switch (property) {
			case StrategySelectionTableViewer.END_OPERATOR_ID:
				row.setEndOperatorId(String.valueOf(value));
				break;
			case StrategySelectionTableViewer.DEGREES:
				row.setCustomDegrees(String.valueOf(value));
				break;
			}
		}

		viewer.refresh();
	}

}
