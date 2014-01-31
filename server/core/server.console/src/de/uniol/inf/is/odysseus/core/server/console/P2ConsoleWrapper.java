package de.uniol.inf.is.odysseus.core.server.console;

import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.engine.IPhaseSet;
import org.eclipse.equinox.p2.engine.PhaseSetFactory;
import org.eclipse.equinox.p2.operations.ProfileModificationJob;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.Update;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class P2ConsoleWrapper {

	private static final String REPOSITORY_LOC = "https://odysseus.informatik.uni-oldenburg.de/update";

	public static IStatus checkForUpdates(CommandInterpreter ci) throws OperationCanceledException {
		BundleContext context = Activator.getContext();
		IProvisioningAgent agent = getAgent(context);
		return checkForUpdates(agent, new NullProgressMonitor());
	}

	private static IStatus checkForUpdates(final IProvisioningAgent agent, IProgressMonitor monitor) throws OperationCanceledException {
		boolean doInstall = true;
		/* 1. Prepare update plumbing */

		final ProvisioningSession session = new ProvisioningSession(agent);
		final UpdateOperation operation = new UpdateOperation(session);

		// create uri
		URI uri = null;
		try {
			uri = new URI(REPOSITORY_LOC);
		} catch (final URISyntaxException e) {
			System.out.println("URI invalid: " + e.getMessage());
			return Status.CANCEL_STATUS;
		}

		// set location of artifact and metadata repo
		operation.getProvisioningContext().setArtifactRepositories(new URI[] { uri });
		operation.getProvisioningContext().setMetadataRepositories(new URI[] { uri });

		/* 2. check for updates */

		// run update checks causing I/O
		final IStatus status = operation.resolveModal(monitor);

		// failed to find updates (inform user and exit)
		if (status.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE) {
			System.out.println("No update. No updates for the current installation have been found");
			return Status.CANCEL_STATUS;
		}

		/* 3. Ask if updates should be installed and run installation */

		// found updates, ask user if to install?
		if (status.isOK() && status.getSeverity() != IStatus.ERROR) {

			String updates = "";
			Update[] possibleUpdates = operation.getPossibleUpdates();
			for (Update update : possibleUpdates) {
				updates += update + "\n";
			}
			System.out.println("Updates for: "+updates);
			doInstall = true;
		}

		// start installation
		if (doInstall) {
			final ProvisioningJob provisioningJob = operation.getProvisioningJob(monitor);
			// updates cannot run from within Eclipse IDE!!!
			if (provisioningJob == null) {
				System.err.println("Running update from within Eclipse IDE? This won't work!!! Use exported product!");
				throw new NullPointerException();
			}

			// register a job change listener to track
			// installation progress and notify user upon success
			provisioningJob.addJobChangeListener(new JobChangeAdapter() {
				@Override
				public void done(IJobChangeEvent event) {
					if (event.getResult().isOK()) {
						boolean restart = true;
						//
						// "Updates installed, restart?",
						// "Updates have been installed successfully, do you want to restart?");
						if (restart) {
							System.out.println("TODO: restart!");
							// workbench.restart();
						}

					}
					super.done(event);
				}
			});

			provisioningJob.schedule();
		}
		return Status.OK_STATUS;

	}

	// ProvisioningSession session = new ProvisioningSession(agent);
	// // the default update operation looks for updates to the currently
	// // running profile, using the default profile root marker. To change
	// // which installable units are being updated, use the more detailed
	// // constructors.
	// UpdateOperation operation = new UpdateOperation(session);
	// SubMonitor sub = SubMonitor.convert(monitor,
	// "Checking for application updates...", 200);
	// IStatus status = operation.resolveModal(sub.newChild(100));
	// if (status.getCode() == UpdateOperation.STATUS_NOTHING_TO_UPDATE) {
	// return status;
	// }
	// if (status.getSeverity() == IStatus.CANCEL)
	// throw new OperationCanceledException();
	//
	// if (status.getSeverity() != IStatus.ERROR) {
	// // More complex status handling might include showing the user what
	// // updates
	// // are available if there are multiples, differentiating patches vs.
	// // updates, etc.
	// // In this example, we simply update as suggested by the operation.
	// ProvisioningJob job = operation.getProvisioningJob(null);
	// status = job.runModal(sub.newChild(100));
	// if (status.getSeverity() == IStatus.CANCEL)
	// throw new OperationCanceledException();
	// }
	// return status;
	// }

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
