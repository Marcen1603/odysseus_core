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
package de.uniol.inf.is.odysseus.datadictionary;

import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;
import de.uniol.inf.is.odysseus.usermanagement.PermissionException;
import de.uniol.inf.is.odysseus.usermanagement.ISession;

public interface IDataDictionary {

	public void addEntity(String uri, SDFEntity entity, ISession user)
			throws PermissionException;

	public SDFEntity getEntity(String uri, ISession caller)
			throws PermissionException;

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
	public SDFSource createSDFSource(String sourcename);

	// no restric
	public boolean emptySourceTypeMap();

	public void setView(String viewname, ILogicalOperator topOperator,
			ISession caller);

	// no restric
	public boolean isView(String name);

	public void setStream(String streamname, ILogicalOperator plan, ISession caller);

	public ILogicalOperator getStreamForTransformation(String name, ISession caller);

	// no restric
	public boolean existsSource(String name);

	public Set<Entry<String, ILogicalOperator>> getStreamsAndViews(ISession caller);

	public ILogicalOperator getViewOrStream(String viewname, ISession caller);

	public Set<Entry<String, ILogicalOperator>> getStreams(ISession caller);
	public Set<Entry<String, ILogicalOperator>> getViews(ISession caller);	

	public void addLogicalPlan(IQuery q, ISession caller);
	public IQuery getLogicalPlan(int id, ISession caller);
	public List<IQuery> getLogicalPlans(ISession caller);
	public void removeLogicalPlan(IQuery q, ISession caller);
	
	public ILogicalOperator removeViewOrStream(String viewname, ISession caller);
	
	public ILogicalOperator removeSink(String name);

	// no restric
	public boolean containsViewOrStream(String viewName, ISession user);

	// no restric
	//public User getUserForViewOrStream(String view);
	
	public ISession getCreator(String resource);

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

	String getSourceType(String sourcename);
	
	public void addDatatype(String name, SDFDatatype dt, ISession caller);
	
	public SDFDatatype getDatatype(String name);
	public Set<String> getDatatypes();
	
	public boolean existsDatatype(String dtName);

	void addSink(String sinkname, ILogicalOperator sink, ISession caller);
	
	public Set<Entry<String, ILogicalOperator>> getSinks(ISession caller);

	ILogicalOperator getSinkTop(String sinkname);
	ILogicalOperator getSinkInput(String sinkname);

	boolean existsSink(String sinkname);

	ILogicalOperator getView(String viewname, ISession caller);

	AccessAO getStream(String viewname, ISession caller);


}