package de.uniol.inf.is.odysseus.securitypunctuation.datatype;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public interface ISecurityPunctuation extends IPunctuation {
	public IDataDescriptionPart getDDP();

	public ISecurityRestrictionPart getSRP();

	public boolean getSign();

	public boolean getImmutable();

	void setDDP(IDataDescriptionPart ddp);

	void setSRP(ISecurityRestrictionPart srp);

	void setImmutable(boolean immutable);

	void setSign(boolean sign);

	void setTime(PointInTime timestamp);

	public ISecurityPunctuation intersect(ISecurityPunctuation punctuation, SDFSchema schema, SDFSchema otherSchema, PointInTime ts);

	public boolean isEmpty();

	
	//removes the attributes, that dont fit from the tuple
	public Tuple<?> restrictObject(Tuple<?> object, SDFSchema schema, List<ISecurityPunctuation> matchingSPs,
			String tupleRangeAttribute);

	ISecurityPunctuation createEmptySP(PointInTime timestamp);

	public boolean checkRole(List<String> roles);

	public List<ISecurityPunctuation> union(List<ISecurityPunctuation> buffer);
}
