package base.pace.paceplace.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import base.pace.paceplace.R;
import base.pace.paceplace.course.CourseDetail;

public class CommonWSJSONArrayInvoke extends AsyncTask<Object, Void, WebServiceResponse> {

    public static final String TAG = "CommonWSInvoke";

    private Context mContext;
    private RequestQueue mQueue;

    public static final String REQUEST_TAG = "VolleyBlockingRequestActivity";
    public WebServiceResponse mWsr;
    public String mUrl = "";
    ArrayList mArrayList;

    public CommonWSJSONArrayInvoke(Context ctx) {
        mContext = ctx;
    }

    @Override
    protected WebServiceResponse doInBackground(Object... objects) {
        JSONObject outputDataJsonObject = new JSONObject();
        JSONArray outputJsonArray, outputDataJsonArray;
        JSONArray inputJsonArray = inputParams(objects);

        final RequestFuture<JSONArray> futureRequest = RequestFuture.newFuture();
        mQueue = CustomVolleyRequestQueue.getInstance(mContext.getApplicationContext())
                .getRequestQueue();
        final JsonArrayRequest jsonRequest = new JsonArrayRequest(Request.Method.POST, mUrl,
                inputJsonArray, futureRequest, futureRequest);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest);
        try {
            outputJsonArray = futureRequest.get(10, TimeUnit.SECONDS);
            if (outputJsonArray.length() > 0) {
                // TODO : Code for JSONArray
                Log.i(TAG,"Output");
                mWsr = new WebServiceResponse(Boolean.TRUE, outputJsonArray);
            }

            return mWsr;
        } catch (TimeoutException | ExecutionException | InterruptedException /*| JSONException*/ e) {
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

    private JSONArray inputParams(Object... objects) {
        String inputParamsFor = "";
        JSONArray jsonArray = new JSONArray();
        try {
            mUrl = (String) objects[0];
            inputParamsFor = (String) objects[1];
            if (inputParamsFor.equalsIgnoreCase(PacePlaceConstants.COURSES_REGISTRATION)) {
                jsonArray = (JSONArray) objects[2];
                Log.i(TAG,"Array");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonArray;
    }
}
