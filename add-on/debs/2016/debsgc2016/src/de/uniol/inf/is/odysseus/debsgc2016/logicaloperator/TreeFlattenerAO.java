package de.uniol.inf.is.odysseus.debsgc2016.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.LongParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ResolvedSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name="TreeFlattenerAO", minInputPorts=2, maxInputPorts=2,category={LogicalOperatorCategory.APPLICATION}, doc="An operator that creates ...")
public class TreeFlattenerAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -7308670306275116203L;

	private SDFAttribute rootNodeKey;
	private SDFAttribute nRootNodeKey;
	private SDFAttribute nRootRefToRoot;
	private SDFAttribute nRootRefToNRoot;
	
	private boolean keepAlive = true;
	private long cleanUpRate = 1000;
	
	private String outputListAttributeName = "List";
	
	public TreeFlattenerAO(TreeFlattenerAO op) {
		super(op);
		this.rootNodeKey = op.rootNodeKey;
		this.nRootNodeKey = op.nRootNodeKey;
		this.nRootRefToRoot = op.nRootRefToRoot;
		this.nRootRefToNRoot = op.nRootRefToNRoot;
		this.outputListAttributeName = op.outputListAttributeName;
	}

	public TreeFlattenerAO() {
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new TreeFlattenerAO(this);
	}

	
	public SDFAttribute getRootNodeKey() {
		return rootNodeKey;
	}

	@Parameter(type=ResolvedSDFAttributeParameter.class, optional = false, doc="The attribute in the root node (left input) with the id of this node.")
	public void setRootNodeKey(SDFAttribute rootNodeKey) {
		this.rootNodeKey = rootNodeKey;
	}

	public SDFAttribute getnRootNodeKey() {
		return nRootNodeKey;
	}

	@Parameter(type=ResolvedSDFAttributeParameter.class, optional = false, doc="The attribute in the non root node (right input) with the id of this node.")
	public void setnRootNodeKey(SDFAttribute nRootNodeKey) {
		this.nRootNodeKey = nRootNodeKey;
	}

	public SDFAttribute getnRootRefToRoot() {
		return nRootRefToRoot;
	}

	@Parameter(type=ResolvedSDFAttributeParameter.class, optional = false, doc="The attribute in the non root node (right input) that has a reference to its root node.")
	public void setnRootRefToRoot(SDFAttribute nRootRefToRoot) {
		this.nRootRefToRoot = nRootRefToRoot;
	}

	public SDFAttribute getnRootRefToNRoot() {
		return nRootRefToNRoot;
	}

	@Parameter(type=ResolvedSDFAttributeParameter.class, optional = false, doc="The attribute in the non root node (right input) that has a reference to a father non root node.")
	public void setnRootRefToNRoot(SDFAttribute nRootRefToNRoot) {
		this.nRootRefToNRoot = nRootRefToNRoot;
	}

	@Parameter(type=StringParameter.class, optional = true, doc="This name will be used in the output schema for the list of non root nodes.")
	public void setOutputListAttributeName(String outputListAttributeName) {
		this.outputListAttributeName = outputListAttributeName;
	}
	
	public String getOutputListAttributeName() {
		return outputListAttributeName;
	}
	
	public boolean isKeepAlive() {
		return keepAlive;
	}

	@Parameter(type=BooleanParameter.class, optional = true, doc="Typically, every time a child is added to the root, the root timestamp will be kept alive by extending the time interval. Set to false to avoid this.")
	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}
	
	public long getCleanUpRate() {
		return cleanUpRate;
	}

	@Parameter(type=LongParameter.class, optional = true, doc="After a number of elements the state is cleaned. Typically, every element is too expensive. 0 means no cleanup.")
	public void setCleanUpRate(long cleanUpRate) {
		this.cleanUpRate = cleanUpRate;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		SDFSchema r = getInputSchema(0);
		SDFSchema n = getInputSchema(1);
		SDFDatatype newType = SDFDatatype.createTypeWithSubSchema(SDFDatatype.LIST, SDFDatatype.TUPLE, n);
		SDFAttribute list = new SDFAttribute(n.getURI(), outputListAttributeName, newType);
		SDFSchema n_neu = SDFSchemaFactory.createNewAddAttribute(list, r);
		return n_neu;
	}
	
}
