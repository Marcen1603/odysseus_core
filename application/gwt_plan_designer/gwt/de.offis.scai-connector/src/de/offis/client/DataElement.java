package de.offis.client;

import java.io.Serializable;
import java.util.List;

/**
 * Scai Data Model for DataElement.
 *
 * @author Alexander Funk
 * 
 */
public class DataElement extends AbstractScaiDTO implements Serializable {

	private static final long serialVersionUID = -5500540268794975175L;
	private String name;
	private String dataTypeName;
	private List<String> dataElementNames;
	
	public DataElement(String name, String dataTypeName) {
		super(ScaiType.DATA_ELEMENT, Type.ATOMIC);
		this.name = name;
		this.dataTypeName = dataTypeName;
		this.dataElementNames = null;
	}
	
	public DataElement(String name, List<String> dataElementNames) {
		super(ScaiType.DATA_ELEMENT, Type.COMPLEX);
		this.name = name;
		this.dataTypeName = null;
		this.dataElementNames = dataElementNames;
	}
	
	protected DataElement() {
		super();
		// Serializable
	}
	
	public String getName() {
		return name;
	}
	
	public String getDataTypeNameA() {
		// A = nur bei atomic
		return dataTypeName;
	}
	
	public List<String> getDataElementNamesC() {
		// C = nur bei complex
		return dataElementNames;
	}
}
