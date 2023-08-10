package com.example.javalearning;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.javalearning.databinding.ActivityMainBinding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseFirestore database;
    FirebaseAuth auth;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());  
        setSupportActionBar(binding.toolbar);

        ArrayList<CategoryModel> category = new ArrayList<>();
        database = FirebaseFirestore.getInstance();
        CategoryAdapter adapter = new CategoryAdapter(this,category);
        binding.categoryList.setLayoutManager(new GridLayoutManager(this,2));
        binding.categoryList.setAdapter(adapter);

        /*if (auth.getCurrentUser() != null) {

            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }*/

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClicked() {
                Toast.makeText(MainActivity.this, "Banner ads added", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                /*super.onAdFailedToLoad(adError);
                mAdView.loadAd(adRequest);*/
            }

            @Override
            public void onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        });

        database.collection("categories").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                  for(DocumentSnapshot snapshot : value.getDocuments()){
                      CategoryModel model = snapshot.toObject(CategoryModel.class);
                      model.setCategoryId(snapshot.getId());
                      category.add(model);
                  }
                  adapter.notifyDataSetChanged();
            }
        });

        /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, new HomeFragment());
        transaction.commit();

        binding.bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                switch (i) {
                    case 0:
                        transaction.replace(R.id.frame_layout, new HomeFragment());
                        transaction.commit();
                        break;
                    case 1:
                        transaction.replace(R.id.frame_layout, new LeaderboardsFragment());
                        transaction.commit();
                        break;
                    case 2:
                        transaction.replace(R.id.frame_layout , new ProfileFragment());
                        transaction.commit();
                        break;
                }
                return false;
            }
        });*/


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout) {
            /*auth = FirebaseAuth.getInstance();
            auth.signOut();
            Toast.makeText(this, "Logout successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);*/

            AlertDialog.Builder checkAlert = new AlertDialog.Builder(MainActivity.this);
            checkAlert.setMessage("Do you want to Logout?")
                    .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            auth = FirebaseAuth.getInstance();
                            auth.signOut();
                            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert = checkAlert.create();
            alert.setTitle("LogOut");
            alert.show();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void onBackPressed() {
             super.onBackPressed();
    }
}