package de.uniol.inf.is.odysseus.p2p_new.lb.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.p2p_new.lb.logicaloperator.FragmentAO;

/**
 * A {@link HashFragmentPO} can be used to realize a {@link FragmentAO}. <br />
 * The {@link HashFragmentPO} uses {@link IStreamObject#hashCode()} and  a modulo n function to transfer 
 * {@link StreamObject}s to different output ports and can handle {@link IPunctuations}.
 * @author Michael Brand
 */
public class HashFragmentPO<T extends IStreamObject<? extends ITimeInterval>> 
		extends AbstractFragmentPO<T> {
	
	/**
	 * Constructs a new {@link HashFragmentPO}.
	 * @param fragmentAO the {@link FragmentAO} transformed to this {@link HashFragmentPO}.
	 */
	public HashFragmentPO(FragmentAO fragmentAO) {
		
		super(fragmentAO);
		
	}

	/**
	 * Constructs a new {@link HashFragmentPO} as a copy of an existing one.
	 * @param fragmentPO The {@link HashFragmentPO} to be copied.
	 */
	public HashFragmentPO(HashFragmentPO<T> fragmentPO) {
		
		super(fragmentPO);
		
	}
	
	@Override
	public AbstractPipe<T, T> clone() {
		
		return new HashFragmentPO<T>(this);
		
	}
	
	@Override
	protected int route(IStreamable object) {
		
		return (int) (Math.abs(object.hashCode()) % this.numFragments);
		
	}
	
}