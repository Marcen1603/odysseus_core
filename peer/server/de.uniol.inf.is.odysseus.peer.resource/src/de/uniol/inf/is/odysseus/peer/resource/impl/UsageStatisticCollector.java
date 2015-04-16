package de.uniol.inf.is.odysseus.peer.resource.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.peer.resource.IResourceUsage;
import de.uniol.inf.is.odysseus.updater.FeatureUpdateUtility;

public class UsageStatisticCollector {

	private static final Logger LOG = LoggerFactory.getLogger(UsageStatisticCollector.class);
	
	private static final int MAX_STAT_COUNT = 2;
	
	private static ISession currentSession;
	
	private final NumberAverager memFree = new NumberAverager(MAX_STAT_COUNT);
	private final NumberAverager cpuFree = new NumberAverager(MAX_STAT_COUNT);
	private final NumberAverager netOutputRate = new NumberAverager(MAX_STAT_COUNT);
	private final NumberAverager netInputRate = new NumberAverager(MAX_STAT_COUNT);

	private long memMaxBytes;
	private double cpuMax;
	private int runningQueriesCount;
	private int stoppedQueriesCount;
	private int remotePeerCount;
	private double netBandwidthMax;
	
	private int[] version;
	private long startupTimestamp = -1;
	
	public static ISession getActiveSession() {
		if( currentSession == null || !currentSession.isValid()) {
			currentSession = UserManagementProvider.getSessionmanagement().loginSuperUser(null, UserManagementProvider.getDefaultTenant().getName());
		}
		return currentSession;
	}
	
	public synchronized void addStatistics(long memFreeBytes, long memMaxBytes, double cpuFree, double cpuMax, int runningQueriesCount, int stoppedQueriesCount, int remotePeerCount, double netBandwidthMax, double netOutputRate, double netInputRate) {
		memFreeBytes = assureNonNegative(memFreeBytes);
		memMaxBytes = assureNonNegative(memMaxBytes);
		
		cpuFree = assureNonNegative(cpuFree);
		cpuMax = assureNonNegative(cpuMax);
		
		netBandwidthMax = assureNonNegative(netBandwidthMax);
		netOutputRate = assureNonNegative(netOutputRate);
		netInputRate = assureNonNegative(netInputRate);
		
		if( memFreeBytes > memMaxBytes ) {
			memFreeBytes = memMaxBytes;
		}
		
		if( cpuFree > cpuMax ) {
			cpuFree = cpuMax;
		}

		this.memFree.addValue(memFreeBytes);
		this.cpuFree.addValue(cpuFree);
		this.netInputRate.addValue(netInputRate);
		this.netOutputRate.addValue(netOutputRate);
		
		this.memMaxBytes = memMaxBytes;
		this.cpuMax = cpuMax;
		this.stoppedQueriesCount = stoppedQueriesCount;
		this.runningQueriesCount = runningQueriesCount;
		this.remotePeerCount = remotePeerCount;
		this.netBandwidthMax = netBandwidthMax;
	}
	
	private static long assureNonNegative(long longValue) {
		return longValue >= 0 ? longValue : 0;
	}

	private static double assureNonNegative(double doubleValue) {
		return doubleValue >= 0.0 ? doubleValue : 0.0;
	}

	public synchronized IResourceUsage getCurrentResourceUsage() {
		if( version == null ) {
			try {
				String versionString = FeatureUpdateUtility.getVersionNumber(getActiveSession());
				version = toVersionDigits(versionString);
			} catch( Throwable t ) {
			version = new int[] {0,0,0,0};
			}
		}
		
		if( startupTimestamp == -1 ) {
			startupTimestamp = System.currentTimeMillis();
		}
		
		return new ResourceUsage((long) memFree.getAverage(), 
				memMaxBytes, cpuFree.getAverage(), 
				cpuMax, 
				runningQueriesCount, 
				stoppedQueriesCount, 
				remotePeerCount,
				netBandwidthMax, 
				netOutputRate.getAverage(), 
				netInputRate.getAverage(), 
				version,
				startupTimestamp,
				System.currentTimeMillis());
	}

	private static int[] toVersionDigits(String versionString) {
		if( Strings.isNullOrEmpty(versionString) || versionString.equals("-1")) {
			return new int[] { 0, 0, 0, 0};
		}
		
		String[] versionStringParts = versionString.split("\\.");
		if( versionStringParts.length != 4 ) {
			LOG.warn("Could not determine version digits from version string '{}'", versionString);
			return new int[] { 0, 0, 0, 0};			
		}
			
		return new int[] {
			tryToInt(versionStringParts[0]),
			tryToInt(versionStringParts[1]),
			tryToInt(versionStringParts[2]),
			tryToIntAdvanced(versionStringParts[3]),
		};
	}

	private static int tryToIntAdvanced(String string) {
		int pos = string.indexOf("-");
		if( pos != -1 ) {
			string = string.substring(0, pos);
		}
		
		return tryToInt(string);
	}

	private static int tryToInt(String string) {
		try {
			return Integer.valueOf(string);
		} catch( Throwable t ) {
			LOG.error("Illegal version digit string: {}", string);
			return 0;
		}
	}
}
