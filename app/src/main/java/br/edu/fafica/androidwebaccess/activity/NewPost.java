package br.edu.fafica.androidwebaccess.activity;

import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import br.edu.fafica.androidwebaccess.R;
import br.edu.fafica.androidwebaccess.databinding.ActivityNewPostBinding;
import br.edu.fafica.androidwebaccess.handler.INewPostHandler;
import br.edu.fafica.androidwebaccess.util.StringUtil;

public class NewPost extends AppCompatActivity implements INewPostHandler {

    private ActivityNewPostBinding mActivityNewPostBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityNewPostBinding = DataBindingUtil.setContentView(NewPost.this, R.layout.activity_new_post);
        mActivityNewPostBinding.setActivityNewPostHandler(NewPost.this);
        setContentView(mActivityNewPostBinding.getRoot());
    }

    public static String newPost(HashMap<String, String> hashMap) {

        HttpURLConnection httpURLConnection = null;
        DataOutputStream dataOutputStream;
        URL urlConnection;
        String serverResponseMessage = null;

        try {
            urlConnection = new URL("https://jsonplaceholder.typicode.com/posts");

            httpURLConnection = (HttpURLConnection) urlConnection.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");

            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + "b5d1a6c7f84366149c32fcb4fd0f94b3b2848a5d");
            httpURLConnection.connect();


            String[] hashKeys = new String[hashMap.size()];
            int posAux = 0;

            for (String key: hashMap.keySet()) {
                hashKeys[posAux] = key;
                posAux++;
            }

            dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());

            for (int i = 0; i < hashMap.size(); i++) {
                dataOutputStream.writeBytes("--" + "b5d1a6c7f84366149c32fcb4fd0f94b3b2848a5d" + "\r\n");
                dataOutputStream.writeBytes("Content-Disposition: form-data;" + "name=\"" + hashKeys[i] + "\"");
                dataOutputStream.writeBytes("\r\n");
                dataOutputStream.writeBytes("\r\n");
                dataOutputStream.writeBytes(hashMap.get(hashKeys[i]));
                dataOutputStream.writeBytes("\r\n");
            }

            dataOutputStream.writeBytes("--" + "b5d1a6c7f84366149c32fcb4fd0f94b3b2848a5d" + "--" + "\r\n");

            serverResponseMessage = StringUtil.streamToString(httpURLConnection.getInputStream());

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            } else {
                Log.e("TAG", "New Post Success! " + httpURLConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e("TAG", "callServer: " + e.getMessage());
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }

        return serverResponseMessage;
    }

    @Override
    public void onClick(View view) {
        HashMap<String, String> hashMapSolicitante = new HashMap<>();

        hashMapSolicitante.put("title", mActivityNewPostBinding.textInputEditTextTitle.getText().toString());
        hashMapSolicitante.put("body", mActivityNewPostBinding.textInputEditTextBody.getText().toString());

        NewPostAsyncTask newPostAsyncTask = new NewPostAsyncTask();
        newPostAsyncTask.execute(hashMapSolicitante);


    }

    public class NewPostAsyncTask extends AsyncTask<HashMap<String, String>, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(NewPost.this, "Aguarde!", "Post sendo enviado...", false, false);
        }

        @Override
        protected String doInBackground(HashMap<String, String>... params) {
            return newPost(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Log.d("TAG", "Result -> " + s);
        }
    }

}
