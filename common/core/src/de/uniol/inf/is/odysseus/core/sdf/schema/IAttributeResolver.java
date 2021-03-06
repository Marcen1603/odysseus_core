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
package de.uniol.inf.is.odysseus.core.sdf.schema;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author Jonas Jacobi, Marco Grawunder
 *
 */
public interface IAttributeResolver extends Serializable {

	/**
	 * The schemas for this attribute resolver, could be empty
	 * @return
	 */
    public List<SDFSchema> getSchema();

    /**
     * Translate name into attribute (if more that one schema, it must be unambigious
     * @param name
     * @return
     * @throws AmbiguousAttributeException
     * @throws NoSuchAttributeException
     */
	public SDFAttribute getAttribute(String name) throws AmbiguousAttributeException, NoSuchAttributeException;

	/**
	 * Retrieve set of all attributes
	 * @return
	 */
	public Set<SDFAttribute> getAllAttributes();

	/**
	 * In some cases (e.g. Key Value) there are no attribute in the schema, in this case the attribute resolver is empty
	 * @return
	 */
	public boolean isEmpty();

	/**
	 * Clone
	 * @return
	 */
	public IAttributeResolver clone() ;


}