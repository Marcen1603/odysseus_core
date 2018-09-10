package de.uniol.inf.is.odysseus.server.fragmentation.horizontal.physicaloperator;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.RoundRobinFragmentAO;

/**
 * A {@link RoundRobinFragmentPO} can be used to realize a {@link RoundRobinFragmentAO}. <br />
 * The {@link RoundRobinFragmentPO} uses a modulo n function to transfer {@link StreamObject}s to different output ports and can handle {@link IPunctuations}.
 * @author Michael Brand
 * @author Marco Grawunder 
 */
public class RoundRobinFragmentPO<T extends IStreamObject<IMetaAttribute>> 
		extends AbstractStaticFragmentPO<T> {
	
	/**
	 * The counter for all incoming objects.
	 */
	private int nextPort;

	/**
	 * Constructs a new {@link RoundRobinFragmentPO}.
	 * @param fragmentAO the {@link RoundRobinFragmentAO} transformed to this {@link RoundRobinFragmentPO}.
	 */
	public RoundRobinFragmentPO(RoundRobinFragmentAO fragmentAO) {
		
		super(fragmentAO);
		this.nextPort = 0;
		
	}
	
	@Override
	protected int route(IStreamObject<IMetaAttribute> object) {
		
		if (nextPort < this.numFragments-1){
			nextPort++;
		}else{
			nextPort = 0;
		}
		return nextPort;
				
	}
	
}