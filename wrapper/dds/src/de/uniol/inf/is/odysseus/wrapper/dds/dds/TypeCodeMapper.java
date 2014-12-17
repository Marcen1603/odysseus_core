package de.uniol.inf.is.odysseus.wrapper.dds.dds;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.rti.dds.typecode.EnumMember;
import com.rti.dds.typecode.ExtensibilityKind;
import com.rti.dds.typecode.StructMember;
import com.rti.dds.typecode.TCKind;
import com.rti.dds.typecode.TypeCode;
import com.rti.dds.typecode.TypeCodeFactory;


public class TypeCodeMapper {

	static final Map<String, TypeCode> nameToCode = new HashMap<>();
	static{
	
		add(TypeCode.TC_BOOLEAN);
		add(TypeCode.TC_CHAR);
		add(TypeCode.TC_DOUBLE);
		add(TypeCode.TC_FLOAT);
		add(TypeCode.TC_LONG);
		add(TypeCode.TC_LONGDOUBLE);
		add(TypeCode.TC_LONGLONG);
		add(TypeCode.TC_OCTET);
		add(TypeCode.TC_SHORT);
		add(TypeCode.TC_ULONG);
		add(TypeCode.TC_ULONGLONG);
		add(TypeCode.TC_USHORT);
		add(TypeCode.TC_WCHAR);
	}
	
	static public void add(TypeCode code){
		nameToCode.put(code.get_type_as_string(), code);
	}

	static public void addAlias(String name, TypeCode code){
		nameToCode.put(name, code);
	}

	
	public static TypeCode getTypeCode(String name) {
		TypeCode val = nameToCode.get(name);
		return val;
	}
	
	static final Map<String, IDDSDataReader<?>> nameToDataReader = new HashMap<>();
	static{
		// TODO: Register further functions
		add(TypeCode.TC_LONG, new DDSLongDataReader());
	}
	
	public static void add(TypeCode code, IDDSDataReader<?> dataReader){
		add(code.get_type_as_string(), dataReader);
	}
	
	public static void add(String typeName,
			IDDSDataReader<?> dataReader) {
		nameToDataReader.put(typeName, dataReader);
	}
	
	public static IDDSDataReader<?> getDataReader(String typeName){
		IDDSDataReader<?> val = nameToDataReader.get(typeName);
		return val;
	}

	public static IDDSDataReader<?> getDataReader(TypeCode type){
		IDDSDataReader<?> val = nameToDataReader.get(type.get_type_as_string());
		return val;
	}

	public static TypeCode createTypeCode(String type) {
		// Test if type is simple or complex
		final TypeCode tc;
		int pos = type.indexOf("<");
		if (pos > 0){
			String typePart = type.substring(0, pos);
			// TODO: be more generic
			if (typePart.equalsIgnoreCase("string")){
				int size = getSize(type, pos); 
				tc = new TypeCode(TCKind.TK_STRING, size);
			}else if (typePart.equalsIgnoreCase("wstring")){
				int size = getSize(type, pos); 
				tc = new TypeCode(TCKind.TK_WSTRING, size);	
			}else if (typePart.equalsIgnoreCase("sequence")){
				// sequence<Type,size>
				int pos2 = type.indexOf(",");
				int pos3 = type.indexOf(">");
				String subtype = type.substring(pos+1, pos2);
				TypeCode subTypeCode = getTypeCode(subtype);
				if (subTypeCode == null){
					throw new IllegalArgumentException("Type "+subtype+" not found");
				}
				String sizeStr = type.substring(pos2+1, pos3);
				// ToDo: Check const
				int size = Integer.parseInt(sizeStr);
				tc = new TypeCode(size,subTypeCode);
			}else{
				throw new IllegalArgumentException("Type "+type+" not known");
			}
			nameToCode.put(type, tc);
		}else{
			throw new IllegalArgumentException("Type "+type+" not known");
		}
		return tc;
	}
	
	public static TypeCode getOrCreateTypeCode(String type){
		TypeCode tc = getTypeCode(type);
		if (tc == null){
			tc = createTypeCode(type);
		}
		return tc;
	}

	private static int getSize(String type, int pos) {
		int pos2 = type.indexOf(">");
		String sizeStr = type.substring(pos+1, pos2);
		// Todo: Could be a constant, replace
		// and store into sizeStr
		int size = Integer.parseInt(sizeStr);
		return size;
	}

	public static TypeCode createComplexType(String name, List<String> attributes,
			List<TypeCode> types, List<Boolean> isKey) {

		StructMember sm[] = new StructMember[attributes.size()];
		for (int pos = 0; pos < attributes.size(); pos++) {
			sm[pos] = new StructMember(attributes.get(pos), false, (short) -1,
					isKey.get(pos),types.get(pos), pos,
					false);
		}

		TypeCode newType = TypeCodeFactory.TheTypeCodeFactory.create_struct_tc(
				name, ExtensibilityKind.EXTENSIBLE_EXTENSIBILITY, sm);
		TypeCodeMapper.add(newType);

		TypeCodeMapper.add(newType, new DDSDynamicDataDataReader(attributes,
				types));

		return newType;
	}
	
	public static TypeCode createEnumType(String name, List<String> values){
		EnumMember[] em = new EnumMember[values.size()];
		for (int pos = 0;pos<values.size();pos++){
			em[pos] = new EnumMember(values.get(pos), pos);
		}	
		TypeCode newType = TypeCodeFactory.TheTypeCodeFactory.create_enum_tc(name, em);
		TypeCodeMapper.add(newType);
		return newType;
	}
	
}
