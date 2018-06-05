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
package de.uniol.inf.is.odysseus.nexmark.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.nexmark.simulation.ITupleContainerListener;

/**
 * The NEXMarkGenerator creates a simulation of the streams person, auction
 * and bid. It sends a stream {@link TupleContainer} that contains the tuple and 
 * the type of the tuple.
 * 
 * @author Bernd Hochschulz
 * 
 */
public class NEXMarkGenerator extends Thread {
	private NEXMarkGeneratorConfiguration configuration;

	private TupleStreamGenerator generator;

	private SimpleCalendar calender;

	private Tuple<ITimeInterval> personTuple;
	private Tuple<ITimeInterval> auctionTuple;
	private Tuple<ITimeInterval> bidTuple;
	private Tuple<ITimeInterval> currentTuple;

	private boolean burst = false;

	private NEXMarkBurstGenerator burstGenerator;

	private List<ITupleContainerListener> listener = new ArrayList<>();

	/**
	 * Creates a {@link NEXMarkGenerator}. 
	 * Tuples from the stream can be read using getInputStream.
	 * The generator creates tuples according to the parameter configuration.
	 * 
	 * @param configuration
	 *            - {@link NEXMarkGeneratorConfiguration} that specifies the tuples to be created
	 * @throws IOException
	 *             if the streams that are used to send the tuples cannot be created
	 *             
	 */
	public NEXMarkGenerator(NEXMarkGeneratorConfiguration configuration,
			boolean deterministic, ITupleContainerListener listener) throws IOException {
		this.configuration = configuration;
		calender = new SimpleCalendar();
		generator = new TupleStreamGenerator(configuration, calender,
				deterministic);

		this.listener.add(listener);

		burstGenerator = new NEXMarkBurstGenerator(configuration.burstConfig,
				this, deterministic);
	}

	public void addListner(ITupleContainerListener listener){
		this.listener.add(listener);
	}

	public void removeListner(ITupleContainerListener listener){
		this.listener.remove(listener);
	}

	
	/**
	 * determines the next tuple to be sent by using the smallest timestamp 
	 */
	private void setCurrentTuple() {
		currentTuple = personTuple;

		if ((Long) auctionTuple.getAttribute(0) < (Long) currentTuple
				.getAttribute(0)) {
			currentTuple = auctionTuple;
		}

		// ignore if no bid could be created
		if ((Long) bidTuple.getAttribute(0) < (Long) currentTuple
				.getAttribute(0)) {
			currentTuple = bidTuple;
		}

	}

	/**
	 * Returns the time to wait until the next tuple to be sent
	 * 
	 * @return time to wait
	 */
	private long getTimeToWait() {
		long timeToWait = ((Long) currentTuple.getAttribute(0) / configuration.accelerationFactor)
				- calender.getTimeInMS();
		return timeToWait;
	}

	/**
	 * Wraps the tuple to be sent into a {@link TupleContainer}.
	 * 
	 * @return TupleContainer with tuple and type
	 * @throws CouldNotCreateAuction
	 *             no auction could be created (no person present)
	 * @throws CouldNotCreateBid
	 *             no bid could be created (no person or auction present)
	 */
	private synchronized TupleContainer createTupleContainer()
			throws CouldNotCreateAuction, CouldNotCreateBid {
		TupleContainer container = null;

		// System.out.println(" "+currentTuple+" "+personTuple+" "+auctionTuple+
		// " "+bidTuple);
		synchronized (generator) {

			// fill tuple of the type to be sent
			if (currentTuple == personTuple) {
				generator.fillPersonTuple(personTuple);
				container = new TupleContainer(personTuple,
						NEXMarkStreamType.PERSON);
				personTuple = generator.generateRawPersonTuple();

			} else if (currentTuple == auctionTuple) {
				// check if person is registered yet
				try {
					generator.fillAuctionTuple(auctionTuple);
				} catch (CouldNotGetExistingPersonId e) {
					throw new CouldNotCreateAuction();
				}
				container = new TupleContainer(auctionTuple,
						NEXMarkStreamType.AUCTION);
				auctionTuple = generator.generateRawAuctionTuple();

			} else if (currentTuple == bidTuple) {
				// check if bid can be created (only of auction is open yet)
				try {
					generator.fillBidTuple(bidTuple);
				} catch (CouldNotGetExistingAuctionIdException e) {
					throw new CouldNotCreateBid();
				} catch (CouldNotGetExistingPersonId e) {
					throw new CouldNotCreateBid();
				}
				container = new TupleContainer(bidTuple, NEXMarkStreamType.BID);
				bidTuple = generator.generateRawBidTuple();
			} else {
				// should not happen
				// TODO exception?
			}
		}
		return container;
	}

	/**
	 * Generates person, auction and bid tuples and sends them according to the timestamps over the stream
	 */
	@Override
	public void run() {
		calender.reset();

		if (burstGenerator.shouldGenerateBursts()) {
			burstGenerator.start();
		} else {
			burstGenerator = null;
		}

		// generate first tuples
		synchronized (generator) {
			// == removed ==
			// Make sure that there is at least one person
			// personTuple = generator.generateFirstRawPersonTuple();
			// ==================
			personTuple = generator.generateRawPersonTuple();
			auctionTuple = generator.generateRawAuctionTuple();
			bidTuple = generator.generateRawBidTuple();
		}

		setCurrentTuple();

		// generate tuples until the generator is interrupted
		try {
			while (!interrupted()) { // && counter < limit
				// determine time to wait
				long timeToWait = 0;
				synchronized (this) {
					timeToWait = getTimeToWait();
				}

				// wait according to time to wait
				if (timeToWait > 0) {
					long curTime = System.currentTimeMillis();
					long expectedTme = curTime + timeToWait;
					while (curTime < expectedTme) {
						try {
							long millis = expectedTme - curTime;
							Thread.sleep(millis);
							curTime = System.currentTimeMillis();
							// Thread.sleep(timeToWait);
						} catch (InterruptedException e) {
							break;
						}
					}
				}

				synchronized (this) {
					// if there is a burst, ignore last tuple and determine new time to wait
					if (burst) {
						burst = false;
						// System.out.println("BURST");
						continue;
					}

					// fill tuple to be sent
					TupleContainer container = null;
					try {
						container = createTupleContainer();
					} catch (CouldNotCreateBid e) {
//						e.printStackTrace();
						// if no bid could be created, try again next time
						synchronized (generator) {
							bidTuple = generator.generateRawBidTuple();
						}

						setCurrentTuple();
						continue;
					} catch (CouldNotCreateAuction e) {
//						e.printStackTrace();
						// if no auction could be created, try again next time
						synchronized (generator) {
							auctionTuple = generator.generateRawAuctionTuple();
						}

						setCurrentTuple();
						continue;
					}

					// send tuple
					fireNewObject(container);

					// determine next tuple
					setCurrentTuple();
				}
			}
		} finally {
			if (burstGenerator != null) {
				burstGenerator.interrupt();
			}
		}
	}
	
	private void fireNewObject(TupleContainer container){
		Iterator<ITupleContainerListener> iter = this.listener.iterator();
		while(iter.hasNext()){
			ITupleContainerListener l = iter.next();
			try {
				l.newObject(container);
			} catch (IOException e) {
				try{
					iter.remove();
				}catch(Exception e2){
					e2.printStackTrace();
				}
			}
		}
	}

//	public ObjectInputStream getInputStream() {
//		return inputStream;
//	}

	/**
	 * Call to tell the generator that a burst happens
	 * Is called from {@link NEXMarkBurstGenerator}
	 * 
	 * @param time
	 *            - time when burst happens
	 */
	public void startBurst(long time) {
		// To send new tuples in the burst immediately, create new tuples
		synchronized (this) {
			synchronized (generator) {
				generator.startBurst(time);
				personTuple = generator.generateRawPersonTuple();
				auctionTuple = generator.generateRawAuctionTuple();
				bidTuple = generator.generateRawBidTuple();
			}

			setCurrentTuple();
			// if generator waits to send new tuples, wake it up
			if (this.getState() == Thread.State.TIMED_WAITING) {
				burst = true;
				this.notify();
			}
		}
	}

	/**
	 * Call to tell generator to stop a burst.
	 * Called from {@link NEXMarkBurstGenerator}.
	 * 
	 */
	public void stopBurst() {
		synchronized (generator) {
			generator.stopBurst();
		}

		// synchronized (this) {
		// synchronized (generator) {
		// generator.stopBurst();
		// personTuple = generator.generateRawPersonTuple();
		// auctionTuple = generator.generateRawAuctionTuple();
		// bidTuple = generator.generateRawBidTuple();
		// }
		// getCurrentTuple();
		// if (this.getState() == Thread.State.TIMED_WAITING) {
		// this.notify();
		// }
		// }
	}
}

class CouldNotCreateAuction extends Exception {
	private static final long serialVersionUID = 7205123255194069115L;
}

class CouldNotCreateBid extends Exception {
	private static final long serialVersionUID = -2241555793889688555L;
}
