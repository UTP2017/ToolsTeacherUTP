package pe.edu.utp.toolsteacherutp.Controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import pe.edu.utp.toolsteacherutp.Interfaces.CourseService;
import pe.edu.utp.toolsteacherutp.Models.Course;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by elbuenpixel on 24/02/17.
 */

public class CourseController implements Callback<List<Course>> {
    static final String BASE_URL = "http://162.243.185.203/wp-json/wp/v2/";


    public void start() {
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        CourseService courseApi = retrofit.create(CourseService.class);

        Call<List<Course>> call = courseApi.getCourses();
        call.enqueue(this);

    }


    @Override
    public void onResponse(Call<List<Course>> call, Response<List<Course>> response) {
        if(response.isSuccessful()) {
            List<Course> coursesList = response.body();
            for ( Course course: coursesList ) {
                if ( !course.getAcf().equals( null )  )
                    System.out.println(course.getAcf().getRequiere().size());
                System.out.println(course.getTitle());
            }
        } else {
            System.out.println(response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<List<Course>> call, Throwable t) {
        t.printStackTrace();
    }
}
