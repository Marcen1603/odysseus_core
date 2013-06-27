package de.uniol.inf.is.odysseus.wsenrich.util;

import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpEntityToStringConverter {
	
	/**
	 * For Logging
	 */
	static Logger logger = LoggerFactory.getLogger(HttpEntityToStringConverter.class);
	
	/**
	 * Static Variable for a possibly wrong converted "<"
	 */
	private static final String LESS_THEN = "&lt;";
	
	/**
	 * Static Variable for a possibly wrong converted ">"
	 */
	private static final String GREATER_THEN = "&gt;";
	
	/**
	 * Static Variable for the  String of "<"
	 */
	private static final String LESS = "<";
	
	/**
	 * Static Variable for the String of ">"
	 */
	private static final String GREATER = ">";
	
	/**
	 * The HttpEntity
	 */
	private HttpEntity entity;
	
	/**
	 * The HttpEntity as a String representation
	 */
	private String output;
	
	/**
	 * The used charset for converting
	 */
	private String charset;
	
	/**
	 * Constructor with a specified charset
	 * @param charset the charset for converting
	 */
	public HttpEntityToStringConverter(String charset) {
		this.charset = charset;
	}
	
	/**
	 * Constructor with a HttpEntity
	 * @param entity the HttpEntity to convert
	 */
	public HttpEntityToStringConverter(HttpEntity entity) {	
		this.entity = entity;
	}
	
	/**
	 * Constructior with specified HttpEntity and charset
	 * @param entity the HttpEntity to convert
	 * @param charset the charset for converting
	 */
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
				StringBuffer temp = new StringBuffer(IOUtils.toString(entity.getContent(), "UTF-8"));
				replaceHtmlCodecs(temp, LESS_THEN, LESS);
				replaceHtmlCodecs(temp, GREATER_THEN, GREATER);
				this.output = temp.toString();
			} catch (IllegalStateException | IOException e) {
				logger.error("There was an error while converting the HttpEntity to a String. Cause {}", e.getMessage());
			} 	
		//Converts with a specified charset
		} else {
			try {
				
				StringBuffer temp = new StringBuffer(IOUtils.toString(entity.getContent(), charset));
				replaceHtmlCodecs(temp, LESS_THEN, LESS);
				replaceHtmlCodecs(temp, GREATER_THEN, GREATER);
				this.output = temp.toString();
			} catch (IllegalStateException | IOException e) {
				logger.error("There was an error while converting the HttpEntity to a String. Cause {}", e.getMessage());
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
	
	/**
	 * Recursive Method to replace wrong Characters
	 * @param temp the message
	 * @param htmlCode the html code to be replaced
	 * @param replacement the character which replace the htmlCode
	 * @return
	 */
	private StringBuffer replaceHtmlCodecs(StringBuffer temp, String htmlCode, String replacement) {	
		int match = temp.indexOf(htmlCode);
		if(match > -1) {
			temp.replace(match, match + htmlCode.length(), replacement);
			 return replaceHtmlCodecs(temp, htmlCode, replacement);	
		} else 
			return temp;	
	}
}
