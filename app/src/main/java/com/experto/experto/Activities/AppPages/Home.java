package com.experto.experto.Activities.AppPages;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.experto.experto.Activities.Authentication.Profile;
import com.experto.experto.Activities.Authentication.SignIn;
import com.experto.experto.Adapters.HomeItemsAdapter;
import com.experto.experto.AppData.Service;
import com.experto.experto.ListItems.HomeItem;
import com.experto.experto.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Home extends AppCompatActivity {
    private static final String TAG = "Main";
    private HomeItemsAdapter adapter;
    private List<HomeItem> homeItems;
    private GridView homeItemsList;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private static HashMap<String,Object> requestInfo = new HashMap<>();
    private NavigationView navigationmenu;
    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        homeItemsList = (GridView) findViewById(R.id.grid_main);
        homeItems = new ArrayList<HomeItem>();
        navigationmenu = (NavigationView) findViewById(R.id.navigation_menu);
        // start making the navigation menu button
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open_drawer_layout,R.string.close_drawer_layout);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // end making the navigation menu button
        mAuth = FirebaseAuth.getInstance();
        onNavigationItemSelectedListener= initializeNavigationMenuListener(this,
                drawerLayout,navigationmenu,onNavigationItemSelectedListener,mAuth.getCurrentUser());
        navigationmenu.setNavigationItemSelectedListener(onNavigationItemSelectedListener);

        adapter = new HomeItemsAdapter(this, R.layout.home_item, homeItems);
        final List<Service> menuList = ExpertoLoading.getMenuList();
        if (menuList != null) {
            for (int i = 0; i < menuList.size(); i++) {
                    Service service = menuList.get(i);
                    String name = service.getName();
                    String nameLowerCase = name.toLowerCase();
                    // get the resource of the image
                    int resource = getResources().getIdentifier(nameLowerCase, "drawable", getPackageName());
                    Drawable drawable;
                    if(resource !=0){
                        drawable = getResources().getDrawable(resource);
                    }
                    else {
                         drawable = null;
                    }
                    HomeItem homeItem = new HomeItem(name);
                    homeItem.setImg(drawable);
                    adapter.add(homeItem);

            }
            homeItemsList.setAdapter(adapter);
            homeItemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    // save the selected service object in the Request info list
                    requestInfo.put("service",menuList.get(i));
                    String serviceName = homeItems.get(i).getName();
                    if(serviceName.equals("Computer") || serviceName.equals("Support")){
                        Intent intent = new Intent(Home.this,ProblemActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(Home.this,CompanyActivity.class);
                        startActivity(intent);

                    }
                }
            });
        }
        else {
            Toast.makeText(this,R.string.noData,Toast.LENGTH_LONG).show();
        }

    }

    public static HashMap<String, Object> getRequestInfo() {
        return requestInfo;
    }

    public static NavigationView.OnNavigationItemSelectedListener initializeNavigationMenuListener(final Context context, final DrawerLayout mDrawerLayout, NavigationView navigationmenu,
                                                                                                   NavigationView.OnNavigationItemSelectedListener listener, FirebaseUser user){
        // free the list to have a new list whenever the menu is shown and to avoid duplicates
        navigationmenu.getMenu().clear();
        // to have different menus when there is a user and when there is not
        if(user == null){
            navigationmenu.inflateMenu(R.menu.navigation_menu);
        }
        else {
            navigationmenu.inflateMenu(R.menu.navigation_menu_user);
        }
        // to differentiate between the Home and the other activities
        final Boolean isHome = context instanceof Home;
        listener = new NavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()){
                    case R.id.nav_home:
                        if(context instanceof Home){
                            mDrawerLayout.closeDrawers();
                        }
                        else {
                            if(!isHome){
                                ((Activity)context).finish();
                            }
                            else {
                                mDrawerLayout.closeDrawers();
                            }
                        }
                        break;
                    case R.id.nav_prfile:
                        if(context instanceof Profile){
                            mDrawerLayout.closeDrawers();
                        }
                        else {
                            intent = new Intent(context,Profile.class);
                            context.startActivity(intent);
                            if(!isHome){
                                ((Activity)context).finish();
                            }
                            else {
                                mDrawerLayout.closeDrawers();
                            }
                        }
                        break;
                    case R.id.nav_sign_in:
                        if(context instanceof SignIn){
                            mDrawerLayout.closeDrawers();
                        }
                        else {
                            intent = new Intent(context,SignIn.class);
                            context.startActivity(intent);
                            if(!isHome){
                                ((Activity)context).finish();
                            }
                            else {
                                mDrawerLayout.closeDrawers();
                            }
                        }
                        break;
                    case R.id.nav_feeds:
                        if(context instanceof Feedback){
                            mDrawerLayout.closeDrawers();
                        }
                        else {
                            intent = new Intent(context,Feedback.class);
                            context.startActivity(intent);
                            mDrawerLayout.closeDrawers();
                            if(!isHome){
                                ((Activity)context).finish();
                            }
                            else {
                                mDrawerLayout.closeDrawers();
                            }
                        }
                        break;
                    case R.id.nav_requests:
                        if(context instanceof RequestsActivity){
                            mDrawerLayout.closeDrawers();
                        }
                        else {
                            intent = new Intent(context,RequestsActivity.class);
                            context.startActivity(intent);
                            if(!isHome){
                                ((Activity)context).finish();
                            }
                            else {
                                mDrawerLayout.closeDrawers();
                            }
                        }
                        break;
                    case R.id.nav_conditions:
                        // to open the link in a browser
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://google.com"));
                        context.startActivity(intent);
                        mDrawerLayout.closeDrawers();
                }
                return true;
            }
        };
        return listener;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // this for the navigation menu button
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        // this for the News button
        switch (item.getItemId()){
            case R.id.home_main_news:
                Intent intent = new Intent(this,News.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // this is to refresh the menu when the user login
        mAuth = FirebaseAuth.getInstance();
        onNavigationItemSelectedListener= initializeNavigationMenuListener(this,
                drawerLayout,navigationmenu,onNavigationItemSelectedListener,mAuth.getCurrentUser());
    }
}
