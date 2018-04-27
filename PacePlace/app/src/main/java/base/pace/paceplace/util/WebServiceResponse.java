package base.pace.paceplace.util;

import org.json.JSONArray;
import org.json.JSONObject;

public class WebServiceResponse{
    Boolean mResponse;
    JSONObject mJsonObjectResponse;
    JSONArray mJsonArrayResponse;

    public WebServiceResponse(Boolean response, JSONObject jsonObject){
        mResponse = response;
        mJsonObjectResponse = jsonObject;
    }

    public WebServiceResponse(Boolean response, JSONArray jsonArray){
        mResponse = response;
        mJsonArrayResponse = jsonArray;
    }

    public Boolean getmResponse() {
        return mResponse;
    }

    public void setmResponse(Boolean mResponse) {
        this.mResponse = mResponse;
    }

    public JSONObject getmJsonObjectResponse() {
        return mJsonObjectResponse;
    }

    public void setmJsonObjectResponse(JSONObject mJsonObjectResponse) {
        this.mJsonObjectResponse = mJsonObjectResponse;
    }

    public JSONArray getmJsonArrayResponse() {
        return mJsonArrayResponse;
    }

    public void setmJsonArrayResponse(JSONArray mJsonArrayResponse) {
        this.mJsonArrayResponse = mJsonArrayResponse;
    }
}
