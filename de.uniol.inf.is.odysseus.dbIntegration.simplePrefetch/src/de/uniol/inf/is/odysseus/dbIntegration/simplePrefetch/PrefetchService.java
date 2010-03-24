package de.uniol.inf.is.odysseus.dbIntegration.simplePrefetch;

import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import org.osgi.service.component.ComponentContext;

import de.uniol.inf.is.odysseus.dbIntegration.exceptions.PrefetchException;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBResult;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.ICache;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.IDataAccess;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.IPrefetch;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;


/**
 * Diese Klasse implementiert das Interface IPrefetch des Datenbankframeworks und
 * stellt den entsprechenden Service bereit.
 * 
 * @author crolfes
 *
 */
public class PrefetchService implements IPrefetch {
	
	HashMap<DBQuery, PrefetchQueryLearner> queries = new HashMap<DBQuery, PrefetchQueryLearner>();
	private ResourceBundle defaults;
	private String MAXOBJECTS = "maxClassifyingObjs";
	private String PREVISIONWIDTH = "previsionWidth";
	private String PREVISIONDEPTH = "previsionDepth";
	
	private int maxClassifyingObjs = 100;
	private int previsionWidth = 1;
	private int previsionDepth = 0;
	
	@Override
	public void addDataStreamTuple(DBQuery dbQuery, RelationalTuple<?> streamTuple) {
		queries.get(dbQuery).addInputTupel(streamTuple);		
	}

	@Override
	public void addQuery(DBQuery dbQuery, List<String> queryPrefs,
			IDataAccess dataAccess, SDFAttributeList inputSchema, ICache cache) {
		try {
			if (queryPrefs != null) {
				for (String string : queryPrefs) {
					if (string.startsWith(MAXOBJECTS)) {
						string = string.replaceAll(MAXOBJECTS, "").trim();
						int temp = Integer.valueOf(string);
						if (temp >= 1) {
							maxClassifyingObjs = temp;
						}
					} else if (string.startsWith(PREVISIONWIDTH)) {
						string = string.replaceAll(PREVISIONWIDTH, "").trim();
						int temp = Integer.valueOf(string);
						if (temp >= 1) {
							previsionWidth = temp;
						}
					} else if (string.startsWith(PREVISIONDEPTH)) {
						string = string.replaceAll(PREVISIONDEPTH, "").trim();
						int temp = Integer.valueOf(string);
						if (temp >= 0) {
							previsionDepth = temp;
						}
					}
					
				}
			}
		} catch (NumberFormatException e) {
			
		}
		PrefetchQueryLearner newQuery = new PrefetchQueryLearner(dbQuery, maxClassifyingObjs, inputSchema, dataAccess, cache);
		newQuery.setPrevisionWidth(previsionWidth);
		newQuery.setPrevisionDepth(previsionDepth);
		queries.put(dbQuery, newQuery);
	}

	@Override
	public void closeQuery(DBQuery dbQuery) {
		queries.remove(dbQuery);
	}

	@Override
	public List<RelationalTuple<?>> getData(RelationalTuple<?> streamTuple,
			DBQuery dbQuery) throws PrefetchException {
		
		PrefetchQueryLearner query = queries.get(dbQuery);
		
		List<RelationalTuple<?>> tuples = query.getNextTuples(streamTuple);
		if (tuples == null) {
			return null;
		}
		
		DBResult resultTuples = query.getDataAccess().executeBaseQuery(tuples.remove(0));
		query.getCache().addData(streamTuple, tuples, dbQuery);
		
		CacheInserter insertCache = new CacheInserter(dbQuery, tuples, query.getCache(), query.getDataAccess());
		insertCache.start();
		
		if (resultTuples == null) {
			return null;
		} else {
			return resultTuples.getResult();
		}
	}
	
	protected void activate(ComponentContext componentContext) {
		try {
			defaults = ResourceBundle.getBundle("defaults");
			
			try {
				maxClassifyingObjs = Integer.parseInt(defaults.getString(MAXOBJECTS));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				previsionWidth = Integer.parseInt(defaults.getString(PREVISIONWIDTH));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			try {
				previsionDepth = Integer.parseInt(defaults.getString(PREVISIONDEPTH));
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			
		}
	}

	

}
