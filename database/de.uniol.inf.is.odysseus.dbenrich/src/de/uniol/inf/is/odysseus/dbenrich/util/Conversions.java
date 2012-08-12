package de.uniol.inf.is.odysseus.dbenrich.util;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class Conversions {

	public static SDFSchema createSDFSchemaByResultSetMetaData(ResultSetMetaData resultSetMetaData) throws SQLException {
		if(resultSetMetaData==null) {
			throw new RuntimeException("ResultSetMetaData could not be loaded");
		}

		ArrayList<SDFAttribute> dbAttributes = new ArrayList<SDFAttribute>();

		for (int i=1; i<=resultSetMetaData.getColumnCount(); i++) {
			String label = resultSetMetaData.getColumnLabel(i);
			int sqlType  = resultSetMetaData.getColumnType(i);

			dbAttributes.add(createSDFAttribute(label, sqlType));
		}

		SDFSchema dbFetchSchema = new SDFSchema("", dbAttributes);

		return dbFetchSchema;
	}

	private static SDFAttribute createSDFAttribute(String label, int sqlType) {
		SDFDatatype sdfDatatype = null;

		/*
		 * The mapping java.sql.Types.X -> SDFDatatype.Y
		 * 
		 * Note: It's basically the same as in
		 * de.uniol.inf.is.odysseus.database.Activator.start
		 * but a full registry is not needed in this bundle, and by default
		 * there is no registry/mapping already available anyway
		 */
		switch(sqlType) {
			case Types.ARRAY: sdfDatatype = SDFDatatype.OBJECT; break;
			case Types.BIGINT: sdfDatatype = SDFDatatype.LONG; break;
			case Types.BINARY: sdfDatatype = SDFDatatype.LONG; break;
			case Types.BIT: sdfDatatype = SDFDatatype.BOOLEAN; break;
			case Types.BLOB: sdfDatatype = SDFDatatype.OBJECT; break;
			case Types.BOOLEAN: sdfDatatype = SDFDatatype.BOOLEAN; break;
			case Types.CHAR: sdfDatatype = SDFDatatype.STRING; break;
			case Types.CLOB: sdfDatatype = SDFDatatype.OBJECT; break;
			case Types.DATALINK: sdfDatatype = SDFDatatype.OBJECT; break;
			case Types.DATE: sdfDatatype = SDFDatatype.DATE; break;
			case Types.DECIMAL: sdfDatatype = SDFDatatype.INTEGER; break;
			case Types.DISTINCT: sdfDatatype = SDFDatatype.OBJECT; break;
			case Types.DOUBLE: sdfDatatype = SDFDatatype.DOUBLE; break;
			case Types.FLOAT: sdfDatatype = SDFDatatype.FLOAT; break;
			case Types.INTEGER: sdfDatatype = SDFDatatype.INTEGER; break;
			case Types.JAVA_OBJECT: sdfDatatype = SDFDatatype.OBJECT; break;
			case Types.LONGNVARCHAR: sdfDatatype = SDFDatatype.STRING; break;
			case Types.LONGVARBINARY: sdfDatatype = SDFDatatype.LONG; break;
			case Types.LONGVARCHAR: sdfDatatype = SDFDatatype.STRING; break;
			case Types.NCHAR: sdfDatatype = SDFDatatype.STRING; break;
			case Types.NCLOB: sdfDatatype = SDFDatatype.OBJECT; break;
			case Types.NULL: sdfDatatype = SDFDatatype.OBJECT; break;
			case Types.NUMERIC: sdfDatatype = SDFDatatype.DOUBLE; break;
			case Types.NVARCHAR: sdfDatatype = SDFDatatype.STRING; break;
			case Types.OTHER: sdfDatatype = SDFDatatype.OBJECT; break;
			case Types.REAL: sdfDatatype = SDFDatatype.FLOAT; break;
			case Types.REF: sdfDatatype = SDFDatatype.OBJECT; break;
			case Types.ROWID: sdfDatatype = SDFDatatype.OBJECT; break;
			case Types.SMALLINT: sdfDatatype = SDFDatatype.INTEGER; break;
			case Types.SQLXML: sdfDatatype = SDFDatatype.STRING; break;
			case Types.STRUCT: sdfDatatype = SDFDatatype.OBJECT; break;
			case Types.TIME: sdfDatatype = SDFDatatype.TIMESTAMP; break;
			case Types.TIMESTAMP: sdfDatatype = SDFDatatype.TIMESTAMP; break;
			case Types.TINYINT: sdfDatatype = SDFDatatype.INTEGER; break;
			case Types.VARBINARY: sdfDatatype = SDFDatatype.LONG; break;
			case Types.VARCHAR: sdfDatatype = SDFDatatype.STRING; break;

			default: throw new RuntimeException("SQLType not supported yet!");
		}

		// The source name could also include e.g. the connection name
		return new SDFAttribute("dbenrich", label, sdfDatatype);
	}
}
