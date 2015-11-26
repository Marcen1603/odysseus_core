package de.uniol.inf.is.odysseus.net.data.impl;

import java.util.Collection;
import java.util.UUID;

import org.json.JSONObject;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.net.data.IDistributedData;
import de.uniol.inf.is.odysseus.net.data.IDistributedDataManager;

public class DistributedDataManager implements IDistributedDataManager {

	// called by OSGi-DS
	public void activate() {

	}

	// called by OSGi-DS
	public void deactivate() {

	}

	@Override
	public IDistributedData create(JSONObject data, String name, boolean persistent, long lifetime) {
		return null;
	}

	@Override
	public IDistributedData create(JSONObject data, String name, boolean persistent) {
		return null;
	}

	@Override
	public Optional<IDistributedData> destroy(UUID uuid) {
		return null;
	}

	@Override
	public Collection<IDistributedData> destory(String name) {
		return null;
	}

	@Override
	public Collection<UUID> getUUIDs() {
		return null;
	}

	@Override
	public Collection<String> getNames() {
		return null;
	}

	@Override
	public Optional<IDistributedData> get(UUID uuid) {
		return null;
	}

	@Override
	public Collection<IDistributedData> get(String name) {
		return null;
	}

	@Override
	public boolean containsUUID(UUID uuid) {
		return false;
	}

	@Override
	public boolean containsName(String name) {
		return false;
	}

}
