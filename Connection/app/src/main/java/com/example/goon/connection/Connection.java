package com.example.goon.connection;
import android.annotation.SuppressLint;
import android.media.TimedText;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

public class Connection extends AppCompatActivity {

    private Button buttonConnection;
    private TextView text;
    private EditText username;
    private EditText password;
    private EditText email;
    private String usernameS="";
    private String passwordS="";
    private String emailS="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        text= (TextView)findViewById(R.id.textView);
        buttonConnection= (Button) findViewById(R.id.button);
        username = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        usernameS=username.getText().toString();
        passwordS=username.getText().toString();
        emailS=username.getText().toString();

        buttonConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInternetConnection()) {
                   // new JSONTask().execute("http://goonapp.herokuapp.com/helloWorld");
                    // User user = new User(username.toString(), password.toString(), email.toString());
                    // postData("http://goonapp.herokuapp.com/user",  user);
                    if(validate()) {
                        new HttpAsyncTask().execute("https://goonapp.herokuapp.com/user");
                    }else{

                        Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }



    public class JSONTask extends AsyncTask<String,String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                String line = "";
                StringBuffer buffer = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                return buffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            text.setText(result);
        }
    }

    public static String POST(final String url, User user) throws JSONException, IOException {
        InputStream inputStream= null;
        String result = "";
        String username=user.getUsername();
        String password = user.getPassword();
        String email = user.getEmail();

        try {
            JSONObject requestObject = new JSONObject();
            requestObject.put("username", username);
            requestObject.put("email", email);
            requestObject.put("password", password);
            String json = requestObject.toString();

            HttpClient httpClient= new DefaultHttpClient();
            HttpPost httpPost= new HttpPost(url);

            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);

            httpPost.setHeader("GET", "/user/json");
           // httpPost.setHeader("Content-type", "application/json");

            HttpResponse httpResponse= httpClient.execute(httpPost);

            inputStream=httpResponse.getEntity().getContent();

            if (inputStream!=null){
                result=convertInputStreamToString(inputStream);
            }else{
                result="Did not wor!k";
            }
        }catch(Exception e){

            Log.d("InputStream", e.getLocalizedMessage());

        }
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

    private boolean validate(){
        if(username.getText().toString().trim().equals(""))
            return false;
        else if(password.getText().toString().trim().equals(""))
            return false;
        else if(email.getText().toString().trim().equals(""))
            return false;
        else
            return true;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            User user = new User();
            user.setUsername(usernameS);
            user.setUsername(passwordS);
            user.setEmail(emailS);


            try {
                return POST(urls[0],user);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Data Sent!", Toast.LENGTH_LONG).show();
        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_connection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

*/
    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||

                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
          //  Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        }else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
           Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }


}
