# InstntSDK
This documentation covers the basics of Instnt SDK. For a detailed overview of Instnt's functionality, visit the [Instnt documentation library](https://support.instnt.org/hc/en-us/articles/360055345112-Integration-Overview)

## Rendering a Standard Signup Workflow with Instnt SDK

Instnt SDK can render a standard workflow that includes the following fields:
* Email Address
* First Name
* Surname
* Mobile Number
* State
* Street Address
* Zip code
* City
* Country
* Submit My Workflow Button

## Getting Started

Note that a Workflow ID is required in order to properly execute this function. For more information concerning Workflow IDs, please visit
[Instnt's documentation library.](https://support.instnt.org/hc/en-us/articles/360055345112-Integration-Overview)

1. Create a form in the Instnt dashboard and get the Workflow ID.

2. Install InstntSDK.
### Gradle
```
dependencies {
   implementation 'com.instant:instantsdk:1.0.0'
}
```

3. Implement the following code to present the form associated with the Workflow ID

### Initialize SDK and callback
```java

public class MainActivity extends BaseActivity implements SubmitCallback {
   private InstntSDK instantSDK;
  
   private void init() {
        instantSDK = InstntSDK.getInstance();      
        instantSDK.setCallback(this);
    }
    
   @Override
   public void didCancel() {
        binding.result.setText("No JWT");
        CommonUtils.showToast(this, "User Cancelled");
   }

   @Override
   public void didSubmit(FormSubmitData data, String errMessage) {
        if (data == null) {        
            binding.result.setText("No JWT");
            CommonUtils.showToast(this, errMessage);
        }else {
            binding.result.setText(data.getJwt());            
        }
   }
```
 ### Call api with workflow ID
 ``` java
      String formId = binding.formid.getText().toString().trim();

      instantSDK.setup(formId, binding.sandboxSwitch.isChecked());

 ```
 ### Call render function to show form field screen and submit
 ``` java
   private void showForm(FormCodes formCodes) {
        instantSDK.showForm(this, formCodes, this);
    }
 ```
  ### Submitcallback
  ``` java
   public interface SubmitCallback {
      void didCancel();
      void didSubmit(FormSubmitData submitData, String errMessage);
   }
  ```
  `didCancel()` is called when the user taps the Cancel button.
  `didSubmit()` is called when the data is submitted successfully.
        - `submitData`: Form submission result. `Jwt` and `decision` will be included.
        - `errMessage`: Error when api call fails.


## Custom Usage

InstntSDK provides the following two functions for submitting custom forms to the Instnt API.
``` java
public void getFormData(GetFormCallback callback)
public void submitForm(String url, Map<String, Object> body, SubmitCallback callback)
```
1. Call `setUp` function first to prepare the form submission.
``` java
    instantSDK.setup(formId, binding.sandboxSwitch.isChecked());
```

2. Call `submitForm` function with user data.
```swift
   private void submit() {
        Map<String, Object> paramMap = new HashMap<>();
        for (int i = 0; i<binding.container.getChildCount(); i++) {
            BaseInputView inputView = (BaseInputView)binding.container.getChildAt(i);

            if (!inputView.checkValid())
                return;

            inputView.input(paramMap);
        }

        instantSDK.submitForm(paramMap, this);
   }
```

 `this` from above `submitForm` function is SubmitCallback. See below:

``` java
 @Override
    public void didSubmit(FormSubmitData data, String errMessage) {
        if (data == null) {
            //api call is failed
            binding.result.setText(errMessage);
        }else {
            binding.result.setText(data.getJwt());
        }
    }
```
