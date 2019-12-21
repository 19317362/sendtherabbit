package com.hayn.sendtherabbit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class MainActivity extends AppCompatActivity {

    EditText editHost;
    EditText editMessage;
    Button buttonSend;
    TextWatcher tw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //init activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setup widgets
        editHost = findViewById(R.id.editHost);
        editMessage = findViewById(R.id.editMessage);
        buttonSend = findViewById(R.id.buttonSend);

        //set not clickable, because app starts with no text entered in fields
        buttonSend.setClickable(false);

        //TextWatcher to enable or disable send button
        tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(isEmpty(editHost) | isEmpty(editMessage)){
                    buttonSend.setClickable(false);
                }else{
                    buttonSend.setClickable(true);
                }
            }
        };

        //setup onClickListener for send button
        buttonSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                publishMessage(); //send message

                //create Toast
                createToast("Message Sent!", Toast.LENGTH_SHORT);
            }
        });

        editHost.addTextChangedListener(tw);
        editMessage.addTextChangedListener(tw);
    }

    private synchronized void publishMessage(){ //publishes current editText's text to host address

    }

    private boolean isEmpty(EditText etText){ //returns true if not empty
        return etText.getText().toString().trim().length() == 0;
    }

    public void createToast(CharSequence msg, int length){
        Toast.makeText(getApplicationContext(), msg, length).show();
    }

    //is this needed??
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected void onDestroy(){
        super.onDestroy();
        //createToast("exiting...",Toast.LENGTH_SHORT); //possible to do
    }
}
