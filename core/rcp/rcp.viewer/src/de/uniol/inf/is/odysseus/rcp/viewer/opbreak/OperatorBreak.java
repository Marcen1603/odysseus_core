package de.uniol.inf.is.odysseus.rcp.viewer.opbreak;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.physicaloperator.ISource;

public class OperatorBreak implements IBufferedPipeListener {

	private ISource<?> operator;
	private BufferedPipeWithListener<?> buffer;
	private List<IOperatorBreakListener> listeners = new ArrayList<IOperatorBreakListener>();
	private boolean isBreaked = false;
	
	public OperatorBreak( ISource<?> source ) {
		operator = source;
	}
	
	public boolean startBreak() {
		if( isBreaked ) return true;
		
		final int subCount = operator.getSubscriptions().size();

		if (subCount == 1) {

			// TODO: Buffer erstellen (BufferedPipe verwenden)
			// Buffer einklinken
			// Listener in Buffer eintragen
			
			// Wenn die OperatorBreakView noch nicht angezeigt wird, danach 
			// fragen und ggfs. anzeigen lassen
			
			
			isBreaked = true;
			fireBreakStartEvent();
			
			System.out.println(this + " : Start Break");
			
			return true;

		} else {
			// Fehlermeldung, dass der Operator
			// genau eine Senke haben muss
			System.out.println("Operator " + operator + " must have 1 sinks, but have " + subCount);
			return false;
		}
	}
	
	public void endBreak() {
		if( !isBreaked ) return;
		
		// TODO: Buffer entfernen
		
		isBreaked = false;
		fireBreakEndEvent();
		
		System.out.println(this + " : End Break");
	}
	
	public void step() {
		if( !isBreaked ) 
			return;
		
		// Hier genau ein Element
		// des Buffers weiterschicken
		System.out.println(this + " : Step");
	}
	
	public final ISource<?> getOperator() {
		return operator;
	}
	
	public final BufferedPipeWithListener<?> getBuffer() {
		return buffer;
	}
	
	public void addListener( IOperatorBreakListener listener ) {
		if( listener == null ) return;
		synchronized( listeners ) {
			listeners.add(listener);
		}
	}
	
	public void removeListener( IOperatorBreakListener listener ) {
		synchronized( listeners ) {
			listeners.remove(listener);
		}
	}
	
	protected final void fireBreakStartEvent() {
		synchronized( listeners ) {
			for( IOperatorBreakListener listener : listeners ) {
				listener.breakStarted(this);
			}
		}
	}
	
	protected final void fireBreakEndEvent() {
		synchronized( listeners ) {
			for( IOperatorBreakListener listener : listeners ) {
				listener.breakStopped(this);
			}
		}
	}
	
	protected final void fireSizeEvent() {
		synchronized( listeners ) {
			for( IOperatorBreakListener listener : listeners ) {
				listener.sizeChanged(this);
			}
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[OperatorBreak ").append(operator).append("]");
		return sb.toString();
	}

	@Override
	public void sizeChanged(BufferedPipeWithListener<?> buffer) {
		fireSizeEvent();
	}
	
	public boolean isBreaked() {
		return isBreaked;
	}
}
