package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.unit.SDFUnit;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;

public class CreateAndRenameSDFAttributeParameter extends
		AbstractParameter<RenameAttribute> {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory
			.getLogger(CreateAndRenameSDFAttributeParameter.class);

	private static final long serialVersionUID = -544787040358885000L;

	public CreateAndRenameSDFAttributeParameter() {
	}

	public CreateAndRenameSDFAttributeParameter(String name, REQUIREMENT requirement,
			USAGE usage) {
		super(name, requirement, usage);
	}

	public CreateAndRenameSDFAttributeParameter(String name, REQUIREMENT requirement) {
		this(name, requirement, USAGE.RECENT);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void internalAssignment() {
		if (inputValue instanceof RenameAttribute) {
			setValue((RenameAttribute) inputValue);
			return;
		}

		List<?> l1 = (List<?>) inputValue;
		List<List<String>> constraintList = null;
		if (l1.get(l1.size() - 1) instanceof List) {
			constraintList = (List<List<String>>) l1.get(l1.size() - 1);
			l1.remove(l1.size() - 1);
		}

		List<String> list = (List<String>) l1;

		if (list.size() == 3) {
			setValue(new RenameAttribute(determineAttribute(null, list.get(0), list.get(2),
					constraintList), list.get(1)));
		} else if (list.size() == 2) {
			setValue(new RenameAttribute(determineAttribute(null, list.get(0), list.get(1),
					constraintList), ""));
		} else {
			throw new IllegalArgumentException(
					"Wrong number of inputs for SDFAttribute. Expecting [path] attributename and datatype [constraintlist].");
		}
	}

	@Override
	protected String getPQLStringInternal() {
		String attributeFullName = getValue().getAttribute().getAttributeName();
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		if (!Strings.isNullOrEmpty(getValue().getAttribute().getSourceName())) {
			sb.append("'").append(getValue().getAttribute().getSourceName()).append("',");
		}
		sb.append("'").append(attributeFullName).append("','")
				.append(getValue().getAttribute().getDatatype().getURI()).append("'");
		if (getValue().getAttribute().getUnit() != null
				|| getValue().getAttribute().getDtConstraints().size() > 0) {
			sb.append(",[");
			if (getValue().getAttribute().getUnit() != null) {
				sb.append("['Unit','").append(getValue().getAttribute().getUnit())
						.append("']");
				if (getValue().getAttribute().getDtConstraints().size() > 0) {
					sb.append(",");
				}
			}
			if (getValue().getAttribute().getDtConstraints().size() > 0) {
				Iterator<SDFConstraint> iter = getValue().getAttribute().getDtConstraints()
						.iterator();
				for (int i = 0; i < getValue().getAttribute().getDtConstraints().size(); i++) {
					SDFConstraint cs = iter.next();
					sb.append("['").append(cs.getQualName()).append("','")
							.append(cs.getValue()).append("']");
					if (i < getValue().getAttribute().getDtConstraints().size() - 1) {
						sb.append(",");
					}

				}
			}
			sb.append("]");
		}

		sb.append("]");
		return sb.toString();
	}

	private SDFAttribute determineAttribute(String sourcename,
			String attributeName, String dataTypeName,
			List<List<String>> constraintList) {
		try {
			SDFUnit unit = null;
			List<SDFConstraint> dtList = new LinkedList<>();
			if (constraintList != null && constraintList.size() > 0) {
				for (List<String> pair : constraintList) {
					if (pair.size() != 2) {
						throw new IllegalArgumentException(
								"Wrong Constraint definition part. Use ['type', 'value']");
					}
					if (pair.get(0).equalsIgnoreCase(
							SDFUnit.class.getSimpleName())
							|| pair.get(0).equalsIgnoreCase("Unit")) {
						unit = new SDFUnit(pair.get(1));
					} else {
						dtList.add(new SDFConstraint(pair.get(0).toLowerCase(),
								pair.get(1)));
					}
				}
			}

			int pos = dataTypeName.indexOf('(');
			if (pos == -1) {
				pos = dataTypeName.indexOf('<');
			}
			if (pos > 0) {
				String upperTypeStr = dataTypeName.substring(0, pos);
				SDFDatatype upperType = getDataDictionary().getDatatype(
						upperTypeStr);
				String subTypeListStr = dataTypeName.substring(pos + 1,
						dataTypeName.length() - 1);
				final SDFDatatype dataType;
				List<SDFDatatype> subTypeList = new LinkedList<SDFDatatype>();
				String[] subtypes = subTypeListStr.split(",");
				if (subtypes.length > 0) {
					for (String t : subtypes) {
						subTypeList.add(getDataDictionary().getDatatype(
								t.trim()));
					}
				} else {
					subTypeList.add(getDataDictionary().getDatatype(
							subTypeListStr));
				}
				dataType = getDataDictionary().getDatatype(upperType,
						subTypeList);
				return new SDFAttribute(sourcename, attributeName, dataType,
						unit, dtList);
			}
			return new SDFAttribute(sourcename, attributeName,
					getDataDictionary().getDatatype(dataTypeName), unit, dtList);
		} catch (DataDictionaryException e) {
			throw new QueryParseException(e);
		}
	}

}
