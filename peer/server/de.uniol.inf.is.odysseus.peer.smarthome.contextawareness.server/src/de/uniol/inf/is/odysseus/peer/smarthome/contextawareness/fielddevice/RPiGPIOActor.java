package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice;

import java.util.LinkedHashMap;

public class RPiGPIOActor extends Actor {
	private static final long serialVersionUID = 1L;

	public enum State {
		ON, OFF, TOGGLE
	}

	private int pin;

	public RPiGPIOActor(String name, String prefix, String postfix) {
		super(name, prefix, postfix);
		addActorAction(new RPiGPIOActorAction(State.ON));
		addActorAction(new RPiGPIOActorAction(State.OFF));
		addActorAction(new RPiGPIOActorAction(State.TOGGLE));
	}

	@Override
	public void createLogicRuleWithState(String activityName, State state) {
		RPiGPIOActorLogicRule newRule = new RPiGPIOActorLogicRule(this,
				activityName, getPrefix(), getPostfix());
		newRule.setState(state);
		newRule.setPin(getPin());

		addLogicRuleForActivity(newRule);
	}

	private int getPin() {
		return this.pin;
	}

	@Override
	public boolean createLogicRuleWithAction(String activityName,
			AbstractActorAction actorAction) {
		RPiGPIOActorLogicRule newRule = new RPiGPIOActorLogicRule(this,
				activityName, getPrefix(), getPostfix());
		switch (actorAction.getName()) {
		case "ON":
			newRule.setState(State.ON);
			break;
		case "OFF":
			newRule.setState(State.OFF);

			break;
		case "TOGGLE":
			newRule.setState(State.TOGGLE);

			break;
		default:
			break;
		}

		if (logicRuleExist(newRule)) {
			return false;
		}

		addLogicRuleForActivity(newRule);
		return true;
	}

	@Override
	public boolean logicRuleExist(LogicRule newRule) {
		if (newRule instanceof RPiGPIOActorLogicRule) {
			RPiGPIOActorLogicRule rpiNewRule = (RPiGPIOActorLogicRule) newRule;

			for (LogicRule rule : getLogicRules()) {
				if (rule instanceof RPiGPIOActorLogicRule) {
					RPiGPIOActorLogicRule rpiRule = (RPiGPIOActorLogicRule) rule;
					if (rpiRule.getActivityName().equals(
							rpiNewRule.getActivityName())
							&& rpiRule.getState().equals(rpiNewRule.getState())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	class RPiGPIOActorLogicRule extends LogicRule {
		private static final long serialVersionUID = 1L;
		private State state;
		private int pin;

		RPiGPIOActorLogicRule(Actor actor, String activityName, String prefix,
				String postfix) {
			super(actor, activityName, prefix, postfix);

		}

		public void setPin(int pin) {
			this.pin = pin;
		}

		public void setState(State _state) {
			this.state = _state;
		}

		private State getState() {
			return this.state;
		}

		@Override
		public LinkedHashMap<String, String> getLogicRuleQueries(
				ActivityInterpreter activityInterpreter) {
			LinkedHashMap<String, String> map = new LinkedHashMap<>();

			String peerName = activityInterpreter.getSensor().getSmartDevice().getPeerName();
			String activity = activityInterpreter.getActivityName();
			String activitySourceName = activityInterpreter
					.getActivitySourceName();

			// ////

			// logicRules = new HashMap<String, LinkedHashMap<String,
			// String>>();

			// super.get

			// 1. Import data stream with activities of the field device:
			// fieldDevice.getName()
			// and listen for the activity: "hot"
			String activityImportedName = getNameCombination(
					"Activity_imported", activity, peerName);
			StringBuilder activityImport = activityImportedStream(
					activityImportedName, activitySourceName);

			map.put(activityImportedName, activityImport.toString());

			// 2. create configuration of the actor, how it will be set if
			// the fieldDevice.getName() is participating at the activity:
			String actorConfigIfParticipateActivity = getNameCombination(
					"Config", activity, peerName);
			//StringBuilder sbConfig = actorConfigIfParticipateActivity(activity,
			//		actorConfigIfParticipateActivity);

			//map.put(actorConfigIfParticipateActivity, sbConfig.toString());

			String setConfigStreamName = getNameCombination("SetActor",
					activity, peerName);
			StringBuilder sbSetConfig = entityConfigStream(
					actorConfigIfParticipateActivity, setConfigStreamName,
					activitySourceName, activity);

			map.put(setConfigStreamName, sbSetConfig.toString());

			// InputSchema: schema=[['PinNumber', 'String'],['PinState',
			// 'String']]
			String entitySetStateStreamName = getNameCombination(getName(),
					activity, peerName);
			StringBuilder sbEntitySetState = entitySetStateStream(
					setConfigStreamName, entitySetStateStreamName);

			map.put(entitySetStateStreamName, sbEntitySetState.toString());

			// ///

			return map;
		}

		private StringBuilder activityImportedStream(
				String activityImportedName, String activitySourceName) {
			StringBuilder activityImport = new StringBuilder();
			activityImport.append("#PARSER PQL\n");
			activityImport
					.append("#QNAME " + activityImportedName + "_query\n");
			activityImport.append("#RUNQUERY\n");
			activityImport.append(activityImportedName + " := SELECT({\n");
			activityImport.append("    predicate=\"ActivityName = '"+getActivityName()+"'\"\n");
			activityImport.append("    },\n");
			activityImport.append("    " + activitySourceName + "\n");
			activityImport.append(")\n");
			activityImport.append("\n");
			return activityImport;
		}

		//TODO: 
		@SuppressWarnings("unused")
		private StringBuilder actorConfigIfParticipateActivity(String activity,
				String actorConfigIfParticipateActivity) {

			String setState;
			if (getState().equals(State.ON) || getState().equals(State.TOGGLE)) {
				setState = "1";
			} else {
				setState = "0";
			}

			StringBuilder sbConfig = new StringBuilder();
			sbConfig.append("#PARSER PQL\n");
			sbConfig.append("#QNAME " + actorConfigIfParticipateActivity
					+ "_query\n");
			sbConfig.append("#RUNQUERY\n");
			sbConfig.append("    " + actorConfigIfParticipateActivity
					+ " := ACCESS({\n");
			sbConfig.append("    transport = 'activityconfiguration',\n");
			sbConfig.append("    source = '" + actorConfigIfParticipateActivity
					+ "',\n");
			sbConfig.append("    datahandler = 'Tuple',\n");
			sbConfig.append("    wrapper = 'GenericPull',\n");
			sbConfig.append("    protocol='none',\n");
			sbConfig.append("    options=[\n");
			sbConfig.append("      ['entity', '" + setState + "'],\n");
			sbConfig.append("      ['activity', '" + activity + "']\n");
			sbConfig.append("    ],\n");
			sbConfig.append("    schema=[\n");
			sbConfig.append("      ['ConfigEntityName', 'String'],\n");
			sbConfig.append("      ['ConfigActivityName', 'String']\n");
			sbConfig.append("    ]\n");
			sbConfig.append("  }\n");
			sbConfig.append(")\n");
			sbConfig.append("\n");
			sbConfig.append("\n");
			return sbConfig;
		}

		private StringBuilder entityConfigStream(
				String actorConfigIfParticipateActivity,
				String setConfigStreamName, String activitySourceName, String activity) {
			StringBuilder sbSetConfig = new StringBuilder();
			sbSetConfig.append("#PARSER PQL\n");
			sbSetConfig.append("#QNAME " + setConfigStreamName + "_query\n");
			sbSetConfig.append("#RUNQUERY\n");
			sbSetConfig.append(""+setConfigStreamName+" := RENAME({\n");
			sbSetConfig.append("                        aliases = ['PinNumber', 'PinState']\n");                                                             
			sbSetConfig.append("                      },MAP({\n");
			sbSetConfig.append("                      expressions = ['EntityName','concat(substring(toString(ActivityName),0,0),1)']\n");                                                                       
			sbSetConfig.append("                    },SELECT({\n");
			sbSetConfig.append("                          predicate=\"ActivityName = '"+activity+"'\"\n");                                                                                                                                                                          
			sbSetConfig.append("                        },\n");
			sbSetConfig.append("                        "+activitySourceName+"\n");
			sbSetConfig.append("                      )))\n");
			
			/*
			sbSetConfig.append("" + setConfigStreamName + " := RENAME({\n");
			sbSetConfig.append("        aliases = ['PinNumber', 'PinState']\n");
			sbSetConfig.append("    },\n");
			sbSetConfig.append("    PROJECT({\n");
			sbSetConfig.append("        attributes = "
					+ "    ['ActivityName','ConfigEntityName']\n");
			sbSetConfig.append("    },\n");
			sbSetConfig.append("        JOIN({\n");
			sbSetConfig.append("            predicate = '" + activitySourceName
					+ ".ActivityName = " + actorConfigIfParticipateActivity
					+ ".ConfigActivityName'\n");
			sbSetConfig.append("        },\n");
			sbSetConfig.append("ELEMENTWINDOW({size = 1}, "
					+ activitySourceName + "),\n");
			sbSetConfig.append("ELEMENTWINDOW({size = 1}, "
					+ actorConfigIfParticipateActivity + ")\n");
			sbSetConfig.append("        )\n");
			sbSetConfig.append("    )\n");
			sbSetConfig.append(")\n");
			sbSetConfig.append("\n");
			*/
			
			
			
			
			
			
			return sbSetConfig;
		}

		private StringBuilder entitySetStateStream(String setConfigStreamName,
				String entitySetStateStreamName) {
			StringBuilder sbEntitySetState = new StringBuilder();
			sbEntitySetState.append("#PARSER PQL\n");
			sbEntitySetState.append("#QNAME " + entitySetStateStreamName
					+ "_query\n");
			sbEntitySetState.append("#RUNQUERY\n");
			//sbEntitySetState.append("" + entitySetStateStreamName
			//		+ " = RPIGPIOSINK({sink='" + entitySetStateStreamName
			//		+ "', pin="+getPin()+", pinstate='low'}," + setConfigStreamName
			//		+ ")\n");
			
			sbEntitySetState.append("" + entitySetStateStreamName +" = RPIGPIOSINK({pin="+getPin()+", pinstate='low'}," + setConfigStreamName+")");
			
			return sbEntitySetState;
		}

		private int getPin() {
			return this.pin;
		}

		@Override
		public String getReactionDescription() {
			return "pin:"+getPin()+" State change:" + getState();
		}
	}

	class RPiGPIOActorAction extends AbstractActorAction {
		private static final long serialVersionUID = 1L;
		private State name;

		RPiGPIOActorAction(State state) {
			super();
			this.name = state;
		}

		@Override
		public String getName() {
			String _name = this.name.toString();
			return _name;
		}

	}

	public void setPin(int pin) {
		this.pin = pin;
	}

}
