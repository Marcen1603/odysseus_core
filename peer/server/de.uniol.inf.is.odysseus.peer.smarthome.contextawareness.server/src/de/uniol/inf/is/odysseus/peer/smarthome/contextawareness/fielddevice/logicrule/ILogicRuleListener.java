package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.logicrule;

public interface ILogicRuleListener {
	public void logicRuleAdded(AbstractLogicRule rule);
	public void logicRuleRemoved(AbstractLogicRule rule);
}
