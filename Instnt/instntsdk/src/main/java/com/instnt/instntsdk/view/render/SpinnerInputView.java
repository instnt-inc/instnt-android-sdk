package com.instnt.instntsdk.view.render;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.instnt.instntsdk.R;
import com.instnt.instntsdk.data.FormField;

import java.util.HashMap;
import java.util.Map;

public class SpinnerInputView extends BaseInputView {

    private Spinner valueView;

    public SpinnerInputView(Context context, FormField formField) {
        super(context, formField);
    }

    @Override
    void setupView() {
        View view = inflate(getContext(), R.layout.child_select, this);

        TextView label = view.findViewById(R.id.label);
        valueView = view.findViewById(R.id.value);

        label.setText(formField.isRequired()? generateAsterisk(formField.getLabel()) : formField.getLabel());

        if (TextUtils.isEmpty(formField.getValue()))
            return;

        String[] data = formField.getValue().split(",");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                R.layout.custom_spinner,
                data
        );

        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown);
        valueView.setAdapter(adapter);
    }

    @Override
    public void input(Map<String, Object> map) {
        map.put(formField.getName(), (String)valueView.getSelectedItem());
    }

    @Override
    public boolean checkValid() {
        return true;
    }
}
