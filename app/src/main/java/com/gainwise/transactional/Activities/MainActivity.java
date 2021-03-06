package com.gainwise.transactional.Activities;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.gainwise.transactional.Fragments.SettingsFragment;
import com.gainwise.transactional.R;
import com.gainwise.transactional.RoomFiles.MyRoomDatabase;
import com.gainwise.transactional.Utilities.AdapterViewPager;

import osmandroid.project_basics.Task;
import spencerstudios.com.bungeelib.Bungee;
import spencerstudios.com.fab_toast.FabToast;

public class MainActivity extends AppCompatActivity {

    public static com.gainwise.transactional.RoomFiles.MyRoomDatabase db;
    public static ViewPager viewPager;
   public static SharedPreferences prefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // Find the view pager that will allow the user to swipe between fragments
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        AdapterViewPager adapter = new AdapterViewPager(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);



//TODO remove the maintrhreadallowance
         db = Room.databaseBuilder(getApplicationContext(),
                com.gainwise.transactional.RoomFiles.MyRoomDatabase.class, "_database_transaction_master")
                // .fallbackToDestructiveMigration()
                 .addMigrations(MyRoomDatabase.MIGRATION_2_3)
                 .allowMainThreadQueries().build();
         prefs = PreferenceManager.getDefaultSharedPreferences(this);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        startActivity(new Intent(this, SettingsActivity.class));
            Bungee.card(this);

            return true;
        }
        if (id == R.id.rate) {
        Task.RateApp(this,"com.gainwise.transactional");

            return true;
        }
        if (id == R.id.share) {
            Task.ShareApp(this,"com.gainwise.transactional", "TransActive", "Easily track your purchases and deposites, find out where your money is going!");


            return true;
        }
        if (id == R.id.license) {
            startActivity(new Intent(this, javatest.class));
            Bungee.slideUp(this);

            return true;
        }




        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("JOSHMain", "onPause");
    }
    public static String getCurrencySymbol() {
        String currencySymbol = MainActivity.prefs.getString(SettingsFragment.CURRENCY_SYMBOL, "$");
        return currencySymbol;
    }

    public static boolean expenseTrackerOnlyMode(){
        if(MainActivity.prefs.getString(SettingsFragment.MODE_KEY,"expenseTracker").equals("expenseTracker")){
            return true;
        }else{
            return false;
        }
    }

    public static String getColorBasedOffMainLabel(String MainLabel){
        String color = "";
        //TODO complete this method for when user selects the label from list
        return color;
    }


    public static String getIncludeStatStatus(String main){
        return db.TransactionDAO().getIncludeStatStatus(main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("JOSHspawn", "onDestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("JOSHspawn", "onStop");
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.i("JOSHspawn", "MemoryLow");
    }

    boolean double_backpressed = false;
    @Override
    public void onBackPressed(){
        if(double_backpressed) {
            super.onBackPressed();
            return;
        }
        this.double_backpressed=true;
        FabToast.makeText(MainActivity.this,
                "Click back again to exit.",Toast.LENGTH_SHORT,FabToast.INFORMATION,FabToast.POSITION_DEFAULT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                double_backpressed = false;
            }
        },2000);

    }
}
