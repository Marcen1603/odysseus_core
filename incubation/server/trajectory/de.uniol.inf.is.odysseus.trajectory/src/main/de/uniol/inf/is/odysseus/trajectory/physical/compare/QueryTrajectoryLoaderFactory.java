package de.uniol.inf.is.odysseus.trajectory.physical.compare;

import java.util.Map;

import org.javatuples.Pair;

import de.uniol.inf.is.odysseus.trajectory.util.AbstractObjectLoaderFactory;

public class QueryTrajectoryLoaderFactory extends AbstractObjectLoaderFactory<IQueryTrajectoryLoader, RawQueryTrajectory, String, Pair<Integer, Map<String, String>>>{

	private final static QueryTrajectoryLoaderFactory INSTANCE = new QueryTrajectoryLoaderFactory();
	
	public static QueryTrajectoryLoaderFactory getInstance() {
		return INSTANCE;
	}
	
	private QueryTrajectoryLoaderFactory() { }
	
	
	@Override
	protected String convertKey(String key) {
		return key.substring(key.lastIndexOf('.') + 1).toUpperCase();
	}

	@Override
	protected IQueryTrajectoryLoader createLoader(String convertedKey) {
		switch(convertedKey) {
			case "CSV" : return new CsvQueryTrajectoryLoader();
		}
		
		return null;
	}

}
