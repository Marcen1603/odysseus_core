package de.uniol.inf.is.odysseus.wrapper.base.logicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.OutputSchemaSettable;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

/**
 * @author ckuka
 */

@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "ADAPTER")
public class SourceAO extends AbstractLogicalOperator implements
		OutputSchemaSettable {
	/**
     * 
     */
	private static final long serialVersionUID = 2514000374871326771L;

	private final Map<Integer, SDFAttributeList> outputSchema = new HashMap<Integer, SDFAttributeList>();
	private final Map<String, String> options = new HashMap<String, String>();
	private String adapter;

	/**
     * 
     */
	public SourceAO() {
		super();
	}

	/**
	 * @param ao
	 */
	public SourceAO(final SourceAO ao) {
		super(ao);
		for (final Entry<Integer, SDFAttributeList> entry : ao.outputSchema
				.entrySet()) {
			this.outputSchema.put(entry.getKey(), entry.getValue().clone());
		}
		this.adapter = ao.adapter;
		this.options.putAll(ao.options);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator#getOutputSchema
	 * ()
	 */
	@Override
	public SDFAttributeList getOutputSchema() {
		return this.getOutputSchema(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator#
	 * getOutputSchema(int)
	 */
	@Override
	public SDFAttributeList getOutputSchema(final int port) {
		return this.outputSchema.get(port);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.OutputSchemaSettable#setOutputSchema
	 * (de.uniol.inf .is.odysseus.sourcedescription.sdf.schema.SDFAttributeList)
	 */
	@Override
	public void setOutputSchema(final SDFAttributeList outputSchema) {
		this.setOutputSchema(outputSchema, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.OutputSchemaSettable#setOutputSchema
	 * (de.uniol.inf .is.odysseus.sourcedescription.sdf.schema.SDFAttributeList,
	 * int)
	 */
	@Override
	public void setOutputSchema(final SDFAttributeList outputSchema,
			final int port) {
		this.outputSchema.put(port, outputSchema);
	}

	/**
	 * Defines the source output schema through the builder
	 * 
	 * @param schemaAttributes
	 *            The source schema in attribute:type notation
	 * @FIXME Use {@link CreateSDFAttributeParameter} when newInstance bug is
	 *        fixed
	 */
	@Parameter(name = "SCHEMA", type = StringParameter.class, isList = true)
	public void setOutputSchemaWithList(final List<String> schemaAttributes) {
		final SDFAttributeList schema = new SDFAttributeList("");
		for (final String item : schemaAttributes) {
			final String[] schemaInformation = item.split(":");
			final SDFAttribute attribute = new SDFAttribute(null,
					schemaInformation[0], GlobalState.getActiveDatadictionary()
							.getDatatype(schemaInformation[1]));
			schema.add(attribute);
		}
		this.setOutputSchema(schema, 0);
	}

	@Parameter(name = "OPTIONS", optional = true, type = StringParameter.class, isList = true)
	public void setOptions(final List<String> optionsList) {
		for (final String item : optionsList) {
			final String[] option = item.split(":");
			if (option.length == 2) {
				this.options.put(option[0],
						item.substring(option[0].length() + 1));
			} else {
				this.options.put(option[0], "");
			}
		}
	}

	@Parameter(name = "ADAPTER", type = StringParameter.class, isList = false)
	public void setAdapter(final String adapterName) {
		this.adapter = adapterName;
	}

	public String getAdapter() {
		return this.adapter;
	}

	public List<String> getOptions() {
		final List<String> optionList = new ArrayList<String>();
		for (final String key : this.options.keySet()) {
			optionList.add(key + ":" + this.options.get(key));
		}
		return optionList;
	}

	public Map<String, String> getOptionsMap() {
		return this.options;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator#clone()
	 */
	@Override
	public SourceAO clone() {
		return new SourceAO(this);
	}

}
