package de.uniol.inf.is.odysseus.p2p_new.lb.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.p2p_new.lb.logicaloperator.FragmentAO;

/**
 * A {@link AbstractFragmentPO} can be used to realize a {@link FragmentAO}.
 * @author Michael Brand
 */
public abstract class AbstractFragmentPO<T extends IStreamObject<?>> 
		extends AbstractPipe<T, T> {
	
	/**
	 * The number of fragments.
	 */
	protected long numFragments;

	/**
	 * Constructs a new {@link AbstractFragmentPO}.
	 * @param fragmentAO the {@link FragmentAO} transformed to this {@link AbstractFragmentPO}.
	 */
	public AbstractFragmentPO(FragmentAO fragmentAO) {
		
		super();
		this.numFragments = fragmentAO.getNumberOfFragments();
		
	}

	/**
	 * Constructs a new {@link AbstractFragmentPO} as a copy of an existing one.
	 * @param fragmentPO The {@link AbstractFragmentPO} to be copied.
	 */
	public AbstractFragmentPO(AbstractFragmentPO<T> fragmentPO) {
		
		super(fragmentPO);
		this.numFragments = fragmentPO.numFragments;
		
	}
	
	@Override
	public abstract AbstractPipe<T, T> clone() ;

	@Override
	public OutputMode getOutputMode() {
		
		return OutputMode.INPUT;
		
	}
	
	@Override
	protected synchronized boolean isDone() {
		
		for(int port = 0; port < this.getInputPortCount(); port++) {
			
			if(!this.getSubscribedToSource(port).isDone())
				return false;
			
		}
		
		return true;
		
	}
	
	@Override
	protected synchronized void process_next(T object, int port) {
		
		this.transfer(object, this.route(object));

	}
	
	@Override
	public synchronized void processPunctuation(IPunctuation punctuation, int port) {
		
		this.sendPunctuation(punctuation, this.route(punctuation));
		
	}
	
	/**
	 * Routes an incoming object to the next output port.
	 * @param object The incoming {@link IStreamable} object.
	 * @return The output port to which <code>object</code> shall be transfered.
	 */
	protected abstract int route(IStreamable object);
	
}