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

public class UserDetailsHttpClient {
    private final String TAG = "UserDetailsHttpClient";
    private final String BASE_URL = PacePlaceConstants.URL;
    private Retrofit mRetrofit;
    private UserService mUserService;
    private static final UserDetailsHttpClient ourInstance = new UserDetailsHttpClient();

    public static UserDetailsHttpClient getInstance() {
        return ourInstance;
    }

    private UserDetailsHttpClient() {
        mRetrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mUserService = mRetrofit.create(UserService.class);
    }

    private interface UserService {

        @POST("getUserInfo/")
        @FormUrlEncoded
        Call<JsonObject> login(@Field(PacePlaceConstants.EMAIL) String userId, @Field(PacePlaceConstants.PASSWORD) String password);

        @POST("addUser/")
        @FormUrlEncoded
        Call<JsonObject> registerUser(@Field(PacePlaceConstants.EMAIL) String email,
                                      @Field(PacePlaceConstants.PASSWORD) String password,
                                      @Field(PacePlaceConstants.FIRST_NAME) String firstname,
                                      @Field(PacePlaceConstants.LAST_NAME) String lastname,
                                      @Field(PacePlaceConstants.MOBILE) String mobile,
                                      @Field(PacePlaceConstants.DOB) String dob,
                                      @Field(PacePlaceConstants.OPERATION) String operation,
                                      @Field(PacePlaceConstants.USER_ID) String userId,
                                      @Field(PacePlaceConstants.GENDER) String gender,
                                      @Field(PacePlaceConstants.GRADUATION_TYPE) String graduationType,
                                      @Field(PacePlaceConstants.STUDENT_TYPE) String studentType,
                                      @Field(PacePlaceConstants.SUBJECT) String subject,
                                      @Field(PacePlaceConstants.ACCOUNT_TYPE) String accountType
                                      );

        @POST("static_info/")
        Call<JsonObject> getStaticInfo();
    }

    public void login(String userId,String password, Callback<JsonObject> cb) {
        mUserService.login(userId,password).enqueue(cb);

    }

    public void getStaticInfo(Callback<JsonObject> cb) {
        mUserService.getStaticInfo().enqueue(cb);
    }

    public void registerUser(String email, String getmPassword, String getmFirstName, String getmLastName,
                             String getmContact, String getmDob, String mOperationType, String userId,
                             String getmGender, String getmGraduationType, String getmStudentType,
                             String getmSubject, String getmAccountType, Callback<JsonObject> cb) {
        mUserService.registerUser(email,getmPassword,getmFirstName,getmLastName,getmContact,getmDob,mOperationType,userId,getmGender,getmGraduationType,
                getmStudentType,getmSubject, getmAccountType).enqueue(cb);
    }
}
