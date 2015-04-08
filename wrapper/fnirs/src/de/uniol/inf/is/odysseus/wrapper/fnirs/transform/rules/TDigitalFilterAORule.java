/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.wrapper.fnirs.transform.rules;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.wrapper.fnirs.digitalfilter.DigitalFilter;
import de.uniol.inf.is.odysseus.wrapper.fnirs.digitalfilter.DigitalFilter.FilterType;
import de.uniol.inf.is.odysseus.wrapper.fnirs.digitalfilter.DigitalFilter.PassType;
import de.uniol.inf.is.odysseus.wrapper.fnirs.logicaloperator.DigitalFilterAO;
import de.uniol.inf.is.odysseus.wrapper.fnirs.physicaloperator.RelationalDigitalFilterPO;

/**
 * This rule handles the digital filter operator
 * 
 * @author Henrik Surm
 */

public class TDigitalFilterAORule extends AbstractTransformationRule<DigitalFilterAO> 
{
	static Logger LOG = LoggerFactory.getLogger(TDigitalFilterAORule.class);
	static InfoService infoService = InfoServiceFactory.getInfoService(TDigitalFilterAORule.class);

	@Override
	public int getPriority() {
		return 0;
	}

	@SuppressWarnings("rawtypes")
	@Override public void execute(DigitalFilterAO operator, TransformationConfiguration config) throws RuleException 
	{
		RelationalDigitalFilterPO po = new RelationalDigitalFilterPO();
                
		DigitalFilter.FilterType filterType;
		if (operator.getFilterType().equalsIgnoreCase("butterworth")) filterType = FilterType.BUTTERWORTH;
		else
		if (operator.getFilterType().equalsIgnoreCase("chebychev")) filterType = FilterType.CHEBYCHEV;
		else
		if (operator.getFilterType().equalsIgnoreCase("bessel")) filterType = FilterType.BESSEL;
		else
			throw new IllegalArgumentException("Unknown filter type: \"" + operator.getFilterType() + "\"");
		
		DigitalFilter.PassType passType;
		if (operator.getPassType().equalsIgnoreCase("lowpass")) passType = PassType.LOWPASS;
		else
		if (operator.getPassType().equalsIgnoreCase("highpass")) passType = PassType.HIGHPASS;
		else
		if (operator.getPassType().equalsIgnoreCase("bandpass")) passType = PassType.BANDPASS;
		else
			throw new IllegalArgumentException("Unknown pass type: \"" + operator.getPassType() + "\"");
		
		DigitalFilter filter = new DigitalFilter(filterType, passType, operator.getOrder(), 
												 operator.getCornerFreq1() / operator.getSamplingFreq(), 
												 operator.getCornerFreq2() / operator.getSamplingFreq(), 
												 operator.getRippleAttenuation(), false);
		
		po.setByteBufferSampleDepth(operator.getByteBufferSampleDepth());
		po.setFilter(filter, operator.getAttributePositions(), operator.getInputSchema());
		
        defaultExecute(operator, po, config, true, true);
	}

	@Override public boolean isExecutable(DigitalFilterAO operator, TransformationConfiguration config) 
	{
		return operator.isAllPhysicalInputSet() && (operator.getInputSchema().getType() == Tuple.class);
	}	
	
	@Override
	public String getName() {
		return "DigitalFilterAO --> RelationalDigitalFilterPO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super DigitalFilterAO> getConditionClass() {
		return DigitalFilterAO.class;
	}
}
