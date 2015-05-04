package de.uniol.inf.is.odysseus.systemload.impl;

import de.uniol.inf.is.odysseus.systemload.SystemLoadPlugIn;

public class SystemLoadMeasurement {

	public enum MeasureType {
		TIME, CPU_MAX, CPU_FREE, NET_INPUT_RATE, NET_MAX, NET_OUTPUT_RATE, MEM_FREE, MEM_TOTAL
	};
	
	public static Object getValue(MeasureType measureType) {
		switch (measureType) {
		case TIME:
			return System.currentTimeMillis();
		case CPU_MAX:
			return SystemLoadPlugIn.SIGAR_WRAPPER.getCpuMax();
		case CPU_FREE:
			return SystemLoadPlugIn.SIGAR_WRAPPER.getCpuFree();
		case NET_INPUT_RATE:
			return SystemLoadPlugIn.SIGAR_WRAPPER.getNetInputRate();
		case NET_MAX:
			return SystemLoadPlugIn.SIGAR_WRAPPER.getNetMax();
		case NET_OUTPUT_RATE:
			return SystemLoadPlugIn.SIGAR_WRAPPER.getNetOutputRate();
		case MEM_FREE:
			return Runtime.getRuntime().freeMemory();
		case MEM_TOTAL:
			return Runtime.getRuntime().totalMemory();
		default:
			return null;
		}
	}
}
