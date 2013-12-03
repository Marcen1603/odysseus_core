package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized.resourceusage;

public interface IResourceUsageUpdateListener {
	void updateResourceUsage(double cpuUsage, double mem_free, double mem_total, double mem_used, double networkUsage);
	String getPeerName();
}
