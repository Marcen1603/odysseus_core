package de.uniol.inf.is.odysseus.server.intervalapproach.state;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractOperatorState;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

public class JoinTIPOState<K extends ITimeInterval, T extends IStreamObject<K>> extends AbstractOperatorState {
	
	public static final int SIZE_ESTIMATION_SAMPLE_SIZE = 100;

	private static final long serialVersionUID = 3316591729332909753L;

	private List<Map<Object, ITimeIntervalSweepArea<T>>> groups;;
	private ITransferArea<T, T> transferArea;
	
	/***
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(JoinTIPOState.class);

	public List<Map<Object, ITimeIntervalSweepArea<T>>> getGroups() {
		return groups;
	}

	public void setGroups(List<Map<Object, ITimeIntervalSweepArea<T>>> groups) {
		this.groups = groups;
	}
	
	public ITransferArea<T, T> getTransferArea() {
		return transferArea;
	}

	public void setTransferArea(ITransferArea<T, T> transferArea) {
		this.transferArea = transferArea;
	}
	
	@Override
	public Serializable getSerializedState() {
		return this;
	}
	
	//Not used because even average is at least 5 times the real size...
	@SuppressWarnings("unused")
	private int getMaxTupleSizeInBytesOfSweepAreaSample(ITimeIntervalSweepArea<T> area, int sampleSize) {
		Iterator<T> areaIterator = area.iterator();
		int i=0;
		int maxTupleSize = 0;
		//Get maximum Number of Bytes for (serialized) Tuple/Streamable
		while(areaIterator.hasNext() && i<sampleSize) {
			T streamObject = areaIterator.next();
			int objectSize = getSizeInBytesOfSerializable(streamObject);
			maxTupleSize = (maxTupleSize>objectSize)?maxTupleSize:objectSize;
			i+=1;
		}
		return maxTupleSize;
	}
	

	
	@SuppressWarnings("unused")
	private int getAvgTupleSizeInBytesOfSweepAreaSample(ITimeIntervalSweepArea<T> area, int sampleSize) {
		Iterator<T> areaIterator = area.iterator();
		int i=0;
		long sum = 0;
		//Get maximum Number of Bytes for (serialized) Tuple/Streamable
		while(areaIterator.hasNext() && i<sampleSize) {
			T streamObject = areaIterator.next();
			int objectSize = getSizeInBytesOfSerializable(streamObject);
			sum += objectSize;
			i+=1;
		}
		if(i==0) {
			return 0;
		}
		else {
			return (int)(sum/i);
		}
	}
	
	@Override
	public long estimateSizeInBytes() {
		
		LOG.debug("Estimating JoinTIPOState Size.");
		
		long totalSize = 0;
		
		for (Map<Object, ITimeIntervalSweepArea<T>> port : groups ) {
			for (ITimeIntervalSweepArea<T> sweepArea : port.values()) {
				totalSize += estimateSweepAreaSizeInBytes(sweepArea, SIZE_ESTIMATION_SAMPLE_SIZE);
			}
		}
		
		totalSize += getSizeInBytesOfSerializable(transferArea);
		
		/** Schema based Method.... Result is too small.
		long estimatedSize = 0;
		
		int sizeOfBiggestInputSchema = 0;
		
		for (int i=0; i<sweepAreas.length;i++) {

			ITimeIntervalSweepArea<T> area = sweepAreas[i];
			SDFSchema schema;
			if(referenceOperator.isInOrder()) {
				schema = referenceOperator.getInputSchema(i^1);
			}
			else {
				schema = referenceOperator.getInputSchema(i);
			}
			
			
			int sizeOfSchema = getSizeOfSimpleSchemaInBytes(schema);
			if(hasStringOrListOrComplexDatatypes(schema)){
				Collection<Integer> nonSimpleAttributes = findNonSimpleAttributes(schema);
				int avgSizeOfNonSimpleAttributes = sampleLengthOfNonSimpleAttributesInSweepArea(area, nonSimpleAttributes, SIZE_ESTIMATION_SAMPLE_SIZE);
				sizeOfSchema +=avgSizeOfNonSimpleAttributes;
			}
			int areaSizeInBytes = sizeOfSchema*area.size();
			sizeOfBiggestInputSchema = Math.max(sizeOfBiggestInputSchema,sizeOfSchema);
			LOG.info("SweepArea Area est. size in Bytes {} for {} Tuples",areaSizeInBytes,area.size());
			LOG.info("Real size(serialized): {}",this.getSizeInBytesOfSerializable((Serializable)area));
			estimatedSize+=areaSizeInBytes;
		}
		
		//As transfer Areas store tuples of both Input Ports, we take the biggest Schema estimation for all elements in TransferArea.
		int areaSizeInBytes = sizeOfBiggestInputSchema*transferArea.size();
		estimatedSize+=areaSizeInBytes;
		LOG.info("TransferArea est. size in Bytes {} for {} Tuples",areaSizeInBytes,transferArea.size());
		*///
		
		
		/***	Method by serialization. This works but the result is about 5-8 times to big.
		//For each SweepArea: Multiply number of Tuples with max size of Tuples in Sample from that area.
		for(ITimeIntervalSweepArea<T> area : sweepAreas) {
			
			
			long saSize = getAvgTupleSizeInBytesOfSweepAreaSample(area, SIZE_ESTIMATION_SAMPLE_SIZE)*area.size();
			estimatedSize += saSize;
			combinedMaxSizeOfAllAreas += saSize;
			LOG.debug("Sweep Area with estimated size of {} for {} tuples",saSize,area.size());
		}
		
		long taSize = transferArea.size()*combinedMaxSizeOfAllAreas;
		
		estimatedSize += taSize;
		
		LOG.debug("Transfer Area est. size {} for {} Tuples",taSize,transferArea.size());
		*/
		///
		return totalSize;
	}
	
	
	private long estimateSweepAreaSizeInBytes(ITimeIntervalSweepArea<T> originalArea, int sampleSize) {
		
		ITimeIntervalSweepArea<T> area = originalArea.clone();
		
		if(area.size()<sampleSize) {
			return getSizeInBytesOfSerializable(area);
		}
		
		ITimeIntervalSweepArea<T> oneElementArea = originalArea.clone();
		oneElementArea.clear();
		ITimeIntervalSweepArea<T> fullElementArea = oneElementArea.clone();
		
		oneElementArea.insert(area.peek());
		
		Iterator<T> areaIterator = area.iterator();
		int i=0;
		//Get maximum Number of Bytes for (serialized) Tuple/Streamable
		
		while(areaIterator.hasNext() && i<sampleSize) {
			T streamObject = areaIterator.next();
			fullElementArea.insert(streamObject);
			i+=1;
		}
		
		int sizeOfOneElementArea = getSizeInBytesOfSerializable(oneElementArea);
		int sizeOfFullElementArea = getSizeInBytesOfSerializable(fullElementArea);
		
		int ByteGrowthPerElement = (sizeOfFullElementArea-sizeOfOneElementArea)/(sampleSize-1);
		
		long estimatedAreaSize = ByteGrowthPerElement*(area.size()-1)+sizeOfOneElementArea;
		LOG.info("SweepArea Area est. size in Bytes {} for {} Tuples",estimatedAreaSize,area.size());
		return estimatedAreaSize;
	}
	
	@SuppressWarnings("unused")
	private Collection<Integer> findNonSimpleAttributes(SDFSchema schema) {
		ArrayList<Integer> indizesOfComplexAttributes = Lists.newArrayList();
		for(SDFAttribute attribute : schema) {
			if(isSimpleDataType(attribute)) {
				continue;
			}
			indizesOfComplexAttributes.add(schema.indexOf(attribute));
		}
		return indizesOfComplexAttributes;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unused" })
	private int sampleLengthOfNonSimpleAttributesInSweepArea(ITimeIntervalSweepArea<T> area, Collection<Integer> attributesToSample, int sampleSize) {
		
		Iterator<T> areaIterator = area.iterator();
		int i=0;
		int sum = 0;
		while(areaIterator.hasNext() && i<sampleSize) {
			T streamObject = areaIterator.next();
			if(streamObject instanceof Tuple) {
				Tuple tuple = (Tuple) streamObject;
				for(int attr : attributesToSample) {
					Object attrValue = tuple.getAttribute(attr);
					//If Attribute is String -> Add 2 bytes for every char (As UTF-8 is used for serialization)
					if(attrValue instanceof String) {
						sum += ((String)attrValue).length()*2;
					}
					//TODO Lists.
					if(attrValue instanceof List) {
						
					}
					
				}
				i+=1;
			}
		}
		
		if(i==0) {
			return 0;
		}
		else {
			return sum/i;
		}
	}
}
