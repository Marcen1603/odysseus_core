package de.uniol.inf.is.odysseus.wsenrich.util;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;

public class HttpEntityToStringConverter {
	
	
	private HttpEntity entity;
	private String output;
	
	
	public HttpEntityToStringConverter(HttpEntity entity) {
		
		this.entity = entity;
		convert(null);
		
	}
	
	public HttpEntityToStringConverter(HttpEntity entity, String charset) {
		
		this.entity = entity;
		convert(charset);
	}
	
	/**
	 * Converts the Http Entity to a String
	 */
	private void convert(String charset) {
		
		//Converts with Standard-Charset UTF-8
		if(charset == null || charset.equals("")) {
			try {
				
				this.output = IOUtils.toString(entity.getContent(), "UTF-8");
				
			} catch (IllegalStateException e) {
				// TODO 
				e.printStackTrace();
				
			} catch (IOException e) {
				// TODO 
				e.printStackTrace();
			}
			
		//Converts with a specified charset
		} else {
			try {
				
				this.output = IOUtils.toString(entity.getContent(), charset);
				
			} catch (IllegalStateException e) {
				// TODO 
				e.printStackTrace();
				
			} catch (IOException e) {
				// TODO 
				e.printStackTrace();
			}
	
		}
		
	}
	
	/**
	 * @return the converted output as a String representation
	 */
	public String getOutput() {
		return this.output;
		
	}
	
}
