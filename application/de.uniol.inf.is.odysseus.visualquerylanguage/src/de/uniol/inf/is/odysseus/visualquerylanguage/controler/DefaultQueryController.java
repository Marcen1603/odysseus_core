package de.uniol.inf.is.odysseus.visualquerylanguage.controler;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.visualquerylanguage.model.query.DefaultQuery;

public class DefaultQueryController implements IQueryController<DefaultQuery>{
	
	private static final Logger log = LoggerFactory.getLogger(DefaultQueryController.class);
	
	private Collection<DefaultQuery> queries;
	
	public DefaultQueryController() {
		this.queries = new ArrayList<DefaultQuery>();
	}

	@Override
	public Collection<DefaultQuery> getAllQueries() {
		return this.queries;
	}

	@Override
	public void addQuery(DefaultQuery query) {
		this.queries.add(query);
	}

	@Override
	public DefaultQuery getQueryById(int id) {
		for (DefaultQuery query : this.queries) {
			if(query.getId() == id) {
				return query;
			}
		}
		return null;
	}

	@Override
	public void removeQuery(DefaultQuery query) {
		for (DefaultQuery dQuery : this.queries) {
			if(dQuery == query) {
				this.queries.remove(query);
				break;
			}else {
				log.info("Query not in list.");
			}
		}
	}

}
