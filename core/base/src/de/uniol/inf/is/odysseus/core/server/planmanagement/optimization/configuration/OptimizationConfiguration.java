/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.configuration;

import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.Configuration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.configuration.ISetting;
import de.uniol.inf.is.odysseus.core.server.planmanagement.optimization.IOptimizer;

/**
 * OptimizationConfiguration holds the configuration of an {@link IOptimizer}.
 * 
 * @author Tobias Witt
 *
 */
public class OptimizationConfiguration extends Configuration<IOptimizationSetting<?>> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2407355981276802762L;

	public OptimizationConfiguration(IOptimizationSetting<?>... entries) {
		super(entries);
	}
	
	public OptimizationConfiguration(Configuration<?> config){
		setName(config.getName());
		for(Object s : config.values()){
			if(s instanceof IOptimizationSetting<?>){
				set((IOptimizationSetting<?>)s);
			}
		}
	}
	
	public OptimizationConfiguration(ISetting<?>[] iSettings) {
		for (ISetting<?> s:iSettings){
			if (s instanceof IOptimizationSetting<?>){
				set((IOptimizationSetting<?>)s);
			}
		}
	}

	/**
	 * Gets the current parameter for {@link ParameterDoRewrite}.
	 * 
	 * @return current parameter for {@link ParameterDoRewrite}
	 */
	public ParameterDoRewrite getParameterDoRewrite() {
		return this.get(ParameterDoRewrite.class);
	}
	
	public RewriteConfiguration getRewriteConfiguration(){
		if (!this.contains(RewriteConfiguration.class)) {
			set(new RewriteConfiguration());
		}
		return this.get(RewriteConfiguration.class);
	}

	/**
	 * Gets the current parameter for {@link ParameterQueryOptimizer}.
	 * 
	 * @return current parameter for {@link ParameterQueryOptimizer}
	 */
	public ParameterQueryOptimizer getParameterQueryOptimizer() {
		return this.get(ParameterQueryOptimizer.class);
	}
	
	public SettingMaxConcurrentOptimizations getSettingMaxConcurrentOptimizations() {
		if (!this.contains(SettingMaxConcurrentOptimizations.class)) {
			set(new SettingMaxConcurrentOptimizations(2), false);
		}
		return this.get(SettingMaxConcurrentOptimizations.class);
	}
	
	public SettingComparePlanCandidates getSettingComparePlanCandidates() {
		if (!this.contains(SettingComparePlanCandidates.class)) {
			set(new SettingComparePlanCandidates(5));
		}
		return this.get(SettingComparePlanCandidates.class);
	}
	
	public SettingRefuseOptimizationAtCpuLoad getSettingRefuseOptimizationAtCpuLoad() {
		if (!this.contains(SettingRefuseOptimizationAtCpuLoad.class)) {
			set(new SettingRefuseOptimizationAtCpuLoad(85.0));
		}
		return this.get(SettingRefuseOptimizationAtCpuLoad.class);
	}
	
	public SettingRefuseOptimizationAtMemoryLoad getSettingRefuseOptimizationAtMemoryLoad() {
		if (!this.contains(SettingRefuseOptimizationAtMemoryLoad.class)) {
			set(new SettingRefuseOptimizationAtMemoryLoad(80.0));
		}
		return this.get(SettingRefuseOptimizationAtMemoryLoad.class);
	}
	
	public ParameterPerformQuerySharing getParameterPerformQuerySharing() {
		return this.get(ParameterPerformQuerySharing.class);
	}
	
	public ParameterAllowRestructuringOfCurrentPlan getParameterAllowRestructuringOfCurrentPlan() {
		return this.get(ParameterAllowRestructuringOfCurrentPlan.class);
	}
	
	public ParameterShareSimilarOperators getParameterShareSimilarOperators() {
		return this.get(ParameterShareSimilarOperators.class);
	}


}
