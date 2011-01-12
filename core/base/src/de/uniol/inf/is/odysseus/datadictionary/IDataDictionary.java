package de.uniol.inf.is.odysseus.datadictionary;

import java.util.Set;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;
import de.uniol.inf.is.odysseus.usermanagement.HasNoPermissionException;
import de.uniol.inf.is.odysseus.usermanagement.User;

public interface IDataDictionary {

	public void addEntity(String uri, SDFEntity entity, User user)
			throws HasNoPermissionException;

	public SDFEntity getEntity(String uri, User caller)
			throws HasNoPermissionException;

	/**
	 * returns the username from the creator of the given entity
	 * 
	 * @param entityuri
	 * @return username
	 * @throws HasNoPermissionException
	 */
	// no restric
	public String getUserForEntity(String entityuri);

	// no restirc
	public void addSourceType(String sourcename, String sourcetype);

	// no restric
	public SDFSource createSDFSource(String sourcename);

	// no restric
	public boolean emptySourceTypeMap();

	@SuppressWarnings("unchecked")
	public void setView(String viewname, ILogicalOperator topOperator,
			User caller);

	// no restric
	public boolean isView(String name);

	public void setStream(String streamname, ILogicalOperator plan, User caller);

	public ILogicalOperator getStreamForTransformation(String name, User caller);

	// no restric
	public boolean hasStream(String name, User user);

	public Set<Entry<String, ILogicalOperator>> getStreamsAndViews(User caller);

	public ILogicalOperator getViewOrStream(String viewname, User caller);

	public ILogicalOperator removeViewOrStream(String viewname, User caller);

	// no restric
	public boolean containsViewOrStream(String viewName, User user);

	// no restric
	public User getUserForViewOrStream(String view);

	/**
	 * checks if the given user has higher permission as the given action.
	 * Calls the corresponding method in the action class.
	 * 
	 * @param action
	 * @param objecturi
	 * @param user
	 * @return boolean
	 */
	public boolean hasSuperAction(DataDictionaryAction action,
			String objecturi, User user);

	// no restric
	public void addListener(IDataDictionaryListener listener);

	// no restirc
	public void removeListener(IDataDictionaryListener listener);

	boolean isCreatorOfObject(String caller, String objecturi);

}