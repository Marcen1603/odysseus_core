package de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.rcp.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.fielddevice.ASmartDevice;
import de.uniol.inf.is.odysseus.peer.smarthome.contextawareness.rcp.SmartHomeRCPActivator;

public class AddLogicRuleViewPart extends ViewPart {

	public static final String ID = "de.uniol.inf.is.odysseus.rcp.peer.smarthome.contextawareness.AddLogicRuleViewPart";
	private ASmartDevice localSmartDevice;

	@Override
	public void createPartControl(Composite parent) {
		localSmartDevice = SmartHomeRCPActivator.getSmartDeviceService().getLocalSmartDevice();
		
		@SuppressWarnings("unused")
		final Composite tableComposite = new Composite(parent, SWT.NONE);
		
		//text field: for activity name
		
		//Drop-Down list: with connected actors
		//localSmartDevice = SmartHomeRCPActivator.getSmartDeviceService().getLocalSmartDevice();
		
		
		//Drop-Down list: to select action of the actor to execute when the entered activity occures
		
		
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public ASmartDevice getLocalSmartDevice() {
		return localSmartDevice;
	}
}
