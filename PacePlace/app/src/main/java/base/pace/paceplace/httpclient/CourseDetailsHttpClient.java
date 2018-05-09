package base.pace.paceplace.httpclient;

import com.google.gson.JsonObject;

import base.pace.paceplace.util.PacePlaceConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public class CourseDetailsHttpClient {
    private final String TAG = "CourseDetailsHttpClient";
    private final String BASE_URL = PacePlaceConstants.URL;
    private Retrofit mRetrofit;
    private CourseService mCourseService;
    private static final CourseDetailsHttpClient ourInstance = new CourseDetailsHttpClient();

    public static CourseDetailsHttpClient getInstance() {
        return ourInstance;
    }

    private CourseDetailsHttpClient() {
        mRetrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mCourseService = mRetrofit.create(CourseService.class);
    }

    private interface CourseService {

        @POST("getUserCourses/")
        @FormUrlEncoded
        Call<JsonObject> getUserCourseDetails(@Field(PacePlaceConstants.USER_ID) String params);

        @POST("saveUserRatings/")
        @FormUrlEncoded
        Call<JsonObject> saveUserRatings(@Field(PacePlaceConstants.STUDENT_COURSE_RATING) String studentCourseRating,
                                         @Field(PacePlaceConstants.OVERALL_COURSE_RATERS) String overallCourseRaters,
                                         @Field(PacePlaceConstants.OVERALL_COURSE_RATING) String overallCourseRatings,
                                         @Field(PacePlaceConstants.STUDENT_PROF_RATING) String studentProfRating,
                                         @Field(PacePlaceConstants.OVERALL_PROF_RATERS) String overallProfRaters,
                                         @Field(PacePlaceConstants.OVERALL_PROF_RATING) String overallProfRatings,
                                         @Field(PacePlaceConstants.COURSE_ID) String courseId,
                                         @Field(PacePlaceConstants.STUDENT_COURSE_ID) String studentCourseId,
                                         @Field(PacePlaceConstants.PROF_RATE_ID) String profRateId,
                                         @Field(PacePlaceConstants.PROF_USER_ID) String courseProfessorId
        );

        @POST("getCoursesForRegistration/")
        @FormUrlEncoded
        Call<JsonObject> getCourseForRegistration(@Field(PacePlaceConstants.USER_ID) String params);

        @Headers({
                "Content-Type: application/json"
        })
        @POST("saveRegisteredCourses/")
        Call<String> saveCourseRegistration(@Body String data);

    }

    public void getUserCourseDetails(String userId, Callback<JsonObject> cb) {
        mCourseService.getUserCourseDetails(userId).enqueue(cb);
    }

    public void saveCourseRegistration(String body, Callback<String> cb) {
        mCourseService.saveCourseRegistration(body).enqueue(cb);
    }

    public void saveUserRatings(String studentCourseRating, String overallCourseRaters, String overallCourseRatings,
                                String studentProfRating, String overallProfRaters, String overallProfRatings,
                                String courseId, String studentCourseId, String profRateId,
                                String courseProfessorId,Callback<JsonObject> cb) {
        mCourseService.saveUserRatings(studentCourseRating, overallCourseRaters, overallCourseRatings,
                studentProfRating, overallProfRaters, overallProfRatings,
                courseId, studentCourseId, profRateId,
                courseProfessorId).enqueue(cb);
    }

    public void getCourseForRegistration(String userId, Callback<JsonObject> cb) {
        mCourseService.getCourseForRegistration(userId).enqueue(cb);
    }
}
