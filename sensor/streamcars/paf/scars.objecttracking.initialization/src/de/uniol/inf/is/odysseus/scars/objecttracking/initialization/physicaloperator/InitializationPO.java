package de.uniol.inf.is.odysseus.scars.objecttracking.initialization.physicaloperator;


import java.util.HashMap;

import de.uniol.inf.is.odysseus.base.ISubscription;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.scars.objecttracking.filter.Parameters;
import de.uniol.inf.is.odysseus.scars.objecttracking.initialization.AbstractInitializationFunction;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.Connection;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.ConnectionList;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IGain;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleHelper;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleInfo;
import de.uniol.inf.is.odysseus.scars.util.TupleIterator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

/**
 * @author dtwumasi
 * 
 */
public class InitializationPO<M extends IGain & IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer> extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private SchemaHelper schemaHelper;
	private SchemaIndexPath newObjectListPath;
	private SchemaIndexPath newObjPath;
	private SchemaIndexPath oldObjectListPath;
	private SchemaIndexPath oldObjPath;
	SDFAttributeList inputSchema;
	
	private AbstractInitializationFunction initalizationFunction;
	
	

	// path to new  objects
	private String newObjListPath;
	// path to old  objects
	private String oldObjListPath;
	
	private HashMap<Enum, Object> parameters;
	
	protected boolean havingData = false;
	
	public InitializationPO() {
		super();
	}
	
	public InitializationPO(InitializationPO<M> copy) {
		super(copy);
		this.initalizationFunction= copy.getInitalizationFunction().clone();
		this.setNewObjListPath(new String(copy.getNewObjListPath()));
		this.setOldObjListPath(new String(copy.getOldObjListPath()));
		this.setParameters(new HashMap<Enum, Object>(copy.getParameters()));	
		this.havingData= copy.havingData;
	}
	

	@Override
	public AbstractPipe clone() {
		return new InitializationPO<M>(this);
	}

	@Override
	public de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe.OutputMode getOutputMode() {
	  return OutputMode.NEW_ELEMENT;
	}
	
	 @Override
	  public void processPunctuation(PointInTime timestamp, int port) {
	    if (!havingData) {
	      this.sendPunctuation(timestamp);
	      havingData = false;
	    }
	  }

	public void setParameters(HashMap<Enum, Object> parameters) {
		this.parameters = parameters;
	}

	public HashMap<Enum, Object> getParameters() {
		return parameters;
	}
	
	@Override
	protected void process_open() throws OpenFailedException {
	
		super.process_open();
		inputSchema = this.getSubscribedToSource(0).getTarget()
				.getOutputSchema();
		this.schemaHelper = new SchemaHelper(inputSchema);

		this.newObjectListPath = this.schemaHelper.getSchemaIndexPath(this
				.getNewObjListPath());
		this.newObjPath = this.schemaHelper.getSchemaIndexPath(this
				.getNewObjListPath()
				+ SchemaHelper.ATTRIBUTE_SEPARATOR
				+ this.newObjectListPath.getAttribute().getSubattribute(0)
						.getAttributeName());
	
		this.oldObjectListPath = this.schemaHelper.getSchemaIndexPath(this
				.getOldObjListPath());
	

	}
	
	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
	
		havingData = true;
		
		object = initalizationFunction.compute(object, newObjectListPath, oldObjectListPath);
		
		transfer(object);
	}
	
	public AbstractInitializationFunction getInitalizationFunction() {
		return initalizationFunction;
	}

	public void setInitalizationFunction(
			AbstractInitializationFunction initalizationFunction) {
		this.initalizationFunction = initalizationFunction;
	}
	
	public void setNewObjListPath(String newObjListPath) {
		this.newObjListPath = newObjListPath;
	}

	public String getNewObjListPath() {
		return newObjListPath;
	}

	public void setOldObjListPath(String oldObjListPath) {
		this.oldObjListPath = oldObjListPath;
	}

	public String getOldObjListPath() {
		return oldObjListPath;
	}

}
