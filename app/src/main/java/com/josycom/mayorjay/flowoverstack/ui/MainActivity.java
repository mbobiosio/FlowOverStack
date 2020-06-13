package com.josycom.mayorjay.flowoverstack.ui;

import android.content.Intent;
import android.os.Bundle;

import com.josycom.mayorjay.flowoverstack.R;
import com.josycom.mayorjay.flowoverstack.databinding.ActivityMainBinding;
import com.josycom.mayorjay.flowoverstack.util.StringConstants;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.Menu;
import android.view.MenuItem;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    private FragmentTransaction mFragmentTransaction;
    private boolean isFragmentDisplayed = false;
    private NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        setSupportActionBar(activityMainBinding.toolbar);
        mNavController = Navigation.findNavController(this, R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(mNavController.getGraph()).build();
        NavigationUI.setupWithNavController(activityMainBinding.toolbar, mNavController, appBarConfiguration);

//        if (savedInstanceState != null) {
//            isFragmentDisplayed = savedInstanceState.getBoolean(StringConstants.STATE_FRAGMENT);
//        }
//
//        if (!isFragmentDisplayed) {
//            if (findViewById(R.id.fragment_container) != null) {
//                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
//                mFragmentTransaction.add(R.id.fragment_container, new QuestionsByActivityFragment()).commit();
//                isFragmentDisplayed = true;
//            }
//        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(mNavController.getGraph()).build();
        return NavigationUI.navigateUp(mNavController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_activity_dest_to_search_dest) {
            //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            mNavController.navigate(R.id.search_dest);
//            startActivity(new Intent(this, SearchActivity.class));
//            overridePendingTransition(R.anim.fade_in_anim, R.anim.fade_out_anim);
            return true;
        } else if (id == R.id.action_activity_dest_to_recent_dest) {
            if (item.getTitle().equals("Filter by Recency")){
                //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                Bundle arg = new Bundle();
                arg.putString("recent_header_text_key", "Recent Questions");
                arg.putString("recent_sort_key", StringConstants.SORT_BY_CREATION);
                mNavController.navigate(R.id.activity_dest, arg);
                item.setTitle("Filter by Activity");
            } else if (item.getTitle().equals("Filter by Activity")){
                //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                Bundle arg = new Bundle();
                arg.putString("active_header_text_key", "Active Questions");
                arg.putString("active_sort_key", StringConstants.SORT_BY_ACTIVITY);
                mNavController.navigate(R.id.activity_dest, arg);
                item.setTitle("Filter by Recency");
            }

//            if (findViewById(R.id.fragment_container) != null && item.getTitle().equals(getString(R.string.action_filter_by_recency))) {
//                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
//                mFragmentTransaction.replace(R.id.fragment_container, new QuestionsByCreationFragment()).commit();
//                item.setTitle(R.string.action_filter_by_activity);
//            } else if (findViewById(R.id.fragment_container) != null && item.getTitle().equals(getString(R.string.action_filter_by_activity))) {
//                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
//                mFragmentTransaction.replace(R.id.fragment_container, new QuestionsByActivityFragment()).commit();
//                item.setTitle(R.string.action_filter_by_recency);
//            }
            return true;
        } else if (id == R.id.action_activity_dest_to_hot_dest) {
            //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            Bundle arg = new Bundle();
            arg.putString("hot_header_text_key", "Hot Questions");
            arg.putString("hot_sort_key", StringConstants.SORT_BY_HOT);
            mNavController.navigate(R.id.activity_dest, arg);
//            if (findViewById(R.id.fragment_container) != null) {
//                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
//                mFragmentTransaction.replace(R.id.fragment_container, new QuestionsByHotFragment()).commit();
//            }
        } else if (id == R.id.action_activity_dest_to_vote_dest) {
            //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            Bundle arg = new Bundle();
            arg.putString("vote_header_text_key", "Voted Questions");
            arg.putString("vote_sort_key", StringConstants.SORT_BY_VOTES);
            mNavController.navigate(R.id.activity_dest, arg);
//            if (findViewById(R.id.fragment_container) != null) {
//                mFragmentTransaction = getSupportFragmentManager().beginTransaction();
//                mFragmentTransaction.replace(R.id.fragment_container, new QuestionsByVoteFragment()).commit();
//            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(StringConstants.STATE_FRAGMENT, isFragmentDisplayed);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fade_in_anim, R.anim.fade_out_anim);
    }
}
