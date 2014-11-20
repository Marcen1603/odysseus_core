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


			String setConfigStreamName = getNameCombination("SetActor",
					activity, peerName);
			StringBuilder sbSetConfig = entityConfigStream(setConfigStreamName,
					activitySourceName, activity);

			map.put(setConfigStreamName, sbSetConfig.toString());

			
			String entitySetStateStreamName = getNameCombination(getName(),
					activity, peerName);
			StringBuilder sbEntitySetState = entitySetStateStream(
					setConfigStreamName, entitySetStateStreamName);

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
