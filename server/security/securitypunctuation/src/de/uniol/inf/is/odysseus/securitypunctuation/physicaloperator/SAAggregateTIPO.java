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
package de.uniol.inf.is.odysseus.securitypunctuation.physicaloperator;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;
import de.uniol.inf.is.odysseus.core.server.collection.PairMap;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunction;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.basefunctions.IPartialAggregate;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.securitypunctuation.helper.StandardSecurityEvaluator;
import de.uniol.inf.is.odysseus.server.intervalapproach.AggregateTIPO;

public class SAAggregateTIPO<Q extends ITimeInterval, R extends IStreamObject<Q>, W extends IStreamObject<Q>>
		extends AggregateTIPO<Q, R, W> {

	private static Logger LOG = LoggerFactory.getLogger(SAAggregateTIPO.class);

	@SuppressWarnings("unchecked")
	private StandardSecurityEvaluator<R> evaluator = new StandardSecurityEvaluator<R>(
			(AbstractPipe<R, R>) this);

	public SAAggregateTIPO(SDFSchema inputSchema, SDFSchema outputSchemaIntern,
			List<SDFAttribute> groupingAttributes,
			Map<SDFSchema, Map<AggregateFunction, SDFAttribute>> aggregations, boolean fastGrouping) {
		super(inputSchema, outputSchemaIntern, groupingAttributes, aggregations, fastGrouping);
	}

	@Override
	protected synchronized List<PairMap<SDFSchema, AggregateFunction, W, Q>> updateSA(
			DefaultTISweepArea<PairMap<SDFSchema, AggregateFunction, IPartialAggregate<R>, Q>> sa,
			R elemToAdd, boolean outputPA) {
		if (evaluator.evaluate(elemToAdd, this.getOwner(),
				this.getOutputSchema())) {
			LOG.debug("evaluated");
			return super.updateSA(sa, elemToAdd, outputPA);
		} else {
			LOG.debug("not evaluated");
			return null;
		}
	}

	@Override
	public synchronized void processPunctuation(IPunctuation punctuation,
			int port) {
		if (punctuation instanceof ISecurityPunctuation) {
			processSecurityPunctuation((ISecurityPunctuation) punctuation, port);
		}
		super.processPunctuation(punctuation, port);
	}

	private void processSecurityPunctuation(ISecurityPunctuation sp, int port) {
		evaluator.addToCache(sp);
		// ???
		this.sendPunctuation(sp);
	}

	@Override
	public String getName() {
		return "SAAggregate";
	}
}
