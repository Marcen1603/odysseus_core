package de.uniol.inf.is.odysseus.eca.plugin;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EcaPluginImpl {
	private static final Logger LOG = LoggerFactory.getLogger(EcaPluginImpl.class);
	private static ArrayList<Query> finalQueries;
	private static ArrayList<Query> mapQueries;
	private static ArrayList<EcaRuleObj> map;
	private static ArrayList<Query> joinQueries;
	private static ArrayList<String> systemFunctions;
	private static ArrayList<String> queryFunctions;
	private static StringBuffer query;
	private static String finalName;
	private static int windowSize;
	private static String source;
	private static Integer timer;

	protected static String buildQueries(ArrayList<EcaRuleObj> ruleSet) {

		finalQueries = new ArrayList<>();
		mapQueries = new ArrayList<>();
		map = new ArrayList<>();
		joinQueries = new ArrayList<>();
		systemFunctions = new ArrayList<>();
		queryFunctions = new ArrayList<>();
		query = new StringBuffer();
		windowSize = ruleSet.get(0).getWindowSize();
		timer = 2000;

		if (ruleSet.get(0).getRulesource().equals("QueryEvent")) {
			source = "eca_planmodification";
		} else if (ruleSet.get(0).getRulesource().equals("TimerEvent")) {
			source = "eca_time";
		} else {
			source = ruleSet.get(0).getRulesource();
		}
		finalName = source;

		if (!(ruleSet.get(0).getTimerIntervall() < 1)) {
			timer = ruleSet.get(0).getTimerIntervall();
		}
		/*
		 * ruleSet = eine Regel Jedes AdmissionCLRule-Objekt enthaelt: den Namen
		 * der Regel die Quelle, auf die sich die Regel bezieht, die Aktion,
		 * welche ausgeloest werden soll und einen Bedingungstyp sowie die
		 * zugehoerige Bedingung selbst
		 */

		// Die Regel enthaelt verschiedene Bedingungstypen!
		for (int i = 0; i < ruleSet.size(); i++) {
			// unterschiedliche Behandlung nach Typ!
			EcaRuleObj current = ruleSet.get(i);
			if (current.getSystemFunctions().size() > 0 && systemFunctions.size() < 1) {
				systemFunctions = current.getSystemFunctions();
			}
			if (current.getQueryFunctions().size() > 0 && queryFunctions.size() < 1) {
				queryFunctions = current.getQueryFunctions();
			}
			if (current.getType().equals("source")) {
				buildSourceAndSystemQuery(current, i);
			} else if (current.getType().equals("free")) {
				buildFreeQuery(current, i);
			} else if (current.getType().equals("map")) {
				map.add(current);
				// buildMapQuery(current, i);
			} else {
				LOG.error("Invalid Source-Type! " + current.getType());
			}
		}
		if (systemFunctions.size() > 0) {
			addSystemLoadFunctions(systemFunctions);
		}

		if (finalQueries.size() >= 2) {
			// die ersten beiden Queries Joinen
			joinQueries(finalQueries.get(0), finalQueries.get(1));

			for (int i = 2; i < finalQueries.size(); i++) {
				joinQueries(joinQueries.get(i - 2), finalQueries.get(i));
			}
		}

		if (map.size() > 0 && finalQueries.size() < 1 && joinQueries.size() < 1) {
			// Nur Map-Queries
			for (int i = 0; i < map.size(); i++) {
				if (mapQueries.size() < 1) {
					buildMapQuery(map.get(i), null, i);
				} else {
					buildMapQuery(map.get(i), mapQueries.get(mapQueries.size() - 1), i);
				}

			}

		} else if (map.size() > 0 && finalQueries.size() > 0 && joinQueries.size() < 1) {
			// Kein Join
			for (int i = 0; i < map.size(); i++) {
				if (mapQueries.size() < 1) {
					buildMapQuery(map.get(i), finalQueries.get(finalQueries.size() - 1), i);
				} else {
					buildMapQuery(map.get(i), mapQueries.get(mapQueries.size() - 1), i);
				}

			}

		} else if (map.size() > 0 && joinQueries.size() > 0) {
			// Mit Join
			for (int i = 0; i < map.size(); i++) {
				if (mapQueries.size() < 1) {
					buildMapQuery(map.get(i), joinQueries.get(joinQueries.size() - 1), i);
				} else {
					buildMapQuery(map.get(i), mapQueries.get(mapQueries.size() - 1), i);
				}

			}

		} else {
			// Keine Map-Queries
		}

		for (int i = 0; i < finalQueries.size(); i++) {
			query.append(finalQueries.get(i).getQuery());
			finalName = finalQueries.get(i).getName();
		}
		for (int i = 0; i < joinQueries.size(); i++) {
			query.append(joinQueries.get(i).getQuery());
			finalName = joinQueries.get(i).getName();
		}
		for (int i = 0; i < mapQueries.size(); i++) {
			query.append(mapQueries.get(i).getQuery());
			finalName = mapQueries.get(i).getName();
		}
		if (queryFunctions.size() > 0) {
			query.append(queryExists(finalName, queryFunctions));
			finalName = "eca_exQueryNotNull" + (queryFunctions.size() - 4 );
		}

		return addAction(query.toString(), finalName, ruleSet.get(0).getRuleAction());

	}

	private static void buildSourceAndSystemQuery(EcaRuleObj rule, Integer count) {
		StringBuffer build = new StringBuffer();
		build.setLength(0);
		String source;
		if (rule.getType().equals("system")) {
			source = "eca_systemload";
		} else if (rule.getRulesource().equals("QueryEvent")) {
			source = "eca_planmodification";
		} else if (rule.getRulesource().equals("TimerEvent")) {
			build.append("eca_time = TIMER({period = " + timer + ", timefromstart = true, source = 'source'}) ");
			source = "eca_time";
		} else {
			source = rule.getRulesource();
		}

		build.append("eca_" + (rule.getRuleName() + count) + " = SELECT({PREDICATE = '" + rule.getRuleCondition()
				+ "'}, " + source + ") ");

//		build.append("ecaW_" + (rule.getRuleName() + count) + " = TIMEWINDOW({SIZE=" + windowSize + ", SLIDE="
//				+ windowSize + "}, " + "eca_" + (rule.getRuleName() + count) + ") ");
		build.append("ecaW_" + (rule.getRuleName() + count) + " = TIMEWINDOW({SIZE=" + windowSize + "}, " + "eca_" + (rule.getRuleName() + count) + ") ");
		finalQueries.add(new Query(build.toString(), "ecaW_" + (rule.getRuleName() + count), rule.getType(), false));

	}

	private static void buildFreeQuery(EcaRuleObj rule, Integer count) {
		StringBuffer build = new StringBuffer();
		build.setLength(0);

		build.append("eca_" + (rule.getRuleName() + count) + " = " + rule.getRuleCondition() + " ");

//		build.append("ecaW_" + (rule.getRuleName() + count) + " = TIMEWINDOW({SIZE=" + windowSize + ", SLIDE="
//				+ windowSize + "}, " + "eca_" + (rule.getRuleName() + count) + ") ");
		build.append("ecaW_" + (rule.getRuleName() + count) + " = TIMEWINDOW({SIZE=" + windowSize + "}, " + "eca_" + (rule.getRuleName() + count) + ") ");
		finalQueries.add(new Query(build.toString(), "ecaW_" + (rule.getRuleName() + count), rule.getType(), false));

	}

	private static void buildMapQuery(EcaRuleObj rule, Query query, Integer count) {
		StringBuffer build = new StringBuffer();
		build.setLength(0);
		String source;
		if (rule.getType().equals("system")) {
			source = "eca_systemload";
		} else if (rule.getRulesource().equals("QueryEvent")) {
			source = "eca_planmodification";
		} else if (rule.getRulesource().equals("TimerEvent")) {
			build.append("eca_time = TIMER({period = " + timer + ", timefromstart = true, source = 'source'}) ");
			source = "eca_time";
		} else {
			source = rule.getRulesource();
		}

		if (query != null) {
			build.append("ecaM_" + (rule.getRuleName() + count) + " = MAP({EXPRESSIONS = [ " + rule.getRuleCondition()
					+ " ]}, " + query.getName() + ") ");

		} else {
			build.append("ecaM_" + (rule.getRuleName() + count) + " = MAP({EXPRESSIONS = [ " + rule.getRuleCondition()
					+ " ]}, " + source + ") ");
		}

		mapQueries.add(new Query(build.toString(), "ecaM_" + (rule.getRuleName() + count), rule.getType(), true));

	}

	private static void joinQueries(Query queryA, Query queryB) {
		StringBuffer build = new StringBuffer();
		build.setLength(0);

		build.append(queryA.getName() + "join = JOIN( { PREDICATE='true' }, " + queryA.getName() + ", "
				+ queryB.getName() + " ) ");
		joinQueries.add(new Query(build.toString(), (queryA.getName() + "join"), "join", false));
	}

	private static void addSystemLoadFunctions(ArrayList<String> functionList) {
		StringBuffer build = new StringBuffer();
		build.setLength(0);
		int c = 0;
		if (source.equals("eca_time")) {
			build.append("eca_time = TIMER({period = " + timer + ", timefromstart = true, source = 'source'}) ");
		}

		build.append("eca_sysLoad = MAP({EXPRESSIONS = [ ");
		for (int i = 0; i < functionList.size(); i++) {
			// i = attribute, i+1 = op; i+2 = val
			if (functionList.get(i).equals("curCPULoad")) {
				if (c > 0) {
					build.append(", ");
				}
				c++;
				build.append("[' (CPULoad() * 100 / CPUMAX())','CPULoad' ]");
			} else if (functionList.get(i).equals("curMEMLoad")) {
				if (c > 0) {
					build.append(", ");
				}
				c++;
				build.append("[' (MEMLoad() * 100 / MEMMax())','MEMLoad' ]");
			} else if (functionList.get(i).equals("curNETLoad")) {
				if (c > 0) {
					build.append(", ");
				}
				c++;
				build.append("[' (NETLoad() * 100 / NETMAX())','NETLoad' ]");
			}
			if (functionList.size() > i + 2) {
				i = i + 2;
			}
		}

		build.append(" ]}, " + source + ") ");
		c = 0;
		build.append("eca_sysLoadFinal = SELECT ({predicate='");
		for (int i = 0; i < functionList.size(); i++) {
			// i = attribute, i+1 = op; i+2 = val
			if (functionList.get(i).equals("curCPULoad")) {

				if (c > 0) {
					build.append(" && ");
				}
				c++;
				build.append("CPULoad ");
			} else if (functionList.get(i).equals("curMEMLoad")) {

				if (c > 0) {
					build.append(" && ");
				}
				c++;
				build.append("MEMLoad ");
			} else if (functionList.get(i).equals("curNETLoad")) {

				if (c > 0) {
					build.append(" && ");
				}
				c++;
				build.append("NETLoad ");
			}
			build.append(functionList.get(i + 1) + " " + functionList.get(i + 2));
			if (functionList.size() > i + 2) {
				i = i + 2;
			} else {
				break;
			}
		}
		build.append("'}, eca_sysLoad) ");

//		build.append("ecaW_eca_sysLoadFinal = TIMEWINDOW({SIZE=" + windowSize + ", SLIDE=" + windowSize
//				+ "}, eca_sysLoadFinal) ");
		build.append("ecaW_eca_sysLoadFinal = TIMEWINDOW({SIZE=" + windowSize + "}, eca_sysLoadFinal) ");
		finalQueries.add(new Query(build.toString(), "ecaW_eca_sysLoadFinal", "sysLoad", false));
	}

	private static String addAction(String finalQuery, String finalNameA, ArrayList<String> action) {

		StringBuffer build = new StringBuffer();
		StringBuffer withFunction = new StringBuffer();
		String tmp = "";
		int count = 0;
		boolean function = false;
		build.setLength(0);
		withFunction.setLength(0);

		build.append(" cmd = COMMAND({CommandExpression='");
		for (int i = 0; i < action.size(); i++) {
			// 0function - 1prioperator - 2privalue - 3state
			if (action.get(i).equals("rnd") || action.get(i).equals("MIN") || action.get(i).equals("MAX")) {
				tmp = createQueryIdFunction(finalNameA, action.get(i), action);
				function = true;
				break;
			}
			if (i > 0 && action.get(i) != null) {
				build.append("(");
				count++;
			}
			if (action.get(i) != null) {
				build.append(action.get(i));
			}
			if (action.size() < i + 1) {
				if (action.get(i + 1) == null) {
					i++;
				}
			}
		}
		if (!function) {
			for (int i = 0; i < count; i++) {
				build.append(")");
			}
			build.append(" )'}, " + finalNameA + ")");
			return finalQuery + build.toString();
		} else {
			withFunction.append(tmp + build.toString() + "(id)");
			for (int i = 0; i < count; i++) {
				withFunction.append(")");
			}
			withFunction.append("'}, eca_IdQueryNoACRnd)");
			return finalQuery + withFunction.toString();
		}
	}

	private static String createQueryIdFunction(String finalName, String type, ArrayList<String> actionList) {
		StringBuffer build = new StringBuffer();
		build.setLength(0);

		build.append("eca_IdQuery = MAP({EXPRESSIONS = [['retrieveQueryIDs(\"priority " + actionList.get(2)
				+ actionList.get(3) + " && state =\\'" + actionList.get(4) + "\\'\")', 'qids']]}, " + finalName + ") ");

		build.append(
				"eca_IdQueryNoAC0 = MAP({EXPRESSIONS = [['filterQueryIDs(qids,\"acquery=false\")','qids']]}, eca_IdQuery) ");

		build.append(
				"eca_IdQueryNoAC = MAP({EXPRESSIONS = [['filter(qids,\"isAcQuery(x)=false\")','qids']]}, eca_IdQueryNoAC0) ");

		build.append("eca_IdQueryEmpty = MAP({EXPRESSIONS = [['isEmpty(qids)','isEmpty'], 'qids']}, eca_IdQueryNoAC) "
				+ "eca_IdQueryNotEmpty = SELECT({PREDICATE = 'isEmpty = false'}, eca_IdQueryEmpty) ");

		if (type.equals("MIN")) {
			// TODO: NICHT ID, SONDERN PRIO!
			build.append("eca_IdQueryNoACRnd = MAP({EXPRESSIONS = [['min_(qids)','id']]}, eca_IdQueryNotEmpty) ");
		} else if (type.equals("MAX")) {
			build.append("eca_IdQueryNoACRnd = MAP({EXPRESSIONS = [['max_(qids)','id']]}, eca_IdQueryNotEmpty) ");
		} else {
			build.append("eca_IdQueryNoACRnd = MAP({EXPRESSIONS = [['rnd(qids)','id']]}, eca_IdQueryNotEmpty) ");
		}

		return build.toString();
	}

	private static String queryExists(String finalName, ArrayList<String> queryCond) {
		StringBuffer build = new StringBuffer();
		build.setLength(0);
		for (int i = 0; i < queryCond.size(); i++) {
			build.append("eca_exQuery" + i + " = MAP({EXPRESSIONS = [['retrieveQueryIDs(\"priority " + queryCond.get(i + 1)
					+ queryCond.get(i + 2) + " && state =\\'" + queryCond.get(i + 3) + "\\'\")', 'qids']]}, ");
			if (i > 0) {
				build.append("eca_exQueryNotNull" + (i-4) + ") ");
			} else {
				build.append(finalName + ") ");
			}

			build.append(
					"eca_exQueryNoAC0" +i+ " = MAP({EXPRESSIONS = [['filterQueryIDs(qids,\"acquery=false\")','qids']]}, eca_exQuery" + i + ") ");
			
			build.append(
					"eca_exQueryNoAC" +i+ " = MAP({EXPRESSIONS = [['filter(qids,\"isAcQuery(x)=false\")','qids']]}, eca_exQueryNoAC0" +i+ ") ");

			build.append(
					"eca_exQueryEmpty" +i+ " = MAP({EXPRESSIONS = [['isEmpty(qids)','isEmpty'], 'qids']}, eca_exQueryNoAC" +i+ ") ");

			if (queryCond.get(i).equals("!exists")) {
				build.append("eca_exQueryNotNull" +i+ " = SELECT({PREDICATE = 'isEmpty = true'}, eca_exQueryEmpty" +i+ ") ");
				finalName =  "eca_exQueryNotNull" +i;
			} else if ((queryCond.get(i).equals("exists"))) {
				build.append("eca_exQueryNotNull" +i+ " = SELECT({PREDICATE = 'isEmpty = false'}, eca_exQueryEmpty" +i+ ") ");
				finalName =  "eca_exQueryNotNull" +i;
			} else {
				return null;
			}
			i = i+3;
		}
		return build.toString();
	}

}

class Query {
	private String query;
	private String name;
	private String type;
	private boolean isMapQuery;

	Query(String query, String name, String type, Boolean isMap) {
		this.setQuery(query);
		this.setName(name);
		this.setType(type);
		this.setIsMapQuery(isMap);
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isMapQuery() {
		return isMapQuery;
	}

	public void setIsMapQuery(Boolean isMap) {
		this.isMapQuery = isMap;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
