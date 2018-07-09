package de.uniol.inf.is.odysseus.planmanagement.executor.standardexecutor.statehandling;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.physicaloperator.IOperatorState;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IStatefulPO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.IPlanModificationListener;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.AbstractPlanModificationEvent;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.eventhandling.planmodification.event.PlanModificationEventType;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.util.OsgiObjectInputStream;

/**
 * This is experimental!
 * 
 * @author cornelius
 *
 */
public class HandleStatePlanModificationListener implements IPlanModificationListener {

	private static Logger _logger = LoggerFactory.getLogger(HandleStatePlanModificationListener.class);
	private String path = System.getProperty("user.home") + "/.odysseus/operatorstates/";

	public HandleStatePlanModificationListener() {
		(new File(path)).mkdirs();
		_logger.info("Saving operator states in: " + path);
	}

	@Override
	public void planModificationEvent(AbstractPlanModificationEvent<?> eventArgs) {
		IPhysicalQuery query = (IPhysicalQuery) eventArgs.getValue();
		// ISession caller = query.getSession();
		Resource queryName = query.getName();
		// int queryId = query.getID();
		switch ((PlanModificationEventType) eventArgs.getEventType()) {
		case QUERY_REMOVE:
			_logger.debug("Query " + queryName + " remove.");
			saveState(query, "query remove");
			break;
		case QUERY_START:
			_logger.debug("Query " + queryName + " start.");
			loadState(query);
			break;
		case QUERY_PARTIAL:

			break;
		case QUERY_STOP:
			_logger.debug("Query " + queryName + " stop.");
			saveState(query, "query stop");
			break;
		case QUERY_SUSPEND:

			break;
		case QUERY_RESUME:

			break;
		default:
			/*
			 * Nothing to do for: PLAN_REOPTIMIZE, QUERY_ADDED, QUERY_REOPTIMIZE
			 */
			return;
		}

	}

	private void saveState(IPhysicalQuery query, String reason) {
		Set<IPhysicalOperator> saveOperators = saveOperators(query);
		for (IPhysicalOperator op : saveOperators) {
			if (op instanceof IStatefulPO) {
				IStatefulPO statefulPo = (IStatefulPO) op;
				IOperatorState state = statefulPo.getState();
				Set<String> ids = op.getUniqueIds().values().stream()
						.filter(res -> res.getResourceName().startsWith("save:"))
						.map(res -> res.getResourceName().replaceAll(":", "_")).collect(Collectors.toSet());
				_logger.info("Saving state of the following operators: " + ids + " (reason: " + reason + ")");
				for (String id : ids) {
					try (FileOutputStream fos = new FileOutputStream(
							path + id + "_" + System.currentTimeMillis() + ".bin");
							ObjectOutputStream oos = new ObjectOutputStream(fos)) {
						oos.writeObject(state.getSerializedState());
						_logger.info("State for operator '" + id + "' saved.");
					} catch (FileNotFoundException e) {
						_logger.error("Saving state failed for operator: " + op + " (" + id + ")", e);
					} catch (IOException e) {
						_logger.error("Saving state failed for operator: " + op + " (" + id + ")", e);
					}
				}
			}
		}
	}

	private void loadState(IPhysicalQuery query) {
		Set<IPhysicalOperator> saveOperators = saveOperators(query);
		for (IPhysicalOperator op : saveOperators) {
			if (op instanceof IStatefulPO) {
				IStatefulPO statefulPo = (IStatefulPO) op;

				Serializable state = null;

				Set<String> ids = op.getUniqueIds().values().stream()
						.filter(res -> res.getResourceName().startsWith("save:"))
						.map(res -> res.getResourceName().replaceAll(":", "_")).collect(Collectors.toSet());
				_logger.info("Loading state of the following operators: " + ids);
				for (String id : ids) {
					String file = getStateFile(id);
					if (file != null && !file.isEmpty()) {
						try (FileInputStream fos = new FileInputStream(file);
								OsgiObjectInputStream oos = new OsgiObjectInputStream(fos)) {
							Object object = oos.readObject();
							state = (Serializable) object;
							if (state != null) {
								statefulPo.setState(state);
								_logger.info("State for operator '" + id + "' loaded.");
							} else
								_logger.warn("State for operator '" + id + "' is null.");
						} catch (IOException e) {
							_logger.error("Loading state failed for operator: " + op + " (" + id + ")", e);
						} catch (ClassNotFoundException e) {
							_logger.error("Loading state failed for operator: " + op + " (" + id + ")", e);
						}

					} else {
						_logger.info("No state file for operator '" + id + "'.");
					}
				}
			}
		}

	}

	private Set<IPhysicalOperator> saveOperators(IPhysicalQuery query) {
		Set<IPhysicalOperator> operators = query.getAllOperators();

		return operators.stream().filter(
				op -> op.getUniqueIds().values().stream().anyMatch(res -> res.getResourceName().startsWith("save:")))
				.collect(Collectors.toSet());
	}

	private String getStateFile(String id) {
		File dir = new File(path);
		File[] files = dir.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.startsWith(id + "_");
			}
		});
		String result = "";
		long lastTs = 0;
		for (File file : files) {
			String name = file.getName();
			String timestamp = name.substring(id.length() + 1, name.length() - 4);
			long ts = Long.parseLong(timestamp);
			if (ts > lastTs) {
				lastTs = ts;
				result = file.getAbsolutePath();
			}
		}
		return result;
	}

}