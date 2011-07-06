/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.nexmark.generator;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

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
				a.setDatatype(new SDFDatatype("Long"));
				schema.add(a);
				a = new SDFAttribute("id");
				a.setDatatype(new SDFDatatype("Integer"));
				schema.add(a);
				a = new SDFAttribute("name");
				a.setDatatype(new SDFDatatype("String"));
				schema.add(a);
				a = new SDFAttribute("email");
				a.setDatatype(new SDFDatatype("String"));
				schema.add(a);
				a = new SDFAttribute("creditcard");
				a.setDatatype(new SDFDatatype("String"));
				schema.add(a);
				a = new SDFAttribute("city");
				a.setDatatype(new SDFDatatype("String"));
				schema.add(a);
				a = new SDFAttribute("state");
				a.setDatatype(new SDFDatatype("String"));
				schema.add(a);
				break;
			case AUCTION:
				schema = new SDFAttributeList();
				a = new SDFAttribute("timestamp");
				a.setDatatype(new SDFDatatype("Long"));
				schema.add(a);
				a = new SDFAttribute("id");
				a.setDatatype(new SDFDatatype("Integer"));
				schema.add(a);
				a = new SDFAttribute("itemname");
				a.setDatatype(new SDFDatatype("String"));
				schema.add(a);
				a = new SDFAttribute("description");
				a.setDatatype(new SDFDatatype("String"));
				schema.add(a);
				a = new SDFAttribute("initialbid");
				a.setDatatype(new SDFDatatype("Integer"));
				schema.add(a);
				a = new SDFAttribute("reserver");
				a.setDatatype(new SDFDatatype("Integer"));
				schema.add(a);
				a = new SDFAttribute("expires");
				a.setDatatype(new SDFDatatype("Long"));
				schema.add(a);
				a = new SDFAttribute("seller");
				a.setDatatype(new SDFDatatype("Integer"));
				schema.add(a);
				a = new SDFAttribute("category");
				a.setDatatype(new SDFDatatype("Integer"));
				schema.add(a);
				break;
			case BID:
				schema = new SDFAttributeList();
				a = new SDFAttribute("timestamp");
				a.setDatatype(new SDFDatatype("Long"));
				schema.add(a);
				a = new SDFAttribute("auction");
				a.setDatatype(new SDFDatatype("Integer"));
				schema.add(a);
				a = new SDFAttribute("bidder");
				a.setDatatype(new SDFDatatype("Integer"));
				schema.add(a);
				a = new SDFAttribute("datetime");
				a.setDatatype(new SDFDatatype("Long"));
				schema.add(a);
				a = new SDFAttribute("price");
				a.setDatatype(new SDFDatatype("Double"));
				schema.add(a);
				break;
			case CATEGORY:
				schema = new SDFAttributeList();
				a = new SDFAttribute("id");
				a.setDatatype(new SDFDatatype("Integer"));
				schema.add(a);
				a = new SDFAttribute("name");
				a.setDatatype(new SDFDatatype("String"));
				schema.add(a);
				a = new SDFAttribute("description");
				a.setDatatype(new SDFDatatype("String"));
				schema.add(a);
				a = new SDFAttribute("parentid");
				a.setDatatype(new SDFDatatype("Integer"));
				schema.add(a);
				break;
			}
			schemaMap.put(type, schema);
		}
		return schema;
	}
}
