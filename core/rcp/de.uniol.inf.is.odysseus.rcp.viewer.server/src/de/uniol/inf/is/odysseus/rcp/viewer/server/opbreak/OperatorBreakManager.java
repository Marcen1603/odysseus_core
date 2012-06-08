package de.uniol.inf.is.odysseus.rcp.viewer.server.opbreak;

import java.util.ArrayList;
import java.util.List;

public class OperatorBreakManager {

	private static OperatorBreakManager instance = null;
	
	private List<IOperatorBreakManagerListener> listeners = new ArrayList<IOperatorBreakManagerListener>();
	private List<OperatorBreak> breaks = new ArrayList<OperatorBreak>();
	
	private OperatorBreakManager() {}
	
	public static OperatorBreakManager getInstance() {
		if( instance == null ) 
			instance = new OperatorBreakManager();
		return instance;
	}
	
	public void add( OperatorBreak ob ) {
		if( ob == null ) return;
		
		synchronized( breaks ) {
			if( breaks.contains(ob)) return;
			breaks.add(ob);
		}
		fireAddEvent(ob);
	}
	
	public void remove( OperatorBreak ob ) {
		synchronized( breaks ) {
			breaks.remove(ob);
		}
		fireRemoveEvent(ob);
	}
	
	public List<OperatorBreak> getAll() {
		return breaks;
	}
	
	public void addListener( IOperatorBreakManagerListener listener ) {
		if( listener == null ) return;
		synchronized( listeners ) {
			listeners.add(listener);
		}
	}
	
	public void removeListener( IOperatorBreakManagerListener listener ) {
		synchronized( listeners ) {
			listeners.remove(listener);
		}
	}
	
	protected final void fireAddEvent( OperatorBreak ob) {
		synchronized( listeners ) {
			for( IOperatorBreakManagerListener listener : listeners )
				listener.operatorBreakAdded(this, ob);
		}
	}
	
	protected final void fireRemoveEvent( OperatorBreak ob) {
		synchronized( listeners ) {
			for( IOperatorBreakManagerListener listener : listeners )
				listener.operatorBreakRemoved(this, ob);
		}
	}
}
