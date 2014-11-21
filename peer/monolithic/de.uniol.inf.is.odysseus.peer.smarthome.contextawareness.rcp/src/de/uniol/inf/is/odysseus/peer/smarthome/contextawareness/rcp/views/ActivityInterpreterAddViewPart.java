package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.rcp.views;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.activityinterpreter.ActivityInterpreter;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.actor.AbstractActorAction;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.actor.Actor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.sensor.Sensor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.rcp.SmartHomeRCPActivator;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.smartdevice.ASmartDevice;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;
import net.jxta.peer.PeerID;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

import com.google.common.base.Optional;

public class ActivityInterpreterAddViewPart extends ViewPart {

	public static final String ID = "de.uniol.inf.is.odysseus.rcp.peer.smarthome.contextawareness.ActivityInterpreterAddViewPart";

	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeRCPActivator.class);

	private static final int REFRESH_REPEAT_TIME_MS = 2000;

	// private List<PeerID> foundPeerIDs = Lists.newArrayList();

	private ASmartDevice localSmartDevice;
	private Combo sensorsCombo;
	private Combo activityNamesCombo;
	private Combo sensorsConditionCombo;
	private ComboViewer smartDeviceComboViewer;
	private ArrayList<PeerID> foundPeerIDs = Lists.newArrayList();

	@Override
	public void createPartControl(Composite parent) {
		localSmartDevice = SmartHomeRCPActivator.getSmartDeviceService()
				.getLocalSmartDevice();

		final Composite tableComposite = new Composite(parent, SWT.NONE);
		tableComposite.setLayout(new GridLayout(2, false));

		Label lblSmartdevice = new Label(tableComposite, SWT.NONE);
		lblSmartdevice.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblSmartdevice.setText("SmartDevice:");

		/* if the current person is selected, show text */

		smartDeviceComboViewer = new ComboViewer(tableComposite, SWT.READ_ONLY);
		Combo combo = smartDeviceComboViewer.getCombo();
		GridData gd_combo = new GridData(SWT.FILL, SWT.CENTER, false, false, 1,
				1);
		gd_combo.widthHint = 485;
		combo.setLayoutData(gd_combo);
		Combo smartDeviceCombo = smartDeviceComboViewer.getCombo();
		smartDeviceCombo.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshActivityNamesCombo();
				refreshSensorsComboItems();
				refreshSensorConditionsCombo();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		// smartDeviceComboViewer.setLayoutData(new GridData(SWT.FILL,
		// SWT.CENTER, true, false, 1, 1));
		smartDeviceComboViewer.setContentProvider(ArrayContentProvider
				.getInstance());
		smartDeviceComboViewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof PeerID) {
					PeerID current = (PeerID) element;

					if (SmartHomeRCPActivator.getP2PNetworkManager()
							.getLocalPeerID() == current) {
						return "<local>"
								+ SmartHomeRCPActivator.getP2PNetworkManager()
										.getLocalPeerName();
					}

					return SmartHomeRCPActivator.getPeerDictionary()
							.getRemotePeerName(current);
				}
				return super.getText(element);
			}
		});
		smartDeviceComboViewer.setInput(foundPeerIDs);

		// smartDeviceCombo = new Combo(tableComposite, SWT.READ_ONLY);
		// smartDeviceCombo.setItems(new String[] {"Raspberry", "banana"});
		// refreshSmartDevicesCombo();
		// smartDeviceCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
		// true, false, 1, 1));

		Label lblAaaaaa = new Label(tableComposite, SWT.NONE);
		lblAaaaaa.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblAaaaaa.setText("ActivityName:");

		activityNamesCombo = new Combo(tableComposite, SWT.NONE);
		activityNamesCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		Label lblAaad = new Label(tableComposite, SWT.NONE);
		lblAaad.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		lblAaad.setText("Sensor:");

		sensorsCombo = new Combo(tableComposite, SWT.READ_ONLY);
		sensorsCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		sensorsCombo.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshSensorConditionsCombo();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		Label lblAction = new Label(tableComposite, SWT.NONE);
		lblAction.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblAction.setText("Action:");

		sensorsConditionCombo = new Combo(tableComposite, SWT.READ_ONLY);
		sensorsConditionCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		Button btnCreate = new Button(tableComposite, SWT.NONE);
		btnCreate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				createActivityInterpreter();
			}
		});
		btnCreate.setText("Create");

		Button btnCancel = new Button(tableComposite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Cancel");
			}
		});
		btnCancel.setText("Cancel");

		refreshAll();

		refreshRepeatAsync();
	}

	private void refreshRepeatAsync() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(REFRESH_REPEAT_TIME_MS);
					} catch (InterruptedException e) {
					}
					refreshFoundPeerIDsList();
					refreshsmartDeviceComboAsync();
				}
			}
		});
		t.setName("AddActivityInterpreter GUI element refresher thread.");
		t.setDaemon(true);
		t.start();
	}

	protected void refreshAll() {
		refreshFoundPeerIDsList();
		refreshActivityNamesCombo();
		refreshSensorsComboItems();
		refreshSensorConditionsCombo();
	}

	private void refreshFoundPeerIDsList() {
		// Collection<PeerID> foundPeerIDsCopy = null;
		synchronized (foundPeerIDs) {
			try {
				foundPeerIDs.clear();
				if (SmartHomeRCPActivator.getPeerDictionary() != null
						&& SmartHomeRCPActivator.getPeerDictionary()
								.getRemotePeerIDs() != null) {
					foundPeerIDs.add(SmartHomeRCPActivator
							.getP2PNetworkManager().getLocalPeerID());
					foundPeerIDs.addAll(SmartHomeRCPActivator
							.getPeerDictionary().getRemotePeerIDs());
				}
				// foundPeerIDsCopy = Lists.newArrayList(foundPeerIDs);
			} catch (NullPointerException ex) {
				LOG.error(ex.getMessage(), ex);
			} catch (Exception ex) {
				LOG.error(ex.getMessage(), ex);
			}
		}
	}

	private void refreshsmartDeviceComboAsync() {
		Display disp = PlatformUI.getWorkbench().getDisplay();
		if (!disp.isDisposed()) {
			disp.asyncExec(new Runnable() {
				@Override
				public void run() {
					synchronized (smartDeviceComboViewer) {
						if (!smartDeviceComboViewer.getCombo().isDisposed()) {
							smartDeviceComboViewer.refresh();

							// synchronized (foundPeerIDs) {
							// setPartName("Smart Devices (" +
							// foundPeerIDs.size() + ")");
							// }

						}
					}
				}
			});
		}
	}

	private void refreshActivityNamesCombo() {

		ArrayList<String> list = new ArrayList<>();
		for (Sensor sensor : getLocalSmartDevice().getConnectedSensors()) {
			for (ActivityInterpreter interpreter : sensor.getActivityInterpreters()) {
				list.add(interpreter.getActivityName());
			}
		}

		String[] listArray = convertToArray(list);
		synchronized (activityNamesCombo) {
			activityNamesCombo.setItems(listArray);

		}
	}

	private void refreshSensorsComboItems() {
		int selectedSmartDevice = smartDeviceComboViewer.getCombo()
				.getSelectionIndex();
		if (selectedSmartDevice < 0) {
			return;
		}

		PeerID currentPeer = foundPeerIDs.get(selectedSmartDevice);
		ASmartDevice smartDevice = getSmartDevice(currentPeer);

		if (smartDevice == null) {
			return;
		}

		String[] items = new String[smartDevice.getConnectedSensors().size()];

		for (int i = 0; i < smartDevice.getConnectedSensors().size(); i++) {
			items[i] = smartDevice.getConnectedSensors().get(i).getName();
		}

		sensorsCombo.setItems(items);
	}

	private static boolean isLocalID(PeerID pid) {
		boolean value = false;
		try {
			if (SmartHomeRCPActivator.getP2PNetworkManager() != null
					&& SmartHomeRCPActivator.getP2PNetworkManager()
							.getLocalPeerID() != null && pid != null) {
				value = SmartHomeRCPActivator.getP2PNetworkManager()
						.getLocalPeerID().equals(pid);
			}
		} catch (NullPointerException ex) {
			throw ex;
		}
		return value;
	}

	private void refreshSensorConditionsCombo() {
		int selectedSmartDevice = smartDeviceComboViewer.getCombo()
				.getSelectionIndex();
		if (selectedSmartDevice < 0) {
			return;
		}

		PeerID currentPeer = foundPeerIDs.get(selectedSmartDevice);
		ASmartDevice smartDevice = getSmartDevice(currentPeer);

		if (smartDevice == null) {
			return;
		}

		int selectedSensor = sensorsCombo.getSelectionIndex();

		if (selectedSensor >= 0) {
			/*
			Actor actor = smartDevice.getConnectedActors().get(selectedSensor);

			ArrayList<String> list = new ArrayList<>();
			for (AbstractActorAction actions : actor.getActions()) {
				list.add(actions.getName());
			}

			String[] listArray = convertToArray(list);
			sensorsConditionCombo.setItems(listArray);
			*/
		} else {
			sensorsConditionCombo.setItems(new String[] {});
		}
	}

	private String[] convertToArray(ArrayList<String> list) {
		String[] newList = new String[list.size()];

		for (int i = 0; i < list.size(); i++) {
			newList[i] = list.get(i);
		}

		return newList;
	}

	@Override
	public void setFocus() {

	}

	public ASmartDevice getLocalSmartDevice() {
		return localSmartDevice;
	}

	@SuppressWarnings("unused")
	private void createActivityInterpreter() {
		int selectedPeerID = smartDeviceComboViewer.getCombo()
				.getSelectionIndex();
		String activityName = activityNamesCombo.getText();
		// String actorName = actorsCombo.getText();
		// String actorActionName = actorActionsCombo.getText();
		int selectedActor = sensorsCombo.getSelectionIndex();
		int selectedActorAction = sensorsConditionCombo.getSelectionIndex();

		if (selectedPeerID < 0 || foundPeerIDs.get(selectedPeerID) == null) {
			MessageDialog msgDialog = new MessageDialog(
					Display.getCurrent().getActiveShell(),
					null,
					null,
					"The activity interpreter can't created. Please select a smart device.",
					MessageDialog.WARNING, new String[] { "Ok" }, 0);
			msgDialog.open();
			return;
		}

		if (activityName == null || activityName.equals("")) {
			MessageDialog msgDialog = new MessageDialog(
					Display.getCurrent().getActiveShell(),
					null,
					null,
					"The activity interpreter can't created. Please enter an activity name.",
					MessageDialog.WARNING, new String[] { "Ok" }, 0);
			msgDialog.open();
			return;
		}

		if (selectedActor < 0) {
			MessageDialog msgDialog = new MessageDialog(Display.getCurrent()
					.getActiveShell(), null, null,
					"Theactivity interpreter can't created. Please select an actor.",
					MessageDialog.WARNING, new String[] { "Ok" }, 0);
			msgDialog.open();
			return;
		}

		/*
		if (selectedActorAction < 0) {
			MessageDialog msgDialog = new MessageDialog(
					Display.getCurrent().getActiveShell(),
					null,
					null,
					"The activity interpreter can't created. Please select an action for the actor.",
					MessageDialog.WARNING, new String[] { "Ok" }, 0);
			msgDialog.open();
			return;
		}
		*/

		PeerID currentPeer = foundPeerIDs.get(selectedPeerID);
		ASmartDevice smartDevice = getSmartDevice(currentPeer);

		Actor actor = smartDevice.getConnectedActors().get(selectedActor);

		AbstractActorAction actorAction = actor.getActions().get(
				selectedActorAction);

		try {
			if (isLocalID(currentPeer)) {
				//TODO: 
				if (false && actor.createLogicRuleWithAction(activityName, actorAction)) {
					refreshActivityInterpreterViewPart();
					StatusBarManager.getInstance().setMessage(
							"Activity interpreter created for device: "
									+ smartDevice.getPeerName());
				} else {
					MessageDialog msgDialog = new MessageDialog(
							Display.getCurrent().getActiveShell(),
							null,
							null,
							"The activity interpreter can't created. Maybe it already exists?",
							MessageDialog.WARNING, new String[] { "Ok" }, 0);
					msgDialog.open();
				}
			} else {
				try {
					//TODO: 
					if (false && actor.createLogicRuleWithAction(activityName,
							actorAction) && actor.save()) {
						StatusBarManager.getInstance().setMessage(
								"Activity interpreter created for device: "
										+ smartDevice.getPeerName());
					} else {
						MessageDialog msgDialog = new MessageDialog(
								Display.getCurrent().getActiveShell(),
								null,
								null,
								"The activity interpreter can't created. Maybe it already exists.",
								MessageDialog.WARNING, new String[] { "Ok" }, 0);
						msgDialog.open();
					}
				} catch (Exception ex) {
					MessageDialog msgDialog = new MessageDialog(
							Display.getCurrent().getActiveShell(),
							null,
							null,
							"The activity interpreter can't created. Maybe there is no connection to the remote peer.",
							MessageDialog.WARNING, new String[] { "Ok" }, 0);
					msgDialog.open();
				}
			}
		} catch (Exception ex) {
			MessageDialog msgDialog = new MessageDialog(Display.getCurrent()
					.getActiveShell(), null, null,
					"The activity interpreter can't created.", MessageDialog.WARNING,
					new String[] { "Ok" }, 0);
			msgDialog.open();
		}
	}

	private ASmartDevice getSmartDevice(PeerID currentPeer) {
		ASmartDevice smartDevice = SmartHomeRCPActivator
				.getSmartDeviceService()
				.getSmartDeviceServerDictionaryDiscovery()
				.getFoundSmartDevices().get(currentPeer.intern().toString());
		if (isLocalID(currentPeer)) {
			smartDevice = getLocalSmartDevice();
		}
		return smartDevice;
	}

	private void refreshActivityInterpreterViewPart() {
		Optional<ActivityInterpreterShowView> optView = ActivityInterpreterShowView
				.getInstance();
		if (optView.isPresent()) {
			ActivityInterpreterShowView view = optView.get();
			view.refresh();
		}
	}
}
