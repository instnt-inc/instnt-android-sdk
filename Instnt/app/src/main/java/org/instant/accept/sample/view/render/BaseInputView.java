package org.instant.accept.sample.view.render;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.LinearLayout;

import org.instant.accept.instntsdk.model.FormField;

import java.util.Map;

public abstract class BaseInputView extends LinearLayout {

    public FormField formField;

    public BaseInputView(Context context, FormField formField) {
        super(context);
        this.formField = formField;

        setupView();
    }

    public SpannableStringBuilder generateAsterisk(String label){
        SpannableStringBuilder builder = new SpannableStringBuilder(label);

        SpannableString asteriskSpan = new SpannableString(" *");
        asteriskSpan.setSpan(new ForegroundColorSpan(Color.RED), 0, asteriskSpan.length(), 0);
        builder.append(asteriskSpan);

        return builder;
    }

    abstract void setupView();
    public abstract void input(Map<String, Object> map);
    public abstract boolean checkValid();
}
