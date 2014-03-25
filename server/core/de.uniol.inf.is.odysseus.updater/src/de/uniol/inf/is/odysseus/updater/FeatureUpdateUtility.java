package de.uniol.inf.is.odysseus.updater;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.URIUtil;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.equinox.p2.core.IAgentLocation;
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
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.usermanagement.UpdatePermission;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.PermissionException;

public class FeatureUpdateUtility {

	private static Logger LOGGER = LoggerFactory.getLogger(FeatureUpdateUtility.class);

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
				IStatus status = operation.resolveModal(getDefaultMonitor());
				if (status.isOK()) {
					final ProvisioningJob provisioningJob = operation.getProvisioningJob(getDefaultMonitor());
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
				IMetadataRepository repo = metadataManager.loadRepository(uri, getDefaultMonitor());
				IQueryResult<IInstallableUnit> units = repo.query(QueryUtil.createIUGroupQuery(), getDefaultMonitor());
				List<IInstallableUnit> toinstall = new ArrayList<>();
				if (!id.endsWith("feature.group")) {
					id = id + ".feature.group";
				}

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
			IQueryResult<IInstallableUnit> allIUs = profileSelf.query(query, getDefaultMonitor());

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

	public static List<IInstallableUnit> getInstallableFeatures(ISession caller) {
		if (UserManagementProvider.getUsermanagement().hasPermission(caller, UpdatePermission.LIST, UpdatePermission.objectURI)) {
			BundleContext context = Activator.getContext();
			IProvisioningAgent agent = getAgent(context);
			IMetadataRepositoryManager metadataManager = (IMetadataRepositoryManager) agent.getService(IMetadataRepositoryManager.SERVICE_NAME);
			try {
				URI uri = null;
				uri = new URI(REPOSITORY_LOC);
				IMetadataRepository repo = metadataManager.loadRepository(uri, getDefaultMonitor());
				IQueryResult<IInstallableUnit> units = repo.query(QueryUtil.createIUGroupQuery(), getDefaultMonitor());
				List<IInstallableUnit> installable = new ArrayList<>();
				String id = ".feature.group";
				List<IInstallableUnit> alreadyInstalled = getInstalledFeatures(caller);
				for (IInstallableUnit unit : units.toSet()) {
					// use starts with to ignore version and qualifier
					String unitid = unit.getId().toLowerCase();

					if (unitid.contains(id) && unitid.startsWith("de.uniol.inf.is") && !unitid.contains("source.feature")) {
						if (!containsWithSameID(alreadyInstalled, unit)) {
							installable.add(unit);
						}
					}
				}
				Collections.sort(installable);
				return installable;
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
			throw new PermissionException("This user may not list installable features!");
		}
	}

	private static boolean containsWithSameID(Collection<IInstallableUnit> list, IInstallableUnit unit) {
		for (IInstallableUnit inList : list) {
			if (inList.getId().equalsIgnoreCase(unit.getId())) {
				return true;
			}
		}
		return false;
	}

	public static boolean checkForUpdates(ISession caller) {
		if (UserManagementProvider.getUsermanagement().hasPermission(caller, UpdatePermission.UPDATE, UpdatePermission.objectURI)) {
			try {

				BundleContext context = Activator.getContext();
				IProvisioningAgent agent = getAgent(context);
				IProgressMonitor monitor = getDefaultMonitor();

				final ProvisioningSession session = new ProvisioningSession(agent);
				final UpdateOperation operation = new UpdateOperation(session);

				URI uri = new URI(REPOSITORY_LOC);
				refreshArtifactRepositories(uri, context);
				operation.getProvisioningContext().setArtifactRepositories(new URI[] { uri });
				operation.getProvisioningContext().setMetadataRepositories(new URI[] { uri });
				final IStatus status = operation.resolveModal(monitor);

				// failed to find updates (inform user and exit)
				if (!status.isOK()) {
					System.out.println(status.getMessage());
					return false;
				}
				if (status.isOK() && status.getSeverity() != IStatus.ERROR) {

					Update[] possibleUpdates = operation.getPossibleUpdates();
					if (possibleUpdates.length > 0) {
						String updates = "";
						for (Update update : possibleUpdates) {
							updates += update + "\n";
						}
						System.out.println("Following updates found: \n" + updates);

						return true;
					} else {
						System.out.println("No updates found.");
						return false;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			throw new PermissionException("User is not allowed to update the system!");
		}
		return false;
	}

	public static IStatus checkForAndInstallUpdates(final ISession caller) throws OperationCanceledException {

		if (UserManagementProvider.getUsermanagement().hasPermission(caller, UpdatePermission.UPDATE, UpdatePermission.objectURI)) {

			try {

				BundleContext context = Activator.getContext();
				IProvisioningAgent agent = getAgent(context);
				IProgressMonitor monitor = getDefaultMonitor();

				final ProvisioningSession session = new ProvisioningSession(agent);

				final UpdateOperation operation = new UpdateOperation(session);

				URI uri = new URI(REPOSITORY_LOC);
				refreshArtifactRepositories(uri, context);
				operation.getProvisioningContext().setArtifactRepositories(new URI[] { uri });
				operation.getProvisioningContext().setMetadataRepositories(new URI[] { uri });

				final IStatus status = operation.resolveModal(monitor);

				if (status.isOK() && status.getSeverity() != IStatus.ERROR) {
					final ProvisioningJob provisioningJob = operation.getProvisioningJob(getDefaultMonitor());
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

	public static String getVersionNumber(ISession caller) {
		List<IInstallableUnit> units = getInstalledFeatures(caller);
		for (IInstallableUnit unit : units) {
			if (unit.getId().toLowerCase().startsWith("de.uniol.inf.is.odysseus.core")) {
				return unit.getVersion().toString();
			}
		}
		return "-1";
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

	public static void restart(ISession caller) {
		BundleContext context = Activator.getContext();
		ServiceReference<?> eventAdminServiceReference = context.getServiceReference(EventAdmin.class.getName());

		// EventAdmin Service lookup
		final EventAdmin eventAdmin = (EventAdmin) context.getService(eventAdminServiceReference);

		Executors.newSingleThreadExecutor().execute(new Runnable() {

			@Override
			public void run() {
				LOGGER.debug("Sending restart event");
				Map<String, String> hashMap = new HashMap<>();
				hashMap.put("TYPE", "RESTART");
				eventAdmin.sendEvent(new Event("de/uniol/inf/odysseus/application/" + System.currentTimeMillis(), hashMap));
			}
		});
	}

	private static IProgressMonitor getDefaultMonitor() {
		return new IProgressMonitor() {

			private boolean canceled = false;
			private String name = "";
			private int totalWork = 100;

			@Override
			public void worked(int work) {
				int percent = (work * 100) / totalWork;
				if (this.name.isEmpty()) {
					LOGGER.info(percent + "% completed");
				} else {
					LOGGER.info(this.name + ": " + percent + "% completed");
				}
			}

			@Override
			public void subTask(String subname) {
				LOGGER.info(this.name + ": " + subname + "...");
			}

			@Override
			public void setTaskName(String name) {
				name = name.trim();
				if (name == null || name.equalsIgnoreCase("null")) {
					name = "";
				}
				this.name = name;
			}

			@Override
			public void setCanceled(boolean value) {
				this.canceled = value;
			}

			@Override
			public boolean isCanceled() {
				return this.canceled;
			}

			@Override
			public void internalWorked(double work) {
				// TODO Auto-generated method stub

			}

			@Override
			public void done() {
				if (this.name.isEmpty()) {
					LOGGER.info("100% completed");
				} else {
					LOGGER.info(this.name + ": 100% completed");
				}
				LOGGER.info("Task " + this.name + " done");
			}

			@Override
			public void beginTask(String name, int totalWork) {
				if (name == null) {
					name = "";
				}
				this.name = name;
				LOGGER.info("Starting task " + name + "...");
				if (totalWork > 0) {
					this.totalWork = totalWork;
				}

			}
		};
	}

	public static void refreshArtifactRepositories(URI uri, BundleContext context) throws ProvisionException {

		IProvisioningAgent agent = getAgent(context);

		if (agent != null) {
			IAgentLocation agentLocation = (IAgentLocation) agent.getService(IAgentLocation.SERVICE_NAME);
			IArtifactRepositoryManager manager = (IArtifactRepositoryManager) agent.getService(IArtifactRepositoryManager.SERVICE_NAME);
			URI locationUri = agentLocation.getDataArea("org.eclipse.equinox.p2.repository");
			locationUri = URIUtil.append(locationUri, "cache/");
			try {
				manager.refreshRepository(locationUri, getDefaultMonitor());
			} catch (ProvisionException e) {
				System.out.println("Warn: Could not refresh repository, because there is no one!");
			}

		} else {
			throw new ProvisionException("No repository manager found");
		}
	}

}
