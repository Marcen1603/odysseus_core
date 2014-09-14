package de.uniol.inf.is.odysseus.sports.rest.dto;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;


public class DataTransferObject{

	private String datatype;
	private String payload;
	
	public DataTransferObject(String datatype, Object payload) {
		this.datatype = datatype;
		this.payload = getJSONString(payload);
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
		this.payload = getJSONString(payload);
	}
	
	public static String getJSONString(Object object) {
		ObjectMapper mapper = new ObjectMapper();
	
		String json = "";
		try {
			json = mapper.writeValueAsString(object);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		return json;

	}
	
}
