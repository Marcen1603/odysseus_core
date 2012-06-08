package de.uniol.inf.is.odysseus.rcp.viewer.server.opbreak;

public interface IOperatorBreakListener {

	public void breakStarted( OperatorBreak ob );
	public void breakStopped( OperatorBreak ob );
	public void sizeChanged( OperatorBreak ob );
}
