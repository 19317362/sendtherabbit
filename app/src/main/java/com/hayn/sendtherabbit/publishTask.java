package com.hayn.sendtherabbit;

import android.os.AsyncTask;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

class publisherTask extends AsyncTask<String,Void,Void> {

    static Channel channel;
    static Connection connection;
    ConnectionFactory factory;
    static String exchange = "exchange1";
    static String host;

    @Override
    protected Void doInBackground(String... message) {
        factory = new ConnectionFactory();

        factory.setHost(host);
        connection = null;

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.basicPublish(exchange,"", null, message[0].getBytes());
            System.out.println("Message Sent '" + message[0] + "'");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }

        return null;
    }

    static void exit() throws IOException, TimeoutException {
        connection.close();
        channel.close();
    }

    static void setHost(String host) {
        publisherTask.host = host;
    }
}
