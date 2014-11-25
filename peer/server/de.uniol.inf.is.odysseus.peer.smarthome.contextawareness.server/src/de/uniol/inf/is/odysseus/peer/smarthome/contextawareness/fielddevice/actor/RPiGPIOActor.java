package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.actor;

import java.util.LinkedHashMap;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.activityinterpreter.ActivityInterpreter;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.logicrule.AbstractLogicRule;

public class RPiGPIOActor extends AbstractActor {
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
		newRule.setPin(getPin());
		
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
	public boolean logicRuleExist(AbstractLogicRule newRule) {
		if (newRule instanceof RPiGPIOActorLogicRule) {
			RPiGPIOActorLogicRule rpiNewRule = (RPiGPIOActorLogicRule) newRule;

			for (AbstractLogicRule rule : getLogicRules()) {
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

	class RPiGPIOActorLogicRule extends AbstractLogicRule {
		private static final long serialVersionUID = 1L;
		private State state;
		private int pin;
		private String activitySourceName;

		RPiGPIOActorLogicRule(AbstractActor actor, String activityName, String prefix,
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
			System.out.println("  !!!!!!!!  ");
			
			LinkedHashMap<String, String> map = new LinkedHashMap<>();

			String peerName = activityInterpreter.getSensor().getSmartDevice().getPeerName();
			String activity = activityInterpreter.getActivityName();
			String activitySourceName = activityInterpreter
					.getActivitySourceName();

			//TODO:
			String setConfigStreamName = getNameCombination("SetActor",
					activity, peerName);
			@SuppressWarnings("unused")
			StringBuilder sbSetConfig = entityConfigStream(setConfigStreamName,
					activitySourceName, activity);

			//map.put(setConfigStreamName, sbSetConfig.toString());

			
			String entitySetStateStreamName = getNameCombination(getName(),
					activity, peerName);
			StringBuilder sbEntitySetState = entitySetStateStream(entitySetStateStreamName, getActivitySourceName(), getActivityName());

			map.put(entitySetStateStreamName, sbEntitySetState.toString());
			
			return map;
		}
		
		@Override
		public LinkedHashMap<String, String> getLogicRulesQueriesWithActivitySourceName() {
			if(getActivitySourceName()==null || getActivitySourceName().equals("")){
				return null;
			}
			
			LinkedHashMap<String, String> map = new LinkedHashMap<>();
			
			String peerName = getActor().getSmartDevice().getPeerName();
			
			//TODO:
			///String setConfigStreamName = getNameCombination("SetActor",
			///		getActivityName(), peerName);
			///@SuppressWarnings("unused")
			///StringBuilder sbSetConfig = entityConfigStream(setConfigStreamName,
			///		getActivitySourceName(), getActivityName());

			//map.put(setConfigStreamName, sbSetConfig.toString());

			
			String entitySetStateStreamName = getNameCombination(getName(),
					getActivityName(), peerName);
			StringBuilder sbEntitySetState = entitySetStateStream(
					entitySetStateStreamName, getActivitySourceName(), getActivityName());

			map.put(entitySetStateStreamName, sbEntitySetState.toString());
			
			return map;
		}

		private StringBuilder entityConfigStream(String setConfigStreamName, String activitySourceName, String activity) {
			String v = "";
			switch (getState()) {
			case ON:
				v = "1";
				break;
			case OFF:
				v = "0";
				break;
			default:
				break;
			}
			
			
			StringBuilder sbSetConfig = new StringBuilder();
			sbSetConfig.append("#PARSER PQL\n");
			sbSetConfig.append("#QNAME " + setConfigStreamName + "_query\n");
			sbSetConfig.append("#RUNQUERY\n");
			sbSetConfig.append(""+setConfigStreamName+" := RENAME({\n");
			sbSetConfig.append("                        aliases = ['PinNumber', 'PinState']\n");                                                             
			sbSetConfig.append("                      },MAP({\n");
			sbSetConfig.append("                      expressions = ['EntityName','concat(substring(toString(ActivityName),0,0),"+v+")']\n");                                                                       
			sbSetConfig.append("                    },SELECT({\n");
			sbSetConfig.append("                          predicate=\"ActivityName = '"+activity+"'\"\n");                                                                                                                                                                          
			sbSetConfig.append("                        },\n");
			sbSetConfig.append("                        "+activitySourceName+"\n");
			sbSetConfig.append("                      )))\n");
			sbSetConfig.append("\n");
			
			System.out.println("query:\n"+sbSetConfig.toString()+"\n");
			
			return sbSetConfig;
		}

		private StringBuilder entitySetStateStream(
				String entitySetStateStreamName, String activitySourceName, String activity) {
			
			String v = "";
			switch (getState()) {
			case ON:
				v = "1";
				break;
			case OFF:
				v = "0";
				break;
			default:
				break;
			}
			
			
			StringBuilder sbEntitySetState = new StringBuilder();
			sbEntitySetState.append("#PARSER PQL\n");
			sbEntitySetState.append("#QNAME " + entitySetStateStreamName
					+ "_query\n");
			sbEntitySetState.append("#RUNQUERY\n");
			//sbEntitySetState.append("" + entitySetStateStreamName
			//		+ " = RPIGPIOSINK({sink='" + entitySetStateStreamName
			//		+ "', pin="+getPin()+", pinstate='low'}," + setConfigStreamName
			//		+ ")\n");
			
			//sbEntitySetState.append("" + entitySetStateStreamName +" = RPIGPIOSINK({pin="+getPin()+", pinstate='low'}," + setConfigStreamName+")");
			
			
			sbEntitySetState.append("" + entitySetStateStreamName +" = RPIGPIOSINK({pin="+getPin()+", pinstate='low'},RENAME({\n");
			sbEntitySetState.append("    aliases = ['PinNumber', 'PinState']\n");
			sbEntitySetState.append("  },MAP({\n");
			sbEntitySetState.append("  expressions = ['EntityName','concat(substring(toString(ActivityName),0,0),"+v+")']\n");
			sbEntitySetState.append("},SELECT({\n");
			sbEntitySetState.append("      predicate=\"ActivityName = '"+activity+"'\"\n");
			sbEntitySetState.append("    },\n");
			sbEntitySetState.append("    "+activitySourceName+"\n");
			sbEntitySetState.append("  ))))\n");
			
			
			/*
			sbEntitySetState.append("" + entitySetStateStreamName +" := RENAME({\n");
			sbEntitySetState.append("    aliases = ['PinNumber', 'PinState']\n");
			sbEntitySetState.append("  },MAP({\n");
			sbEntitySetState.append("  expressions = ['EntityName','concat(substring(toString(ActivityName),0,0),"+v+")']\n");
			sbEntitySetState.append("},SELECT({\n");
			sbEntitySetState.append("      predicate=\"ActivityName = '"+activity+"'\"\n");
			sbEntitySetState.append("    },\n");
			sbEntitySetState.append("    "+activitySourceName+"\n");
			sbEntitySetState.append("  )))\n");
			*/
			
			sbEntitySetState.append("\n");
			
			return sbEntitySetState;
		}

		private int getPin() {
			return this.pin;
		}

		@Override
		public String getReactionDescription() {
			return "pin:"+getPin()+" State change:" + getState();
		}

		@Override
		public void setActivitySourceName(
				String activitySourceName) {
			this.activitySourceName = activitySourceName;
		}
		
		@Override
		public String getActivitySourceName() {
			return this.activitySourceName;
		}
	}


	public void setPin(int pin) {
		this.pin = pin;
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
	
}
