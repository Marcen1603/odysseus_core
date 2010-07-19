package de.uniol.inf.is.odysseus.rcp.editor.model.commands;

import java.util.List;

import org.eclipse.gef.commands.Command;

import de.uniol.inf.is.odysseus.rcp.editor.model.Operator;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorConnection;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorPlan;

public class OperatorDeleteCommand extends Command {

	private Operator operator;
	private OperatorPlan plan;
	
	public OperatorDeleteCommand( OperatorPlan plan,  Operator op ) {
		operator = op;
		this.plan = plan;
	}
	
	@Override
	public void execute() {
		
		// Verbindungen entfernen
		List<OperatorConnection> connectionsAsSource = operator.getConnectionsAsSource();
		List<OperatorConnection> connectionsAsTarget = operator.getConnectionsAsTarget();
		for( OperatorConnection conn : connectionsAsSource ) {
			Operator op = conn.getTarget();
			op.removeConnection(conn);
		}
		for( OperatorConnection conn : connectionsAsTarget ) {
			Operator op = conn.getSource();
			op.removeConnection(conn);
		}

		plan.removeOperator(operator);
	}
	
	@Override
	public void undo() {
		// Verbindungen entfernen
		List<OperatorConnection> connectionsAsSource = operator.getConnectionsAsSource();
		List<OperatorConnection> connectionsAsTarget = operator.getConnectionsAsTarget();
		for( OperatorConnection conn : connectionsAsSource ) {
			Operator op = conn.getTarget();
			op.addConnection(conn);
		}
		for( OperatorConnection conn : connectionsAsTarget ) {
			Operator op = conn.getSource();
			op.addConnection(conn);
		}

		plan.addOperator(operator);
		
	}
	
	@Override
	public void redo() {
		execute();
	}
}
