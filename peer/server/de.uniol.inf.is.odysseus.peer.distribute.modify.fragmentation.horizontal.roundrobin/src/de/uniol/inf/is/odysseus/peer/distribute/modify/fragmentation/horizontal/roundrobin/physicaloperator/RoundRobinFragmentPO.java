package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.roundrobin.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.physicaloperator.AbstractFragmentPO;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.roundrobin.logicaloperator.RoundRobinFragmentAO;

/**
 * A {@link RoundRobinFragmentPO} can be used to realize a {@link RoundRobinFragmentAO}. <br />
 * The {@link RoundRobinFragmentPO} uses a modulo n function to transfer {@link StreamObject}s to different output ports and can handle {@link IPunctuations}.
 * @author Michael Brand
 */
public class RoundRobinFragmentPO<T extends IStreamObject<IMetaAttribute>> 
		extends AbstractFragmentPO<T> {
	
	/**
	 * The counter for all incoming objects.
	 */
	private long objectCounter;

	/**
	 * Constructs a new {@link RoundRobinFragmentPO}.
	 * @param fragmentAO the {@link RoundRobinFragmentAO} transformed to this {@link RoundRobinFragmentPO}.
	 */
	public RoundRobinFragmentPO(RoundRobinFragmentAO fragmentAO) {
		
		super(fragmentAO);
		this.objectCounter = 0;
		
	}

	/**
	 * Constructs a new {@link RoundRobinFragmentPO} as a copy of an existing one.
	 * @param fragmentPO The {@link RoundRobinFragmentPO} to be copied.
	 */
	public RoundRobinFragmentPO(RoundRobinFragmentPO<T> fragmentPO) {
		
		super(fragmentPO);
		this.objectCounter = fragmentPO.objectCounter;
		
	}
	
	@Override
	public AbstractPipe<T, T> clone() {
		
		return new RoundRobinFragmentPO<T>(this);
		
	}
	
	@Override
	protected int route(IStreamable object) {
		
		int outputPort = (int) (this.objectCounter % this.numFragments);
		this.objectCounter++;
		return outputPort;
		
	}
	
}