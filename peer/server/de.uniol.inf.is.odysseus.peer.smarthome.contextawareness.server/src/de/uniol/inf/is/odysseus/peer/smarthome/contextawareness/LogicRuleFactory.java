package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.actor.AbstractActorAction;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.actor.AbstractActor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.logicrule.AbstractLogicRule;

public class LogicRuleFactory {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	
	public static AbstractLogicRule createLogicRule(String activityName, AbstractActor actor,
			AbstractActorAction actorAction) {
		LOG.debug("createLogicRule activityName:"+activityName+" actor:"+actor+" actorAction:"+actorAction.getName());
		//TODO:
		
		
		
		
		
		
		return null;
	}
}
