package com.dreamlive.jokes.retrofit;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import android.widget.Toast;

import com.dreamlive.jokes.AppApplication;
import com.dreamlive.jokes.BuildConfig;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;


import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;


public final class RetrofitJacksonFactory {
    /**
     * 将Application进行全局保存
     */
    private static AppApplication application;
    private static final String BASE_URL = "http://gank.io/api/";
    private static Retrofit retrofit;

    static {
        Gson gson = new GsonBuilder()
                .serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();
        okhttp3.OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient httpClient = builder.connectTimeout(10, TimeUnit.SECONDS).writeTimeout(20,
                TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).build();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient = new OkHttpClient.Builder().addInterceptor(logging).build();
        }
        retrofit = new Retrofit.Builder().baseUrl(BASE_URL).client(httpClient)
//                .addConverterFactory(CustomGsonConverterFactory.create(gson))
                .addConverterFactory(CustomJacksonConverterFactory.create(new BasicObjectMapper(), application))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
    }

    //在Application调用
    public static void init(AppApplication application) {
        RetrofitJacksonFactory.application = application;
    }

    public static AppApplication getInitedApplication() {
        return application;
    }

    /**
     * 根据接口创建请求API
     *
     * @param controllerInterface
     * @param <T>
     * @return
     */
    public static <T> T createAPI(Class<T> controllerInterface) {
        return retrofit.create(controllerInterface);
    }

    public static <T> T createBaseCallApi(Class<T> controllerInterface) {
        okhttp3.OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient httpClient = builder.connectTimeout(10, TimeUnit.SECONDS).writeTimeout(20,
                TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS).build();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient = new OkHttpClient.Builder().addInterceptor(logging).build();
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL).client(httpClient)
//                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(CustomJacksonConverterFactory.create(new BasicObjectMapper(), application))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        return retrofit.create(controllerInterface);
    }


    private static class CustomGsonConverterFactory extends Converter.Factory {
        private Gson gson;

        public CustomGsonConverterFactory(Gson gson) {
            this.gson = gson;
        }

        public static CustomGsonConverterFactory create(Gson gson) {
            return new CustomGsonConverterFactory(gson);
        }

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                                Retrofit retrofit) {
            TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
            return new CustomGsonResponseBodyConverter<>(gson, adapter);
        }

        @Override
        public Converter<?, RequestBody> requestBodyConverter(Type type,
                                                              Annotation[] parameterAnnotations,
                                                              Annotation[] methodAnnotations,
                                                              Retrofit retrofit) {
            TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
            try {
                Class<?> cls = Class.forName("retrofit2.converter.gson.GsonRequestBodyConverter");
                Constructor constructor = cls.getDeclaredConstructor(gson.getClass(), adapter
                        .getClass());
                return (Converter<?, RequestBody>) constructor.newInstance(gson, adapter);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private static class CustomGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
        private final Gson gson;
        private final TypeAdapter<T> adapter;

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Toast.makeText(application, (String) msg.obj, Toast.LENGTH_SHORT).show();
            }
        };

        CustomGsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
            this.gson = gson;
            this.adapter = adapter;
        }

        @Override
        public T convert(ResponseBody value) throws IOException, CustomException,
                JsonParseException {
            try {
                JsonObject jsonObject = gson.getAdapter(JsonObject.class)
                        .read(gson.newJsonReader(value.charStream()));
                boolean error = jsonObject.get("error").getAsBoolean();
                if (error) {
                    throw new CustomException(0, "error");
                } else {
                    if (jsonObject.has("data")) {
                        JsonElement resultElement = jsonObject.get("data");
                        return adapter.fromJsonTree(resultElement);
                    } else {
                        return null;
                    }

                }

            } catch (JsonParseException je) {
                je.printStackTrace();
                throw je;
            } finally {
                value.close();
            }
        }
    }


    public static class CustomJacksonConverterFactory extends Converter.Factory {

        private ObjectMapper mObjectMapper;
        private AppApplication mAppApplication;

        public CustomJacksonConverterFactory(ObjectMapper objectMapper, AppApplication appApplication) {
            mObjectMapper = objectMapper;
            mAppApplication = appApplication;
        }

        public static CustomJacksonConverterFactory create(ObjectMapper objectMapper, AppApplication appApplication) {
            return new CustomJacksonConverterFactory(objectMapper, appApplication);
        }

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            return new CustomJacksonResponseBodyConverter<>(type);
        }

        private class CustomJacksonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

            private Type type;

            public CustomJacksonResponseBodyConverter(Type type) {
                this.type = type;
            }

            @Override
            public T convert(ResponseBody value) throws IOException, CustomException {
                JsonNode node = null;
                try {
                    node = mObjectMapper.readTree(value.string());
                    boolean error = node.get("error").asBoolean();

                    if (error) {
                        throw new CustomException(0, "error");
                    } else {
                        if (node.has("results")) {
                            JsonNode _node = node.get("results");
                            JavaType javaType = TypeFactory.defaultInstance().constructType(type);
                            return (T) mObjectMapper.readValue(_node.toString(), javaType);
                        } else {
                            return null;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
    }
}
