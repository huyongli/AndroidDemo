package cn.ittiger.demo;

import cn.ittiger.demo.bean.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @author: laohu on 2016/8/15
 * @site: http://ittiger.cn
 */
public interface LoginService {
    @FormUrlEncoded
    @POST("login")
    Call<User> loginService(@Field("username") String username, @Field("password") String password);
}
