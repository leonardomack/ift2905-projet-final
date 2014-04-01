package com.example.nobelprize;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //pour test Vincent, � supprimer
        Intent testSearch = new Intent(getApplicationContext(), SearchActivity.class);
        //sera utile pour envoyer param�tre entre les pages ex: id du laur�at
        //testSearch.putExtra("key", "value");
        //String value = i.getStringExtra("key");
        startActivity(testSearch);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
