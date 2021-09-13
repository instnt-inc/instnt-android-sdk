package org.instnt.instntsdk.view.render;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;

import org.instnt.instntsdk.R;
import org.instnt.instntsdk.data.FormField;

import java.util.Map;

public class CheckboxInputView extends BaseInputView {

    private CheckBox valueView;

    public CheckboxInputView(Context context, FormField formField) {
        super(context, formField);
    }

    @Override
    void setupView() {
        View view = inflate(getContext(), R.layout.child_checkbox, this);

        valueView = view.findViewById(R.id.value);

        valueView.setText(formField.getLabel());

        if (!TextUtils.isEmpty(formField.getValue())){
            boolean checked = formField.getValue().equals("true");
            valueView.setChecked(checked);
        }
    }

    @Override
    public void input(Map<String, Object> map) {
        map.put(formField.getName(), String.valueOf(valueView.isChecked()));
    }

    @Override
    public boolean checkValid() {
        return true;
    }
}
