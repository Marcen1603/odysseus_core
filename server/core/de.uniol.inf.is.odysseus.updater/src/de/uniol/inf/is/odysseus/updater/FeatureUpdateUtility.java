package de.uniol.inf.is.odysseus.updater;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.operations.InstallOperation;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.Update;
import org.eclipse.equinox.p2.operations.UpdateOperation;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;

import de.uniol.inf.is.odysseus.core.server.usermanagement.UpdatePermission;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.PermissionException;

public class FeatureUpdateUtility {

	private static final String REPOSITORY_LOC = "http://odysseus.informatik.uni-oldenburg.de/update";

	public static IStatus installFeature(String id, final ISession caller) {
		if (UserManagementProvider.getUsermanagement().hasPermission(caller, UpdatePermission.INSTALL, UpdatePermission.objectURI)) {
			List<IInstallableUnit> units = getInstallableUnits(id, caller);

			if (units != null && !units.isEmpty()) {
				System.out.println("Found following features that will be installed now: ");
				for (IInstallableUnit unit : units) {
					System.out.println(" - " + unit.getId());
				}
				BundleContext context = Activator.getContext();
				IProvisioningAgent agent = getAgent(context);
				final ProvisioningSession session = new ProvisioningSession(agent);
				final InstallOperation operation = new InstallOperation(session, units);

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
				System.out.println("Starting install process...");
				IStatus status = operation.resolveModal(new NullProgressMonitor());
				if (status.isOK()) {					
					final ProvisioningJob provisioningJob = operation.getProvisioningJob(new NullProgressMonitor());					
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
									System.out.println("Features were installed. You have to restart Odysseus for the changed to take effekt!");
									restart(caller);
								}

							}
							super.done(event);
						}
					});

					provisioningJob.schedule();

					return Status.OK_STATUS;
				} else {
					System.out.println(status.getMessage());
					return Status.CANCEL_STATUS;
				}
			} else {
				System.out.println("There is no update with this feature id");
				return Status.CANCEL_STATUS;
			}
		} else {
			throw new PermissionException("This user is not allowed to install new features!");
		}
	}

	private static List<IInstallableUnit> getInstallableUnits(String id, ISession caller) {
		if (UserManagementProvider.getUsermanagement().hasPermission(caller, UpdatePermission.LIST, UpdatePermission.objectURI)) {
			BundleContext context = Activator.getContext();
			IProvisioningAgent agent = getAgent(context);
			IMetadataRepositoryManager metadataManager = (IMetadataRepositoryManager) agent.getService(IMetadataRepositoryManager.SERVICE_NAME);
			try {
				URI uri = null;
				uri = new URI(REPOSITORY_LOC);
				IMetadataRepository repo = metadataManager.loadRepository(uri, new NullProgressMonitor());
				IQueryResult<IInstallableUnit> units = repo.query(QueryUtil.createIUGroupQuery(), new NullProgressMonitor());
				List<IInstallableUnit> toinstall = new ArrayList<>();
				id = id+".feature.group";
				for (IInstallableUnit unit : units.toSet()) {
					// use starts with to ignore version and qualifier
					if (unit.getId().startsWith(id)) {
						toinstall.add(unit);
					}
				}
				return toinstall;
			} catch (final URISyntaxException e) {
				System.out.println("URI invalid: " + e.getMessage());
				return new ArrayList<>();
			} catch (ProvisionException e) {
				e.printStackTrace();
			} catch (OperationCanceledException e) {
				e.printStackTrace();
			}
			return null;
		} else {
			throw new PermissionException("This user may not list the installed features!");
		}
	}

	public static boolean isFeatureInstalled(String id, ISession caller) {
		if (UserManagementProvider.getUsermanagement().hasPermission(caller, UpdatePermission.LIST, UpdatePermission.objectURI)) {
			List<IInstallableUnit> units = getInstalledFeatures(caller);
			for (IInstallableUnit unit : units) {
				if (unit.getId().startsWith(id)) {
					return true;
				}
			}
			return false;
		} else {
			throw new PermissionException("This user may not list the installed features!");
		}
	}

	public static List<IInstallableUnit> getInstalledFeatures(ISession caller) {
		if (UserManagementProvider.getUsermanagement().hasPermission(caller, UpdatePermission.LIST, UpdatePermission.objectURI)) {
			BundleContext context = Activator.getContext();
			IProvisioningAgent agent = getAgent(context);
			IProfileRegistry regProfile = (IProfileRegistry) agent.getService(IProfileRegistry.SERVICE_NAME);
			IProfile profileSelf = regProfile.getProfile(IProfileRegistry.SELF);

			IQuery<IInstallableUnit> query = QueryUtil.createIUGroupQuery();
			IQueryResult<IInstallableUnit> allIUs = profileSelf.query(query, new NullProgressMonitor());

			List<IInstallableUnit> units = new ArrayList<>();
			units.addAll(allIUs.toUnmodifiableSet());

			Collections.sort(units);
			List<IInstallableUnit> features = new ArrayList<>();
			for (IInstallableUnit unit : units) {
				if (unit.getId().contains("feature.group")) {
					features.add(unit);
				}
			}
			return features;
		} else {
			throw new PermissionException("This user may not list the installed features!");
		}
	}

	public static List<IInstallableUnit> getInstallableUnits(ISession caller) {
		if (UserManagementProvider.getUsermanagement().hasPermission(caller, UpdatePermission.LIST, UpdatePermission.objectURI)) {
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
		} else {
			throw new PermissionException("This user may not list installable features!");
		}
	}

	public static IStatus checkForUpdates(final ISession caller) throws OperationCanceledException {

		if (UserManagementProvider.getUsermanagement().hasPermission(caller, UpdatePermission.UPDATE, UpdatePermission.objectURI)) {

			BundleContext context = Activator.getContext();
			IProvisioningAgent agent = getAgent(context);
			IProgressMonitor monitor = new NullProgressMonitor();

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
					System.out.println(status.getMessage());
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
									System.out.println("Updates were installed. You have to restart Odysseus for the changed to take effekt!");
									restart(caller);
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
		} else {
			throw new PermissionException("User is not allowed to update the system!");
		}

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
	
	private static void restart(ISession caller){
		System.out.println("Forcing a refresh of all bundles...");
		Bundle bundle = Activator.getContext().getBundle(0);
		try {
			if(bundle!=null){
				bundle.update();
			}else{
				System.out.println("restart failed, because there osgi bundle was not found!");
			}
		} catch (BundleException e) {
			e.printStackTrace();
		}
	}

}
