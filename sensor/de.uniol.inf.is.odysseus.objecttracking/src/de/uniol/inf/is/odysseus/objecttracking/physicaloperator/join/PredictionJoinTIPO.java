package de.uniol.inf.is.odysseus.objecttracking.physicaloperator.join;

import java.lang.management.MemoryUsage;
import java.util.Iterator;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.base.IMetadataMergeFunction;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunction;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.metadata.LinearProbabilityPredictionFunction;
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
 * @author Jonas Jacobi, abolles
 * 
 * @param <K>
 *            Metadatentyp
 * @param <T>
 *            Datentyp
 */
@SuppressWarnings("unchecked")
public class PredictionJoinTIPO<K extends ITimeInterval & IProbability & IPredictionFunction, T extends MVRelationalTuple<K>>
		extends AbstractPipe<T, T> {

	@Override
	public de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe.OutputMode getOutputMode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void process_next(T object, int port) {
		// TODO Auto-generated method stub
		
	}

//	private ObjectTrackingJoinSweepArea<K, T>[] areas;
//	protected IDataMergeFunction<T> dataMerge;
//	protected IMetadataMergeFunction<K> metadataMerge;
//
//
//	protected ITransferFunction<T> transferFunction;
//	protected IPredicate<? super T> joinPredicate;
//	protected SDFAttributeList leftInputSchema;
//	protected SDFAttributeList rightInputSchema;
//	protected SDFAttributeList outputSchema;
//	
//	int counter = 0;
//	public long duration = 0;
//	public long outDuration = 0;
//	public long outCounter = 0;
//	public long purgeDuration = 0;
//	
//	public PredictionJoinTIPO(){
//		this.areas = null;
//		this.dataMerge = null;
//		this.metadataMerge = null;
//		this.transferFunction = null;
//		this.joinPredicate = null;
//		this.leftInputSchema = null;
//		this.rightInputSchema = null;
//		this.outputSchema = null;
//	}
//
//	public PredictionJoinTIPO(IDataMergeFunction<T> dataMerge,
//			IMetadataMergeFunction<K> metadataMerge,
//			ITransferFunction<T> transferFunction, ObjectTrackingJoinSweepArea<K, T>[] areas) {
//		this.dataMerge = dataMerge;
//		this.metadataMerge = metadataMerge;
//		this.transferFunction = transferFunction;
//		this.areas = areas;
//	}
//	
//	public IPredicate<? super T> getJoinPredicate(){
//		return this.joinPredicate;
//	}
//	
//	public void setJoinPredicate(IPredicate<? super T> joinPred){
//		this.joinPredicate = joinPred;
//	}
//	
//	public ISweepArea<T>[] getAreas() {
//		return areas;
//	}
//	
//	/**
//	 * Returns the SweepArea for the specified port
//	 * @param port 0 returns the left area, 1 returns the right one
//	 * @return the SweepArea for the specified port
//	 */
//	public ISweepArea<T> getArea(int port){
//		return areas[port];
//	}
//	
//	public void setAreas(ObjectTrackingJoinSweepArea<K, T> leftArea, ObjectTrackingJoinSweepArea<K, T> rightArea){
//		this.areas = new ObjectTrackingJoinSweepArea[2];
//		this.areas[0] = leftArea;
//		this.areas[1] = rightArea;
//	}
//	
//	/**
//	 * This method initializes the areas of a prediction join by default with
//	 * RelationalJoinTIMVPredictionSweepAreas and the left and right input schema
//	 * of the join, that has been set before
//	 */
//	public void initDefaultAreas(){
//		this.areas = new ObjectTrackingJoinSweepArea[2];
//		
//		this.areas[0] = new ObjectTrackingJoinSweepArea<K, T>(this.leftInputSchema, this.rightInputSchema);
//		this.areas[0].setQueryPredicate(this.joinPredicate);
//		// the remove predicate is set automatically
//		
//		this.areas[1] = new ObjectTrackingJoinSweepArea<K, T>(this.leftInputSchema, this.rightInputSchema);
//		this.areas[1].setQueryPredicate(this.joinPredicate);
//		// the remove predicate is set automatically
//	}
//	
//	public ITransferFunction<T> getTransferFunction() {
//		return transferFunction;
//	}
//	
//	public void setTransferFunction(ITransferFunction<T> transferFunc){
//		this.transferFunction = transferFunc;
//	}
//	
//	public IDataMergeFunction<T> getDataMerge(){
//		return this.dataMerge;
//	}
//	
//	public void setDataMerge(IDataMergeFunction<T> dataMerge){
//		this.dataMerge = dataMerge;
//	}
//	
//	public IMetadataMergeFunction<K> getMetadataMerge() {
//		return metadataMerge;
//	}
//
//	public void setMetadataMerge(IMetadataMergeFunction<K> metadataMerge) {
//		this.metadataMerge = metadataMerge;
//	}
//	
//	public SDFAttributeList getInputSchema(int port){
//		if(port == 0){
//			return this.leftInputSchema;
//		}
//		else if(port == 1){
//			return this.rightInputSchema;
//		}
//		return null;
//	}
//	
//	public void setInputSchema(int port, SDFAttributeList schema){
//		if(port == 0){
//			this.leftInputSchema = schema;
//		}
//		else if(port == 1){
//			this.rightInputSchema = schema;
//		}
//		
//	}
//	
//	public SDFAttributeList getOutputSchema(){
//		return this.outputSchema;
//	}
//	
//	public void setOutputSchema(SDFAttributeList outSchema){
//		this.outputSchema = outSchema;
//	}
//
//
//	@Override
//	protected void process_next(T object, int port) {
//		long start = System.currentTimeMillis();
//		counter++;
//		if (isDone()) { // TODO bei den sources abmelden ?? MG: Warum??
//			// propagateDone gemeint?
//			// JJ: weil man schon fertig sein
//			// kann, wenn ein strom keine elemente liefert, der
//			// andere aber noch, dann muss man von dem anderen keine
//			// eingaben mehr verarbeiten, was dazu fuehren kann,
//			// dass ein kompletter teilplan nicht mehr ausgefuehrt
//			// werden muss, man also ressourcen spart
//			return;
//		}
//		
//		int otherport = port ^ 1;
//		Order order = Order.fromOrdinal(port);
//		synchronized (this.areas[otherport]) {
//			long sp = System.currentTimeMillis();
//			areas[otherport].purgeElements(object, order);
//			long ep = System.currentTimeMillis();
//			purgeDuration += (ep - sp);
//		}
//
//		synchronized (this) {
//			// status could change, if the other port was done and
//			// its sweeparea is now empty after purging
//			if (isDone()) {
//				propagateDone();
//				return;
//			}
//		}
//		
//		// Testausgabe
////		if(((RelationalTuple)object).getAttribute(0).toString().equals("1951")){
////			System.out.println("Found object");
////		}
//		
//		Iterator<MergePair<T>> qualifies;
//		synchronized (this.areas) {
//			synchronized (this.areas[otherport]) {
//				qualifies = areas[otherport].queryCopyPrediction(object, order);
//			}
//			transferFunction.newElement(object, port);
//			synchronized (areas[port]) {
//				areas[port].insert(object);
//			}
//		}
//		
//		long end = System.currentTimeMillis();
//		this.duration += (end - start);
//
//		while (qualifies.hasNext()) {
//			start = System.currentTimeMillis();
//			outCounter++;
//			
//			MergePair<T> next = qualifies.next();
//			T newElement = merge(next.getLeft(), next.getRight(), order);
//			transferFunction.transfer(newElement);
//			
//			end = System.currentTimeMillis();
//			outDuration += (end - start);
//		}
//		
//
//	}
//
//	private T merge(T left, T right, Order order) {
//		T mergedData;
//		K mergedMetadata;
//		if (order == Order.LeftRight) {
//			mergedData = dataMerge.merge(left, right);
//			mergedMetadata = metadataMerge.mergeMetadata(left.getMetadata(),
//					right.getMetadata());
//		} else {
//			mergedData = dataMerge.merge(right, left);
//			mergedMetadata = metadataMerge.mergeMetadata(right.getMetadata(),
//					left.getMetadata());
//		}
//		mergedData.setMetadata(mergedMetadata);
//		return mergedData;
//	}
//
//	@Override
//	protected void process_open() throws OpenFailedException {
//		for (int i = 0; i < 2; ++i) {
//			this.areas[i].clear();
//			this.areas[i].init();
//		}
//		this.dataMerge.init();
//		this.metadataMerge.init();
//		this.transferFunction.init(this);
//	}
//
//	@Override
//	protected void process_done() {
//		transferFunction.done();
//		areas[0].clear();
//		areas[1].clear();
//		System.out.println("Join-Duration (JoinTIPO): " + this.duration);
//		System.out.println("Join SA Duration: " + ObjectTrackingJoinSweepArea.duration);
//		System.out.println("Join SA Duration normal: " + ObjectTrackingJoinSweepArea.durationNormal);
//		System.out.println("Join SA Duration prediction: " + ObjectTrackingJoinSweepArea.durationPrediction);
//		System.out.println("Join SA Duration prediction predicate: "+ObjectTrackingJoinSweepArea.durationPredictionPredicate);
//		System.out.println("Join Prediction Data Duration: " + LinearProbabilityPredictionFunction.durationData);
//		System.out.println("Join Prediction Data Expression Duration: " + LinearProbabilityPredictionFunction.durationExpr);
//		System.out.println("Join Prediction MData Duration: " + LinearProbabilityPredictionFunction.durationMData);
//		System.out.println("Query Aufrufe SA: " + ObjectTrackingJoinSweepArea.counter);
//		System.out.println("Prediction Aufrufe: " + LinearProbabilityPredictionFunction.counter);
//		System.out.println("Join ProcessNext-Aufrufe: " + this.counter);
//		System.out.println("OutDuration: " + this.outDuration);
//		System.out.println("OutCounter: " + this.outCounter);
//		System.out.println("PurgeDuration: " + this.purgeDuration);
//		System.out.println("Compares: " + ObjectTrackingJoinSweepArea.compareCounter);
//	}
//
//	@Override
//	protected boolean isDone() {
//		if (getSubscribedToSource(0).isDone()) {
//			return getSubscribedToSource(1).isDone() || areas[0].isEmpty();
//		} else {
//			return getSubscribedToSource(0).isDone() && areas[1].isEmpty();
//		}
//	}
//
//
//	/**
//	 * Sorgt dafuer, dass das {@link MemoryUsage} MetadataItem an die SweepAreas
//	 * des Operators gelangen kann, um die enthaltenen Elemente zaehlen zu
//	 * koennen
//	 * 
//	 * @param factory
//	 *            - {@link MemoryUsageMonitoringDataFactory}, die ein MetadataItem
//	 *            erzeugt welches auf die SweepAreas zugreifen kann.
//	 *            
//	 * TODO Memyory usage erneut einbauen. Muss aber von  intervall und pn entkoppelt werden.
//	 */
////	public void addMemoryUsageMetadataItem(MemoryUsageMonitoringDataFactory factory) {
////		IMonitoringData<Integer> item = factory.createTIJoinMemoryHandler(this, areas);
////		addMonitoringData(MemoryUsageMonitoringDataFactory.METADATA_TYPE, item);
////	}
//
//	@Override
//	public OutputMode getOutputMode() {
//		// TODO Auto-generated method stub
//		return OutputMode.MODIFIED_INPUT;
//	}
	
	@Override
	public PredictionJoinTIPO<K,T> clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

}