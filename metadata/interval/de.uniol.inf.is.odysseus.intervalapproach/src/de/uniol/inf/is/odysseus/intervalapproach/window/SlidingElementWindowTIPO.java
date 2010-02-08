package de.uniol.inf.is.odysseus.intervalapproach.window;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

public class SlidingElementWindowTIPO<T extends IMetaAttributeContainer<ITimeInterval>> extends AbstractWindowTIPO<T> {

	List<T> _buffer = null;
	boolean forceElement = true;
	
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
			_buffer.add(object);
			processBuffer(_buffer, object);
		}
	}

	protected void processBuffer(List<T> buffer, T object) {
		// TODO: Advance wird nicht berücksichtigt!
		// Fall testen, dass der Strom zu Ende ist ...
		if (buffer.size() == this.windowSize +1){
			T toReturn = buffer.remove(0);
			toReturn.getMetadata().setEnd(object.getMetadata().getStart());
			this.transfer(toReturn);
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
	
	public void process_close(){
	}
	

}
