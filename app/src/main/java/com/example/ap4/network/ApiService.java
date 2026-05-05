package com.example.ap4.network;

import com.example.ap4.model.AdherentModel;
import com.example.ap4.model.AuthRequest;
import com.example.ap4.model.AuthReponse;
import com.example.ap4.model.UserUpdate;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import java.util.List;

public interface ApiService {
    @POST("login/login")
    Call<AuthReponse> login(@Body AuthRequest authRequest);

    // CRUD User (Loggué)
    @GET("api/AdherentController/{id}")
    Call<AuthReponse.User> getCurrentUser(@Path("id") int id);

    @PUT("api/AdherentController/update/{id}")
    Call<AuthReponse.User> updateProfile(@Path("id") int id, @Body UserUpdate userUpdate);

    // CRUD Admin (Gestion des adhérents)
    @GET("api/AdherentController")
    Call<List<AdherentModel>> getAllAdherent();

    @POST("api/AdherentController")
    Call<Void> addAdherent(@Body AdherentModel adherent);

    @DELETE("api/AdherentController/{id}")
    Call<Void> deleteAdherent(@Path("id") int id);
}
