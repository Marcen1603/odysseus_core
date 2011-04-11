package de.uniol.inf.is.odysseus.sparql.physicalops.base.set;

import java.util.Iterator;

import de.uniol.inf.is.odysseus.monitor.metadata.IMonitoringData;
import de.uniol.inf.is.odysseus.monitor.metadata.MemoryUsage;
import de.uniol.inf.is.odysseus.monitor.metadata.MemoryUsageMonitoringDataFactory;
import de.uniol.inf.is.odysseus.queryexecution.po.base.object.IClone;
import de.uniol.inf.is.odysseus.queryexecution.po.base.object.PointInTime;
import de.uniol.inf.is.odysseus.queryexecution.po.base.object.sweeparea.ISweepArea;
import de.uniol.inf.is.odysseus.queryexecution.po.base.object.sweeparea.ISweepArea.Order;
import de.uniol.inf.is.odysseus.queryexecution.po.base.operators.AbstractPipe;
import de.uniol.inf.is.odysseus.queryexecution.po.base.set.UnionHelper;
import de.uniol.inf.is.odysseus.queryexecution.po.sparql.object.NodeList;
import de.uniol.inf.is.odysseus.queryexecution.po.sparql.util.SPARQL_Util;
import de.uniol.inf.is.odysseus.querytranslation.logicalops.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Andre Bolles
 */
public class SPARQLUnionPO<M extends IClone> extends AbstractPipe<NodeList<M>, NodeList<M>> {

	/**
	 * the logical counterpart to this one
	 */
	AbstractLogicalOperator ao;
	
	/**
	 * 
	 * WICHTIG: Im PN-Ansatz darf keine SweepArea verwendet werden, die Element
	 * beim Insert l�scht (negatives Einf�gen).
	 */
	ISweepArea<NodeList<M>> area;

	/**
	 * 0 = left min timestamp 1 = right min timestamp
	 */
	PointInTime minTs[];

	UnionHelper<NodeList<M>> helper;

	/**
	 * WICHTIG: Im PN-Ansatz darf keine SweepArea verwendet werden, die Element
	 * beim Insert l�scht (negatives Einf�gen).
	 * 
	 * @param area
	 * @param helper
	 */
	public SPARQLUnionPO(AbstractLogicalOperator ao, ISweepArea<NodeList<M>> area, UnionHelper<NodeList<M>> helper) {
		super();
		this.ao = ao;
		this.area = area;
		this.helper = helper;
		minTs = new PointInTime[2];
	}

	public SPARQLUnionPO(SPARQLUnionPO<M> unionPO) {
		this.ao = unionPO.ao;
		this.area = unionPO.area;
		this.helper = unionPO.helper;
		minTs = unionPO.minTs;
	}

	@Override
	public SPARQLUnionPO<M> clone() {
		return new SPARQLUnionPO<M>(this);
	}

	//private boolean[] done;

	@Override
	protected synchronized void process_next(NodeList<M> object, int port,
			boolean isReadOnly) {

		// setting the timestamp must be independent
		// of the metadata type
		synchronized (minTs) {
			synchronized (this.helper) {
				minTs[port] = this.helper.getStart(object);
			}
		}

		// getting the minimal timestamp of both inputs
		PointInTime min_ts = null;
		synchronized (this.minTs) {
			if (this.minTs[0] != null && this.minTs[1] != null) {
				min_ts = PointInTime.min(this.minTs[0], this.minTs[1]);
			}
		}

		synchronized (this.area) {
			// if the object is from the left, additional attributes
			// have to appended on the right
			
			if(port == 0){
				NodeList<M> attrs = new NodeList<M>();
				SDFAttributeList outAttrs = this.ao.getOutputSchema();
				SDFAttributeList leftAttrs = this.ao.getInputSchema(0);
				
				// the number of the actual attribute to add
				for(int i = 0; i<outAttrs.size(); i++){
					// check if this attribute is in left
					// if it is in left, add it to the list
					boolean found = false;
					for(int u = 0; u<leftAttrs.size() && !found; u++){
						if(refersSameVar(leftAttrs.get(u), outAttrs.get(i))){
							attrs.add(object.get(u));
							found = true;
						}
					}
					// if not add the extra attribute node
					if(!found){
						attrs.add(SPARQL_Util.EXTRA_ATTRIBUTE_NODE);
					}
				}
				attrs.setMetadata(object.getMetadata());
				object = attrs;
			}			
			// if the object ist from the right, additional attributes
			// have to be appended on the left
			else if(port == 1){
				// fill the attributes correctly
				NodeList<M> attrs = new NodeList<M>();
				SDFAttributeList outAttrs = this.ao.getOutputSchema();
				SDFAttributeList rightAttrs = this.ao.getInputSchema(1);
				
				// the number of the actual attribute to add
				for(int i = 0; i<outAttrs.size(); i++){
					// check if this attribute is in left
					// if it is in left, add it to the list
					boolean found = false;
					for(int u = 0; u<rightAttrs.size() && !found; u++){
						if(refersSameVar(rightAttrs.get(u), outAttrs.get(i))){
							attrs.add(object.get(u));
							found = true;
						}
					}
					// if not add the extra attribute node
					if(!found){
						attrs.add(SPARQL_Util.EXTRA_ATTRIBUTE_NODE);
					}
				}
				attrs.setMetadata(object.getMetadata());
				object = attrs;
			}

			this.area.insert(object);

			if (min_ts != null) {
				// get all elements from the queue, that
				// have a (start) timestamp smaller than
				// min_ts and transfer them to the next operator
				synchronized (this.helper) {
					// WICHTIG: RightLeft benutzen, da so ein
					// StartsBeforePredicate zu einem StartsAfterPredicate wird
					Iterator<NodeList<M>> iter = this.area.extractElements(this.helper
							.getReferenceElement(min_ts, object),
							Order.RightLeft);
					while (iter.hasNext()) {
						this.transfer(iter.next());
					}
				}
			}
		}
	}
	
	private boolean refersSameVar(SDFAttribute a, SDFAttribute b) {
		return a.getQualName().equals(b.getQualName());
	}


	public final void process_close() {
	}

	/**
	 * Sorgt dafuer, dass das {@link MemoryUsage} MetadataItem an die SweepArea
	 * des Operators gelangen kann, um die enthaltenen Elemente zaehlen zu
	 * koennen
	 * 
	 * @param factory
	 *            - {@link MemoryUsageMonitoringDataFactory}, die ein MetadataItem
	 *            erzeugt welches auf die SweepAreas zugreifen kann.
	 */
	public void addMemoryUsageMetadataItem(MemoryUsageMonitoringDataFactory factory) {
		IMonitoringData<Integer> item = factory.createUnionMemoryHandler(this, area);
		addMonitoringData(MemoryUsageMonitoringDataFactory.METADATA_TYPE, item);
	}

//	@Override
//	public void done(int port) {
//		synchronized (this.done) {
//			done[port] = true;
//			if (done[0] && done[1]) {
//				propagateDone();
//			}
//		}
//	}
}
