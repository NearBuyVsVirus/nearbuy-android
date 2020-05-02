package app.nexd.android.api;

import java.util.List;

import app.nexd.android.api.model.UpdateUserDto;
import app.nexd.android.api.model.User;
import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;

public interface UsersApi {
  /**
   * Get user profile of the requesting user
   * 
   * @return Observable&lt;User&gt;
   */
  @GET("users/me")
  Observable<User> userControllerFindMe();
    

  /**
   * Get user profile of a specific user
   * 
   * @param userId user id (required)
   * @return Observable&lt;User&gt;
   */
  @GET("users/{userId}")
  Observable<User> userControllerFindOne(
    @retrofit2.http.Path("userId") String userId
  );

  /**
   * Get all users
   * 
   * @param xAdminSecret Secret to access the admin functions. (required)
   * @return Observable&lt;List&lt;User&gt;&gt;
   */
  @GET("users")
  Observable<List<User>> userControllerGetAll(
    @retrofit2.http.Header("x-admin-secret") String xAdminSecret
  );

  /**
   * Update profile of a specific user
   * 
   * @param userId user id (required)
   * @param updateUserDto  (required)
   * @return Observable&lt;User&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("users/{userId}")
  Observable<User> userControllerUpdate(
    @retrofit2.http.Path("userId") String userId, @retrofit2.http.Body UpdateUserDto updateUserDto
  );

  /**
   * Update profile of the requesting user
   * 
   * @param updateUserDto  (required)
   * @return Observable&lt;User&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("users/me")
  Single<User> userControllerUpdateMyself(
    @retrofit2.http.Body UpdateUserDto updateUserDto
  );

}
