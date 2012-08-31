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
package de.uniol.inf.is.odysseus.fusion.physicaloperator.context;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.fusion.metadata.IFusionProbability;
import de.uniol.inf.is.odysseus.fusion.store.context.FusionContextStore;

public class ContextStorePO extends AbstractPipe<Tuple<? extends IFusionProbability>, Tuple<? extends IFusionProbability>> {

    private final SDFSchema schema;
    
    
    public ContextStorePO(final SDFSchema schema) {
        this.schema = schema;
        FusionContextStore.setStoreSchema(schema);
    }

    public ContextStorePO(final ContextStorePO po) {
        this.schema = po.schema;
        FusionContextStore.setStoreSchema(po.schema);
    }
    
    
    @Override
    public ContextStorePO clone() {
        return new ContextStorePO(this);
    }

    @Override
    public void processPunctuation(final PointInTime timestamp, final int port) {

    }

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.MODIFIED_INPUT;
	}

	@Override
	protected void process_next(Tuple<? extends IFusionProbability> tuple, int port) {
		if(FusionContextStore.getStoreMap().containsKey(tuple.getAttribute(4))){
			FusionContextStore.update((Integer) tuple.getAttribute(4), tuple);
		}
		else{
			tuple.append(FusionContextStore.getNextStoreId(), true);
			FusionContextStore.insertNew(FusionContextStore.getNextStoreId(),tuple);
		}
		transfer(tuple);
		process_done();
	}
}
