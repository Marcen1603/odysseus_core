package de.uniol.inf.is.odysseus.core.server.datadictionary;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalPlan;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IAccessAO;
import de.uniol.inf.is.odysseus.core.server.store.IStore;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;


public interface IDataDictionaryWritable extends IDataDictionary{

	// -------------------------------------------------------------------------
	// View and Stream Management
	// -------------------------------------------------------------------------

	ILogicalPlan removeViewOrStream(String viewname, ISession caller);
	ILogicalPlan removeViewOrStream(Resource viewname, ISession caller);

	// -------------------------------------------------------------------------
	// View Management
	// -------------------------------------------------------------------------

	void setView(String viewname, ILogicalPlan topOperator, ISession caller)
			throws DataDictionaryException;

	// -------------------------------------------------------------------------
	// Stream Management
	// -------------------------------------------------------------------------

	void setStream(String streamname, ILogicalPlan plan, ISession caller)
			throws DataDictionaryException;

	// -------------------------------------------------------------------------
	// Sink Management
	// -------------------------------------------------------------------------

	void addSink(String sinkname, ILogicalPlan sink, ISession caller)
			throws DataDictionaryException;

	void addSink(Resource sinkname, ILogicalPlan sink, ISession caller)
			throws DataDictionaryException;

	ILogicalPlan removeSink(String name, ISession caller);

	ILogicalPlan removeSink(Resource name, ISession caller);

	// -------------------------------------------------------------------------
	// Query Management
	// -------------------------------------------------------------------------

	void addQuery(ILogicalQuery q, ISession caller, String queryBuildConfigName);

	void removeQuery(ILogicalQuery q, ISession caller);

	// -------------------------------------------------------------------------
	// Rights Management
	// -------------------------------------------------------------------------

	// ----------------------------------------------------------
	// Operatormanagement
	// ----------------------------------------------------------

	void setOperator(Resource id, IPhysicalOperator physical);

	void removeOperator(Resource id);

	// ------------------------------------------
	// Physical sinks and sources (from WrapperPlanFactory)
	// ------------------------------------------

	void putAccessPlan(Resource uri, ISource<?> s);

	void removeAccessPlan(Resource uri);

	void clearSources();

	void removeClosedSources();

	void removeClosedSinks();

	void putSinkplan(Resource name, ISink<?> sinkPO);

	ISource<?> getAccessPO(Resource name);
	IAccessAO getAccessAO(Resource name, ISession user);

	void putAccessPO(Resource name, ISource<?> access);
	void putAccessAO(IAccessAO access);


	// -------------------------------------------------------------------------
	// Stored Procedure Management
	// -------------------------------------------------------------------------
	void addStoredProcedure(StoredProcedure procedure, ISession user);

	void removeStoredProcedure(String name, ISession user);

	// -------------------------------------------------------------------------
	// Stores
	// -------------------------------------------------------------------------
	void addStore(String name, IStore<String, Object> store, ISession user);

	void removeStore(String name, ISession user);

	// -------------------------------------------------------------------------
	// Datatypes
	// -------------------------------------------------------------------------
	public void addDatatype(SDFDatatype dt);

	void removeDatatype(SDFDatatype dt) throws DataDictionaryException;

	// Stores
	List<IStreamObject<?>> getOrCreateStore(Resource name);
	List<IStreamObject<?>> createStore(Resource name);
	void deleteStore(Resource name);




}
