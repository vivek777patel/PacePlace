package base.pace.paceplace.util;

import android.app.ProgressDialog;
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

public class CommonWSInvoke extends AsyncTask<String, Void, WebServiceResponse> {

    public static final String TAG = "CommonWSInvoke";
    public static ProgressDialog mProcessDialog;

    private Context mContext;
    private RequestQueue mQueue;
    public static final String REQUEST_TAG = "VolleyBlockingRequestActivity";
    public WebServiceResponse mWsr;
    public CommonWSInvoke(Context ctx) {
        mContext = ctx;
    }

    @Override
    protected WebServiceResponse doInBackground(String... params) {
        JSONObject outputJsonObject;
        JSONObject inputJsonObject = inputParams(params);

        final RequestFuture<JSONObject> futureRequest = RequestFuture.newFuture();
        mQueue = CustomVolleyRequestQueue.getInstance(mContext.getApplicationContext())
                .getRequestQueue();
        final JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method
                .POST, PacePlaceConstants.URL_LOGIN,
                inputJsonObject, futureRequest, futureRequest);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);
        try {
            outputJsonObject =  futureRequest.get(10, TimeUnit.SECONDS);
            Boolean outputResponse = Boolean.FALSE;
            if(outputJsonObject.length()>0)
            {
                outputResponse = Boolean.TRUE;
            }
            mWsr = new WebServiceResponse(outputResponse,outputJsonObject);
            return mWsr;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return null;
    }

    private JSONObject inputParams(String... params){
        JSONObject inputJsonObject = new JSONObject();
        String inputParamsFor = "";
        try {
            inputParamsFor = params[0];
            if(inputParamsFor.equalsIgnoreCase(PacePlaceConstants.LOGIN)){
                inputJsonObject.put(PacePlaceConstants.EMAIL,params[1]);
                inputJsonObject.put(PacePlaceConstants.PASSWORD,params[2]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return inputJsonObject;
    }
}
