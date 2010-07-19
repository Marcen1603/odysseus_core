package de.uniol.inf.is.odysseus.rcp.editor.model.commands;

import org.eclipse.gef.commands.Command;

import de.uniol.inf.is.odysseus.rcp.editor.model.Operator;
import de.uniol.inf.is.odysseus.rcp.editor.model.OperatorConnection;

public class ConnectionCreateCommand extends Command {

	private final Operator source;
	private Operator target = null;
	private OperatorConnection connection = null;
	
	public ConnectionCreateCommand( Operator src ) {
		source = src;
	}
	
	@Override
	public boolean canExecute() {
		if( source.equals(target)) 
			return false;
		
		for (OperatorConnection connection : source.getConnectionsAsSource()) {
			if (connection.getTarget().equals(target)) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public void execute() {
		connection = new OperatorConnection();
		connection.setSource(source);
		connection.setTarget(target);
		source.addConnection(connection);
		target.addConnection(connection);
	}
	
	@Override
	public void undo() {
		target.removeConnection(connection);
		source.removeConnection(connection);
		connection.setSource( null );
		connection.setTarget( null );
	}
	
	@Override
	public void redo() {
		connection.setSource(source);
		connection.setTarget(target);
		target.addConnection(connection);
		source.addConnection(connection);
		super.redo();
	}
	
	public void setTarget( Operator tgt ) {
		target = tgt;
	}
}
