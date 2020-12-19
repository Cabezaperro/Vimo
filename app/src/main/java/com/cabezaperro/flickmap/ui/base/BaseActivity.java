package com.cabezaperro.flickmap.ui.base;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cabezaperro.flickmap.FlickmapApplication;
import com.cabezaperro.flickmap.R;
import com.cabezaperro.flickmap.data.repository.UserRepository;
import com.cabezaperro.flickmap.ui.AboutActivity;
import com.cabezaperro.flickmap.ui.SplashActivity;
import com.cabezaperro.flickmap.ui.settings.SettingsActivity;
import com.cabezaperro.flickmap.ui.SignInActivity;
import com.cabezaperro.flickmap.ui.cinema.CinemaActivity;
import com.cabezaperro.flickmap.ui.favourite.FavouriteActivity;
import com.cabezaperro.flickmap.ui.movie.MovieActivity;
import com.cabezaperro.flickmap.ui.showtime.ShowtimeActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class BaseActivity extends AppCompatActivity implements DrawerLayout.DrawerListener
{
    public static int itemChecked = -1;
    protected DrawerLayout dwlBaseDrawerLayout;
    protected NavigationView nvvBaseNavigationView;
    private Toolbar tlbToolbar;

    private CircleImageView civNavigationProfileImage;
    private TextView txvNavigationUsername;
    private TextView txvNavigationEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        dwlBaseDrawerLayout = findViewById(R.id.dwlBaseDrawerLayout);
        nvvBaseNavigationView = findViewById(R.id.nvvBaseNavigationView);
        tlbToolbar = findViewById(R.id.tlbToolbar);

        setSupportActionBar(tlbToolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dwlBaseDrawerLayout.addDrawerListener(this);
        setupNavigationView();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        if (itemChecked != -1)
            nvvBaseNavigationView.getMenu().getItem(itemChecked).setChecked(true);

        View header = nvvBaseNavigationView.getHeaderView(0);

        civNavigationProfileImage = header.findViewById(R.id.civNavigationProfileImage);
        txvNavigationUsername = header.findViewById(R.id.txvNavigationUsername);
        txvNavigationEmail = header.findViewById(R.id.txvNavigationEmail);

        if (FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            Glide.with(FlickmapApplication.currentContext)
                    .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                    .placeholder(R.drawable.profile_image)
                    .error(R.drawable.profile_image)
                    .into(civNavigationProfileImage);
            txvNavigationUsername.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
            txvNavigationEmail.setText(UserRepository.getInstance().getCurrentUser().getEmail());
        }
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) { }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) { }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) { }

    @Override
    public void onDrawerStateChanged(int newState)
    {
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
        {
            Glide.with(FlickmapApplication.currentContext)
                    .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                    .placeholder(R.drawable.profile_image)
                    .error(R.drawable.profile_image)
                    .into(civNavigationProfileImage);
        }
    }

    public void setupNavigationView()
    {
        nvvBaseNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                Intent intent;

                switch (item.getItemId())
                {
                    case R.id.mniMovies:
                        if (itemChecked != 0)
                        {
                            intent = new Intent(BaseActivity.this, MovieActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        break;
                    case R.id.mniCinemas:
                        if (itemChecked != 1)
                        {
                            intent = new Intent(BaseActivity.this, CinemaActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        break;
                    case R.id.mniShowtimes:
                        if (itemChecked != 2)
                        {
                            intent = new Intent(BaseActivity.this, ShowtimeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        break;
                    case R.id.mniFavourites:
                        if (itemChecked != 3)
                        {
                            intent = new Intent(BaseActivity.this, FavouriteActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        break;
                    case R.id.mniSettings:
                        intent = new Intent(BaseActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        itemChecked = -1;
                        break;
                    case R.id.mniAbout:
                        intent = new Intent(BaseActivity.this, AboutActivity.class);
                        startActivity(intent);
                        itemChecked = -1;
                        break;
                    case R.id.mniLogOut:
                        intent = new Intent(BaseActivity.this, SignInActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("logout", true);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        itemChecked = -1;
                        FirebaseAuth.getInstance().signOut();
                        FlickmapApplication.repositoriesInitialised = false;
                        finish();
                        break;
                }

                dwlBaseDrawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });
    }

    private void setItemChecked(boolean value)
    {
        if (itemChecked != -1)
            nvvBaseNavigationView.getMenu().getItem(itemChecked).setChecked(value);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                dwlBaseDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        if (dwlBaseDrawerLayout.isDrawerOpen(GravityCompat.START))
            dwlBaseDrawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
    }
}
