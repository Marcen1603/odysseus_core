package de.uniol.inf.is.odysseus.rcp.editor.model.commands;

import org.eclipse.gef.commands.Command;

import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorConnection;

public class ConnectionDeleteCommand extends Command {

	private OperatorConnection connection;
	
	public ConnectionDeleteCommand( OperatorConnection connection ) {
		this.connection = connection;
	}
	
	@Override
	public void execute() {
		connection.getSource().removeConnection(connection);
		connection.getTarget().removeConnection(connection);
	}
	
	@Override
	public void redo() {
		execute();
	}
	
	@Override
	public void undo() {
		connection.getSource().addConnection(connection);
		connection.getTarget().addConnection(connection);
	}
}
