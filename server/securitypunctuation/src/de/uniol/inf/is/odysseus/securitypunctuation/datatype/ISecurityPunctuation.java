package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public interface ISecurityPunctuation extends IPunctuation {
	public DataDescriptionPart getDDP();

	public SecurityRestrictionPart getSRP();

	public boolean getSign();

	public boolean getImmutable();

	void setDDP(DataDescriptionPart ddp);

	void setSRP(SecurityRestrictionPart srp);

	void setImmutable(boolean immutable);

	void setSign(boolean sign);

	void setTime(PointInTime timestamp);
	
	public SDFSchema getSchema();

}
