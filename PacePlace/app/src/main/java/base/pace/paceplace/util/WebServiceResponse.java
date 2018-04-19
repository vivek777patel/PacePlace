package base.pace.paceplace.util;

import org.json.JSONObject;

public class WebServiceResponse{
    Boolean mResponse;
    JSONObject mJsonObjectResponse;

    public WebServiceResponse(Boolean response, JSONObject jsonObject){
        mResponse = response;
        mJsonObjectResponse = jsonObject;
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
}
