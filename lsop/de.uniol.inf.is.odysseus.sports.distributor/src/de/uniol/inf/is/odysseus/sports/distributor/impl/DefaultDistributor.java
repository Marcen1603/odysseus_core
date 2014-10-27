package de.uniol.inf.is.odysseus.sports.distributor.impl;

import de.uniol.inf.is.odysseus.sports.distributor.helper.DistributionConfigBuildHelper;
import de.uniol.inf.is.odysseus.sports.distributor.helper.enums.Allocate;
import de.uniol.inf.is.odysseus.sports.distributor.helper.enums.Partition;
import de.uniol.inf.is.odysseus.sports.distributor.helper.enums.Postprocessor;

public class DefaultDistributor implements ISportsQLDistributor {

	private final String distributorName = "default";

	@Override
	public String getDistributorName() {
		return distributorName;
	}

	@Override
	public String getDistributionConfig() {

		StringBuilder distributionConfig = new StringBuilder();
		distributionConfig.append(DistributionConfigBuildHelper
				.createPeerPartition(Partition.OPERATORCLOUD));
		distributionConfig.append(DistributionConfigBuildHelper
				.createPeerAllocate(Allocate.ROUNDROBIN));
		distributionConfig.append(DistributionConfigBuildHelper
				.createPeerPostProcessor(Postprocessor.MERGE));
		return distributionConfig.toString();
	}

}
