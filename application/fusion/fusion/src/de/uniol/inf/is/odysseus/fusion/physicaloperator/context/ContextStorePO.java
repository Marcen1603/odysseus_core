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

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.fusion.store.context.FusionContextStore;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/*
 * 
 * 
 * 
 * 
 */
public class ContextStorePO extends AbstractPipe<Tuple<? extends IMetaAttribute>, Tuple<? extends IMetaAttribute>> {

    private final SDFSchema schema;
    
    @SuppressWarnings("unused")
	private final FusionContextStore<Tuple<IMetaAttribute>> contextStore;
    
    public ContextStorePO(final SDFSchema schema) {
        this.schema = schema;
        contextStore = new FusionContextStore<Tuple<IMetaAttribute>>(schema);
    }

    public ContextStorePO(final ContextStorePO po) {
        this.schema = po.schema;
        contextStore = new FusionContextStore<Tuple<IMetaAttribute>>(po.schema);
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
		return OutputMode.INPUT;
	}

	@Override
	protected void process_next(Tuple<? extends IMetaAttribute> object, int port) {
		//contextStore.insertValue(object); 
		transfer(object);
		process_done();
	}





}
