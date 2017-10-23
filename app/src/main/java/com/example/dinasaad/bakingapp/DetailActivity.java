package com.example.dinasaad.bakingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by DinaSaad on 02/07/2017.
 */
public class DetailActivity extends AppCompatActivity {
    Context context;
    private boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(isNetworkAvailable(this)) {

            setContentView(R.layout.activity_detail);

        }
        else{
            // Provide feedback to user
            Toast.makeText(this, "No internet connection", Toast.LENGTH_LONG).show();
        }
        if (findViewById(R.id.tabletcontainer) != null) {
            Utilities.Istablet = true;


        } else {
            Utilities.Istablet = false;
        }
    }
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

}
