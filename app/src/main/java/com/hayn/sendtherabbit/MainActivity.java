package com.hayn.sendtherabbit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //init activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setup widgets
        editHost = findViewById(R.id.editHost);
        editMessage = findViewById(R.id.editMessage);
        buttonSend = findViewById(R.id.buttonSend);

        //setup onClickListener for send button
        buttonSend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                publishMessage(); //send message

                //create Toast
                createToast("Message Sent!", Toast.LENGTH_SHORT);
            }
        });
    }

    private synchronized void publishMessage(){ //publishes current editText's text to host address

    }

    public void createToast(CharSequence msg, int length){
        Toast.makeText(getApplicationContext(), msg, length).show();
    }

    //is this needed??
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected void onDestroy(){
        //createToast("exiting...",Toast.LENGTH_SHORT); //possible to do
        super.onDestroy();
    }
}
