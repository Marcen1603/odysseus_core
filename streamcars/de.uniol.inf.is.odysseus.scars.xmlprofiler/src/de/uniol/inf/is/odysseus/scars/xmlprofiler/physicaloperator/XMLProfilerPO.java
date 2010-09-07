package de.uniol.inf.is.odysseus.scars.xmlprofiler.physicaloperator;

import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.xmlprofiler.profiler.ScarsXMLProfiler;

public class XMLProfilerPO<M extends IProbability & ITimeInterval & IConnectionContainer & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>>>
extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	String operatorName;
	String fileName;
	String filePath;

	public XMLProfilerPO() {
		super();
	}

	public XMLProfilerPO(XMLProfilerPO<M> clone) {
		super(clone);
		this.operatorName = clone.operatorName;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		this.sendPunctuation(timestamp);
	}

	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		ScarsXMLProfiler p = ScarsXMLProfiler.getInstance(fileName, 0, 2);
		p.profile(operatorName,0, getOutputSchema(), object);
		p.setAttribute(operatorName, "wolf", "ist schlau");
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		return new XMLProfilerPO<M>(this);
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
