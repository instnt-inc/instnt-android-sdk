package org.instnt.accept.instntsdk;

import org.instnt.accept.instntsdk.utils.CommonUtils;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Response;

public class CommonUtilsTest {

    @Before
    public void doBfore() {
    }

    @Test
    public void testWithHttpExceptionAndMessage() throws IOException, JSONException {

        String errorContent = "{\"formKey\":\"v390156100000\",\"stackTrace\":[\"filesubmit-form.py\",\"functionsubmitFormWithNewData()\",\"Email 'kyrewilder98gmail.com' is invalid.\"],\"errorType\":\"Exception\",\"errorMessage\":\"Email 'kyrewilder98gmail.com' is invalid.\"}";
        Response response = Response.error(422, ResponseBody.create(MediaType.parse("application/json"), errorContent));
        Throwable throwable = new HttpException(response);

        String message = CommonUtils.getErrorMessage(throwable);
        Assert.assertEquals(message, "Email 'kyrewilder98gmail.com' is invalid.");
    }

    @Test
    public void testWithoutHttpExceptionAndMessage() throws IOException, JSONException {

        Throwable throwable = new Exception();
        String message = CommonUtils.getErrorMessage(throwable);
        Assert.assertEquals(message, "Unprocessable Error");
    }

    @Test
    public void testWithKeyMessageInErrorResponse() throws IOException, JSONException {

        String errorContent = "{\"message\":\"Endpoint request timed out\"}";
        Response response = Response.error(422, ResponseBody.create(MediaType.parse("application/json"), errorContent));
        Throwable throwable = new HttpException(response);

        String message = CommonUtils.getErrorMessage(throwable);
        Assert.assertEquals(message, "Endpoint request timed out");
    }

    @Test
    public void testWithDifferentMessagePattern() throws IOException, JSONException {

        String errorContent = "{\"anUnknownKey\":\"Any unknown message\"}";
        Response response = Response.error(422, ResponseBody.create(MediaType.parse("application/json"), errorContent));
        Throwable throwable = new HttpException(response);

        String message = CommonUtils.getErrorMessage(throwable);
        Assert.assertEquals(message, "{\"anUnknownKey\":\"Any unknown message\"}");
    }
}