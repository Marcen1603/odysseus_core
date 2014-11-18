package de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.hash.physicaloperator;

import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.IStreamable;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.hash.logicaloperator.HashFragmentAO;
import de.uniol.inf.is.odysseus.peer.distribute.modify.fragmentation.horizontal.physicaloperator.AbstractFragmentPO;

/**
 * A {@link HashFragmentPO} can be used to realize a {@link HashFragmentAO}. <br />
 * The {@link HashFragmentPO} uses a hash modulo n function to transfer
 * {@link StreamObject}s to different output ports and can handle
 * {@link IPunctuations}.
 * 
 * @author Michael Brand
 * @author Marco Grawunder
 */
public class HashFragmentPO<T extends IStreamObject<IMetaAttribute>> extends
		AbstractFragmentPO<T> {

	/**
	 * The indices of the attributes forming the hash key, if the key is not the
	 * whole tuple.
	 */
	final private int[] indices;

	/**
	 * Constructs a new {@link HashFragmentPO}.
	 * 
	 * @param fragmentAO
	 *            the {@link HashFragmentAO} transformed to this
	 *            {@link HashFragmentPO}.
	 */
	public HashFragmentPO(HashFragmentAO fragmentAO) {

		super(fragmentAO);

		if (fragmentAO.isPartialKey()) {

			indices = new int[fragmentAO.getAttributes().size()];
			int i = 0;
			
			SDFSchema inputSchema = fragmentAO.getInputSchema();
			
			for (String restrictAttribute:fragmentAO.getAttributes()){
				int pos = inputSchema.findAttributeIndex(restrictAttribute);
				if (pos > 0){
					indices[i++] = pos;
				}else{
					throw new IllegalArgumentException("Attribute "+restrictAttribute+" not found in input schema");
				}
			}
		}else{
			indices = null;
		}
			
			
	}


	@Override
	public AbstractPipe<T, T> clone() {

		throw new IllegalArgumentException("Currently not implemented");
	}

	@Override
	protected int route(IStreamObject<IMetaAttribute> object) {

		final int hashCode;

		if (this.isPartialKey()) {
			hashCode = Math.abs(object.restrictedHashCode(indices));
		} else{
			hashCode = Math.abs(object.hashCode());
		}
		
		return hashCode % this.numFragments;

	}
	
	/**
	 * Checks, if the key is not the whole tuple.
	 */
	public boolean isPartialKey() {

		return indices != null;

	}

}