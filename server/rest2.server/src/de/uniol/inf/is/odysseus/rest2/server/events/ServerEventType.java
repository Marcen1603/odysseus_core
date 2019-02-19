package de.uniol.inf.is.odysseus.rest2.server.events;

public enum ServerEventType {

	QUERY_ADDED("Event when a query was added."), ERROR_EVENT("Event when an error during the query execution occurs."),
	PLAN_MODIFICATION("Event when a query plan is modified, e.g., started, stopped, etc."),
	SCHEDULER_MANAGER(
			"Event when the scheduler is started or stopped (typically start and stop of the Odysseus instance)"),
	EXECUTOR_COMMAND(
			"Event from the executor, typically when a query was added. You should typically use QUERY_ADDED instead."),
	COMPILER("Events when a parser, transformation, rewrite rule or plan generator is bound."),
	SESSION("Simple event on login and logout."), DATADICTIONARY("Event when the data dictionary changed."),
	USER("Event when some user is changed, e.g. created, changed permission, etc."),
	QUERY("Event when a query was added, removed, started, stopped, etc."),
	SCHEDULING("Event when the scheduler is started or stopped (typically start and stop of the Odysseus instance)");

	private final String description;

	ServerEventType(String description) {
		this.description = description;
	}

	public String description() {
		return this.description;
	}
}
