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
package de.uniol.inf.is.odysseus.logicaloperator.builder;

import java.util.List;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

public class AggregateItemParameter extends AbstractParameter<AggregateItem> {

    private static final long serialVersionUID = 579333420134666505L;

    public AggregateItemParameter() {
        super();
    }

    public AggregateItemParameter(String name, REQUIREMENT requirement) {
        super(name, requirement);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void internalAssignment() {
        List<String> value = (List<String>) inputValue;
        if ((value.size() < 3) || (value.size() > 4)) {
            throw new IllegalParameterException("illegal value for aggregation");
        }

        String funcStr = value.get(0);
        String attributeStr = value.get(1);
        SDFAttribute attribute = getAttributeResolver().getAttribute(attributeStr);
        String outputName = value.get(2);
        SDFAttribute outAttr = new SDFAttribute(outputName);

        if (value.size() == 4) {
            String outputType = value.get(3);
            // Check for available SDFDatatypes
            // TODO Implement easy lookup table/dictionary for registered aggregation functions
            if (outputType.equalsIgnoreCase(SDFDatatype.OBJECT.getQualName())) {
                outAttr.setDatatype(SDFDatatype.OBJECT);
            }
            else if (outputType.equalsIgnoreCase(SDFDatatype.STRING.getQualName())) {
                outAttr.setDatatype(SDFDatatype.STRING);
            }
            else if (outputType.equalsIgnoreCase(SDFDatatype.LONG.getQualName())) {
                outAttr.setDatatype(SDFDatatype.LONG);
            }
            else if (outputType.equalsIgnoreCase(SDFDatatype.INTEGER.getQualName())) {
                outAttr.setDatatype(SDFDatatype.INTEGER);
            }
            else if (outputType.equalsIgnoreCase(SDFDatatype.FLOAT.getQualName())) {
                outAttr.setDatatype(SDFDatatype.FLOAT);
            }
            else if (outputType.equalsIgnoreCase(SDFDatatype.DOUBLE.getQualName())) {
                outAttr.setDatatype(SDFDatatype.DOUBLE);
            }
            else if (outputType.equalsIgnoreCase(SDFDatatype.DATE.getQualName())) {
                outAttr.setDatatype(SDFDatatype.DATE);
            }
            else if (outputType.equalsIgnoreCase(SDFDatatype.BOOLEAN.getQualName())) {
                outAttr.setDatatype(SDFDatatype.BOOLEAN);
            }
            else if (outputType.equalsIgnoreCase(SDFDatatype.SPATIAL_POINT.getQualName())) {
                outAttr.setDatatype(SDFDatatype.SPATIAL_POINT);
            }
            else if (outputType.equalsIgnoreCase(SDFDatatype.SPATIAL_MULTI_POINT.getQualName())) {
                outAttr.setDatatype(SDFDatatype.SPATIAL_MULTI_POINT);
            }
            else if (outputType.equalsIgnoreCase(SDFDatatype.SPATIAL_LINE.getQualName())) {
                outAttr.setDatatype(SDFDatatype.SPATIAL_LINE);
            }
            else if (outputType.equalsIgnoreCase(SDFDatatype.SPATIAL_MULTI_LINE.getQualName())) {
                outAttr.setDatatype(SDFDatatype.SPATIAL_MULTI_LINE);
            }
            else if (outputType.equalsIgnoreCase(SDFDatatype.SPATIAL_POLYGON.getQualName())) {
                outAttr.setDatatype(SDFDatatype.SPATIAL_POLYGON);
            }
            else if (outputType.equalsIgnoreCase(SDFDatatype.SPATIAL_MULTI_POLYGON.getQualName())) {
                outAttr.setDatatype(SDFDatatype.SPATIAL_MULTI_POLYGON);
            }
            else if (outputType.equalsIgnoreCase(SDFDatatype.SPATIAL.getQualName())) {
                outAttr.setDatatype(SDFDatatype.SPATIAL);
            }
            else if (outputType.equalsIgnoreCase(SDFDatatype.START_TIMESTAMP.getQualName())) {
                outAttr.setDatatype(SDFDatatype.START_TIMESTAMP);
            }
            else if (outputType.equalsIgnoreCase(SDFDatatype.END_TIMESTAMP.getQualName())) {
                outAttr.setDatatype(SDFDatatype.END_TIMESTAMP);
            }
            else if (outputType.equalsIgnoreCase(SDFDatatype.TIMESTAMP.getQualName())) {
                outAttr.setDatatype(SDFDatatype.TIMESTAMP);
            }
            else if (outputType.equalsIgnoreCase(SDFDatatype.POINT_IN_TIME.getQualName())) {
                outAttr.setDatatype(SDFDatatype.POINT_IN_TIME);
            }
            else if (outputType.equalsIgnoreCase(SDFDatatype.MV.getQualName())) {
                outAttr.setDatatype(SDFDatatype.MV);
            }
            else if (outputType.equalsIgnoreCase(SDFDatatype.MATRIX_DOUBLE.getQualName())) {
                outAttr.setDatatype(SDFDatatype.MATRIX_DOUBLE);
            }
            else if (outputType.equalsIgnoreCase(SDFDatatype.VECTOR_DOUBLE.getQualName())) {
                outAttr.setDatatype(SDFDatatype.VECTOR_DOUBLE);
            }
            else if (outputType.equalsIgnoreCase(SDFDatatype.TUPLE.getQualName())) {
                outAttr.setDatatype(SDFDatatype.TUPLE);
            }
            else {
                outAttr.setDatatype(SDFDatatype.DOUBLE);
            }
        }
        else {
            // Fallback to old DOUBLE value for aggregation results
            outAttr.setDatatype(SDFDatatype.DOUBLE);
        }
        setValue(new AggregateItem(funcStr, attribute, outAttr));
    }

}
