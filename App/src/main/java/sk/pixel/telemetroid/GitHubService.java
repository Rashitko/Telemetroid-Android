package sk.pixel.telemetroid;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;

public interface GitHubService {
    @GET("/params")
    String listRepos(@Path("user") String user);
}
