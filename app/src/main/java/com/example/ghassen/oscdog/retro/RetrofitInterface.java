package com.example.ghassen.oscdog.retro;


import com.example.ghassen.oscdog.Entities.Collier;
import com.example.ghassen.oscdog.Entities.Dog;
import com.example.ghassen.oscdog.Entities.Event;
import com.example.ghassen.oscdog.Entities.EventsResponse;
import com.example.ghassen.oscdog.Entities.HardRegisterResponse;
import com.example.ghassen.oscdog.Entities.NotificationResponse;
import com.example.ghassen.oscdog.Entities.PositionDog;
import com.example.ghassen.oscdog.Entities.SignInResponse;
import com.example.ghassen.oscdog.Entities.SignUpResponse;
import com.example.ghassen.oscdog.Entities.Station;
import com.example.ghassen.oscdog.Entities.User;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitInterface {
    String BASE_URL = "https://oprex-backend.herokuapp.com/";

    /*@GET("api/Users1")
    public Call<List<User>> getAllUsers();
    @GET("api/Users1/{id}")
    public Call<User> getUserById(@Path("id") int id);
    @DELETE("api/Users1/{id}")
    public Call<User> deleteUserById(@Path("id") int id);
*/


    @POST("login")
    public Call<SignInResponse> login(@Body JsonObject object);

    @POST("user/registration")
    public Call<SignUpResponse> SignUp(@Body JsonObject object);

    @POST("custom/dogs")
    public Call<ResponseBody> RegisterDog( @Body JsonObject object,
                                  @Header("Authorization") String token);

    @GET("dogs")
    public  Call<List<Dog>> GetDogs(@Header("Authorization") String token);

    @Multipart
    @POST("{id}/upload/image")
    public Call<ResponseBody> uploadImage(@Path("id") String id,@Part MultipartBody.Part file,@Header("Authorization") String token);

    @POST("events")
    public Call<ResponseBody> AddEvent(@Body JsonObject object,@Header("Authorization") String token);

    @GET("events")
    public  Call<EventsResponse> GetEvents(@Header("Authorization") String token);

    @GET("notifications")
    public  Call<NotificationResponse> GetNotification(@Header("Authorization") String token);

    @GET("events")
    public  Call<EventsResponse> GetEventsDate(@Query("dateString") String date, @Header("Authorization") String token);

    @GET("custom/dogs")
    public Call<List<Dog>> GetDogByUser(@Query("userId") String id,@Header("Authorization") String token);

    @PATCH("dogs/{id}")
    public Call<Dog> UpdateDog(@Path("id") String id,@Body JsonObject object,@Header("Authorization") String token,@Header("Content-Type") String content,@Header("Cache-Control") String cache);

    @PATCH("users/{id}")
    public Call<User> UpdateUser(@Path("id") String id,@Body JsonObject object,@Header("Authorization") String token,@Header("Content-Type") String content,@Header("Cache-Control") String cache);

    @GET("custom/dogs/{id}/position")
    public Call<PositionDog> GetPositionDog(@Path("id") String id, @Header("Authorization") String token);

    @GET("dogs/{id}")
    public Call<Dog> GetDogById(@Path("id") String id,@Header("Authorization") String token);

    @GET("users/{id}")
    public Call<User> GetUserById(@Path("id") String id, @Header("Authorization") String token);

    @POST("custom/linkCollarDogStation")
    public Call<HardRegisterResponse> PostCollarSattion(@Body JsonObject object,@Header("Authorization") String token);

    @GET("dogs/{id}/station")
    public Call<Station> GetStationById(@Path("id") String id,@Header("Authorization") String token);

    @GET("dogs/{id}/collar")
    public Call<Collier> GetCollarById(@Path("id") String id, @Header("Authorization") String token);

    @Headers({"cache-control: no-cache","Content-Type: application/json","Accept: application/json"})
    @PATCH("stations/{id}")
    public Call<Station> UpdateStation(@Path("id") String id,@Body JsonObject object,@Header("Authorization") String token);
}
