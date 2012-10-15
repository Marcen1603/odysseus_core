/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.scars.metadata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.mep.Variable;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.mep.MEP;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SchemaHelper;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SchemaIndexPath;
/**
 * 
 * @author Benjamin G
 *
 */
public class PredictionExpression {
	
	/** the regular expression for the pattern to identify the variable names */
	private static final String variableRegexp = "(\\p{Alpha}[\\p{Alnum}\\_:\\.]*)([^\\(\\p{Alnum}_:\\.\"]|$)";
	private static final Pattern variablePattern = Pattern.compile(variableRegexp);
	/** the JEP parser for executing the expression */
//	private transient JEP expressionParser;
	/** the non-compiled expression string defined as in the grammar */
	private String expressionString;
	/** the input source name of the target attribute */
	private String targetSource;
	/** the integer array in which the targetPath is being stored, during initAttributePaths(SDFSchema schema) */
	private int[] targetPath;
	/** the target variable name taken from the grammar, inclusive the input source name. */
	private String targetVarName;
	/** the target value which will be set during the evaluation of this expression */
	private Object targetValue;
	/** A Mapping betreen source names and the corresponding variable names, which belongs to that source. */
	private Map<String, List<String>> variableSourceMapping = new HashMap<String, List<String>>();
	/** A Mapping between all variable names and their corresponding attribute index paths. */
	private Map<String, int[]> variablePathMapping = new HashMap<String, int[]>();
	/** the Node object used by the JEP expression to parse and evaluate this expression string */
//	private Node node;
	
	/** */
	private IExpression<?> expression;
	private Set<Variable> variables;
	
	/**
	 * 
	 * @param target
	 * @param expressionString
	 */
	public PredictionExpression(String expressionString) {
		this(null, expressionString);
	}
	
	
	public PredictionExpression(String target, String expressionString) {
		this.targetVarName = target;
		this.expressionString = expressionString.trim();
		
//		expressionParser = new JEP();
//		expressionParser.setAllowUndeclared(false);
//		expressionParser.setAllowAssignment(false);
//		expressionParser.addStandardConstants();
//		expressionParser.addStandardFunctions();
		
		aquireVariables(expressionString);
		if(target != null) {
			aquireTargetVariable(target);
		}
		try {
			expression = MEP.getInstance().parse(expressionString);
			variables = expression.getVariables();
		} catch (de.uniol.inf.is.odysseus.core.mep.ParseException e1) {
			e1.printStackTrace();
		}

//		try {
//			node = expressionParser.parse(expressionString);
//		} catch (ParseException e) { e.printStackTrace(); }
	}
	
	/**
	 * 
	 * @param expression
	 */
	protected void aquireVariables(String expression) {
		Matcher variableMatcher = variablePattern.matcher(expression);
		while (variableMatcher.find()) {
			String variable = variableMatcher.group(1);
			createVariable(variable);
		}
	}
	
	/**
	 * 
	 * @param targetAttributeName
	 */
	protected void aquireTargetVariable(String targetAttributeName) {
		String[] varSplit = targetAttributeName.split("\\.", 2);
		targetSource = varSplit[0];
	}
	
	/**
	 * 
	 * @param variableName
	 */
	protected void createVariable(String variableName) {
		String[] varSplit = variableName.split("\\.", 2);
		String source = varSplit[0];
		List<String> varNames = variableSourceMapping.get(source);
		if(varNames == null) {
			varNames = new ArrayList<String>();
			variableSourceMapping.put(source, varNames);
		}
		varNames.add(variableName);
//		expressionParser.addVariable(variableName, null);
	}
	
	/**
	 * 
	 * @param schemata
	 */
	public void initAttributePaths(SDFSchema...schemata) {
		for(SDFSchema schema : schemata) {
			initAttributePaths(schema);
		}
	}
	
	/**
	 * 
	 * @param schema
	 */
	public void initAttributePaths(SDFSchema schema) {
		String sourceName = getSourceName(schema);
		SchemaHelper helper = new SchemaHelper(schema);
		List<String> schemaVariables = variableSourceMapping.get(sourceName);
		if(schemaVariables == null) {
			//schema not requiered by expression, so simple ignore
			return;
		}
		for(String var : schemaVariables) {
			SchemaIndexPath path = helper.getSchemaIndexPath(var);
			int[] p = path.toArray(true);
			replaceWithRelativeIndex(schema, p);
			variablePathMapping.put(var, p);
		}
		
		if(sourceName.equals(targetSource)) {
			SchemaIndexPath path = helper.getSchemaIndexPath(targetVarName);
			int[]p = path.toArray(true);
			replaceWithRelativeIndex(schema, p);
			targetPath = p;
		}
	}
	
	/**
	 * 
	 * @param schema
	 * @param attributeName
	 * @return
	 */
//	protected int[] resolveAttributePath(SDFSchema schema, String attributeName) {
//		String attr = attributeName.split("\\.", 2)[1];
//		String[] attrParts = attr.split("\\:");
//		int[] indices = new int[attrParts.length];
//		SDFSchema current = schema;
//		
//		for(int depth=0; depth<attrParts.length; depth++) {
//			for(int index=0; index<current.size(); index++) {
//				SDFAttribute a = current.get(index);
//				if(a.getAttributeName().equals(attrParts[depth])) {
//					indices[depth] = index;
//					current = a.getSubattributes();
//					break;
//				}
//			}
//		}
//		replaceWithRelativeIndex(schema, indices);
//		
//		return indices;
//	}
	
	/**
	 * 
	 * @param schema
	 * @param indices
	 */
	protected void replaceWithRelativeIndex(SDFSchema schema, int[] indices) {
		boolean isListBefore = false;
		SDFSchema current = schema;
		for(int index=0; index<indices.length; index++) {
			// maybe current is null, since the last attribute
			// was of type set with primitive type. In this case
			// this must be the last run of this loop.
			SDFAttribute attr = null;
			if(current != null){
				attr = current.get(indices[index]);
			}

			if(isListBefore) {
				indices[index] = -1;
				isListBefore = false;
			}
			
			// in the case that attr == null this is 
			// last run of this loop
			if(attr != null && attr.getDatatype().isMultiValue()) {
				isListBefore = true;
			}

			// if there is still an index left, the list attribute
			// must be of complex type. Otherwise no more indices
			// are left. 
			// example: if we search for a.b:c:d and c is a list attribute
			// than this attribute must be of complex type. Otherwise the
			// we would have searched for a.b:c In this case the subschema
			// of c is null however this loop will not be processed any more
			// since we also reached the last index of our path. This means
			// that in the following line we can set current to null without
			// the danger of a NullpointerException, this this must be the 
			// last run of the loop.
			current = attr.getDatatype().getSchema();
		}
	}
	
	/**
	 * 
	 * @param schema
	 * @return
	 */
	public List<String> getAttributeNames(SDFSchema schema) {
		List<String> attrNames = variableSourceMapping.get(getSourceName(schema));
		if(attrNames == null) {
			return new ArrayList<String>();
		}
		return attrNames;
	}
	
	/**
	 * 
	 * @param attrName
	 * @return
	 */
	public int[] getAttributePath(String attrName) {
		return variablePathMapping.get(attrName);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTargetAttributeName() {
		return targetVarName;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getTargetSource() {
		return targetSource;
	}
	
	/**
	 * 
	 * @return
	 */
	public int[] getTargetPath() {
		return targetPath;
	}
	
	/**
	 * 
	 * @return
	 */
	public Object getTargetValue() {
		return targetValue;
	}
	
	public double getTargetDoubleValue() {
		return (((Number) this.targetValue)).doubleValue();
	}
	
	/**
	 * 
	 * @param attrName
	 * @param value
	 */
	public void bindVariable(String attrName, Object value) {
//		int[] path = getAttributePath(attrName);
//		System.out.println("");
//		System.out.print("bind: " + attrName + ", " + value + "Path: ");
//		for(int p : path) { System.out.print(p + " "); }
//		expressionParser.addVariable(attrName, value);
		
		for(Variable var : variables) {
			if(var.getIdentifier().equals(attrName)) {
				var.bind(value);
			}
		}
		
	}
	
	/**
	 * 
	 */
	public void evaluate() {
//		try {
//			targetValue = expressionParser.evaluate(node);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
		targetValue = expression.getValue();
	}
	
	public String getExpression() {
		return expressionString;
	}

	/**
	 * 
	 * @param schema
	 * @param index
	 */
	public void replaceVaryingAttributeIndex(SDFSchema schema, int index) {
		
		String sourceName = getSourceName(schema);
		List<String> schemaVarNames = variableSourceMapping.get(sourceName);
		if(schemaVarNames == null) {
			//schema not requiered by expression, so simple ignore
			return;
		}
		SchemaHelper helper = new SchemaHelper(schema);
		for(String var : schemaVarNames) {
			int[] path = helper.getSchemaIndexPath(var).toArray(true);
			replaceWithRelativeIndex(schema, path);
			replaceIndex(path, index);
			
			variablePathMapping.put(var, path);
		}
		if(sourceName.equals(targetSource)) {
			int[] path = helper.getSchemaIndexPath(targetVarName).toArray(true);
			replaceWithRelativeIndex(schema, path);
			replaceIndex(path, index);
			
			targetPath = path;
		}
		
	}
	
	/**
	 * 
	 * @param path
	 * @param index
	 */
	private static void replaceIndex(int[] path, int index) {
		for(int i=0; i<path.length; i++) {
			if(path[i] == -1) {
				path[i] = index;
			}
		}
	}
	
	/**
	 * 
	 * @param schema
	 * @return
	 */
	public String getSourceName(SDFSchema schema) {
		return schema.getAttribute(0).getSourceName();
	}
	
	@Override
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("Class: " + super.toString());
		s.append("\nExpression: " + expressionString);
		s.append("\nTarget: ");
		s.append("\n\tVarName: " + targetVarName);
		s.append("\n\tPath: ");
		if(targetPath != null) {
			for(int index : targetPath) {
				s.append(index + " ");
			}
		}

		s.append("\nVariablePaths:");
		for(Entry<String, int[]> entry : variablePathMapping.entrySet()) {
			s.append("\n\t" + entry.getKey() + ": ");
			int[] list = entry.getValue();
			for(int index : list) {
				s.append(index + " ");
			}
		}
		s.append("\nVariableValues:");
		for(Entry<String, List<String>> entry : variableSourceMapping.entrySet()) {
			for(String var : entry.getValue()) {
				s.append("\n\t"+var + " := ");
				s.append(expression.getVariable(var).getValue());
			}
		}
		return s.toString();
	}
	
//	public static void main(String[] args) {
//		SDFSchema scan = new SDFSchema();
//		
//		SDFAttribute list = new SDFAttribute("a.list");
//		list.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("List"));
//		
//		SDFAttribute obj = new SDFAttribute("obj");
//		obj.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("Record"));
//		
//		SDFAttribute pos = new SDFAttribute("pos");
//		pos.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("Record"));
//		
//		SDFAttribute x = new SDFAttribute("x");
//		x.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("MV"));
//		SDFAttribute y = new SDFAttribute("y");
//		y.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("MV"));
//		SDFAttribute z = new SDFAttribute("z");
//		z.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("MV"));
//		
//		SDFSchema time = new SDFSchema();
//		
//		SDFAttribute currentTime = new SDFAttribute("b.currentTime");
//		currentTime.setDatatype(SDFDatatypeFactory.createAndReturnDatatype("MV"));
//		
//		scan.add(list);
//		list.addSubattribute(obj);
//		obj.addSubattribute(pos);
//		pos.addSubattribute(x);
//		pos.addSubattribute(y);
//		pos.addSubattribute(z);
//		
//		time.add(currentTime);
//		
//		
//		String expression = "a.list:obj:pos:x + a.list:obj:pos:y + a.list:obj:pos:z * 10 * b.currentTime";
////		expression = "1.5";
//		PredictionExpression p = new PredictionExpression(null, expression);
//		p.initAttributePaths(scan);
//		p.initAttributePaths(time);
//		System.out.println("TEST: BEFORE INDEX REPLACE:");
//		System.out.println(p);
//		p.replaceVaryingAttributeIndex(scan, 0);
//		System.out.println("TEST: AFTER INDEX REPLACE:");
//		System.out.println(p);
//		
//		p.bindVariable("a.list:obj:pos:x", 10);
//		p.bindVariable("a.list:obj:pos:y", 10);
//		p.bindVariable("a.list:obj:pos:z", 10);
//		p.bindVariable("b.currentTime", 3);
//		
//		p.evaluate();
//		System.out.println("TEST: AFTER PREDICTION:");
//		System.out.println("##########value: " + p.getTargetValue());
//		System.out.println(p);
//	}
}
