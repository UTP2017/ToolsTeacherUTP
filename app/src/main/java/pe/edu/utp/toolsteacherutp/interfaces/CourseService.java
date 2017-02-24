package pe.edu.utp.toolsteacherutp.Interfaces;

import java.util.List;

import pe.edu.utp.toolsteacherutp.Models.Course;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by elbuenpixel on 24/02/17.
 */

public interface CourseService {

    @GET("curso")
    Call<List<Course>> getCourses();

    @GET("curso/{id}")
    Call<Course> getCourse( @Path("id") int groupId );
}
