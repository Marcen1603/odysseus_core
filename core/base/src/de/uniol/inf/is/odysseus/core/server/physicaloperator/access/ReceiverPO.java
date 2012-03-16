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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource;

public class ReceiverPO<R,W> extends AbstractSource<W> implements IAccessConnectionListener<R>, ITransferHandler {

	volatile protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(ReceiverPO.class);
		}
		return _logger;
	}

	protected IObjectHandler<W> objectHandler;
	private boolean opened;
	
	final IAccessConnectionHandler<R> accessHandler;
	final private IInputDataHandler<R,W> inputDataHandler;


	public ReceiverPO(IObjectHandler<W> objectHandler, IInputDataHandler<R,W> inputDataHandler, IAccessConnectionHandler<R> accessHandler) {
		super();
		this.objectHandler = objectHandler;
		this.inputDataHandler = inputDataHandler;
		this.accessHandler = accessHandler;
		setName("ReceiverPO " + accessHandler);
		this.opened = false;
	}

	@SuppressWarnings("unchecked")
	public ReceiverPO(ReceiverPO<R,W> other) {
		super();
		objectHandler = (IObjectHandler<W>) other.objectHandler.clone();
		inputDataHandler = other.inputDataHandler.clone();
		accessHandler = (IAccessConnectionHandler<R>) other.clone();
		
		opened = other.opened;
	}

	@Override
	public boolean isOpened() {
		return super.isOpen();
	}

	@Override
	public synchronized void process_open() throws OpenFailedException {
		getLogger().debug("Process_open");
		if (!opened) {
			try {
				objectHandler.clear();
				inputDataHandler.init();
				accessHandler.open(this);
				opened = true;
			} catch (Exception e) {
				throw new OpenFailedException(e);
			}
		}
	}

	@Override
	public void process_close() {
		getLogger().debug("Process_close");
		if (opened) {
			try {
				opened = false; // Do not read any data anymore
				accessHandler.close(this);
				inputDataHandler.done();
				objectHandler.clear();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
	@Override
	public void done() {
		propagateDone();
	}

  

	@Override
	public synchronized void transfer()  {

		W toTrans = null;
		try {
			toTrans = objectHandler.create();
		} catch (Exception e) {
			getLogger().error(e.getMessage() + ". Terminating Processing ...");
			e.printStackTrace();
			process_done();
			process_close();
			return;
		}
		// logger.debug("Transfer "+toTrans);
		transfer(toTrans);
	}
	
	@Override
	public void process(R buffer){
		inputDataHandler.process(buffer, objectHandler, accessHandler, this);
	}

	@Override
	public String getSourceName() {
		return toString();
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof ReceiverPO)) {
			return false;
		}
		@SuppressWarnings("rawtypes")
		ReceiverPO bbrpo = (ReceiverPO) ipo;
		if (this.objectHandler.equals(bbrpo.objectHandler) && this.accessHandler.equals(bbrpo.accessHandler)) {
			return true;
		}
		return false;
	}

	@Override
	public AbstractSource<W> clone() {
		return new ReceiverPO<R,W>(this);
	}


}
