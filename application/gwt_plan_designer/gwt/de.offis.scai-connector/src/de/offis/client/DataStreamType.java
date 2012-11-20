package de.offis.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Scai Data Model for DataStreamType.
 *
 * @author Alexander Funk
 * 
 */
public class DataStreamType extends AbstractScaiDTO implements Serializable {
	
	private static final long serialVersionUID = -1395441350236152100L;
	private String name;
	private List<String> dataElementNames;
	
	public DataStreamType(String name, List<String> dataElementNames) {
		super(ScaiType.DATASTREAM_TYPE, Type.NONE);
		this.name = name;
		this.dataElementNames = new ArrayList<String>(dataElementNames);
	}
	
	protected DataStreamType() {
		super(ScaiType.DATASTREAM_TYPE, Type.NONE);
		// Serializable
	}
	
	public List<String> getDataElementNames() {
		return new ArrayList<String>(dataElementNames);
	}
	
	public String getName() {
		return name;
	}
}
