package com.example.womensafetyapp;

//import static com.example.womensafetyapp.R.id.fragment_contain;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

public class DashboardActivity extends FragmentActivity {

    MeowBottomNavigation bottomNavigation;
//    TextView temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

//        temp = findViewById(R.id.textView2);
//
//        Intent intent = getIntent();
//        String fullname = intent.getStringExtra("name");
//        String age = intent.getStringExtra("age");
//        String gender = intent.getStringExtra("gender");
//        String number = intent.getStringExtra("number");
//        String password = intent.getStringExtra("password");

        bottomNavigation = findViewById(R.id.bottom_navigation);

        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.home));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.name));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.forum));
        bottomNavigation.add(new MeowBottomNavigation.Model(4,R.drawable.settings));

//        FragmentManager fragmentManager = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
        Fragment homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_layout, homeFragment).commit();
        Fragment contactsFragment = new ContactsFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_layout, contactsFragment).commit();
        Fragment forumFragment = new ForumFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_layout, forumFragment).commit();
        Fragment settingsFragment = new SettingsFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_layout, settingsFragment).commit();
//
//        fragmentTransaction.add(R.id.bottom_navigation,homeFragment);
//
//        fragmentTransaction.commit();

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                Fragment fragment = null;
                switch (item.getId())
                {
                    case 1:
                        fragment = homeFragment;
                        break;
                    case 2:
                        fragment = contactsFragment;
                        break;
                    case 3:
                        fragment = forumFragment;
                        break;
                    case 4:
                        fragment = settingsFragment;
                        break;
                }
                loadFragment(fragment);
            }
        });

        bottomNavigation.show(1,true);
        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                Toast.makeText(getApplicationContext(), "YOU SELECTED "+item.getId(), Toast.LENGTH_SHORT).show();
            }
        });
        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                Toast.makeText(getApplicationContext(), "YOU AGAIN SELECTED "+item.getId(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout,fragment)
                .commit();
    }


}