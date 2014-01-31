package de.uniol.inf.is.odysseus.core.server.console;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubMonitor;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.engine.IPhaseSet;
import org.eclipse.equinox.p2.engine.PhaseSetFactory;
import org.eclipse.equinox.p2.operations.ProfileModificationJob;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class P2ConsoleWrapper {

	public static IStatus checkForUpdates(CommandInterpreter ci) throws OperationCanceledException {
		BundleContext context = Activator.getContext();
		IProvisioningAgent agent = getAgent(context);
		return checkForUpdates(agent, new NullProgressMonitor());
	}

	private static IStatus checkForUpdates(IProvisioningAgent agent, IProgressMonitor monitor) throws OperationCanceledException {
		ProvisioningSession session = new ProvisioningSession(agent);
		// the default update operation looks for updates to the currently
		// running profile, using the default profile root marker. To change
		// which installable units are being updated, use the more detailed
		// constructors.
		UpdateOperation operation = new UpdateOperation(session);
		SubMonitor sub = SubMonitor.convert(monitor, "Checking for application updates...", 200);
		IStatus status = operation.resolveModal(sub.newChild(100));
		if (status.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE) {
			return status;
		}
		if (status.getSeverity() == IStatus.CANCEL)
			throw new OperationCanceledException();

		if (status.getSeverity() != IStatus.ERROR) {
			// More complex status handling might include showing the user what
			// updates
			// are available if there are multiples, differentiating patches vs.
			// updates, etc.
			// In this example, we simply update as suggested by the operation.
			ProvisioningJob job = operation.getProvisioningJob(null);
			status = job.runModal(sub.newChild(100));
			if (status.getSeverity() == IStatus.CANCEL)
				throw new OperationCanceledException();
		}
		return status;
	}

	public static IStatus checkForUpdatesOLD(CommandInterpreter ci) throws OperationCanceledException {

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

		if (status.getSeverity() == IStatus.ERROR) {
			ci.println("Error during update!");
			ci.println("Updating failes with: " + status.getMessage());
			return status;
		}
		if (status.isOK()) {
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
		// final IProvisioningAgent agent = getAgent(context);
		// if (agent == null) {
		// return;
		// }
		// ProvisioningSession session = new ProvisioningSession(agent);
		// URI[] repositories =
		// ProvisioningUI.getDefaultUI().getRepositoryTracker().getKnownRepositories(session);
		// for (URI repository : repositories) {
		// refreshRepository(repository, session, monitor);
		// }
	}
	//
	// private static void refreshRepository(URI location, ProvisioningSession
	// session, IProgressMonitor monitor) {
	// monitor.beginTask(URIUtil.toUnencodedString(location), 100);
	// try {
	// ProvUI.getMetadataRepositoryManager(session).refreshRepository(location,
	// monitor);
	// ProvUI.getArtifactRepositoryManager(session).refreshRepository(location,
	// monitor);
	// } catch (OperationCanceledException e) {
	// e.printStackTrace();
	// } catch (ProvisionException e) {
	// e.printStackTrace();
	// }
	// monitor.done();
	// }

}
