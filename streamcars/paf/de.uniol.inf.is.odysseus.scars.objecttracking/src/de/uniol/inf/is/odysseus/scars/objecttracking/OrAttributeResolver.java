package de.uniol.inf.is.odysseus.scars.objecttracking;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

public class OrAttributeResolver {
	
	
	public static int[] resolveIndices(SDFAttributeList schema, String...path) {
		int[] indices = new int[path.length];
		
		SDFAttributeList currentAttr = schema;
		for(int i=0; i<path.length; i++) {
			indices[i] = resolveIndex(currentAttr, path[i]);
			currentAttr = resolveAttribute(currentAttr, path[i]).getSubattributes();
		}
		return indices;
	}
	
	// TIMO START
	
	public static int[] getAttributePath( SDFAttributeList schema, String absoluteAttributeName ) {
		String sensorName = getSensorName(absoluteAttributeName);
		String relativeAttributeName = getAttributeName(absoluteAttributeName);
		String[] paths = getAttributeNamePath(relativeAttributeName);
		
		int[] indices = new int[paths.length];
		SDFAttributeList currentAttributeList = schema;
		for( int i = 0; i < indices.length; i++ ) {
			indices[i] = getIndexOf( currentAttributeList, paths[i]);
			currentAttributeList = currentAttributeList.get(indices[i]).getSubattributes();
		}
		
		return indices;
	}
	
	public static int getIndexOf( SDFAttributeList list, String attributeName ) {
		for( int i = 0; i < list.getAttributeCount(); i++ ) {
			if( list.getAttribute(i).getAttributeName().equals(attributeName))
				return i;
		}
		return -1;
	}
	
	public static int[] getAttributePath( SDFAttributeList schema, String sensorName, String relativeAttributeName ) {
		return getAttributePath(schema, sensorName + "." + relativeAttributeName );
	}
	
	public static String getSensorName( String fullAttributeName ) {
		String[] name = fullAttributeName.split("\\.");
		if( name.length == 2 ) {
			return name[0];
		} else {
			// fullAttributeName hatte keinen Sensornamen...
			return null;
		}
	}
	
	public static String getAttributeName( String fullAttributeName ) {
		String[] name = fullAttributeName.split("\\.");
		if( name.length == 2 ) {
			return name[1];
		} else {
			// fullAttributeName hatte keinen Sensornamen...
			return name[0];
		}
	}
	
	// TIMO END
	
	public static String[] getAttributeNamePath( String relativeAttributeName ) {
		String[] parts = relativeAttributeName.split("\\:");
		String[] result = new String[parts.length];
		
		for( int i = 0; i < parts.length; i++ ) {
			if( i == 0 ) 
				result[i] = parts[i];
			else {
				result[i] = result[i-1] + ":" + parts[i];
			}
		}
		return result;
	}
	
	private static SDFAttribute resolveAttribute(SDFAttributeList attr, String name) {
//		System.out.println("resolveAttribute: " + attr + ", name: " + name);
		for(SDFAttribute a : attr) {
			if(a.getAttributeName().equals(name)) {
//				System.out.println("return: " + a);
				return a;
			}
		}
		return null;
	}
	
	public static<M extends IMetaAttribute> Object resolveTuple(RelationalTuple<?> root, int[] path) {
		Object currentTuple = root;
		for(int index=0; index<path.length; index++) {
			if(currentTuple instanceof RelationalTuple) {
				currentTuple = ((RelationalTuple<?>) currentTuple).getAttributes()[path[index]];
			} else {
				return null;
			}
		}
		return currentTuple;
	}
	
	private static int resolveIndex(SDFAttributeList attr, String name) {
		for(int index=0; index<attr.getAttributeCount(); index++) {
			SDFAttribute a = attr.get(index);
			if(a.getAttributeName().equals(name)) {
//				SDFAttribute listType = SDFDatatypeFactory.getDatatype("List");
				return index;
			}
		}
		return -1;
	}
	
	public static SDFAttributeList getSubSchema(SDFAttributeList root, String[] path) {
		SDFAttributeList currentSchema = root;
		for(String p : path) {
			currentSchema = resolveAttribute(currentSchema, p).getSubattributes(); 
		}
		
		return currentSchema;
	}
	
	public static SDFAttributeList getSubSchema(SDFAttributeList root, int[] path) {
		SDFAttributeList currentSchema = root;
		for(int p : path) {
			currentSchema = currentSchema.get(p).getSubattributes();
		}
		
		return currentSchema;
	}
	
	public static void setAttribute(RelationalTuple<?> source, int[] path, Object value) {
		RelationalTuple<?> current = source;
		
		for(int depth=0; depth<path.length-1; depth++) {
			if(current instanceof RelationalTuple) {
				current = (RelationalTuple<?>)current.getAttribute(path[depth]);
			}
		}
		current.setAttribute(path[path.length-1], value);
	}
	
	public static int indexOfAttribute(RelationalTuple<?> parent, Object attr) {
		Object[] children = parent.getAttributes();
		for(int index=0; index<children.length; index++) {
			if(attr.equals(children[index])) {
				return index;
			}
		}
		return -1;
	}
	
	public static <M extends IProbability> double[] getMeasurementValues(ArrayList<int[]> pathsOfMeasurements, MVRelationalTuple<M> tuple) {
		ArrayList<Double> tmp = new ArrayList<Double>();
		for(int[] path : pathsOfMeasurements) {
			tmp.add((Double) resolveTuple(tuple, path));
		}
		double[] retArr = new double[tmp.size()];
		for(int i = 0; i < tmp.size(); i++) {
			retArr[i] = tmp.get(i);
		}
		return retArr;
	}
	
	public static ArrayList<int[]> getPathsOfMeasurements(SDFAttributeList attrList) {
		ArrayList<int[]> tmp = new ArrayList<int[]>();
		
		return getPathsOfMeasurementValue(attrList, null, tmp);
	}
	
	private static ArrayList<int[]> getPathsOfMeasurementValue(SDFAttributeList attrList, int[] preliminaryPath, ArrayList<int[]> tmp) {
		
		//Erster Durchlauf:
		if (preliminaryPath == null) {
			preliminaryPath = new int[0];
		}
		
		int counter = 0;
		for (SDFAttribute attr : attrList) {
			int[] singlePath = new int[preliminaryPath.length+1];
			for (int i=0; i<singlePath.length-1; i++) {
				singlePath[i] = preliminaryPath[i];	
			}
			singlePath[singlePath.length-1] = counter;
			
			if (SDFDatatypes.isMeasurementValue(attr.getDatatype())){
				tmp.add(singlePath);
			}
			if (!SDFDatatypes.isMeasurementValue(attr.getDatatype()) && attr.getSubattributes() != null) {
				tmp = getPathsOfMeasurementValue(attr.getSubattributes(), singlePath, tmp);
			}
			counter++;
		}
		
		return tmp;
	}
	
	
//	public static void main(String[] args) {
//		SDFAttributeList list = new SDFAttributeList("Scan");
//		SDFAttribute attr1 = new SDFAttribute("list1");
//		SDFAttribute attr2 = new SDFAttribute("list2");
//	
//		SDFAttribute attr11 = new SDFAttribute("attr11");
//		SDFAttribute attr12 = new SDFAttribute("attr12");
//		SDFAttribute attr21 = new SDFAttribute("attr21");
//		
//		SDFAttribute attr211 = new SDFAttribute("attr211");
//		SDFAttribute attr2111 = new SDFAttribute("attr2111");
//		SDFAttribute attr2112 = new SDFAttribute("attr2112");
//		
//		list.add(attr1);
//		
//		list.add(attr2);
//		attr1.addSubattribute(attr11);
//		attr1.addSubattribute(attr12);
//		attr2.addSubattribute(attr21);
//		
//		attr21.addSubattribute(attr211);
//		attr211.addSubattribute(attr2111);
//		attr211.addSubattribute(attr2112);
//		
//		attr1.setDatatype(SDFDatatypeFactory.getDatatype(""));
//		attr2.setDatatype(SDFDatatypeFactory.getDatatype(""));
//		attr11.setDatatype(SDFDatatypeFactory.getDatatype(""));
//		attr12.setDatatype(SDFDatatypeFactory.getDatatype(""));
//		attr21.setDatatype(SDFDatatypeFactory.getDatatype(""));
//		attr211.setDatatype(SDFDatatypeFactory.getDatatype(""));
//		
//		attr2111.setDatatype(SDFDatatypeFactory.getDatatype("MV"));
//		attr2112.setDatatype(SDFDatatypeFactory.getDatatype("MV"));
//
//		ArrayList<int[]> ausgabe = getPathsOfMeasurements(list);
//
//		for (int i=0;i<ausgabe.size(); i++) {
//			int[] current = ausgabe.get(i);
//			for (int j=0; j<current.length; j++) {
//				System.out.print(current[j]);
//			}
//			System.out.println();
//		}
//	}

}
