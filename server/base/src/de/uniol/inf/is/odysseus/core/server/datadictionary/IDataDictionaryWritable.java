package de.uniol.inf.is.odysseus.core.server.datadictionary;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;


public interface IDataDictionaryWritable extends IDataDictionary{

	// -------------------------------------------------------------------------
	// View and Stream Management
	// -------------------------------------------------------------------------

	ILogicalOperator removeViewOrStream(String viewname, ISession caller);
	ILogicalOperator removeViewOrStream(Resource viewname, ISession caller);
	
	// -------------------------------------------------------------------------
	// View Management
	// -------------------------------------------------------------------------

	void setView(String viewname, ILogicalOperator topOperator, ISession caller)
			throws DataDictionaryException;

	// -------------------------------------------------------------------------
	// Stream Management
	// -------------------------------------------------------------------------

	void setStream(String streamname, ILogicalOperator plan, ISession caller)
			throws DataDictionaryException;

	// -------------------------------------------------------------------------
	// Sink Management
	// -------------------------------------------------------------------------

	void addSink(String sinkname, ILogicalOperator sink, ISession caller)
			throws DataDictionaryException;
	
	void addSink(Resource sinkname, ILogicalOperator sink, ISession caller)
			throws DataDictionaryException;

	ILogicalOperator removeSink(String name, ISession caller);
	
	ILogicalOperator removeSink(Resource name, ISession caller);

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
	
	ISource<?> getAccessAO(Resource name);

	void putAccessAO(Resource name, ISource<?> access);


	// -------------------------------------------------------------------------
	// Stored Procedure Management
	// -------------------------------------------------------------------------
	void addStoredProcedure(StoredProcedure procedure, ISession user);

	void removeStoredProcedure(String name, ISession user);


	// -------------------------------------------------------------------------
	// Datatypes
	// -------------------------------------------------------------------------
	public void addDatatype(SDFDatatype dt);

	void removeDatatype(SDFDatatype dt) throws DataDictionaryException;




}
