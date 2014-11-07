package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.util.Collection;

import net.jxta.peer.PeerID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.peer.recovery.IP2PNetworkController;

/**
 * The P2P network controller provides services to detect if any peer enters or
 * leaves the network.
 * 
 * @author Simon Kuespert & Michael Brand
 * 
 */
public class P2PNetworkController implements IP2PNetworkController {

	/**
	 * The logger instance for this class.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(P2PNetworkController.class);

	/**
	 * The bound listeners for P2P network controller events.
	 */
	private static Collection<IP2PNetworkControllerListener> cListeners = Lists
			.newArrayList();

	/**
	 * Binds a listener. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The listener to bind. <br />
	 *            Must be not null.
	 */
	public void bindListener(IP2PNetworkControllerListener serv) {

		Preconditions.checkNotNull(serv);
		cListeners.add(serv);
		LOG.debug("Bound {} as a P2P network controller listener.", serv
				.getClass().getSimpleName());

	}

	/**
	 * Unbinds a listener, if it's a bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The listener to unbind. <br />
	 *            Must be not null.
	 */
	public static void unbindListener(IP2PNetworkControllerListener serv) {

		Preconditions.checkNotNull(serv);

		if (cListeners.remove(serv)) {

			LOG.debug("Unbound {} as a P2P network controller listener.", serv
					.getClass().getSimpleName());

		}

	}

	/**
	 * The P2P network manager, if there is one bound.
	 */
	private static Optional<IP2PNetworkManager> cP2PNetworkManager = Optional
			.absent();

	/**
	 * Binds a P2P network manager. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The P2P network manager to bind. <br />
	 *            Must be not null.
	 */
	public void bindP2PNetworkManager(IP2PNetworkManager serv) {

		Preconditions.checkNotNull(serv);
		cP2PNetworkManager = Optional.of(serv);
		LOG.debug("Bound {} as a P2P network manager.", serv.getClass()
				.getSimpleName());

		if (!mNetworkObserver.isPresent() && cPeerDictionary.isPresent()) {

			this.start();

		}

	}

	/**
	 * Unbinds a P2P network manager, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The P2P network manager to unbind. <br />
	 *            Must be not null.
	 */
	public void unbindP2PNetworkManager(IP2PNetworkManager serv) {

		Preconditions.checkNotNull(serv);

		if (cP2PNetworkManager.isPresent() && cP2PNetworkManager.get() == serv) {

			this.stop();
			cP2PNetworkManager = Optional.absent();
			LOG.debug("Unbound {} as a P2P network manager.", serv.getClass()
					.getSimpleName());

		}

	}

	/**
	 * The Peer dictionary, if there is one bound.
	 */
	private static Optional<IPeerDictionary> cPeerDictionary = Optional.absent();

	/**
	 * Binds a Peer dictionary. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The Peer dictionary to bind. <br />
	 *            Must be not null.
	 */
	public void bindPeerDictionary(IPeerDictionary serv) {

		Preconditions.checkNotNull(serv);
		cPeerDictionary = Optional.of(serv);
		LOG.debug("Bound {} as a Peer dictionary.", serv.getClass()
				.getSimpleName());

		if (!mNetworkObserver.isPresent() && cP2PNetworkManager.isPresent()) {

			this.start();

		}

	}

	/**
	 * Unbinds a Peer dictionary, if it's the bound one. <br />
	 * Called by OSGi-DS.
	 * 
	 * @param serv
	 *            The Peer dictionary to unbind. <br />
	 *            Must be not null.
	 */
	public void unbindPeerDictionary(IPeerDictionary serv) {

		Preconditions.checkNotNull(serv);

		if (cPeerDictionary.isPresent() && cPeerDictionary.get() == serv) {

			this.stop();
			cPeerDictionary = Optional.absent();
			LOG.debug("Unbound {} as a P2P dictionary.", serv.getClass()
					.getSimpleName());

		}

	}

	/**
	 * The time [ms] between two network look ups for failed or new peers.
	 */
	private final static int WAIT_TIME_MS = 1000;

	/**
	 * The P2P network controller provides services to detect if any peer enters
	 * or leaves the network.
	 * 
	 * @author Simon Kuespert & Michael Brand
	 * 
	 */
	private class P2PNetworkControllerThread extends Thread {

		/**
		 * True, if the thread is currently running.
		 */
		private volatile boolean mActive = false;

		@Override
		public void interrupt() {

			this.mActive = false;
			super.interrupt();

		}

		/**
		 * Sends a notification indicating that a peer left the network.
		 * 
		 * @param pid
		 *            The ID of the left peer.
		 */
		private void handlePeerLost(PeerID pid) {

			Preconditions.checkNotNull(pid);

			for (IP2PNetworkControllerListener listener : cListeners) {

				listener.onPeerLeaved(pid);

			}

		}

		/**
		 * Sends a notification indicating that a peer joined the network.
		 * 
		 * @param pid
		 *            The ID of the entered peer.
		 */
		private void handleNewPeer(PeerID pid) {

			Preconditions.checkNotNull(pid);

			for (IP2PNetworkControllerListener listener : cListeners) {

				listener.onPeerEntered(pid);

			}

		}

		@Override
		public void run() {

			// Preconditions
			if (!cPeerDictionary.isPresent()) {

				LOG.error("No P2P dictionary bound!");
				return;

			}

			this.mActive = true;
			LOG.debug("P2PNetworkObserver started");

			while (this.mActive) {

				if (!cPeerDictionary.isPresent()) {

					this.mActive = false;
					break;

				}
				Collection<PeerID> peerIDs = cPeerDictionary.get()
						.getRemotePeerIDs();

				if (P2PNetworkController.this.mKnownPeerIDs == null) {

					P2PNetworkController.this.mKnownPeerIDs = peerIDs;

				} else {

					// Check, if we lost a peer
					for (PeerID pid : P2PNetworkController.this.mKnownPeerIDs) {

						if (!peerIDs.contains(pid)) {

							if (!cPeerDictionary.isPresent()) {

								this.mActive = false;
								break;

							}
							LOG.debug("Lost peer: {}", cPeerDictionary.get()
									.getRemotePeerName(pid));
							this.handlePeerLost(pid);

						}
					}

					// Check, if there is a new peer
					for (PeerID pid : peerIDs) {

						if (!P2PNetworkController.this.mKnownPeerIDs
								.contains(pid)) {

							if (!cPeerDictionary.isPresent()) {

								this.mActive = false;
								break;

							}
							LOG.debug("Found new peer: {}", cPeerDictionary
									.get().getRemotePeerName(pid));
							this.handleNewPeer(pid);

						}
					}

					P2PNetworkController.this.mKnownPeerIDs = peerIDs;

				}

				synchronized (this) {

					try {

						this.wait(WAIT_TIME_MS);

					} catch (InterruptedException e) {

						this.mActive = false;

					}

				}

			}

		}

	}

	/**
	 * All currently known peers.
	 */
	private Collection<PeerID> mKnownPeerIDs;

	/**
	 * P2P network controller thread, if there is one at the moment.
	 */
	private Optional<P2PNetworkControllerThread> mNetworkObserver = Optional
			.absent();

	@Override
	public void start() {

		// Preconditions
		if (!cP2PNetworkManager.isPresent()) {

			LOG.error("No P2P network manager bound!");
			return;

		}

		if (this.mNetworkObserver.isPresent()) {

			// Stop old thread if it is already running
			this.mNetworkObserver.get().interrupt();
			this.mNetworkObserver = Optional.absent();
			LOG.debug("Stopped already running peer failure detection.");

		}

		final P2PNetworkControllerThread networkObserver = new P2PNetworkControllerThread();
		Thread waitForNetwork = new Thread("Wait for network") {

			@Override
			public void run() {

				while (!cP2PNetworkManager.get().isStarted()) {

					synchronized (this) {

						try {

							this.wait(WAIT_TIME_MS);

						} catch (InterruptedException e) {

							e.printStackTrace();
							break;

						}

					}

				}

				networkObserver.setName("PeerFailureDetection_"
						+ cP2PNetworkManager.get().getLocalPeerName() + "_"
						+ cP2PNetworkManager.get().getLocalPeerID().toString());
				networkObserver.setDaemon(true);
				networkObserver.start();
				P2PNetworkController.this.mNetworkObserver = Optional
						.of(networkObserver);

			}

		};
		waitForNetwork.start();

	}

	@Override
	public void stop() {

		if (this.mNetworkObserver.isPresent()) {

			mNetworkObserver.get().interrupt();
			LOG.debug("Peer failure detection stopped");

		}

	}

}