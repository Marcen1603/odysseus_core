package de.uniol.inf.is.odysseus.parser.eca;

import java.io.Reader;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IQueryParser;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.base.planmanagement.ICompiler;

public class ECAParser implements IQueryParser{
	private ICompiler compiler;
	private HashMap<String, String> languagePatterns = new HashMap<String, String>();
	private Pattern ecaPatternStart;
	private Pattern ecaPatternEnd;

	public ECAParser () {
		this.ecaPatternStart = Pattern.compile("WHEN[\\s]+EVENT[\\s]*\\(");
		
		String param = "[\\w]+[\\s]*";
		String method = "[\\w]+[\\s]*\\(["+param+"[,"+param+"]*]?\\)";
		String actuator = "[\\w]+\\."+method;	
		this.ecaPatternEnd = Pattern.compile("\\)[\\s]*THEN[\\s]+EXECUTE[\\s]+"+actuator+"[[\\s]+"+actuator+"]*");
		
	}
	
	@Override
	public String getLanguage() {
		return "ECA";
	}
	
	public void bindCompiler (ICompiler compiler){
		this.compiler = compiler;
	}

	@Override
	public List<ILogicalOperator> parse(String query)
			throws QueryParseException {
		//extract internal query
		
		String language = this.determineQueryLanguage(query);
		return null;
	}

	private String determineQueryLanguage(String query) throws QueryParseException {
		for (Entry<String, String> entry : this.languagePatterns.entrySet()){
			if (query.matches(entry.getKey())){
				return entry.getValue();
			}
		}
		throw new QueryParseException("Unknown Eventlanguage");
	}

	@Override
	public List<ILogicalOperator> parse(Reader reader)
			throws QueryParseException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean match (String string){
		string = string.toUpperCase();
		Pattern p = Pattern.compile(this.ecaPatternStart.pattern()+".*"+this.ecaPatternEnd);
		return p.matcher(string).find();
	}
	
	public static void main(String[] args) {
		ECAParser parser = new ECAParser();
		String[] statements = {
				//correct
				"WHEN EVENT (CQL) THEN EXECUTE a.a()",
				"WHEN EVENT  (Select * From a where a.a=5;)THEN EXECUTE a.a(5) ",
				"WHEN EVENT(Select * From (Select a from a)) THEN  execute a.b()",
				"WHEN EVENT () then execute a.b(), b.a(5, \"2\")",
				//incorrect
				"WHEN EVENT (Select * From THEN execute a.b()",
				"WHEN EVENT Select * From) THEN execute a.b()",
				"WHEN event () then a.b()",
				"WHen event (blub) then execute a.b, b.a()"};		
		for (String statement : statements){
			System.out.println(parser.match(statement));
		}
	}

}
