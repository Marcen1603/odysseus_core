package de.uniol.inf.is.odysseus.server.fragmentation.horizontal.physicaloperator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator.HashFragmentAO;

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
AbstractStaticFragmentPO<T> {

	/**
	 * The indices of the attributes forming the hash key, if the key is not the
	 * whole tuple.
	 */
	final private int[] indices;
	final boolean optimizeDistribution;
	final Map<Integer,Integer> portMapping = new HashMap<>();
	int lastPort = 0;

	/**
	 * Constructs a new {@link HashFragmentPO}.
	 * 
	 * @param fragmentAO
	 *            the {@link HashFragmentAO} transformed to this
	 *            {@link HashFragmentPO}.
	 */
	public HashFragmentPO(HashFragmentAO fragmentAO) {

		super(fragmentAO);
		
		this.optimizeDistribution = fragmentAO.isOptimizeDistribution();

		List<SDFAttribute> fragmentAttributes = fragmentAO.getAttributes() ;
		
		if (fragmentAttributes != null) {

			indices = new int[fragmentAO.getAttributes().size()];
			int i = 0;
			
			SDFSchema inputSchema = fragmentAO.getInputSchema();
			
			for (SDFAttribute restrictAttribute:fragmentAO.getAttributes()){
				int pos = inputSchema.indexOf(restrictAttribute);
				if (pos == -1){
					throw new IllegalArgumentException("Attribute "+restrictAttribute+" not found in input schema");
				}
				indices[i++] = pos;
			}		
		} else{
			
			indices = null;
			
		}
			
	}

	public HashFragmentPO(SDFSchema inputSchema,List<SDFAttribute> fragmentAttributes, boolean optimizeDistribution, int numFragments,int heartbeatRate) {
		super(numFragments, heartbeatRate);
		
		this.optimizeDistribution = optimizeDistribution;
		
		
		if (fragmentAttributes != null) {

			indices = new int[fragmentAttributes.size()];
			int i = 0;
		
			for (SDFAttribute restrictAttribute:fragmentAttributes){
				int pos = inputSchema.indexOf(restrictAttribute);
				if (pos == -1){
					throw new IllegalArgumentException("Attribute "+restrictAttribute+" not found in input schema");
				}
				indices[i++] = pos;
			}		
		} else{
			
			indices = null;
			
		}
	}
	


	@Override
	protected int route(IStreamObject<IMetaAttribute> object) {

		final int hashCode;

		if (this.isPartialKey()) {
			
			hashCode = Math.abs(object.restrictedHashCode(indices));
			
		} else{
			
			hashCode = Math.abs(object.hashCode());
			
		}
		if (optimizeDistribution){
			return getPort(hashCode);
		}else{
			return hashCode % numFragments;
		}
	}
	
	
	
	private int getPort(int hashCode) {
		Integer port = portMapping.get(hashCode);
		if (port == null){
			if (lastPort+1 < numFragments){
				lastPort++;
			}else{
				lastPort = 0;
			}
			port = lastPort;
			portMapping.put(hashCode, lastPort);
		}
		return port;
	}


	/**
	 * Checks, if the key is not the whole tuple.
	 */
	public boolean isPartialKey() {

		return indices != null;

	}
	
	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof HashFragmentPO)){
			return false;
		}
		
		@SuppressWarnings("unchecked")
		HashFragmentPO<T> po = (HashFragmentPO<T>) ipo;
		if (po.optimizeDistribution != this.optimizeDistribution){
			return false;
		}
		
		if (this.indices != null && po.indices == null){
			return false;
		}
		
		if (this.indices == null && po.indices != null){
			return false;
		}
		if (this.indices != null && po.indices != null){
			if (this.indices.length != po.indices.length){
				return false;
			}
			for (int i=0;i<this.indices.length;i++){
				if (this.indices[i] != po.indices[i]){
					return false;
				}
			}
		}
		
		return super.isSemanticallyEqual(ipo);
	}
}