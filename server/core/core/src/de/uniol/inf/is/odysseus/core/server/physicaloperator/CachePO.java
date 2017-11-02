package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.util.LinkedList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractPhysicalSubscription;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;

public class CachePO<R extends IStreamObject<? extends IMetaAttribute>> extends AbstractPipe<R, R> {

	private final List<IStreamable> cache = new LinkedList<>();
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

	@SuppressWarnings("unchecked")
	@Override
	protected void newReceiver(AbstractPhysicalSubscription<?, ISink<IStreamObject<?>>> sink){
		synchronized(cache){
			for (IStreamable elem : cache) {
				if (elem.isPunctuation()) {
					sendPunctuation((IPunctuation) elem, 0, sink);
				}else {
					transfer((R)elem,0,sink);
				}
			}
		}
	}
	
	@Override
	protected void process_next(R object, int port) {
		synchronized(cache){
			// add to buffer
			cache.add(object);
			cacheInvalidation(cache, object);
			// deliver to all connected;
			transfer(object);
		}
	}

	protected void cacheInvalidation(List<IStreamable> cache, R object) {
		if (maxSize > 0 && cache.size() > maxSize){
			cache.remove(0);
		}
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		synchronized(cache){
			cache.add(punctuation);
		}
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
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof CachePO)){
			return false;
		} else {
			return ((CachePO<R>)ipo).maxSize == this.maxSize;
		}
	}

}
