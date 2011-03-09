/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.scars.util.helper;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Hilfsklasse. Damit lassen sich die verschiedenen Informationen zu Tupel und
 * Schemata in der Konsole ausgeben lassen. Diese Klasse beinhaltet nur
 * statische Methoden. Eine Instanziierung ist nicht möglich.
 * 
 * @author Timo Michelsen
 * 
 */
public class UtilPrinter {

  // Privat. Nicht benötigt.
  private UtilPrinter() {

  }

  /**
   * Schreibt einen vollständigen Tuple in die Konsole. Das Schema ist nötig, um
   * die notwendigen Informationen zusammen zu tragen. Schema und Tupel könne
   * objektrelational sein. Die Ausgabe beginnt bei dem angegegbenen
   * Attributnamen. Alle Attribute oberhalb des angegebenen Attributes werden
   * nicht ausgegeben.
   * <p>
   * Die Ausgabe geschieht verschachtelt. Im Tupelbaum wird erst in die Tiefe
   * verzweigt, danach werden die Geschwisterknoten ausgegeben.
   * 
   * @param tuple
   *          Vollständiger Tupel (evtl. objektrelational), welcher in der
   *          Konsole ausgegeben werden soll. Darf nicht <code>null</code> sein.
   * @param schema
   *          Vollständiges Schema, welcher zum angegebenen Tupel gehört(evtl.
   *          objektrelational). Darf nicht <code>null</code> sein.
   * @param attributeName
   *          Name das Attributs im Schema, wo mit der Ausgabe begonnen werden
   *          soll. Darf nicht <code>null</code> sein.
   * @throws IllegalArgumentException
   *           Wenn mindestens einer der angegebenen Parameter <code>null</code>
   *           ist.
   */
  public static void printTuple(MVRelationalTuple<?> tuple, SDFAttributeList schema, String attributeName) {
    if (tuple == null)
      throw new IllegalArgumentException("tuple is null");
    if (schema == null)
      throw new IllegalArgumentException("schema is null");
    if (attributeName == null)
      throw new IllegalArgumentException("attributeName is null");

    SchemaHelper pathHelper = new SchemaHelper(schema);

    SchemaIndexPath pathToSomeList = pathHelper.getSchemaIndexPath(attributeName);

    for (TupleInfo info : new TupleIterator(tuple, pathToSomeList)) {

      // Tabs für Hierarchie
      for (int i = 0; i < info.level; i++)
        System.out.print("\t");

      // Infos ausgeben
      Object obj = info.tupleObject;
      if (obj instanceof MVRelationalTuple) {
        System.out.print("TUPLE");
      } else {
        System.out.print(obj);
      }
      System.out.print(" (" + info.attribute.getAttributeName() + " : " + info.attribute.getDatatype().getQualName()
          + ")");
      System.out.print(info.tupleIndexPath);
      System.out.print(info.schemaIndexPath);
      System.out.println(info.schemaIndexPath.getFullAttributeName());
    }
  }

  /**
   * Gibt das angegebene, vollständige Schema in der Konsole aus. Die Ausgabe
   * ist verschachtelt, um Hierarchien zu erkennen. Objektrelationale Schemata
   * sind erlaubt.
   * <p>
   * Bei der Ausgabe wird erst in die Tiefe des Schemabaums gegangen, danach
   * werden die Geschwisterattribute besucht.
   * 
   * @param schema
   *          Schema, welches ausgegeben werden soll. Kann ein Subschema sein.
   *          Darf aber nicht <code>null</code> sein.
   * @throws IllegalArgumentException
   *           Wenn das angegebene Schema <code>null</code> ist.
   */
  public static void printSchema(SDFAttributeList schema) {
    if (schema == null)
      throw new IllegalArgumentException("schema is null");

    for (SchemaInfo info : new SchemaIterator(schema)) {

      // Tabs für Hierarchie
      for (int i = 0; i < info.level; i++)
        System.out.print("\t");

      System.out.print(info.schemaIndexPath.getFullAttributeName() + " ");
      System.out.print(info.attribute.getAttributeName());
      System.out.print(" (" + info.attribute.getDatatype().getQualName() + ")");
      System.out.println(info.schemaIndexPath);
    }
  }

}
