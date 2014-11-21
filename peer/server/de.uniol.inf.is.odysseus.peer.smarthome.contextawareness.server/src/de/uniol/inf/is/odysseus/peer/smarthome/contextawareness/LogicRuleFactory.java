package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.Actor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.AbstractActorAction;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.LogicRule;

public class LogicRuleFactory {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	
	public static LogicRule createLogicRule(String activityName, Actor actor,
			AbstractActorAction actorAction) {
		LOG.debug("createLogicRule activityName:"+activityName+" actor:"+actor+" actorAction:"+actorAction.getName());
		//TODO:
		
		
		
		
		
		
		return null;
	}
}
