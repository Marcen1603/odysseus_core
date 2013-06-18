package de.uniol.inf.is.odysseus.wsenrich.util;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;

public class HttpEntityToStringConverter {
	
	private HttpEntity entity;
	private String output;
	private String charset;
	
	public HttpEntityToStringConverter(String charset) {
		
		this.charset = charset;
		
	}
	
	public HttpEntityToStringConverter(HttpEntity entity) {
		
		this.entity = entity;
		
		
	}
	
	public HttpEntityToStringConverter(HttpEntity entity, String charset) {
		
		this.entity = entity;
		this.charset = charset;
		
	}
	
	/**
	 * Converts the Http Entity to a String
	 */
	public void convert() {
		
		//Converts with Standard-Charset UTF-8
		if(this.charset == null || this.charset.equals("")) {
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
	
	/**
	 * @return the charset
	 */
	public String getCharset() {
		return this.charset;
		
	}
	
	/**
	 * Setter for the HttpEntity Input
	 * @param entity
	 */
	public void setInput(HttpEntity entity) {
		this.entity = entity;
	}
	
	
}
