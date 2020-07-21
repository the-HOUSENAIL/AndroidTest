package com.example.teltest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.Activity;
import android.content.ComponentName;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    public static int PICK_CONTACT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn = (Button) findViewById(R.id.showSecondActivityBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent生成
                Intent intent = new Intent();
                // Action設定(連絡先)
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Type設定
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                // 起動
                startActivityForResult(intent, PICK_CONTACT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONTACT) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contactData = data.getData();
                CursorLoader cursorLoader = new CursorLoader(this, contactData, null, null, null, null);
                Cursor cursor = cursorLoader.loadInBackground();

                if (cursor.moveToFirst()) {
                    // 連絡先ID
                    String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    // 連絡先名取得
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME));

                    String phoneNum = "電話番号がないよ";

                    Cursor phone = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null);

                    if (phone.moveToFirst()) {
                        //電話番号取得
                        phoneNum = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    // cursor閉じる
                    phone.close();

                    // 画面表示
                    EditText show1 = (EditText) findViewById(R.id.edit1);
                    EditText show2 = (EditText) findViewById(R.id.edit2);
                    show1.setText(name);
                    show2.setText(phoneNum);
                }
                cursor.close();
            }
        }
    }
}