package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized;

import java.util.Collection;

import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;
import net.jxta.id.ID;
import net.jxta.pipe.InputPipe;
import net.jxta.pipe.OutputPipe;
import net.jxta.pipe.PipeMsgEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.p2p_new.distribute.queryPart.QueryPartController;

public class PhysicalQueryPartController extends QueryPartController {
	private static final Logger LOG = LoggerFactory.getLogger(PhysicalQueryPartController.class);
	private CentralizedDistributor centralizedDistributor;

	
	/**
	 * in addition to the actions of the QueryPartController, the removal of a query on a peer must lead to an update of the
	 * operators-map.
	 */
	
	@Override
	// called by executor
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		if (inEvent) {
			return; // avoid stack overflow
		}

		try {
			inEvent = true;

			if (PlanModificationEventType.QUERY_REMOVE.equals(eventArgs.getEventType())) { 
				final IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();
	
				final int queryID = query.getID();
				final ID sharedQueryID = sharedQueryIDMap.get(queryID);
				if (sharedQueryID == null) {
					return; // query was not shared
				}
	
				final Collection<Integer> ids = determineLocalIDs(sharedQueryIDMap, sharedQueryID);
				final OutputPipe outputPipe = outputPipeMap.get(sharedQueryID);
				if (outputPipe != null) {
					LOG.debug("Got REMOVE-event for queryid={}", queryID);
					LOG.debug("Shared query id is {}", sharedQueryID);

					sendMessage(outputPipe, sharedQueryID, REMOVE_MSG_TYPE);

					outputPipe.close();
					outputPipeMap.remove(sharedQueryID);

					// queryID is already removed here, thats the reason that
					// it is the exception here
					tryRemoveQueries(executor, ids, queryID);
					for (final Integer id : ids) {
						if (id != queryID) {
							sharedQueryIDMap.remove(id);
						}
					}

				}
				// get the centralized Distributor to update the map accordingly
				this.getCentralizedDistributor().removeQuery(sharedQueryID);
			}
		} finally {
			inEvent = false;
		}
	}
	
	@Override
	public void pipeMsgEvent(PipeMsgEvent event) {
		final Message msg = event.getMessage();
		final MessageElement typeElement = msg.getMessageElement(TYPE_TAG);
		final MessageElement sharedQueryIDElement = msg.getMessageElement(SHARED_QUERY_ID_TAG);

		final String type = new String(typeElement.getBytes(false));
		final String sharedQueryIDString = new String(sharedQueryIDElement.getBytes(false));
		final ID sharedQueryID = convertToID(sharedQueryIDString);

		final Collection<Integer> ids = determineLocalIDs(sharedQueryIDMap, sharedQueryID);

		if (!ids.isEmpty()) {
			LOG.debug("Got message for shared query id {}", sharedQueryID);
			LOG.debug("Local queries are {}", ids);

			switch (type) {
			case REMOVE_MSG_TYPE:
				LOG.debug("Remove queries {}", ids);

				tryRemoveQueries(executor, ids, null);
				for (final Integer id : ids) {
					sharedQueryIDMap.remove(id);
				}
				final InputPipe inputPipe = inputPipeMap.remove(sharedQueryID);
				inputPipe.close();
				// get the centralized Distributor to update its map accordingly
				this.getCentralizedDistributor().removeQuery(sharedQueryID);
				
				break;
			default:
				LOG.error("Unknown message type {}", type);
				break;
			}
		}
	}

	public CentralizedDistributor getCentralizedDistributor() {
		if(centralizedDistributor == null) {
			this.setCentralizedDistributor(CentralizedDistributor.getInstance());
		}
		return centralizedDistributor;
	}

	public void setCentralizedDistributor(
			CentralizedDistributor centralizedDistributor) {
		this.centralizedDistributor = centralizedDistributor;
	}
}
