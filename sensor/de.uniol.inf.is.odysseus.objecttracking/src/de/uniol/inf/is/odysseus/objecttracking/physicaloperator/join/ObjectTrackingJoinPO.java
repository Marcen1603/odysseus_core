package de.uniol.inf.is.odysseus.objecttracking.physicaloperator.join;

import java.lang.management.MemoryUsage;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IApplicationTime;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.physicaloperator.join.sweeparea.ObjectTrackingJoinSweepArea;
import de.uniol.inf.is.odysseus.objecttracking.predicate.range.IRangePredicate;
import de.uniol.inf.is.odysseus.objecttracking.util.Pair;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.base.IDataMergeFunction;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea;
import de.uniol.inf.is.odysseus.physicaloperator.base.ITransferFunction;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISweepArea.Order;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


/**
 * Der JoinOperator kann zwar von den Generics her gesehen unabhaengig von
 * Daten- und Metadatenmodell betrachtet werden. Die Logik des in dieser Klasse
 * implementierten Operators entspricht jedoch der Logik eines JoinOperators im
 * Intervallansatz.
 * 
 * @author Jonas Jacobi, Andre Bolles
 * 
 * @param <K>
 *            Metadatentyp
 * @param <T>
 *            Datentyp
 */
@SuppressWarnings("unchecked")
public class ObjectTrackingJoinPO<K extends ITimeInterval & IProbability & IPredictionFunctionKey & IApplicationTime, T extends MVRelationalTuple<K>>
		extends AbstractPipe<T, T> {

	private ObjectTrackingJoinSweepArea<K, T>[] areas;
	protected IDataMergeFunction<T> dataMerge;
	protected IMetadataMergeFunction<K> metadataMerge;
	
	protected Map<IPredicate, IRangePredicate> rangePredicates;

	protected ITransferFunction<T> transferFunction;
	protected IPredicate<? super T> joinPredicate;
	protected SDFAttributeList leftInputSchema;
	protected SDFAttributeList rightInputSchema;
	protected SDFAttributeList outputSchema;
	
	int counter = 0;
	public long duration = 0;
	public long outDuration = 0;
	public long outCounter = 0;
	public long purgeDuration = 0;
	
	public ObjectTrackingJoinPO(){
		this.areas = null;
		this.dataMerge = null;
		this.metadataMerge = null;
		this.transferFunction = null;
		this.joinPredicate = null;
		this.leftInputSchema = null;
		this.rightInputSchema = null;
		this.outputSchema = null;
		this.rangePredicates = null;
	}

	public ObjectTrackingJoinPO(
			Map<IPredicate, IRangePredicate> rangePredicates,
			IDataMergeFunction<T> dataMerge,
			IMetadataMergeFunction<K> metadataMerge,
			ITransferFunction<T> transferFunction,
			ObjectTrackingJoinSweepArea<K, T>[] areas) {
		this.rangePredicates = rangePredicates;
		this.dataMerge = dataMerge;
		this.metadataMerge = metadataMerge;
		this.transferFunction = transferFunction;
		this.areas = areas;
	}
	
	// The join predicate is not used in this join.
	// Instead the corrsponding range predicates are used.
//	public IPredicate<? super T> getJoinPredicate(){
//		return this.joinPredicate;
//	}
//	
//	public void setJoinPredicate(IPredicate<? super T> joinPred){
//		this.joinPredicate = joinPred;
//	}
	
	public void setRangePredicates(Map<IPredicate, IRangePredicate> rangePredicates){
		this.rangePredicates = rangePredicates;
	}
	
	public Map<IPredicate, IRangePredicate> getRangePredicates(){
		return this.rangePredicates;
	}
	
	public ISweepArea<T>[] getAreas() {
		return areas;
	}
	
	/**
	 * Returns the SweepArea for the specified port
	 * @param port 0 returns the left area, 1 returns the right one
	 * @return the SweepArea for the specified port
	 */
	public ISweepArea<T> getArea(int port){
		return areas[port];
	}
	
	public void setAreas(ObjectTrackingJoinSweepArea<K, T> leftArea, ObjectTrackingJoinSweepArea<K, T> rightArea){
		this.areas = new ObjectTrackingJoinSweepArea[2];
		this.areas[0] = leftArea;
		this.areas[1] = rightArea;
	}
	
	/**
	 * This method initializes the areas of a prediction join by default with
	 * RelationalJoinTIMVPredictionSweepAreas and the left and right input schema
	 * of the join, that has been set before
	 */
	public void initDefaultAreas(){
		this.areas = new ObjectTrackingJoinSweepArea[2];
		
		this.areas[0] = new ObjectTrackingJoinSweepArea<K, T>(this.leftInputSchema, this.rightInputSchema);
		this.areas[0].setRangePredicates(this.rangePredicates);
		// the remove predicate is set automatically
		
		this.areas[1] = new ObjectTrackingJoinSweepArea<K, T>(this.leftInputSchema, this.rightInputSchema);
		this.areas[1].setRangePredicates(this.rangePredicates);
		// the remove predicate is set automatically
	}
	
	public ITransferFunction<T> getTransferFunction() {
		return transferFunction;
	}
	
	public void setTransferFunction(ITransferFunction<T> transferFunc){
		this.transferFunction = transferFunc;
	}
	
	public IDataMergeFunction<T> getDataMerge(){
		return this.dataMerge;
	}
	
	public void setDataMerge(IDataMergeFunction<T> dataMerge){
		this.dataMerge = dataMerge;
	}
	
	public IMetadataMergeFunction<K> getMetadataMerge() {
		return metadataMerge;
	}

	public void setMetadataMerge(IMetadataMergeFunction<K> metadataMerge) {
		this.metadataMerge = metadataMerge;
	}
	
	public SDFAttributeList getInputSchema(int port){
		if(port == 0){
			return this.leftInputSchema;
		}
		else if(port == 1){
			return this.rightInputSchema;
		}
		return null;
	}
	
	public void setInputSchema(int port, SDFAttributeList schema){
		if(port == 0){
			this.leftInputSchema = schema;
		}
		else if(port == 1){
			this.rightInputSchema = schema;
		}
		
	}
	
	public SDFAttributeList getOutputSchema(){
		return this.outputSchema;
	}
	
	public void setOutputSchema(SDFAttributeList outSchema){
		this.outputSchema = outSchema;
	}


	@Override
	protected void process_next(T object, int port) {
		if (isDone()) { // TODO bei den sources abmelden ?? MG: Warum??
			// propagateDone gemeint?
			// JJ: weil man schon fertig sein
			// kann, wenn ein strom keine elemente liefert, der
			// andere aber noch, dann muss man von dem anderen keine
			// eingaben mehr verarbeiten, was dazu fuehren kann,
			// dass ein kompletter teilplan nicht mehr ausgefuehrt
			// werden muss, man also ressourcen spart
			return;
		}
		
		int otherport = port ^ 1;
		Order order = Order.fromOrdinal(port);
		synchronized (this.areas[otherport]) {
			areas[otherport].purgeElements(object, order);
		}

		synchronized (this) {
			// status could change, if the other port was done and
			// its sweeparea is now empty after purging
			if (isDone()) {
				propagateDone();
				return;
			}
		}
		
		
		Iterator<Pair<T, List<ITimeInterval>>> qualifies;
		synchronized (this.areas) {
			synchronized (this.areas[otherport]) {
				long start = System.nanoTime();
				qualifies = areas[otherport].queryOT(object, order);
				long end = System.nanoTime();
				this.duration += (end - start);
			}
			transferFunction.newElement(object, port);
			synchronized (areas[port]) {
				areas[port].insert(object);
			}
		}
		

		while (qualifies.hasNext()) {
			Pair<T, List<ITimeInterval>> next = qualifies.next();
			T newElement = merge(object, next, order);
			transferFunction.transfer(newElement);			
		}
		
		

	}

	private T merge(T left, Pair<T, List<ITimeInterval>> fromSA, Order order) {
		T mergedData;
		K mergedMetadata;
		if (order == Order.LeftRight) {
			mergedData = dataMerge.merge(left, fromSA.getLeft());
			mergedMetadata = metadataMerge.mergeMetadata(left.getMetadata(),
					fromSA.getLeft().getMetadata());
		} else {
			mergedData = dataMerge.merge(fromSA.getLeft(), left);
			mergedMetadata = metadataMerge.mergeMetadata(fromSA.getLeft().getMetadata(),
					left.getMetadata());
		}
		mergedData.setMetadata(mergedMetadata);
		// setting the application intervals
		mergedData.getMetadata().setApplicationIntervals(fromSA.getRight());
		
		return mergedData;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		for (int i = 0; i < 2; ++i) {
			this.areas[i].clear();
			this.areas[i].init();
		}
		this.dataMerge.init();
		this.metadataMerge.init();
		this.transferFunction.init(this);
	}

	@Override
	protected void process_done() {
		transferFunction.done();
		System.out.println("Join comparisons: " + (this.areas[0].compareCounter + this.areas[1].compareCounter));
		System.out.println("Join duration: " + this.duration + "ns");
		System.out.println("Comparisons per second: " + (1e9 * (this.areas[0].compareCounter + this.areas[1].compareCounter) / this.duration));
		areas[0].clear();
		areas[1].clear();
	}

	@Override
	protected boolean isDone() {
		if (getSubscribedToSource(0).isDone()) {
			return getSubscribedToSource(1).isDone() || areas[0].isEmpty();
		} else {
			return getSubscribedToSource(0).isDone() && areas[1].isEmpty();
		}
	}


	/**
	 * Sorgt dafuer, dass das {@link MemoryUsage} MetadataItem an die SweepAreas
	 * des Operators gelangen kann, um die enthaltenen Elemente zaehlen zu
	 * koennen
	 * 
	 * @param factory
	 *            - {@link MemoryUsageMonitoringDataFactory}, die ein MetadataItem
	 *            erzeugt welches auf die SweepAreas zugreifen kann.
	 *            
	 * TODO Memyory usage erneut einbauen. Muss aber von  intervall und pn entkoppelt werden.
	 */
//	public void addMemoryUsageMetadataItem(MemoryUsageMonitoringDataFactory factory) {
//		IMonitoringData<Integer> item = factory.createTIJoinMemoryHandler(this, areas);
//		addMonitoringData(MemoryUsageMonitoringDataFactory.METADATA_TYPE, item);
//	}

	@Override
	public OutputMode getOutputMode() {
		// TODO Auto-generated method stub
		return OutputMode.MODIFIED_INPUT;
	}
	
	@Override
	public ObjectTrackingJoinPO<K, T> clone(){
		throw new RuntimeException();
	}

}