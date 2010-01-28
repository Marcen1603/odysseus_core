package de.uniol.inf.is.odysseus.parser.eca;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.action.exception.ActionException;
import de.uniol.inf.is.odysseus.action.exception.AttributeParsingException;
import de.uniol.inf.is.odysseus.action.operator.EventDetectionAO;
import de.uniol.inf.is.odysseus.action.output.Action;
import de.uniol.inf.is.odysseus.action.output.IActionParameter;
import de.uniol.inf.is.odysseus.action.output.StaticParameter;
import de.uniol.inf.is.odysseus.action.output.StreamAttributeParameter;
import de.uniol.inf.is.odysseus.action.services.actuator.IActuator;
import de.uniol.inf.is.odysseus.action.services.actuator.IActuatorFactory;
import de.uniol.inf.is.odysseus.action.services.exception.ActuatorException;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IQueryParser;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.base.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Queryparser for Event, Condition, Action Queries.
 * Pattern for queries:
 * ON ([LANG:<LanguageName>]<Query>) 
 * DO <managerName>.<actuatorName>.<methodName>(<staticValue>:<type>,<attributeName>, ...)
 * [,<managerName>.<actuatorName>.<methodName>(<params>)]*
 * @author Simon Flandergan
 *
 */
public class ECAParser implements IQueryParser{
	private ICompiler compiler;

	private IActuatorFactory actuatorFactory;
	
	private static Pattern ecaPattern;
	
	private static final Pattern ACTIONPATTERN = Pattern.compile("(\\w+)\\.(\\w+)\\.(\\w+\\s*)\\(([^\\(\\)]*)\\)",Pattern.CASE_INSENSITIVE);
	private static final Pattern LANGPATTERN = Pattern.compile("\\[LANG:(\\w*)\\]",Pattern.CASE_INSENSITIVE);
	private static final Pattern PARAMPATTERN = Pattern.compile("[^,]+");
	private static final Pattern PARAMTYPEPATTERN = Pattern.compile("(.*):(.*)");
	private static final String DEFAULTLANG = "CQL";
	
	
		
	public ECAParser () {
		//non final - just for clearness!
		if (ecaPattern == null){
			String method = "\\w+\\s*";
			String actuator = "\\w+";
			String manager = "\\w+";
			String actionConstruct = manager+"\\."+actuator+"\\."+method+"\\(.*\\)";
			ecaPattern = Pattern.compile("(ON\\s*\\().*(\\)\\s*DO\\s+"+actionConstruct+"[\\s*\\,"+actionConstruct+"]*)", Pattern.CASE_INSENSITIVE);		
		}
	}
	
	@Override
	public String getLanguage() {
		return "ECA";
	}
	
	public void bindActuatorFactory(IActuatorFactory factory){
		this.actuatorFactory = factory; 
	}
	
	
	/**
	 * Service binding (used by OSGI)
	 * @param compiler
	 */
	public void bindCompiler (ICompiler compiler){
		this.compiler = compiler;
	}
	
	@Override
	public List<ILogicalOperator> parse(String query)
			throws QueryParseException {
		HashMap<Action, List<IActionParameter>> actions = new HashMap<Action, List<IActionParameter>>();
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
				//remove ')DO'
				actionString = actionString.substring(3, actionString.length());
				actionString = actionString.trim();
				
				//split action into manager, actuator, method & params 
				Matcher actionMatcher = ACTIONPATTERN.matcher(actionString);
				while (actionMatcher.find()){
					String managerName = actionMatcher.group(1).trim();
					String actuatorName = actionMatcher.group(2).trim();
					String methodName = actionMatcher.group(3).trim();
					String params = actionMatcher.group(4).trim();	

					//extract params 
					ArrayList<IActionParameter> actionParameters = new ArrayList<IActionParameter>();
					if (params.length() > 0){
						Matcher paramMatcher = PARAMPATTERN.matcher(params);
						while (paramMatcher.find()){
							String completeParam = paramMatcher.group().trim();
							Matcher paramTypeMatcher = PARAMTYPEPATTERN.matcher(completeParam);
							if (paramTypeMatcher.find()){
								String paramValue = paramTypeMatcher.group(1);
								String paramType = paramTypeMatcher.group(2);
								
								Object value = this.generateStandardValue(paramValue, paramType);
								actionParameters.add(new StaticParameter(value));
							
							}else{
								//check if schema contains attribute
								boolean attributeFound = false;
								ArrayList<Integer> possibleMatches = new ArrayList<Integer>(); 
								for (int i=0; i<schema.getAttributeCount(); i++){
									SDFAttribute attribute = schema.get(i);
									//check for uri since it is unique
									if (attribute.getURI().equals(completeParam)){
										try {
											actionParameters.add(new StreamAttributeParameter(attribute.getDatatype(), i));
										} catch (AttributeParsingException e) {
											throw new QueryParseException(e.getMessage());
										}
										attributeFound = true;
										break;
									}else if (attribute.getAttributeName().equals(completeParam)){
										//attribute name is not unique so, it is only a possible match
										possibleMatches.add(i);
									}
								}
								if (!attributeFound){
									//check there is only 1 possible match
									if (possibleMatches.size() == 1){
										int index = possibleMatches.get(0);
										try {
											actionParameters.add(new StreamAttributeParameter(schema.get(index).getDatatype(), index));
										} catch (AttributeParsingException e) {
											throw new QueryParseException(e.getMessage());
										}
									}else {
										throw new QueryParseException("Referenced attribute <"+completeParam+"> is ambiguous or inexistent");
									}
								}
							}	
						}
					}
					//fetch actuator
					IActuator actuator = null;
					try {
						actuator = this.actuatorFactory.getActuator(actuatorName, managerName);
					} catch (ActuatorException e) {
						throw new QueryParseException("Actuator<"+actuatorName+"> unknown.");
					}
					
					//create action object, sort parameters & map both
					Action action = this.createAction(actuator, methodName, actionParameters);
					actions.put(action, actionParameters);
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
			ArrayList<IActionParameter> actionParameters) throws QueryParseException {
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
			if (paramType.equals("double")){
				return Double.valueOf(paramValue);
			}else if (paramType.equals("float")){
				return Float.valueOf(paramValue);
			}else if (paramType.equals("long")){
				return Long.valueOf(paramValue);
			}else if (paramType.equals("int")){
				return Integer.valueOf(paramValue);
			}else if (paramType.equals("short")){
				return Short.valueOf(paramValue);
			}else if (paramType.equals("byte")){
				return Byte.valueOf(paramValue);
			}else if (paramType.equals("char")){
				return paramValue.charAt(0);
			}else if (paramType.equals("boolean")){
				return Boolean.valueOf(paramValue);
			}else if (paramType.equals("string")){
				return paramValue;
			}
		}catch (Exception e) {
			throw new QueryParseException(e.getMessage());
		}
		throw new QueryParseException(paramType+" is an irregulator datatype");
	}

	private List<ILogicalOperator> createNewPlan(HashMap<Action, List<IActionParameter>> actions,
			List<ILogicalOperator> plan) {
		//not necessary cause top operator is always the one in the plan
		//this.determineOutputOperator(plan.get(0));
		ILogicalOperator outputOperator = plan.get(0);

		//create new sink and subscribe to outputoperator
		EventDetectionAO eAO = new EventDetectionAO(actions);
		eAO.subscribeToSource(outputOperator, 0, 0, outputOperator.getOutputSchema());
		
		//replace old top element through sink
		plan.set(0, eAO);
		return plan;
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
	 * Find the output operator by walking one subscriptions-path.
	 * Assumption: outputOperator has no subscriptions
	 * @param iLogicalOperator
	 * @return
	 */
	private ILogicalOperator determineOutputOperator(ILogicalOperator iLogicalOperator) {
		for (LogicalSubscription subscription : iLogicalOperator.getSubscriptions()){
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
	
	//just for tets
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
	
	//just for tests
	public ICompiler getCompiler() {
		return compiler;
	}
}
