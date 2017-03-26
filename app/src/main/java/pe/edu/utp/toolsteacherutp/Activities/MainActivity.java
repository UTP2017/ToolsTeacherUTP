package pe.edu.utp.toolsteacherutp.Activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import pe.edu.utp.toolsteacherutp.Fragments.CalificationsFragment;
import pe.edu.utp.toolsteacherutp.Fragments.CoursesFragment;
import pe.edu.utp.toolsteacherutp.Fragments.CreditsFragment;
import pe.edu.utp.toolsteacherutp.Fragments.SettingsFragment;
import pe.edu.utp.toolsteacherutp.Fragments.ProfileFragment;
import pe.edu.utp.toolsteacherutp.Fragments.SchedulesFragment;
import pe.edu.utp.toolsteacherutp.Models.AccessToken;
import pe.edu.utp.toolsteacherutp.Models.User;
import pe.edu.utp.toolsteacherutp.MyAplication;
import pe.edu.utp.toolsteacherutp.R;
import pe.edu.utp.toolsteacherutp.Transforms.CircleTransform;

public class MainActivity extends AppCompatActivity {

    private AccessToken currentToken;
    private User currentUser;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentToken = ((MyAplication) getApplication() ).getCurrentAccesToken();
        currentUser = (( MyAplication ) getApplication() ).getCurrentUser();

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nav_view);

        drawerToggle = setupDrawerToggle();
        setupDrawerContent(nvDrawer);
        mDrawer.addDrawerListener(drawerToggle);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, new SchedulesFragment() ).commit();

        mDrawer.closeDrawers();

        View headerView = nvDrawer.getHeaderView( 0 );

        ImageView teacherImageView = ( ImageView ) headerView.findViewById(R.id.teacherImageView);
        TextView teacherNameView = (TextView) headerView.findViewById(R.id.teacherNameView);
        TextView teacherEmailView = ( TextView ) headerView.findViewById(R.id.teacherEmailView);

        Picasso.with( this ).load( currentUser.getAvatar() ).transform( new CircleTransform() ).into(teacherImageView);
        teacherNameView.setText( currentUser.getName() );
        teacherEmailView.setText( currentUser.getCorreo() );
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (id == R.id.action_sign_out) {
            ((MyAplication) getApplication() ).signOut();
            startActivity( new Intent( this, LoginActivity.class ));
            finish();
            return true;
        }
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });

    }

    private void selectDrawerItem(MenuItem menuItem) {
        Fragment fragment = null;
        Class fragmentClass;
        switch(menuItem.getItemId()) {
            case R.id.nav_schedules:
                fragmentClass = SchedulesFragment.class;
                break;
            case R.id.nav_courses:
                fragmentClass = CoursesFragment.class;
                break;
            case R.id.nav_profile:
                fragmentClass = ProfileFragment.class;
                break;
            case R.id.nav_manage:
                fragmentClass = SettingsFragment.class;
                startActivity( new Intent( this, SettingsActivity.class ));
                break;
            default:
                fragmentClass = CoursesFragment.class;
        }
        if ( menuItem.getItemId() == R.id.nav_manage ){
            return;
        }

        if ( menuItem.getItemId() == R.id.nav_sign_out ){
            ((MyAplication) getApplication() ).signOut();
            startActivity( new Intent( this, LoginActivity.class ));
            finish();
            return;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment).commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();

    }
}

