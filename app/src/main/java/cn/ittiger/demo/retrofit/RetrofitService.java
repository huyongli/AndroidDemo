package cn.ittiger.demo.retrofit;

import cn.ittiger.demo.bean.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.io.File;
import java.util.Map;

/**
 * @author: laohu on 2016/7/23
 * @site: http://ittiger.cn
 */
public interface RetrofitService {

    @GET("getModelService")
    Call<User> getModelServiceSync();

    @GET("getModelService")
    Call<User> getModelServiceAsync(@Query("async") boolean async);

    @GET("{path}")
    Call<String> getStringServiceAsync(@Path("path") String path, @Query("string") boolean isString,
                                       @Query("async") boolean async);

    @Multipart
    @POST("fileService")
    Call<User> uploadFile(@PartMap Map<String, RequestBody> requestBodyMap);

    @Multipart
    @POST("fileService")
    Call<User> uploadOneFile(@Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST("postService")
    Call<String> postModelServiceAsync(@Field("msg") String msg, @Field("string") boolean isString);

    @FormUrlEncoded
    @POST("{path}")
    Call<User> postModelServiceAsync(@Path("path") String path, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("fileService")
    Call<ResponseBody> downloadFile(@Field("isDownload") boolean isDownload);
}
