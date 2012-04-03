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
package de.uniol.inf.is.odysseus.core.server.datadictionary;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.datadictionary.IAddDataType;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;
import de.uniol.inf.is.odysseus.core.usermanagement.PermissionException;

public interface IDataDictionary extends IAddDataType{

	public void addEntitySchema(String uri, SDFSchema entity, ISession user)
			throws PermissionException;

	public SDFSchema getEntitySchema(String uri, ISession caller)
			throws PermissionException, DataDictionaryException;

	/**
	 * returns the username from the creator of the given entity
	 * 
	 * @param entityuri
	 * @return username
	 * @throws PermissionException
	 */
	// no restric
	public String getUserForEntity(String entityuri);

	// no restirc
	public void addSourceType(String sourcename, String sourcetype);

	// no restric
	public boolean emptySourceTypeMap();

	public void setView(String viewname, ILogicalOperator topOperator,
			ISession caller) throws DataDictionaryException;

	// no restric
	public boolean isView(String name);

	public void setStream(String streamname, ILogicalOperator plan, ISession caller) throws DataDictionaryException;

	public ILogicalOperator getStreamForTransformation(String name, ISession caller);

	public Set<Entry<String, ILogicalOperator>> getStreamsAndViews(ISession caller);

	public ILogicalOperator getViewOrStream(String viewname, ISession caller) throws DataDictionaryException;

	public Set<Entry<String, ILogicalOperator>> getStreams(ISession caller);
	public Set<Entry<String, ILogicalOperator>> getViews(ISession caller);	

	public void addQuery(ILogicalQuery q, ISession caller, String queryBuildConfigName);
	public ILogicalQuery getQuery(int id, ISession caller);
	public String getQueryBuildConfigName(int id);
	public List<ILogicalQuery> getQueries(IUser user, ISession caller);
	public void removeQuery(ILogicalQuery q, ISession caller);
	
	public ILogicalOperator removeViewOrStream(String viewname, ISession caller);
	
	public ILogicalOperator removeSink(String name, ISession caller);

	// no restric
	public boolean containsViewOrStream(String viewName, ISession user);

	// no restric
	//public User getUserForViewOrStream(String view);
	
	public IUser getCreator(String resource);

	/**
	 * checks if the given user has higher permission as the given action.
	 * Calls the corresponding method in the action class.
	 * 
	 * @param action
	 * @param objecturi
	 * @param user
	 * @return boolean
	 */
	public boolean hasSuperAction(DataDictionaryPermission action,
			 ISession user);

	// no restric
	public void addListener(IDataDictionaryListener listener);

	// no restirc
	public void removeListener(IDataDictionaryListener listener);

	boolean isCreatorOfObject(String caller, String objecturi);

	String getSourceType(String sourcename) throws DataDictionaryException;
	
//	public void addDatatype(String name, SDFDatatype dt, ISession caller);
	public void removeDatatype(String name) throws DataDictionaryException;
	
	public SDFDatatype getDatatype(String name) throws DataDictionaryException;
	public Set<String> getDatatypes();
	
	public boolean existsDatatype(String dtName);

	void addSink(String sinkname, ILogicalOperator sink, ISession caller) throws DataDictionaryException;
	
	public Set<Entry<String, ILogicalOperator>> getSinks(ISession caller);

	ILogicalOperator getSinkTop(String sinkname, ISession caller) throws DataDictionaryException;
	ILogicalOperator getSinkInput(String sinkname, ISession caller) throws DataDictionaryException;

	boolean existsSink(String sinkname);

	ILogicalOperator getView(String viewname, ISession caller);

	AccessAO getStream(String viewname, ISession caller) throws DataDictionaryException;

	// ------------------------------------------
	// Physical sinks and sources (from WrapperPlanFactory)
	// ------------------------------------------
	ISource<?> getAccessPlan(String uri);
	void putAccessPlan(String uri, ISource<?> s);
	Map<String, ISource<?>> getSources();
	void clearSources();
	void removeClosedSources();
	void removeClosedSinks();
	ISink<?> getSink(String sinkName);
	void putSink(String name, ISink<?> sinkPO);

}