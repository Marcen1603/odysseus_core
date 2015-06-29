package de.uniol.inf.is.odysseus.query.transformation.java.utils;

public class TransformIntegerArray {


	public static String getCodeForIntegerArray(int[] restrictList ){
		int i=1;
		
		StringBuilder code = new StringBuilder();
		code.append("new int[]{");
		
		
		for(Integer pos : restrictList){
			code.append(pos);
			
			if((i==1 && restrictList.length > 1) || i!=restrictList.length)
			code.append(",");
			i++;
		}
		
		code.append("}");
		return code.toString();
		
	}
}
