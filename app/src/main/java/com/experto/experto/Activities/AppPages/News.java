package com.experto.experto.Activities.AppPages;

import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.experto.experto.Adapters.PostsAdapter;
import com.experto.experto.R;
import com.google.firebase.auth.FirebaseAuth;

public class News extends AppCompatActivity {

    private GridView postsGrid;
    private PostsAdapter postsAdapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationmenu;
    private NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        postsGrid = (GridView)findViewById(R.id.grid_news);
        postsAdapter = new PostsAdapter(this, R.layout.post_item, ExpertoLoading.getPostList());
        postsGrid.setAdapter(postsAdapter);
        mAuth = FirebaseAuth.getInstance();
        navigationmenu = (NavigationView) findViewById(R.id.navigation_menu);
        // start making the navigation menu button
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open_drawer_layout,R.string.close_drawer_layout);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // end making the navigation menu button
        onNavigationItemSelectedListener= Home.initializeNavigationMenuListener(this,drawerLayout,navigationmenu,
                onNavigationItemSelectedListener,mAuth.getCurrentUser());
        navigationmenu.setNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // this for the navigation menu button
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        // this for the Home button
        switch (item.getItemId()){
            case R.id.news_main_hoem:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
