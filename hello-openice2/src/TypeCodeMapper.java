import java.util.HashMap;
import java.util.Map;

import com.rti.dds.typecode.TCKind;
import com.rti.dds.typecode.TypeCode;


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
		//TCKind.TK_ALIAS;
	}
	
}
