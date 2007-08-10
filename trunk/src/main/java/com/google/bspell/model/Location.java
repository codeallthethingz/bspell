package com.google.bspell.model;

public class Location {
    private int line;

    private int column;

    public int getLine() {
        return line;
    }

    public void setLine(final int newLine) {
        this.line = newLine;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(final int newColumn) {
        this.column = newColumn;
    }

    /**
     * Calculate the hash code for this object.
     * 
     * <p>The rules laid out in J. Blosh's Effective Java are used
     * for the hash code calculation.</p>
     * 
     * @return the hash code.
     * 
     * @see java.lang.Object#hashCode
     */
    public final int hashCode() {
        int code = 11;
        
        code = code * 37 + (int) (line ^ (line >> 32));
        code = code * 37 + (int) (column ^ (column >> 32));
        
        return code;
    }
    
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        return toString().equals(obj.toString());
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("(");
        sb.append(line);
        sb.append(", ");
        sb.append(column);
        sb.append(")");
        return sb.toString();
    }
}