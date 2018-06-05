package de.uniol.inf.is.odysseus.wsenrich.util.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.jsonpath.JsonPath;

import de.uniol.inf.is.odysseus.wsenrich.util.interfaces.IKeyFinder;

public class JsonPathKeyFinder implements IKeyFinder {

	/**
	 * for logging
	 */
	static Logger logger = LoggerFactory.getLogger(XPathKeyFinder.class);

	/**
	 * The json message as a string representation
	 */
	private String message;

	/**
	 * The searched element
	 */
	private String search;

	/**
	 * The element data of the searched element
	 */
	private Object value;

	public JsonPathKeyFinder() {
		//Needed for the KeyFinderRegistry
	}

	@Override
	public String getSearch() {
		return this.search;
	}

	@Override
	public void setSearch(String search) {
		this.search = search;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

	@Override
	public void setMessage(String message, String charset, boolean multiTupleOutput) {
		this.message = message;
	}

	@Override
	public Object getValueOf(String search, boolean keyValue, int tupleCount) {
		if(!this.search.equals(search)) {
			this.search = search;
		}
		try {
			Object values = JsonPath.read(this.message, search);
			StringBuffer temp = new StringBuffer(values.toString());
			//replace [, ], {, }, ",
			replaceJsonData(temp, temp.indexOf("["), "[", "");
			replaceJsonData(temp, temp.indexOf("]"), "]", "");
			replaceJsonData(temp, temp.indexOf("{"), "{", "");
			replaceJsonData(temp, temp.indexOf("}"), "}", "");
			replaceJsonData(temp, temp.indexOf("\""), "\"", "");
			replaceJsonData(temp, temp.indexOf(","), ",", ", ");

			if(temp.equals("") || temp.length() == 0 || temp == null) {
				return null;
			} else {
				this.value = temp.toString();
				return value;
			}
		} catch (RuntimeException e) {
			logger.error("The specified JsonPath-Expression is invalid! Cause: {}", e.getMessage());
			return null;
		} catch (Exception e) {
			logger.error("Generally Error while parsing JsonString. Cause: {}", e.getMessage());
			return null;
		}
	}

	@Override
	public String getName() {
		return "JSONPATH";
	}

	@Override
	public IKeyFinder createInstance() {
		return new JsonPathKeyFinder();
	}

	/**
	 * Recursive Method to replace wrong Characters
	 * @param temp the message
	 * @param beginOfSearch the Begin of the search
	 * @param htmlCode the html code to be replaced
	 * @param replacement the character which replace the htmlCode
	 * @return the cleanded StringBuffer
	 */
	private StringBuffer replaceJsonData(StringBuffer temp, int beginOfSearch, String htmlCode, String replacement) {
		if(beginOfSearch > -1) {
			temp.replace(beginOfSearch, beginOfSearch + htmlCode.length(), replacement);
			 return replaceJsonData(temp, temp.indexOf(htmlCode, beginOfSearch+1), htmlCode, replacement);
		} else
			return temp;
	}

	@Override
	public int getTupleCount() {
		return 1;
	}
}
