package de.uniol.inf.is.odysseus.parser.cql2.generator.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import de.uniol.inf.is.odysseus.parser.cql2.cQL.SimpleSelect;

public class OperatorCache implements Cache {

	private Collection<OperatorPlan> operatorIdBACKUP;
	private Map<SimpleSelect, OperatorPlan> operatorsBACKUP;
	
	private Map<SimpleSelect, OperatorPlan> operatorPlans = new HashMap<>();
	private List<OperatorPlan> operatorPlanList = new ArrayList<>();
	private Collection<OperatorPlan> sourcePlan = new ArrayList<>();
	private Map<String, String> streamTo;
	private Map<String, String> sinks;
	private Map<SimpleSelect, String> subQueries;
	private Map<SimpleSelect, String> lastOperators;
	private Map<SimpleSelect, String> generated;
	private boolean backupState;

	private static int operator_count;
	private final static String OP = "operator_";
	private final String VIEW = "VIEW_KEY_";
	private final String ASSIG1 = "=";
	private final String ASSIG2 = ":=";
	//TODO Refactor
	final String replacement1 = "AXZTGHHAJJJSUEJJ23123123123";
	final String replacement2 = "BNHUEOLASJJKEOOS12312309203";
	final String DISTINCT_KEY = "DISTINCT";
	
	private final String SEPERATOR = System.getProperty("line.separator");
	private StringBuilder builder;


	public OperatorCache() {
		
		streamTo = new HashMap<>();
		sinks = new HashMap<>();
		subQueries = new HashMap<>();
		lastOperators = new HashMap<>();
		generated = new HashMap<>();

	}

	public void toDistinct(String key, String value) {
		Optional<OperatorPlan> o = containsKey(key);
		if(o.isPresent()) {
			final String d = DISTINCT_KEY + "(" + value + ")";
			o.get().mapping.put(key, d);
		}
	}
	
	public String getPQL() {

		builder = new StringBuilder();
		
		sourcePlan.stream().forEach(p -> {
			buildOperatorPlan(p);
		});
		
		operatorPlanList.stream().forEach(p -> {
			buildOperatorPlan(p);
		});
		
		// place existence operators at the end of the operator plan
		existence.stream().forEach(e -> builder.append(e));
		
		return builder.toString();
	}

	private Collection<String> existence = new ArrayList<>();
	
	private void buildOperatorPlan(OperatorPlan plan) {
		
		removeUnusedOperators();
		
		plan.operators.stream().forEach(e -> {
			if(plan.mapping.get(e).contains("EXISTENCE(")) {
				StringBuilder b = new StringBuilder();
				b.append(e);
				b.append(" ");
				b.append(ASSIG1);
				b.append(" ");
				b.append(format(plan.mapping.get(e)));
				b.append(SEPERATOR);
				existence.add(b.toString());
			} else {
				if (e.contains(VIEW)) {
					builder.append(e.replace(VIEW,  ""));
					builder.append(" ");
					builder.append(ASSIG2);
				} else {
					builder.append(e);
					builder.append(" ");
					builder.append(ASSIG1);
				}
				builder.append(" ");
				builder.append(format(plan.mapping.get(e)));
				builder.append(SEPERATOR);
			}
		});
		
	}
	
	private void removeUnusedOperators() {
		operatorPlanList.stream().forEach(e -> e.cleanup());
	}
	
	public String last() {
		return operatorPlanList.size() > 0 ? operatorPlanList.get(operatorPlanList.size() - 1).lastOperator : null;
	}

	public void registerLastOperator(SimpleSelect select, String lastOperator) {
			
		lastOperators.put(select, lastOperator);
		if (generated.containsKey(select)) {
			String r = generated.get(select);
			
			operatorPlans.values().forEach(e -> {
				e.mapping.entrySet().stream().forEach(k -> {
					if (k.getValue().contains(r)) {
						e.mapping.put(k.getKey(), k.getValue().replace(r, lastOperator));
					}
				});
			});
			
		}
			
	}

	public String add(SimpleSelect select, String operator) {
		
		OperatorPlan plan = null;
		if (operatorPlans.containsKey(select)) {
			operatorPlans.get(select).add(operator);
			plan = operatorPlans.get(select);
		} else {
			plan = new OperatorPlan();
			plan.add(operator);
			operatorPlanList.add(plan);
			operatorPlans.put(select, plan);
		}
		
		return plan.last();
	}

	public String add(String key, String operator) {
		
		final OperatorPlan p = new OperatorPlan();
		p.add(key, operator);
		sourcePlan.add(p);
		
		return p.last();
	}

	public String getLastOperator(SimpleSelect select) {
		
		if (lastOperators.containsKey(select)) {
			return lastOperators.get(select);
		} else {
			final String gen = UUID.randomUUID().toString();
			lastOperators.put(select, gen);
			generated.put(select, gen);
			return gen;
		}
		
	}

	public Optional<String> getOperator(String key) {
		return operatorPlans.entrySet()
				.stream()
				.filter(p -> p.getValue().mapping.keySet().contains(key))
				.map(e -> e.getValue().mapping.get(key))
				.findFirst();
	}

	public void removeOperator(String key) {
		
		Optional<OperatorPlan> p = findOperatorPlan(key);
		if (p.isPresent()) {
			p.get().remove(key);
		}
		
	}

	public void addStreamTo(String key, String value) {
		streamTo.put(key, value);
	}

	public void addSink(String view) {
			
			String viewId = VIEW + view;
			
			
			String lastOperator = last();
			Optional<String> o = getOperator(lastOperator);
			
			if (o.isPresent()) {
				
				String operator = o.get();//operators.get(lastOperator);
				removeOperator(lastOperator);
				
				add(viewId, operator);
		
	//		operators.remove(lastOperator);
	//		ids.remove(lastOperator);
	//		
	//		operators.put(viewId, operatorPlan);
	//		ids.add(viewId);
			}
			
		}

	public void changeToBACKUP() {

		if (!backupState) {
			operatorIdBACKUP = new ArrayList<>(operatorPlanList);
			operatorsBACKUP = new HashMap<>(operatorPlans);
			operatorPlanList.clear();
			operatorPlans.clear();
			backupState = true;
		} else {
			operatorPlanList = new ArrayList<>(operatorIdBACKUP);
			operatorPlans = new HashMap<>(operatorsBACKUP);
			backupState = false;
		}

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
		existence.clear();
		sourcePlan.clear();
		lastOperators.clear();
		generated.clear();
		operatorPlanList.clear();
		operatorPlans.clear();
		streamTo.clear();
		sinks.clear();
		subQueries.clear();
	}

	public boolean isBACKUPState() {
		return backupState;
	}

	private String format(String pqlString) {
	
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

	private Optional<OperatorPlan> findOperatorPlan(String key) {
		
		Optional<OperatorPlan> o = operatorPlans.entrySet()
				.stream()
				.filter(p -> p.getValue().mapping.keySet().contains(key))
				.map(e -> e.getValue())
				.findFirst();
		
		return o;
	}
	
	private Optional<OperatorPlan> containsKey(String key) {
		return operatorPlanList.stream()
				.filter(e -> e.mapping.keySet().contains(key))
				.map(e -> e)
				.findFirst();
	}
	
	public static class OperatorPlan {
		
		private List<String> operators;
		private Map<String, String> mapping;
		private String lastOperator;
		
		public OperatorPlan() {
			operators = new ArrayList<>();
			mapping = new HashMap<>();
		}
		
		public String add(String value) {
			return add(OP + operator_count++, value);
		}

		public String add(String key, String value) {
			addToOperators(key, value);
			return key;
		}
		
		public void remove(String key) {
			if (operators.contains(key)) {
				operators.remove(key);
				mapping.remove(key);
				if (lastOperator.equals(key)) {
					lastOperator = get(operators.size() - 1);
				}
			}
		}
		
		public String get(int index) {
			return index > 0 && index < operators.size() ? operators.get(index) : null;
		}
		
		public int count() {
			return operators.size();
		}
		
		public String last() {
			return lastOperator;
		}
		
		private void addToOperators(String key, String value) {
			if (!operators.contains(key)) {
				operators.add(key);
				mapping.put(key, value);
				lastOperator = key;
			}
		}
		
		public void cleanup() {

			
			Iterator<String> iter = operators.iterator();
			while (iter.hasNext()) {
				
				long count = 0L;
				final String operator = iter.next();
//				final String value = mapping.get(operator);
//TODO not working properly
//				// 1) remove multiple definitions of the same operator
//				mapping.entrySet()
//					.stream()
//					.filter(p -> p.getKey() != operator && p.getValue().equals(value))
//					.forEach(e -> {
//						
//						operators
//							.stream()
//							.filter(q -> mapping.get(q).contains(e.getKey()))
//							.forEach(k -> mapping.put(k, mapping.get(k).replaceAll(e.getKey(), operator)));
//						
////						mapping.entrySet()
////							.stream()
////							.filter(q -> q.getValue().contains(e.getKey()))
////							.forEach(k -> mapping.put(k.getKey(), k.getValue().replaceAll(e.getKey(), operator)));
//					});
				
				
//				mapping.entrySet()
//						.stream()
//						.filter(p -> p.getKey() != operator && p.getValue().equals(mapping.get(operator)))
//						.forEach(e -> {
//							mapping.values().stream()
//							.filter(p -> p.contains(e.getKey()))
//							.map(k -> k.replaceAll(e.getKey(), operator))
//							.forEach(k -> {
//								mapping.put(e.getKey(), k);
//							});
//						});

				// 2) remove unused operators
				if (!operator.equals(lastOperator)) {
					// count occurrences
					count = mapping.values()
							.stream()
							.filter(p -> p.contains(operator))
							.count();
					// remove operator if it does not appear as an
					// input in any other operator
					if (count == 0) {
						mapping.remove(operator);
						iter.remove();
					}
				}
				
				
			}
			
		}
		
	}

}
