package de.uniol.inf.is.odysseus.server.intervalapproach.state;

import java.io.Serializable;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.AbstractOperatorState;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferArea;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;

public class JoinTIPOState<K extends ITimeInterval, T extends IStreamObject<K>> extends AbstractOperatorState {
	
	public static final int SIZE_ESTIMATION_SAMPLE_SIZE = 100;

	private static final long serialVersionUID = 3316591729332909753L;

	private ITimeIntervalSweepArea<T>[] sweepAreas;
	private ITransferArea<T, T> transferArea;
	

	/***
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(JoinTIPOState.class);

	public ITimeIntervalSweepArea<T>[] getSweepAreas() {
		return sweepAreas;
	}

	public void setSweepAreas(ITimeIntervalSweepArea<T>[] sweepAreas) {
		this.sweepAreas = sweepAreas;
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
	
	public long estimateSizeInBytes() {
		LOG.debug("Estimating JoinTIPOState Size.");
		long estimatedSize = 0;
		
		int combinedMaxSizeOfAllAreas = 0;
		
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
		
		return estimatedSize;
	}
	
}
