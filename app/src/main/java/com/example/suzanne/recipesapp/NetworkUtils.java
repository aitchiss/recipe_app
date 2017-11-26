package com.example.suzanne.recipesapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.example.suzanne.recipesapp.models.Recipe;
import com.example.suzanne.recipesapp.utilities.RecipeJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by suzanne on 26/11/2017.
 */

public class NetworkUtils {

    private static final String API_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
//    private static ArrayList<Recipe> mRecipes = new ArrayList<>();
//    private OkHttpClient mClient;
//    private Context mContext;

//    public NetworkUtils(Context context){
//        mContext = context;
////    }

//    public ArrayList<Recipe> getRecipesFromApi(){
//        if (mClient == null){
//            mClient = new OkHttpClient();
//        }
//
//        if (isOnlineOrConnecting(mContext)){
//            Request request = new Request.Builder().url(API_URL).build();
//            Call call = mClient.newCall(request);
//
//            call.enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
////                TODO - handle UI of a network error
//                    Log.d("okhttp", "fail");
//                }
//
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    parseJsonResponse(response.body().string());
//                }
//            });
//
//        }
//        Log.d("network utils", String.valueOf(mRecipes.size()));
//        return mRecipes;
//    }
//
//    private static void parseJsonResponse(String jsonResponse){
//        try {
//            mRecipes = RecipeJsonUtils.convertJsonToRecipes(jsonResponse);
//            Log.d("network utils json", String.valueOf(mRecipes.size()));
//        } catch (JSONException e){
//            e.printStackTrace();
//        }
//
//    }

    public static URL buildUrl(){
//        TODO - is this necessary?
        Uri builtUri = Uri.parse(API_URL);
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput){
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }


    public static boolean isOnlineOrConnecting(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
