package de.uniol.inf.is.odysseus.wrapper.dds.dds;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rti.dds.infrastructure.BadKind;
import com.rti.dds.infrastructure.Bounds;
import com.rti.dds.typecode.EnumMember;
import com.rti.dds.typecode.ExtensibilityKind;
import com.rti.dds.typecode.StructMember;
import com.rti.dds.typecode.TCKind;
import com.rti.dds.typecode.TypeCode;
import com.rti.dds.typecode.TypeCodeFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.sdf.unit.SDFUnit;
import de.uniol.inf.is.odysseus.wrapper.dds.dds.simple.DDSBooleanDataReader;
import de.uniol.inf.is.odysseus.wrapper.dds.dds.simple.DDSCharDataReader;
import de.uniol.inf.is.odysseus.wrapper.dds.dds.simple.DDSDoubleDataReader;
import de.uniol.inf.is.odysseus.wrapper.dds.dds.simple.DDSFloatDataReader;
import de.uniol.inf.is.odysseus.wrapper.dds.dds.simple.DDSLongDataReader;
import de.uniol.inf.is.odysseus.wrapper.dds.dds.simple.DDSShortDataReader;

public class TypeCodeMapper {

	static final InfoService INFO = InfoServiceFactory.getInfoService(TypeCodeMapper.class);

	private static final Map<String, TypeCode> nameToCode = new HashMap<>();
	private static final Map<TypeCode, TypeCode> alias = new HashMap<>();

	private static final Map<TypeCode, SDFDatatype> tcToSDFDatatype = new HashMap<>();
	static final Map<String, IDDSDataReader<?>> nameToDataReader = new HashMap<>();


	static {	
		add(TypeCode.TC_BOOLEAN, SDFDatatype.BOOLEAN, new DDSBooleanDataReader());
		add(TypeCode.TC_CHAR, SDFDatatype.CHAR, new DDSCharDataReader());
		add(TypeCode.TC_DOUBLE, SDFDatatype.DOUBLE, new DDSDoubleDataReader());
		add(TypeCode.TC_FLOAT, SDFDatatype.FLOAT, new DDSFloatDataReader());	
		add(TypeCode.TC_LONG, SDFDatatype.LONG, new DDSLongDataReader());
		add(TypeCode.TC_LONGDOUBLE, SDFDatatype.DOUBLE, new DDSDoubleDataReader());
		add(TypeCode.TC_LONGLONG, SDFDatatype.LONG, new DDSLongDataReader());
		add(TypeCode.TC_OCTET, SDFDatatype.LONG, new DDSLongDataReader());		
		add(TypeCode.TC_SHORT, SDFDatatype.SHORT, new DDSShortDataReader());
		add(TypeCode.TC_ULONG, SDFDatatype.LONG, new DDSLongDataReader());
		add(TypeCode.TC_ULONGLONG, SDFDatatype.LONG, new DDSLongDataReader());
		add(TypeCode.TC_USHORT,SDFDatatype.SHORT, new DDSShortDataReader());
		add(TypeCode.TC_WCHAR, SDFDatatype.CHAR, new DDSCharDataReader());
	}

	static public void add(TypeCode code) {
		getNametocode().put(code.get_type_as_string(), code);
	}

	private static void add(TypeCode typeCode, SDFDatatype sdftype, IDDSDataReader<?> dataReader) {
		add(typeCode);
		tcToSDFDatatype.put(typeCode, sdftype);
		add(typeCode, dataReader);
	}

	static public void addAlias(TypeCode code1, TypeCode code2) {
		alias.put(code1, code2);
	}

	public static TypeCode getTypeCode(String name) {
		TypeCode val = getNametocode().get(name);
		return val;
	}

	public static void add(TypeCode code, IDDSDataReader<?> dataReader) {
		if (dataReader == null) {
			INFO.warning("dataReader for " + code + " is null! Will not read any data of type code!");
		}
		add(code.get_type_as_string(), dataReader);
	}

	public static void add(IDDSDataReader<?> dataReader) {
		if (dataReader == null) {
			INFO.warning("dataReader is null! Will not read any data of type code!");
		}
		add(dataReader.getTypeCode().get_type_as_string(), dataReader);
	}

	public static void add(String typeName, IDDSDataReader<?> dataReader) {
		nameToDataReader.put(typeName, dataReader);
	}

	public static IDDSDataReader<?> getDataReader(String typeName) {
		IDDSDataReader<?> val = nameToDataReader.get(typeName);
		return val;
	}

	public static IDDSDataReader<?> getDataReader(TypeCode type) {
		IDDSDataReader<?> val = nameToDataReader.get(type.get_type_as_string());
		return val;
	}

	public static TypeCode createTypeCode(String module, String type) {
		// TODO: Move assignment of DataReader somewhere else?

		// Test if type is simple or complex
		final TypeCode tc;
		int pos = type.indexOf("<");
		if (pos > 0) {
			String typePart = type.substring(0, pos);
			// TODO: be more generic
			if (typePart.equalsIgnoreCase("string")) {
				int size = getSize(type, pos);
				tc = new TypeCode(TCKind.TK_STRING, size);
				add(tc, DDSDataReaderFactory.getKindReader(TCKind.TK_STRING.name()));
			} else if (typePart.equalsIgnoreCase("wstring")) {
				int size = getSize(type, pos);
				tc = new TypeCode(TCKind.TK_WSTRING, size);
				add(tc, DDSDataReaderFactory.getKindReader(TCKind.TK_WSTRING.name()));
			} else if (typePart.equalsIgnoreCase("sequence")) {
				// sequence<Type,size>
				int pos2 = type.indexOf(",");
				int pos3 = type.indexOf(">");
				String subtype = type.substring(pos + 1, pos2);
				TypeCode subTypeCode = getTypeCode(subtype);
				if (subTypeCode == null) {
					subTypeCode = getTypeCode(module + "::" + subtype);
				}
				if (subTypeCode == null) {
					throw new IllegalArgumentException("Type " + subtype + " not found");
				}
				String sizeStr = type.substring(pos2 + 1, pos3);
				// ToDo: Check const
				int size = Integer.parseInt(sizeStr);
				tc = new TypeCode(size, subTypeCode);
				add(tc, DDSDataReaderFactory.getSequenceReader(subTypeCode.get_type_as_string()));
			} else {
				throw new IllegalArgumentException("Type " + type + " not known");
			}
			getNametocode().put(type, tc);
		} else {
			throw new IllegalArgumentException("Type " + type + " not known");
		}
		return tc;
	}

	public static TypeCode getOrCreateTypeCode(String module, String type) {
		TypeCode tc = getTypeCode(type);
		if (tc == null) {
			tc = getTypeCode(module + "::" + type);
			if (tc == null) {
				tc = createTypeCode(module, type);
			}
		}
		return tc;
	}

	private static int getSize(String type, int pos) {
		int pos2 = type.indexOf(">");
		String sizeStr = type.substring(pos + 1, pos2);
		// Todo: Could be a constant, replace
		// and store into sizeStr
		int size = Integer.parseInt(sizeStr);
		return size;
	}

	public static TypeCode createTypeAlias(String name, TypeCode typeCode) {
		TypeCode typeAlias = TypeCodeFactory.TheTypeCodeFactory.create_alias_tc(name, typeCode, false);
		add(typeAlias);
		addAlias(typeAlias, typeCode);
		add(typeAlias, getDataReader(typeCode));
		return typeAlias;
	}

	public static TypeCode createComplexType(String name, List<String> attributes, List<TypeCode> types,
			List<String> isKey) {

		StructMember sm[] = new StructMember[attributes.size()];
		for (int pos = 0; pos < attributes.size(); pos++) {
			sm[pos] = new StructMember(attributes.get(pos), false, (short) -1, isKey.contains(attributes.get(pos)), types.get(pos), pos,
					false);
		}

		TypeCode newType = TypeCodeFactory.TheTypeCodeFactory.create_struct_tc(name,
				ExtensibilityKind.EXTENSIBLE_EXTENSIBILITY, sm);
		TypeCodeMapper.add(newType);

		TypeCodeMapper.add(newType, new DDSDynamicDataDataReader(attributes, types));

		return newType;
	}

	public static TypeCode createEnumType(String name, List<String> values) {
		EnumMember[] em = new EnumMember[values.size()];
		for (int pos = 0; pos < values.size(); pos++) {
			em[pos] = new EnumMember(values.get(pos), pos);
		}
		TypeCode newType = TypeCodeFactory.TheTypeCodeFactory.create_enum_tc(name, em);
		TypeCodeMapper.add(newType);
		return newType;
	}

	/**
	 * @return the nametocode
	 */
	public static Map<String, TypeCode> getNametocode() {
		return nameToCode;
	}

	public static SDFDatatype getOdysseusType(TypeCode lookFor) throws BadKind, Bounds {
		TypeCode type = alias.containsKey(lookFor) ? alias.get(lookFor) : lookFor;

		if (type.is_primitive()) {
			return tcToSDFDatatype.get(type);
		}

		if (type.kind() == TCKind.TK_STRING || type.kind() == TCKind.TK_WSTRING) {
			return SDFDatatype.STRING;
		}

		if (type.kind() == TCKind.TK_SEQUENCE) {
			TypeCode base = type.content_type();
			SDFDatatype datatype = TypeCodeMapper.getOdysseusType(base);
			return SDFDatatype.createTypeWithSubSchema(SDFDatatype.LIST, datatype, null);
		}

		if (type.kind() == TCKind.TK_STRUCT) {
			SDFSchema subSchema = typeCodeToSchema(type);
			return SDFDatatype.createTypeWithSubSchema(SDFDatatype.TUPLE, subSchema);
		}
		
		throw new IllegalArgumentException("Sorry. Type "+type.get_type_as_string()+" currently not supported");		
	}
	
	public static SDFSchema typeCodeToSchema(TypeCode typeCode) throws BadKind, Bounds {
		List<SDFAttribute> attributes = new ArrayList<>();
		
		if (typeCode.kind() != TCKind.TK_STRUCT){
			throw new IllegalArgumentException("Cannot create Schema for this type");
		}
		
		for (int i=0;i<typeCode.member_count();i++){
			String name = typeCode.member_name(i);
			TypeCode type = typeCode.member_type(i);
			SDFDatatype datatype = TypeCodeMapper.getOdysseusType(type);
			attributes.add(new SDFAttribute("", name, datatype, (SDFUnit)null, (Collection<SDFConstraint>) null));
		}
		
		SDFSchema schema = SDFSchemaFactory.createNewSchema("", Tuple.class, attributes);
		return schema;
	}


}
