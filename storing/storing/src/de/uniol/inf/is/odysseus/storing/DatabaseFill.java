package de.uniol.inf.is.odysseus.storing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

public class DatabaseFill {

	private String tableName;
	private SDFAttributeList schema;

	public DatabaseFill(String name, SDFAttributeList schema) {
		this.tableName = name;
		this.schema = schema;
	}

	public static void fillDB() {
		String tableName = "testTable";

		Connection con = DatabaseServiceLoader.getConnection();
		Statement s;
		try {
			s = con.createStatement();
			s.execute("DROP TABLE " + tableName);
		} catch (SQLException e1) {
			System.err.println("kein drop, weil es nicht existierte...");
		}

		// schema
		SDFAttributeList list = new SDFAttributeList();
		SDFAttribute a = new SDFAttribute(tableName, "timestart");
		a.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
		list.add(a);
		
		a = new SDFAttribute(tableName, "timeend");
		a.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
		list.add(a);
		
		a = new SDFAttribute(tableName, "name");
		a.setDatatype(SDFDatatypeFactory.getDatatype("String"));
		list.add(a);
		
		a = new SDFAttribute(tableName, "price");
		a.setDatatype(SDFDatatypeFactory.getDatatype("Double"));
		list.add(a);

		DatabaseFill df = new DatabaseFill(tableName, list);
		df.createTable();

		PreparedStatement ps;
		try {
			ps = con.prepareStatement("INSERT INTO "+tableName+" (timestart, timeend, name, price) VALUES (?, ?, ?, ?)");
			Random r = new Random();
			for (int i = 0; i < 100; i++) {
				ps.setLong(1, i * 500);
				ps.setLong(2, (i*500)+2);
				ps.setString(3, "Beispiel " + i);
				ps.setDouble(4, r.nextDouble() + r.nextInt(100));
				ps.executeUpdate();
			}
			ps.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void createTable() {
		Connection conn = DatabaseServiceLoader.getConnection();
		Statement s;
		try {
			s = conn.createStatement();
		} catch (SQLException e) {
			// exists...
			e.printStackTrace();
			return;
		}

		String query = "CREATE TABLE " + this.tableName + "(";
		String delimiter = "";
		for (SDFAttribute attribute : this.schema) {
			String name = attribute.getAttributeName();
			SDFDatatype type = attribute.getDatatype();
			String typeString = getSQLType(type);
			query = query + delimiter + name + " " + typeString;
			delimiter = ", ";
		}
		query = query + ")";
		System.out.println("CREATING TABLE: " + query);
		try {
			s.execute(query);
		} catch (SQLException e) {
			e.printStackTrace(System.err);
		}
	}

	private String getSQLType(SDFDatatype type) {
		if (SDFDatatypes.isDouble(type)) {
			return "DOUBLE";
		}
		if (SDFDatatypes.isDate(type)) {
			return "DATE";
		}
		if (SDFDatatypes.isInteger(type)) {
			return "INTEGER";
		}
		if (SDFDatatypes.isString(type)) {
			return "VARCHAR(255)";
		}
		if (SDFDatatypes.isLong(type)) {
			return "BIGINT";
		}
		return "VARCHAR(255)";
	}
}
