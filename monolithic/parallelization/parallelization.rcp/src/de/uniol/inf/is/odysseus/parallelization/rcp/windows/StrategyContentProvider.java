package de.uniol.inf.is.odysseus.parallelization.rcp.windows;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class StrategyContentProvider implements IStructuredContentProvider {

	private List<StrategySelectionRow> strategySelectionRows;

	public StrategyContentProvider(List<StrategySelectionRow> strategySelectionRows){
		this.strategySelectionRows = strategySelectionRows;
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getElements(Object inputElement) {
		return this.strategySelectionRows.toArray();
	}
}