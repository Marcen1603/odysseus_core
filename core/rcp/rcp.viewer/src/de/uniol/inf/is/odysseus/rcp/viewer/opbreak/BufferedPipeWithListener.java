package de.uniol.inf.is.odysseus.rcp.viewer.opbreak;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.BufferedPipe;

public class BufferedPipeWithListener<T extends IClone> extends BufferedPipe<T> {

	private final List<IBufferedPipeListener> listeners = new ArrayList<IBufferedPipeListener>();
	
	@Override
	protected void process_next(T object, int port) {
		super.process_next(object, port);
		fireSizeEvent();
	}
	
	@Override
	public void transferNext() {
		super.transferNext();
		fireSizeEvent();
	}
	
	public void addListener( IBufferedPipeListener listener ) {
		if( listener == null ) return;
		synchronized( listeners ) {
			listeners.add(listener);
		}
	}
	
	public void removeListener( IBufferedPipeListener listener ) {
		synchronized( listeners ) {
			listeners.remove(listener);
		}
	}
	
	protected final void fireSizeEvent() {
		synchronized( listeners ){
			for( IBufferedPipeListener listener : listeners ) 
				listener.sizeChanged(this);
		}
	}

}
