package de.uniol.inf.is.odysseus.mining.logicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name = "FREQUENTITEMSET", minInputPorts = 1, maxInputPorts = 1, doc="This operator create frequent item sets from a given stream. The result stream creates a tuple with 3 attributes: id: the number (a simple counter) of the pattern, set: the frequent pattern, which is a list of tuples (a nested attribute ~ NF^2), support: the support of the pattern",category={LogicalOperatorCategory.MINING})
public class FrequentItemsetAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 7771591123865284928L;
	private int transactions = Integer.MAX_VALUE;
	private String learner = "";
	private Map<String, String> options = new HashMap<>();
	private double support;

	public FrequentItemsetAO() {

	}

	public FrequentItemsetAO(FrequentItemsetAO frequentItemsetAO) {
		this.learner = frequentItemsetAO.learner;
		this.support = frequentItemsetAO.support;
		this.transactions = frequentItemsetAO.transactions;
		this.options = new HashMap<>(frequentItemsetAO.options);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new FrequentItemsetAO(this);
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		if (pos == 0) {
			List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
			SDFAttribute attributeId = new SDFAttribute(null, "id", SDFDatatype.INTEGER);
			attributes.add(attributeId);
			// subschema is inputschema!
			SDFDatatype list = new SDFDatatype("PATTERN", SDFDatatype.KindOfDatatype.LIST, this.getInputSchema(0));
			SDFAttribute attributeSet = new SDFAttribute(null, "set", list);
			attributes.add(attributeSet);			
			SDFAttribute support = new SDFAttribute(null, "support", SDFDatatype.INTEGER);
			attributes.add(support);
			SDFSchema outSchema = new SDFSchema(getInputSchema(0).getURI(), getInputSchema(0).getType(), attributes);
			return outSchema;
		} else {
			List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
			SDFAttribute attributeCount = new SDFAttribute(null, "count", SDFDatatype.INTEGER);
			attributes.add(attributeCount);
			SDFAttribute attributeNeeded = new SDFAttribute(null, "needed", SDFDatatype.INTEGER);
			attributes.add(attributeNeeded);
			SDFAttribute attributeTotal = new SDFAttribute(null, "total", SDFDatatype.INTEGER);
			attributes.add(attributeTotal);
			SDFSchema outSchema = new SDFSchema(getInputSchema(0).getURI(), getInputSchema(0).getType(), attributes);
			return outSchema;
		}
	}

	public String getLearner() {
		return learner;
	}

	@Parameter(name = "learner", type = StringParameter.class, optional = true)
	public void setLearner(String learner) {
		this.learner = learner;
	}
	
	@Parameter(name = "algorithm", type = StringParameter.class, optional = true, isMap = true)
	public void setOptions(Map<String, String> options) {
		for (Entry<String, String> o : options.entrySet()) {
			this.options.put(o.getKey().toLowerCase(), o.getValue());
		}
	}

	public Map<String, String> getOptions() {
		return this.options;
	}

	@Parameter(name = "support", optional = true, type = DoubleParameter.class)
	public void setSupport(double support) {
		this.support = support;
	}
	
	public double getSupport(){
		return this.support;
	}

	public int getMinSupport() {
		if(support<=1.0){
			return (int) (this.transactions*support);
		}else{
			return (int)support;
		}		
	}

	@Parameter(name = "transactions", optional = true, type = IntegerParameter.class)
	public void setMaxTransactions(int transactions) {
		this.transactions = transactions;
	}

	public int getMaxTransactions() {
		return transactions;
	}
}
