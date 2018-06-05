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
package de.uniol.inf.is.odysseus.rcp.viewer.server.opbreak;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.buffer.BufferPO;

public class BufferPOWithListener<T extends IStreamObject<?>> extends BufferPO<T> {

	private final List<IBufferedPipeListener> listeners = new ArrayList<IBufferedPipeListener>();
	
	@Override
	protected void process_next(T object, int port) {
		super.process_next(object, port);
		fireSizeEvent();
	}
	
	@Override
	public void transferNext() {
		super.transferNext();
		fireSizeEvent();
	}
	
	public void addListener( IBufferedPipeListener listener ) {
		if( listener == null ) return;
		synchronized( listeners ) {
			listeners.add(listener);
		}
	}
	
	public void removeListener( IBufferedPipeListener listener ) {
		synchronized( listeners ) {
			listeners.remove(listener);
		}
	}
	
	protected final void fireSizeEvent() {
		synchronized( listeners ){
			for( IBufferedPipeListener listener : listeners ) 
				listener.sizeChanged(this);
		}
	}

}
