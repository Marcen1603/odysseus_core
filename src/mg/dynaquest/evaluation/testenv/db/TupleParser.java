/*
 * Created on 01.11.2005
 *
 */
package mg.dynaquest.evaluation.testenv.db;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import mg.dynaquest.evaluation.testenv.shared.misc.Tuple;
import mg.dynaquest.evaluation.testenv.shared.misc.Value;


/**
 * Übersetzt das Ergebnis einer Datenbank-Anfrage (<code>ResultSet</code>) in ein 
 * Feld von Tupeln (<code>Tuple</code>), wie es von den Datenquellen benötigt wird.
 * Indizes von Arrays, Vectoren, ... beginnen nach Java-Art wann immer möglich bei <code>0</code>.
 * 
 * @see shared.misc.Tuple
 * @author <a href="mailto:tmueller@polaris-neu.offis.uni-oldenburg.de">Tobias Mueller</a>
 *
 */
public class TupleParser {
	
	/**
	 * Übersetzt ein das Ergebnis der DB-Anfrage (<code>ResultSet</code>) in ein Array von 
	 * Tupeln ({@link Tuple Tuple}).
     *
	 * <p>
	 * TODO leere Ergebnisse und null-Werte behandeln 
	 * 
	 * 
	 * @param rs        das Ergebnis der SQL-Anfrage
	 * @return          ein Feld von Tupeln  
	 * 					
	 */
	public static Tuple[] parseTuples(ResultSet rs)  {
		Vector<Tuple>  rrv = new Vector<Tuple>();
		Tuple rr = null;
		// Zeilenzahl des ResultSets
		int columns = 0;
		// Vollständige Java-Klassennamen für die Objekte, die die getObject()-Methode
		// der Klasse ResultSet beim Aufruf mit dem entsprechenden Zeilenindex 
		// erzeugen würde (z.B. 'java.lang.String' für VARCHAR2) 
		String[] columnClassNames = null;
		// Entsprechende Java-Typen, kodiert durch die Konstanten der Klasse Values
		int[] columnTypes = null;
		
		// auslesen der Metadaten des ResultSets
		try  {
			ResultSetMetaData rsmd = rs.getMetaData();
			columns = rsmd.getColumnCount();
			System.out.println("TupleParser.parseResultRows: ResultSet hat " +
					columns + " Spalten");
            
			columnClassNames = new String[columns];
			for (int i = 0; i < columns; i++)  {
				columnClassNames[i] = rsmd.getColumnClassName(i + 1);
				System.out.println("TupleParser.parseResultRows: " + i + ". Zeilentyp: "
						+ columnClassNames[i]);
			}
		} catch(SQLException e)  {
			System.out.println("TupleParser.parseTuples: Fehler beim Lesen der ResultSet-Metadaten:");
			e.printStackTrace();
			return null;
		}
		
		// Übersetzen der Klassennamen in die Konstanten der Klasse Value
		columnTypes = new int[columns];
		String name;
		for (int i = 0; i < columnClassNames.length; i++)  {
			name = columnClassNames[i];
			if (name.equals("java.lang.String"))
				columnTypes[i] = Value.STRING;
			else if (name.equals("java.lang.Integer"))
				columnTypes[i] = Value.INT;
			else if (name.equals("java.lang.Long"))
				columnTypes[i] = Value.LONG;
			else if (name.equals("java.lang.Double"))
				columnTypes[i] = Value.DOUBLE;
			else if (name.equals("java.lang.Float"))
				columnTypes[i] = Value.FLOAT;
			else if (name.equals("java.math.BigDecimal")) 
				columnTypes[i] = Value.BIGDEC;
			else   {
				System.out.println("TupleParser.parseResultRows: Klasse " + columnClassNames[i]
				                                           + " konnte nicht zugeordnet werden");
				System.exit(1);
				// TODO nicht hier!!! Exception o.ä.
			}
		}
		
		// erzeugen der ResultRows, abrufen des ResultSets mit Hilfe der 
		// Metainformationen
		try  {
			String s;
			int i;
			long l;
			float f;
			double d;
			BigDecimal b;
            int rows = 0;
			while(rs.next())  {
                rows++;
				rr = new Tuple(columns);
				for (int j = 0; j < columns; j++)  {
					switch (columnTypes[j])  {
					case Value.STRING:
						s = rs.getString(j + 1);
						rr.setValue(j, new Value(s,null,null));
						break;
					case Value.BIGDEC:				
						b = rs.getBigDecimal(j + 1);
						rr.setValue(j, new Value(b,null,null));
					case Value.INT:
						i = rs.getInt(j + 1);
						rr.setValue(j, new Value(i,null,null));
						break;
					case Value.LONG:
						l = rs.getLong(j + 1);
						rr.setValue(j, new Value(l,null,null));
						break;
					case Value.FLOAT:
						f = rs.getFloat(j + 1);
						rr.setValue(j, new Value(f,null,null));
						break;
					case Value.DOUBLE:
						d = rs.getDouble(j + 1);
						rr.setValue(j, new Value(d,null,null));
						break;	
					}
				}
				// Tupel an den Vector hängen, da Anzahl im voraus nicht bekannt.
				rrv.add(rr);
			}
            
            System.out.println("TupleParser.parseResultRows: ResultSet hat " +
                    rows + " Zeilen");

			
		} catch (SQLException e)  {
			System.out.println("TupleParser.parseResultRows: Fehler beim Auslesen des ResultSet");
			// TODO hier nicht !!!
			System.exit(1);
		}
		
		// Anzahl der Tupel ist jetzt bekannt -> in Feld speichern
//		Tuple[] rra = new Tuple[rrv.size()];
//		for (int i = 0; i < rrv.size(); i++) {
//			rra[i] = (Tuple)rrv.get(i);
//		}
		return rrv.toArray(new Tuple[rrv.size()]);
	}
}
