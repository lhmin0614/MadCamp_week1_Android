package com.example.madcamp1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ContactFrag extends Fragment {
    RecyclerView recyclerView;
    List<Contacts> contactsList = new ArrayList<>();
    ContactsAdapter adapter;
    ContactsAdapter.ContactsViewHolder cvh;
    private View view;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    public void onDestroy() {
        contactsList.clear();
        super.onDestroy();
    }

    @Nullable
    public View onCreateView(@NotNull LayoutInflater inflater, @NotNull ViewGroup container, @NotNull Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.contact_frag, container, false);

        ActionBar ab = ((MainActivity)getActivity()).getSupportActionBar();
        ab.setTitle("Contacts");

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycle1);
        adapter = new ContactsAdapter(getActivity(), contactsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        Log.e("Frag", "MainFragment");

        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE)
                .withListener(new MultiplePermissionsListener(){
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        //권한 여부 다 물어보고 실행되는 메소드
                        getContacts();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        //이전 권한 여부를 거부한 권한이 있으면 실행되는 메소드

                    }

//                    @Override
//                    public void onPermissionsGranted(PermissionGrantedResponse response) {
//                        if (response.getPermissionName().equals(Manifest.permission.READ_CONTACTS))
//                            getContacts();
//                    }
//
//                    @Override
//                    public void onPermissionsDenied(PermissionDeniedResponse response) {
//                        Toast.makeText(getActivity(), "Permission should be granted!", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onPermissionsRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//                        token.continuePermissionRequest();
//                    }
                }).check();
        setHasOptionsMenu(true);
        return rootView;
    }

    private void getContacts() {
        Cursor phones = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        while (phones.moveToNext()) {
            String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String phoneUri = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            Contacts contacts = new Contacts(name, phoneNumber, phoneUri);
            contactsList.add(contacts);
            adapter.notifyDataSetChanged();
        }
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.actionbar_contacts, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent = new Intent(ContactsContract.Intents.Insert.ACTION);
        intent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

        if(item.getItemId()==R.id.action_add){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getLayoutInflater();
            builder.setTitle("💜새 연락처 추가💜");
            View view = inflater.inflate(R.layout.add_new, null);
            builder.setView(view);
            builder.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    EditText eName = (EditText) view.findViewById(R.id.new_name);
                    EditText ePhone= (EditText) view.findViewById(R.id.new_phone);
                    String new_eName = eName.getText().toString();
                    String new_ePhone = ePhone.getText().toString();
                    Contacts contacts = new Contacts(new_eName,new_ePhone, null);
                    contactsList.add(contacts);
                    intent.putExtra(ContactsContract.Intents.Insert.NAME, new_eName);
                    intent.putExtra(ContactsContract.Intents.Insert.PHONE, new_ePhone);
                    startActivity(intent);
                    adapter.notifyDataSetChanged();
                }
            });
            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getActivity().getApplicationContext(), "취소되었습니다", Toast.LENGTH_LONG).show();
                }
            });
           builder.create().show();
        }
        return super.onOptionsItemSelected(item);
    }
}

