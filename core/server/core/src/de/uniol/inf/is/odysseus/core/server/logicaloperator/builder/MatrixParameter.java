/**
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.server.logicaloperator.builder;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class MatrixParameter extends AbstractParameter<double[][]> {

    /**
     * 
     */
    private static final long serialVersionUID = 4248966349007802578L;

    public MatrixParameter(String name, REQUIREMENT requirement) {
        super(name, requirement, USAGE.RECENT);
    }

    public MatrixParameter(String name, REQUIREMENT requirement, USAGE usage) {
        super(name, requirement, usage);
    }

    public MatrixParameter() {
        super();
    }

    @Override
    protected void internalAssignment() {
    	if( inputValue instanceof double[][] ) {
    		setValue( (double[][]) inputValue);
    		return;
    	}
    	
        String representation = (String) this.inputValue;
        String[] rowsData = representation.split(";");
        int rows = rowsData.length;
        int columns = rowsData[0].split(",").length;
        double[][] values = new double[rows][columns];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < columns; c++) {
                String[] columnsData = rowsData[r].split(",");
                values[r][c] = Double.parseDouble(columnsData[c]);
            }
        }
        setValue(values);
    }

    @Override
    protected String getPQLStringInternal() {
        StringBuilder sb = new StringBuilder();
        for (int r = 0; r < getValue().length; r++) {
            if (r != 0) {
                sb.append(";");
            }
            for (int c = 0; c < getValue()[r].length; c++) {
                if (c != 0) {
                    sb.append(",");
                }
                sb.append(getValue()[r][c]);
            }
        }
        return "'" + sb.toString() + "'";
    }

}
