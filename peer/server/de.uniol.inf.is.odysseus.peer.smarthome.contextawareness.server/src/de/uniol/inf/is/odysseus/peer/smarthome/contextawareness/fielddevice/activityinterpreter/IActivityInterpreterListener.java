package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.activityinterpreter;

public interface IActivityInterpreterListener {
	void activityInterpreterAdded(ActivityInterpreter activityInterpreter);
	void activityInterpreterRemoved(ActivityInterpreter activityInterpreter);
}
