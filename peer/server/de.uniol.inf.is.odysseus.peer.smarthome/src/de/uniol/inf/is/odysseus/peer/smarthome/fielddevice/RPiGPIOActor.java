package de.uniol.inf.is.odysseus.peer.smarthome.fielddevice;

import java.util.LinkedHashMap;

public class RPiGPIOActor extends Actor {
	private static final long serialVersionUID = 1L;

	public enum State {
		ON, OFF, TOGGLE
	}

	public RPiGPIOActor(String name, String prefix, String postfix) {
		super(name, prefix, postfix);
	}

	public void createLogicRuleWithState(String activityName, State state) {
		RPiGPIOActorLogicRule newRule = new RPiGPIOActorLogicRule(this,
				activityName, getPrefix(), getPostfix());
		newRule.setState(state);
		
		addLogicRuleForActivity(newRule);
	}

	class RPiGPIOActorLogicRule extends LogicRule {
		private static final long serialVersionUID = 1L;
		private State state;

		RPiGPIOActorLogicRule(Actor actor, String activityName, String prefix,
				String postfix) {
			super(actor, activityName, prefix, postfix);

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
					"Activity_imported", activity);
			StringBuilder activityImport = activityImportedStream(
					activityImportedName, activitySourceName);

			map.put(activityImportedName, activityImport.toString());

			// 2. create configuration of the actor, how it will be set if
			// the fieldDevice.getName() is participating at the activity:
			String actorConfigIfParticipateActivity = getNameCombination(
					"Config", activity);
			StringBuilder sbConfig = actorConfigIfParticipateActivity(activity,
					actorConfigIfParticipateActivity);

			map.put(actorConfigIfParticipateActivity, sbConfig.toString());

			String setConfigStreamName = getNameCombination("SetActor",
					activity);
			StringBuilder sbSetConfig = entityConfigStream(
					actorConfigIfParticipateActivity, setConfigStreamName,
					activitySourceName);

			map.put(setConfigStreamName, sbSetConfig.toString());

			// InputSchema: schema=[['PinNumber', 'String'],['PinState',
			// 'String']]
			String entitySetStateStreamName = getNameCombination("rpigpio11",
					activity);
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
			activityImport.append("    predicate=\"ActivityName = 'hot'\"\n");
			activityImport.append("    },\n");
			activityImport.append("    " + activitySourceName + "\n");
			activityImport.append(")\n");
			activityImport.append("\n");
			return activityImport;
		}

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
				String setConfigStreamName, String activitySourceName) {
			StringBuilder sbSetConfig = new StringBuilder();
			sbSetConfig.append("#PARSER PQL\n");
			sbSetConfig.append("#QNAME " + setConfigStreamName + "_query\n");
			sbSetConfig.append("#RUNQUERY\n");
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
			return sbSetConfig;
		}

		private StringBuilder entitySetStateStream(String setConfigStreamName,
				String entitySetStateStreamName) {
			StringBuilder sbEntitySetState = new StringBuilder();
			sbEntitySetState.append("#PARSER PQL\n");
			sbEntitySetState.append("#QNAME " + entitySetStateStreamName
					+ "_query\n");
			sbEntitySetState.append("#RUNQUERY\n");
			sbEntitySetState.append("" + entitySetStateStreamName
					+ " = RPIGPIOSINK({sink='" + entitySetStateStreamName
					+ "', pin=11, pinstate='low'}," + setConfigStreamName
					+ ")\n");
			return sbEntitySetState;
		}

		@Override
		public String getReactionDescription() {
			return "State change:"+getState();
		}
	}
}
