package com.example.madcamp1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


@SuppressLint("HandlerLeak")
public class MainActivity extends AppCompatActivity
{
    private BottomNavigationView bottomNavigationView; // 바텀 네비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private HomeFrag hf;
    private ContactFrag cf;
    private PhotoFrag pf;
    private ExtraFrag ef;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                switch (menuItem.getItemId())
                {
                    case R.id.home:
                        setFrag(0);
                        break;
                    case R.id.contacts:
                        setFrag(1);
                        break;
                    case R.id.photos:
                        setFrag(2);
                        break;
                    case R.id.extra:
                        setFrag(3);
                        break;
                }
                return true;
            }
        });

        hf = new HomeFrag();
        cf = new ContactFrag();
        pf = new PhotoFrag();
        ef = new ExtraFrag();

        setFrag(0); // 첫 프래그먼트 화면 지정
    }

    // 프레그먼트 교체
    private void setFrag(int n)
    {
        fm = getSupportFragmentManager();
        ft= fm.beginTransaction();
        switch (n)
        {
            case 0:
                ft.replace(R.id.Main_Frame,hf);
                ft.commit();
                break;

            case 1:
                ft.replace(R.id.Main_Frame,cf);
                ft.commit();
                break;

            case 2:
                ft.replace(R.id.Main_Frame,pf);
                ft.commit();
                break;

            case 3:
                ft.replace(R.id.Main_Frame,ef);
                ft.commit();
                break;
        }
    }
}