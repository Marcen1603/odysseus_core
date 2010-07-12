package de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nfunk.jep.JEP;
import org.nfunk.jep.ParseException;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class PredictionExpression {
	
	private static final String variableRegexp = "(\\p{Alpha}[\\p{Alnum}\\_:\\.]*)([^\\(\\p{Alnum}_:\\.\"]|$)";
	private static final Pattern variablePattern = Pattern.compile(variableRegexp);

	private transient JEP expressionParser;

	private String expression;
	private List<String> varNames;
	
	private String targetSource;
	private int[] targetPath;
	private String targetVarName;
	private Map<String, List<String>> variables;
	private Map<String, List<int[]>> paths;
	
	
	public PredictionExpression(String target, String expression) {
		this.targetVarName = target;
		this.expression = expression.trim();
		varNames = new ArrayList<String>();
		
		expressionParser = new JEP();
		expressionParser.setAllowUndeclared(false);
		expressionParser.setAllowAssignment(false);
		expressionParser.addStandardConstants();
		expressionParser.addStandardFunctions();
		
		aquireVariables(expression);

		try {
			expressionParser.parse(expression);
		} catch (ParseException e) { e.printStackTrace(); }
	}
	
	protected void aquireVariables(String expression) {
		Matcher variableMatcher = variablePattern.matcher(expression);
		while (variableMatcher.find()) {
			String variable = variableMatcher.group(1);
			createVariable(variable);
		}
	}
	
	protected void createVariable(String variableName) {
		String[] varSplit = variableName.split("\\.", 2);
		String source = varSplit[0];
		List<String> varNames = variables.get(source);
		if(varNames == null) {
			varNames = new ArrayList<String>();
			variables.put(source, varNames);
		}
		varNames.add(variableName);
		expressionParser.addVariable(variableName, null);
	}
	
	public void initAttributPaths(SDFAttributeList schema) {
		
	}
	
	protected int[] resolveAttributePath(SDFAttributeList schema, String attributeName) {
		String attr = attributeName.split("\\.", 2)[1];
		String[] attrParts = attr.split("\\:");
		int[] indices = new int[attrParts.length];
		SDFAttributeList current = schema;
		
		for(int depth=0; depth<attrParts.length; depth++) {
			for(int index=0; index<current.getAttributeCount(); index++) {
				SDFAttribute a = current.get(index);
				if(a.getAttributeName().equals(attrParts[depth])) {
					indices[depth] = index;
					current = a.getSubattributes();
					break;
				}
			}
		}
		return indices;
	}
	
	public int[][] getAttributePaths(SDFAttributeList schema) {
		return null;
	}
	
	public void replaceVaryingAttributeIndex(int index) {
		
	}
	
	public String getSourceName(SDFAttributeList schema) {
		String firstAttrName = schema.getAttribute(0).getAttributeName();
		return firstAttrName.split("\\.", 2)[0];
	}
}
