package de.uniol.inf.is.odysseus.core.server.console;

import java.net.URI;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.equinox.internal.p2.ui.ProvUI;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.engine.IPhaseSet;
import org.eclipse.equinox.p2.engine.PhaseSetFactory;
import org.eclipse.equinox.p2.operations.ProfileModificationJob;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.equinox.p2.ui.ProvisioningUI;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

@SuppressWarnings("restriction")
public class P2ConsoleWrapper {

	public static IStatus checkForUpdates(CommandInterpreter ci) throws OperationCanceledException {
		
		BundleContext context = Activator.getContext();
		IProvisioningAgent agent = getAgent(context);
		ProvisioningSession session = new ProvisioningSession(agent);
		IProgressMonitor monitor = new NullProgressMonitor();
		
		ci.println("Refreshing repositories...");
		refreshRepositories(context, monitor);
		ci.println("Checking for updates...");
		
		UpdateOperation operation = new UpdateOperation(session);
		IStatus status = operation.resolveModal(monitor);

		if (status.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE) {
			monitor.done();
			ci.println("Nothing to update!");
			return status;
		}

		if (status.getSeverity() == IStatus.CANCEL) {
			ci.println("Update canceled!");
			throw new OperationCanceledException();
		}

		if (status.getSeverity() != IStatus.ERROR) {
			ci.println("Error during update!");
			ci.println("Updating failes with: " + status.getMessage());
			ProvisioningJob job = operation.getProvisioningJob(null);
			if (job instanceof ProfileModificationJob) {
				ProfileModificationJob pJob = (ProfileModificationJob) job;
				IPhaseSet phaseSet = PhaseSetFactory.createDefaultPhaseSetExcluding(new String[] { PhaseSetFactory.PHASE_CHECK_TRUST });
				pJob.setPhaseSet(phaseSet);
				if (status.getSeverity() == IStatus.CANCEL)
					throw new OperationCanceledException();
			} else {
				monitor.done();
				return Status.CANCEL_STATUS;
			}
		}
		monitor.done();

		return status;
	}

	public static IProvisioningAgent getAgent(BundleContext context) {
		IProvisioningAgent agent = null;

		ServiceReference<?> reference = context.getServiceReference(IProvisioningAgent.SERVICE_NAME);
		if (reference != null) {
			agent = (IProvisioningAgent) context.getService(reference);
			context.ungetService(reference);
		}
		if (agent == null) {
			System.out.println("No provisioning agent found.  This application is not set up for updates.");
		}

		return agent;
	}

	public static void refreshRepositories(BundleContext context, IProgressMonitor monitor) {
		final IProvisioningAgent agent = getAgent(context);
		if (agent == null) {
			return;
		}
		ProvisioningSession session = new ProvisioningSession(agent);
		URI[] repositories = ProvisioningUI.getDefaultUI().getRepositoryTracker().getKnownRepositories(session);
		for (URI repository : repositories) {
			refreshRepository(repository, session, monitor);
		}
	}

	private static void refreshRepository(URI location, ProvisioningSession session, IProgressMonitor monitor) {
		monitor.beginTask(URIUtil.toUnencodedString(location), 100);
		try {
			ProvUI.getMetadataRepositoryManager(session).refreshRepository(location, monitor);
			ProvUI.getArtifactRepositoryManager(session).refreshRepository(location, monitor);
		} catch (OperationCanceledException e) {
			e.printStackTrace();
		} catch (ProvisionException e) {
			e.printStackTrace();
		}
		monitor.done();
	}

}
