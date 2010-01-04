package de.uniol.inf.is.odysseus.parser.eca;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.action.actuatorManagement.ActuatorFactory;
import de.uniol.inf.is.odysseus.action.exception.ActionException;
import de.uniol.inf.is.odysseus.action.output.Action;
import de.uniol.inf.is.odysseus.action.output.ActionAttribute;
import de.uniol.inf.is.odysseus.action.output.ActionParameter;
import de.uniol.inf.is.odysseus.action.output.IActuator;
import de.uniol.inf.is.odysseus.action.output.ActionParameter.ParameterType;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IQueryParser;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.base.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class ECAParser implements IQueryParser{
	private ICompiler compiler;
	
	private static Pattern ecaPattern;
	
	private static final Pattern ACTIONPATTERN = Pattern.compile("(\\w+)\\.(\\w+)\\.(\\w+\\s*)\\(([^\\(\\)]*)\\)",Pattern.CASE_INSENSITIVE);
	private static final Pattern LANGPATTERN = Pattern.compile("\\[LANG:(\\w*)\\]",Pattern.CASE_INSENSITIVE);
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
	
	/**
	 * static Service binding (used by OSGI)
	 * @param compiler
	 */
	public void bindCompiler (ICompiler compiler){
		this.compiler = compiler;
	}
	
	@Override
	public List<ILogicalOperator> parse(String query)
			throws QueryParseException {
		HashMap<Action, ArrayList<ActionParameter>> actions = new HashMap<Action, ArrayList<ActionParameter>>();
		//extract internal query
		Matcher ecaMatcher = ecaPattern.matcher(query.toLowerCase());
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
				SDFAttributeList schema = this.determineSchema(plan);
				
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
						Matcher paramMatcher = PARAMPATTERN.matcher(params);
						while (paramMatcher.find()){
							String completeParam = paramMatcher.group().trim();
							Matcher paramTypeMatcher = PARAMTYPEPATTERN.matcher(completeParam);
							if (paramTypeMatcher.find()){
								String paramValue = paramTypeMatcher.group(1);
								String paramType = paramTypeMatcher.group(2);
								
								Object value = this.generateStandardValue(paramValue, paramType);
								actionParameters.add(new ActionParameter(ParameterType.Value, value));
							
							}else{
								//check if schema contains attribute
								boolean attributeFound = false;
								for (int i=0; i<schema.getAttributeCount(); i++){
									SDFAttribute attribute = schema.get(i);
									String attributeName = attribute.getAttributeName();
									if (attributeName.equals(
											completeParam)){
										actionParameters.add(new ActionParameter(ParameterType.Attribute, 
												new ActionAttribute(attribute.getDatatype(), i)));
										attributeFound = true;
										break;
									}
								}
								if (!attributeFound){
									throw new QueryParseException("Referenced attribute: "+completeParam+" not found");
								}
							}	
						}
					}
					//create action object & map with parameters
					IActuator actuator = ActuatorFactory.getInstance().getActuator(actuatorName, managerName);
					actions.put(this.createAction(actuator, methodName, actionParameters), actionParameters);
				}
				if (!actions.isEmpty()){
					return this.createNewPlan(actions, plan);
				}
				throw new QueryParseException("No Actions defined");
			}
		}
		throw new QueryParseException("Incorrect ECA syntax");
	}

	/**
	 * Creates an Action Object if schema of the actuator is compatible 
	 * @param actuator
	 * @param methodName
	 * @param actionParameters
	 * @return
	 * @throws QueryParseException
	 */
	private Action createAction(IActuator actuator, String methodName,
			ArrayList<ActionParameter> actionParameters) throws QueryParseException {
		Class<?>[] parameters = new Class<?>[actionParameters.size()];
		for (int i=0; i<actionParameters.size(); i++){
			parameters[i] = actionParameters.get(i).getParamClass();
		}
		try {
			return new Action(actuator, methodName, parameters);
		}catch (ActionException e){
			throw new QueryParseException(e.getMessage());
		}
	}

	private Object generateStandardValue(String paramValue, String paramType) throws QueryParseException {
		try {
			if (paramValue.equals("double")){
				return Double.valueOf(paramValue);
			}else if (paramValue.equals("float")){
				return Float.valueOf(paramValue);
			}else if (paramValue.equals("long")){
				return Long.valueOf(paramValue);
			}else if (paramValue.equals("int")){
				return Integer.valueOf(paramValue);
			}else if (paramValue.equals("short")){
				return Short.valueOf(paramValue);
			}else if (paramValue.equals("byte")){
				return Byte.valueOf(paramValue);
			}else if (paramValue.equals("char")){
				return paramValue.charAt(0);
			}else if (paramValue.equals("boolean")){
				return Boolean.valueOf(paramValue);
			}else if (paramValue.equals("string")){
				return paramValue;
			}
		}catch (Exception e) {
			throw new QueryParseException(e.getMessage());
		}
		throw new QueryParseException(paramType+" is an irregulator datatype");
	}

	private List<ILogicalOperator> createNewPlan(HashMap<Action, ArrayList<ActionParameter>> actions,
			List<ILogicalOperator> plan) {
		//TODO senke erzeugen
		return null;
	}

	private SDFAttributeList determineSchema(List<ILogicalOperator> plan) throws QueryParseException {
		if (!plan.isEmpty()){
			if (plan.size()>1){
				throw new QueryParseException("Multiple plans defined, cannot determine output scheme");
			}else{
				ILogicalOperator outputOperator = this.determineOutputOperator(plan.get(0));
				return outputOperator.getOutputSchema();
			}
		}
		throw new QueryParseException("No output schema defined");
	}

	/**
	 * Find the output operator by walking one subScribedTo-path
	 * @param iLogicalOperator
	 * @return
	 */
	private ILogicalOperator determineOutputOperator(ILogicalOperator iLogicalOperator) {
		for (LogicalSubscription subscription : iLogicalOperator.getSubscribedTo()){
			return this.determineOutputOperator(subscription.getTarget());
		}
		return iLogicalOperator;
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
