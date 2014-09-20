/**
* Copyright (c) 2006, Seweryn Habdank-Wojewodzki
* Copyright (c) 2006, Janusz Rybarski
*
* All rights reserved.
* 
* Redistribution and use in source and binary forms,
* with or without modification, are permitted provided
* that the following conditions are met:
*
* Redistributions of source code must retain the above
* copyright notice, this list of conditions and the
* following disclaimer.
*
* Redistributions in binary form must reproduce the
* above copyright notice, this list of conditions
* and the following disclaimer in the documentation
* and/or other materials provided with the distribution.
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS
* AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
* WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
* WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
* A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
* THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
* DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
* PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
* USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
* HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
* WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
* (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
* WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
* OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package topology;


import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Matric Topology is a topology where neurons is set in rows and columns.
 * @author Janusz Rybarski
 * e-mail: janusz.rybarski AT ae DOT krakow DOT pl
 * @author Seweryn Habdank-Wojewdzki
 * e-mail: habdank AT megapolis DOT pl
 * @version 1.0 2006/05/02
 */

public class MatrixTopology implements TopologyModel {
    private int colNumber, rowNumber;
    private int radius = 0;

    /**
     * Creates a new instance of matrixTopology with specified numbers of rows and columns. 
     * Radius is default set to <I>0</I>
     * @param row number of rows
     * @param col number of columns
     */
    public MatrixTopology(int row, int col) {
        this.rowNumber = row;
        this.colNumber = col;
    }

    /**
     * Creates a new instance of matrixTopology with specified numbers of rows, columns and radius.
     * @param row number of rows
     * @param col number of columns
     * @param radius radius
     */
    public MatrixTopology(int row, int col, int radius) {
        this(row, col);
        this.radius = radius;
    }

    /**
     * Returns a string representation of the topology.<br>
     * <I>Neuron number</I><U> nr</U> <I>is connected with: </I> [ list of neurons ]
     * @return string representation of the topology.
     */
    @Override
	public String toString() {
        ArrayList<Integer> tempList = new ArrayList<Integer>();
        String    conn     = "";

        for (int i = 0; i < colNumber * rowNumber; i++) {
            tempList = getConnectedNeurons(i);
            conn += "Neuron number " + i + " is connected with: " + tempList + "\n";
        }
        return conn;
    }

   /**
     * Return number of columns
     * @return number of columns
     */
    @Override
	public int getColNumber() {
        return this.colNumber;
    }

    /**
     * Return ArrayList of neurons connected to neuron with <I>neuron Number</I>
     * @param neuronNumber neuron number
     * @return list of connected neurons
     * @see ArrayList
     */
    @Override
	public ArrayList<Integer> getConnectedNeurons(int neuronNumber) {
        ArrayList<Integer> connectedNeurons = new ArrayList<Integer>();

        if ((neuronNumber < colNumber * rowNumber) && (neuronNumber >= 0)){
            if (neuronNumber - colNumber >= 0) {
                connectedNeurons.add(neuronNumber - colNumber);
            }

            if ((neuronNumber - 1 >= 0) && ((neuronNumber % colNumber) != 0)) {
                connectedNeurons.add(neuronNumber - 1);
            }

            if ((neuronNumber + 1 < colNumber * rowNumber)
                    && ((neuronNumber % colNumber) != (colNumber - 1))) {
                connectedNeurons.add(neuronNumber + 1);
            }

            if (neuronNumber + colNumber < colNumber * rowNumber) {
                connectedNeurons.add(neuronNumber + colNumber);
            }
        }
      return connectedNeurons;   
    }

    /**
     * Return temporary Array List of neurons connected to neurons from input Array Lis
     * @param tempConnection array list of neurons
     * @return list of connected neurons
     */
    
    private ArrayList<Integer> getN(ArrayList<Integer> tempConnection) {
        ArrayList<Integer> neighborgoodConn = new ArrayList<Integer>();
        ArrayList<Integer> tempConn         = new ArrayList<Integer>();

        for (int j = 0; j < tempConnection.size(); j++) {
            tempConn = getConnectedNeurons(tempConnection.get(j));
            for (int i = 0; i < tempConn.size(); i++) {
                neighborgoodConn.add(tempConn.get(i));
            }
        }
        return neighborgoodConn;
    }

    /**
     * Return TreeMap containng information about neuron and distance to neuron for which neighbourhood is 
     * calculated
     * @param neuronNumber neuron number
     * @return Tree map containn neuron number and distance
     * @see TreeMap
     */
    @Override
	@SuppressWarnings("unchecked")
    public TreeMap<Integer,Integer> getNeighbourhood(int neuronNumber) {
        TreeMap<java.lang.Integer, java.lang.Integer> neighbornhood =
            new TreeMap<java.lang.Integer, java.lang.Integer>();
        ArrayList<Integer> tempConnection   = new ArrayList<Integer>();
        ArrayList<Integer> neighborgoodConn = new ArrayList<Integer>();

        tempConnection.add(neuronNumber);

        int   key;

        for (int i = 0; i < radius; i++) {
            neighborgoodConn = getN(tempConnection);

            for (int k = 0; k < neighborgoodConn.size(); k++) {
                key = neighborgoodConn.get(k);

                if (!neighbornhood.containsKey(key) && (key != neuronNumber)) {
                    neighbornhood.put(key, i + 1);
                }
            }
            
            tempConnection = (ArrayList<Integer>) neighborgoodConn.clone();
        }

        return neighbornhood;
    }

    /**
     * Return Coord object contain intormation about neuron co-ordinate
     * @param neuronNumber neuron number
     * @return coords object
     */
    @Override
	public Coords getNeuronCoordinate(int neuronNumber) {
        int x = ((neuronNumber - 1) / colNumber) + 1;
        int y = neuronNumber - ((x - 1) * colNumber);

        return new Coords(x, y);
    }

   /**
     * Return neuron number with specyfied co-ordiante
     * @param coords neuron coordinate
     * @return neuron number
     */
    @Override
	public int getNeuronNumber(Coords coords) {
        if ((coords.x < rowNumber) && (coords.y < colNumber)) {
            return (coords.x - 1) * colNumber + coords.y;
        }

        return -1;
    }

    /**
     * Return number of neuron.
     * @return number of neurons
     */
    @Override
	public int getNumbersOfNeurons() {
        return colNumber * rowNumber;
    }

     /**
     * Return radius for calculate neighbourhood
     * @return radius
     */
    @Override
	public int getRadius() {
        return radius;
    }

    /**
     * Return number of rows
     * @return numbers of rows
     */
    @Override
	public int getRowNumber() {
        return this.rowNumber;
    }

    /**
     * Set number of columns
     * @param colNumber numbers of columns
     */
    @Override
	public void setColNumber(int colNumber) {
        this.colNumber = colNumber;
    }

    /**
     * Set radius
     * @param radius Radius
     */
    @Override
	public void setRadius(int radius) {
        this.radius = radius;
    }

    /**
     * Set numbers of rows
     * @param rowNumber numbers of rows
     */
    @Override
	public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }
}