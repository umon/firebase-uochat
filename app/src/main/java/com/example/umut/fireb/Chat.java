package com.example.umut.fireb;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;

/**
 * Created by umut on 13.05.2016.
 */
public class Chat extends AppCompatActivity {
    Firebase firebase2;
    ListView listView;
    EditText editText;
    ImageView imageView;

    String takmaAd;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);

        Firebase.setAndroidContext(this);
        firebase2 = MainActivity.firebase;

        takmaAd = getIntent().getStringExtra("takma ad");

        listView = (ListView) findViewById(R.id.listView);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                android.R.id.text1, new ArrayList<String>());
        listView.setAdapter(arrayAdapter);

        editText = (EditText) findViewById(R.id.editText);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //-- imageView'e tıklandığında Kullanıcının Rumuzu ve Yazdıkları, veri tabanındaki "Mesajlar"
                //   kısmına eklenir.r
                String mesaj = editText.getText().toString();
                firebase2.child("Mesajlar").push().setValue(takmaAd + " : " + mesaj);

                //-- daha sonra editText nesnesindeki yazılar temizlenir.
                editText.setText("");
            }
        });


        //-- dinleyicinin ayarlanması
        firebase2.child("Mesajlar").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //-- bu kısımda "Mesajlar" içine bir veri eklendiğinde, eklenen veririn
                //   listView içine de eklenmesi sağlanmıştır.
                arrayAdapter.add(dataSnapshot.getValue());
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
