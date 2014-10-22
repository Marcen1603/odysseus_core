package de.uniol.inf.is.odysseus.sports.distributor.impl;

public class NoDistributionDistributor implements ISportsQLDistributor{
	
	private final String distributorName = "noDistribution";

	@Override
	public String getDistributorName() {
		return distributorName;
	}

	@Override
	public String getDistributionConfig() {
		/*
		 * return a empty distribution config to deactivate the distribution
		 */
		return "";
	}

}
