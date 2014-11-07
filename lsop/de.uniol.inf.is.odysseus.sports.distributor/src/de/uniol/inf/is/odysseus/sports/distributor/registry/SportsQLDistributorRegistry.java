package de.uniol.inf.is.odysseus.sports.distributor.registry;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.p2p_new.dictionary.IPeerDictionary;
import de.uniol.inf.is.odysseus.sports.distributor.helper.DistributionConfigBuildHelper;
import de.uniol.inf.is.odysseus.sports.distributor.helper.enums.Parser;
import de.uniol.inf.is.odysseus.sports.distributor.impl.ISportsQLDistributor;
import de.uniol.inf.is.odysseus.sports.distributor.impl.NoDistributionDistributor;

/**
 * This class represents a registry for @link ISportsQLDistributor. SportsQL
 * distibutor are registered and unregistered by OSGI service binding mechanism.
 * 
 * @author Marc Preuschaft
 * 
 */

public class SportsQLDistributorRegistry {

	static Logger logger = LoggerFactory
			.getLogger(SportsQLDistributorRegistry.class);

	private static Map<String, ISportsQLDistributor> sportsQLDistributorMap = Maps
			.newHashMap();
	private static IPeerDictionary peerDictionary;

	public static void bindPeerDictionary(IPeerDictionary dictionary) {
		peerDictionary = dictionary;
	}

	public static void unbindPeerDictionary(IPeerDictionary dictionary) {
		if (peerDictionary == dictionary) {
			peerDictionary = null;
		}
	}

	public static void registerSportsQLDistributor(
			ISportsQLDistributor distributor) {
		if (!sportsQLDistributorMap.containsKey(distributor
				.getDistributorName().toLowerCase())) {
			sportsQLDistributorMap.put(distributor.getDistributorName()
					.toLowerCase(), distributor);
		} else {
			logger.debug("Distributor "
					+ distributor.getDistributorName().toLowerCase()
					+ " already added");
		}

	}

	public static void unregisterSportsQLDistributor(
			ISportsQLDistributor distributor) {
		if (sportsQLDistributorMap.containsKey(distributor.getDistributorName()
				.toLowerCase())) {
			sportsQLDistributorMap.remove(distributor.getDistributorName()
					.toLowerCase());
		}
	}

	public static String addSportsQLDistributorConfig(String sportsQL) {

		ISportsQLDistributor distributor = getSportsQLDistributor(sportsQL);
		String outputConfig = DistributionConfigBuildHelper
				.createParser(Parser.SportsQL);
		if (!(distributor instanceof NoDistributionDistributor)) {
			outputConfig += DistributionConfigBuildHelper.createDistribute();
		}
		outputConfig += distributor.getDistributionConfig();
		outputConfig += DistributionConfigBuildHelper.createRunQuery();
		outputConfig += sportsQL;

		return outputConfig;
	}
	
	

	public static ISportsQLDistributor getSportsQLDistributor(String sportsQL) {
		String type = null;
		String game = null;
		String name = null;

		try {
			JSONObject obj = new JSONObject(sportsQL);
			type = obj.getString("statisticType");
			game = obj.getString("gameType");
			name = obj.getString("name");
		} catch (JSONException e) {
			throw new RuntimeException("error");
		}

		// check if any peer is online
		if (peerDictionary.getRemotePeerIDs().size() > 0) {
			if (!sportsQLDistributorMap.containsKey(type + "_" + game + "_"
					+ name)) {
				logger.info("Distributor for: " + type + "_" + game + "_"
						+ name
						+ " is not valid. You get the default distributor!");
				ISportsQLDistributor distributor = sportsQLDistributorMap
						.get("default");
				return distributor;
			} else {
				ISportsQLDistributor distributor = sportsQLDistributorMap
						.get(type + "_" + game + "_" + name);
				return distributor;
			}
		} else {
			// no peer found --> no distribution
			ISportsQLDistributor distributor = sportsQLDistributorMap
					.get("nodistribution");
			return distributor;
		}
	}
}
