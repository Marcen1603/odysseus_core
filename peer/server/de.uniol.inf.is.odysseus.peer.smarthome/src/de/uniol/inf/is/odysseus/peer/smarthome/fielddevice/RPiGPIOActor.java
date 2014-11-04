package de.uniol.inf.is.odysseus.peer.smarthome.fielddevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RPiGPIOActor extends Actor {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory
			.getLogger(RPiGPIOActor.class);

	private HashMap<String, State> stateForActivity;
	private boolean initWasRun = false;

	public enum State {
		ON, OFF, TOGGLE
	}

	public RPiGPIOActor(String name, String prefix, String postfix) {
		super(name, prefix, postfix);
	}

	/**
	 * This method adds activities to listen for and change the state if it appears.
	 * More then one activity are handled like an "OR" relation.
	 * @param activity
	 * @param state
	 */
	public void addActivityForState(String activity, State state) {
		addPossibleActivityName(activity);
		this.getStateForActivity().put(activity, state);
	}

	private HashMap<String, State> getStateForActivity() {
		if (this.stateForActivity == null) {
			this.stateForActivity = new HashMap<String, State>();
		}
		return this.stateForActivity;
	}

	@Override
	protected void initLogicRules() {
		if (!initWasRun) {
			for (Entry<String, State> entry : getStateForActivity().entrySet()) {
				String activity = entry.getKey();
				State state = entry.getValue();

				LOG.debug("---activity:" + activity + " state:" + state
						+ "----------");

				// logicRules = new HashMap<String, LinkedHashMap<String,
				// String>>();

				// super.get

				// 1. Import data stream with activities of the field device:
				// fieldDevice.getName()
				// and listen for the activity: "hot"
				String activityImportedName = getPrefix() + "_" + getName()
						+ "_Activity_imported";
				StringBuilder activityImport = new StringBuilder();
				activityImport.append("#PARSER PQL\n");
				activityImport.append("#QNAME " + activityImportedName
						+ "_query\n");
				activityImport.append("#RUNQUERY\n");
				activityImport.append(activityImportedName + " := SELECT({\n");
				activityImport
						.append("    predicate=\"ActivityName = 'hot'\"\n");
				activityImport.append("    },\n");
				activityImport.append("    " + getActivitySourceName() + "\n");
				activityImport.append(")\n");
				activityImport.append("\n");

				String setState;
				if (state.equals(State.ON) || state.equals(State.TOGGLE)) {
					setState = "1";
				} else {
					setState = "0";
				}

				// 2. create configuration of the actor, how it will be set if
				// the fieldDevice.getName() is participating at the activity:
				String actorConfigIfParticipateActivity = getPrefix() + "_"
						+ getName() + "_Config";
				StringBuilder sbConfig = new StringBuilder();
				sbConfig.append("#PARSER PQL\n");
				sbConfig.append("#QNAME " + actorConfigIfParticipateActivity
						+ "_query\n");
				sbConfig.append("#RUNQUERY\n");
				sbConfig.append("    " + actorConfigIfParticipateActivity
						+ " := ACCESS({\n");
				sbConfig.append("    transport = 'activityconfiguration',\n");
				sbConfig.append("    source = '"
						+ actorConfigIfParticipateActivity + "',\n");
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

				String setConfigStreamName = getPrefix() + "_" + getName()
						+ "_SetActor_" + getName();
				StringBuilder sbSetConfig = new StringBuilder();
				sbSetConfig.append("#PARSER PQL\n");
				sbSetConfig
						.append("#QNAME " + setConfigStreamName + "_query\n");
				sbSetConfig.append("#RUNQUERY\n");
				sbSetConfig.append("" + setConfigStreamName + " := RENAME({\n");
				sbSetConfig
						.append("        aliases = ['PinNumber', 'PinState']\n");
				sbSetConfig.append("    },\n");
				sbSetConfig.append("    PROJECT({\n");
				sbSetConfig.append("        attributes = "
						+ "    ['ActivityName','ConfigEntityName']\n");
				sbSetConfig.append("    },\n");
				sbSetConfig.append("        JOIN({\n");
				sbSetConfig.append("            predicate = '"
						+ getActivitySourceName() + ".ActivityName = "
						+ actorConfigIfParticipateActivity
						+ ".ConfigActivityName'\n");
				sbSetConfig.append("        },\n");
				sbSetConfig.append("ELEMENTWINDOW({size = 1}, "
						+ getActivitySourceName() + "),\n");
				sbSetConfig.append("ELEMENTWINDOW({size = 1}, "
						+ actorConfigIfParticipateActivity + ")\n");
				sbSetConfig.append("        )\n");
				sbSetConfig.append("    )\n");
				sbSetConfig.append(")\n");
				sbSetConfig.append("\n");

				// InputSchema: schema=[['PinNumber', 'String'],['PinState',
				// 'String']]
				String entitySetStateStreamName = getPrefix() + "_rpigpio11_";
				StringBuilder sbEntitySetState = new StringBuilder();
				sbEntitySetState.append("#PARSER PQL\n");
				sbEntitySetState.append("#QNAME " + entitySetStateStreamName
						+ "_query\n");
				sbEntitySetState.append("#RUNQUERY\n");
				sbEntitySetState.append("" + entitySetStateStreamName
						+ " = RPIGPIOSINK({sink='" + entitySetStateStreamName
						+ "', pin=11, pinstate='low'}," + setConfigStreamName
						+ ")\n");

				// ///
				// 1. activityImportedName, activityImport.toString()
				// 2. actorConfigIfParticipateActivity, sbConfig.toString()
				// 3. setConfigStreamName, sbSetConfig.toString()
				// 4. entitySetStateStreamName, sbEntitySetState.toString()
				LinkedHashMap<String, String> logicRuleQueries = new LinkedHashMap<String, String>();
				logicRuleQueries.put(activityImportedName,
						activityImport.toString());
				logicRuleQueries.put(actorConfigIfParticipateActivity,
						sbConfig.toString());
				logicRuleQueries.put(setConfigStreamName,
						sbSetConfig.toString());
				logicRuleQueries.put(entitySetStateStreamName,
						sbEntitySetState.toString());

				
//
				super.addLogicRuleForActivity(activity, logicRuleQueries);
			}
			initWasRun = true;
		}
	}

	/**
	 * Activities added over this method are handled like an "AND"-relation.
	 * @param manyActivities
	 * @param toggle
	 */
	public void addActivitiesForState(ArrayList<String> manyActivities,
			State toggle) {
		// TODO Auto-generated method stub
		
	}
}
