package br.edu.fafica.androidwebaccess.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import br.edu.fafica.androidwebaccess.R;
import br.edu.fafica.androidwebaccess.adapter.PostRecyclerView;
import br.edu.fafica.androidwebaccess.databinding.ActivityMainBinding;
import br.edu.fafica.androidwebaccess.entity.Post;
import br.edu.fafica.androidwebaccess.handler.IMainActivityHandler;
import br.edu.fafica.androidwebaccess.util.Constants;
import br.edu.fafica.androidwebaccess.util.StringUtil;

public class MainActivity extends AppCompatActivity implements IMainActivityHandler {

    private ActivityMainBinding mActivityMainBinding;
    private static final int SECONDS = 1000;

    private PostRecyclerView mPostRecyclerView;
    private HashMap<String, String> hashParams;
    private Post[] mPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityMainBinding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        mActivityMainBinding.setActivityMainHandler(MainActivity.this);
        setContentView(mActivityMainBinding.getRoot());
        setSupportActionBar(mActivityMainBinding.toolbar);

        loadRecyclerView();

        PostAsyncTaskGET postAsyncTaskGET = new PostAsyncTaskGET();
        postAsyncTaskGET.execute();


    }

    private void loadRecyclerView() {

        mPostRecyclerView = new PostRecyclerView(this.mPosts);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            this.mActivityMainBinding.recyclerViewPosts.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false));
        } else {
            this.mActivityMainBinding.recyclerViewPosts.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        }

        mActivityMainBinding.recyclerViewPosts.setAdapter(mPostRecyclerView);

    }

    public static String simpleGet(String url, String httpMethod) {
        HttpURLConnection httpURLConnection = null;
        DataOutputStream dataOutputStream;
        URL urlConnection;
        String serverResponseMessage = null;

        try {
            urlConnection = new URL(url);

            httpURLConnection = (HttpURLConnection) urlConnection.openConnection();
            httpURLConnection.setReadTimeout(15 * SECONDS);
            httpURLConnection.setConnectTimeout(15 * SECONDS);
            httpURLConnection.setRequestMethod(httpMethod);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(false);

            httpURLConnection.connect();

            serverResponseMessage = StringUtil.streamToString(httpURLConnection.getInputStream());

            if (httpURLConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.e("TAG", "Deu merda. msg -> " + httpURLConnection.getResponseMessage());
            }

        } catch (IOException ioe) {
            Log.e("TAG", ioe.getMessage());
        }

        return serverResponseMessage;
    }

    private void readRequestResult(String s) {
        Post[] posts = new Gson().fromJson(s, Post[].class);

        mPostRecyclerView.updateDataSet(posts);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                startActivity(new Intent(MainActivity.this, NewPost.class));
                break;
        }
    }

    public class PostAsyncTaskGET extends AsyncTask<Void, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "Aguarde...", "Carregando posts", false, false);
        }

        @Override
        protected String doInBackground(Void... params) {
            return simpleGet("https://jsonplaceholder.typicode.com/posts", Constants.HTTP_METHOD_GET);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            readRequestResult(s);
            progressDialog.dismiss();
        }
    }

}
