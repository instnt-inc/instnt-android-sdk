package org.instant.accept.instntsdk.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class FormRow implements Serializable {

    @SerializedName("row_type")
    private FormRowType rowType;

    @SerializedName("columns")
    private List<FormColumn> columns;

    public FormRowType getRowType() {
        return rowType;
    }

    public void setRowType(FormRowType rowType) {
        this.rowType = rowType;
    }

    public List<FormColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<FormColumn> columns) {
        this.columns = columns;
    }
}
