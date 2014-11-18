package de.uniol.inf.is.odysseus.server.fragmentation.horizontal.logicaloperator;

import java.util.List;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;

/**
 * A {@link HashFragmentAO} can be used to fragment incoming streams. <br />
 * The {@link HashFragmentAO} must have exact one input and can have only one parameter for the number of fragments to build the 
 * hash key from the whole tuple or more parameters specifying the hash key by attributes. <br />
 * It can be used in PQL: <br />
 * <code>output = HASHFRAGMENT([FRAGMENTS=n], input)</code> or <br />
 * <code>output = HASHFRAGMENT([FRAGMENTS=n, ATTRIBUTES=[attr1...attrn]], input)</code>
 * @author Michael Brand
 */
@LogicalOperator(name = "HASHFRAGMENT", minInputPorts = 1, maxInputPorts = 1, doc="Can be used to fragment incoming streams",category={LogicalOperatorCategory.PROCESSING})
public class HashFragmentAO extends AbstractFragmentAO {
	
	private static final long serialVersionUID = -6789007084291408905L;
	
	/**
	 * The URIs of the attributes forming the hash key, if the key is not the whole tuple.
	 */
	private Optional<List<String>> attributeURIs;
	
	/**
	 * The indices of the attributes forming the hash key, if the key is not the whole tuple.
	 */
	private Optional<List<Integer>> attributeIndices;
	
	/**
	 * Constructs a new {@link HashFragmentAO}.
	 * @see UnaryLogicalOp#UnaryLogicalOp()
	 */
	public HashFragmentAO() {
		
		super();
		
		this.attributeURIs = Optional.absent();
		this.attributeIndices = Optional.absent();
		
	}

	/**
	 * Constructs a new {@link HashFragmentAO} as a copy of an existing one.
	 * @param fragmentAO The {@link HashFragmentAO} to be copied.
	 * @see UnaryLogicalOp#UnaryLogicalOp(AbstractLogicalOperator)
	 */
	public HashFragmentAO(HashFragmentAO fragmentAO) {
		
		super(fragmentAO);
		
		if(fragmentAO.isPartialKey()) {
			
			List<String> attributeURIs = Lists.newArrayList(fragmentAO.attributeURIs.get());
			this.attributeURIs = Optional.of(attributeURIs);
			List<Integer> attributeIndices = Lists.newArrayList(fragmentAO.attributeIndices.get());
			this.attributeIndices = Optional.of(attributeIndices);
			
		} else {
			
			this.attributeURIs = Optional.absent();
			this.attributeIndices = Optional.absent();
			
		}
		
	}

	@Override
	public AbstractLogicalOperator clone() {
		
		return new HashFragmentAO(this);
		
	}
	
	@Override
	public void initialize() {
		
		if(!this.isPartialKey())
			return;
		
		List<String> attributeURIs = this.attributeURIs.get();
		List<Integer> attributeIndices = Lists.newArrayList();
		
		// Determine indices of attributes
		for(int index = 0; index < this.getInputSchema().size(); index++) {
			
			if(attributeURIs.contains(this.getInputSchema().get(index).getAttributeName()))
				attributeIndices.add(index);
			
		}
		
		if(attributeIndices.size() != attributeURIs.size())
			throw new QueryParseException("Could not find all attributes within " + this.getInputSchema() + "!");
		else if(attributeIndices.size() == this.getInputSchema().size()) {
			
			// All attributes chosen -> no partial key
			this.attributeURIs = Optional.absent();
			this.attributeIndices = Optional.absent();
			
		} else this.attributeIndices = Optional.of(attributeIndices);
		
	}
	
	/**
	 * Returns the URIs of the attributes forming the hash key, if the key is not the whole tuple.
	 */
	@GetParameter(name = "ATTRIBUTES")
	public List<String> getAttributes() {

		if(this.isPartialKey()) {
			return this.attributeURIs.get();
		} 
			
		List<String> attributes = Lists.newArrayList();
		return attributes;
	}

	/**
	 * Sets the URIs of the attributes forming the hash key, if the key is not the whole tuple.
	 */
	@Parameter(type = StringParameter.class, name = "ATTRIBUTES", optional = true, isList = true)
	public void setAttributes(List<String> uris) {
		
		this.attributeURIs = Optional.fromNullable(uris);
		
		List<String> attributes = Lists.newArrayList();
		for(String uri : uris)
			attributes.add("'" + uri + "'");
		
		this.addParameterInfo("ATTRIBUTES", attributes);
		
	}
	
	/**
	 * Checks, if the key is not the whole tuple
	 * @return True, if the key was specified by {@link #setAttributes(List)}
	 */
	public boolean isPartialKey() {
		
		return this.attributeURIs.isPresent();
		
	}

}