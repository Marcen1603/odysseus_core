package de.uniol.inf.is.odysseus.costmodel;

import java.util.Collection;

import com.google.common.base.Optional;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

public interface ICostModelKnowledge {

	public Optional<IHistogram> getHistogram(SDFAttribute attribute);
	public Optional<IHistogram> getHistogram(String attributeName);
	public Collection<SDFAttribute> getHistogramAttributes();
	
}
