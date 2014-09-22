package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.hash.physicaloperator;

import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
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
	 * The indices of the attributes forming the hash key, if the key is not the whole tuple.
	 */
	private Optional<List<Integer>> attributeIndices;

	/**
	 * Constructs a new {@link HashFragmentPO}.
	 * @param fragmentAO the {@link HashFragmentAO} transformed to this {@link HashFragmentPO}.
	 */
	public HashFragmentPO(HashFragmentAO fragmentAO) {
		
		super(fragmentAO);
		
		if(fragmentAO.isPartialKey()) {
		
			List<Integer> attributeIndices = Lists.newArrayList();
			
			// Determine indices of attributes
			for(int index = 0; index < fragmentAO.getInputSchema().size(); index++) {
				
				if(fragmentAO.getAttributes().contains(fragmentAO.getInputSchema().get(index).getAttributeName()))
					attributeIndices.add(index);
				
			}
			
			if(attributeIndices.size() != fragmentAO.getAttributes().size())
				throw new QueryParseException("Could not find all attributes within " + fragmentAO.getInputSchema() + "!");
			else if(attributeIndices.size() == fragmentAO.getInputSchema().size()) {
				
				// All attributes chosen -> no partial key
				this.attributeIndices = Optional.absent();
				
			} else this.attributeIndices = Optional.of(attributeIndices);
			
		} else this.attributeIndices = Optional.absent();
		
	}

	/**
	 * Constructs a new {@link HashFragmentPO} as a copy of an existing one.
	 * @param fragmentPO The {@link HashFragmentPO} to be copied.
	 */
	public HashFragmentPO(HashFragmentPO<T> fragmentPO) {
		
		super(fragmentPO);
		
		if(fragmentPO.isPartialKey()) {
			
			List<Integer> attributeIndices = Lists.newArrayList(fragmentPO.attributeIndices.get());
			this.attributeIndices = Optional.of(attributeIndices);
			
		} else this.attributeIndices = Optional.absent();
		
	}
	
	@Override
	public AbstractPipe<T, T> clone() {
		
		return new HashFragmentPO<T>(this);
		
	}
	
	@Override
	protected int route(IStreamable object) {
		
		int hashCode = 0;
		
		if(this.isPartialKey() && object instanceof Tuple) {
			
			@SuppressWarnings("unchecked")
			Tuple<IMetaAttribute> tuple = (Tuple<IMetaAttribute>) object;
			int[] indices = new int[this.attributeIndices.get().size()];
			
			for(int index = 0; index < this.attributeIndices.get().size(); index++)
				indices[index] = this.attributeIndices.get().get(index);
			
			hashCode = Math.abs(tuple.restrictedHashCode(indices));
			
		} else hashCode = Math.abs(object.hashCode());
		
		return hashCode % this.numFragments;
		
	}
	
	/**
	 * Checks, if the key is not the whole tuple.
	 */
	public boolean isPartialKey() {
		
		return this.attributeIndices.isPresent();
		
	}
	
}