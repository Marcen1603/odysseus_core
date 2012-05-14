/*
 *    This program is free software; you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation; either version 2 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program; if not, write to the Free Software
 *    Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */

/*
 * ResultMatrix.java
 * Copyright (C) 2005 University of Waikato, Hamilton, New Zealand
 *
 */

package weka.experiment;

import weka.core.RevisionHandler;
import weka.core.Utils;

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;

/**
 * This matrix is a container for the datasets and classifier setups and 
 * their statistics. Derived classes output the data in different formats.
 * Derived classes need to implement the following methods:
 * <ul>
 *   <li><code>toStringMatrix()</code></li>
 *   <li><code>toStringKey()</code></li>
 *   <li><code>toStringHeader()</code></li>
 *   <li><code>toStringSummary()</code></li>
 *   <li><code>toStringRanking()</code></li>
 * </ul>
 *
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 1.9 $
 * @see #toStringMatrix()
 * @see #toStringKey()
 * @see #toStringHeader()
 * @see #toStringSummary()
 * @see #toStringRanking()
 */
public abstract class ResultMatrix
  implements Serializable, RevisionHandler {

  /** for serialization */
  private static final long serialVersionUID = 4487179306428209739L;
  
  /** tie */
  public final static int SIGNIFICANCE_TIE = 0;

  /** win */
  public final static int SIGNIFICANCE_WIN = 1;

  /** loss */
  public final static int SIGNIFICANCE_LOSS = 2;

  /** tie string */
  public String TIE_STRING = " ";

  /** win string */
  public String WIN_STRING = "v";

  /** loss string */
  public String LOSS_STRING = "*";

  /** the left parentheses for enumerating cols/rows */
  public String LEFT_PARENTHESES = "(";

  /** the right parentheses for enumerating cols/rows */
  public String RIGHT_PARENTHESES = ")";

  /** the column names */
  protected String[] m_ColNames = null;

  /** the row names */
  protected String[] m_RowNames = null;

  /** whether a column is hidden */
  protected boolean[] m_ColHidden = null;
  
  /** whether a row is hidden */
  protected boolean[] m_RowHidden = null;
  
  /** the significance */
  protected int[][] m_Significance = null;

  /** the values */
  protected double[][] m_Mean = null;

  /** the standard deviation */
  protected double[][] m_StdDev = null;

  /** the counts for the different datasets */
  protected double[] m_Counts = null;

  /** the standard mean precision */
  protected int m_MeanPrec;

  /** the standard std. deviation preicision */
  protected int m_StdDevPrec;

  /** whether std. deviations are printed as well */
  protected boolean m_ShowStdDev;

  /** whether the average for each column should be printed */
  protected boolean m_ShowAverage;
  
  /** whether the names or numbers are output as column declarations */
  protected boolean m_PrintColNames;

  /** whether the names or numbers are output as row declarations */
  protected boolean m_PrintRowNames;

  /** whether a "(x)" is printed before each column name with "x" as the
   * index */
  protected boolean m_EnumerateColNames;

  /** whether a "(x)" is printed before each row name with "x" as the index */
  protected boolean m_EnumerateRowNames;

  /** the size of the names of the columns */
  protected int m_ColNameWidth;

  /** the size of the names of the rows */
  protected int m_RowNameWidth;

  /** the size of the mean columns */
  protected int m_MeanWidth;

  /** the size of the std dev columns */
  protected int m_StdDevWidth;

  /** the size of the significance columns */
  protected int m_SignificanceWidth;

  /** the size of the counts */
  protected int m_CountWidth;

  /** contains the keys for the header */
  protected Vector m_HeaderKeys = null;

  /** contains the values for the header */
  protected Vector m_HeaderValues = null;

  /** the non-significant wins */
  protected int[][] m_NonSigWins = null;

  /** the significant wins */
  protected int[][] m_Wins = null;

  /** the wins in ranking */
  protected int[] m_RankingWins = null;

  /** the losses in ranking */
  protected int[] m_RankingLosses = null;

  /** the difference between wins and losses */
  protected int[] m_RankingDiff = null;

  /** the ordering of the rows */
  protected int[] m_RowOrder = null;

  /** the ordering of the columns */
  protected int[] m_ColOrder = null;

  /** whether to remove the filter name from the dataaset name */
  protected boolean m_RemoveFilterName = false;
  
  /**
   * initializes the matrix as 1x1 matrix
   */
  public ResultMatrix() {
    this(1, 1);
  }
  
  /**
   * initializes the matrix with the given dimensions
   */
  public ResultMatrix(int cols, int rows) {
    setSize(cols, rows);
    clear();
  }

  /**
   * initializes the matrix with the values from the given matrix
   * @param matrix      the matrix to get the values from
   */
  public ResultMatrix(ResultMatrix matrix) {
    assign(matrix);
  }

  /**
   * returns the name of the output format
   */
  public abstract String getDisplayName();

  /**
   * acquires the data from the given matrix
   */
  public void assign(ResultMatrix matrix) {
    int         i;
    int         n;
    
    setSize(matrix.getColCount(), matrix.getRowCount());
    
    // output parameters
    TIE_STRING          = matrix.TIE_STRING;
    WIN_STRING          = matrix.WIN_STRING;
    LOSS_STRING         = matrix.LOSS_STRING;
    LEFT_PARENTHESES    = matrix.LEFT_PARENTHESES;
    RIGHT_PARENTHESES   = matrix.RIGHT_PARENTHESES;
    m_MeanPrec          = matrix.m_MeanPrec;
    m_StdDevPrec        = matrix.m_StdDevPrec;
    m_ShowStdDev        = matrix.m_ShowStdDev;
    m_ShowAverage       = matrix.m_ShowAverage;
    m_PrintColNames     = matrix.m_PrintColNames;
    m_PrintRowNames     = matrix.m_PrintRowNames;
    m_EnumerateColNames = matrix.m_EnumerateColNames;
    m_EnumerateRowNames = matrix.m_EnumerateRowNames;
    m_RowNameWidth      = matrix.m_RowNameWidth;
    m_MeanWidth         = matrix.m_MeanWidth;
    m_StdDevWidth       = matrix.m_StdDevWidth;
    m_SignificanceWidth = matrix.m_SignificanceWidth;
    m_CountWidth        = matrix.m_CountWidth;
    m_RemoveFilterName  = matrix.m_RemoveFilterName;
    
    // header
    m_HeaderKeys   = (Vector) matrix.m_HeaderKeys.clone();
    m_HeaderValues = (Vector) matrix.m_HeaderValues.clone();

    // matrix
    for (i = 0; i < matrix.m_Mean.length; i++) {
      for (n = 0; n < matrix.m_Mean[i].length; n++) {
        m_Mean[i][n]         = matrix.m_Mean[i][n];
        m_StdDev[i][n]       = matrix.m_StdDev[i][n];
        m_Significance[i][n] = matrix.m_Significance[i][n];
      }
    }

    for (i = 0; i < matrix.m_ColNames.length; i++) {
      m_ColNames[i]  = matrix.m_ColNames[i];
      m_ColHidden[i] = matrix.m_ColHidden[i];
    }

    for (i = 0; i < matrix.m_RowNames.length; i++) {
      m_RowNames[i]  = matrix.m_RowNames[i];
      m_RowHidden[i] = matrix.m_RowHidden[i];
    }

    for (i = 0; i < matrix.m_Counts.length; i++)
      m_Counts[i] = matrix.m_Counts[i];

    // summary
    if (matrix.m_NonSigWins != null) {
      m_NonSigWins = new int[matrix.m_NonSigWins.length][];
      m_Wins       = new int[matrix.m_NonSigWins.length][];
      for (i = 0; i < matrix.m_NonSigWins.length; i++) {
        m_NonSigWins[i] = new int[matrix.m_NonSigWins[i].length];
        m_Wins[i]       = new int[matrix.m_NonSigWins[i].length];

        for (n = 0; n < matrix.m_NonSigWins[i].length; n++) {
          m_NonSigWins[i][n] = matrix.m_NonSigWins[i][n];
          m_Wins[i][n]       = matrix.m_Wins[i][n];
        }
      }
    }

    // ranking
    if (matrix.m_RankingWins != null) {
      m_RankingWins   = new int[matrix.m_RankingWins.length];
      m_RankingLosses = new int[matrix.m_RankingWins.length];
      m_RankingDiff   = new int[matrix.m_RankingWins.length];
      for (i = 0; i < matrix.m_RankingWins.length; i++) {
        m_RankingWins[i]   = matrix.m_RankingWins[i];
        m_RankingLosses[i] = matrix.m_RankingLosses[i];
        m_RankingDiff[i]   = matrix.m_RankingDiff[i];
      }
    }
  }

  /**
   * removes the stored data and the ordering, but retains the dimensions of
   * the matrix
   */
  public void clear() {
    m_MeanPrec          = 2;
    m_StdDevPrec        = 2;
    m_ShowStdDev        = false;
    m_ShowAverage       = false;
    m_PrintColNames     = true;
    m_PrintRowNames     = true;
    m_EnumerateColNames = true;
    m_EnumerateRowNames = false;
    m_RowNameWidth      = 0;
    m_ColNameWidth      = 0;
    m_MeanWidth         = 0;
    m_StdDevWidth       = 0;
    m_SignificanceWidth = 0;
    m_CountWidth        = 0;

    setSize(getColCount(), getRowCount());
  }

  /**
   * clears the content of the matrix and sets the new size
   * @param cols        the number of mean columns
   * @param rows        the number of mean rows
   */
  public void setSize(int cols, int rows) {
    int       i;
    int       n;

    m_ColNames     = new String[cols];
    m_RowNames     = new String[rows];
    m_Counts       = new double[rows];
    m_ColHidden    = new boolean[cols];
    m_RowHidden    = new boolean[rows];
    m_Mean         = new double[rows][cols];
    m_Significance = new int[rows][cols];
    m_StdDev       = new double[rows][cols];
    m_ColOrder     = null;
    m_RowOrder     = null;

    // NaN means that there exists no value! -> toArray()
    for (i = 0; i < m_Mean.length; i++) {
      for (n = 0; n < m_Mean[i].length; n++)
        m_Mean[i][n]   = Double.NaN;
    }

    for (i = 0; i < m_ColNames.length; i++)
      m_ColNames[i] = "col" + i;
    for (i = 0; i < m_RowNames.length; i++)
      m_RowNames[i] = "row" + i;

    clearHeader();
    clearSummary();
    clearRanking();
  }

  /**
   * sets the precision for the means
   */
  public void setMeanPrec(int prec) {
    if (prec >= 0)
      m_MeanPrec = prec;
  }

  /**
   * returns the current precision for the means
   */
  public int getMeanPrec() {
    return m_MeanPrec;
  }

  /**
   * sets the precision for the standard deviation
   */
  public void setStdDevPrec(int prec) {
    if (prec >= 0)
      m_StdDevPrec = prec;
  }

  /**
   * returns the current standard deviation precision
   */
  public int getStdDevPrec() {
    return m_StdDevPrec;
  }

  /**
   * sets the width for the column names (0 = optimal)
   */
  public void setColNameWidth(int width) {
    if (width >= 0)
      m_ColNameWidth = width;
  }

  /**
   * returns the current width for the column names
   */
  public int getColNameWidth() {
    return m_ColNameWidth;
  }

  /**
   * sets the width for the row names (0 = optimal)
   */
  public void setRowNameWidth(int width) {
    if (width >= 0)
      m_RowNameWidth = width;
  }

  /**
   * returns the current width for the row names
   */
  public int getRowNameWidth() {
    return m_RowNameWidth;
  }

  /**
   * sets the width for the mean (0 = optimal)
   */
  public void setMeanWidth(int width) {
    if (width >= 0)
      m_MeanWidth = width;
  }

  /**
   * returns the current width for the mean
   */
  public int getMeanWidth() {
    return m_MeanWidth;
  }

  /**
   * sets the width for the std dev (0 = optimal)
   */
  public void setStdDevWidth(int width) {
    if (width >= 0)
      m_StdDevWidth = width;
  }

  /**
   * returns the current width for the std dev
   */
  public int getStdDevWidth() {
    return m_StdDevWidth;
  }

  /**
   * sets the width for the significance (0 = optimal)
   */
  public void setSignificanceWidth(int width) {
    if (width >= 0)
      m_SignificanceWidth = width;
  }

  /**
   * returns the current width for the significance
   */
  public int getSignificanceWidth() {
    return m_SignificanceWidth;
  }

  /**
   * sets the width for the counts (0 = optimal)
   */
  public void setCountWidth(int width) {
    if (width >= 0)
      m_CountWidth = width;
  }

  /**
   * returns the current width for the counts
   */
  public int getCountWidth() {
    return m_CountWidth;
  }

  /**
   * sets whether to display the std deviations or not
   */
  public void setShowStdDev(boolean show) {
    m_ShowStdDev = show;
  }

  /**
   * returns whether std deviations are displayed or not
   */
  public boolean getShowStdDev() {
    return m_ShowStdDev;
  }

  /**
   * sets whether to display the average per column or not
   */
  public void setShowAverage(boolean show) {
    m_ShowAverage = show;
  }

  /**
   * returns whether average per column is displayed or not
   */
  public boolean getShowAverage() {
    return m_ShowAverage;
  }

  /**
   * sets whether to remove the filter classname from the dataset name
   */
  public void setRemoveFilterName(boolean remove) {
    m_RemoveFilterName = remove;
  }

  /**
   * returns whether the filter classname is removed from the dataset name
   */
  public boolean getRemoveFilterName() {
    return m_RemoveFilterName;
  }

  /**
   * sets whether the column names or numbers instead are printed.
   * deactivating automatically sets m_EnumerateColNames to TRUE.
   * @see #setEnumerateColNames(boolean)
   */
  public void setPrintColNames(boolean print) {
    m_PrintColNames = print;
    if (!print)
      setEnumerateColNames(true);
  }

  /**
   * returns whether column names or numbers instead are printed
   */
  public boolean getPrintColNames() {
    return m_PrintColNames;
  }

  /**
   * sets whether the row names or numbers instead are printed
   * deactivating automatically sets m_EnumerateColNames to TRUE.
   * @see #setEnumerateRowNames(boolean)
   */
  public void setPrintRowNames(boolean print) {
    m_PrintRowNames = print;
    if (!print)
      setEnumerateRowNames(true);
  }

  /**
   * returns whether row names or numbers instead are printed
   */
  public boolean getPrintRowNames() {
    return m_PrintRowNames;
  }

  /**
   * sets whether the column names are prefixed with "(x)" where "x" is
   * the index
   */
  public void setEnumerateColNames(boolean enumerate) {
    m_EnumerateColNames = enumerate;
  }

  /**
   * returns whether column names or numbers instead are enumerateed
   */
  public boolean getEnumerateColNames() {
    return m_EnumerateColNames;
  }

  /**
   * sets whether to the row names or numbers instead are enumerateed
   */
  public void setEnumerateRowNames(boolean enumerate) {
    m_EnumerateRowNames = enumerate;
  }

  /**
   * returns whether row names or numbers instead are enumerateed
   */
  public boolean getEnumerateRowNames() {
    return m_EnumerateRowNames;
  }

  /**
   * returns the number of columns
   */
  public int getColCount() {
    return m_ColNames.length;
  }

  /**
   * returns the number of visible columns
   */
  public int getVisibleColCount() {
    int         cols;
    int         i;
    
    cols = 0;
    for (i = 0; i < getColCount(); i++) {
      if (!getColHidden(i))
        cols++;
    }

    return cols;
  }

  /**
   * returns the number of rows
   */
  public int getRowCount() {
    return m_RowNames.length;
  }

  /**
   * returns the number of visible rows
   */
  public int getVisibleRowCount() {
    int         rows;
    int         i;
    
    rows= 0;
    for (i = 0; i < getRowCount(); i++) {
      if (!getRowHidden(i))
        rows++;
    }

    return rows;
  }
  
  /**
   * sets the name of the column (if the index is valid)
   * @param index     the index of the column
   * @param name      the name of the column
   */
  public void setColName(int index, String name) {
    if ( (index >= 0) && (index < getColCount()) )
      m_ColNames[index] = name;
  }

  /**
   * returns the name of the row, if the index is valid, otherwise null.
   * if getPrintColNames() is FALSE then an empty string is returned or if
   * getEnumerateColNames() is TRUE then the 1-based index surrounded by
   * parentheses.
   * @see #setPrintColNames(boolean)
   * @see #getPrintColNames()
   * @see #setEnumerateColNames(boolean)
   * @see #getEnumerateColNames()
   */
  public String getColName(int index) {
    String        result;
    
    result = null;
    
    if ( (index >= 0) && (index < getColCount()) ) {
      if (getPrintColNames())
        result = m_ColNames[index];
      else
        result = "";

      if (getEnumerateColNames()) {
        result =   LEFT_PARENTHESES 
                 + Integer.toString(index + 1) 
                 + RIGHT_PARENTHESES
                 + " " + result;
        result = result.trim();
      }
    }

    return result;
  }

  /**
   * sets the name of the row (if the index is valid)
   * @param index     the index of the row
   * @param name      the name of the row
   */
  public void setRowName(int index, String name) {
    if ( (index >= 0) && (index < getRowCount()) )
      m_RowNames[index] = name;
  }

  /**
   * returns the name of the row, if the index is valid, otherwise null.
   * if getPrintRowNames() is FALSE then an empty string is returned or if
   * getEnumerateRowNames() is TRUE then the 1-based index surrounded by
   * parentheses.
   * @see #setPrintRowNames(boolean)
   * @see #getPrintRowNames()
   * @see #setEnumerateRowNames(boolean)
   * @see #getEnumerateRowNames()
   */
  public String getRowName(int index) {
    String        result;
    
    result = null;
    
    if ( (index >= 0) && (index < getRowCount()) ) {
      if (getPrintRowNames())
        result = m_RowNames[index];
      else
        result = "";

      if (getEnumerateRowNames()) {
        result =   LEFT_PARENTHESES 
                 + Integer.toString(index + 1) 
                 + RIGHT_PARENTHESES
                 + " " + result;
        result = result.trim();
      }
    }
    
    return result;
  }

  /**
   * sets the hidden status of the column (if the index is valid)
   * @param index       the index of the column
   * @param hidden      the hidden status of the column
   */
  public void setColHidden(int index, boolean hidden) {
    if ( (index >= 0) && (index < getColCount()) )
      m_ColHidden[index] = hidden;
  }

  /**
   * returns the hidden status of the column, if the index is valid, otherwise
   * false
   */
  public boolean getColHidden(int index) {
    if ( (index >= 0) && (index < getColCount()) )
      return m_ColHidden[index];
    else
      return false;
  }

  /**
   * sets the hidden status of the row (if the index is valid)
   * @param index       the index of the row
   * @param hidden      the hidden status of the row
   */
  public void setRowHidden(int index, boolean hidden) {
    if ( (index >= 0) && (index < getRowCount()) )
      m_RowHidden[index] = hidden;
  }

  /**
   * returns the hidden status of the row, if the index is valid, otherwise
   * false
   */
  public boolean getRowHidden(int index) {
    if ( (index >= 0) && (index < getRowCount()) )
      return m_RowHidden[index];
    else
      return false;
  }

  /**
   * sets the count for the row (if the index is valid)
   * @param index     the index of the row
   * @param count     the count for the row
   */
  public void setCount(int index, double count) {
    if ( (index >= 0) && (index < getRowCount()) )
      m_Counts[index] = count;
  }

  /**
   * returns the count for the row. if the index is invalid then 0.
   * @param index     the index of the row
   * @return          the count for the row
   */
  public double getCount(int index) {
    if ( (index >= 0) && (index < getRowCount()) )
      return m_Counts[index];
    else
      return 0;
  }

  /**
   * sets the mean at the given position (if the position is valid)
   * @param col     the column of the mean
   * @param row     the row of the mean
   * @param value   the value of the mean
   */
  public void setMean(int col, int row, double value) {
    if (    (col >= 0) && (col < getColCount()) 
         && (row >= 0) && (row < getRowCount()) )
      m_Mean[row][col] = value;
  }

  /**
   * returns the mean at the given position, if the position is valid,
   * otherwise 0
   */
  public double getMean(int col, int row) {
    if (    (col >= 0) && (col < getColCount()) 
         && (row >= 0) && (row < getRowCount()) )
      return m_Mean[row][col];
    else
      return 0;
  }

  /**
   * returns the average of the mean at the given position, if the position is
   * valid, otherwise 0
   */
  public double getAverage(int col) {
    int       i;
    double    avg;
    int       count;

    if ( (col >= 0) && (col < getColCount()) ) {
      avg   = 0;
      count = 0;

      for (i = 0; i < getRowCount(); i++) {
        if (!Double.isNaN(getMean(col, i))) {
          avg += getMean(col, i);
          count++;
        }
      }
      
      return avg / (double) count;
    }
    else {
      return 0;
    }
  }

  /**
   * sets the std deviation at the given position (if the position is valid)
   * @param col     the column of the std. deviation
   * @param row     the row of the std deviation
   * @param value   the value of the std deviation
   */
  public void setStdDev(int col, int row, double value) {
    if (    (col >= 0) && (col < getColCount()) 
         && (row >= 0) && (row < getRowCount()) )
      m_StdDev[row][col] = value;
  }

  /**
   * returns the std deviation at the given position, if the position is valid,
   * otherwise 0
   */
  public double getStdDev(int col, int row) {
    if (    (col >= 0) && (col < getColCount()) 
         && (row >= 0) && (row < getRowCount()) )
      return m_StdDev[row][col];
    else
      return 0;
  }

  /**
   * sets the significance at the given position (if the position is valid)
   * @param col     the column of the significance
   * @param row     the row of the significance
   * @param value   the value of the significance
   */
  public void setSignificance(int col, int row, int value) {
    if (    (col >= 0) && (col < getColCount()) 
         && (row >= 0) && (row < getRowCount()) )
      m_Significance[row][col] = value;
  }

  /**
   * returns the significance at the given position, if the position is valid,
   * otherwise SIGNIFICANCE_ATIE
   */
  public int getSignificance(int col, int row) {
    if (    (col >= 0) && (col < getColCount()) 
         && (row >= 0) && (row < getRowCount()) )
      return m_Significance[row][col];
    else
      return SIGNIFICANCE_TIE;
  }

  /**
   * counts the occurrences of the given significance type in the given
   * column.
   * @param col       the columnn to gather the information from
   * @param type      the significance type, WIN/TIE/LOSS
   */
  public int getSignificanceCount(int col, int type) {
    int       result;
    int       i;

    result = 0;

    if ( (col >= 0) && (col < getColCount()) ) {
      for (i = 0; i < getRowCount(); i++) {
        if (getRowHidden(i))
          continue;

        // no value?
        if (Double.isNaN(getMean(col, i)))
          continue;

        if (getSignificance(col, i) == type)
          result++;
      }
    }

    return result;
  }

  /**
   * sets the ordering of the rows, null means default
   * @param order       the new order of the rows
   */
  public void setRowOrder(int[] order) {
    int         i;
    
    // default order?
    if (order == null) {
      m_RowOrder = null;
    }
    else {
      if (order.length == getRowCount()) {
        m_RowOrder = new int[order.length];
        for (i = 0; i < order.length; i++)
          m_RowOrder[i] = order[i];
      }
      else {
        System.err.println("setRowOrder: length does not match (" 
            + order.length + " <> " + getRowCount() + ") - ignored!");
      }
    }
  }

  /**
   * returns the current order of the rows, null means the default order
   * @return        the current order of the rows
   */
  public int[] getRowOrder() {
    return m_RowOrder;
  }

  /**
   * returns the displayed index of the given row, depending on the order of
   * rows, returns -1 if index out of bounds
   * @param index         the row to get the displayed index for
   * @return              the real index of the row
   */
  public int getDisplayRow(int index) {
    if ( (index >= 0) && (index < getRowCount()) ) {
      if (getRowOrder() == null)
        return index;
      else
        return getRowOrder()[index];
    }
    else {
      return -1;
    }
  }

  /**
   * sets the ordering of the columns, null means default
   * @param order       the new order of the columns
   */
  public void setColOrder(int[] order) {
    int         i;
    
    // default order?
    if (order == null) {
      m_ColOrder = null;
    }
    else {
      if (order.length == getColCount()) {
        m_ColOrder = new int[order.length];
        for (i = 0; i < order.length; i++)
          m_ColOrder[i] = order[i];
      }
      else {
        System.err.println("setColOrder: length does not match (" 
            + order.length + " <> " + getColCount() + ") - ignored!");
      }
    }
  }

  /**
   * returns the current order of the columns, null means the default order
   * @return        the current order of the columns
   */
  public int[] getColOrder() {
    return m_ColOrder;
  }

  /**
   * returns the displayed index of the given col, depending on the order of
   * columns, returns -1 if index out of bounds
   * @param index         the column to get the displayed index for
   * @return              the real index of the column
   */
  public int getDisplayCol(int index) {
    if ( (index >= 0) && (index < getColCount()) ) {
      if (getColOrder() == null)
        return index;
      else
        return getColOrder()[index];
    }
    else {
      return -1;
    }
  }

  /**
   * returns the given number as string rounded to the given number of
   * decimals. additional necessary 0's are added
   * @param d       the number to format
   * @param prec    the number of decimals after the point
   * @return        the formatted number
   */
  protected String doubleToString(double d, int prec) {
    String        result;
    int           currentPrec;
    int           i;

    result = Utils.doubleToString(d, prec);

    // decimal point?
    if (result.indexOf(".") == -1)
      result += ".";
    
    // precision so far?
    currentPrec = result.length() - result.indexOf(".") - 1;
    for (i = currentPrec; i < prec; i++)
      result += "0";
    
    return result;
  }

  /**
   * trims the given string down to the given length if longer, otherwise
   * leaves it unchanged. a length of "0" leaves the string always 
   * unchanged.
   * @param s       the string to trim (if too long)
   * @param length  the max. length (0 means infinity)
   * @return        the trimmed string
   */
  protected String trimString(String s, int length) {
    if ( (length > 0) && (s.length() > length) )
      return s.substring(0, length);
    else
      return s;
  }

  /**
   * pads the given string on the right until it reaches the given length, if
   * longer cuts it down. if length is 0 then nothing is done.
   * @param s         the string to pad
   * @param length    the max. length of the string
   * @return          the padded string
   */
  protected String padString(String s, int length) {
    return padString(s, length, false);
  }

  /**
   * pads the given string until it reaches the given length, if longer cuts
   * it down. if length is 0 then nothing is done.
   * @param s         the string to pad
   * @param length    the max. length of the string
   * @param left      whether to pad left or right
   * @return          the padded string
   */
  protected String padString(String s, int length, boolean left) {
    String      result;
    int         i;

    result = s;

    // pad with blanks
    for (i = s.length(); i < length; i++) {
      if (left)
        result = " " + result;
      else
        result = result + " ";
    }
      
    // too long?
    if ( (length > 0) && (result.length() > length) )
      result = result.substring(0, length);

    return result;
  }

  /**
   * returns the length of the longest cell in the given column
   * @param data    the data to base the calculation on
   * @param col     the column to check
   * @return        the maximum length
   */
  protected int getColSize(String[][] data, int col) {
    return getColSize(data, col, false, false);
  }

  /**
   * returns the length of the longest cell in the given column
   * @param data        the data to base the calculation on
   * @param col         the column to check
   * @param skipFirst   whether to skip the first row
   * @param skipLast    whether to skip the last row
   * @return            the maximum length
   */
  protected int getColSize( String[][] data, int col, 
                            boolean skipFirst, boolean skipLast ) {
    int       result;
    int       i;

    result = 0;

    if ( (col >= 0) && (col < data[0].length) ) {
      for (i = 0; i < data.length; i++) {
        // skip first?
        if ( (i == 0) && (skipFirst) )
          continue;

        // skip last?
        if ( (i == data.length - 1) && (skipLast) )
          continue;
        
        if (data[i][col].length() > result)
          result = data[i][col].length();
      }
    }

    return result;
  }

  /**
   * removes the filter classname from the given string if it should be 
   * removed, otherwise leaves the string alone
   * @see     #getRemoveFilterName()
   */
  protected String removeFilterName(String s) {
    if (getRemoveFilterName())
      return s.replaceAll("-weka\\.filters\\..*", "")
              .replaceAll("-unsupervised\\..*",   "")
              .replaceAll("-supervised\\..*",     "");
    else
      return s;
  }

  /**
   * returns a 2-dimensional array with the prepared data. includes the column
   * and row names. hidden cols/rows are already excluded. <br>
   * first row: column names<br>
   * last  row: wins/ties/losses<br>
   * first col: row names<br>
   */
  protected String[][] toArray() {
    int               i;
    int               n;
    int               ii;
    int               nn;
    int               x;
    int               y;
    String[][]        result;
    String[][]        tmpResult;
    int               cols;
    int               rows;
    int[]             widths;
    boolean           valueExists;

    // determine visible cols/rows
    rows = getVisibleRowCount();
    if (getShowAverage())
      rows++;
    cols = getVisibleColCount();
    if (getShowStdDev())
      cols = cols*3;   // mean + stddev + sign.
    else
      cols = cols*2;   // mean + stddev

    result = new String[rows + 2][cols + 1];

    // col names
    result[0][0] = trimString("Dataset", getRowNameWidth());
    x = 1;
    for (ii = 0; ii < getColCount(); ii++) {
      i = getDisplayCol(ii);
      if (getColHidden(i))
        continue;
      
      result[0][x] = trimString(
          removeFilterName(getColName(i)), getColNameWidth());
      x++;
      // std dev
      if (getShowStdDev()) {
        result[0][x] = "";
        x++;
      }
      // sign.
      result[0][x] = "";
      x++;
    }

    // row names
    y = 1;
    for (ii = 0; ii < getRowCount(); ii++) {
      i = getDisplayRow(ii);
      if (!getRowHidden(i)) {
        result[y][0] = trimString(
            removeFilterName(getRowName(i)), getRowNameWidth());
        y++;
      }
    }

    // fill in mean/std dev
    y = 1;
    for (ii = 0; ii < getRowCount(); ii++) {
      i = getDisplayRow(ii);
      if (getRowHidden(i))
        continue;

      x = 1;
      for (nn = 0; nn < getColCount(); nn++) {
        n = getDisplayCol(nn);
        if (getColHidden(n))
          continue;

        // do we have a value in the matrix?
        valueExists = (!Double.isNaN(getMean(n, i)));

        // mean
        if (!valueExists)
          result[y][x] = "";
        else
          result[y][x] = doubleToString(getMean(n, i), getMeanPrec());
        x++;
        
        // stddev
        if (getShowStdDev()) {
          if (!valueExists)
            result[y][x] = "";
          else if (Double.isInfinite(getStdDev(n, i)))
            result[y][x] = "Inf";
          else
            result[y][x] = doubleToString(getStdDev(n, i), getStdDevPrec());
          x++;
        }
        
        // significance
        if (!valueExists) {
          result[y][x] = "";
        }
        else {
          switch (getSignificance(n, i)) {
            case SIGNIFICANCE_TIE:
              result[y][x] = TIE_STRING;
              break;
            case SIGNIFICANCE_WIN:
              result[y][x] = WIN_STRING;
              break;
            case SIGNIFICANCE_LOSS:
              result[y][x] = LOSS_STRING;
              break;
          }
        }
        x++;
      }

      y++;
    }

    // the average
    if (getShowAverage()) {
      y = result.length - 2;
      x = 0;
      result[y][0] = "Average";
      x++;
      for (ii = 0; ii < getColCount(); ii++) {
        i = getDisplayCol(ii);
        if (getColHidden(i))
          continue;

        // mean-average
        result[y][x] = doubleToString(getAverage(i), getMeanPrec());
        x++;

        // std dev.
        if (getShowStdDev()) {
          result[y][x] = "";
          x++;
        }

        // significance
        result[y][x] = "";
        x++;
      }
    }

    // wins/ties/losses
    y = result.length - 1;
    x = 0;
    result[y][0] =   LEFT_PARENTHESES 
                   + WIN_STRING + "/" 
                   + TIE_STRING + "/" 
                   + LOSS_STRING 
                   + RIGHT_PARENTHESES;
    x++;
    for (ii = 0; ii < getColCount(); ii++) {
      i = getDisplayCol(ii);
      if (getColHidden(i))
        continue;

      // mean
      result[y][x] = "";
      x++;

      // std dev.
      if (getShowStdDev()) {
        result[y][x] = "";
        x++;
      }

      // significance
      result[y][x] =   LEFT_PARENTHESES 
                     + getSignificanceCount(i, SIGNIFICANCE_WIN) + "/" 
                     + getSignificanceCount(i, SIGNIFICANCE_TIE) + "/" 
                     + getSignificanceCount(i, SIGNIFICANCE_LOSS) 
                     + RIGHT_PARENTHESES;
      x++;
    }

    // base column has no significance -> remove these columns
    tmpResult = new String[result.length][result[0].length - 1];

    x = 0;
    for (i = 0; i < result[0].length; i++) {
      // significance
      if (    ((i == 3) && ( getShowStdDev()))
           || ((i == 2) && (!getShowStdDev())) )
        continue;
      
      for (n = 0; n < result.length; n++)
        tmpResult[n][x] = result[n][i];

      x++;
    }
    result = tmpResult;

    return result;
  }

  /**
   * returns true if the index (in the array produced by toArray(boolean))
   * is the row name
   */
  protected boolean isRowName(int index) {
    return  (index == 0);
  }

  /**
   * returns true if the index (in the array produced by toArray(boolean))
   * contains a mean
   */
  protected boolean isMean(int index) {
    index--;   // dataset
    if (index == 0) {
      return true;   // base column
    }
    else {
      index--;   // base column

      if (index < 0)
        return false;
      
      if (getShowStdDev())
        return (index % 3 == 1);
      else
        return (index % 2 == 0);
    }
  }

  /**
   * returns true if the row index (in the array produced by toArray(boolean))
   * contains the average row
   */
  protected boolean isAverage(int rowIndex) {
    if (getShowAverage())
      return (getVisibleRowCount() + 1 == rowIndex);
    else
      return false;
  }

  /**
   * returns true if the index (in the array produced by toArray(boolean))
   * contains a std deviation
   */
  protected boolean isStdDev(int index) {
    index--;   // dataset
    index--;   // base column

    if (getShowStdDev()) {
      if (index == 0) {
        return true;   // stddev of base column
      }
      else {
        index--;   // stddev of base column

        if (index < 0)
          return false;
      
        return (index % 3 == 1);
      }
    }
    else
      return false;
  }

  /**
   * returns true if the index (in the array produced by toArray(boolean))
   * contains a significance column
   */
  protected boolean isSignificance(int index) {
    index--;   // dataset
    index--;   // base column
    if (getShowStdDev()) {
      index--;   // stddev of base column

      if (index < 0)
        return false;
      
      return (index % 3 == 2);
    }
    else {
      if (index < 0)
        return false;
      
      return (index % 2 == 1);
    }
  }

  /**
   * returns the matrix as a string
   */
  public abstract String toStringMatrix();

  /**
   * returns the matrix as a string
   * @see #toStringMatrix()
   */
  public String toString() {
    return toStringMatrix();
  }

  /**
   * removes all the header information
   */
  public void clearHeader() {
    m_HeaderKeys   = new Vector();
    m_HeaderValues = new Vector();
  }

  /**
   * adds the key-value pair to the header
   * @param key       the name of the header value
   * @param value     the value of the header value
   */
  public void addHeader(String key, String value) {
    int         pos;
    
    pos = m_HeaderKeys.indexOf(key);
    if (pos > -1) {
      m_HeaderValues.set(pos, value);
    }
    else {
      m_HeaderKeys.add(key);
      m_HeaderValues.add(value);
    }
  }

  /**
   * returns the value associated with the given key, null if if cannot be
   * found
   * @param key       the key to retrieve the value for
   * @return          the associated value
   */
  public String getHeader(String key) {
    int       pos;

    pos = m_HeaderKeys.indexOf(key);
    if (pos == 0)
      return null;
    else
      return (String) m_HeaderKeys.get(pos);
  }

  /**
   * returns an enumeration of the header keys
   * @return all stored keys
   */
  public Enumeration headerKeys() {
    return m_HeaderKeys.elements();
  }
  
  /**
   * returns the header of the matrix as a string
   * @see #m_HeaderKeys
   * @see #m_HeaderValues
   */
  public abstract String toStringHeader();

  /**
   * returns returns a key for all the col names, for better readability if
   * the names got cut off
   */
  public abstract String toStringKey();

  /**
   * clears the current summary data
   */
  public void clearSummary() {
    m_NonSigWins = null;
    m_Wins       = null;
  }

  /**
   * sets the non-significant and significant wins of the resultsets
   * @param nonSigWins      the non-significant wins
   * @param wins         the significant wins
   */
  public void setSummary(int[][] nonSigWins, int[][] wins) {
    int         i;
    int         n;
    
    m_NonSigWins = new int[nonSigWins.length][nonSigWins[0].length];
    m_Wins       = new int[wins.length][wins[0].length];

    for (i = 0; i < m_NonSigWins.length; i++) {
      for (n = 0; n < m_NonSigWins[i].length; n++) {
        m_NonSigWins[i][n] = nonSigWins[i][n];
        m_Wins[i][n]       = wins[i][n];
      }
    }
  }

  /**
   * returns the character representation of the given column
   */
  protected String getSummaryTitle(int col) {
    return "" + (char) ((int) 'a' + col % 26);
  }

  /**
   * returns the summary as string
   */
  public abstract String toStringSummary();

  /**
   * clears the currently stored ranking data
   */
  public void clearRanking() {
    m_RankingWins   = null;
    m_RankingLosses = null;
    m_RankingDiff   = null;
  }

  /**
   * sets the ranking data based on the wins
   * @param wins      the wins 
   */
  public void setRanking(int[][] wins) {
    int         i;
    int         j;
    
    m_RankingWins   = new int[wins.length];
    m_RankingLosses = new int[wins.length];
    m_RankingDiff   = new int[wins.length];

    for (i = 0; i < wins.length; i++) {
      for (j = 0; j < wins[i].length; j++) {
	m_RankingWins[j]   += wins[i][j];
	m_RankingDiff[j]   += wins[i][j];
	m_RankingLosses[i] += wins[i][j];
	m_RankingDiff[i]   -= wins[i][j];
      }
    }
  }

  /**
   * returns the ranking in a string representation
   */
  public abstract String toStringRanking();
}
