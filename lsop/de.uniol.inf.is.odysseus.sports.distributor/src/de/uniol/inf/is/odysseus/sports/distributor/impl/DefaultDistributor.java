package de.uniol.inf.is.odysseus.sports.distributor.impl;


import de.uniol.inf.is.odysseus.sports.distributor.helper.DistributionConfigBuildHelper;
import de.uniol.inf.is.odysseus.sports.distributor.helper.DistributionConfigBuildHelper.ALLOCATE;
import de.uniol.inf.is.odysseus.sports.distributor.helper.DistributionConfigBuildHelper.PARTITION;

public class DefaultDistributor implements ISportsQLDistributor{
	
	private final String distributorName = "default";

	@Override
	public String getDistributorName() {
		return distributorName;
	}

	@Override
	public String getDistributionConfig() {

		StringBuilder distributionConfig = new StringBuilder();
		distributionConfig.append(DistributionConfigBuildHelper.createPeerPartition(PARTITION.OPERATORCLOUD));
		distributionConfig.append(DistributionConfigBuildHelper.createPeerAllocate(ALLOCATE.ROUNDROBINWITHLOCAL));
		return distributionConfig.toString();
	}
	
	
	
	

}
