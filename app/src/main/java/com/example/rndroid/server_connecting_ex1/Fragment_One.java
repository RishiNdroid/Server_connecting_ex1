package com.example.rndroid.server_connecting_ex1;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_One extends Fragment {

    MyTask myTaskClsas;
    TextView textView;
    public Fragment_One() {
        // Required empty public constructor
    }

    public class MyTask extends AsyncTask<String , Void, String>{
        URL myUrl;
        HttpURLConnection urlConnection;
        InputStream inputStream;
        InputStreamReader streamReader;
        BufferedReader bufferedReader;
        String line;
        StringBuilder resultString;

        @Override
        protected String doInBackground(String... strings) {
            try {
                myUrl = new URL(strings[0]);
                urlConnection = (HttpURLConnection) myUrl.openConnection();
                inputStream = urlConnection.getInputStream();
                streamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(streamReader);
                resultString = new StringBuilder();
                line = bufferedReader.readLine();
                //code for reading line from buffered reader
                while (line != null){
                    resultString.append(line);
                    line = bufferedReader.readLine();
                }
                return resultString.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }catch (SecurityException e){
                e.printStackTrace();
                return "No Internet Permission";
            }
            return "Something went wrong!!!!!";
        }

        @Override
        protected void onPostExecute(String s) {
            textView.setText(s);
            super.onPostExecute(s);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_fragment__one, container, false);
        Button button = (Button) v.findViewById(R.id.button);
        textView = (TextView) v.findViewById(R.id.textview);
        myTaskClsas = new MyTask();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInternet() == true){
                    if (myTaskClsas.getStatus() == AsyncTask.Status.RUNNING || myTaskClsas.getStatus() == AsyncTask.Status.FINISHED){
                        Toast.makeText(getActivity(), "Already Running", Toast.LENGTH_SHORT).show();
                    return;
                    }else {
                        myTaskClsas.execute("http://skillgun.com");
                    }
                }else {
                    Toast.makeText(getActivity(), "Please Connect to active network", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return v;
    }

    public boolean checkInternet(){
        //A. To check whether user has internet acces or not
        ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        //B. From Manager, Get network information
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        //C. Check If network is connected or not
        if(networkInfo == null || networkInfo.isConnected() == false){
            return false;
        }else {
            return true;
        }
    }

}
