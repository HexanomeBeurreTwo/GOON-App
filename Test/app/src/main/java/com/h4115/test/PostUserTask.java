package com.h4115.test;

import android.os.AsyncTask;
import org.json.JSONException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class PostUserTask extends AsyncTask<String, Void, String> {

    protected String username;
    protected String password;
    protected String email;
    protected String age;

    public PostUserTask(String username, String password, String email, String age){
        this.username = username;
        this.password = password;
        this.email = email;
        this.age = age;
    }

    @Override
    protected String doInBackground(String... urls) {

        try {
            return POST(urls[0]);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
    }

    public  String POST(final String url) throws JSONException, IOException {
        InputStream inputStream= null;
        String result = "";


        try {

            ArrayList<NameValuePair> pairs = new ArrayList<>();

            pairs.add(new BasicNameValuePair("username",username));
            pairs.add(new BasicNameValuePair("email",email));
            pairs.add(new BasicNameValuePair("password",password));
            pairs.add(new BasicNameValuePair("age",age));
            pairs.add(new BasicNameValuePair("citizen","lyon"));

            HttpClient httpClient= new DefaultHttpClient();
            HttpPost httpPost= new HttpPost(url);

            httpPost.setEntity(new UrlEncodedFormEntity(pairs));

            HttpResponse httpResponse= httpClient.execute(httpPost);

            inputStream=httpResponse.getEntity().getContent();

            if (inputStream!=null){
                result=convertInputStreamToString(inputStream);

            }else{
                result="Muy mal!";
            }
        }
        catch(Exception e){}
        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream)throws IOException{

        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line=bufferedReader.readLine())!=null){
            result+=line;
        }
        inputStream.close();
        return result;

    }
}
