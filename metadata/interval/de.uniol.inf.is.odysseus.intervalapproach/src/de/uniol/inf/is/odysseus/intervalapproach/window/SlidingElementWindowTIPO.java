package de.uniol.inf.is.odysseus.intervalapproach.window;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.logicaloperator.base.WindowAO;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;

public class SlidingElementWindowTIPO<T extends IMetaAttribute<ITimeInterval>> extends AbstractWindowTIPO<T> {

	List<T> buffer = null;
	boolean forceElement = true;
	
	public SlidingElementWindowTIPO(WindowAO ao) {
		super(ao);
		buffer = new LinkedList<T>();
	}
	
	public SlidingElementWindowTIPO(SlidingElementWindowTIPO<T> po) {
		super(po);
		this.buffer = po.buffer;
	}

	@Override
	protected synchronized void process_next(T object, int port, boolean isReadOnly){		
		if(isReadOnly){
			object = (T)object.clone();
		}
		buffer.add(object);

		// Fall testen, dass der Strom zu Ende ist ...
		if (buffer.size() == this.windowSize +1){
			T toReturn = buffer.remove(0);
			toReturn.getMetadata().setEnd(object.getMetadata().getStart());
			this.transfer(toReturn);
		}
	}
	
	@Override
	public SlidingElementWindowTIPO clone() {
		return new SlidingElementWindowTIPO<T>(this);
	}
	
	public void process_open(){
	}
	
	public void process_close(){
	}
	
}
