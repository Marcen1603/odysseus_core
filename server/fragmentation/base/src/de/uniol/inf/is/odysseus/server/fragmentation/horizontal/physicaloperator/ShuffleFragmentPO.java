package de.uniol.inf.is.odysseus.server.fragmentation.horizontal.physicaloperator;

import java.util.Random;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.ShuffleFragmentAO;

public class ShuffleFragmentPO<T extends IStreamObject<IMetaAttribute>> extends
AbstractStaticFragmentPO<T> {

	private Random rand;
	
	public ShuffleFragmentPO(ShuffleFragmentAO fragmentAO) {
		super(fragmentAO);
		rand = new Random();
	}

	@Override
	protected int route(IStreamObject<IMetaAttribute> object) {
		return rand.nextInt(numFragments);
	}

}
