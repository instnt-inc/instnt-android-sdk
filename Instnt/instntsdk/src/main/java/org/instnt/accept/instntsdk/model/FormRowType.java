package org.instnt.accept.instntsdk.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FormRowType implements Serializable {

    @SerializedName("row_type")
    private String rowType;

    @SerializedName("col_class")
    private String colClass;

    @SerializedName("no_of_columns")
    private int noOfColumns;

    public String getRowType() {
        return rowType;
    }

    public void setRowType(String rowType) {
        this.rowType = rowType;
    }

    public String getColClass() {
        return colClass;
    }

    public void setColClass(String colClass) {
        this.colClass = colClass;
    }

    public int getNoOfColumns() {
        return noOfColumns;
    }

    public void setNoOfColumns(int noOfColumns) {
        this.noOfColumns = noOfColumns;
    }
}
