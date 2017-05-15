package de.uniol.inf.is.odysseus.parser.novel.cql.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OperatorBuilderFactory;
import de.uniol.inf.is.odysseus.database.logicaloperator.DatabaseSourceAO;
import de.uniol.inf.is.odysseus.parser.pql.impl.PQLParserImplConstants;

public class PQLBuilder 
{
	private static PQLBuilder instance = null;
	
	public static PQLBuilder getInstance()
	{
		return instance == null ? new PQLBuilder() : instance;
	}
	
	private static Map<String, String[]> operatorArguments;
	private static List<String> operatorNames;
	
	public final String ESC = "---";
	public final String STRING_TYPE = "STRING";
	public final String LIST_TYPE = "LIST";
	public final String INT_TYPE = "INTEGER";
	public final String[] types = 
		{
			STRING_TYPE,
			LIST_TYPE,
			INT_TYPE
			
		};
	
	enum Type
	{
		STRING,
		LIST,
		INT,
	}
	
	private final StringBuilder builder = new StringBuilder();
	
	private PQLBuilder() 
	{
		
		operatorArguments = new HashMap<>();
		operatorNames = new ArrayList<>();
		
		operatorNames.add("DATABASESOURCE");
		operatorArguments.put(
			"DATABASESOURCE", 
			new String[] {
			 "connection",
			 "table",
			 "attributes",
			 "waiteach"
			}
		);
		
		operatorNames.add("ACCESS");
		operatorArguments.put(
			"ACCESS", 
			new String[] {
			 "source",
			 "wrapper",
			 "protocol",
			 "transport",
			 "dataHandler",
			 "schema",
			 "options"
			}
		);
		operatorNames.add("DATABASESINK");
		operatorArguments.put(
				"DATABASESINK", 
				new String[] {
				 "connection",
				 "table",
				 "drop",
				 "truncate",
				 "type",
				 "input"
				}
			);
		
		operatorNames.add("EXISTENCE");
		operatorArguments.put(
				"EXISTENCE", 
				new String[] {
				 "type",
				 "predicate",
				 "input"
				}
			);
		
	}
	
	public String buildTimebasedWindowOperator(String windowSize, String windowUnit, String advanceSize, String advanceUnit, String input)
	{
		builder.setLength(0);
		builder.append("TIMEWINDOW({size=[");
		builder.append(windowSize);
		builder.append(",");
		builder.append("'");
		builder.append(windowUnit);
		builder.append("'");
		builder.append("],advance=[");
		builder.append(advanceSize);
		builder.append(",'");
		builder.append(advanceUnit);
		builder.append("']},");
		builder.append(input);
		builder.append(")");

		return builder.toString();//"TIMEWINDOW({size = ["+windowSize+",'"+windowUnit+"'],advance = ["+advanceSize+",'"+advanceUnit+"']},"+input+")";
	}

	/** Returns a string that represents {@link DatabaseSourceAO} and is build by the given parameters in following order: 
	 * @param connection
	 * @param table
	 * @param attributes
	 * @param waiteach (is optional)
	 */
	@Deprecated
	public String buildCreateDataSourceOperator(Map<String, String> args)
	{	
		String[] variables = 
			{
			 "connection",
			 "table",
			 "attributes",
			 "waiteach"//is optional
			};
		return buildOperator("DATABASESOURCE", args);
	}
	
	/** Returns a string that represents {@link AccessAO} and is build by the given parameters in following order:
	 * @param sourcename
	 * @param wrapper
	 * @param protocol
	 * @param transport
	 * @param datahandler
	 * @param schema
	 * @param options
	 */
	@Deprecated 
	public String buildAccessOperator(Map<String, String> args)
	{
		String[] variables = 
			{
			 "source",
			 "wrapper",
			 "protocol",
			 "transport",
			 "dataHandler",
			 "schema",
			 "options"
			};
		return buildOperator("ACCESS", args);
	}
	
	public String buildOperator(String Operatorname, Map<String, String> args)
	{
		if(!operatorNames.contains(Operatorname))
			throw new IllegalArgumentException("Given operator name is unknown: " + Operatorname);
		
		System.out.println("args= " + args.toString());
		
		buildPrefix(Operatorname);
		buildArguments(operatorArguments.get(Operatorname), args);
		buildSuffix(args.get("input"));
		return builder.toString();
	}
	
	private StringBuilder buildPrefix(String operatorname)
	{
		builder.setLength(0);
		builder.append(operatorname);
		builder.append("(");
		builder.append("{");
		return builder;
	}
	
	private StringBuilder buildSuffix(String string)
	{
		if(string != null)
		{
			builder.append("}");
			builder.append("," + string);
		}
		else
		{
			builder.append("}");
		}
		builder.append(")");
		return builder;
	}
	
	private StringBuilder buildArguments(String[] variables, Map<String, String> map)
	{
		for(String var : variables)
		{
			System.out.println(var);
			if(!var.equals("input"))
			{
				String value = getValue(map.get(var));
				if(value != null)
					builder.append(var + "=" + value + ",");
			}
		}
		builder.deleteCharAt(builder.toString().length() - 1);
		return builder;
	}
	
	private Type getType(String value) 
	{
		String[] split = value.split(ESC);
		
		return null;
	}

	private String getValue(String value)
	{
		if(value == null) return value;
		if(!value.contains(ESC))
		{
			throw new IllegalArgumentException("given value " 
					+ value + " must contain a type that is decoded such as " 
					+ ESC + " with one of the following types:"+types.toString());
		}
		
		String[] split = value.split(ESC);
		String type = split[1];
		String name = split[0];
		switch(type)
		{
		case(STRING_TYPE):
			return "'" + name + "'";
		case(LIST_TYPE):
			return "[" + name + "]";
		default: 
			return name;
		}
	}
	
	private StringBuilder buildWindowOperator()
	{
		builder.setLength(0);
		return null;
	}
	
	public static void main(String[] args) 
	{
	
		for(int i = 0; i < PQLParserImplConstants.tokenImage.length; i++)
		{
			System.out.println("token="+PQLParserImplConstants.tokenImage[i]+", index="+i);
		}
		System.out.println("hey: "+OperatorBuilderFactory.getOperatorBuilderNames());
//		OperatorBuilderFactory.getOperatorBuilderType(name)
		OperatorBuilderFactory.getOperatorBuilderType("ACCESS").getParameters().iterator();
		
	}
	
}
