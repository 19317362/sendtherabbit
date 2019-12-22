package com.hayn.sendtherabbit;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

class publishTask extends AsyncTask<String,Void,Boolean> {

    private static Channel channel;
    private static Connection connection;
    private static String host, username, password;
    private static Context context;

    @Override
    protected Boolean doInBackground(String... message) {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost(host);
        factory.setUsername(username);
        factory.setPassword(password);
        connection = null;

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.basicPublish("exchange1","", null, message[0].getBytes());
            System.out.println("Message Sent '" + message[0] + "'");
            connection.close();
            channel.close();
            return true;
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean){
            Toast.makeText(context, "Message Sent!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Error sending message", Toast.LENGTH_LONG).show();
        }
    }

    static void setHost(String host) {
        publishTask.host = host;
    }

    static void setAccount(String user, String pass){
        username = user;
        password = pass;
    }

    static void setContext(Context c){
        context = c;
    }
}
