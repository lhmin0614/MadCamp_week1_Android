package com.example.madcamp1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder> {
    Context mContext;
    List<Contacts> contactsList;

    public ContactsAdapter(Context mContext, List<Contacts> contactsList) {
        this.mContext = mContext;
        this.contactsList = contactsList;
    }

    @NonNull
    public ContactsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_item,parent,false);
        return new ContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsViewHolder holder, int position) {
        Contacts contacts = contactsList.get(position);
        holder.name_contact.setText(contacts.getName());
        holder.phone_contact.setText(contacts.getPhone_num());

        if(contacts.getPhoto() != null){
            Picasso.get().load(contacts.getPhoto()).into(holder.img_contact);
        } else{
            holder.img_contact.setImageResource(R.drawable.cute_profile);
        }
    }

    public int getItemCount(){
        return contactsList.size();
    }

    public class ContactsViewHolder extends RecyclerView.ViewHolder{
        TextView name_contact, phone_contact;
        CircleImageView img_contact;
        ContactFrag contactFrag;

        public ContactsViewHolder(@NonNull View itemView) {
            super(itemView);
            name_contact = itemView.findViewById(R.id.name_contact);
            phone_contact = itemView.findViewById(R.id.phone_contact);
            img_contact = itemView.findViewById(R.id.img_contact);

            //하나의 constraint view layout 클릭했을때의 리스너
            itemView.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        //텍스트 뷰에서 스트링 받아오기
                        String name_str = name_contact.getText().toString();
                        String phone_str = phone_contact.getText().toString();
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("💜전화💜");
                        builder.setMessage("정말 "+ name_str +"에게 전화를 걸까요??");
                        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String tel = "tel:" + phone_str;
                                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(tel)));
                                //바로 전화하게 하고 싶으면 ACTION_CALL로 바꾸기
                            }
                        });
                        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(mContext.getApplicationContext(), "취소되었습니다", Toast.LENGTH_LONG).show();
                            }
                        });
                        builder.show();
                    }
                }
            });
        }
    }
}