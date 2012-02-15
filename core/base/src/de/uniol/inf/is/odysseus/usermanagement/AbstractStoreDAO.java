package de.uniol.inf.is.odysseus.usermanagement;

import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.store.IStore;

public class AbstractStoreDAO<T extends IAbstractEntity> implements IGenericDAO<T, String> {

	final IStore<String, T> entities;
	final List<T> allEntities;
	
	public AbstractStoreDAO(IStore<String, T> entities, List<T> allEntities) {
		this.entities = entities;
		this.allEntities = allEntities;
	}
	
	@Override
	public T create(T entity) {
		entities.put(entity.getId(), entity);
		allEntities.add(entity);
		return entity;
	}

	@Override
	public void delete(T entity) {
		entities.remove(entity.getId());
		allEntities.remove(entity);	}

	@Override
	public T find(String id) {
		return entities.get(id);
	}

	@Override
	public T findByName(String name) {
		return entities.get(name);
	}
	
	@Override
	public List<T> findAll() {
		if (allEntities.size() != entities.entrySet().size()){
			allEntities.clear();
			for (Entry<String, T> e:entities.entrySet()){
				allEntities.add(e.getValue());
			}
		}
		return Collections.unmodifiableList(allEntities);

	}

	@Override
	public void update(T entity) {
		T toUpdate = entities.get(entity.getId());
		// Do not update if its the same instance!
		if (toUpdate != entity){
			toUpdate.update(entity);	
		}
		entities.commit();
	}

}
