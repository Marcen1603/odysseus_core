package de.uniol.inf.is.odysseus.parser.novel.cql.generator

import java.util.List

class SourceStruct 
{
		public String sourcename;
		public List<AttributeStruct> attributes;
		public List<String> aliases;
		public boolean internal;
		
		def addRenamedAttributes(List<String> list)
		{
			var newStructs = newArrayList
			for(AttributeStruct struct : attributes)
				for(var i = 1; i < list.size; i++)
				{
					if(i % 2 == 1)
					{
						var name = list.get(i).split("\\.").get(1)
						var newStruct = new AttributeStruct()
						newStruct.attributename = name
						newStruct.datatype = struct.datatype
						newStruct.sourcename = struct.sourcename	
						newStruct.aliases = newArrayList
						newStructs.add(newStruct)
					}
				}
			attributes.addAll(newStructs)
		}
		
		def update(AttributeStruct attribute)
		{
			var b = true
			var iter = attributes.iterator
			while(b && iter.hasNext)
			{
				if(iter.next.equals(attribute))
				{
					iter.remove
					b = false
				}
					
			}
			attributes.add(attribute)
		}
		
		def AttributeStruct findbyName(String name)
		{
			if(name.contains("."))
			{
				println("findbyName::"+name)
				var split = name.split("\\.")
				if(split.get(0).equals(this.sourcename))
				{
					for(AttributeStruct attribute : attributes)
						if(attribute.attributename.equals(split.get(1)))
							return attribute
						else if(attribute.aliases.contains(split.get(1)))
							return attribute
							
				}
				else if(this.aliases.contains(split.get(0)))
				{
					for(AttributeStruct attribute2 : attributes)
						if(attribute2.attributename.equals(split.get(1)))
							return attribute2
						else if(attribute2.aliases.contains(split.get(1)))
							return attribute2
							
				}
			}
			for(AttributeStruct attribute : attributes)
				if(attribute.attributename.equals(name))
					return attribute
				else if(attribute.aliases.contains(name))
					return attribute
			return null
		}
		
		def List<AttributeStruct> findByType(String datatype)
		{
			var list = newArrayList
			for(AttributeStruct attribute : attributes)
			{
				if(attribute.datatype.toLowerCase.equals(datatype.toLowerCase))
					list.add(attribute)
			}
			return list
		}
		
		def AttributeStruct getStarttimeStamp()
		{
			var a = findByType('StartTimestamp')
			return  if(!a.empty) a.get(0) else null
		}
		
		def AttributeStruct getEndtimeStamp()
		{
			var a = findByType('EndTimestamp')
			return  if(!a.empty) a.get(0) else null
		}
		
		def boolean containsAttribute(String attributename)
		{
			return if(findbyName(attributename) !== null) true else false
		}
		
		override equals(Object obj) 
		{
			if (obj instanceof SourceStruct)
				if(this.sourcename.equals((obj as SourceStruct).sourcename)
					&& this.attributes.equals((obj as SourceStruct).attributes)
					&& this.aliases.equals((obj as SourceStruct).aliases)
					&& this.internal.equals((obj as SourceStruct).internal)
				  )	
					return true			
			return false
		}
		
		override hashCode() { return 0 }
		
		override toString() 
		{
			return sourcename 
//			+ System.getProperty("line.separator") + " attributes " + attributes.toString
//			+ System.getProperty("line.separator") + " aliases " + aliases.toString
//			+ System.getProperty("line.separator") + " internal " + internal
		}
}