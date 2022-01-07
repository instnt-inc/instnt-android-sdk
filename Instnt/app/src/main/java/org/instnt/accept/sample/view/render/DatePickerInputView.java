package org.instnt.accept.sample.view.render;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import org.instnt.accept.instntsdk.R;
import org.instnt.accept.instntsdk.model.FormField;
import org.instnt.accept.instntsdk.utils.DateUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class DatePickerInputView extends BaseInputView {

    private TextView valueView;

    public DatePickerInputView(Context context, FormField formField) {
        super(context, formField);
    }

    @Override
    void setupView() {
        View view = inflate(getContext(), R.layout.child_date, this);

        TextView label = view.findViewById(R.id.label);
        valueView = view.findViewById(R.id.value);

        label.setText(formField.isRequired()? generateAsterisk(formField.getLabel()) : formField.getLabel());
        valueView.setHint(formField.getPlaceHolder());

        if (!TextUtils.isEmpty(formField.getValue()))
            valueView.setText(formField.getValue());

        valueView.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            Date selected = DateUtil.getDateFromString(DateUtil.DATE_FORMAT, valueView.getText().toString().trim());

            calendar.setTime(selected);

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog picker = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int newYear, int newMonth, int newDayOfMonth) {
                    calendar.set(Calendar.YEAR, newYear);
                    calendar.set(Calendar.MONTH, newMonth);
                    calendar.set(Calendar.DAY_OF_MONTH, newDayOfMonth);

                    valueView.setError(null);
                    valueView.setText(DateUtil.convertMilliToDate(DateUtil.DATE_FORMAT, calendar.getTimeInMillis()));
                }
            }, year, month, day);

            picker.show();

        });
    }

    @Override
    public void input(Map<String, Object> map) {
        map.put(formField.getName(), valueView.getText().toString().trim());
    }

    @Override
    public boolean checkValid() {
        if (formField.isRequired() && TextUtils.isEmpty(valueView.getText().toString().trim())){
            valueView.setError("Please fill out this field");
            valueView.requestFocus();
            return false;
        }

        return true;
    }
}
