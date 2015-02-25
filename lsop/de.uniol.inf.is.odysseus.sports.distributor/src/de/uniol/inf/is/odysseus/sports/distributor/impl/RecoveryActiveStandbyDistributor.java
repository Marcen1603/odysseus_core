package de.uniol.inf.is.odysseus.sports.distributor.impl;

import de.uniol.inf.is.odysseus.sports.distributor.helper.DistributionConfigBuildHelper;
import de.uniol.inf.is.odysseus.sports.distributor.helper.enums.Allocate;
import de.uniol.inf.is.odysseus.sports.distributor.helper.enums.Modification;
import de.uniol.inf.is.odysseus.sports.distributor.helper.enums.Partition;
import de.uniol.inf.is.odysseus.sports.distributor.helper.enums.Postprocessor;

public class RecoveryActiveStandbyDistributor implements ISportsQLDistributor {

	private final String distributorName = "recovery_active_standby";

	@Override
	public String getDistributorName() {
		return distributorName;
	}

	@Override
	public String getDistributionConfig() {

		StringBuilder distributionConfig = new StringBuilder();
		distributionConfig.append(DistributionConfigBuildHelper
				.createPeerPartition(Partition.OPERATORSETCLOUD));
		distributionConfig.append(DistributionConfigBuildHelper
				.createPeerAllocate(Allocate.ROUNDROBIN));
		distributionConfig.append(DistributionConfigBuildHelper
				.createPeerPostProcessor(Postprocessor.MERGE));
		distributionConfig.append(DistributionConfigBuildHelper
				.createPeerPostProcessor(Postprocessor.FORCELOCALSOURCES));
		distributionConfig.append(DistributionConfigBuildHelper.createPeerModification(Modification.REPLICATION,"2"));
		distributionConfig.append(DistributionConfigBuildHelper.createPeerModification(Modification.RECOVERY_ACTIVESTANDBY));
		return distributionConfig.toString();
	}

}
