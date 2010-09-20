package de.uniol.inf.is.odysseus.rcp.viewer.osgicommands;

import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.PlatformUI;

import de.uniol.inf.is.odysseus.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.planmanagement.executor.configuration.ExecutionConfiguration;
import de.uniol.inf.is.odysseus.planmanagement.executor.standardexecutor.SettingBufferPlacementStrategy;
import de.uniol.inf.is.odysseus.rcp.statusbar.StatusBarManager;
import de.uniol.inf.is.odysseus.rcp.viewer.osgicommands.activator.Activator;

public class SetStandardBuffPlacementCommand extends AbstractHandler implements
		IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (Activator.getExecutor() == null) {
			StatusBarManager.getInstance().setMessage("No executor available");

			MessageBox box = new MessageBox(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), SWT.ICON_ERROR
					| SWT.OK);
			box.setMessage("No executor available");
			box.setText("Error");
			box.open();

			return null;
		}

		IExecutor executor = Activator.getExecutor();

		Set<String> list = executor.getRegisteredBufferPlacementStrategies();
		String bufferName = "Standard Buffer Placement";
		if (list.contains(bufferName)) {
			ExecutionConfiguration conf = executor.getConfiguration();
			conf.set(
					new SettingBufferPlacementStrategy(bufferName));
			System.out.println("Strategy " + bufferName + " set.");

		}
		return null;
	}

}
