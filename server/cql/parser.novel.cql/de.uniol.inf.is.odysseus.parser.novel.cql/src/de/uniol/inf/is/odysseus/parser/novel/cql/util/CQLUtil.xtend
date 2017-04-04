package de.uniol.inf.is.odysseus.parser.novel.cql.util

import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Source
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.Attribute
import java.util.List
import java.util.Collection
import de.uniol.inf.is.odysseus.parser.novel.cql.cQL.SimpleSource

class CQLUtil 
{

	static def boolean equality(Object obj1, Object obj2)
	{
		switch obj1
		{
			SimpleSource:
			{
				if(!(obj2 instanceof SimpleSource))
					return false
				if(obj1.name != (obj2 as SimpleSource).name)
					return false
//				if(obj1.name != (obj2 as Source).name)
//					return false	
				return true
			}
			Attribute:
			{
				if(!(obj2 instanceof Attribute))
					return false
				if(obj1.name != (obj2 as Attribute).name)
					return false
				return true
			}
		}
		return false
	}
	
//	static def List<?> merge(Collection<?> l1, Collection<?> l2)
//	{
//		var l = l1
//		if(l1.size > l2.size)
//		{
//			for(Object o : l2)
//			{
//				for(Object p : l1)
//				{
//					if(!equality(o, p))
//					{
//						l.add(o)
//					}
//				}
//			}
//		}
//		else
//		{
//			for(Object o : l1)
//			{
//				for(Object p : l2)
//				{
//					if(!equality(o, p))
//					{
//						l.add(o)
//					}
//				}
//			}
//		}
//		
//		return l
//	}
	
}