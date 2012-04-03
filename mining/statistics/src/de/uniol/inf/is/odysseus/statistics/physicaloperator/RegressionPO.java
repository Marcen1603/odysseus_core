/** Copyright 2012 The Odysseus Team
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

package de.uniol.inf.is.odysseus.statistics.physicaloperator;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

/**
 * 
 * @author Dennis Geesen Created at: 29.03.2012
 */
public class RegressionPO<M extends ITimeInterval, R extends Tuple<M>> extends AbstractPipe<R, Tuple<M>> {

	private SimpleRegression regression;
	private int xAttribute;
	private int yAttribute;
	
	
	public RegressionPO(){
		super();
	}	

	public RegressionPO(int xAttribute, int yAttribute) {
		this.xAttribute = xAttribute;
		this.yAttribute = yAttribute;
	}

	public RegressionPO(RegressionPO<M, R> regressionPO) {
		this.xAttribute = regressionPO.xAttribute;
		this.yAttribute = regressionPO.yAttribute;
	}
	
	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		this.regression = new SimpleRegression();
	}

	@Override
	protected void process_next(R object, int port) {
		double x = object.getAttribute(xAttribute);
		double y = object.getAttribute(yAttribute);
		regression.addData(x, y);

		double slope = regression.getSlope();
		double intercept = regression.getIntercept();
		
		Tuple<M> ne = new Tuple<M>(2);
		ne.setAttribute(0, slope);
		ne.setAttribute(1, intercept);
		M meta = object.getMetadata();
		ne.setMetadata(meta);
		transfer(ne);
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		sendPunctuation(timestamp);
	}

	@Override
	public RegressionPO<M, R> clone() {
		return new RegressionPO<M, R>(this);
	}

}
