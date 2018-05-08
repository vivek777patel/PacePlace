package base.pace.paceplace.httpclient;

import com.google.gson.JsonObject;

import base.pace.paceplace.util.PacePlaceConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public class EventDetailsHttpClient {
    private final String TAG = "EventDetailsHttpClient";
    private final String BASE_URL = PacePlaceConstants.URL;
    private Retrofit mRetrofit;
    private EventService mEventService;

    private static final EventDetailsHttpClient ourInstance = new EventDetailsHttpClient();

    public static EventDetailsHttpClient getInstance() {
        return ourInstance;
    }

    private EventDetailsHttpClient() {
        mRetrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mEventService = mRetrofit.create(EventService.class);
    }

    private interface EventService {
        @POST("addEvent/")
        @FormUrlEncoded
        Call<JsonObject> addEvent(@Field(PacePlaceConstants.EVENT_NAME) String eventName,
                                         @Field(PacePlaceConstants.EVENT_DESCRIPTION) String eventDesc,
                                         @Field(PacePlaceConstants.EVENT_LOCATION) String eventLocation,
                                         @Field(PacePlaceConstants.EVENT_POSTED_BY) String eventPostedBy,
                                         @Field(PacePlaceConstants.EVENT_DATE) String eventDate,
                                         @Field(PacePlaceConstants.SUBJECT) String subject,
                                         @Field(PacePlaceConstants.GRADUATION_TYPE) String graduationType);
        @POST("getEventDetails/")
        Call<JsonObject> getEventDetails();
    }

    public void addEvent(String eventName,String eventDesc,String eventLocation,
                                String eventPostedBy,String eventDate,String subject,
                                String graduationType,Callback<JsonObject> cb) {
        mEventService.addEvent(eventName,eventDesc,eventLocation,eventPostedBy,eventDate,subject,graduationType).enqueue(cb);
    }

    public void getEventDetails(Callback<JsonObject> cb) {
        mEventService.getEventDetails().enqueue(cb);
    }

}
