package de.uniol.inf.is.odysseus.systemload.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name = "SYSTEMLOADTOPAYLOAD", maxInputPorts = 1, minInputPorts = 1, doc = "Adds attributes with the current system load (cpu, mem, net) to each tuple.", category = { LogicalOperatorCategory.BENCHMARK })
public class SystemLoadToPayloadAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = -7641641802321712574L;

	private boolean append = true;
	private String loadname;

	public SystemLoadToPayloadAO(SystemLoadToPayloadAO op) {
		super(op);
		this.append = op.append;
		this.loadname = op.loadname;
	}

	public SystemLoadToPayloadAO() {
	}

	public boolean isAppend() {
		return append;
	}

	@Parameter(name = "append", optional = true, type = BooleanParameter.class, doc ="Append the information to the input or create a new element")
	public void setAppend2(boolean append) {
		this.append = append;
	}
	
	public boolean isAppend2() {
		return append;
	}

	public String getLoadname() {
		return loadname;
	}

	@Parameter(name = "loadname", optional = false, type = StringParameter.class, doc ="TODO: What is this name??")
	public void setLoadname(String loadname) {
		this.loadname = loadname;
	}

	public SDFSchema buildOutputSchema(SDFSchema inputschema) {
		final List<SDFAttribute> attributes;
		final String[] names;
		if (append) {
			attributes = new ArrayList<>(inputschema.getAttributes());
		} else {
			attributes = new ArrayList<>(3);
		}
		names = new String[] { loadname+"_cpu",loadname+"_mem", loadname+"_net"};

		for (String n : names) {
			SDFAttribute a = new SDFAttribute(inputschema.getURI(), n,
					SDFDatatype.LONG, null, null, null);
			attributes.add(a);
		}
		SDFSchema schema = SDFSchemaFactory.createNewWithAttributes(attributes, inputschema);
		return schema;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		SDFSchema old = super.getOutputSchemaIntern(0);
		return buildOutputSchema(old);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SystemLoadToPayloadAO(this);
	}

}
