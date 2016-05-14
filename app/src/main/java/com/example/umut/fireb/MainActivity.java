package com.example.umut.fireb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.core.AndroidSupport;
import com.firebase.client.snapshot.IndexedNode;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //-- Firebase nesnesini diğer activity'lerden de erişebilmek için static olarak tanımlıyoruz.
    public static Firebase firebase;
    EditText takmaAd, email, sifre;
    Button giris, kayit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //-- Firebase Context'ini belirliyoruz
        Firebase.setAndroidContext(this);
        firebase = new Firebase("https://uochat.firebaseio.com/");

        //-- diğer görsel bileşenlerin tanımlamaları
        takmaAd = (EditText) findViewById(R.id.editText4);
        email = (EditText) findViewById(R.id.editText2);
        sifre = (EditText) findViewById(R.id.editText3);

        //-- Giris butonunun tanımlanması
        giris = (Button) findViewById(R.id.button);
        giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAdresi = email.getText().toString();
                String kullanıcıSifresi = sifre.getText().toString();
                String rumuz = takmaAd.getText().toString();
                login(emailAdresi, kullanıcıSifresi, rumuz);
            }
        });

        //-- Kayıt butonunun tanımlanması
        kayit = (Button) findViewById(R.id.button2);
        kayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailAdresi = email.getText().toString();
                String kullanıcıSifresi = sifre.getText().toString();
                kullaniciEkle(emailAdresi, kullanıcıSifresi);
            }
        });

    }

    //-- kullanıcı ekleme metdodu
    public void kullaniciEkle(String email, String sifre) {
        firebase.createUser(email, sifre, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> stringObjectMap) {
                Log.e("Kullanıcı Oluşturuldu", stringObjectMap.get("uid").toString());
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Log.e("Kayıt oluşturulamadı", firebaseError.getMessage());
            }
        });
    }

    //-- login metodu
    public void login(final String email, String sifre, final String takmaAd) {
        firebase.authWithPassword(email, sifre, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                Log.e("Giriş Başarılı", email);

                //-- Rumuz kısmına yazılan takma adınız Chat sayfasına yüklenir ve Chat sayfası açılır..
                Intent i = new Intent(MainActivity.this, Chat.class);
                i.putExtra("takma ad", takmaAd);
                startActivity(i);
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Log.e("Giriş Hatası", firebaseError.getMessage());
            }
        });
    }
}
