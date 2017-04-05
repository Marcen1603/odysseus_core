package de.uniol.inf.is.odysseus.parser.novel.cql.generator

import java.util.List

class AttributeStruct 
{
		public String attributename;
		public String sourcename;
		public List<String> prefixes
		public String datatype;
		public List<String> aliases;
		
		override equals(Object obj) 
		{
			if(obj instanceof AttributeStruct)
				if(this.attributename.equals((obj as AttributeStruct).attributename)
					&& this.sourcename.equals((obj as AttributeStruct).sourcename)
					&& this.datatype.equals((obj as AttributeStruct).datatype)
				)
					return true
			return false
		}
		
		override hashCode() { return 0 }
		
		override toString() 
		{
			return "attributename " + attributename
			+ System.getProperty("line.separator") + " sourcename " + sourcename 
			+ System.getProperty("line.separator") + " datatype " + datatype 	
			+ System.getProperty("line.separator") + " aliases " + aliases.toString
		}
}