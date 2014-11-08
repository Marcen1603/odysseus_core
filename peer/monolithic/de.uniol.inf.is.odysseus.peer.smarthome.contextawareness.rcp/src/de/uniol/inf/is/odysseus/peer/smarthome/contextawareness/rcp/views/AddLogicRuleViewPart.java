package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.rcp.views;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.LogicRuleFactory;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ASmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.Actor;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.AbstractActorAction;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.LogicRule;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.rcp.SmartHomeRCPActivator;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class AddLogicRuleViewPart extends ViewPart {
	public AddLogicRuleViewPart() {
	}

	public static final String ID = "de.uniol.inf.is.odysseus.rcp.peer.smarthome.contextawareness.AddLogicRuleViewPart";
	private ASmartDevice localSmartDevice;
	private Combo actorsCombo;
	private Combo activityNamesCombo;
	private Combo actorActionsCombo;

	@Override
	public void createPartControl(Composite parent) {
		localSmartDevice = SmartHomeRCPActivator.getSmartDeviceService()
				.getLocalSmartDevice();

		final Composite tableComposite = new Composite(parent, SWT.NONE);
		tableComposite.setLayout(new GridLayout(2, false));

		Label lblAaaaaa = new Label(tableComposite, SWT.NONE);
		lblAaaaaa.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblAaaaaa.setText("ActivityName:");

		activityNamesCombo = new Combo(tableComposite, SWT.NONE);
		refreshActivityNamesCombo();
		activityNamesCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		Label lblAaad = new Label(tableComposite, SWT.NONE);
		lblAaad.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false,
				1, 1));
		lblAaad.setText("Actor:");

		actorsCombo = new Combo(tableComposite, SWT.READ_ONLY);
		refreshActorsComboItems();
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
		refreshActorActionsCombo();
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

	}

	private void refreshActivityNamesCombo() {
		ArrayList<String> list = new ArrayList<>();
		for (Actor actor : getLocalSmartDevice().getConnectedActors()) {
			for (LogicRule rule : actor.getLogicRules()) {
				list.add(rule.getActivityName());
			}
		}

		String[] listArray = convertToArray(list);

		activityNamesCombo.setItems(listArray);
	}

	private void refreshActorsComboItems() {
		String[] items = new String[getLocalSmartDevice().getConnectedActors()
				.size()];

		for (int i = 0; i < getLocalSmartDevice().getConnectedActors().size(); i++) {
			items[i] = getLocalSmartDevice().getConnectedActors().get(i)
					.getName();
		}

		actorsCombo.setItems(items);
	}

	private void refreshActorActionsCombo() {
		int selectedActor = actorsCombo.getSelectionIndex();

		if (selectedActor >= 0) {
			Actor actor = getLocalSmartDevice().getConnectedActors().get(
					selectedActor);

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
		String activityName = activityNamesCombo.getText();
		// String actorName = actorsCombo.getText();
		// String actorActionName = actorActionsCombo.getText();
		int selectedActor = actorsCombo.getSelectionIndex();
		int selectedActorAction = actorActionsCombo.getSelectionIndex();

		if (selectedActor >= 0) {
			Actor actor = getLocalSmartDevice().getConnectedActors().get(
					selectedActor);

			if (selectedActorAction >= 0) {
				AbstractActorAction actorAction = actor.getActions().get(
						selectedActorAction);

				LogicRule createdlogicRule = LogicRuleFactory.createLogicRule(
						activityName, actor, actorAction);
				if (createdlogicRule != null) {
					actor.addLogicRuleForActivity(createdlogicRule);
					
				} else {
					MessageDialog msgDialog = new MessageDialog(Display
							.getCurrent().getActiveShell(), null, null,
							"The logic rule can't created.",
							MessageDialog.WARNING, new String[] { "Ok" }, 0);
					msgDialog.open();
				}
			}
		}
	}
}
