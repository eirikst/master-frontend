package com.andreasogeirik.master_frontend.communication;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Pair;

import com.andreasogeirik.master_frontend.gcm.GcmApiService;
import com.andreasogeirik.master_frontend.gcm.GcmTokenSingleton;
import com.andreasogeirik.master_frontend.listener.OnLoginFinishedListener;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.UserPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;


/**
 * Created by Eirik on 23.04.2016.
 */
public class LikeTask extends AsyncTask<Void, Void, Pair<Integer, ResponseEntity<String>>> {
    private String tag = getClass().getSimpleName();
    public final static int COMMENT = 1;
    public final static int POST = 2;

    public final static int LIKE = 1;
    public final static int UNLIKE = 2;

    public interface OnFinishedLikingListener {
        void onSuccessPostLike(int id);
        void onFailurePostLike(int id);

        void onSuccessCommentLike(int id);
        void onFailureCommentLike(int id);

        void onSuccessPostUnlike(int id);
        void onFailurePostUnlike(int id);

        void onSuccessCommentUnlike(int id);
        void onFailureCommentUnlike(int id);
    }

    private OnFinishedLikingListener listener;
    private int id;
    private String urlEnd;
    private HttpMethod method;
    private int postOrComment;
    private int likeOrUnlike;

    public LikeTask(OnFinishedLikingListener listener, int id, int postOrComment, int likeOrUnlike) {
        if((postOrComment != POST && postOrComment != COMMENT) || (likeOrUnlike != LIKE &&
                likeOrUnlike != UNLIKE)) {
            throw new IllegalArgumentException("Wrong params");
        }
        this.listener = listener;
        this.id = id;
        this.postOrComment = postOrComment;
        this.likeOrUnlike = likeOrUnlike;

        if(postOrComment == POST) {
            urlEnd = "posts/" + id + "/likes";
        }
        else {
            urlEnd = "posts/comments/" + id + "/likes";
        }

        if(likeOrUnlike == LIKE) {
            method = HttpMethod.PUT;
        }
        else {
            method = HttpMethod.DELETE;
        }
    }

    protected Pair<Integer, ResponseEntity<String>> doInBackground(Void... params) {
        ResponseEntity<String> response;
        RestTemplate template = new RestTemplate();
        ((SimpleClientHttpRequestFactory) template.getRequestFactory()).setConnectTimeout(1000 * 10);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", UserPreferencesManager.getInstance().getCookie());

        HttpEntity<String> entity = new HttpEntity(null, headers);

        try {
            response = template.exchange(Constants.BACKEND_URL + urlEnd, method, entity, String.class);
            return new Pair(Constants.OK, response);
        }
        catch (HttpClientErrorException e) {
            Log.w(tag, "Client error:" + e);
            return new Pair(Constants.CLIENT_ERROR, null);
        }
        catch (ResourceAccessException e) {
            Log.w(tag, "Resource error:" + e);
            return new Pair(Constants.RESOURCE_ACCESS_ERROR, null);
        }
        catch(Exception e) {
            Log.w(tag, "Some error:" + e);
            return new Pair(Constants.SOME_ERROR, null);
        }
    }

    protected void onPostExecute(Pair<Integer, ResponseEntity<String>> response) {
        //success
        if (response.first == Constants.OK) {
            if(postOrComment == POST) {
                if(likeOrUnlike == LIKE) {
                    listener.onSuccessPostLike(id);
                }
                else {
                    listener.onSuccessPostUnlike(id);
                }
            }
            else {
                if(likeOrUnlike == LIKE) {
                    listener.onSuccessCommentLike(id);
                }
                else {
                    listener.onSuccessCommentUnlike(id);
                }
            }
        }
        //failure
        else {
            if(postOrComment == POST) {
                if(likeOrUnlike == LIKE) {
                    listener.onFailurePostLike(id);
                }
                else {
                    listener.onFailurePostUnlike(id);
                }
            }
            else {
                if(likeOrUnlike == LIKE) {
                    listener.onFailureCommentLike(id);
                }
                else {
                    listener.onFailureCommentUnlike(id);
                }
            }
        }
    }
}