package de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.menu.command;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.viewer.stream.chart.charts.PieChart3D;

public class ShowPieChart3DCommand extends AbstractCommand {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IPhysicalOperator op = super.getSelectedOperator(event);
		if(op!=null){			
			openView(new PieChart3D(), op);
		}		
		return null;
	}

}
