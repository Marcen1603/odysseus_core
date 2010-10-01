package de.uniol.inf.is.odysseus.planmanagement.optimization.configuration;

import de.uniol.inf.is.odysseus.planmanagement.configuration.AbstractTypeSafeMap;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;

/**
 * OptimizationConfiguration holds the configuration of an {@link IOptimizer}.
 * 
 * @author Tobias Witt
 *
 */
public class OptimizationConfiguration extends AbstractTypeSafeMap<AbstractOptimizationSetting<?>> {
	
	public OptimizationConfiguration(AbstractOptimizationSetting<?>... entries) {
		super(entries);
	}
	
	public SettingMaxConcurrentOptimizations getSettingMaxConcurrentOptimizations() {
		if (!this.contains(SettingMaxConcurrentOptimizations.class)) {
			set(new SettingMaxConcurrentOptimizations(2), false);
		}
		return (SettingMaxConcurrentOptimizations) this.get(SettingMaxConcurrentOptimizations.class);
	}
	
	public SettingComparePlanCandidates getSettingComparePlanCandidates() {
		if (!this.contains(SettingComparePlanCandidates.class)) {
			set(new SettingComparePlanCandidates(5));
		}
		return (SettingComparePlanCandidates) this.get(SettingComparePlanCandidates.class);
	}
	
	public SettingRefuseOptimizationAtCpuLoad getSettingRefuseOptimizationAtCpuLoad() {
		if (!this.contains(SettingRefuseOptimizationAtCpuLoad.class)) {
			set(new SettingRefuseOptimizationAtCpuLoad(85.0));
		}
		return (SettingRefuseOptimizationAtCpuLoad) this.get(SettingRefuseOptimizationAtCpuLoad.class);
	}
	
	public SettingRefuseOptimizationAtMemoryLoad getSettingRefuseOptimizationAtMemoryLoad() {
		if (!this.contains(SettingRefuseOptimizationAtMemoryLoad.class)) {
			set(new SettingRefuseOptimizationAtMemoryLoad(80.0));
		}
		return (SettingRefuseOptimizationAtMemoryLoad) this.get(SettingRefuseOptimizationAtMemoryLoad.class);
	}

}
