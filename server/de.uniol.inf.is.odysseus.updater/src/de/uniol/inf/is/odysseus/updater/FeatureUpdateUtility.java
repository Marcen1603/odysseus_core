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
import org.eclipse.equinox.p2.operations.ProfileChangeOperation;
import org.eclipse.equinox.p2.operations.ProvisioningJob;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.operations.UninstallOperation;
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

import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UpdatePermission;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.PermissionException;

public class FeatureUpdateUtility {

	private static Logger LOG = LoggerFactory.getLogger(FeatureUpdateUtility.class);
	private static final InfoService INFO = InfoServiceFactory.getInfoService(FeatureUpdateUtility.class);

	private static URI DEFAULT_REPOSITORY_LOC;
	private static URI current_repository_location;

	static {
		try {
			DEFAULT_REPOSITORY_LOC = new URI(OdysseusConfiguration.instance.get(OdysseusConfiguration.DEFAULT_UPDATE_SITE,
					"http://odysseus.informatik.uni-oldenburg.de/update/"));
		} catch (final URISyntaxException e) {
			LOG.error("URI invalid: " + e.getMessage());
		}
	}

	public static final URI getRepositoryLocation() {
		if (current_repository_location == null) {
			return DEFAULT_REPOSITORY_LOC;
		} 
		return current_repository_location;
	}

	public static final void setRepositoryLocation(String url, final ISession caller) {
		if (UserManagementProvider.instance.getUsermanagement(true).hasPermission(caller, UpdatePermission.CONFIGURE,
				UpdatePermission.objectURI)) {

			try {
				current_repository_location = new URI(url);

			} catch (final URISyntaxException e) {
				LOG.error("URI invalid: " + e.getMessage());
			}
		} else {
			throw new PermissionException("This user is not allowed to change update site!");
		}
	}

	public static final void clearRepositoryLocation(final ISession caller) {
		if (UserManagementProvider.instance.getUsermanagement(true).hasPermission(caller, UpdatePermission.CONFIGURE,
				UpdatePermission.objectURI)) {
			current_repository_location = null;
		} else {
			throw new PermissionException("This user is not allowed to change update site!");
		}
	}

	public static IStatus uninstallFeature(String id, final ISession caller) {
		if (UserManagementProvider.instance.getUsermanagement(true).hasPermission(caller, UpdatePermission.REMOVE,
				UpdatePermission.objectURI)) {
			List<IInstallableUnit> units = getInstalledFeatures(caller);
			Collection<IInstallableUnit> toUninstall = new ArrayList<IInstallableUnit>();
			for (IInstallableUnit u : units) {
				LOG.info("CHECK " + u.getId());
				if (u.getId().startsWith(id)) {
					toUninstall.add(u);
				}
			}
			if (toUninstall.size() == 0) {
				LOG.error("Feature " + id + " not found");
				return Status.CANCEL_STATUS;
			}
			BundleContext context = Activator.getContext();
			IProvisioningAgent agent = getAgent(context);
			final ProvisioningSession session = new ProvisioningSession(agent);
			final UninstallOperation operation = new UninstallOperation(session, toUninstall);
			return runOperation(caller, operation);

		}
		throw new PermissionException("This user is not allowed to remove features!");
	}

	public static IStatus installFeature(String ids, final ISession caller) {
		if (UserManagementProvider.instance.getUsermanagement(true).hasPermission(caller, UpdatePermission.INSTALL,
				UpdatePermission.objectURI)) {
			
			LOG.info("Trying to install "+ids);
			
			String[] idArray = ids.split(",");
			List<IInstallableUnit> units = new ArrayList<>();
			for (String id: idArray) {
				LOG.info("Trying to find "+id);
				units.addAll(getInstallableUnits(id, caller));
			}
			
			if (units != null && !units.isEmpty()) {
				LOG.info("Found following features that will be installed now: ");
				for (IInstallableUnit unit : units) {
					LOG.info("\t" + unit.getId());
				}
				BundleContext context = Activator.getContext();
				IProvisioningAgent agent = getAgent(context);
				final ProvisioningSession session = new ProvisioningSession(agent);
				final InstallOperation operation = new InstallOperation(session, units);

				// set location of artifact and metadata repo

				operation.getProvisioningContext().setArtifactRepositories(new URI[] { getRepositoryLocation() });
				operation.getProvisioningContext().setMetadataRepositories(new URI[] { getRepositoryLocation() });
				return runOperation(caller, operation);

			}
			LOG.error("There is no download with this feature id "+ids);
			return Status.CANCEL_STATUS;

		}
		throw new PermissionException("Sorry, this user is not allowed to install new features!");
	}

	private static IStatus runOperation(final ISession caller, final ProfileChangeOperation operation) {
		LOG.info("Starting install process...");
		IStatus status = operation.resolveModal(getDefaultMonitor());
		if (status.isOK()) {
			final ProvisioningJob provisioningJob = operation.getProvisioningJob(getDefaultMonitor());
			// updates cannot run from within Eclipse IDE!!!
			if (provisioningJob == null) {
				LOG.error("Running update from within Eclipse IDE? This won't work!!! Use exported product!");
				LOG.error("Resolution result: " + operation.getResolutionResult().getMessage());
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
							LOG.info(
									"Features were un/installed. You have to restart Odysseus for the changed to take effekt!");
							restart(caller);
						}

					}
					super.done(event);
				}
			});

			provisioningJob.schedule();

			return Status.OK_STATUS;
		}
		LOG.error(status.getMessage());
		return Status.CANCEL_STATUS;
	}

	private static List<IInstallableUnit> getInstallableUnits(String id, ISession caller) {
		if (UserManagementProvider.instance.getUsermanagement(true).hasPermission(caller, UpdatePermission.LIST,
				UpdatePermission.objectURI)) {
			BundleContext context = Activator.getContext();
			IProvisioningAgent agent = getAgent(context);
			IMetadataRepositoryManager metadataManager = (IMetadataRepositoryManager) agent
					.getService(IMetadataRepositoryManager.SERVICE_NAME);
			try {
				IMetadataRepository repo = metadataManager.loadRepository(getRepositoryLocation(), getDefaultMonitor());
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
			} catch (ProvisionException e) {
				e.printStackTrace();
			} catch (OperationCanceledException e) {
				e.printStackTrace();
			}
			return null;
		}

		throw new PermissionException("User is not allowed to list the installed features!");
	}

	public static boolean isFeatureInstalled(String id, ISession caller) {
		if (UserManagementProvider.instance.getUsermanagement(true).hasPermission(caller, UpdatePermission.LIST,
				UpdatePermission.objectURI)) {
			List<IInstallableUnit> units = getInstalledFeatures(caller);
			for (IInstallableUnit unit : units) {
				if (unit.getId().startsWith(id)) {
					return true;
				}
			}
			return false;
		}

		throw new PermissionException("User is not allowed to list the installed features!");
	}

	public static List<IInstallableUnit> getInstalledFeatures(ISession caller) {
		if (UserManagementProvider.instance.getUsermanagement(true).hasPermission(caller, UpdatePermission.LIST,
				UpdatePermission.objectURI)) {
			BundleContext context = Activator.getContext();
			IProvisioningAgent agent = getAgent(context);
			IProfileRegistry regProfile = (IProfileRegistry) agent.getService(IProfileRegistry.SERVICE_NAME);
			IProfile profileSelf = regProfile.getProfile(IProfileRegistry.SELF);

			if (profileSelf == null) {
				INFO.warning("Could not create profile!");
				return null;
			}

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
		}

		throw new PermissionException("This user may not list the installed features!");

	}

	public static List<IInstallableUnit> getInstallableFeatures(ISession caller) {
		if (UserManagementProvider.instance.getUsermanagement(true).hasPermission(caller, UpdatePermission.LIST,
				UpdatePermission.objectURI)) {
			BundleContext context = Activator.getContext();
			IProvisioningAgent agent = getAgent(context);
			IMetadataRepositoryManager metadataManager = (IMetadataRepositoryManager) agent
					.getService(IMetadataRepositoryManager.SERVICE_NAME);
			try {
				IMetadataRepository repo = metadataManager.loadRepository(getRepositoryLocation(), getDefaultMonitor());
				IQueryResult<IInstallableUnit> units = repo.query(QueryUtil.createIUGroupQuery(), getDefaultMonitor());
				List<IInstallableUnit> installable = new ArrayList<>();
				String id = ".feature.group";
				List<IInstallableUnit> alreadyInstalled = getInstalledFeatures(caller);
				for (IInstallableUnit unit : units.toSet()) {
					// use starts with to ignore version and qualifier
					String unitid = unit.getId().toLowerCase();

					if (unitid.contains(id) && unitid.startsWith("de.uniol.inf.is")
							&& !unitid.contains("source.feature")) {
						if (!containsWithSameID(alreadyInstalled, unit)) {
							installable.add(unit);
						}
					}
				}
				Collections.sort(installable);
				return installable;
			} catch (ProvisionException e) {
				e.printStackTrace();
			} catch (OperationCanceledException e) {
				e.printStackTrace();
			}
			return null;
		}

		throw new PermissionException("This user may not list installable features!");
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
		if (UserManagementProvider.instance.getUsermanagement(true).hasPermission(caller, UpdatePermission.UPDATE,
				UpdatePermission.objectURI)) {
			try {
				LOG.info("Looking for updates on " + getRepositoryLocation());
				BundleContext context = Activator.getContext();
				IProvisioningAgent agent = getAgent(context);
				IProgressMonitor monitor = getDefaultMonitor();

				final ProvisioningSession session = new ProvisioningSession(agent);
				final UpdateOperation operation = new UpdateOperation(session);

				refreshArtifactRepositories(getRepositoryLocation(), context, caller);
				operation.getProvisioningContext().setArtifactRepositories(new URI[] { getRepositoryLocation() });
				operation.getProvisioningContext().setMetadataRepositories(new URI[] { getRepositoryLocation() });
				final IStatus status = operation.resolveModal(monitor);

				// failed to find updates (inform user and exit)
				if (!status.isOK()) {
					LOG.error(status.getMessage());
					return false;
				}
				if (status.isOK() && status.getSeverity() != IStatus.ERROR) {

					Update[] possibleUpdates = operation.getPossibleUpdates();
					if (possibleUpdates.length > 0) {
						String updates = "";
						for (Update update : possibleUpdates) {
							updates += update + "\n";
						}
						LOG.info("Following updates found: \n" + updates);

						return true;
					}
					LOG.info("No updates found.");
					return false;
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

		if (UserManagementProvider.instance.getUsermanagement(true).hasPermission(caller, UpdatePermission.UPDATE,
				UpdatePermission.objectURI)) {

			try {
				LOG.info("Looking for updates on " + getRepositoryLocation());

				BundleContext context = Activator.getContext();
				IProvisioningAgent agent = getAgent(context);
				IProgressMonitor monitor = getDefaultMonitor();

				final ProvisioningSession session = new ProvisioningSession(agent);

				final UpdateOperation operation = new UpdateOperation(session);

				refreshArtifactRepositories(getRepositoryLocation(), context, caller);
				operation.getProvisioningContext().setArtifactRepositories(new URI[] { getRepositoryLocation() });
				operation.getProvisioningContext().setMetadataRepositories(new URI[] { getRepositoryLocation() });

				final IStatus status = operation.resolveModal(monitor);

				if (status.isOK() && status.getSeverity() != IStatus.ERROR) {
					final ProvisioningJob provisioningJob = operation.getProvisioningJob(getDefaultMonitor());
					// updates cannot run from within Eclipse IDE!!!
					if (provisioningJob == null) {
						LOG.error("Running update from within Eclipse IDE? This won't work!!! Use exported product!");
						throw new NullPointerException();
					}

					// register a job change listener to track
					// installation progress and notify user upon success
					provisioningJob.addJobChangeListener(new JobChangeAdapter() {
						@Override
						public void scheduled(IJobChangeEvent event) {
							LOG.info("New update job started");
							super.scheduled(event);
						}

						@Override
						public void done(IJobChangeEvent event) {
							if (event.getResult().isOK()) {
								boolean restart = true;
								if (restart) {
									LOG.info(
											"Updates were installed. You have to restart Odysseus for the changed to take effekt!");
									restart(caller);
								}
							} else {
								LOG.error("No update where installed " + event.getResult().getException());
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
		throw new PermissionException("User is not allowed to update the system!");
	}

	public static String getVersionNumberFromFeatures(ISession caller) {
		if (UserManagementProvider.instance.getUsermanagement(true).hasPermission(caller, UpdatePermission.LIST,
				UpdatePermission.objectURI)) {
			List<IInstallableUnit> units = getInstalledFeatures(caller);
			for (IInstallableUnit unit : units) {
				if (unit.getId().toLowerCase().startsWith("de.uniol.inf.is.odysseus.core")) {
					return unit.getVersion().toString();
				}
			}
		} else {
			throw new PermissionException("This user may not list the installed features!");
		}
		return "-1";
	}

	public static String getVersionNumber(ISession caller) {
		if (UserManagementProvider.instance.getUsermanagement(true).hasPermission(caller, UpdatePermission.LIST,
				UpdatePermission.objectURI)) {
			return Activator.getContext().getBundle().getVersion().toString();
		} 
		throw new PermissionException("This user may not list the installed features!");
	}

	private static IProvisioningAgent getAgent(BundleContext context) {
		IProvisioningAgent agent = null;

		ServiceReference<?> reference = context.getServiceReference(IProvisioningAgent.SERVICE_NAME);
		if (reference != null) {
			agent = (IProvisioningAgent) context.getService(reference);
			context.ungetService(reference);
		}
		if (agent == null) {
			LOG.error("No provisioning agent found.  This application is not set up for updates.");
		}
		return agent;
	}

	public static void restart(ISession caller) {
		if (UserManagementProvider.instance.getUsermanagement(true).hasPermission(caller, UpdatePermission.INSTALL,
				UpdatePermission.objectURI)) {
			BundleContext context = Activator.getContext();
			ServiceReference<?> eventAdminServiceReference = context.getServiceReference(EventAdmin.class.getName());

			// EventAdmin Service lookup
			final EventAdmin eventAdmin = (EventAdmin) context.getService(eventAdminServiceReference);

			Executors.newSingleThreadExecutor().execute(new Runnable() {

				@Override
				public void run() {
					LOG.info("Sending restart event");
					Map<String, String> hashMap = new HashMap<>();
					hashMap.put("TYPE", "RESTART");
					eventAdmin.sendEvent(
							new Event("de/uniol/inf/odysseus/application/" + System.currentTimeMillis(), hashMap));
				}
			});
		} else {
			throw new PermissionException("This user may not restart the system!");
		}
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
					LOG.info(percent + "% completed");
				} else {
					LOG.info(this.name + ": " + percent + "% completed");
				}
			}

			@Override
			public void subTask(String subname) {
				LOG.info(this.name + ": " + subname + "...");
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
			}

			@Override
			public void done() {
				if (this.name.isEmpty()) {
					LOG.info("100% completed");
				} else {
					LOG.info(this.name + ": 100% completed");
				}
				LOG.info("Task " + this.name + " done");
			}

			@Override
			public void beginTask(String name, int totalWork) {
				if (name == null) {
					name = "";
				}
				this.name = name;
				LOG.info("Starting task " + name + "...");
				if (totalWork > 0) {
					this.totalWork = totalWork;
				}

			}
		};
	}

	public static void refreshArtifactRepositories(URI location, BundleContext context, ISession caller)
			throws ProvisionException {
		if (UserManagementProvider.instance.getUsermanagement(true).hasPermission(caller, UpdatePermission.LIST,
				UpdatePermission.objectURI)) {
			IProvisioningAgent agent = getAgent(context);

			if (agent != null) {
				LOG.info("Reloading artifact repository...");
				IArtifactRepositoryManager manager = (IArtifactRepositoryManager) agent
						.getService(IArtifactRepositoryManager.SERVICE_NAME);
				manager.loadRepository(location, new NullProgressMonitor());
				manager.refreshRepository(location, new NullProgressMonitor());
				LOG.info("Reloading metadata repository...");
				IMetadataRepositoryManager metadataManager = (IMetadataRepositoryManager) agent
						.getService(IMetadataRepositoryManager.SERVICE_NAME);
				metadataManager.loadRepository(location, new NullProgressMonitor());
				metadataManager.refreshRepository(location, new NullProgressMonitor());
				LOG.info("Repositories refreshed");
			} else {
				throw new ProvisionException("No repository manager found");
			}
		} else {
			throw new PermissionException("User is not allowed to update the system!");
		}
	}
}
