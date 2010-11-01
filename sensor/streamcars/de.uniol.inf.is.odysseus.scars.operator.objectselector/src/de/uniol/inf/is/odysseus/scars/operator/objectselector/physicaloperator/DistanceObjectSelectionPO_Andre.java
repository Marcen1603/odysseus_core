package de.uniol.inf.is.odysseus.scars.operator.objectselector.physicaloperator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.scars.base.SDFObjectRelationalExpression;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.util.SchemaHelper;
import de.uniol.inf.is.odysseus.scars.util.SchemaIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleHelper;
import de.uniol.inf.is.odysseus.scars.util.TupleIndexPath;
import de.uniol.inf.is.odysseus.scars.util.TupleInfo;
import de.uniol.inf.is.odysseus.scars.util.TupleIterator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

/**
 * THIS CLASS IS ONLY A HACK FOR EVALUATION PURPOSES.
 * 
 * @author Andre Bolles
 *
 * @param <M>
 */
public class DistanceObjectSelectionPO_Andre<M extends IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer & ITimeInterval> extends
		AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	private String trackedObjectList;
	private SchemaHelper schemaHelper;
	private SchemaIndexPath trackedObjectListSchemaIndexPath;
	
	private HashMap<IPredicate, SDFObjectRelationalExpression> solutions = new HashMap<IPredicate, SDFObjectRelationalExpression>();

	public DistanceObjectSelectionPO_Andre() {
	}

	public DistanceObjectSelectionPO_Andre(DistanceObjectSelectionPO_Andre<M> distanceObjectSelectorPO) {
		super(distanceObjectSelectorPO);;
		this.trackedObjectList = distanceObjectSelectorPO.trackedObjectList;
		this.solutions = distanceObjectSelectorPO.solutions;
	}

	protected double getValueByName(MVRelationalTuple<M> tuple, TupleIndexPath tupleIndexPath, String attributName) {
		for (TupleInfo info : new TupleIterator(tuple, tupleIndexPath)) {
			if (info.attribute.getAttributeName().equals(attributName)) {
				return new Double(info.tupleObject.toString());
			}
		}

		return 0;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		this.sendPunctuation(timestamp);
	}
	
	protected double getValueByName(MVRelationalTuple<M> tuple, TupleIndexPath tupleIndexPath, SDFAttribute attribute) {
		for (TupleInfo info : new TupleIterator(tuple, tupleIndexPath)) {
			if (info.attribute.getAttributeName().equals(attribute.getAttributeName())) {
				return new Double(info.tupleObject.toString());
			}
		}

		return 0;
	}
	
//	@SuppressWarnings("unchecked")
//	@Override
//	protected void process_next(MVRelationalTuple<M> object, int port) {
//		TupleIndexPath trackedObjectListTupleIndexPath = this.trackedObjectListSchemaIndexPath.toTupleIndexPath(object);
//		ArrayList<MVRelationalTuple<M>> newList = new ArrayList<MVRelationalTuple<M>>();
//
//		for (TupleInfo tupleInfo : trackedObjectListTupleIndexPath) {
//			double x = getValueByName(object, tupleInfo.tupleIndexPath, this.trackedObjectX);
//			double y = getValueByName(object, tupleInfo.tupleIndexPath, this.trackedObjectY);
//
//			boolean threat = x >= -this.distanceThresholdXLeft && x <= this.distanceThresholdXRight;
//			threat &= y >= -this.distanceThresholdYLeft && y <= this.distanceThresholdYRight;
////			System.out.println("Threat: " + threat + " - " + tupleInfo.tupleObject.toString());
//			if (threat) {
//				newList.add((MVRelationalTuple<M>) tupleInfo.tupleObject);
//			}
//		}
//		
//		Object[] result = new Object[2];
//		// get timestamp path from scanned data
//		SchemaIndexPath path = schemaHelper.getSchemaIndexPath(schemaHelper.getStartTimestampFullAttributeName());
//		result[0] = path.toTupleIndexPath(object).getTupleObject();
//
//		MVRelationalTuple<M> tuples = new MVRelationalTuple<M>(newList.size());
//		int counter = 0;
//		for (MVRelationalTuple<M> mvRelationalTuple : newList) {
//			tuples.setAttribute(counter++, mvRelationalTuple);
//		}
//
//		// get scanned objects
//		result[1] = new MVRelationalTuple<M>(tuples);
//
//		MVRelationalTuple<M> base = new MVRelationalTuple<M>(1);
//		base.setMetadata(object.getMetadata());
//		base.setAttribute(0, new MVRelationalTuple<M>(result));
//
//		transfer(base);
//	}
	

	@SuppressWarnings("unchecked")
	@Override
	protected void process_next(MVRelationalTuple<M> scanTuple, int port) {
		
		for(Entry<IPredicate, SDFObjectRelationalExpression> entry : this.solutions.entrySet()){
			if(entry.getKey().evaluate(scanTuple)){
				SDFObjectRelationalExpression expr = entry.getValue();
				TupleIndexPath trackedObjectListTupleIndexPath = this.trackedObjectListSchemaIndexPath.toTupleIndexPath(scanTuple);
				
				for (TupleInfo tupleInfo : trackedObjectListTupleIndexPath) {
					
					Object[] values = new Object[expr.getAllAttributes().size()];
					
					for(int i = 0; i<expr.getAllAttributes().size(); i++){
						SDFAttribute curAttr = expr.getAllAttributes().get(i);
						values[i] = getValueByName(scanTuple, tupleInfo.tupleIndexPath, curAttr);
					}
					
					expr.bindVariables(values);
					entry.getValue().getValue();
					
				}				

				break;
			}
		}
		
		transfer(scanTuple);
		
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		schemaHelper = new SchemaHelper(getSubscribedToSource(0).getSchema());
		trackedObjectListSchemaIndexPath = schemaHelper.getSchemaIndexPath(this.trackedObjectList);
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	public DistanceObjectSelectionPO_Andre<M> clone() {
		return new DistanceObjectSelectionPO_Andre<M>(this);
	}

	public void setTrackedObjectList(String trackedObjectList) {
		this.trackedObjectList = trackedObjectList;
	}

	public String getTrackedObjectList() {
		return trackedObjectList;
	}
	
	public void setSolutions(HashMap<IPredicate, SDFObjectRelationalExpression> solutions){
		this.solutions = solutions;
	}

}
