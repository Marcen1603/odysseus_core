package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;

public class CachePO<R extends IStreamObject<IMetaAttribute>> extends AbstractPipe<R, R> {

	private final List<R> cache = new LinkedList<>();
	private final long maxSize;
	
	public CachePO(long maxSize){
		this.maxSize = maxSize;
	}
	
	public CachePO(CachePO<R> cachePO) {
		super(cachePO);
		this.maxSize = cachePO.maxSize;
	}

	@Override
	public de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	protected void newReceiver(AbstractPhysicalSubscription<ISink<? super R>> sink){
		synchronized(cache){
			ListIterator<R> iter = cache.listIterator();
			while (iter.hasNext()){
				transfer(iter.next(), 0, sink);
			}
		}
	}
	
	@Override
	protected void process_next(R object, int port) {
		synchronized(cache){
			// add to buffer
			cache.add(object);
			if (maxSize > 0 && cache.size() > maxSize){
				cache.remove(0);
			}
			// deliver to all connected;
			transfer(object);
		}
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// TODO: What to do with punctuations
	}

	
	@Override
	protected void process_close() {
		super.process_close();
		clearCache();
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		clearCache();
	}

	private void clearCache() {
		synchronized( cache ) {
			cache.clear();
		}
	}

	@Override
	public AbstractPipe<R, R> clone() {
		return new CachePO<R>(this);
	}
	
	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof CachePO)){
			return false;
		} else {
			return true;
		}
	}

}
