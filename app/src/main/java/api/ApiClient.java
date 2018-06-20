package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yssh.ground.GroundApplication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient{
        private static Retrofit retrofit = null;

        public static Retrofit getClient() {
            if (retrofit==null) {
                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();
                retrofit = new Retrofit.Builder()
                        .baseUrl(GroundApplication.GROUND_DEV_API)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();
            }
            return retrofit;
        }
}
