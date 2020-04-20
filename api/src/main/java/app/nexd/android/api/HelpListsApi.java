package app.nexd.android.api;

import java.util.List;

import app.nexd.android.api.model.HelpList;
import app.nexd.android.api.model.HelpListCreateDto;
import io.reactivex.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface HelpListsApi {
  /**
   * Add a help request to a help list
   * 
   * @param helpListId Id of the help list (required)
   * @param helpRequestId Id of the help request (required)
   * @return Observable&lt;HelpList&gt;
   */
  @PUT("help-lists/{helpListId}/help-request/{helpRequestId}")
  Observable<HelpList> helpListsControllerAddHelpRequestToList(
    @retrofit2.http.Path("helpListId") Long helpListId, @retrofit2.http.Path("helpRequestId") Long helpRequestId
  );

  /**
   * Delete a help request from help list
   * 
   * @param helpListId Id of the help list (required)
   * @param helpRequestId Id of the help request (required)
   * @return Observable&lt;HelpList&gt;
   */
  @DELETE("help-lists/{helpListId}/help-request/{helpRequestId}")
  Observable<HelpList> helpListsControllerDeleteHelpRequestFromHelpList(
    @retrofit2.http.Path("helpListId") Long helpListId, @retrofit2.http.Path("helpRequestId") Long helpRequestId
  );

  /**
   * Get a specific help list
   * 
   * @param helpListId Id of the help list (required)
   * @return Observable&lt;HelpList&gt;
   */
  @GET("help-lists/{helpListId}")
  Observable<HelpList> helpListsControllerFindOne(
    @retrofit2.http.Path("helpListId") Long helpListId
  );

  /**
   * Get help lists of the requesting user
   * 
   * @param userId If included, filter by userId, otherwise by requesting user. (optional)
   * @return Observable&lt;List&lt;HelpList&gt;&gt;
   */
  @GET("help-lists")
  Observable<List<HelpList>> helpListsControllerGetUserLists(
    @retrofit2.http.Query("userId") String userId
  );

  /**
   * Add a new help list for the current user
   * 
   * @param helpListCreateDto  (required)
   * @return Observable&lt;HelpList&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @POST("help-lists")
  Observable<HelpList> helpListsControllerInsertNewHelpList(
    @retrofit2.http.Body HelpListCreateDto helpListCreateDto
  );

  /**
   * Set/unset article done in all help requests
   * 
   * @param helpListId Id of the help list (required)
   * @param articleId Id of the article (required)
   * @param articleDone true to set the article as \&quot;bought\&quot; (required)
   * @return Observable&lt;HelpList&gt;
   */
  @PUT("help-lists/{helpListId}/article/{articleId}")
  Observable<HelpList> helpListsControllerModifyArticleInAllHelpRequests(
    @retrofit2.http.Path("helpListId") Long helpListId, @retrofit2.http.Path("articleId") Long articleId, @retrofit2.http.Query("articleDone") Boolean articleDone
  );

  /**
   * Set/unset articleDone of an article in a specific help request
   * 
   * @param helpListId Id of the help list (required)
   * @param helpRequestId Id of the help request (required)
   * @param articleId Id of the article (required)
   * @param articleDone true to set the article as \&quot;bought\&quot; (required)
   * @return Observable&lt;HelpList&gt;
   */
  @PUT("help-lists/{helpListId}/help-request/{helpRequestId}/article/{articleId}")
  Observable<HelpList> helpListsControllerModifyArticleInHelpRequest(
    @retrofit2.http.Path("helpListId") Long helpListId, @retrofit2.http.Path("helpRequestId") Long helpRequestId, @retrofit2.http.Path("articleId") Long articleId, @retrofit2.http.Query("articleDone") Boolean articleDone
  );

  /**
   * Modify a help list
   * 
   * @param helpListId Id of the help list (required)
   * @param helpListCreateDto  (required)
   * @return Observable&lt;HelpList&gt;
   */
  @Headers({
    "Content-Type:application/json"
  })
  @PUT("help-lists/{helpListId}")
  Observable<HelpList> helpListsControllerUpdateHelpLists(
    @retrofit2.http.Path("helpListId") Long helpListId, @retrofit2.http.Body HelpListCreateDto helpListCreateDto
  );

}
