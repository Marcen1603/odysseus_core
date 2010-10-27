package de.uniol.inf.is.odysseus.intervalapproach.window;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;

public class SlidingElementWindowTIPO<T extends IMetaAttributeContainer<ITimeInterval>> extends AbstractWindowTIPO<T> {

	List<T> _buffer = null;
	boolean forceElement = true;
	private long elemsToRemoveFromStream;
	
	public SlidingElementWindowTIPO(WindowAO ao) {
		super(ao);
		_buffer = new LinkedList<T>();
	}
	
	public SlidingElementWindowTIPO(SlidingElementWindowTIPO<T> po) {
		super(po);
		this._buffer = po._buffer;
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}
	
	@Override
	protected synchronized void process_next(T object, int port){		

		if (this.windowAO.isPartitioned()){
			throw new RuntimeException("Partioning not supported in this class");
		}else{
			if (elemsToRemoveFromStream > 0){
				elemsToRemoveFromStream--;
			}else{
				_buffer.add(object);
				processBuffer(_buffer, object);
			}
		}
	}

	protected synchronized void processBuffer(List<T> buffer, T object) {
		// Fall testen, dass der Strom zu Ende ist ...
		// Fenster hat die maximale Groesse erreicht
		if (buffer.size() == this.windowSize +1){
			// jetzt advance-Elemente rauswerfen
			Iterator<T> bufferIter = buffer.iterator();
			long elemsToSend = windowAdvance;
			// Problem: Fenster ist kleiner als Schrittlaenge -->
			// dann nur alle Elemente aus dem Fenster werfen
			// und Tupel solange verwerfen bis advance wieder erreicht
			if (windowSize < windowAdvance){
				elemsToSend = windowSize;
				elemsToRemoveFromStream = windowAdvance-windowSize;
			}
			for (int i=0;i<elemsToSend;i++){
				T toReturn = bufferIter.next();
				bufferIter.remove();
				toReturn.getMetadata().setEnd(object.getMetadata().getStart());
				transfer(toReturn);
			}
			if (elemsToRemoveFromStream > 0){
				elemsToRemoveFromStream--;
				// Geht, da noch genau 1 Element im Buffer ist!
				buffer.clear();
			}
		}
	}

	// TODO: Was tut man mit Element-Fenster, wenn der Strom zu Ende ist?
//	@Override
//	protected void process_done() {
//		// Alle noch im Buffer enthaltenen Elemente rausschreiben?
//		super.process_done();
//	}
	
	@Override
	public SlidingElementWindowTIPO<T> clone() {
		return new SlidingElementWindowTIPO<T>(this);
	}
	
	@Override
	public void process_open(){
	}
	
	@Override
	public void process_close(){
	}
	

}
