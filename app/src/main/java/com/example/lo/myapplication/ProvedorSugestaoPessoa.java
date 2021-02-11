package com.example.lo.myapplication;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ProvedorSugestaoPessoa extends ContentProvider {

    List<String> pessoas;

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        if(pessoas == null || pessoas.isEmpty()){
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("https://dl.dropboxusercontent.com/u/6802536/cidades.json").build();

            try{
                Response response = client.newCall(request).execute();
                String jsonString = response.body().string();
                JSONArray jsonArray = new JSONArray(jsonString);

                pessoas = new ArrayList<>();

                int lenght = jsonArray.length();
                for (int i = 0; i < lenght; i++){
                    String pessoa = jsonArray.getString(i);
                    pessoas.add(pessoa);
                }

            }catch (Exception e){

            }
        }

        MatrixCursor cursor = new MatrixCursor(new String[]{BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID});

        if (pessoas != null){
            String query = uri.getLastPathSegment().toUpperCase();
            int limite = Integer.parseInt(uri.getQueryParameter(SearchManager.SUGGEST_PARAMETER_LIMIT));

            int lenght = pessoas.size();
            for (int i = 0; i < lenght && cursor.getCount() < limite; i++){
                String pessoa = pessoas.get(i);
                if (pessoa.toUpperCase().contains(query)){
                    cursor.addRow(new Object[]{i, pessoa, i});
                }
            }
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
