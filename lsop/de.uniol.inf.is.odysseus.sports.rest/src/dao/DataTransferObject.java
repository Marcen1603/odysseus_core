package dao;

import java.io.Serializable;

public class DataTransferObject implements Serializable{
	
	private static final long serialVersionUID = -3042714386806400699L;
	private String datatype;
	private Object payload;
	
	public DataTransferObject(String datatype, Object payload) {
		this.datatype = datatype;
		this.payload = payload;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public Object getPayload() {
		return payload;
	}

	public void setPayload(Object payload) {
		this.payload = payload;
	}
	
	
}
