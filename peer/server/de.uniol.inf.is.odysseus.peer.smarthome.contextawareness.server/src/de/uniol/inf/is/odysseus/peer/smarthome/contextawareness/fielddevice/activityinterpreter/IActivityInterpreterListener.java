package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.activityinterpreter;

public interface IActivityInterpreterListener {
	void activityInterpreterAdded(AbstractActivityInterpreter activityInterpreter);
	void activityInterpreterRemoved(AbstractActivityInterpreter activityInterpreter);
}
