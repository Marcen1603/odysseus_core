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
			 "connection"+ESC+STRING_TYPE,
			 "table"+ESC+STRING_TYPE,
			 "attributes"+ESC+LIST_TYPE,
			 "waiteach"+ESC+INT_TYPE
			}
		);
		
		operatorNames.add("ACCESS");
		operatorArguments.put(
			"ACCESS", 
			new String[] {
			 "source"+ESC+STRING_TYPE,
			 "wrapper"+ESC+STRING_TYPE,
			 "protocol"+ESC+STRING_TYPE,
			 "transport"+ESC+STRING_TYPE,
			 "datahandler"+ESC+STRING_TYPE,
			 "schema"+ESC+LIST_TYPE,
			 "options"+ESC+LIST_TYPE
			}
		);
		
		operatorNames.add("SENDER");
		operatorArguments.put(
			"SENDER", 
			new String[] {
			 "sink"+ESC+STRING_TYPE,
			 "wrapper"+ESC+STRING_TYPE,
			 "protocol"+ESC+STRING_TYPE,
			 "transport"+ESC+STRING_TYPE,
			 "datahandler"+ESC+STRING_TYPE,
			 "schema"+ESC+LIST_TYPE,
			 "options"+ESC+LIST_TYPE,
			 "input"
			}
		);
		
		operatorNames.add("DATABASESINK");
		operatorArguments.put(
				"DATABASESINK", 
				new String[] {
				 "connection"+ESC+STRING_TYPE,
				 "table"+ESC+STRING_TYPE,
				 "drop"+ESC+STRING_TYPE,
				 "truncate"+ESC+STRING_TYPE,
				 "type"+ESC+STRING_TYPE,
				 "input"
				}
			);
		
		operatorNames.add("EXISTENCE");
		operatorArguments.put(
				"EXISTENCE", 
				new String[] {
				 "type"+ESC+STRING_TYPE,
				 "predicate"+ESC+STRING_TYPE,
				 "input"
				}
			);
		
		operatorNames.add("SELECT");
		operatorArguments.put(
				"SELECT", 
				new String[] {
				 "predicate"+ESC+STRING_TYPE,
				 "input"
				}
			);
		
		operatorNames.add("MAP");
		operatorArguments.put(
				"MAP", 
				new String[] {
				 "expressions"+ESC+LIST_TYPE,
				 "input"
				}
			);	
		
		operatorNames.add("TIMEWINDOW");
		operatorArguments.put(
				"TIMEWINDOW", 
				new String[] {
				 "size"+ESC+LIST_TYPE,
				 "advance"+ESC+LIST_TYPE,
				 "input"
				}
			);	

		operatorNames.add("ELEMENTWINDOW");
		operatorArguments.put(
				"ELEMENTWINDOW", 
				new String[] {
				 "size"+ESC+INT_TYPE,
				 "advance"+ESC+INT_TYPE,
				 "partition"+ESC+INT_TYPE,
				 "input"
				}
			);	
		
		operatorNames.add("RENAME");
		operatorArguments.put(
				"RENAME", 
				new String[] {
				 "aliases"+ESC+LIST_TYPE,
				 "pairs"+ESC+STRING_TYPE,
				 "input"
				}
			);	
		
		operatorNames.add("AGGREGATE");
		operatorArguments.put(
				"AGGREGATE", 
				new String[] {
				 "aggregations"+ESC+LIST_TYPE,
				 "group_by"+ESC+LIST_TYPE,
				 "input"
				}
			);			
		
	}

	public String buildOperator(String Operatorname, Map<String, String> args)
	{
		if(!operatorNames.contains(Operatorname))
			throw new IllegalArgumentException("Given operator is unknown: " + Operatorname);
		
		buildPrefix(Operatorname);
		buildArguments(operatorArguments.get(Operatorname.toUpperCase()), args);
		buildSuffix(args.get("input"));
		return builder.toString();
	}
	
	private StringBuilder buildPrefix(String operatorname)
	{
		builder.setLength(0);
		builder.append(operatorname.toUpperCase());
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
			if(!var.equals("input"))
			{
				String[] split = var.split(ESC);
				String value = getValue(split[1], map.get(split[0].toLowerCase()));
				if(value != null)
					builder.append(split[0] + "=" + value + ",");
			}
		builder.deleteCharAt(builder.toString().length() - 1);
		return builder;
	}

	private String getValue(String type, String value)
	{
		if(value == null) return value;
		switch(type)
		{
		case(STRING_TYPE):
			return "'" + value + "'";
		case(LIST_TYPE):
			return "[" + value + "]";
		default: 
			return value;
		}
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
