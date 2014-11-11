package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.rcp.views;

import java.util.ArrayList;

import net.jxta.peer.PeerID;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ASmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.Actor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.AbstractActorAction;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.LogicRule;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.rcp.SmartHomeRCPActivator;
import de.uniol.inf.is.odysseus.rcp.StatusBarManager;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

public class AddLogicRuleViewPart extends ViewPart {
	private static final Logger LOG = LoggerFactory
			.getLogger(SmartHomeRCPActivator.class);

	private static final int REFRESH_REPEAT_TIME_MS = 2000;

	// private List<PeerID> foundPeerIDs = Lists.newArrayList();

	public AddLogicRuleViewPart() {
	}

	public static final String ID = "de.uniol.inf.is.odysseus.rcp.peer.smarthome.contextawareness.AddLogicRuleViewPart";
	private ASmartDevice localSmartDevice;
	private Combo actorsCombo;
	private Combo activityNamesCombo;
	private Combo actorActionsCombo;
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
				refreshActorsComboItems();
				refreshActorActionsCombo();
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
		lblAaad.setText("Actor:");

		actorsCombo = new Combo(tableComposite, SWT.READ_ONLY);
		actorsCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		actorsCombo.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refreshActorActionsCombo();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		Label lblAction = new Label(tableComposite, SWT.NONE);
		lblAction.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblAction.setText("Action:");

		actorActionsCombo = new Combo(tableComposite, SWT.READ_ONLY);
		actorActionsCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		Button btnCreate = new Button(tableComposite, SWT.NONE);
		btnCreate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				createLogicRule();
			}
		});
		btnCreate.setText("Create rule");

		Button btnCancel = new Button(tableComposite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Cancel");
			}
		});
		btnCancel.setText("Cancel");

		// text field: for activity name

		// Drop-Down list: with connected actors
		// localSmartDevice =
		// SmartHomeRCPActivator.getSmartDeviceService().getLocalSmartDevice();

		// Drop-Down list: to select action of the actor to execute when the
		// entered activity occures

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
		t.setName("AddLogicRule GUI element refresher thread.");
		t.setDaemon(true);
		t.start();
	}

	protected void refreshAll() {
		refreshFoundPeerIDsList();
		refreshActivityNamesCombo();
		refreshActorsComboItems();
		refreshActorActionsCombo();
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

	/*
	 * private void refreshSmartDevicesCombo() { refreshFoundPeerIDsList();
	 * //SmartHomeRCPActivator.getSmartDeviceService().getLocalSmartDevice()
	 * ArrayList<String> list = new ArrayList<>(); list.add("<local>");
	 * 
	 * //TODO: add smartdevicedictionary
	 * 
	 * //list.add(foundPeerIDs);
	 * 
	 * String[] listArray = convertToArray(list);
	 * smartDeviceCombo.setItems(listArray); }
	 */

	private void refreshActivityNamesCombo() {

		ArrayList<String> list = new ArrayList<>();
		for (Actor actor : getLocalSmartDevice().getConnectedActors()) {
			for (LogicRule rule : actor.getLogicRules()) {
				list.add(rule.getActivityName());
			}
		}

		String[] listArray = convertToArray(list);
		synchronized (activityNamesCombo) {
			activityNamesCombo.setItems(listArray);

		}
	}

	private void refreshActorsComboItems() {
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

		String[] items = new String[smartDevice.getConnectedActors().size()];

		for (int i = 0; i < smartDevice.getConnectedActors().size(); i++) {
			items[i] = smartDevice.getConnectedActors().get(i).getName();
		}

		actorsCombo.setItems(items);
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

	private void refreshActorActionsCombo() {
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

		int selectedActor = actorsCombo.getSelectionIndex();

		if (selectedActor >= 0) {
			Actor actor = smartDevice.getConnectedActors().get(selectedActor);

			ArrayList<String> list = new ArrayList<>();
			for (AbstractActorAction actions : actor.getActions()) {
				list.add(actions.getName());
			}

			String[] listArray = convertToArray(list);
			actorActionsCombo.setItems(listArray);
		} else {
			actorActionsCombo.setItems(new String[] {});
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

	private void createLogicRule() {
		int selectedPeerID = smartDeviceComboViewer.getCombo()
				.getSelectionIndex();
		String activityName = activityNamesCombo.getText();
		// String actorName = actorsCombo.getText();
		// String actorActionName = actorActionsCombo.getText();
		int selectedActor = actorsCombo.getSelectionIndex();
		int selectedActorAction = actorActionsCombo.getSelectionIndex();

		if (selectedPeerID < 0 || foundPeerIDs.get(selectedPeerID) == null) {
			MessageDialog msgDialog = new MessageDialog(
					Display.getCurrent().getActiveShell(),
					null,
					null,
					"The logic rule can't created. Please select a smart device.",
					MessageDialog.WARNING, new String[] { "Ok" }, 0);
			msgDialog.open();
			return;
		}

		if (activityName == null || activityName.equals("")) {
			MessageDialog msgDialog = new MessageDialog(
					Display.getCurrent().getActiveShell(),
					null,
					null,
					"The logic rule can't created. Please enter an activity name.",
					MessageDialog.WARNING, new String[] { "Ok" }, 0);
			msgDialog.open();
			return;
		}

		if (selectedActor < 0) {
			MessageDialog msgDialog = new MessageDialog(Display.getCurrent()
					.getActiveShell(), null, null,
					"The logic rule can't created. Please select an actor.",
					MessageDialog.WARNING, new String[] { "Ok" }, 0);
			msgDialog.open();
			return;
		}

		if (selectedActorAction < 0) {
			MessageDialog msgDialog = new MessageDialog(
					Display.getCurrent().getActiveShell(),
					null,
					null,
					"The logic rule can't created. Please select an action for the actor.",
					MessageDialog.WARNING, new String[] { "Ok" }, 0);
			msgDialog.open();
			return;
		}

		PeerID currentPeer = foundPeerIDs.get(selectedPeerID);
		ASmartDevice smartDevice = getSmartDevice(currentPeer);

		Actor actor = smartDevice.getConnectedActors().get(selectedActor);

		AbstractActorAction actorAction = actor.getActions().get(
				selectedActorAction);

		try {
			if (isLocalID(currentPeer)) {
				if (actor.createLogicRuleWithAction(activityName, actorAction)) {
					refreshLogicRuleViewPart();
					StatusBarManager.getInstance().setMessage(
							"Logic rule created for device: "
									+ smartDevice.getPeerName());
				} else {
					MessageDialog msgDialog = new MessageDialog(
							Display.getCurrent().getActiveShell(),
							null,
							null,
							"The logic rule can't created. Maybe it already exists?",
							MessageDialog.WARNING, new String[] { "Ok" }, 0);
					msgDialog.open();
				}
			} else {
				try {
					if (actor.createLogicRuleWithAction(activityName,
							actorAction) && actor.save()) {
						StatusBarManager.getInstance().setMessage(
								"Logic rule created for device: "
										+ smartDevice.getPeerName());
					} else {
						MessageDialog msgDialog = new MessageDialog(
								Display.getCurrent().getActiveShell(),
								null,
								null,
								"The logic rule can't created. Maybe it already exists.",
								MessageDialog.WARNING, new String[] { "Ok" }, 0);
						msgDialog.open();
					}
				} catch (Exception ex) {
					MessageDialog msgDialog = new MessageDialog(
							Display.getCurrent().getActiveShell(),
							null,
							null,
							"The logic rule can't created. Maybe there is no connection to the remote peer.",
							MessageDialog.WARNING, new String[] { "Ok" }, 0);
					msgDialog.open();
				}
			}
		} catch (Exception ex) {
			MessageDialog msgDialog = new MessageDialog(Display.getCurrent()
					.getActiveShell(), null, null,
					"The logic rule can't created.", MessageDialog.WARNING,
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

	private void refreshLogicRuleViewPart() {
		Optional<SmartDeviceLogicView> optView = SmartDeviceLogicView
				.getInstance();
		if (optView.isPresent()) {
			SmartDeviceLogicView view = optView.get();
			view.refresh();
		}
	}
}
