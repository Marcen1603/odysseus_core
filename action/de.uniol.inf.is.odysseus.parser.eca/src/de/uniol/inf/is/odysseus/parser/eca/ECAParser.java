package de.uniol.inf.is.odysseus.parser.eca;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.action.actuatorManagement.ActuatorFactory;
import de.uniol.inf.is.odysseus.action.output.Action;
import de.uniol.inf.is.odysseus.action.output.ActionParameter;
import de.uniol.inf.is.odysseus.action.output.IActuator;
import de.uniol.inf.is.odysseus.action.output.ActionParameter.ParameterType;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IQueryParser;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.base.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;

public class ECAParser implements IQueryParser{
	private ICompiler compiler;
	private IAdvancedExecutor executer;
	
	private static Pattern ecaPattern;
	
	private static final Pattern ACTIONPATTERN = Pattern.compile("(\\w+)\\.(\\w+)\\.(\\w+\\s*)\\(([^\\(\\)]*)\\)");
	private static final Pattern LANGPATTERN = Pattern.compile("\\[LANG:(\\w*)\\]");
	private static final Pattern PARAMPATTERN = Pattern.compile("[^,]*");
	private static final Pattern PARAMTYPEPATTERN = Pattern.compile("([^:]*)(\\w*)");
	
	private static final String DEFAULTLANG = "CQL";
	
		
	public ECAParser () {
		//non final - just for clearness!
		if (ecaPattern == null){
			String method = "\\w+\\s*";
			String actuator = "\\w+";
			String manager = "\\w+";
			String actionConstruct = manager+"\\."+actuator+"\\."+method+"\\(.*\\)";
			ecaPattern = Pattern.compile("(ON\\s*\\().*\\)(\\s*DO\\s+"+actionConstruct+"[\\s*\\,"+actionConstruct+"]*)", Pattern.CASE_INSENSITIVE);		
		}
	}
	
	@Override
	public String getLanguage() {
		return "ECA";
	}
	
	public void bindCompiler (ICompiler compiler){
		this.compiler = compiler;
	}
	
	public void bindExecuter (IAdvancedExecutor executer){
		this.executer = executer;

	}

	@Override
	public List<ILogicalOperator> parse(String query)
			throws QueryParseException {
		HashMap<Action, ArrayList<ActionParameter>> actions = new HashMap<Action, ArrayList<ActionParameter>>();
		//extract internal query
		Matcher ecaMatcher = ecaPattern.matcher(query);
		if (ecaMatcher.matches()){
			ecaMatcher.reset();
			if (ecaMatcher.find()){
				String interalQuery = query.substring(ecaMatcher.end(1), ecaMatcher.start(2)).trim();

				//check if internalQuery defines used language
				String lang = DEFAULTLANG; 
				Matcher langMatcher = LANGPATTERN.matcher(interalQuery);
				if (langMatcher.find()){
					lang = langMatcher.group(1);
					interalQuery = langMatcher.replaceFirst("");
				}
				
				//create logical plan and retrieve schema
				List<ILogicalOperator> plan = compiler.translateQuery(interalQuery, lang);
				//this.determineSchema(plan);
				
				//extract action part of query
				String actionString = query.substring(ecaMatcher.start(2), query.length());
				actionString = actionString.trim();
				actionString = actionString.substring(2, actionString.length());
				actionString = actionString.trim();
				
				//split action into manager, actuator, method & params 
				Matcher actionMatcher = ACTIONPATTERN.matcher(actionString);
				while (actionMatcher.find()){
					String managerName = actionMatcher.group(1).trim();
					String actuatorName = actionMatcher.group(2).trim();
					String methodName = actionMatcher.group(3).trim();
					String params = actionMatcher.group(4).trim();	


					//extract params 
					ArrayList<ActionParameter> actionParameters = new ArrayList<ActionParameter>();
					if (params.length() > 0){
						ArrayList<Class<?>> paramList = new ArrayList<Class<?>>();
						Matcher paramMatcher = PARAMPATTERN.matcher(params);
						while (paramMatcher.find()){
							String completeParam = paramMatcher.group();
							Matcher paramTypeMatcher = PARAMTYPEPATTERN.matcher(completeParam);
							if (paramTypeMatcher.find()){
								String paramValue = paramTypeMatcher.group(1);
								String paramType = paramTypeMatcher.group(2);
								
								Object value = this.generateValue(paramValue, paramType);
								
								paramList.add(value.getClass());
								actionParameters.add(new ActionParameter(ParameterType.Value, value));
							}else{
								//TODO determine type from schema
							}	
						}
					}
					//create action object & map with parameters
					IActuator actuator = ActuatorFactory.getInstance().getActuator(actuatorName, managerName);
					actions.put(new Action(actuator, methodName), actionParameters);
				}
				if (!actions.isEmpty()){
					return this.createNewPlan(actions, plan);
				}
				return null;
			}
		}
		throw new QueryParseException("Incorrect ECA syntax");
	}

	private Class<?> generateValue(String paramType, String paramType2) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<ILogicalOperator> createNewPlan(HashMap<Action, ArrayList<ActionParameter>> actions,
			List<ILogicalOperator> plan) {
		// TODO Auto-generated method stub
		return null;
	}

	private void determineSchema(List<ILogicalOperator> plan) throws QueryParseException {
		if (!plan.isEmpty()){
			//TODO last operator, always output operator?
			ILogicalOperator outputOperator = plan.get(plan.size()-1);
			outputOperator.getOutputSchema();
			
		}
		throw new QueryParseException("No output schema defined");
	}

	@Override
	public List<ILogicalOperator> parse(Reader reader)
			throws QueryParseException {
		// TODO Auto-generated method stub 
		return null;
	}
	
	public static void main(String[] args) {
		ECAParser parser = new ECAParser();
		String[] statements = {
				//correct
				"on (CQL) do m.a.a()",
				"ON  ([LANG:SQL]Select * From abc where abc.a=5;)do m.a.a(52)",
				"ON(Select * From (Select a from a)) do  m.a.b()",
				"on () do m.a.b(), m.b.a(2, \"3\")",
				//incorrect
				"on ([Lang:bla] Select * From do a.b()",
				"on Select * From) do a.b()",
				"on (blub) do a.b, b.a()"};		
		for (String statement : statements){
			try {
				parser.parse(statement);
			} catch (QueryParseException e) {
				System.out.println(e.getMessage());
			}
		}
	}

}
