package com.example.havadurumu;

import android.support.v7.app.AppCompatActivity;


import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView city;
    private TextView comment;
    private TextView tempature;
    private TextView weather;
    private Button buton;
    private EditText edit;
    private ImageView image;
    String city2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city=(TextView) findViewById(R.id.textView);
        comment=(TextView) findViewById(R.id.textView4);
        tempature=(TextView) findViewById(R.id.textView2);
        weather=(TextView) findViewById(R.id.textView3);
        buton=(Button) findViewById(R.id.button);
        edit=(EditText) findViewById(R.id.editText);
        image=(ImageView) findViewById(R.id.imageView);

        buton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSonParse obj=new JSonParse();
                edit=(EditText) findViewById(R.id.editText);
                city2=String.valueOf(edit.getText());
                new JSonParse().execute();
            }
        });

    }




    protected class JSonParse extends AsyncTask<Void, Void, Void> {

        String rMain="";
        String rDescription="";
        String rIcon="";
        int rTemp;
        String rCity;
        Bitmap bitImage;

        @Override
        protected Void doInBackground(Void... params){
            String result="";

            try {

                URL wUrl= new URL("http://api.openweathermap.org/data/2.5/weather?q="+city2+"&appid=f5c6b525639d4913f7a2882233e60c50");
                BufferedReader bufferedReader = null;
                bufferedReader = new BufferedReader(new InputStreamReader(wUrl.openStream()));

                String line=null;

                while((line = bufferedReader.readLine()) != null){
                    result += line;
                }
                System.out.println(result);
                bufferedReader.close();

                JSONObject object=new JSONObject(result);
                JSONArray array=object.getJSONArray("weather");

                JSONObject weatherObject=array.getJSONObject(0);

                rMain=weatherObject.getString("main");
                rDescription=weatherObject.getString("description"); //description
                rIcon=weatherObject.getString("icon");

                JSONObject main=object.getJSONObject("main");

                Double temp=main.getDouble("temp");

                rCity=object.getString("name");
                rTemp= (int)(temp-273);

                URL Urlicon = new URL("http://openweathermap.org/img/w/"+rIcon+".png");
                bitImage = BitmapFactory.decodeStream(Urlicon.openConnection().getInputStream());


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid){
            tempature.setText(String.valueOf(rTemp));
            weather.setText(String.valueOf(rMain));
            city.setText(String.valueOf(rCity));
            comment.setText(String.valueOf(rDescription));
            image.setImageBitmap(bitImage);
            super.onPostExecute(aVoid);
        }

    }

}

