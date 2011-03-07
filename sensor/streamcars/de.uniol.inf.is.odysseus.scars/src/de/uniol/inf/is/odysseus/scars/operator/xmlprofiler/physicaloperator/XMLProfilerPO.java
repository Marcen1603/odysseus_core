/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.scars.operator.xmlprofiler.physicaloperator;

import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.operator.xmlprofiler.profiler.ScarsXMLProfiler;

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
		this.fileName = clone.fileName;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		ScarsXMLProfiler p = ScarsXMLProfiler.getInstance(fileName, 0, 25);
		p.profilePunctuation(operatorName, timestamp);
		this.sendPunctuation(timestamp);
	}

	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		ScarsXMLProfiler p = ScarsXMLProfiler.getInstance(fileName, 0, 25);
		p.profile(operatorName, getOutputSchema(), object);
		transfer(object);
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

}
