package de.uniol.inf.is.odysseus.wsenrich.util;

public class JsonKeyFinderExperimental implements IKeyFinder {
	
	/**
	 * Static Variable for the Definition of Keys and Values
	 */
	private static final String ELEMENT_DEFINER = "\"";
	
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
	public void setMessage(String Message) {
		this.message = new StringBuffer(Message);
	}

	@Override
	public Object getValueOf(String search, boolean keyValue) {
		
		if(!this.search.equals(search)) {
			this.search = search;
		}
		
		int match = this.message.indexOf(this.search);
		
		if(match == -1) {
			//No Data found
			return null;
		} else {
			int endOfElement = this.message.indexOf(ELEMENT_DEFINER.toString(), match);
			int startOfData = this.message.indexOf(ELEMENT_DEFINER.toString(), endOfElement + 1);
			int endOfData = this.message.indexOf(ELEMENT_DEFINER, startOfData + 1);
			String temp = this.message.substring(startOfData + 1, endOfData);
			this.value = temp;
		}
		return this.value;
	}

	@Override
	public String getName() {
		return "JSONExperimental";
	}

	@Override
	public IKeyFinder createInstance() {
		return new JsonKeyFinderExperimental();
	}

}
