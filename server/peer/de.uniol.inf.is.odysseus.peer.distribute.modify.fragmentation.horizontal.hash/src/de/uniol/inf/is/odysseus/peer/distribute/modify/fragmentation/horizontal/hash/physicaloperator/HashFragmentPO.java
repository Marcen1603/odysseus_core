package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.hash.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.hash.logicaloperator.HashFragmentAO;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.physicaloperator.AbstractFragmentPO;

/**
 * A {@link HashFragmentPO} can be used to realize a {@link HashFragmentAO}. <br />
 * The {@link HashFragmentPO} uses a hash modulo n function to transfer {@link StreamObject}s to different output ports and can handle {@link IPunctuations}.
 * @author Michael Brand
 */
public class HashFragmentPO<T extends IStreamObject<IMetaAttribute>> 
		extends AbstractFragmentPO<T> {
	
	/**
	 * The counter for all incoming objects.
	 */
	private long objectCounter;

	/**
	 * Constructs a new {@link HashFragmentPO}.
	 * @param fragmentAO the {@link HashFragmentAO} transformed to this {@link HashFragmentPO}.
	 */
	public HashFragmentPO(HashFragmentAO fragmentAO) {
		
		super(fragmentAO);
		this.objectCounter = 0;
		
	}

	/**
	 * Constructs a new {@link HashFragmentPO} as a copy of an existing one.
	 * @param fragmentPO The {@link HashFragmentPO} to be copied.
	 */
	public HashFragmentPO(HashFragmentPO<T> fragmentPO) {
		
		super(fragmentPO);
		this.objectCounter = fragmentPO.objectCounter;
		
	}
	
	@Override
	public AbstractPipe<T, T> clone() {
		
		return new HashFragmentPO<T>(this);
		
	}
	
	@Override
	protected int route(IStreamable object) {
		
		int outputPort = (int) (this.objectCounter % this.numFragments);
		this.objectCounter++;
		return outputPort;
		
	}
	
}