package de.uniol.inf.is.odysseus.parser.novel.cql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class CQLDictionary 
{
	
	protected Map<String, Collection<SDFAttribute>> attributes;
	
	private ISession user;
	
	private CQLDictionary(ISession user) 
	{
		this.user = user;
		attributes = new HashMap<>();
	}
	
	public CQLDictionary() {}
	
	public void add(List<SDFAttribute> attributes)
	{
		add(attributes.toArray(new SDFAttribute[attributes.size()]));
	}
	
	public void add(SDFAttribute ... attr)
	{
		Collection<SDFAttribute> coll; 
		for(int i = 0; i < attr.length; i++)
		{
			String src = attr[i].getSourceName();
			if(attributes.containsKey(src))
			{
				coll = new ArrayList<SDFAttribute>(attributes.get(src));
				coll.add(attr[i]);
				attributes.put(src, coll);
			}
			else
			{
				coll = Arrays.asList(attr[i]);
				attributes.put(src, coll);
			}
		}
	}
	
	public Map<String, Collection<SDFAttribute>> get()
	{
		return attributes;
	}
	
	public Collection<SDFSchema> getSchema()
	{
		Set<SDFSchema> set = new HashSet<>();
		for(Entry<String, Collection<SDFAttribute>> s : attributes.entrySet())
		{
			set.add(SDFSchemaFactory.createNewTupleSchema(s.getKey(), s.getValue()));
		}
		return set;
	}
	
	public Collection<SDFAttribute> get(String src)
	{
		Collection<SDFAttribute> c = attributes.get(src);
		return c != null ? c : new ArrayList<SDFAttribute>();
	}
	
	@Override
	public String toString() { return "CQLDictionary:: "+user.getUser().getName() + this.attributes.toString(); }

	static CQLDictionary create(ISession user) { return new CQLDictionary(user); }

}
