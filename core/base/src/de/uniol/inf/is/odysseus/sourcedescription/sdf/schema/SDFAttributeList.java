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
package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SDFAttributeList extends SDFSchemaElementSet<SDFAttribute>
        implements Comparable<SDFAttributeList>, Serializable {

    private static final long serialVersionUID = 5218658682722448980L;

    public SDFAttributeList(String URI) {
        super(URI);
    }

    public SDFAttributeList() {
        super();
    }

    /**
     * @param attributes1
     */
    public SDFAttributeList(SDFAttributeList attributes1) {
        super(attributes1);
    }

    public SDFAttributeList(SDFAttribute[] attributes1) {
        super();
        for (SDFAttribute a : attributes1) {
            this.add(a);
        }
    }

    public SDFAttributeList(Collection<SDFAttribute> attributes1) {
        super();
        super.addAll(attributes1);
    }

    @Override
    public SDFAttributeList clone() {
        return new SDFAttributeList(this);
    }

    public void addAttribute(SDFAttribute attribute) {
        super.add(attribute);
    }

    public void addAttributes(SDFAttributeList attributes) {
        super.addAll(attributes);
    }

    public int getAttributeCount() {
        return super.size();
    }

    public SDFAttribute getAttribute(int index) {
        return super.get(index);
    }

    public int indexOf(SDFAttribute attribute) {
        return super.indexOf(attribute);
    }

    /**
     * @param requiredAttributes
     * @param requiredAttributes2
     * @return
     */
    public static SDFAttributeList union(SDFAttributeList attributes1,
            SDFAttributeList attributes2) {
        // Zunaechst die beiden Trivialfaelle, wenn eine der beiden Mengen leer
        // ist,
        // ist die andere das Ergebnise
        if (attributes1 == null || attributes1.getAttributeCount() == 0) {
            return attributes2;
        }
        if (attributes2 == null || attributes2.getAttributeCount() == 0) {
            return attributes1;
        }
        SDFAttributeList newSet = new SDFAttributeList(attributes1);
        for (int i = 0; i < attributes2.size(); i++) {
            if (!newSet.contains(attributes2.getAttribute(i))) {
                newSet.addAttribute(attributes2.getAttribute(i));
            }
        }
        return newSet;
    }

    /**
     * attributes1 - attributes2
     * 
     * @param attributes1
     * @param attributes2
     * @return
     */
    public static SDFAttributeList difference(SDFAttributeList attributes1,
            SDFAttributeList attributes2) {

        SDFAttributeList newSet = new SDFAttributeList(attributes1);
        for (int j = 0; j < attributes2.getAttributeCount(); j++) {
            SDFAttribute nextAttr = attributes2.getAttribute(j);

            if (newSet.contains(nextAttr)) {
                newSet.remove(nextAttr);
            }
        }

        return newSet;
    }

    /**
     * Berechnet den Durchschnitt der beiden Mengen, also die Menge der
     * Elemente, welche in beiden Sets vorkommen
     * 
     * @param attributes1
     *            Set1
     * @param attributes2
     *            Set2
     * @return
     */
    public static SDFAttributeList intersection(SDFAttributeList attributes1,
            SDFAttributeList attributes2) {
        SDFAttributeList newSet = new SDFAttributeList();
        for (int j = 0; j < attributes1.getAttributeCount(); j++) {
            SDFAttribute nextAttr = attributes1.getAttribute(j);

            if (attributes2.contains(nextAttr)) {
                newSet.addAttribute(nextAttr);
            }
        }
        return newSet;
    }

    /**
     * Testet ob alle Attribute aus attribs1 ins attribs2 enthalten sind
     * 
     * @param attribs1
     * @param attribs2
     * @return
     */
    public static boolean subset(SDFAttributeList attribs1,
            SDFAttributeList attribs2) {
        return attribs2.elements.containsAll(attribs1.elements);
    }

    public static boolean subset(List<SDFAttribute> attribs1,
            SDFAttributeList attribs2) {
        return attribs2.elements.containsAll(attribs1);
    }

    /**
     * Checks whether this schema is union compatible to another schema. This
     * means: all the attributes of each schema have the same datatype.
     */
    public boolean compatibleTo(SDFAttributeList other) {
        if (other.size() != size()) {
            return false;
        }
        Iterator<SDFAttribute> it = other.iterator();
        for (SDFAttribute attrLeft : this) {
            SDFAttribute attrRight = it.next();
            if (!attrLeft.getDatatype().equals(attrRight.getDatatype())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks whether this schema is union compatible to another schema. This
     * means: all the attributes of each schema have the same datatype.
     */
    public static boolean compatible(SDFAttributeList left,
            SDFAttributeList right) {
        return left.compatibleTo(right);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(SDFAttributeList o) {
        int comp = 0;
        ListIterator<SDFAttribute> iter = super.listIterator();
        ListIterator<SDFAttribute> oIter = o.listIterator();
        while ((iter.hasNext()) && (oIter.hasNext())) {
            comp &= iter.next().compareTo(oIter.next());
        }
        return 0;
    }
}