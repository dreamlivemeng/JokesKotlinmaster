package com.dreamlive.jokes.retrofit

import android.app.Application
import android.util.Log
import com.dreamlive.jokes.BuildConfig
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit
import kotlin.collections.HashMap

/**
 * 网络请求
 * @author 2017/6/20 15:59 / mengwei
 */
class RetrofitFactory internal constructor(baseUrl: String) {


    init {
        val outTime: Long = 10
        var httpClient: OkHttpClient? = null
        //debug模式下输出log
        if (BuildConfig.DEBUG) {
            val logging: HttpLoggingInterceptor = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient = OkHttpClient.Builder().connectTimeout(outTime, TimeUnit.SECONDS).writeTimeout(outTime, TimeUnit.SECONDS)
                    .readTimeout(outTime, TimeUnit.SECONDS).addInterceptor(logging).build()
        } else {
            httpClient = OkHttpClient.Builder().connectTimeout(outTime, TimeUnit.SECONDS).writeTimeout(outTime, TimeUnit.SECONDS)
                    .readTimeout(outTime, TimeUnit.SECONDS) .build()
        }
        //配置retrofit
        retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(httpClient)
                .addConverterFactory(CustomGsonConverterFactory(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
    }

    companion object {
        lateinit var application: Application
        lateinit var retrofit: Retrofit
        val gson: Gson = GsonBuilder().serializeNulls().create()
        val BASE_URL: String = "http://gank.io/api/"
        val OPERATION_PLATFORM_BASE_URL: String = BASE_URL + ""
        var controller: Map<Class<*>, Any> = HashMap<Class<*>, Any>()

        fun <T> getControllerSingleTonOperation(controllerInterface: Class<T>): T {
            return getControllerSingleTon(controllerInterface, OPERATION_PLATFORM_BASE_URL)
        }

        fun <T> getControllerSingleTon(controllerInterface: Class<T>, baseUrl: String): T {
            var t: T? = controller[controllerInterface] as T
            if (t == null) {
                synchronized(RetrofitFactory::class.java) {
                    t = RetrofitFactory(baseUrl).createAPI(controllerInterface)
                    return t as T
                }
            } else {
                return t as T
            }

        }

        fun <T> getControllerSingleTonWorking(redirectURL: String, controllerInterface: Class<T>): T {
            return getControllerSingleTon(controllerInterface, redirectURL)
        }
    }

    internal fun <T> createAPI(cls: Class<T>): T {
        return retrofit.create(cls)
    }

    class CustomGsonConverterFactory(val gson: Gson) : Converter.Factory() {


        override fun responseBodyConverter(type: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<ResponseBody, *>? {
            val adapter = gson.getAdapter(TypeToken.get(type!!))
            return CustomGsonResponseBodyConverter(gson, adapter)
        }

        override fun requestBodyConverter(type: Type?, parameterAnnotations: Array<out Annotation>?, methodAnnotations: Array<out Annotation>?, retrofit: Retrofit?): Converter<*, RequestBody>? {
            val adapter = gson.getAdapter(TypeToken.get(type!!))

            try {
                val cls: Class<*> = Class.forName("retrofit2.converter.gson.GsonRequestBodyConverter")
                val constructor = cls.getDeclaredConstructor(gson.javaClass, adapter.javaClass)
                return constructor.newInstance(gson, adapter) as Converter<*, RequestBody>
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            }
            return null
        }

    }

    class CustomGsonResponseBodyConverter<T>(val gson: Gson, val adapter: TypeAdapter<T>) : Converter<ResponseBody, T> {

        @Throws(IOException::class, CustomException::class, JsonParseException::class)
        override fun convert(value: ResponseBody): T? {
            try {
                val jsonObject = gson.getAdapter(JsonObject::class.java)
                        .read(gson.newJsonReader(value.charStream()))
                val error = jsonObject.get("error").asBoolean

                if (error) {
                    throw CustomException(0, "error")
                } else {
                    if (jsonObject.has("results")) {
                        val resultElement = jsonObject.get("results")
                        return adapter.fromJsonTree(resultElement)
                    } else {
                        return null
                    }
                }

            } catch (e: JsonParseException) {
                e.printStackTrace()
                throw e
            } finally {
                value.close()
            }
        }
    }


}