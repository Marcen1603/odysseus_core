/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

import java.util.Iterator;

import org.apache.commons.math3.stat.regression.SimpleRegression;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * 
 * @author Dennis Geesen Created at: 29.03.2012
 */
public class RegressionPO<M extends ITimeInterval, R extends Tuple<M>> extends AbstractPipe<R, Tuple<M>> {

	private SimpleRegression regression;
	private int xAttribute;
	private int yAttribute;
	private DefaultTISweepArea<R> sweepArea = new DefaultTISweepArea<R>();
//	private double lastSlope = Double.NaN;
//	private double lastIntercept = Double.NaN;

	public RegressionPO() {
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
	protected synchronized void process_next(R object, int port) {
		// TODO: DO NOT SYNCHRONIZE ON THIS!
		double x = 0;
		if (object.getAttribute(xAttribute) instanceof Number) {
			x = ((Number) object.getAttribute(xAttribute)).doubleValue();
		}
		double y = 0;
		if (object.getAttribute(yAttribute) instanceof Number) {
			y = ((Number) object.getAttribute(yAttribute)).doubleValue();
		}

		sweepArea.insert(object);

		Iterator<R> iter = sweepArea.extractElementsBefore(object.getMetadata().getStart());
		while (iter.hasNext()) {
			R toRemove = iter.next();
			double cuX = ((Number) toRemove.getAttribute(xAttribute)).doubleValue();
			double cuY = ((Number) toRemove.getAttribute(yAttribute)).doubleValue();
			regression.removeData(cuX, cuY);
		}
		regression.addData(x, y);
//		System.err.println("Regression: " + regression.getN());
//		System.err.println("----------------------");
//		System.err.println(sweepArea.getSweepAreaAsString(object.getMetadata().getStart()));
//		System.err.println("----------------------");
		double slope = regression.getSlope();
		double intercept = regression.getIntercept();
		// only transfer, if one of the parameters change
		// if (lastSlope != slope || lastIntercept != intercept) {
		if (!Double.isNaN(slope) && !Double.isNaN(intercept)) {
//			lastSlope = slope;
//			lastIntercept = intercept;
			Tuple<M> ne = new Tuple<M>(2, false);
			ne.setAttribute(0, slope);
			ne.setAttribute(1, intercept);
			@SuppressWarnings("unchecked")
			M meta = (M) object.getMetadata().clone();
			meta.setStart(sweepArea.getMinTs());
			meta.setEnd(object.getMetadata().getEnd());
			ne.setMetadata(meta);

			transfer(ne);
		}
		// }
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
	

}
