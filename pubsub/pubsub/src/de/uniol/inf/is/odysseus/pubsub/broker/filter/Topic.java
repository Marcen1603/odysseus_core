package de.uniol.inf.is.odysseus.pubsub.broker.filter;

import java.util.List;

public class Topic {
	private List<Topic> childs;
	private String name;

	public List<Topic> getChilds() {
		return childs;
	}

	public void setChilds(List<Topic> childs) {
		this.childs = childs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
