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
package de.uniol.inf.is.odysseus.usermanagement;

import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public interface IDataDictionary {
	/**
	 * @param uri
	 * @param entity
	 * @param caller
	 * @return
	 */
	SDFEntity createEntity(String uri, SDFEntity entity, ISession caller);

	/**
	 * @param entity
	 * @param caller
	 */
	void deleteEntity(SDFEntity entity, ISession caller);

	/**
	 * @param uri
	 * @param caller
	 * @return
	 */
	SDFEntity getEntity(String uri, ISession caller);

	/**
	 * @param caller
	 * @return
	 */
	List<SDFEntity> getEntities(ISession caller);

	/**
	 * @param uri
	 * @param caller
	 * @return
	 */
	IUser getEntityOwner(String uri, ISession caller);

	/**
	 * @param name
	 * @param type
	 * @param caller
	 * @return
	 */
	String createSourceType(String name, String type, ISession caller);

	/**
	 * @param name
	 * @param type
	 * @param caller
	 */
	void deleteSourceType(String name, String type, ISession caller);

	/**
	 * @param name
	 * @param caller
	 * @return
	 */
	String getSourceType(String name, ISession caller);

	/**
	 * @param caller
	 * @return
	 */
	Map<String, String> getSourceTypes(ISession caller);

	/**
	 * @param name
	 * @param plan
	 * @param caller
	 * @return
	 */
	ILogicalOperator createView(String name, ILogicalOperator plan,
			ISession caller) throws ViewAlreadyExistException;

	/**
	 * @param name
	 * @param caller
	 */
	void deleteView(String name, ISession caller);

	/**
	 * @param name
	 * @param caller
	 * @return
	 */
	ILogicalOperator getView(String name, ISession caller);

	/**
	 * @param caller
	 * @return
	 */
	List<ILogicalOperator> getViews(ISession caller);

	/**
	 * @param name
	 * @param caller
	 * @return
	 */
	boolean isView(String name, ISession caller);

	/**
	 * @param name
	 * @param datatype
	 * @param caller
	 */
	void createDatatype(String name, SDFDatatype datatype, ISession caller);

	/**
	 * @param name
	 * @param caller
	 */
	void deleteDatatype(String name, ISession caller);

	/**
	 * @param name
	 * @param caller
	 * @return
	 */
	SDFDatatype getDatatype(String name, ISession caller);

	/**
	 * @param caller
	 * @return
	 */
	Map<String, SDFDatatype> getDatatypes(ISession caller);

	/**
	 * @param name
	 * @param plan
	 * @param caller
	 * @return
	 */
	ILogicalOperator createStream(String name, ILogicalOperator plan,
			ISession caller) throws StreamAlreadyExistException;

	/**
	 * @param name
	 * @param caller
	 */
	void deleteStream(String name, ISession caller);

	/**
	 * @param name
	 * @param caller
	 * @return
	 */
	ILogicalOperator getStream(String name, ISession caller);

	/**
	 * @param caller
	 * @return
	 */
	List<ILogicalOperator> getStreams(ISession caller);

	/**
	 * @param name
	 * @param caller
	 * @return
	 */
	boolean isStream(String name, ISession caller);

}
