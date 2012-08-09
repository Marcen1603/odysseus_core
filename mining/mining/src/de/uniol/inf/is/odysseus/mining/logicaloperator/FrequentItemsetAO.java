package de.uniol.inf.is.odysseus.mining.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.mining.MiningDatatypes;

@LogicalOperator(name = "FREQUENTITEMSET", minInputPorts = 1, maxInputPorts = 1)
public class FrequentItemsetAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 7771591123865284928L;
	private int transactions = Integer.MAX_VALUE;
	private String algorithm = "";
	private int minsupport = 3;

	public FrequentItemsetAO() {

	}

	public FrequentItemsetAO(FrequentItemsetAO frequentItemsetAO) {
		this.algorithm = frequentItemsetAO.algorithm;
		this.minsupport = frequentItemsetAO.minsupport;
		this.transactions = frequentItemsetAO.transactions;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new FrequentItemsetAO(this);
	}

	protected SDFSchema getOutputSchemaIntern(int pos) {
		if (pos == 0) {
			List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
			SDFAttribute attributeId = new SDFAttribute(null, "id", SDFDatatype.INTEGER);
			attributes.add(attributeId);
			SDFAttribute attributeSet = new SDFAttribute(null, "set", MiningDatatypes.FREQUENT_ITEM_SET);
			attributes.add(attributeSet);
			SDFSchema outSchema = new SDFSchema(getInputSchema(0).getURI(), attributes);
			return outSchema;
		} else {
			List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
			SDFAttribute attributeCount = new SDFAttribute(null, "count", SDFDatatype.INTEGER);
			attributes.add(attributeCount);
			SDFAttribute attributeNeeded = new SDFAttribute(null, "needed", SDFDatatype.INTEGER);
			attributes.add(attributeNeeded);
			SDFAttribute attributeTotal = new SDFAttribute(null, "total", SDFDatatype.INTEGER);
			attributes.add(attributeTotal);
			SDFSchema outSchema = new SDFSchema(getInputSchema(0).getURI(), attributes);
			return outSchema;
		}
	}

	@Parameter(name = "algorithm", type = StringParameter.class)
	public void setAlgorithm(String algo) {
		this.algorithm = algo;
	}

	public String getAlgorithm() {
		return this.algorithm;
	}

	@Parameter(name = "support", optional = true, type = IntegerParameter.class)
	public void setMinSupport(int support) {
		this.minsupport = support;
	}

	public int getMinSupport() {
		return minsupport;
	}

	@Parameter(name = "transactions", optional = true, type = IntegerParameter.class)
	public void setMaxTransactions(int transactions) {
		this.transactions = transactions;
	}

	public int getMaxTransactions() {
		return transactions;
	}
}
