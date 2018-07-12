package de.uniol.inf.is.odysseus.core.server.datadictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;

import de.uniol.inf.is.odysseus.core.datatype.IDatatypeProvider;

public class DataDictionaryProvider {
	public static DataDictionaryProvider instance;

	private List<IDatatypeProvider> datatypeProvider = new ArrayList<>();
	private Map<ITenant, IDataDictionary> datadictionary = new HashMap<ITenant, IDataDictionary>();
	private List<String> allDatatypeNames = new LinkedList<>();
	private Map<IDatadictionaryProviderListener, ITenant> ddPlistener = new HashMap<>();
	private UserManagementProvider userManagementProvider;

	public synchronized void bindDataDictionary(IDataDictionary bindDD) {
		// Is dd the right type
		List<ITenant> tenants = userManagementProvider.getTenants();
		for (ITenant t : tenants) {
			IDataDictionary dd = bindDD.createInstance(t);
			// Default Datatypes
			addDatatypes(dd, SDFDatatype.getTypes());
			for (IDatatypeProvider p : datatypeProvider) {
				addDatatypes(dd, p.getDatatypes());
			}
			datadictionary.put(t, dd);
			fire(t, dd, true);
		}
	}

	public synchronized void unbindDataDictionary(IDataDictionary bb) {
		Iterator<Entry<ITenant, IDataDictionary>> iter = datadictionary.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<ITenant, IDataDictionary> e = iter.next();
			for (IDatatypeProvider p : datatypeProvider) {
				removeDatatypes(e.getValue(), p.getDatatypes());
			}
			iter.remove();
			fire(e.getKey(), e.getValue(), false);

		}

		datadictionary.clear();
	}

	public synchronized void bindDatatypeProvider(IDatatypeProvider provider) {
		if (!datatypeProvider.contains(provider)) {
			datatypeProvider.add(provider);
			for (IDataDictionary dd : datadictionary.values()) {
				addDatatypes(dd, provider.getDatatypes());
			}
		}
	}

	public synchronized void unbindDatatypeProvider(IDatatypeProvider provider) {
		if (datatypeProvider.contains(provider)) {
			datatypeProvider.remove(provider);
			for (IDataDictionary dd : datadictionary.values()) {
				removeDatatypes(dd, provider.getDatatypes());
			}
		}

	}

	public IDataDictionary getDataDictionary(ITenant tenant) {
		return datadictionary.get(tenant);
	}

	public List<String> getAllDatatypeNames() {
		return Collections.unmodifiableList(allDatatypeNames);
	}

	public void subscribe(ITenant forTenant, IDatadictionaryProviderListener listener) {
		ddPlistener.put(listener, forTenant);
	}

	public void unsubscribe(IDatadictionaryProviderListener listener) {
		ddPlistener.remove(listener);
	}

	public void fire(ITenant forTenant, IDataDictionary dd, boolean add) {
		for (Entry<IDatadictionaryProviderListener, ITenant> e : ddPlistener.entrySet()) {
			if (e.getValue().equals(forTenant)) {
				if (add) {
					e.getKey().newDatadictionary(dd);
				} else {
					e.getKey().removedDatadictionary(dd);
				}
			}
		}
	}

	private void addDatatypes(IDataDictionary dd, List<SDFDatatype> types) {
		for (SDFDatatype dt : types) {
			if (!dd.existsDatatype(dt.getURI())) {
				((IDataDictionaryWritable) dd).addDatatype(dt);
			}
			if (!allDatatypeNames.contains(dt.getURI())) {
				allDatatypeNames.add(dt.getURI());
			}
		}
	}

	private void removeDatatypes(IDataDictionary dd, List<SDFDatatype> types) {
		for (SDFDatatype dt : types) {
			if (!dd.existsDatatype(dt.getURI())) {
				((IDataDictionaryWritable) dd).removeDatatype(dt);
			}
			allDatatypeNames.remove(dt.getURI());
		}
	}

	void setUserManagementProvider(UserManagementProvider provider) {
		this.userManagementProvider = provider;
	}

	void setInstance() {
		instance = this;
	}
}
