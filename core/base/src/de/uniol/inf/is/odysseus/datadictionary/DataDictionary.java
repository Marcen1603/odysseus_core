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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;
import de.uniol.inf.is.odysseus.store.IStore;
import de.uniol.inf.is.odysseus.store.MemoryStore;
import de.uniol.inf.is.odysseus.store.StoreException;
import de.uniol.inf.is.odysseus.usermanagement.PermissionException;
import de.uniol.inf.is.odysseus.usermanagement.IPermission;
import de.uniol.inf.is.odysseus.usermanagement.ISession;
import de.uniol.inf.is.odysseus.usermanagement.IUser;
import de.uniol.inf.is.odysseus.usermanagement.NullUserException;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.util.AbstractGraphWalker;
import de.uniol.inf.is.odysseus.util.CopyLogicalGraphVisitor;
import de.uniol.inf.is.odysseus.util.RemoveOwnersGraphVisitor;

public class DataDictionary implements IDataDictionary {

	protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(Query.class);
		}
		return _logger;
	}

	private List<IDataDictionaryListener> listeners = new ArrayList<IDataDictionaryListener>();
	final private IStore<String, ILogicalOperator> streamDefinitions;
	final private IStore<String, ISession> viewOrStreamFromUser;
	final private IStore<String, ILogicalOperator> viewDefinitions;
	final private IStore<String, SDFEntity> entityMap;
	final private IStore<String, ISession> entityFromUser;
	final private IStore<String, String> sourceTypeMap;
	final private IStore<String, SDFDatatype> datatypes;
	final private IStore<IUser, List<IQuery>> logicalPlans;
	
	final private IStore<String, ILogicalOperator> sinkDefinitions;
	final private IStore<String, ISession> sinkFromUser;

	DataDictionary() {
//		try {
//			if (Boolean.parseBoolean(OdysseusDefaults
//					.get("storeDataDictionary"))) {
//				streamDefinitions = new FileStore<String, ILogicalOperator>(
//						OdysseusDefaults.get("streamDefinitionsFilename"));
//				viewOrStreamFromUser = new FileStore<String, I__User>(
//						OdysseusDefaults.get("streamOrViewFromUserFilename"));
//				viewDefinitions = new FileStore<String, ILogicalOperator>(
//						OdysseusDefaults.get("viewDefinitionsFilename"));
//				entityMap = new FileStore<String, SDFEntity>(
//						OdysseusDefaults.get("entitiesFilename"));
//				sourceTypeMap = new FileStore<String, String>(
//						OdysseusDefaults.get("sourceTypeMapFilename"));
//				entityFromUser = new FileStore<String, ISession>(
//						OdysseusDefaults.get("entityFromUserFilename"));
//				datatypes = new FileStore<String, SDFDatatype>(
//						OdysseusDefaults.get("datatypesFromDatatypesFilename"));
//				sinkDefinitions = new FileStore<String, ILogicalOperator>(
//						OdysseusDefaults.get("sinkDefinitionsFilename"));
//				sinkFromUser = new FileStore<String, ISession>(OdysseusDefaults.get("sinkDefinitionsUserFilename"));
//			} else {
				streamDefinitions = new MemoryStore<String, ILogicalOperator>();
				viewOrStreamFromUser = new MemoryStore<String, ISession>();
				viewDefinitions = new MemoryStore<String, ILogicalOperator>();
				entityMap = new MemoryStore<String, SDFEntity>();
				entityFromUser = new MemoryStore<String, ISession>();
				sourceTypeMap = new MemoryStore<String, String>();
				datatypes = new MemoryStore<String, SDFDatatype>();
				sinkDefinitions = new MemoryStore<String, ILogicalOperator>();
				sinkFromUser = new MemoryStore<String, ISession>();
				logicalPlans = new MemoryStore<IUser, List<IQuery>>();
//			}
			
			/**
			 * fill in the built-in datatypes
			 */
			
			addDatatype(SDFDatatype.OBJECT.getURI(), SDFDatatype.OBJECT);
			addDatatype(SDFDatatype.DATE.getURI(), SDFDatatype.DATE);
			addDatatype(SDFDatatype.DOUBLE.getURI(), SDFDatatype.DOUBLE);
			addDatatype(SDFDatatype.END_TIMESTAMP.getURI(), SDFDatatype.END_TIMESTAMP);
			addDatatype(SDFDatatype.FLOAT.getURI(), SDFDatatype.FLOAT);
			addDatatype(SDFDatatype.INTEGER.getURI(), SDFDatatype.INTEGER);
			addDatatype(SDFDatatype.LONG.getURI(), SDFDatatype.LONG);			
			addDatatype(SDFDatatype.SPATIAL_LINE.getURI(), SDFDatatype.SPATIAL_LINE);
			addDatatype(SDFDatatype.SPATIAL_MULTI_LINE.getURI(), SDFDatatype.SPATIAL_MULTI_LINE);
			addDatatype(SDFDatatype.SPATIAL_MULTI_POINT.getURI(), SDFDatatype.SPATIAL_MULTI_POINT);
			addDatatype(SDFDatatype.SPATIAL_MULTI_POLYGON.getURI(), SDFDatatype.SPATIAL_MULTI_POLYGON);
			addDatatype(SDFDatatype.SPATIAL_POINT.getURI(), SDFDatatype.SPATIAL_POINT);
			addDatatype(SDFDatatype.SPATIAL_POLYGON.getURI(), SDFDatatype.SPATIAL_POLYGON);
			addDatatype(SDFDatatype.SPATIAL.getURI(), SDFDatatype.SPATIAL);
			addDatatype(SDFDatatype.START_TIMESTAMP.getURI(), SDFDatatype.START_TIMESTAMP);
			addDatatype(SDFDatatype.STRING.getURI(), SDFDatatype.STRING);
			addDatatype(SDFDatatype.MV.getURI(), SDFDatatype.MV);
			addDatatype(SDFDatatype.TIMESTAMP.getURI(), SDFDatatype.TIMESTAMP);
			addDatatype(SDFDatatype.BOOLEAN.getURI(), SDFDatatype.BOOLEAN);
			addDatatype(SDFDatatype.GRID.getURI(), SDFDatatype.GRID);
			
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}
	}

	// ----------------------------------------------------------------------------
	// Entity Management
	// ----------------------------------------------------------------------------

	@Override
	public void addEntity(String uri, SDFEntity entity, ISession caller)
			throws PermissionException {
		if (hasPermission(caller, DataDictionaryPermission.ADD_ENTITY)){
			try {
				this.entityMap.put(uri, entity);
				this.entityFromUser.put(uri, caller);
				fireDataDictionaryChangedEvent();
			} catch (StoreException e) {
				throw new RuntimeException(e);
			}
		} else {
			throw new PermissionException("User " + caller.getUser().getName()
					+ "has no permission to add entities.");
		}
	}

	@Override
	public SDFEntity getEntity(String uri, ISession caller)
			throws PermissionException {
		if (checkObjectAccess(uri, caller, DataDictionaryPermission.GET_ENTITY)) {
			SDFEntity ret = entityMap.get(uri);
			if (ret == null) {
				throw new IllegalArgumentException("no such entity: " + uri);
			}
			return ret;
		} else {
			throw new PermissionException("User " + caller.getUser().getName()
					+ " has no permission to get entity '" + uri + "'.");
		}
	}

	// TODO: REMOVE Entity

	/**
	 * returns the username from the creator of the given entity
	 * 
	 * @param entityuri
	 * @return username
	 * @throws PermissionException
	 */
	// no restric
	@Override
	public String getUserForEntity(String entityuri) {
		ISession user = this.entityFromUser.get(entityuri);
		return user != null ? user.getUser().getName() : null;
	}

	// ----------------------------------------------------------------------------
	// Source Management
	// ----------------------------------------------------------------------------

	// no restrict
	@Override
	public void addSourceType(String sourcename, String sourcetype) {
		sourceTypeMap.put(sourcename, sourcetype);
	}

	@Override
	public String getSourceType(String sourcename) {
		String value = sourceTypeMap.get(sourcename);
		if (value == null) {
			throw new IllegalArgumentException("missing source type for: "
					+ sourcename);
		}
		return value;
	}

	// no restric
	@Override
	public SDFSource createSDFSource(String sourcename) {
		String type = getSourceType(sourcename);
		SDFSource source = new SDFSource(sourcename, type);

		return source;
	}

	// no restric
	@Override
	public boolean emptySourceTypeMap() {
		return sourceTypeMap.isEmpty();
	}

	// ------------------------------------------------------------------------
	// View Management
	// ------------------------------------------------------------------------

	@Override
	@SuppressWarnings("unchecked")
	public void setView(String viewname, ILogicalOperator topOperator,
			ISession caller) {
		if (hasPermission(caller, DataDictionaryPermission.ADD_VIEW)){
			if (viewDefinitions.containsKey(viewname)) {
				throw new RuntimeException("View " + viewname
						+ " already exists. Drop First");
			}
			try {
				// Remove Owner from View
				RemoveOwnersGraphVisitor<ILogicalOperator> visitor = new RemoveOwnersGraphVisitor<ILogicalOperator>();
				@SuppressWarnings("rawtypes")
				AbstractGraphWalker walker = new AbstractGraphWalker();
				walker.prefixWalk(topOperator, visitor);

				this.viewDefinitions.put(viewname, topOperator);
				viewOrStreamFromUser.put(viewname, caller);
				fireDataDictionaryChangedEvent();
			} catch (StoreException e) {
				throw new RuntimeException(e);
			}
			fireAddEvent(viewname, topOperator);
		} else {
			throw new PermissionException("User " + caller.getUser().getName()
					+ " has no permission to add a view.");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public ILogicalOperator getView(String viewname, ISession caller) {
		if (this.viewDefinitions.containsKey(viewname)) {
			checkViewAccess(viewname, caller, DataDictionaryPermission.READ);
			ILogicalOperator logicalPlan = this.viewDefinitions.get(viewname);
			CopyLogicalGraphVisitor<ILogicalOperator> copyVisitor = new CopyLogicalGraphVisitor<ILogicalOperator>(
					null);
			@SuppressWarnings("rawtypes")
			AbstractGraphWalker walker = new AbstractGraphWalker();
			walker.prefixWalk(logicalPlan, copyVisitor);
			return copyVisitor.getResult();
		} else {
			return null;
		}
	}

	private ILogicalOperator removeView(String viewname, ISession caller) {
		ILogicalOperator op;
		try {
			op = viewDefinitions.remove(viewname);
			if (op != null) {
				viewOrStreamFromUser.remove(viewname);
			}			
		} catch (StoreException e) {
			throw new RuntimeException(e);
		}
		if (op != null){
			fireRemoveEvent(viewname, op);
			fireDataDictionaryChangedEvent();
		}
		return op;
	}

	// no restric
	@Override
	public boolean isView(String name) {
		return this.viewDefinitions.containsKey(name);
	}

	@Override
	public Set<Entry<String, ILogicalOperator>> getViews(ISession caller) {
		return getDefinitions(caller, viewDefinitions);
	}

	// ------------------------------------------------------------------------
	// Stream Management
	// ------------------------------------------------------------------------

	@Override
	public void setStream(String streamname, ILogicalOperator plan, ISession caller) {
		if (hasPermission(caller, DataDictionaryPermission.ADD_STREAM)) {
			if(!this.entityMap.containsKey(streamname)){
				throw new RuntimeException("No entity for " + streamname + ". Add entity before adding access operator.");
			}
			if (streamDefinitions.containsKey(streamname)) {
				throw new RuntimeException("Stream " + streamname
						+ " already exists. Remove First");
			}
			try {
				streamDefinitions.put(streamname, plan);
				viewOrStreamFromUser.put(streamname, caller);
			} catch (StoreException e) {
				throw new RuntimeException(e);
			}
			fireAddEvent(streamname, plan);
			fireDataDictionaryChangedEvent();
		} else {
			throw new PermissionException("User " + caller.getUser().getName()
					+ " has no permission to set a new view.");
		}
	}
	
	@Override
	public AccessAO getStream(String viewname, ISession caller) {
		checkViewAccess(viewname, caller, DataDictionaryPermission.READ);

		if (!this.streamDefinitions.containsKey(viewname)) {
			throw new IllegalArgumentException("no such view: " + viewname);
		}

		SDFSource source = createSDFSource(viewname);
		AccessAO ao = new AccessAO(source);

		SDFEntity entity = getEntity(viewname, caller);
		ao.setOutputSchema(entity.getAttributes());

		return ao;
	}

	@Override
	public ILogicalOperator getStreamForTransformation(String name, ISession caller) {
		checkViewAccess(name, caller, DataDictionaryPermission.READ);
		return streamDefinitions.get(name);
	}

	// no restric
	@Override
	public boolean existsSource(String name) {
		return streamDefinitions.containsKey(name);
	}

	@Override
	public Set<Entry<String, ILogicalOperator>> getStreams(ISession caller) {
		return getDefinitions(caller, streamDefinitions);
	}

	// ------------------------------------------------------------------------
	// Access View or Streams
	// ------------------------------------------------------------------------

	@Override
	public Set<Entry<String, ILogicalOperator>> getStreamsAndViews(ISession caller) {
		Set<Entry<String, ILogicalOperator>> sources = new HashSet<Entry<String, ILogicalOperator>>();

		sources.addAll(getStreams(caller));
		sources.addAll(getViews(caller));

		return sources;
	}

	private Set<Entry<String, ILogicalOperator>> getDefinitions(ISession caller,
			IStore<String, ILogicalOperator> definitions) {
		Set<Entry<String, ILogicalOperator>> sources = new HashSet<Entry<String, ILogicalOperator>>();
		for (Entry<String, ILogicalOperator> viewEntry : definitions.entrySet()) {
			try {
				checkViewAccess(viewEntry.getKey(), caller,
						DataDictionaryPermission.READ);
				sources.add(viewEntry);
			} catch (PermissionException e) {
			}
		}
		return sources;
	}

	@Override
	public ILogicalOperator getViewOrStream(String viewname, ISession caller) {
		if (this.viewDefinitions.containsKey(viewname)) {
			return getView(viewname, caller);
		} else {
			return getStream(viewname, caller);
		}
	}

	@Override
	public ILogicalOperator removeViewOrStream(String viewname, ISession caller) {
		if (this.viewDefinitions.containsKey(viewname)) {
			checkViewAccess(viewname, caller, DataDictionaryPermission.REMOVE_VIEW);
			return removeView(viewname, caller);
		} else {
			ILogicalOperator op;
			checkViewAccess(viewname, caller,
					DataDictionaryPermission.REMOVE_STREAM);
			op = streamDefinitions.remove(viewname);
			if (op != null) {
				viewOrStreamFromUser.remove(viewname);
			}
			if (op != null)
				fireRemoveEvent(viewname, op);
			return op;
		}
	}

	// no restric
	@Override
	public boolean containsViewOrStream(String viewName, ISession user) {
		return this.streamDefinitions.containsKey(viewName)
				|| this.viewDefinitions.containsKey(viewName);
	}

	// no restric
	@Override
	public ISession getCreator(String resource) {
		ISession ret = viewOrStreamFromUser.get(resource);
		if (ret == null){
			ret = sinkFromUser.get(resource);
		}
		return ret;
		
	}

	// ----------------------------------------------------------------------------
	// Datatype Management
	// ----------------------------------------------------------------------------
	
	@Override
	public void addDatatype(String name, SDFDatatype dt, ISession caller){
		if (hasPermission(caller, DataDictionaryPermission.ADD_DATATYPE)){
			addDatatype(name, dt);
		}else{
			throw new PermissionException("User " + caller.getUser().getName()
					+ " has not the permission to create new data types");
		}
	}
	
	private void addDatatype(String name, SDFDatatype dt){
		if(!this.datatypes.containsKey(name.toLowerCase())){
			this.datatypes.put(name.toLowerCase(), dt);
			fireDataDictionaryChangedEvent();
		}
		else{
			throw new IllegalArgumentException("Type '" + name + "' already exists.");
		}
	}
	
	
	@Override
	public SDFDatatype getDatatype(String dtName){
		if(this.datatypes.containsKey(dtName.toLowerCase())){
			return this.datatypes.get(dtName.toLowerCase());
		}
		else{
			throw new IllegalArgumentException("No such datatype: " + dtName);
		}
	}
	
	@Override
	public Set<String> getDatatypes(){
		return this.datatypes.keySet();
	}
	
	@Override
	public boolean existsDatatype(String dtName){
		return this.datatypes.containsKey(dtName.toLowerCase());
	}

	// ----------------------------------------------------------------------------
	// Sink Management
	// ----------------------------------------------------------------------------
	
	@Override
	public void addSink(String sinkname, ILogicalOperator sink, ISession caller){
		if (!this.sinkDefinitions.containsKey(sinkname)){
			this.sinkDefinitions.put(sinkname, sink);
			this.sinkFromUser.put(sinkname, caller);
			fireDataDictionaryChangedEvent();
		}else{
			throw new IllegalArgumentException("Sink name already used");
		}
	}
	
	@Override
	public ILogicalOperator getSinkTop(String sinkname){
		if (this.sinkDefinitions.containsKey(sinkname)){
			return sinkDefinitions.get(sinkname);
		}else{
			throw new IllegalArgumentException("No such sink defined");
		}
	}
	
	@Override
	public ILogicalOperator getSinkInput(String sinkname) {
		ILogicalOperator sinkTop = getSinkTop(sinkname);
		ILogicalOperator ret = sinkTop;
		while (ret.getSubscribedToSource().size() > 0){
			ret = ret.getSubscribedToSource(0).getTarget();
		}
		return ret;
	}
	
	@Override
	public boolean existsSink(String sinkname){
		return this.sinkDefinitions.containsKey(sinkname);
	}
	
	public ISession getUserForSink(String sinkname){
		return this.sinkFromUser.get(sinkname);
	}
	
	// ----------------------------------------------------------------------------
	// Logical Plan Management
	// ----------------------------------------------------------------------------

	@Override
	public void addLogicalPlan(IQuery q, ISession caller) {
		if (this.logicalPlans.containsKey(caller.getUser())) {
			if (getLogicalPlan(q.getID(), caller) != null) {
				removeLogicalPlan(q, caller);
			}
			this.logicalPlans.get(caller.getUser()).add(q);
		} else {
			List<IQuery> queries = new ArrayList<IQuery>();
			queries.add(q);
			this.logicalPlans.put(caller.getUser(), queries);
		}
//		ISerializerStrategy<String> s = new XMLSerializeStrategy(); 
//		String text = s.serialize(q);
//		System.out.println(text);		
//		s.deserialize(text);		
	}

	@Override
	public IQuery getLogicalPlan(int id, ISession caller) {
		for(IQuery q : getLogicalPlans(caller)){
			if(q.getID()==id){
				return q;
			}
		}
		return null;
	}

	@Override
	public List<IQuery> getLogicalPlans(ISession caller) {
		if (this.logicalPlans.containsKey(caller.getUser())) {
			return this.logicalPlans.get(caller.getUser());
		} else {
			return new ArrayList<IQuery>();
		}
	}

	@Override
	public void removeLogicalPlan(IQuery q, ISession caller) {
		IQuery plan = getLogicalPlan(q.getID(), caller);
		if(plan!=null){
			this.logicalPlans.get(caller.getUser()).remove(plan);
		}
	}

	// ----------------------------------------------------------------------------
	// Listener Management
	// ----------------------------------------------------------------------------

	// no restric
	@Override
	public void addListener(IDataDictionaryListener listener) {
		if (listener == null)
			throw new IllegalArgumentException("listener is null");

		if (!listeners.contains(listener))
			listeners.add(listener);
	}

	// no restirc
	@Override
	public void removeListener(IDataDictionaryListener listener) {
		listeners.remove(listener);
	}

	private void fireAddEvent(String name, ILogicalOperator op) {
		for (IDataDictionaryListener listener : listeners) {
			try {
				listener.addedViewDefinition(this, name, op);
			} catch (Exception ex) {
				getLogger().error("Error during executing listener", ex);
			}
		}
	}

	private void fireRemoveEvent(String name, ILogicalOperator op) {
		for (IDataDictionaryListener listener : listeners) {
			try {
				listener.removedViewDefinition(this, name, op);
			} catch (Exception ex) {
				getLogger().error("Error during executing listener", ex);
			}
		}
	}

	@Override
	public Set<Entry<String, ILogicalOperator>> getSinks(ISession caller) {
		return this.sinkDefinitions.entrySet();
	}
	
	private void fireDataDictionaryChangedEvent(){
		for(IDataDictionaryListener listener : listeners){
			listener.dataDictionaryChanged(this);
		}
	}

	@Override
	public ILogicalOperator removeSink(String name) {
		ILogicalOperator op = this.sinkDefinitions.remove(name);
		fireDataDictionaryChangedEvent();
		return op;
	}
	
	// -------------------------------------------------------------------------------------
	// Hilfsmethoden f�r Rechtemanagement
	// -------------------------------------------------------------------------------------
	
	/**
	 * return true if the given user has permission to access the given object
	 * in a certain way (action). Object can be a source or entity.
	 * 
	 * @param uri
	 * @param caller
	 * @param action
	 * @return boolean
	 */
	private boolean checkObjectAccess(String uri, ISession caller,
			DataDictionaryPermission action) {
		return UserManagement.getUsermanagement().hasPermission(caller,action, uri)
				|| isCreatorOfObject(caller.getUser().getName(), uri)
				|| hasSuperAction(action, caller);
	}

	/**
	 * return true if the given user has permission to access the given view in
	 * a certain way (action).
	 * 
	 * @param uri
	 * @param caller
	 * @param action
	 * @return boolean
	 */
	private void checkViewAccess(String viewOrSource, ISession caller,
			DataDictionaryPermission action) {
		if (hasPermission(caller, action, viewOrSource)
				// is owner
				|| isCreatorOfView(caller.getUser().getName(), viewOrSource)
				|| hasSuperAction(action, caller)) {
			return;
		}
		throw new PermissionException("User " + caller.getUser().getName()
				+ " has not the permission '" + action + "' on Source/View '"
				+ viewOrSource);
	}
	
	
	private boolean hasPermission(ISession caller, IPermission permission, String objectURI) {
		return UserManagement.getUsermanagement().hasPermission(caller, permission, objectURI);
	}
	
	private boolean hasPermission(ISession caller, IPermission permission) {
		return hasPermission(caller, permission, DataDictionaryPermission.objectURI);
	}
	
	/**
	 * checks if the given user has higher permission as the given action. Calls
	 * the corresponding method in the action class.
	 * 
	 * @param action
	 * @param objecturi
	 * @param user
	 * @return boolean
	 */
	@Override
	public boolean hasSuperAction(DataDictionaryPermission action, ISession user) {
		return hasPermission(user, DataDictionaryPermission.hasSuperAction(action));
	}

	/**
	 * returns true if username equals creator of the given objecturi
	 * 
	 * @param username
	 * @param objecturi
	 * @return
	 */
	@Override
	public boolean isCreatorOfObject(String username, String objecturi) {
		if (objecturi == null) {
			return false;
		}
		if (username != null && !username.isEmpty()) {
			String user = getUserForEntity(objecturi);
			if (user == null || user.isEmpty()) {
				ISession userObj = getCreator(objecturi);
				user = userObj != null ? userObj.getUser().getName() : null;
			}
			if (user != null && !user.isEmpty()) {
				if (user.equals(username)) {
					return true;
				}
			}
			return false;
		} else {
			throw new NullUserException("Username is empty.");
		}
	}

	/**
	 * returns true if username euqlas creator of the given view
	 * 
	 * @param username
	 * @param viewname
	 * @return
	 */
	public boolean isCreatorOfView(String username, String viewname) {
		if (viewname == null) {
			return false;
		}
		if (!username.isEmpty()) {
			ISession user = getCreator(viewname);
			if (user != null) {
				if (user.getUser().getName().equals(username)) {
					return true;
				}
			}
			return false;
		} else {
			throw new NullUserException("Username is empty.");
		}
	}
	
	
}