package de.offis.client;

import java.io.Serializable;
import java.util.Map;

/**
 * Scai Data Model for OperatorType.
 *
 * @author Alexander Funk
 * 
 */
public class OperatorType extends AbstractScaiDTO implements Serializable {

	private static final long serialVersionUID = 8641181564956458819L;
	private String name;
	private String metaType;
	private Map<String, String> props;
	private Map<String, Boolean> readOnly;
	private String desc;
	
	public OperatorType(String name, String metaType, Map<String, String> props, Map<String, Boolean> readOnly, String desc) {
		super(ScaiType.OPERATOR_TYPE, Type.NONE);
		this.name = name;
		this.metaType = metaType;
		this.props = props;
		this.readOnly = readOnly;
		this.desc = desc;
	}
	
	protected OperatorType() {
		super(ScaiType.OPERATOR_TYPE, Type.NONE);
	}
	
	public String getName() {
		return name;
	}
	
	public String getMetaType() {
		return metaType;
	}
	
	public Map<String, String> getProps() {
		return props;
	}
	
	public Map<String, Boolean> getReadOnly() {
		return readOnly;
	}
	
	public String getDesc() {
		return desc;
	}
	
	public Integer getInputSlots() {
		return Integer.parseInt(props.get("inputSlots"));
	}
	
	public Integer getOutputSlots() {
		return Integer.parseInt(props.get("outputSlots"));
	}
}
