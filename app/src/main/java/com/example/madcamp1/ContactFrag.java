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
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ContactFrag<editText> extends Fragment{
    RecyclerView recyclerView;
    List<Contacts> contactsList = new ArrayList<>();
    List<Contacts> filteredList;
    ContactsAdapter adapter;
    EditText editText;
    Spinner spinner;
    String searchOption;
    Button btnClear;
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
        editText = (EditText) rootView.findViewById(R.id.edittext);
        spinner = (Spinner) rootView.findViewById(R.id.spinner);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        btnClear = (Button) rootView.findViewById(R.id.clearable_button_clear);

        Log.e("Frag", "MainFragment");

        //permission
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
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
                }).check();

        //menu button
        setHasOptionsMenu(true);


        searchOption = "name";
        // spinner option for search setting
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchOption = parent.getItemAtPosition(position).toString();
                Log.i("searchOption", searchOption);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //search bar editText
        TextWatcher textwatcher = new TextWatcher(){
            @Override //before changed
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }
            @Override //during text change
            public void onTextChanged(CharSequence s, int start, int before, int count){
                String text = editText.getText().toString().toLowerCase(Locale.getDefault());
                if( searchOption.equals("name")){
                    text = "0".concat(text);
                }
                else{
                    text = "1".concat(text);
                }
                if(text.length()>1)
                    btnClear.setVisibility(RelativeLayout.VISIBLE);
                else
                    btnClear.setVisibility(RelativeLayout.INVISIBLE);
                adapter.getFilter().filter(text);
                Log.i("onTextChanged", text);
            }
            @Override
            public void afterTextChanged(Editable s){
            }
        };
        editText.addTextChangedListener(textwatcher);

        // text clear button setting
        btnClear.setVisibility(RelativeLayout.INVISIBLE);
        clearText();

        return rootView;
    }

    private void clearText() {
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
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
        Collections.sort(contactsList);
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

