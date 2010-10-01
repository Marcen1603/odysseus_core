package de.uniol.inf.is.odysseus.planmanagement.executor.eventhandling.planmodification.event;

import de.uniol.inf.is.odysseus.event.IEventType;

public enum PlanModificationEventType implements IEventType {
	PLAN_REOPTIMIZE,QUERIES_ADD,QUERY_ADDED,QUERY_REMOVE,QUERY_START,QUERY_STOP, QUERY_REOPTIMIZE 
}
