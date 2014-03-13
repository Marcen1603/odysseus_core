package de.uniol.inf.is.odysseus.core.server.console;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.Update;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class P2ConsoleWrapper {

	private static final String REPOSITORY_LOC = "http://odysseus.informatik.uni-oldenburg.de/update";

//	public static IStatus installFeature(String id) {
//		
//	}

	public static List<IInstallableUnit> getInstalledFeatures(){
		List<IInstallableUnit> units = getInstallableUnits();
		List<IInstallableUnit> features = new ArrayList<>();
		for(IInstallableUnit unit :units){
			if(unit.getId().contains("feature")){
				features.add(unit);
			}
		}
		return features;
	}
	
	public static List<IInstallableUnit> getInstallableUnits() {
		BundleContext context = Activator.getContext();
		IProvisioningAgent agent = getAgent(context);
		IProfileRegistry regProfile = (IProfileRegistry) agent.getService(IProfileRegistry.SERVICE_NAME);
		IProfile profileSelf = regProfile.getProfile(IProfileRegistry.SELF);

		IQuery<IInstallableUnit> query = QueryUtil.createIUAnyQuery();
		IQueryResult<IInstallableUnit> allIUs = profileSelf.query(query, new NullProgressMonitor());

		List<IInstallableUnit> units = new ArrayList<>();
		units.addAll(allIUs.toUnmodifiableSet());
		Collections.sort(units);
		return units;		
	}

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
		try {
			final IStatus status = operation.resolveModal(monitor);

			// failed to find updates (inform user and exit)
			if (!status.isOK()) {
				System.out.println("Repository not reachable or no updates found");
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
				System.out.println("Following updates found: \n" + updates);
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
							if (restart) {
								System.out.println("Updates we're installed. You have to restart Odysseus for the changed to take effekt!");								
							}

						}
						super.done(event);
					}
				});

				provisioningJob.schedule();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Status.OK_STATUS;

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

}
