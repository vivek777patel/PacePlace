package base.pace.paceplace.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import base.pace.paceplace.R;

public class CommonWSInvoke extends AsyncTask<String, Void, WebServiceResponse> {

    public static final String TAG = "CommonWSInvoke";

    private Context mContext;
    private RequestQueue mQueue;
    public static final String REQUEST_TAG = "VolleyBlockingRequestActivity";
    public WebServiceResponse mWsr;
    public String mUrl = "";

    public CommonWSInvoke(Context ctx) {
        mContext = ctx;
    }

    @Override
    protected WebServiceResponse doInBackground(String... params) {
        JSONObject outputJsonObject, outputDataJsonObject = new JSONObject();
        JSONObject inputJsonObject = inputParams(params);

        final RequestFuture<JSONObject> futureRequest = RequestFuture.newFuture();
        mQueue = CustomVolleyRequestQueue.getInstance(mContext.getApplicationContext())
                .getRequestQueue();
        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method
                .POST, mUrl,
                inputJsonObject, futureRequest, futureRequest);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);
        try {
            outputJsonObject = futureRequest.get(10, TimeUnit.SECONDS);
            Boolean outputResponse = Boolean.FALSE;
            if (outputJsonObject.length() > 0) {
                outputResponse = (Boolean) outputJsonObject.get(PacePlaceConstants.RESPONSE);
                if (null != outputJsonObject.get(PacePlaceConstants.DATA) && !"null".equalsIgnoreCase(outputJsonObject.get(PacePlaceConstants.DATA).toString()))
                    outputDataJsonObject = (JSONObject) outputJsonObject.get(PacePlaceConstants.DATA);
                else
                    outputDataJsonObject = new JSONObject();
            }

            mWsr = new WebServiceResponse(outputResponse, outputDataJsonObject);
            return mWsr;
        } catch (TimeoutException | ExecutionException | InterruptedException | JSONException e) {
            e.printStackTrace();
            try {
                mWsr = new WebServiceResponse(Boolean.FALSE, outputDataJsonObject.put(PacePlaceConstants.ERROR,
                        mContext.getResources().getString(R.string.connection_failed)));
                return mWsr;
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }

    private JSONObject inputParams(String... params) {
        JSONObject inputJsonObject = new JSONObject();
        String inputParamsFor = "";
        try {
            mUrl = params[0];
            inputParamsFor = params[1];
            if (inputParamsFor.equalsIgnoreCase(PacePlaceConstants.LOGIN)) {
                inputJsonObject.put(PacePlaceConstants.EMAIL, params[2]);
                inputJsonObject.put(PacePlaceConstants.PASSWORD, params[3]);
            } else if (inputParamsFor.equalsIgnoreCase(PacePlaceConstants.REGISTER)) {
                inputJsonObject.put(PacePlaceConstants.EMAIL, params[2]);
                inputJsonObject.put(PacePlaceConstants.PASSWORD, params[3]);
                inputJsonObject.put(PacePlaceConstants.FIRST_NAME, params[4]);
                inputJsonObject.put(PacePlaceConstants.LAST_NAME, params[5]);
                inputJsonObject.put(PacePlaceConstants.MOBILE, params[6]);
                inputJsonObject.put(PacePlaceConstants.DOB, params[7]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return inputJsonObject;
    }
}
