package de.uniol.inf.is.odysseus.parser.cql2.generator.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;

public class OperatorCache implements Cache {

	private final Logger log = LoggerFactory.getLogger(OperatorCache.class);

	private Collection<String> ids;
	private Map<String, String> operators;
	private Collection<String> operatorIdBACKUP;
	private Map<String, String> operatorsBACKUP;
	private Map<String, String> streamTo;
	private Map<String, String> sinks;
	private Map<SimpleSelect, String> subQueries;
	private boolean backupState;

	final String VIEW = "VIEW_KEY_";
	final String OP = "operator_";
	final String ASSIG1 = "=";
	final String ASSIG2 = ":=";
	//TODO Refactor
	final String replacement1 = "AXZTGHHAJJJSUEJJ23123123123";
	final String replacement2 = "BNHUEOLASJJKEOOS12312309203";
	final String DISTINCT_KEY = "DISTINCT";

	//TODO counter does not increases in the right sense
	private static int counter;

	public OperatorCache() {
		ids = new ArrayList<>();
		operators = new HashMap<>();
		streamTo = new HashMap<>();
		sinks = new HashMap<>();
		subQueries = new HashMap<>();

	}

	public void toDistinctOperator(String key, String value) {

		if (operators.containsKey(key)) {
			String distinct = DISTINCT_KEY + "(" + value + ")";
			operators.put(key, distinct);
		} else {
			log.warn("key " + key + " not present");
		}

	}

	public String getPQL() {
		final StringBuilder builder = new StringBuilder();
		final String sep = System.getProperty("line.separator");

		ids.stream().forEach(e -> {
			if (e.contains(VIEW)) {
				builder.append(formatPQLString(e.replace(VIEW, "") + ASSIG2 + operators.get(e)));
			} else {
				builder.append(formatPQLString(e + ASSIG1 + operators.get(e)));
			}
			builder.append(sep);
		});

		return builder.toString();
	}

	private String formatPQLString(String pqlString) {

		String trimed = pqlString.contains(ASSIG2) ? pqlString.replaceFirst(ASSIG2, replacement1)
				: pqlString.replaceFirst(ASSIG1, replacement2);

		// remove all white space in between
		trimed = trimed.replaceAll("\\s*[\\r\\n]+\\s*", "");
		// remove all trailing whitespace and in between
		trimed = trimed.trim().replace(" ", "");
		trimed = trimed.replace(replacement2, " " + ASSIG1 + " ");
		trimed = trimed.replace(replacement1, " " + ASSIG2 + " ");

		return trimed;
	}

	public void addOperator(String key, String value) {

		if (!ids.contains(key)) {
			ids.add(key);
			operators.put(key, value);
		} else {
			log.warn("key " + key + " already present");
		}

	}

	public void addOperator(String key) {
		ids.add(key);
	}

	public boolean removeOperator(String key) {

		if (operators.containsKey(key)) {
			operators.remove(key);
			return ids.remove(key);
		}

		return false;
	}

	public String lastOperatorId() {

		if (!ids.isEmpty()) {
			return new ArrayList<>(ids).get(ids.size() - 1);
		}

		return null;
	}

	public void swapLastOperators() {

		if (ids.size() > 1) {
			String last1 = lastOperatorId();
			ids.remove(last1);
			String last2 = lastOperatorId();
			ids.remove(last2);
			ids.add(last1);
			ids.add(last2);
		}

	}

	public String registerOperator(String value) {
		return registerOperator(OP + counter++, value);
	}

	public String registerOperator(CharSequence value) {
		return registerOperator(value.toString());
	}

	public String registerOperator(String key, String value) {
		addOperator(key, value);
		return key;
	}

	public void changeToBACKUP() {

		if (!backupState) {
			operatorIdBACKUP = new ArrayList<>(ids);
			operatorsBACKUP = new HashMap<>(operators);
			ids.clear();
			operators.clear();
			backupState = true;
		} else {
			ids = new ArrayList<>(operatorIdBACKUP);
			operators = new HashMap<>(operatorsBACKUP);
			backupState = false;
		}

	}

	public void addStreamTo(String key, String value) {
		streamTo.put(key, value);
	}

	public void addSink(String view) {
		
		String viewId = VIEW + view;
		
		String lastOperator = lastOperatorId();
		String operatorPlan = operators.get(lastOperator);
		
		operators.remove(lastOperator);
		ids.remove(lastOperator);
		
		operators.put(viewId, operatorPlan);
		ids.add(viewId);
		
	}

	public Collection<String> getOperatorId() {
		return ids;
	}

	public Map<String, String> getOperators() {
		return operators;
	}

	public String getOperator(String key) {
		return operators.containsKey(key) ? operators.get(key) : null;
	}

	public Map<String, String> getStreamTo() {
		return streamTo;
	}

	public Map<String, String> getSinks() {
		return sinks;
	}

	public Map<SimpleSelect, String> getSubQueries() {
		return subQueries;
	}

	@Override
	public void flush() {
		ids.clear();
		operators.clear();
		streamTo.clear();
		sinks.clear();
		subQueries.clear();
	}

	public boolean isBACKUPState() {
		return backupState;
	}

}
