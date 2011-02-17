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
package de.uniol.inf.is.odysseus.scars.testdata.provider.extended.schema;

import de.uniol.inf.is.odysseus.scars.testdata.provider.ExtendedProvider;

public class SchemaGeneratorFactory {

	public static SchemaGeneratorFactory instance;

	private SchemaGeneratorFactory() {

	}

	public static synchronized SchemaGeneratorFactory getInstance() {
		if (instance == null) {
			instance = new SchemaGeneratorFactory();
		}
		return instance;
	}

	/**
	 * builds a new SchemaGenerator instance according to the given id
	 * 
	 * @param schemaID
	 *            not null.
	 * @return a new SchemaGenerator instance or null iff the schemaID is
	 *         invalid
	 */
	public ISchemaGenerator buildSchemaGenerator(String schemaID) {
		if (schemaID.equals(ExtendedProvider.SCHEMA_SCARS_DEFAULT)) {
			return new DefaultSchemaGenerator();
		} else if (schemaID.equals(ExtendedProvider.SCHEMA_SCARS_ALTERNATIVE)) {
			return new AlternativeSchemaGenerator();
		} else if (schemaID.equals(ExtendedProvider.SCHEMA_SCARS_LASER)) {
			return new LaserSchemaGenerator();
		} else {
			return null;
		}
	}

}
