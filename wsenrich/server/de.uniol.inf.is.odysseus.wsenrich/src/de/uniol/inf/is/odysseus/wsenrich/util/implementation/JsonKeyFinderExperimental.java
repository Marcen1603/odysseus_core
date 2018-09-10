package de.uniol.inf.is.odysseus.wsenrich.util.implementation;

import de.uniol.inf.is.odysseus.wsenrich.util.interfaces.IKeyFinder;

public class JsonKeyFinderExperimental implements IKeyFinder {
	
	/**
	 * Static Variable for the Key Value Delmiter
	 */
	private static final String KEY_VALUE_DELMITER = ":";
	
	/**
	 * Static Variable for the end of Element Data
	 */
	private static final String END_OF_ELEMENT = ",";
	
	/**
	 * The searched Element
	 */
	private String search;
	
	/**
	 * The Message
	 */
	private StringBuffer message;
	
	/**
	 * The value found (or not) in the message
	 */
	private Object value;
	
	public JsonKeyFinderExperimental() {
		//Needed für IKeyFinderRegistry
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
		return this.message.toString();
	}

	@Override
	public void setMessage(String Message, String charset, boolean multiTupleOutput) {
		this.message = new StringBuffer(Message);
	}

	@Override
	public Object getValueOf(String search, boolean keyValue, int tupleCount) {
		
		StringBuffer temp = new StringBuffer();
		if(!this.search.equals(search)) {
			this.search = search;
		}
		
		int match = this.message.indexOf(this.search);
		
		if(match == -1) {
			return null;
		}
		if(keyValue) {
			temp.append(search + " : ");
			int endOfElement = this.message.indexOf(KEY_VALUE_DELMITER.toString(), match);
			int endofData = this.message.indexOf(END_OF_ELEMENT, endOfElement);
			if(endofData < 1) {  //the last Element of a Json file do not have a , as a seperator
				endofData = this.message.indexOf("}", endOfElement);
			}
			temp.append(this.message.substring(endOfElement + 1, endofData));
			replaceJsonData(temp, "\"", "");	
		} else {
			int endOfElement = this.message.indexOf(KEY_VALUE_DELMITER.toString(), match);
			int endofData = this.message.indexOf(END_OF_ELEMENT, endOfElement);
			if(endofData < 1) { //the last Element of a Json file do not have a , as a seperator
				endofData = this.message.indexOf("}", endOfElement);
			}
			temp.append(this.message.substring(endOfElement + 1, endofData));
			replaceJsonData(temp, "\"", "");
		}
		this.value = temp;
		return this.value;
	}

	@Override
	public String getName() {
		return "JSONEXPERIMENTAL";
	}

	@Override
	public IKeyFinder createInstance() {
		return new JsonKeyFinderExperimental();
	}
	
	/**
	 * Recursive Method to replace wrong Characters
	 * @param temp the message
	 * @param htmlCode the html code to be replaced
	 * @param replacement the character which replace the htmlCode
	 * @return
	 */
	private StringBuffer replaceJsonData(StringBuffer temp, String htmlCode, String replacement) {
		int match = temp.indexOf(htmlCode);
		if(match > -1) {
			temp.replace(match, match + htmlCode.length(), replacement);
			 return replaceJsonData(temp, htmlCode, replacement);	
		} else 
			return temp;
	}

	@Override
	public int getTupleCount() {
		return 1;
	}
}
