package de.uniol.inf.is.odysseus.net.data.impl.server;

import java.util.Collection;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.OdysseusNodeID;
import de.uniol.inf.is.odysseus.net.communication.IMessage;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicator;
import de.uniol.inf.is.odysseus.net.communication.IOdysseusNodeCommunicatorListener;
import de.uniol.inf.is.odysseus.net.communication.OdysseusNodeCommunicationException;
import de.uniol.inf.is.odysseus.net.data.DistributedDataException;
import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataManager;
import de.uniol.inf.is.odysseus.net.data.impl.IDistributedDataCreator;
import de.uniol.inf.is.odysseus.net.data.impl.message.BooleanMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.ContainsNameMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.ContainsOdysseusNodeIDMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.ContainsUUIDMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.CreateDistributedDataMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DestroyDistributedDataWithNameMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DestroyDistributedDataWithNodeIDMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DestroyDistributedDataWithUUIDMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DistributedDataCollectionMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DistributedDataCreatedMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DistributedDataDestroyedMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.DistributedDataUpdatedMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.GetNameMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.GetOdysseusNodeIDMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.GetUUIDMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.MultipleDistributedDataDestroyedMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.NamesMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.OdysseusNodeIDsMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.OptionalDistributedDataMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.RequestNamesMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.RequestOdysseusNodeIDsMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.RequestUUIDsMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.UUIDsMessage;
import de.uniol.inf.is.odysseus.net.data.impl.message.UpdateDistributedDataMessage;

public class DistributedDataServer implements IOdysseusNodeCommunicatorListener {

	private static final Logger LOG = LoggerFactory.getLogger(DistributedDataServer.class);

	private final IDistributedDataCreator creator;
	private final IOdysseusNodeCommunicator communicator;
	private final IDistributedDataManager manager;

	public DistributedDataServer(IOdysseusNodeCommunicator communicator, IDistributedDataCreator creator, IDistributedDataManager manager) {
		Preconditions.checkNotNull(communicator, "communicator must not be null!");
		Preconditions.checkNotNull(creator, "creator must not be null!");
		Preconditions.checkNotNull(manager, "manager must not be null!");

		this.manager = manager;
		this.creator = creator;
		this.communicator = communicator;

		communicator.addListener(this, CreateDistributedDataMessage.class);
		communicator.addListener(this, UpdateDistributedDataMessage.class);
		communicator.addListener(this, DestroyDistributedDataWithUUIDMessage.class);
		communicator.addListener(this, DestroyDistributedDataWithNameMessage.class);
		communicator.addListener(this, DestroyDistributedDataWithNodeIDMessage.class);
		communicator.addListener(this, RequestUUIDsMessage.class);
		communicator.addListener(this, RequestNamesMessage.class);
		communicator.addListener(this, RequestOdysseusNodeIDsMessage.class);
		communicator.addListener(this, GetUUIDMessage.class);
		communicator.addListener(this, GetNameMessage.class);
		communicator.addListener(this, GetOdysseusNodeIDMessage.class);
		communicator.addListener(this, ContainsUUIDMessage.class);
		communicator.addListener(this, ContainsNameMessage.class);
		communicator.addListener(this, ContainsOdysseusNodeIDMessage.class);
		
		LOG.info("Created distributed data server");
	}

	public void stop() {
		communicator.removeListener(this, CreateDistributedDataMessage.class);
		communicator.removeListener(this, UpdateDistributedDataMessage.class);
		communicator.removeListener(this, DestroyDistributedDataWithUUIDMessage.class);
		communicator.removeListener(this, DestroyDistributedDataWithNameMessage.class);
		communicator.removeListener(this, DestroyDistributedDataWithNodeIDMessage.class);
		communicator.removeListener(this, RequestUUIDsMessage.class);
		communicator.removeListener(this, RequestNamesMessage.class);
		communicator.removeListener(this, RequestOdysseusNodeIDsMessage.class);
		communicator.removeListener(this, GetUUIDMessage.class);
		communicator.removeListener(this, GetNameMessage.class);
		communicator.removeListener(this, GetOdysseusNodeIDMessage.class);
		communicator.removeListener(this, ContainsUUIDMessage.class);
		communicator.removeListener(this, ContainsNameMessage.class);
		communicator.removeListener(this, ContainsOdysseusNodeIDMessage.class);
		
		LOG.info("Stopped distributed data server");
	}

	@Override
	public void receivedMessage(IOdysseusNodeCommunicator communicator, IOdysseusNode senderNode, IMessage message) {
		LOG.info("Received message of type {}", message.getClass());
		
		if (message instanceof CreateDistributedDataMessage) {
			CreateDistributedDataMessage msg = (CreateDistributedDataMessage) message;

			try {
				IDistributedData createdData = creator.create(senderNode.getID(), msg.getData(), msg.getName(), msg.isPersistent(), msg.getLifetime());

				DistributedDataCreatedMessage answer = new DistributedDataCreatedMessage(createdData);
				communicator.send(senderNode, answer);

			} catch (DistributedDataException e) {
				// TODO: Handle it!

			} catch (OdysseusNodeCommunicationException e) {
				LOG.error("Could not send answer of create distributed data message", e);
			}
		} else if (message instanceof UpdateDistributedDataMessage) {
			UpdateDistributedDataMessage msg = (UpdateDistributedDataMessage) message;

			try {
				Optional<IDistributedData> updatedData = creator.update(senderNode.getID(), msg.getUUID(), msg.getData());
				if(updatedData.isPresent()) {
					DistributedDataUpdatedMessage answer = new DistributedDataUpdatedMessage(updatedData.get());
					communicator.send(senderNode, answer);
				} else {
					// TODO: Handle it!
				}
			} catch (DistributedDataException e) {
				// TODO: Handle it!
			} catch (OdysseusNodeCommunicationException e) {
				LOG.error("Could not send answer of create distributed data message", e);
			}
		} else if (message instanceof DestroyDistributedDataWithUUIDMessage) {
			DestroyDistributedDataWithUUIDMessage msg = (DestroyDistributedDataWithUUIDMessage) message;

			try {
				Optional<IDistributedData> optDestroyedData = creator.destroy(senderNode.getID(), msg.getUUID());
				if (optDestroyedData.isPresent()) {
					IDistributedData destroyedData = optDestroyedData.get();

					DistributedDataDestroyedMessage answer = new DistributedDataDestroyedMessage(destroyedData);
					communicator.send(senderNode, answer);
				} else {
					// TODO: Handle it!
				}

			} catch (DistributedDataException e) {
				// TODO: Handle it!

			} catch (OdysseusNodeCommunicationException e) {
				LOG.error("Could not send answer of destroy distributed data message", e);
			}
		} else if (message instanceof DestroyDistributedDataWithNameMessage) {
			DestroyDistributedDataWithNameMessage msg = (DestroyDistributedDataWithNameMessage) message;

			try {
				Collection<IDistributedData> destroyedData = creator.destroy(senderNode.getID(), msg.getName());

				MultipleDistributedDataDestroyedMessage answer = new MultipleDistributedDataDestroyedMessage(destroyedData);
				communicator.send(senderNode, answer);
			} catch (DistributedDataException e) {
				// TODO: Handle it!

			} catch (OdysseusNodeCommunicationException e) {
				LOG.error("Could not send answer of destroy distributed data message", e);
			}
		} else if (message instanceof DestroyDistributedDataWithNodeIDMessage) {
			DestroyDistributedDataWithNodeIDMessage msg = (DestroyDistributedDataWithNodeIDMessage) message;
			try {
				Collection<IDistributedData> destroyedData = creator.destroy(senderNode.getID(), msg.getOdysseusNodeID());

				MultipleDistributedDataDestroyedMessage answer = new MultipleDistributedDataDestroyedMessage(destroyedData);
				communicator.send(senderNode, answer);
			} catch (DistributedDataException e) {
				// TODO: Handle it!

			} catch (OdysseusNodeCommunicationException e) {
				LOG.error("Could not send answer of destroy distributed data message", e);
			}
		} else if (message instanceof RequestUUIDsMessage) {
			try {
				Collection<UUID> uuids = manager.getUUIDs();
				UUIDsMessage answer = new UUIDsMessage(uuids);

				communicator.send(senderNode, answer);
			} catch (DistributedDataException e) {
				// TODO: Handle it!

			} catch (OdysseusNodeCommunicationException e) {
				LOG.error("Could not send answer of request uuids message", e);
			}
		} else if (message instanceof RequestNamesMessage) {
			try {
				Collection<String> names = manager.getNames();
				NamesMessage answer = new NamesMessage(names);

				communicator.send(senderNode, answer);
			} catch (DistributedDataException e) {
				// TODO: Handle it!

			} catch (OdysseusNodeCommunicationException e) {
				LOG.error("Could not send answer of request names message", e);
			}
		} else if (message instanceof RequestOdysseusNodeIDsMessage) {
			try {
				Collection<OdysseusNodeID> nodeIDs = manager.getOdysseusNodeIDs();

				communicator.send(senderNode, new OdysseusNodeIDsMessage(nodeIDs));
			} catch (DistributedDataException e) {
				// TODO: Handle it!

			} catch (OdysseusNodeCommunicationException e) {
				LOG.error("Could not send answer of of request odysseus node ids message", e);
			}

		} else if (message instanceof GetUUIDMessage) {
			GetUUIDMessage msg = (GetUUIDMessage) message;

			try {
				Optional<IDistributedData> optData = manager.get(msg.getUUID());
				OptionalDistributedDataMessage answer = new OptionalDistributedDataMessage(optData);

				communicator.send(senderNode, answer);
			} catch (DistributedDataException e) {
				// TODO: Handle it!

			} catch (OdysseusNodeCommunicationException e) {
				LOG.error("Could not send answer of get distributed data with uuid message", e);
			}
		} else if (message instanceof GetNameMessage) {
			GetNameMessage msg = (GetNameMessage) message;

			try {
				Collection<IDistributedData> dataCollection = manager.get(msg.getName());
				DistributedDataCollectionMessage answer = new DistributedDataCollectionMessage(dataCollection);

				communicator.send(senderNode, answer);
			} catch (DistributedDataException e) {
				// TODO: Handle it!

			} catch (OdysseusNodeCommunicationException e) {
				LOG.error("Could not send answer of get distributed data with name message", e);
			}
		} else if (message instanceof GetOdysseusNodeIDMessage) {
			GetOdysseusNodeIDMessage msg = (GetOdysseusNodeIDMessage) message;

			try {
				Collection<IDistributedData> dataCollection = manager.get(msg.getOdysseusNodeID());
				DistributedDataCollectionMessage answer = new DistributedDataCollectionMessage(dataCollection);

				communicator.send(senderNode, answer);
			} catch (DistributedDataException e) {
				// TODO: Handle it!

			} catch (OdysseusNodeCommunicationException e) {
				LOG.error("Could not send answer of get distributed data with odysseus node id message", e);
			}
		} else if (message instanceof ContainsUUIDMessage) {
			ContainsUUIDMessage msg = (ContainsUUIDMessage) message;

			try {
				boolean value = manager.containsUUID(msg.getUUID());

				communicator.send(senderNode, new BooleanMessage(value));
			} catch (DistributedDataException e) {
				// TODO: Handle it!

			} catch (OdysseusNodeCommunicationException e) {
				LOG.error("Could not send answer of containment of distributed data with uuid", e);
			}
		} else if (message instanceof ContainsNameMessage) {
			ContainsNameMessage msg = (ContainsNameMessage) message;

			try {
				boolean value = manager.containsName(msg.getName());

				communicator.send(senderNode, new BooleanMessage(value));
			} catch (DistributedDataException e) {
				// TODO: Handle it!

			} catch (OdysseusNodeCommunicationException e) {
				LOG.error("Could not send answer of containment of distributed data with name", e);
			}
		} else if (message instanceof ContainsOdysseusNodeIDMessage) {
			ContainsOdysseusNodeIDMessage msg = (ContainsOdysseusNodeIDMessage) message;

			try {
				boolean value = manager.containsOdysseusNodeID(msg.getOdysseusNodeID());

				communicator.send(senderNode, new BooleanMessage(value));
			} catch (DistributedDataException e) {
				// TODO: Handle it!

			} catch (OdysseusNodeCommunicationException e) {
				LOG.error("Could not send answer of containment of distributed data with odysseus node id", e);
			}
		}
	}
}
