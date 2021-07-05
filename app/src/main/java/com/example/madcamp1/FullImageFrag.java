package com.example.madcamp1;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FullImageFrag extends Fragment {
    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        view = inflater.inflate(R.layout.full_image,container,false);
        ImageView iv = view.findViewById(R.id.full_image_view);

        setHasOptionsMenu(true);

        // get uri data and load
        Bundle bundle = getArguments();
        String uri = bundle.getString("Uri");
        Glide.with(this).load(uri).into(iv);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setFrag(2);
                }
            });

        return view;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.actionbar_back, menu);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()==R.id.back){
            ((MainActivity)getActivity()).setFrag(2);
        }
        return super.onOptionsItemSelected(item);
    }
}
