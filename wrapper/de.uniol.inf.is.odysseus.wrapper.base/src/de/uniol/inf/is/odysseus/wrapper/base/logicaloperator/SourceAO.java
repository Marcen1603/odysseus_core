package de.uniol.inf.is.odysseus.wrapper.base.logicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.wrapper.base.Activator;

/**
 * @author ckuka
 */

@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "ADAPTER")
public class SourceAO extends AbstractLogicalOperator {
	/**
     * 
     */
	private static final long serialVersionUID = 2514000374871326771L;

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
		this.adapter = ao.adapter;
		this.options.putAll(ao.options);
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
		List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
		for (final String item : schemaAttributes) {
			final String[] schemaInformation = item.split(":");
			SDFAttribute attribute;
			try {
				attribute = new SDFAttribute(null,
						schemaInformation[0], Activator.getDataDictionary().getDatatype(schemaInformation[1]));
			} catch (DataDictionaryException e) {
				throw new QueryParseException(e.getMessage());
			}
			attrs.add(attribute);
		}
		// TODO: Add Sourcename to Attributes ... e.g. collect Attributes
		this.setOutputSchema(0,new SDFSchema("", attrs));
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
	 * de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator#clone()
	 */
	@Override
	public SourceAO clone() {
		return new SourceAO(this);
	}

}
