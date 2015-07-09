package de.uniol.inf.is.odysseus.recovery.querystate;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.recovery.IRecoveryComponent;
import de.uniol.inf.is.odysseus.recovery.querystate.queryaddedInfo.AbstractQueryAddedInfo;
import de.uniol.inf.is.odysseus.recovery.systemlog.ISysLogEntry;

// TODO javaDoc
public class QueryStateRecoveryComponent implements IRecoveryComponent {

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory
			.getLogger(QueryStateRecoveryComponent.class);

	private static ImmutableSet<String> cTags;

	static {
		generateTags();
	}

	private static void generateTags() {
		Set<String> tags = Sets.newHashSet();
		for (QueryState queryState : QueryState.values()) {
			tags.add(queryState.toString());
		}
		tags.add(QueryStateUtils.TAG_QUERYADDED);
		tags.add(QueryStateUtils.TAG_QUERYSTATECHANGED);
	}

	/**
	 * The executor, if bound.
	 */
	private static Optional<IServerExecutor> cExecutor;

	/**
	 * Binds an implementation of the executor.
	 * 
	 * @param executor
	 *            The implementation to bind.
	 */
	public void bindExecutor(IExecutor executor) {
		IServerExecutor serverExecutor = (IServerExecutor) executor;
		cExecutor = Optional.of(serverExecutor);
	}

	/**
	 * Unbinds an implementation of the executor.
	 * 
	 * @param executor
	 *            The implementation to unbind.
	 */
	public void unbindExecutor(IExecutor executor) {
		if (cExecutor.isPresent() && cExecutor.get() == executor) {
			cExecutor = Optional.absent();
		}

	}

	@Override
	public String getName() {
		return "QueryStateRecoveryComponent";
	}

	/**
	 * No dependencies.
	 */
	@Override
	public ImmutableCollection<String> getDependencies() {
		Set<String> dependencies = Sets.newHashSet();
		return ImmutableSet.copyOf(dependencies);
	}

	@Override
	public void recover(List<ISysLogEntry> log) throws Exception {
		if(!cExecutor.isPresent()) {
			cLog.error("No executorbound!");
			return;
		}
		cLog.debug("Begin of recovery...");
		for (ISysLogEntry entry : log) {
			if (cTags.contains(entry.getTag())) {
				cLog.debug("Try to recover {}", entry);
				// TODO recover
				if(entry.getTag().equals(QueryStateUtils.TAG_QUERYADDED)) {
					if(!entry.getComment().isPresent()) {
						cLog.error("No query text found!");
						continue;
					}
					AbstractQueryAddedInfo info = AbstractQueryAddedInfo.fromBase64Binary(entry.getComment().get());
//					cExecutor.get().addQuery(info.queryText, info.parserId, info.session, info.buildConfiguration, context)
				}
			}
		}
	}

}