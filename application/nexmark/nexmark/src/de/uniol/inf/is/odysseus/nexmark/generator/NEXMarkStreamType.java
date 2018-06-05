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
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;

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
				schema = SDFSchemaFactory.createNewTupleSchema("Person", new SDFAttribute(null,
						"timestamp", SDFDatatype.LONG, null, null, null), new SDFAttribute(null,
						"id", SDFDatatype.INTEGER, null, null, null), new SDFAttribute(null,
						"name", SDFDatatype.STRING, null, null, null), new SDFAttribute(null,
						"email", SDFDatatype.STRING, null, null, null), new SDFAttribute(null,
						"creditcard", SDFDatatype.STRING, null, null, null), new SDFAttribute(
						null, "city", SDFDatatype.STRING, null, null, null), new SDFAttribute(
						null, "state", SDFDatatype.STRING, null, null, null));
				break;
			case AUCTION:
				schema = SDFSchemaFactory.createNewTupleSchema(
						"Auction",
						new SDFAttribute(null, "timestamp", SDFDatatype.LONG, null, null, null),
						new SDFAttribute(null, "id", SDFDatatype.INTEGER, null, null, null),
						new SDFAttribute(null, "itemname", SDFDatatype.STRING, null, null, null),
						new SDFAttribute(null, "description",
								SDFDatatype.STRING, null, null, null),
						new SDFAttribute(null, "initialbid",
								SDFDatatype.INTEGER, null, null, null),
						new SDFAttribute(null, "reserver", SDFDatatype.INTEGER, null, null, null),
						new SDFAttribute(null, "expires", SDFDatatype.LONG, null, null, null),
						new SDFAttribute(null, "seller", SDFDatatype.INTEGER, null, null, null),
						new SDFAttribute(null, "category", SDFDatatype.INTEGER, null, null, null));
				break;
			case BID:
				schema = SDFSchemaFactory.createNewTupleSchema("Bid", new SDFAttribute(null,
						"timestamp", SDFDatatype.LONG, null, null, null), new SDFAttribute(null,
						"auction", SDFDatatype.INTEGER, null, null, null), new SDFAttribute(null,
						"bidder", SDFDatatype.INTEGER, null, null, null), new SDFAttribute(null,
						"datetime", SDFDatatype.LONG, null, null, null), new SDFAttribute(null,
						"price", SDFDatatype.DOUBLE, null, null, null));
				break;
			case CATEGORY:
				schema = SDFSchemaFactory.createNewTupleSchema("Category", new SDFAttribute(null, "id",
						SDFDatatype.INTEGER, null, null, null), new SDFAttribute(null, "name",
						SDFDatatype.STRING, null, null, null), new SDFAttribute(null,
						"description", SDFDatatype.STRING, null, null, null), new SDFAttribute(
						null, "parentid", SDFDatatype.INTEGER, null, null, null));
				break;
			}
			schemaMap.put(type, schema);
		}
		return schema;
	}
}
