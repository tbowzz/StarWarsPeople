package com.tbowdev.starwarspeople.Model;

import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.exception.ApolloException;

import java.io.IOException;
import java.util.List;

import javax.annotation.Nonnull;

import apollo.AllPeopleQuery;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by tylerbowers on 3/14/18.
 */

public class SwapiClient {

    private static final String TAG = "SwapiClient";
    private static final String SERVER_URL = "http://10.0.0.100:58558/";

    public static void queryAllPeople() {

        getClient().query(AllPeopleQuery.builder().build()).enqueue(new ApolloCall.Callback<AllPeopleQuery.Data>() {
            @Override
            public void onResponse(@Nonnull com.apollographql.apollo.api.Response<AllPeopleQuery.Data> response) {
                List<AllPeopleQuery.person> people = response.data().allPeople().people();
                Log.d(TAG, "Retreived " + people.size() + " people.");

                if (people != null && people.size() > 0) {

                    DataCache.getInstance().setAllPeople(people);
                }
                else {
                    Log.d(TAG, "AllPeopleQuery returned no people.");
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                Log.e(TAG, "Execution of AllPeopleQuery has failed.");
                e.printStackTrace();
            }
        });
    }

    private static ApolloClient getClient() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor()
        {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException
            {
                Request orig = chain.request();
                Request.Builder builder = orig.newBuilder().method(orig.method(), orig.body());
                return chain.proceed(builder.build());
            }
        }).build();
        // running local instance of graphQL swapi from https://github.com/graphql/swapi-graphql
        return ApolloClient.builder().serverUrl(SERVER_URL).okHttpClient(okHttpClient).build();
    }
}
