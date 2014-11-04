package de.uniol.inf.is.odysseus.peer.smarthome.server;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.Actor;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.Sensor;
import de.uniol.inf.is.odysseus.peer.smarthome.fielddevice.SmartDevice;


public class LogicExecutionContainer {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeServerPlugIn.class);
	
	private static LogicExecutionContainer instance;

	public static LogicExecutionContainer getInstance() {
		if (instance == null) {
			instance = new LogicExecutionContainer();
		}
		return instance;
	}

	public void removeLogicRules(SmartDevice smDevice) {
		Integer removeIndex = new Integer(-1);
		for (int i = 0; i < getRuleContainer().size(); i++) {
			DeviceContainer devCon = getRuleContainer().get(i);
			if (devCon.getSmartDevice().getPeerID()
					.equals(smDevice.getPeerID())) {
				removeIndex = new Integer(i);
				break;
			}
		}
		if (removeIndex != null && removeIndex.intValue() >= 0) {
			LOG.debug("removeLogicRules(" + smDevice.getPeerName() + ")");

			getRuleContainer().remove(removeIndex.intValue());
			removeLogicRules(smDevice);
		}
	}

	private ArrayList<DeviceContainer> ruleContainer;

	public void addRunningLogicRule(String possibleActivityName,
			Sensor sensor, Actor actor, SmartDevice smartDevice) {

		if (!isLogicRuleRunning(possibleActivityName, sensor, actor,
				smartDevice)) {
			DeviceContainer coll = new DeviceContainer();
			coll.setPossibleActivityName(possibleActivityName);
			coll.setSensor(sensor);
			coll.setActor(actor);
			coll.setSmartDevice(smartDevice);

			getRuleContainer().add(coll);
		}
	}

	private ArrayList<DeviceContainer> getRuleContainer() {
		if (ruleContainer == null) {
			ruleContainer = new ArrayList<DeviceContainer>();
		}
		return ruleContainer;
	}

	public boolean isLogicRuleRunning(String possibleActivityName,
			Sensor sensor, Actor actor, SmartDevice smartDevice) {
		// TODO Auto-generated method stub
		// TODO: überprüfen ob die Logic-Regel bereits ausgeführt
		// wird/wurde!

		DeviceContainer testingRule = new DeviceContainer();
		testingRule.setPossibleActivityName(possibleActivityName);
		testingRule.setSensor(sensor);
		testingRule.setActor(actor);
		testingRule.setSmartDevice(smartDevice);

		for (DeviceContainer devCon : getRuleContainer()) {
			if (devCon.equals(testingRule)) {
				System.out.println("Equal: " + devCon.toString() + "");
				return true;
			}
		}

		return false;
	}

	class DeviceContainer {
		String possibleActivityName;
		Sensor sensor;
		Actor actor;
		SmartDevice smartDevice;

		public Sensor getSensor() {
			return sensor;
		}

		public void setSensor(Sensor sensor) {
			this.sensor = sensor;
		}

		public Actor getActor() {
			return actor;
		}

		public void setActor(Actor actor) {
			this.actor = actor;
		}

		public SmartDevice getSmartDevice() {
			return smartDevice;
		}

		public void setSmartDevice(SmartDevice smartDevice) {
			this.smartDevice = smartDevice;
		}

		public String getPossibleActivityName() {
			return possibleActivityName;
		}

		public void setPossibleActivityName(String possibleActivityName) {
			this.possibleActivityName = possibleActivityName;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj instanceof DeviceContainer) {
				DeviceContainer devCon = (DeviceContainer) obj;
				return this.getPossibleActivityName().equals(
						devCon.getPossibleActivityName())
						&& this.getSensor().getName()
								.equals(devCon.getSensor().getName())
						&& this.getActor().getName()
								.equals(devCon.getActor().getName())
						&& this.getSmartDevice()
								.getPeerID()
								.equals(devCon.getSmartDevice().getPeerID());
			}

			return super.equals(obj);
		}

		@Override
		public String toString() {
			try {
				return getPossibleActivityName() + ","
						+ getSensor().getName() + ","
						+ getActor().getName() + ","
						+ getSmartDevice().getPeerName();
			} catch (Exception ex) {
				return super.toString();
			}
		}
	}
}
