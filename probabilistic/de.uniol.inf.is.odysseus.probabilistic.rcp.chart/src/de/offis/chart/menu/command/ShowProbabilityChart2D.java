package de.offis.chart.menu.command;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.offis.chart.charts.ProbabilityChart2D;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.menu.command.AbstractCommand;

public class ShowProbabilityChart2D extends AbstractCommand {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IPhysicalOperator op = super.getSelectedOperator(event);
		if(op!=null){			
			openView(new ProbabilityChart2D(), op);
		}
		return null;
	}
}
