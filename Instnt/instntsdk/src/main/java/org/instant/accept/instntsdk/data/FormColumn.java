package org.instant.accept.instntsdk.data;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FormColumn implements Serializable {

    @SerializedName("fieldId")
    private int fieldId;

    @SerializedName("field")
    private FormField field;

    @SerializedName("droppable")
    private boolean droppable;

    public int getFieldId() {
        return fieldId;
    }

    public void setFieldId(int fieldId) {
        this.fieldId = fieldId;
    }

    public FormField getField() {
        return field;
    }

    public void setField(FormField field) {
        this.field = field;
    }

    public boolean isDroppable() {
        return droppable;
    }

    public void setDroppable(boolean droppable) {
        this.droppable = droppable;
    }
}
