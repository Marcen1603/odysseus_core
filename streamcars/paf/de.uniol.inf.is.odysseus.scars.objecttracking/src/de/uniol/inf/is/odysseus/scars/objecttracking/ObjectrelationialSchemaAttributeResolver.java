package de.uniol.inf.is.odysseus.scars.objecttracking;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class ObjectrelationialSchemaAttributeResolver {
	
	
	public static int[] resolveIndices(SDFAttributeList schema, String...path) {
		int[] indices = new int[path.length];
		
		SDFAttributeList currentAttr = schema;
		for(int i=0; i<path.length; i++) {
			indices[i] = resolveIndex(currentAttr, path[i]);
			currentAttr = resolveAttribute(currentAttr, path[i]).getSubattributes();
		}
		return indices;
	}
	
	private static SDFAttribute resolveAttribute(SDFAttributeList attr, String name) {
		System.out.println("resolveAttribute: " + attr + ", name: " + name);
		for(SDFAttribute a : attr) {
			if(a.getAttributeName().equals(name)) {
				System.out.println("return: " + a);
				return a;
			}
		}
		return null;
	}
	
	public static<M extends IMetaAttribute> Object resolveTuple(RelationalTuple<?> root, int[] path) {
		Object currentTuple = root;
		for(int index=0; index<path.length; index++) {
			if(currentTuple instanceof RelationalTuple<?>) {
				currentTuple = ((RelationalTuple<?>) currentTuple).getAttributes()[path[index]];
			} else {
				return null;
			}
		}
		return currentTuple;
	}
	
	private static int resolveIndex(SDFAttributeList attr, String name) {
		System.out.println("resolveIndex: " + attr + ", name: " + name);
		for(int index=0; index<attr.getAttributeCount(); index++) {
			SDFAttribute a = attr.get(index);
			if(a.getAttributeName().equals(name)) {
				System.out.println("return index from: " + a + ", " + index);
				return index;
			}
		}
		return -1;
	}
	
	public static void main(String[] args) {
		SDFAttributeList list = new SDFAttributeList("Scan");
		SDFAttribute attr1 = new SDFAttribute("list1");
		SDFAttribute attr2 = new SDFAttribute("list2");
	
		SDFAttribute attr11 = new SDFAttribute("attr1");
		SDFAttribute attr12 = new SDFAttribute("attr12");
		SDFAttribute attr21 = new SDFAttribute("attr21");
		
		SDFAttribute attr211 = new SDFAttribute("attr211");
		SDFAttribute attr2111 = new SDFAttribute("attr2111");
		SDFAttribute attr2112 = new SDFAttribute("attr2112");
		
		list.add(attr1);
		list.add(attr2);
		attr1.addSubattribute(attr11);
		attr1.addSubattribute(attr12);
		attr2.addSubattribute(attr21);
		
		attr21.addSubattribute(attr211);
		attr211.addSubattribute(attr2111);
		attr211.addSubattribute(attr2112);
		
		int[] indices = resolveIndices(list, new String[] {"list2", "attr21", "attr211", "attr2112"});
		int[] indices2 = resolveIndices(list, "list1");
		for(int index : indices) {
			System.out.println(index);
		}
		
		System.out.println(indices2[0]);
		
		MVRelationalTuple<IProbability> root = new MVRelationalTuple<IProbability>(2);
		MVRelationalTuple<IProbability>  tuple1 = new MVRelationalTuple<IProbability>(1);
		MVRelationalTuple<IProbability>  tuple2 = new MVRelationalTuple<IProbability>(1);
		MVRelationalTuple<IProbability>  tuple11 = new MVRelationalTuple<IProbability>(1);
		root.setAttribute(0, tuple1);
		root.setAttribute(1, tuple2);
		tuple1.setAttribute(0, tuple11);
		tuple2.setAttribute(0, "tuple2Attribute");
		tuple11.setAttribute(0, "tuple11attribute");

		System.out.println("===== get tuple test");
		
//		System.out.println(root + ", isTuple: "+ (root instanceof RelationalTuple<?>));
//		System.out.println(root.getAttribute(0) + ", isTuple: "+ (root.getAttributes()[0] instanceof MVRelationalTuple<?>));
//		System.out.println(tuple1.getAttribute(0) + ", isTuple: "+ (tuple1.getAttributes()[0] instanceof MVRelationalTuple<?>));
		
//		System.out.println(tuple11.getAttribute(0) + ", isTuple: "+ (tuple11.getAttributes()[0] instanceof RelationalTuple<?>));
		
		
		System.out.println(resolveTuple(root, new int[] {0, 0, 0}));
		
		
	}

}
