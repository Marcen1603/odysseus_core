/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.physicaloperator.relational;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.IHeartbeatGenerationStrategy;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.NoHeartbeatGenerationStrategy;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class RelationalSelectPO extends AbstractPipe<Tuple<IMetaAttribute>, Tuple<IMetaAttribute>>{

	final private RelationalExpression<IMetaAttribute> predicate;
    private int heartbeatRate;
    private IHeartbeatGenerationStrategy<Tuple<IMetaAttribute>> heartbeatGenerationStrategy = new NoHeartbeatGenerationStrategy<>();

    public RelationalSelectPO(RelationalExpression<IMetaAttribute> predicate) {
    	this.predicate = predicate;
    }

    /**
     * @return the heartbeat rate
     */
    public int getHeartbeatRate() {
        return this.heartbeatRate;
    }

    /**
     * Set the heartbeat rate
     * 
     * @param heartbeatRate
     */
    public void setHeartbeatRate(int heartbeatRate) {
        this.heartbeatRate = heartbeatRate;
    }

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}
	
	@Override public boolean deliversStoredElement(int outputPort) { 
		return false; 
	}

	@Override
	protected void process_next(Tuple<IMetaAttribute> object, int port) {
		try {
			Boolean expr = (Boolean) predicate.evaluate(object, getSessions(), null);
			if (expr) {
				transfer(object);
			} else {
				// Send filtered data to output port 1
				// Removed sending negated elements to port 1 --> use Route
				// instead (Selectivity measurement will always be one in this
				// case)
				// transfer(object,1);
				heartbeatGenerationStrategy.generateHeartbeat(object, this);
			}
		} catch (Exception e) {
			infoService.warning("Cannot evaluate "+predicate+" predicate with input "+object,e);
		}
	}
	
	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		sendPunctuation(punctuation);
	}
	@Override
	public String toString() {
		return super.toString() + " predicate: "
				+ this.predicate.toString();
	}

	public IHeartbeatGenerationStrategy<Tuple<IMetaAttribute>> getHeartbeatGenerationStrategy() {
		return heartbeatGenerationStrategy;
	}

	public void setHeartbeatGenerationStrategy(
			IHeartbeatGenerationStrategy<Tuple<IMetaAttribute>> heartbeatGenerationStrategy) {
		this.heartbeatGenerationStrategy = heartbeatGenerationStrategy;
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof RelationalSelectPO)) {
			return false;
		}
		RelationalSelectPO spo = (RelationalSelectPO) ipo;
		// Predicates match
		if (this.predicate.equals(spo.predicate)) {
			return true;
		}

		return false;
	}


}
