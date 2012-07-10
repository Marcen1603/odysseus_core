/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

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
				schema = new SDFSchema("Person", new SDFAttribute(null,
						"timestamp", SDFDatatype.LONG), new SDFAttribute(null,
						"id", SDFDatatype.INTEGER), new SDFAttribute(null,
						"name", SDFDatatype.STRING), new SDFAttribute(null,
						"email", SDFDatatype.STRING), new SDFAttribute(null,
						"creditcard", SDFDatatype.STRING), new SDFAttribute(
						null, "city", SDFDatatype.STRING), new SDFAttribute(
						null, "state", SDFDatatype.STRING));
				break;
			case AUCTION:
				schema = new SDFSchema(
						"Auction",
						new SDFAttribute(null, "timestamp", SDFDatatype.LONG),
						new SDFAttribute(null, "id", SDFDatatype.INTEGER),
						new SDFAttribute(null, "itemname", SDFDatatype.STRING),
						new SDFAttribute(null, "description",
								SDFDatatype.STRING),
						new SDFAttribute(null, "initialbid",
								SDFDatatype.INTEGER),
						new SDFAttribute(null, "reserver", SDFDatatype.INTEGER),
						new SDFAttribute(null, "expires", SDFDatatype.LONG),
						new SDFAttribute(null, "seller", SDFDatatype.INTEGER),
						new SDFAttribute(null, "category", SDFDatatype.INTEGER));
				break;
			case BID:
				schema = new SDFSchema("Bid", new SDFAttribute(null,
						"timestamp", SDFDatatype.LONG), new SDFAttribute(null,
						"auction", SDFDatatype.INTEGER), new SDFAttribute(null,
						"bidder", SDFDatatype.INTEGER), new SDFAttribute(null,
						"datetime", SDFDatatype.LONG), new SDFAttribute(null,
						"price", SDFDatatype.DOUBLE));
				break;
			case CATEGORY:
				schema = new SDFSchema("Category", new SDFAttribute(null, "id",
						SDFDatatype.INTEGER), new SDFAttribute(null, "name",
						SDFDatatype.STRING), new SDFAttribute(null,
						"description", SDFDatatype.STRING), new SDFAttribute(
						null, "parentid", SDFDatatype.INTEGER));
				break;
			}
			schemaMap.put(type, schema);
		}
		return schema;
	}
}
