package de.uniol.inf.is.odysseus.p2p_new.distribute.centralized;

import org.hyperic.sigar.Mem;

public interface IResourceUsageUpdateListener {
	void updateResourceUsage(double cpuUsage, Mem mem, double networkUsage);
}
