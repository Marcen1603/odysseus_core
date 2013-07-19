package de.uniol.inf.is.odysseus.p2p_new.distribute.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.p2p_new.distribute.logicaloperator.RRFragmentAO;

/**
 * A {@link RRFragmentPO} can be used to realize a {@link RRFragmentAO}. <br />
 * The {@link RRFragmentPO} uses a modulo n function to transfer {@link StreamObject}s to different output ports and can handle {@link IPunctuations}.
 * @author Michael Brand
 */
public class RRFragmentPO<T extends IStreamObject<?>> 
		extends AbstractPipe<T, T> {
	
	/**
	 * The number of fragments.
	 */
	private long numFragments;
	
	/**
	 * The counter for all incoming objects.
	 */
	private long objectCounter;

	/**
	 * Constructs a new {@link RRFragmentPO}.
	 * @param fragmentAO the {@link RRFragmentAO} transformed to this {@link RRFragmentPO}.
	 */
	public RRFragmentPO(RRFragmentAO fragmentAO) {
		
		super();
		this.numFragments = fragmentAO.getNumberOfFragments();
		this.objectCounter = 0;
		
	}

	/**
	 * Constructs a new {@link RRFragmentPO} as a copy of an existing one.
	 * @param fragmentPO The {@link RRFragmentPO} to be copied.
	 */
	public RRFragmentPO(RRFragmentPO<T> fragmentPO) {
		
		super(fragmentPO);
		this.numFragments = fragmentPO.numFragments;
		this.objectCounter = fragmentPO.objectCounter;
		
	}
	
	@Override
	public AbstractPipe<T, T> clone() {
		
		return new RRFragmentPO<T>(this);
		
	}

	@Override
	public OutputMode getOutputMode() {
		
		return OutputMode.INPUT;
		
	}
	
	@Override
	protected synchronized void process_open() throws OpenFailedException {
		
		this.objectCounter = 0;
		
	}
	
	@Override
	protected synchronized void process_close() {
		
		this.objectCounter = 0;
		
	}
	
	@Override
	protected synchronized void process_done() {
		
		if(this.isOpen())
			this.objectCounter = 0;
		
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
		
		int outputPort = (int) (this.objectCounter % this.numFragments);
		this.objectCounter++;
		this.transfer(object, outputPort);

	}
	
}