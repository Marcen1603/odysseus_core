package de.uniol.inf.is.odysseus.peer.smarthome.server;

import java.util.Map.Entry;

import net.jxta.document.Advertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementDiscovererListener;
import de.uniol.inf.is.odysseus.p2p_new.IP2PNetworkManager;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.MultipleSourceAdvertisement;
import de.uniol.inf.is.odysseus.p2p_new.dictionary.SourceAdvertisement;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.Actor;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.Sensor;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.SmartDevice;

public class Logic implements
		ISmartDeviceDictionaryListener, IAdvertisementDiscovererListener {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	private static Logic instance;
	private IP2PNetworkManager p2pNetworkManager;

	Logic() {

	}

	/************************************************
	 * ISmartDeviceDictionaryListener
	 */
	@Override
	public void smartDeviceAdded(SmartDeviceServerDictionaryDiscovery sender,
			SmartDevice newSmartDevice) {
		LOG.debug("smartDeviceAdded: " + newSmartDevice.getPeerID()
				+ " processLogic(...");

		processLogic(newSmartDevice);
	}

	@Override
	public void smartDeviceRemoved(SmartDeviceServerDictionaryDiscovery sender,
			SmartDevice remoteSmartDevice) {

		LOG.debug("SmartHomeServerPlugIn smartDeviceRemoved: "
				+ SmartDeviceServer.getInstance().getLocalSmartDevice()
						.getPeerID());

		if (QueryExecutor.getInstance().isRunningLogicRule(remoteSmartDevice)) {
			QueryExecutor.getInstance().removeAllLogicRules(remoteSmartDevice);
		}
	}

	@Override
	public void smartDeviceUpdated(SmartDeviceServerDictionaryDiscovery sender,
			SmartDevice smartDevice) {
		// LOG.debug("smartDeviceUpdated: " +
		// smartDevice.getPeerIDString());

		// TODO: Nachschauen was sich am SmartDevice geändert hat.
		// Falls Sensoren hinzugefügt oder entfernt wurden, dann müssen die
		// Logik-Regeln überprüft und anschließend ausgeführt oder entfernt
		// werden!

	}

	void processLogic(SmartDevice newSmartDevice) {
		/*******************************
		 * TODO: Logic processing: 1. find matches
		 *******************************/

		String smartDeviceContextName = "";
		if (SmartDeviceServer.getInstance().getLocalSmartDevice() == null) {
			return;
		} else {
			smartDeviceContextName = SmartDeviceServer.getInstance()
					.getLocalSmartDevice().getContextName();
			LOG.debug("smartDeviceCtx:" + smartDeviceContextName
					+ " newSmartDeviceCtx:" + newSmartDevice.getContextName());
			// TODO: comment out next line
			smartDeviceContextName = "Office";
		}

		if (smartDeviceContextName != null
				&& newSmartDevice.getContextName().equals(
						smartDeviceContextName)) {

			for (Sensor remoteSensor : newSmartDevice.getConnectedSensors()) {
				// LOG.debug("_remoteSensor:"+remoteSensor.getName());
				for (String possibleActivityNameFromSensor : remoteSensor
						.getPossibleActivityNames()) {
					// LOG.debug("__possibleActivityNameFromSensor:"+possibleActivityNameFromSensor);
					for (Actor localActor : SmartDeviceServer.getInstance()
							.getLocalSmartDevice().getConnectedActors()) {
						// LOG.debug("___:"+localActor.getName());
						if (localActor != null) {
							for (String actorActivity : localActor
									.getPossibleActivityNames()) {
								// LOG.debug("____:"+actorActivity);
								if (actorActivity
										.equals(possibleActivityNameFromSensor)) {

									LOG.debug("_!_a:"
											+ possibleActivityNameFromSensor
											+ " rS:" + remoteSensor + " ac:"
											+ localActor.getName() + " nD:"
											+ newSmartDevice.getPeerName());

									QueryExecutor
											.getInstance()
											.addNeededSourceForImport(
													remoteSensor
															.getActivitySourceName(possibleActivityNameFromSensor),
													newSmartDevice.getPeerID());

									QueryExecutor
											.getInstance()
											.executeActorLogicRuleWhenPossibleAsync(
													possibleActivityNameFromSensor,
													(Sensor) remoteSensor,
													(Actor) localActor,
													newSmartDevice);
								}
							}
						}
					}
				}
			}
		} else {
			// The context does not matter:

		}

	}

	public static boolean isSourceNameNeddedByLogicRule(String sourceName) {
		// TODO: check correctness

		for (Actor actor : SmartDeviceServer.getInstance()
				.getLocalSmartDevice().getConnectedActors()) {
			for (String activity : actor.getPossibleActivityNames()) {
				try {
					for (Entry<String, String> map : actor
							.getLogicRuleOfActivity(activity).entrySet()) {
						String queryName = map.getKey();
						// String query = map.getValue();

						if (sourceName.equals(queryName)) {
							return true;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return false;
	}

	public static Logic getInstance() {
		if (instance == null) {
			instance = new Logic();
		}
		return instance;
	}

	public void bindP2PNetworkManager(IP2PNetworkManager serv) {
		p2pNetworkManager = serv;
		p2pNetworkManager.addAdvertisementListener(this);
	}

	public void unbindP2PNetworkManager(IP2PNetworkManager serv) {
		if (p2pNetworkManager == serv) {
			getP2PNetworkManager().removeAdvertisementListener(this);
			p2pNetworkManager = null;
		}
	}

	private IP2PNetworkManager getP2PNetworkManager() {
		return p2pNetworkManager;
	}

	@Override
	public void advertisementDiscovered(Advertisement advertisement) {
		// LOG.debug("advertisementDiscovered AdvType:"
		// + advertisement.getAdvType());
		if (advertisement != null
				&& advertisement instanceof SourceAdvertisement
				&& advertisement.getAdvType().equals(
						SourceAdvertisement.getAdvertisementType())) {
			SourceAdvertisement srcAdv = (SourceAdvertisement) advertisement;

			QueryExecutor.getInstance().importIfNeccessary(srcAdv);
		} else if (advertisement != null
				&& advertisement instanceof MultipleSourceAdvertisement
				&& advertisement.getAdvType().equals(
						MultipleSourceAdvertisement.getAdvertisementType())) {
			MultipleSourceAdvertisement multiSourceAdv = (MultipleSourceAdvertisement) advertisement;

			// String peerIDStr =
			// multiSourceAdv.getPeerID().intern().toString();

			for (SourceAdvertisement sAdv : multiSourceAdv
					.getSourceAdvertisements()) {

				QueryExecutor.getInstance().importIfNeccessary(sAdv);
			}

		}
	}

	@Override
	public void updateAdvertisements() {
	}

}
