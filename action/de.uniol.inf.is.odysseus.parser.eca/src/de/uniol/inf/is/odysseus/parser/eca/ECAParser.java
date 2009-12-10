package de.uniol.inf.is.odysseus.parser.eca;

import java.io.Reader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IQueryParser;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.base.planmanagement.ICompiler;
import de.uniol.inf.is.odysseus.planmanagement.executor.IAdvancedExecutor;

public class ECAParser implements IQueryParser{
	private Pattern ecaPattern;
	
	private ICompiler compiler;
	private IAdvancedExecutor executer;

	public ECAParser () {
		String param = "[a-zA-Z0-9\"]+\\s*";
		String method = "\\w+\\s*\\(["+param+"[,\\s*"+param+"]*]?\\)";
		String actuator = "\\w+\\."+method;	
		this.ecaPattern = Pattern.compile("(ON\\s*\\().*(\\)\\s*DO\\s+"+actuator+"[\\s+"+actuator+"]*)", Pattern.CASE_INSENSITIVE);
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
		//extract internal query
		String lang = ""; // in query
		Matcher matcher = this.ecaPattern.matcher(query);
		if (matcher.matches()){
			matcher.reset();
			if (matcher.find()){
				String interalQuery = query.substring(matcher.end(1), matcher.start(2));
				
				String actuatorString = query.substring(matcher.start(2)+1, query.length());
				//remove do-key phrase
				actuatorString = actuatorString.trim();
				actuatorString = actuatorString.substring(2, actuatorString.length());
				actuatorString = actuatorString.trim();
			
				//List<ILogicalOperator> plan = compiler.translateQuery(interalQuery, lang);
				//this.determineSchema(plan);
				return null;
			}
		}
		throw new QueryParseException("Incorrect ECA syntax");
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
				"on (CQL) do a.a()",
				"ON  (Select * From abc where abc.a=5;)do a.a(5)",
				"ON(Select * From (Select a from a)) do  a.b()",
				"on () do a.b(), b.a(2, \"3\")",
				//incorrect
				"on (Select * From do a.b()",
				"on Select * From) do a.b()",
				"on (blub) do a.b, b.a()"};		
		for (String statement : statements){
			try {
				parser.parse(statement);
			} catch (QueryParseException e) {
			}
		}
	}

}
