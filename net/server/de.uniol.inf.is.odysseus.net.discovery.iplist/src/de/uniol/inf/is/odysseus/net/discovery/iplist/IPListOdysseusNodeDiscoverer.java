package de.uniol.inf.is.odysseus.net.discovery.iplist;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.config.OdysseusBaseConfiguration;
import de.uniol.inf.is.odysseus.net.IOdysseusNode;
import de.uniol.inf.is.odysseus.net.IOdysseusNodeManager;
import de.uniol.inf.is.odysseus.net.discovery.AbstractOdysseusNodeDiscoverer;
import de.uniol.inf.is.odysseus.net.discovery.OdysseusNetDiscoveryException;

public final class IPListOdysseusNodeDiscoverer extends AbstractOdysseusNodeDiscoverer {

	private static final Logger LOG = LoggerFactory.getLogger(IPListOdysseusNodeDiscoverer.class);
	private static final String IP_LIST_FILENAME = "nodeIPList.conf";

	@Override
	public void startImpl(IOdysseusNodeManager manager, IOdysseusNode localNode) throws OdysseusNetDiscoveryException {
		LOG.info("Beginning node discovery with ip list from file");

		String filename = determineFullFilename();
		LOG.info("File is {}", filename);

		try {
			Collection<String> ips = tryReadIPsFromFile(filename);
			if (!ips.isEmpty()) {
				logIPList(ips);

				// TODO: create nodes

			} else {
				throw new OdysseusNetDiscoveryException("List of ips is empty! Discovering no nodes!");
			}
		} catch (IOException e) {
			throw new OdysseusNetDiscoveryException("Could not discover nodes with file", e);
		}

	}

	private static void logIPList(Collection<String> ips) {
		LOG.info("Got {} ips", ips.size());
		if (LOG.isDebugEnabled()) {
			for (String ip : ips) {
				LOG.debug("\t{}", ip);
			}
		}
	}

	private static Collection<String> tryReadIPsFromFile(String filename) throws IOException {
		LOG.info("Loading file '{}' of list of ips", filename);
		Collection<String> lines = Lists.newLinkedList();

		BufferedReader br = new BufferedReader(new FileReader(filename));
		try {
			String line = br.readLine();

			while (line != null) {
				lines.add(line);
				line = br.readLine();
			}
			return lines;
		} finally {
			br.close();
		}
	}

	private static String determineFullFilename() {
		return OdysseusBaseConfiguration.getHomeDir() + IP_LIST_FILENAME;
	}

	@Override
	public void stopImpl() {

	}

}
