package de.uniol.inf.is.odysseus.parser.novel.cql;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class CQLDictionary 
{
	
	private static CQLDictionary instance = null;
	public static CQLDictionary getDictionary() 
	{ return instance != null ? instance : (instance = new CQLDictionary()); }
	
	private SDFSchema schema;
	private Collection<SDFAttribute> attributes;
	private Map<ISession, Collection<SDFAttribute>> queries;
//	private Map<>
	
	
	private CQLDictionary() 
	{
		queries = new HashMap<>();
		
//		createDictionary();
		
	}
	
	public void add(ISession user, SDFAttribute ... attribute)
	{
		
	}
	
	public void createDictionary()
	{
		SDFSchemaFactory.createNewAddAttributes(attributes, schema);
	}
	
	public SDFSchema getOutputSchema() { return schema; }

}
