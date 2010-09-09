/*
 * Copyright 2008 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.drools.reteoo;

import org.drools.common.InternalWorkingMemory;
import org.drools.common.RuleBasePartitionId;
import org.drools.spi.PropagationContext;

/**
 * @author: <a href="mailto:tirelli@post.com">Edson Tirelli</a>
 */
public class AsyncSingleLeftTupleSinkAdapter extends SingleLeftTupleSinkAdapter {

    public AsyncSingleLeftTupleSinkAdapter() {
    }

    public AsyncSingleLeftTupleSinkAdapter( RuleBasePartitionId partitionId, LeftTupleSink tupleSink ) {
        super( partitionId, tupleSink );
    }


    @Override
	protected void doPropagateAssertLeftTuple( PropagationContext context, InternalWorkingMemory workingMemory,
                                               LeftTuple leftTuple ) {
        PartitionTaskManager manager = workingMemory.getPartitionManager( this.partitionId );
        manager.enqueue( new PartitionTaskManager.LeftTupleAssertAction( leftTuple, context, this.sink ) );
    }

    @Override
	protected void doPropagateRetractLeftTuple( PropagationContext context, InternalWorkingMemory workingMemory,
                                                LeftTuple leftTuple, LeftTupleSink tupleSink ) {
        PartitionTaskManager manager = workingMemory.getPartitionManager( this.partitionId );
        manager.enqueue( new PartitionTaskManager.LeftTupleRetractAction( leftTuple, context, tupleSink ) );
    }
}
