package de.uniol.inf.is.odysseus.wrapper.dds.dds.kind;

import com.rti.dds.typecode.TCKind;

public class DDSWStringDataReader extends DDSStringDataReader {

		@Override
		public TCKind getKind() {
			return TCKind.TK_WSTRING;
		}
}
