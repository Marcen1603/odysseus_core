package de.uniol.inf.is.odysseus.scars.objecttracking.prediction.sdf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nfunk.jep.JEP;
import org.nfunk.jep.Node;
import org.nfunk.jep.ParseException;

import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;
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
	private transient JEP expressionParser;
	/** the non-compiled expression string defined as in the grammar */
	private String expression;
	/** the input source name of the target attribute */
	private String targetSource;
	/** the integer array in which the targetPath is being stored, during initAttributePaths(SDFAttributeList schema) */
	private int[] targetPath;
	/** the target variable name taken from the grammar, inclusive the input source name. */
	private String targetVarName;
	/** the target value which will be set during the evaluation of this expression */
	private Object targetValue;
	/** A Mapping betreen source names and the corresponding variable names, which belongs to that source. */
	private Map<String, List<String>> variables = new HashMap<String, List<String>>();
	/** A Mapping between all variable names and their corresponding attribute index paths. */
	private Map<String, int[]> variablePaths = new HashMap<String, int[]>();
	/** the Node object used by the JEP expression to parse and evaluate this expression string */
	private Node node;
	
	/**
	 * 
	 * @param target
	 * @param expression
	 */
	public PredictionExpression(String target, String expression) {
		this.targetVarName = target;
		this.expression = expression.trim();
		
		expressionParser = new JEP();
		expressionParser.setAllowUndeclared(false);
		expressionParser.setAllowAssignment(false);
		expressionParser.addStandardConstants();
		expressionParser.addStandardFunctions();
		
		aquireVariables(expression);
		aquireTargetVariable(target);

		try {
			node = expressionParser.parse(expression);
		} catch (ParseException e) { e.printStackTrace(); }
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
		List<String> varNames = variables.get(source);
		if(varNames == null) {
			varNames = new ArrayList<String>();
			variables.put(source, varNames);
		}
		varNames.add(variableName);
		expressionParser.addVariable(variableName, null);
	}
	
	/**
	 * 
	 * @param schemata
	 */
	public void initAttributePaths(SDFAttributeList...schemata) {
		for(SDFAttributeList schema : schemata) {
			initAttributePaths(schema);
		}
	}
	
	/**
	 * 
	 * @param schema
	 */
	public void initAttributePaths(SDFAttributeList schema) {
		String sourceName = getSourceName(schema);
		SchemaHelper helper = new SchemaHelper(schema);
		List<String> schemaVariables = variables.get(sourceName);
		if(schemaVariables == null) {
			//schema not requiered by expression, so simple ignore
			return;
		}
		for(String var : schemaVariables) {
			SchemaIndexPath path = helper.getSchemaIndexPath(var);
			int[] p = path.toArray();
			replaceWithRelativeIndex(schema, p);
			variablePaths.put(var, p);
		}
		
		if(sourceName.equals(targetSource)) {
			SchemaIndexPath path = helper.getSchemaIndexPath(targetVarName);
			int[] p = path.toArray();
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
//	protected int[] resolveAttributePath(SDFAttributeList schema, String attributeName) {
//		String attr = attributeName.split("\\.", 2)[1];
//		String[] attrParts = attr.split("\\:");
//		int[] indices = new int[attrParts.length];
//		SDFAttributeList current = schema;
//		
//		for(int depth=0; depth<attrParts.length; depth++) {
//			for(int index=0; index<current.getAttributeCount(); index++) {
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
	protected void replaceWithRelativeIndex(SDFAttributeList schema, int[] indices) {
		boolean isListBefore = false;
		SDFAttributeList current = schema;
		for(int index=0; index<indices.length; index++) {
			SDFAttribute attr = current.get(indices[index]);
			if(isListBefore) {
				indices[index] = -1;
				isListBefore = false;
			}
			if(attr.getDatatype().getURI(false).equals("List")) {
				isListBefore = true;
			}

			current = attr.getSubattributes();
		}
	}
	
	/**
	 * 
	 * @param schema
	 * @return
	 */
	public List<String> getAttributeNames(SDFAttributeList schema) {
		List<String> attrNames = variables.get(getSourceName(schema));
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
		return variablePaths.get(attrName);
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
		expressionParser.addVariable(attrName, value);
	}
	
	/**
	 * 
	 */
	public void evaluate() {
		try {
			targetValue = expressionParser.evaluate(node);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param schema
	 * @param index
	 */
	public void replaceVaryingAttributeIndex(SDFAttributeList schema, int index) {
		
		String sourceName = getSourceName(schema);
		List<String> schemaVarNames = variables.get(sourceName);
		if(schemaVarNames == null) {
			//schema not requiered by expression, so simple ignore
			return;
		}
		SchemaHelper helper = new SchemaHelper(schema);
		for(String var : schemaVarNames) {
			int[] path = helper.getSchemaIndexPath(var).toArray();
			replaceWithRelativeIndex(schema, path);
			replaceIndex(path, index);
			
			variablePaths.put(var, path);
		}
		if(sourceName.equals(targetSource)) {
			int[] path = helper.getSchemaIndexPath(targetVarName).toArray();
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
	private void replaceIndex(int[] path, int index) {
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
	public String getSourceName(SDFAttributeList schema) {
		return schema.getAttribute(0).getSourceName();
	}
	
	public String toString() {
		StringBuffer s = new StringBuffer();
		s.append("Class: " + super.toString());
		s.append("\nExpression: " + expression);
		s.append("\nTarget: ");
		s.append("\n\tVarName: " + targetVarName);
		s.append("\n\tPath: ");
		for(int index : targetPath) {
			s.append(index + " ");
		}
		s.append("\nVariablePaths:");
		for(Entry<String, int[]> entry : variablePaths.entrySet()) {
			s.append("\n\t" + entry.getKey() + ": ");
			int[] list = entry.getValue();
			for(int index : list) {
				s.append(index + " ");
			}
		}
		s.append("\nVariableValues:");
		for(Entry<String, List<String>> entry : variables.entrySet()) {
			for(String var : entry.getValue()) {
				s.append("\n\t"+var + " := ");
				s.append(expressionParser.getVar(var).getValue());
			}
		}
		return s.toString();
	}
	
	public static void main(String[] args) {
		SDFAttributeList scan = new SDFAttributeList();
		
		SDFAttribute list = new SDFAttribute("a.list");
		list.setDatatype(SDFDatatypeFactory.getDatatype("List"));
		
		SDFAttribute obj = new SDFAttribute("obj");
		obj.setDatatype(SDFDatatypeFactory.getDatatype("Record"));
		
		SDFAttribute pos = new SDFAttribute("pos");
		pos.setDatatype(SDFDatatypeFactory.getDatatype("Record"));
		
		SDFAttribute x = new SDFAttribute("x");
		x.setDatatype(SDFDatatypeFactory.getDatatype("MV"));
		SDFAttribute y = new SDFAttribute("y");
		y.setDatatype(SDFDatatypeFactory.getDatatype("MV"));
		SDFAttribute z = new SDFAttribute("z");
		z.setDatatype(SDFDatatypeFactory.getDatatype("MV"));
		
		SDFAttributeList time = new SDFAttributeList();
		
		SDFAttribute currentTime = new SDFAttribute("b.currentTime");
		currentTime.setDatatype(SDFDatatypeFactory.getDatatype("MV"));
		
		scan.add(list);
		list.addSubattribute(obj);
		obj.addSubattribute(pos);
		pos.addSubattribute(x);
		pos.addSubattribute(y);
		pos.addSubattribute(z);
		
		time.add(currentTime);
		
		
		String expression = "a.list:obj:pos:x + a.list:obj:pos:y + a.list:obj:pos:z * 10";
		PredictionExpression p = new PredictionExpression("a.list:obj:pos:y", expression);
		p.initAttributePaths(scan);
		p.initAttributePaths(time);
		System.out.println("TEST: BEFORE INDEX REPLACE:");
		System.out.println(p);
		p.replaceVaryingAttributeIndex(scan, 2);
		System.out.println("TEST: AFTER INDEX REPLACE:");
		System.out.println(p);
		
		p.bindVariable("a.list:obj:pos:x", 10);
		p.bindVariable("a.list:obj:pos:y", 10);
		p.bindVariable("a.list:obj:pos:z", 10);
		p.bindVariable("b.currentTime", 3);
		
		p.evaluate();
		System.out.println("TEST: AFTER PREDICTION:");
		System.out.println(p.getTargetValue());
		System.out.println(p);
	}
}
