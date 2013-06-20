package de.uniol.inf.is.odysseus.pubsub.broker.filter;

import java.util.ArrayList;
import java.util.List;

public class Topic {
	private List<Topic> childs;
	private String name;

	public Topic(String name) {
		this.name = name;
		this.childs = new ArrayList<Topic>();
	}

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

	public boolean isHierarchical() {
		if (childs.isEmpty()) {
			return false;
		}
		return true;
	}

	public boolean equals(Object obj) {
		Topic other;
		if (obj instanceof Topic) {
			other = (Topic) obj;
		} else {
			return false;
		}
		if (this.name.toLowerCase().equals(other.name.toLowerCase()) && this.getChilds().size() == other.getChilds().size()){
			return true;
		}
		return false;

	}

}
