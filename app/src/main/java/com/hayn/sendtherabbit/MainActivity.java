package com.hayn.sendtherabbit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {

    /**
     * APP to send rabbitMQ messages
     *
     * TODO:
     */

    EditText editHost, editMessage, editUser, editPass;
    Button buttonSend;
    TextWatcher textWatcher;
    SharedPreferences prefs;

    String hostKey = "hostname";
    String userKey = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //init activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get saved preferences
        prefs = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);

        //setup widgets
        editHost = findViewById(R.id.editHost);
        editMessage = findViewById(R.id.editMessage);
        editUser = findViewById(R.id.editUser);
        editPass = findViewById(R.id.editPass);
        buttonSend = findViewById(R.id.buttonSend);

        //set not clickable, because app starts with no text entered in fields
        buttonSend.setEnabled(false);

        //TextWatcher to enable or disable send button
        textWatcher = new TextWatcher() {
            //before- and onTextChanged both unused, but needed for TextWatcher
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override public void afterTextChanged(Editable s) {
                //Enable button only if text present in both editTexts
                buttonSend.setEnabled(!(isEmpty(editHost) | isEmpty(editMessage) | isEmpty(editUser)));
            }
        };

        //setup onClickListener for send button
        buttonSend.setOnClickListener(new View.OnClickListener(){
            @Override public void onClick(View view){
                //createToast("Message Sent!", Toast.LENGTH_SHORT); //create Toast
                publishTask.setHost(editHost.getText().toString().trim());
                publishTask.setAccount(editUser.getText().toString().trim(), editPass.getText().toString().trim());
                publishTask.setContext(getApplicationContext());
                new publishTask().execute(editMessage.getText().toString().trim());
            }
        });

        //setup textChanged Listeners to evaluate if sending message should be enabled or not
        editHost.addTextChangedListener(textWatcher);
        editMessage.addTextChangedListener(textWatcher);
        editUser.addTextChangedListener(textWatcher);

        //load saved Preferences, like Host address etc..
        loadPreferences();
    }

    /**
    private synchronized void publishMessage(){ //publishes current editText's text to host address
        factory.setHost(editHost.getText().toString().trim()); //set host to sanitized Host editText
        String message = editMessage.getText().toString().trim(); // sanitize message from Message editText
        connection = null;

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.basicPublish(exchange,"", null, message.getBytes());
            System.out.println("Message Sent '" + message + "'");
            createToast("Message Sent!", Toast.LENGTH_SHORT);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            createToast("ERROR: " + e.getLocalizedMessage() ,Toast.LENGTH_LONG);
        }
    }
     */

    private boolean isEmpty(EditText etText){ //returns true if editText not empty
        return etText.getText().toString().trim().length() == 0;
    }

    void createToast(CharSequence msg, int length){
        Toast.makeText(getApplicationContext(), msg, length).show();
    }

    private void savePreferences(){
        prefs.edit().putString(hostKey, editHost.getText().toString().trim()).apply();
        prefs.edit().putString(userKey, editUser.getText().toString().trim()).apply();
    }

    private void loadPreferences(){
        editHost.setText(prefs.getString(hostKey,""));
        editUser.setText(prefs.getString(userKey,""));
    }

    @Override
    protected void onStop(){
        super.onStop();
        savePreferences(); //save preferences when leaving activity
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected void onDestroy(){
        super.onDestroy();

        try {
            publishTask.exit();
            createToast("Exit successful",Toast.LENGTH_SHORT); //possible to do
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
