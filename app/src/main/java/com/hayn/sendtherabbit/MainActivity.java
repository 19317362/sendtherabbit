package com.hayn.sendtherabbit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    /**
     * APP capable of sending messages to rabbitMQ instance
     *
     * This App is developed for Emergency Situations and allows parties to send
     * Emergency Broadcast Messages to a RabbitMQ Direct Exchange, where it will be
     * further distributed to all people in the compromised area.
     *
     * Written by Roman Hayn, 25138
     */

    EditText editHost, editMessage, editUser, editPass;
    Button buttonSend;
    TextWatcher textWatcher;
    SharedPreferences prefs;

    final String hostKey = "hostname";
    final String userKey = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //init activity
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get saved preferences (Username and Host address)
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
                //Enable button only if text present in Host-, User- and Message-Text box
                buttonSend.setEnabled(!(isEmpty(editHost) | isEmpty(editMessage) | isEmpty(editUser)));
            }
        };

        //setup onClickListener for send button
        buttonSend.setOnClickListener(view -> {
            //createToast("Message Sent!", Toast.LENGTH_SHORT); //create Toast
            publishTask.setHost(editHost.getText().toString().trim());
            publishTask.setAccount(editUser.getText().toString().trim(), editPass.getText().toString().trim());
            publishTask.setContext(getApplicationContext());
            new publishTask().execute(editMessage.getText().toString().trim());
        });

        //setup textChanged Listeners to evaluate if sending message should be enabled or not
        editHost.addTextChangedListener(textWatcher);
        editMessage.addTextChangedListener(textWatcher);
        editUser.addTextChangedListener(textWatcher);

        //load saved Preferences, like Host address and username
        loadPreferences();
    }

    private boolean isEmpty(EditText tmpText){ //returns false if editText not empty
        return tmpText.getText().toString().trim().length() == 0;
    }

    void createToast(CharSequence msg, int length){ //simple function to create Toasts
        Toast.makeText(getApplicationContext(), msg, length).show();
    }

    private void savePreferences(){ // It saves Host and Username in the preferences
        prefs.edit().putString(hostKey, editHost.getText().toString().trim()).apply();
        prefs.edit().putString(userKey, editUser.getText().toString().trim()).apply();
    }

    private void loadPreferences(){ // loads Hostname and Username from Preferences
        String host = prefs.getString(hostKey,"");
        String user = prefs.getString(userKey,"");
        editHost.setText(host);
        editUser.setText(user);
        if(host.equals("") && user.equals("")){
            createToast("Previously saved data not found", Toast.LENGTH_LONG);
        }else{
            createToast("Data done loading", Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        savePreferences(); //save preferences when leaving activity
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected void onDestroy(){
        super.onDestroy();
        createToast("Exit successful",Toast.LENGTH_SHORT);
    }
}
