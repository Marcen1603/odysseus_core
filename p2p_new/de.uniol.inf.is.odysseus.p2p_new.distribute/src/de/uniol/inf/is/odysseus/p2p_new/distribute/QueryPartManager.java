package de.uniol.inf.is.odysseus.p2p_new.distribute;

import java.util.Collection;
import java.util.List;

import net.jxta.document.Advertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.IQueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration.ParameterDoRewrite;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.IQueryBuildSetting;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementListener;
import de.uniol.inf.is.odysseus.p2p_new.IAdvertisementManager;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.P2PDictionaryService;
import de.uniol.inf.is.odysseus.p2p_new.distribute.service.SessionManagementService;

public class QueryPartManager implements IAdvertisementListener {

	private static final Logger LOG = LoggerFactory.getLogger(QueryPartManager.class);

	private static QueryPartManager instance;

	private IServerExecutor executor;

	public QueryPartManager() {
		instance = this;
	}

	@Override
	public void advertisementAdded(IAdvertisementManager sender, Advertisement advertisement) {
		if (advertisement instanceof QueryPartAdvertisement) {
			final QueryPartAdvertisement adv = (QueryPartAdvertisement) advertisement;
			if (adv.getPeerID().equals(P2PDictionaryService.get().getLocalPeerID())) {
				try {
					final List<IQueryBuildSetting<?>> configuration = determineQueryBuildSettings(executor, adv.getTransCfgName());
					final Collection<Integer> ids = executor.addQuery(adv.getPqlStatement(), "PQL", SessionManagementService.getActiveSession(), adv.getTransCfgName(), configuration);

					QueryPartController.getInstance().registerAsSlave(ids, adv.getSharedQueryID());

				} catch (final Throwable t) {
					LOG.error("Could not execute query part", t);
				}
			}
		}
	}

	@Override
	public void advertisementRemoved(IAdvertisementManager sender, Advertisement adv) {
		// do nothing
	}

	// called by OSGi-DS
	public void bindExecutor(IExecutor exe) {
		if (exe instanceof IServerExecutor) {
			executor = (IServerExecutor) exe;

			LOG.debug("Bound ServerExecutor {}", exe);
		} else {
			throw new IllegalArgumentException("Executor " + exe + " is not a ServerExecutor");
		}
	}

	// called by OSGi-DS
	public void unbindExecutor(IExecutor exe) {
		if (executor == exe) {
			LOG.debug("Unbound Executor {}", exe);

			executor = null;
		}
	}

	public static QueryPartManager getInstance() {
		return instance;
	}

	private static List<IQueryBuildSetting<?>> determineQueryBuildSettings(IServerExecutor executor, String cfgName) {
		final IQueryBuildConfiguration qbc = executor.getQueryBuildConfiguration(cfgName);
		final List<IQueryBuildSetting<?>> configuration = qbc.getConfiguration();

		final List<IQueryBuildSetting<?>> settings = Lists.newArrayList();
		settings.addAll(configuration);
		settings.add(ParameterDoRewrite.FALSE);
		return settings;
	}
	
}
