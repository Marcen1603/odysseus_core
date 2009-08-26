package de.uniol.inf.is.odysseus.nexmark.generator;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public enum NEXMarkStreamType {
	PERSON("person"), AUCTION("auction"), BID("bid"), CATEGORY("category");

	public final String name;

	static private Map<NEXMarkStreamType, SDFAttributeList> schemaMap = new HashMap<NEXMarkStreamType, SDFAttributeList>();

	private NEXMarkStreamType(String name) {
		this.name = name;
	}

	static public SDFAttributeList getSchema(NEXMarkStreamType type) {
		SDFAttributeList schema = schemaMap.get(type);
		if (schema == null) {
			switch (type) {
			case PERSON:
				schema = new SDFAttributeList();
				SDFAttribute a = new SDFAttribute("timestamp");
				a.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
				schema.add(a);
				a = new SDFAttribute("id");
				a.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
				schema.add(a);
				a = new SDFAttribute("name");
				a.setDatatype(SDFDatatypeFactory.getDatatype("String"));
				schema.add(a);
				a = new SDFAttribute("email");
				a.setDatatype(SDFDatatypeFactory.getDatatype("String"));
				schema.add(a);
				a = new SDFAttribute("creditcard");
				a.setDatatype(SDFDatatypeFactory.getDatatype("String"));
				schema.add(a);
				a = new SDFAttribute("city");
				a.setDatatype(SDFDatatypeFactory.getDatatype("String"));
				schema.add(a);
				a = new SDFAttribute("state");
				a.setDatatype(SDFDatatypeFactory.getDatatype("String"));
				schema.add(a);
				break;
			case AUCTION:
				schema = new SDFAttributeList();
				a = new SDFAttribute("timestamp");
				a.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
				schema.add(a);
				a = new SDFAttribute("id");
				a.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
				schema.add(a);
				a = new SDFAttribute("itemname");
				a.setDatatype(SDFDatatypeFactory.getDatatype("String"));
				schema.add(a);
				a = new SDFAttribute("description");
				a.setDatatype(SDFDatatypeFactory.getDatatype("String"));
				schema.add(a);
				a = new SDFAttribute("initialbid");
				a.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
				schema.add(a);
				a = new SDFAttribute("reserver");
				a.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
				schema.add(a);
				a = new SDFAttribute("expires");
				a.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
				schema.add(a);
				a = new SDFAttribute("seller");
				a.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
				schema.add(a);
				a = new SDFAttribute("category");
				a.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
				schema.add(a);
				break;
			case BID:
				schema = new SDFAttributeList();
				a = new SDFAttribute("timestamp");
				a.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
				schema.add(a);
				a = new SDFAttribute("auction");
				a.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
				schema.add(a);
				a = new SDFAttribute("bidder");
				a.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
				schema.add(a);
				a = new SDFAttribute("datetime");
				a.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
				schema.add(a);
				a = new SDFAttribute("price");
				a.setDatatype(SDFDatatypeFactory.getDatatype("Double"));
				schema.add(a);
				break;
			case CATEGORY:
				schema = new SDFAttributeList();
				a = new SDFAttribute("id");
				a.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
				schema.add(a);
				a = new SDFAttribute("name");
				a.setDatatype(SDFDatatypeFactory.getDatatype("String"));
				schema.add(a);
				a = new SDFAttribute("description");
				a.setDatatype(SDFDatatypeFactory.getDatatype("String"));
				schema.add(a);
				a = new SDFAttribute("parentid");
				a.setDatatype(SDFDatatypeFactory.getDatatype("Integer"));
				schema.add(a);
				break;
			}
			schemaMap.put(type, schema);
		}
		return schema;
	}
}
