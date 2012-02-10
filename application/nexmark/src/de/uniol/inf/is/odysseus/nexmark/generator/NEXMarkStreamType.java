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
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public enum NEXMarkStreamType {
	PERSON("person"), AUCTION("auction"), BID("bid"), CATEGORY("category");

	public final String name;

	static private Map<NEXMarkStreamType, SDFSchema> schemaMap = new HashMap<NEXMarkStreamType, SDFSchema>();

	private NEXMarkStreamType(String name) {
		this.name = name;
	}

	static public SDFSchema getSchema(NEXMarkStreamType type) {
		SDFSchema schema = schemaMap.get(type);
		if (schema == null) {
			switch (type) {
			case PERSON:
				schema = new SDFSchema("Person");
				SDFAttribute a = new SDFAttribute(null,"timestamp", SDFDatatype.LONG);
				schema.add(a);
				a = new SDFAttribute(null,"id", SDFDatatype.INTEGER);
				schema.add(a);
				a = new SDFAttribute(null,"name", SDFDatatype.STRING);
				schema.add(a);
				a = new SDFAttribute(null,"email", SDFDatatype.STRING);
				schema.add(a);
				a = new SDFAttribute(null,"creditcard", SDFDatatype.STRING);
				schema.add(a);
				a = new SDFAttribute(null,"city", SDFDatatype.STRING);
				schema.add(a);
				a = new SDFAttribute(null,"state", SDFDatatype.STRING);
				schema.add(a);
				break;
			case AUCTION:
				schema = new SDFSchema("Auction");
				a = new SDFAttribute(null,"timestamp", SDFDatatype.LONG);
				schema.add(a);
				a = new SDFAttribute(null,"id", SDFDatatype.INTEGER);
				schema.add(a);
				a = new SDFAttribute(null,"itemname", SDFDatatype.STRING);
				schema.add(a);
				a = new SDFAttribute(null,"description", SDFDatatype.STRING);
				schema.add(a);
				a = new SDFAttribute(null,"initialbid", SDFDatatype.INTEGER);
				schema.add(a);
				a = new SDFAttribute(null,"reserver", SDFDatatype.INTEGER);
				schema.add(a);
				a = new SDFAttribute(null,"expires", SDFDatatype.LONG);
				schema.add(a);
				a = new SDFAttribute(null,"seller", SDFDatatype.INTEGER);
				schema.add(a);
				a = new SDFAttribute(null,"category", SDFDatatype.INTEGER);
				schema.add(a);
				break;
			case BID:
				schema = new SDFSchema("Bid");
				a = new SDFAttribute(null,"timestamp", SDFDatatype.LONG);
				schema.add(a);
				a = new SDFAttribute(null,"auction", SDFDatatype.INTEGER);
				schema.add(a);
				a = new SDFAttribute(null,"bidder", SDFDatatype.INTEGER);
				schema.add(a);
				a = new SDFAttribute(null,"datetime", SDFDatatype.LONG);
				schema.add(a);
				a = new SDFAttribute(null,"price", SDFDatatype.DOUBLE);
				schema.add(a);
				break;
			case CATEGORY:
				schema = new SDFSchema("Category");
				a = new SDFAttribute(null,"id", SDFDatatype.INTEGER);
				schema.add(a);
				a = new SDFAttribute(null,"name", SDFDatatype.STRING);
				schema.add(a);
				a = new SDFAttribute(null,"description", SDFDatatype.STRING);
				schema.add(a);
				a = new SDFAttribute(null,"parentid", SDFDatatype.INTEGER);
				schema.add(a);
				break;
			}
			schemaMap.put(type, schema);
		}
		return schema;
	}
}
