package de.uniol.inf.is.odysseus.core.server.datadictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ITenant;

public class DataDictionaryProvider {

	static private List<IDatatypeProvider> datatypeProvider = new ArrayList<>();
	static private Map<ITenant, IDataDictionary> datadictionary = new HashMap<ITenant, IDataDictionary>();
	static private List<String> allDatatypeNames = new LinkedList<>();
	static private Map<IDatadictionaryProviderListener, ITenant> ddPlistener = new HashMap<>();

	public void bindDataDictionary(IDataDictionary bindDD) {
		// Is dd the right type
		List<ITenant> tenants = UserManagementProvider.getTenants();
		for (ITenant t : tenants) {
			IDataDictionary dd = bindDD.createInstance(t);
			// Default Datatypes
			addDatatypes(dd, SDFDatatype.getTypes());
			for (IDatatypeProvider p : datatypeProvider) {
				addDatatypes(dd, p.getDatatypes());
			}
			datadictionary.put(t, dd);
			fire(t, dd);
		}
	}

	public void unbindDataDictionary( IDataDictionary dataDictionary ) {
		// TODO: Implement this!
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

	public void bindDatatypeProvider(IDatatypeProvider provider) {
		if (!datatypeProvider.contains(provider)) {
			datatypeProvider.add(provider);
			for (IDataDictionary dd : datadictionary.values()) {
				addDatatypes(dd, provider.getDatatypes());
			}
		}
	}

	public void unbindDatatypeProvider(IDatatypeProvider provider) {
		if (datatypeProvider.contains(provider)) {
			datatypeProvider.remove(provider);
			// TODO: Remove Datatypes from DD!
		}

	}

	public static IDataDictionary getDataDictionary(ITenant tenant) {
		return datadictionary.get(tenant);
	}

	public static List<String> getAllDatatypeNames() {
		return Collections.unmodifiableList(allDatatypeNames);
	}

	public void subscribe(ITenant forTenant, IDatadictionaryProviderListener listener){
		ddPlistener.put(listener, forTenant);
	}
	
	public void unsubscribe(IDatadictionaryProviderListener listener){
		ddPlistener.remove(listener);
	}
	
	public void fire(ITenant forTenant, IDataDictionary dd){
		for (Entry<IDatadictionaryProviderListener, ITenant> e: ddPlistener.entrySet()){
			if (e.getValue().equals(forTenant)){
				e.getKey().newDatadictionary(dd);
			}
		}
	}
}
