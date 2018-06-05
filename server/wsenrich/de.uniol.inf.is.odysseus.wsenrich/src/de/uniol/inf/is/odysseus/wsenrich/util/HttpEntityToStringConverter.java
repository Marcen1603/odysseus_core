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
	private static final String WRONG_LESS = "&lt;";

	/**
	 * Static Variable for a possibly wrong converted ">"
	 */
	private static final String WRONG_GREATER = "&gt;";

	/**
	 * Static Variable for the String of "<"
	 */
	private static final String CORRECT_LESS = "<";

	/**
	 * Static Variable for the String of ">"
	 */
	private static final String CORRECT_GREATER = ">";

	/**
	 * Static Variable for the correct begin of a xml Document
	 */
	private static final String CORRECT_DOCUMENT_BEGIN = "<?xml";

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
	 *
	 * @param charset
	 *            the charset for converting
	 */
	public HttpEntityToStringConverter(String charset) {
		this.charset = charset;
	}

	/**
	 * Constructor with a HttpEntity
	 *
	 * @param entity
	 *            the HttpEntity to convert
	 */
	public HttpEntityToStringConverter(HttpEntity entity) {
		this.entity = entity;
	}

	/**
	 * Constructior with specified HttpEntity and charset
	 *
	 * @param entity
	 *            the HttpEntity to convert
	 * @param charset
	 *            the charset for converting
	 */
	public HttpEntityToStringConverter(HttpEntity entity, String charset) {
		this.entity = entity;
		this.charset = charset;
	}

	/**
	 * Converts the Http Entity to a String
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	public void convert() throws IllegalStateException, IOException {
		// Converts with Standard-Charset UTF-8
		if (this.charset == null || this.charset.equals("")) {
			StringBuffer temp = new StringBuffer(IOUtils.toString(entity.getContent(), "UTF-8"));
			replaceHtmlCodecs(temp, WRONG_LESS, CORRECT_LESS);
			replaceHtmlCodecs(temp, WRONG_GREATER, CORRECT_GREATER);
			checkForCorrectDocumentBegin(temp);
			this.output = temp.toString();
			// Converts with a specified charset
		} else {
			StringBuffer temp = new StringBuffer(IOUtils.toString(entity.getContent(), charset));
			replaceHtmlCodecs(temp, WRONG_LESS, CORRECT_LESS);
			replaceHtmlCodecs(temp, WRONG_GREATER, CORRECT_GREATER);
			checkForCorrectDocumentBegin(temp);
			this.output = temp.toString();
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
	 *
	 * @param entity
	 */
	public void setInput(HttpEntity entity) {
		this.entity = entity;
	}

	/**
	 * Recursive Method to replace wrong Characters Works ONLY if each Character
	 * in the parameters htmlCode an replacement are different!!!
	 *
	 * @param temp
	 *            the message
	 * @param htmlCode
	 *            the html code to be replaced
	 * @param replacement
	 *            the character which replace the htmlCode
	 * @return
	 */
	private StringBuffer replaceHtmlCodecs(StringBuffer temp, String htmlCode, String replacement) {
		int match = temp.indexOf(htmlCode);
		if (match > -1) {
			temp.replace(match, match + htmlCode.length(), replacement);
			return replaceHtmlCodecs(temp, htmlCode, replacement);
		} else
			return temp;
	}

	private StringBuffer checkForCorrectDocumentBegin(StringBuffer temp) {
		int match = temp.indexOf(CORRECT_DOCUMENT_BEGIN);
		if (match < 0) {
			return temp;
		}
		if (match != 0) {
			temp.replace(0, temp.length(), temp.substring(match, temp.length()));
		}
		return temp;
	}

}
