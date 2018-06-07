package de.uniol.inf.is.odysseus.anomalydetection.rest.datatypes;

public class QueryInformation {

	private String content;
	private String parser;

	public QueryInformation(String content, String parser) {
		super();
		this.content = content;
		this.parser = parser;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getParser() {
		return parser;
	}

	public void setParser(String parser) {
		this.parser = parser;
	}

}
