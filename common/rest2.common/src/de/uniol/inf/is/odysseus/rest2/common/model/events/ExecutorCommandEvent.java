package de.uniol.inf.is.odysseus.rest2.common.model.events;

import java.util.Collection;

public class ExecutorCommandEvent {

	public String type;
	public Collection<Integer> createdQueryIds;

	public ExecutorCommandEvent(String type, Collection<Integer> createdQueryIds) {
		this.type = type;
		this.createdQueryIds = createdQueryIds;
	}

}
