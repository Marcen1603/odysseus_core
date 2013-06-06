package de.uniol.inf.is.odysseus.wsenrich.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.apache.http.HttpEntity;
import org.xml.sax.InputSource;

public class HttpEntityToInputSourceConverter {
	
	/**
	 * The reader
	 */
	private Reader reader;
	
	/**
	 * The output as an InputSource
	 */
	private InputSource output;
	
	/**
	 * The HttpEntity which has to be convertet to a InputSource
	 */
	private HttpEntity entity;
	
	/**
	 * Constructor for the converter. The given HttpEntity will
	 * be automatically converted into a InputSource. 
	 * Uses standard-charset "UTF-8" for converting
	 * @param entity the entity to convert
	 */
	public HttpEntityToInputSourceConverter(HttpEntity entity) {
		
		this.entity = entity;
		convert(null);
	}
	
	/**
	 * Consturctor for the converter. The given HttpEntity will
	 * be automatically converted into a InputSource.
	 * Uses a specified Charset for converting
	 * @param entity entity the entity to convert
	 * @param charset
	 */
	public HttpEntityToInputSourceConverter(HttpEntity entity, String charset) {
		
		convert(charset);
	}
	
	/**
	 * Converts the given Http Entity into a InputSource
	 * @param charset The charset to convert
	 */
	private void convert(String charset) {
		
		if(charset == null || charset.equals("")) {
			//Converts using Standard-Charset "UTF-8"
			try {
				
				InputStream input = this.entity.getContent();
				this.reader = new InputStreamReader(input, "UTF-8");
				this.output = new InputSource(reader);

				
			} catch (IllegalStateException e) {
				// TODO 
				e.printStackTrace();
				
			} catch (IOException e) {
				// TODO 
				e.printStackTrace();
				
			}
		//Converts using specified Charset	
		} else {
			
			try {
				
				InputStream input = this.entity.getContent();
				this.reader = new InputStreamReader(input, charset);
				this.output = new InputSource(reader);
	
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
	 * @return the converted HttpEntity
	 */
	public InputSource getOutput() {
		return this.output;
	}
	
	
	

}
