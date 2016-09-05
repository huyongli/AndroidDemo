package cn.ittiger.demo.retrofit;

import cn.ittiger.demo.bean.User;
import cn.ittiger.demo.util.UIUtil;
import cn.ittiger.demo.util.Util;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: laohu on 2016/7/23
 * @site: http://ittiger.cn
 */
public class RetrofitManager {

    private static final String BASE_URL = "http://192.168.1.104:8080/AndroidService/";
    private static final String KEY_MODEL = "model";
    private static final String KEY_STRING = "String";
    private static final String KEY_FILE = "file";
    private static RetrofitManager sRetrofitManager;
    private HashMap<String, RetrofitService> mServiceHashMap;

    private RetrofitManager() {

        mServiceHashMap = new HashMap<>();
    }

    private RetrofitService getModelRetrofitService() {

        if(mServiceHashMap.get(KEY_MODEL) == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RetrofitService service = retrofit.create(RetrofitService.class);
            mServiceHashMap.put(KEY_MODEL, service);
        }
        return mServiceHashMap.get(KEY_MODEL);
    }

    private RetrofitService getStringRetrofitService() {

        if(mServiceHashMap.get(KEY_STRING) == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();

            RetrofitService service = retrofit.create(RetrofitService.class);
            mServiceHashMap.put(KEY_STRING, service);
        }
        return mServiceHashMap.get(KEY_STRING);
    }

    private <T> RetrofitService getFileRetrofitService(final RetrofitCallback<T> callback) {

        if(mServiceHashMap.get(KEY_FILE) == null) {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            clientBuilder.addInterceptor(new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {

                    okhttp3.Response response = chain.proceed(chain.request());
                    return response.newBuilder().body(new FileResponseBody<T>(response.body(), callback)).build();
                }
            });
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(clientBuilder.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            RetrofitService service = retrofit.create(RetrofitService.class);
            mServiceHashMap.put(KEY_FILE, service);
        }
        return mServiceHashMap.get(KEY_FILE);
    }

    public static RetrofitManager getInstance() {

        if(sRetrofitManager == null) {
            synchronized (RetrofitManager.class) {
                if(sRetrofitManager == null) {
                    sRetrofitManager = new RetrofitManager();
                }
            }
        }
        return sRetrofitManager;
    }

    public void syncGetRequestModel(final Activity activity) {

        new Thread(){
            @Override
            public void run() {

                Call<User> call = getModelRetrofitService().getModelServiceSync();
                try {
                    Response<User> response = call.execute();
                    if(response.isSuccessful()) {
                        runOnUIThread(activity, response.body().toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void asyncGetRequestModel(final Activity activity) {

        Call<User> call = getModelRetrofitService().getModelServiceAsync(true);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(response.isSuccessful()) {
                    runOnUIThread(activity, response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                runOnUIThread(activity, t.getMessage());
            }
        });
    }

    public void asyncGetRequestString(final Activity activity) {

        Call<String> call = getStringRetrofitService().getStringServiceAsync("getModelService", true, true);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if(response.isSuccessful()) {
                    runOnUIThread(activity, response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                runOnUIThread(activity, t.getMessage());
            }
        });
    }

    public void uploadFile(final Activity activity, final Handler handler) {
        String file1 = "/storage/sdcard0/049.jpg";
        String file2 = "/storage/sdcard0/057.jpg";

        File file = new File(file1);
        if(!file.exists()) {
            return;
        }

        RetrofitCallback<User> callback = new RetrofitCallback<User>() {
            @Override
            public void onSuccess(Call<User> call, Response<User> response) {

                runOnUIThread(activity, response.body().toString());
                handler.obtainMessage(2).sendToTarget();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                runOnUIThread(activity, t.getMessage());
                handler.obtainMessage(2).sendToTarget();
            }

            @Override
            public void onLoading(long total, long progress) {

                super.onLoading(total, progress);
                Message message = handler.obtainMessage();
                message.what = 1;
                message.getData().putLong("total", total);
                message.getData().putLong("progress", progress);
                message.sendToTarget();
            }
        };

//        RequestBody body1 = RequestBody.create(MediaType.parse("multipart/form-data"), new File(file1));
//        RequestBody body2 = RequestBody.create(MediaType.parse("multipart/form-data"), new File(file2));

//        Map<String, RequestBody> map = new HashMap<>();
//        map.put("file\"; filename=\"img_5177.jpg", new FileRequestBody<>(body1, callback));
//        map.put("file\"; filename=\"016.jpg", new FileRequestBody<>(body2, callback));
//        Call<User> call = getModelRetrofitService().uploadFile(map);

        RequestBody body1 = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        FileRequestBody body = new FileRequestBody(body1, callback);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), body);
        Call<User> call = getModelRetrofitService().uploadOneFile(part);
        call.enqueue(callback);
    }

    public void asyncDownloadFile(final Activity activity, final Handler handler) {

        RetrofitCallback<ResponseBody> callback = new RetrofitCallback<ResponseBody>() {
            @Override
            public void onSuccess(Call<ResponseBody> call, Response<ResponseBody> response) {

                try {
                    InputStream is = response.body().byteStream();
                    String path = Util.getSdCardPath();
                    File file = new File(path, "download.jpg");
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                    }
                    fos.flush();
                    fos.close();
                    bis.close();
                    is.close();
                    Message message = handler.obtainMessage();
                    message.what = 3;
                    message.getData().putString("file", file.getAbsolutePath());
                    message.sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                runOnUIThread(activity, t.getMessage());
                handler.obtainMessage(2).sendToTarget();
            }

            @Override
            public void onLoading(long total, long progress) {

                super.onLoading(total, progress);
                Message message = handler.obtainMessage();
                message.what = 1;
                message.getData().putLong("total", total);
                message.getData().putLong("progress", progress);
                message.sendToTarget();
            }
        };

        Call<ResponseBody> call = getFileRetrofitService(callback).downloadFile(true);
        call.enqueue(callback);
    }

    public void syncPostRequestString(final Activity activity) {

        new Thread(){
            @Override
            public void run() {

                Call<String> call = getStringRetrofitService().postModelServiceAsync("post request model sync", true);
                try {
                    Response<String> response = call.execute();
                    if(response.isSuccessful()) {
                        runOnUIThread(activity, response.body().toString());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public void asyncPostRequestModelMap(final Activity activity) {

        Map<String, String> map = new HashMap<>();
        map.put("string", "false");
        map.put("msg", "post request model async by map");
        Call<User> call = getModelRetrofitService().postModelServiceAsync("postService", map);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(response.isSuccessful()) {
                    runOnUIThread(activity, response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

                runOnUIThread(activity, t.getMessage());
            }
        });
    }

    public void runOnUIThread(final Activity activity, final String msg) {

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showToast(activity, msg);
            }
        });
    }

    public void showToast(Activity activity, String msg) {

        UIUtil.showToast(activity, msg);
    }
}
