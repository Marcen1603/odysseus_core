package de.uniol.inf.is.odysseus.net.util;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;

public class NamedInterfaceRegistry<T extends INamedInterface> {

	private static final Logger LOG = LoggerFactory.getLogger(NamedInterfaceRegistry.class);

	private final Map<String, T> interfaceMap = Maps.newHashMap();

	public final void add(T interfaceContribution) {
		Preconditions.checkNotNull(interfaceContribution, "Interface contribution to add to registry must not be null!");
		Preconditions.checkArgument(!contains(interfaceContribution.getName()), "Interface contribution %s already registered", determineSignature(interfaceContribution));

		synchronized (interfaceMap) {
			interfaceMap.put(interfaceContribution.getName().toUpperCase(), interfaceContribution);
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("Interface contribution added : {}", determineSignature(interfaceContribution));
		}
	}

	public final void remove(T interfaceContribution) {
		Preconditions.checkNotNull(interfaceContribution, "Interface contribution to remove from registry must not be null!");

		String allocatorName = interfaceContribution.getName().toUpperCase();
		synchronized (interfaceMap) {
			if (interfaceMap.containsKey(allocatorName)) {
				interfaceMap.remove(allocatorName);
				if (LOG.isDebugEnabled()) {
					LOG.debug("Interface contribution removed : {}", determineSignature(interfaceContribution));
				}
			} else {
				if (LOG.isDebugEnabled()) {
					LOG.warn("Tried to remove Interface contribution which was not registered before: {}", determineSignature(interfaceContribution));
				}
			}
		}
	}

	private static String determineSignature(INamedInterface iFace) {
		StringBuilder sb = new StringBuilder();
		sb.append("{").append(determinePrintableClassName(iFace)).append(":").append(iFace.getName()).append("}");
		return sb.toString();
	}

	private static String determinePrintableClassName(INamedInterface iFace) {
		Class<?>[] classes = iFace.getClass().getInterfaces();
		for (Class<?> clazz : classes) {
			if (!clazz.equals(INamedInterface.class) && !clazz.equals(iFace.getClass())) {
				return clazz.getSimpleName();
			}
		}

		return iFace.getClass().getSimpleName();
	}

	public final boolean contains(String name) {
		Preconditions.checkNotNull(!Strings.isNullOrEmpty(name), "Name of Interface contribution must not be null or empty!");
		synchronized (interfaceMap) {
			return interfaceMap.containsKey(name.toUpperCase());
		}
	}

	public final ImmutableCollection<String> getNames() {
		synchronized (interfaceMap) {
			return ImmutableList.copyOf(interfaceMap.keySet());
		}
	}

	public final ImmutableCollection<T> getInterfaceContributions() {
		synchronized (interfaceMap) {
			return ImmutableList.copyOf(interfaceMap.values());
		}
	}

	public final T get(String name) {
		Preconditions.checkNotNull(!Strings.isNullOrEmpty(name), "Name of Interface contribution to get must not be null or empty!");
		
		synchronized (interfaceMap) {
			return interfaceMap.get(name.toUpperCase());
		}
	}
}
