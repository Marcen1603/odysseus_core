package de.uniol.inf.is.odysseus.rcp.viewer.opbreak;

public interface IOperatorBreakManagerListener {

	public void operatorBreakAdded( OperatorBreakManager manager, OperatorBreak ob );
	public void operatorBreakRemoved( OperatorBreakManager manager, OperatorBreak ob );
	
}
