package org.instant.accept.instntsdk.view.render;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.instant.accept.instntsdk.InstntSDK;
import org.instant.accept.instntsdk.R;
import org.instant.accept.instntsdk.model.FormField;
import org.instant.accept.instntsdk.network.NetworkUtil;
import org.instant.accept.instntsdk.network.RestUrl;
import org.instant.accept.instntsdk.utils.CommonUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OTPInputView extends BaseInputView {

    private NetworkUtil networkModule;
    private Button valueView;
    private TextInputView mobileTextInputView;

    public OTPInputView(Context context, FormField formField, TextInputView textInputView) {
        super(context, formField);

        networkModule = new NetworkUtil(); // this should call only instnt SDK interface  TODO
        networkModule.setServerUrl(RestUrl.SANDBOX_URL);
        this.mobileTextInputView = textInputView;
    }

    @Override
    void setupView() {
        View view = inflate(getContext(), R.layout.child_otp_inputs, this);
        //valueView = view.findViewById(R.id.value);

        Button sendOTPButton = view.findViewById(R.id.sendOTPButton);

        TextView enterOTPText = view.findViewById(R.id.enterOTPText);
        enterOTPText.setVisibility(View.INVISIBLE);

        Button verifyOTPButton = view.findViewById(R.id.verifyOTPButton);
        verifyOTPButton.setVisibility(View.INVISIBLE);

        sendOTPButton.setOnClickListener(v -> {
            TextView textView = this.mobileTextInputView.findViewById(R.id.value);
            String mobileNumber = textView.getText().toString();

            //Validate number
            if (!isValidE123(mobileNumber)) {
                CommonUtils.showToast(getContext(), "Please enter a valid number with country code");
                return;
            }

            //InstntSDK.instance.sendOTP(mobileNumber);

            networkModule.sendOTP(mobileNumber).subscribe(otpResponse->{

                if(otpResponse != null && !otpResponse.getResponse().isValid()) {
                    CommonUtils.showToast(getContext(), otpResponse.getResponse().getErrors()[0]);
                    return;
                }

                CommonUtils.showToast(getContext(), "Please verify the OTP sent to your mobile number");
                sendOTPButton.setVisibility(View.INVISIBLE);
                enterOTPText.setVisibility(View.VISIBLE);
                verifyOTPButton.setVisibility(View.VISIBLE);
            }, throwable -> {
                CommonUtils.showToast(getContext(), CommonUtils.getErrorMessage(throwable));
            });
        });

        verifyOTPButton.setOnClickListener(v -> {

            String mobileNumber = this.mobileTextInputView.findViewById(R.id.value).toString();
            String enteredOTP = (String) enterOTPText.getText().toString();

            networkModule.verifyOTP(mobileNumber, enteredOTP).subscribe(otpResponse->{

                if(otpResponse != null && !otpResponse.getResponse().isValid()) {
                    CommonUtils.showToast(getContext(), otpResponse.getResponse().getErrors()[0]);
                    return;
                }

                CommonUtils.showToast(getContext(), "OTP verified successfully");
                enterOTPText.setVisibility(View.INVISIBLE);
                verifyOTPButton.setVisibility(View.INVISIBLE);

            }, throwable -> {
                CommonUtils.showToast(getContext(), CommonUtils.getErrorMessage(throwable));
            });
        });
    }

    private static boolean isValidE123(String s) {
        Pattern p = Pattern.compile("^\\+(?:[0-9] ?){6,14}[0-9]$");

        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }

    @Override
    public void input(Map<String, Object> map) {
        map.put(formField.getName(), valueView.getText().toString().trim());
    }

    @Override
    public boolean checkValid() {
        return true;
    }
}
