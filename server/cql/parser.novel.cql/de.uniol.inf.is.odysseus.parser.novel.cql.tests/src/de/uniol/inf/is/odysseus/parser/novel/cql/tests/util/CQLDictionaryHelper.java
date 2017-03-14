package de.uniol.inf.is.odysseus.parser.novel.cql.tests.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.parser.novel.cql.CQLDictionary;

public class CQLDictionaryHelper extends CQLDictionary 
{

	public CQLDictionaryHelper() 
	{
		super.attributes = new HashMap<>();
		String src1 = "stream1";
		String src2 = "stream2";
		String src3 = "stream3";
		String src4 = "stream4";
		
		SDFAttribute[] attributes = 
			{
					new SDFAttribute(src1, "attr1", new SDFDatatype("Integer")),
					new SDFAttribute(src1, "attr2", new SDFDatatype("String")),
					new SDFAttribute(src2, "attr3", new SDFDatatype("Integer")),
					new SDFAttribute(src2, "attr4", new SDFDatatype("String")),
					new SDFAttribute(src3, "attr5", new SDFDatatype("Integer")),
					new SDFAttribute(src3, "attr6", new SDFDatatype("String")),
					new SDFAttribute(src4, "attr1", new SDFDatatype("String")),
					new SDFAttribute(src4, "attr2", new SDFDatatype("String"))
					// ...
			};
		
		add(Arrays.asList(attributes));
	}
	
	
//	public CQLDictionaryHelper(String src, String ... attribute) 
//	{
//		super.attributes = new HashMap<>();
//		SDFAttribute[] attributes = new SDFAttribute[datatypes.length];
//		for(int i = 0; i < datatypes.length; i++)
//		{
//			attributes[i] = new SDFAttribute(src, "attr"+(i+1), new SDFDatatype(datatypes[i]));
//		}
//		add(Arrays.asList(attributes));
//	}
	
	public CQLDictionaryHelper(String src, String ... attributes) 
	{
		super.attributes = new HashMap<>();
//		for(String s : attributes)
//			System.out.println(s);
		List<SDFAttribute> attrs = new ArrayList<>();
		List<String> processed = new ArrayList<>();
		for(int i = 0; i < attributes.length; i++)
		{
			if(!processed.contains(attributes[i]))
			{	
				processed.add(attributes[i]);
				attrs.add(new SDFAttribute(src, attributes[i], new SDFDatatype("INTEGER")));
			}
		}
		add(attrs);
	}
	
}
