package de.rebleyama.server;

import de.rebleyama.server.connection.MessageBroker;

public class Main {

    private Main() {

    }

    public static void main(String[] args) {
        // Initialize the message broker

        MessageBroker messageBroker = new MessageBroker(21012);

        messageBroker.setRunning(true);
        new Thread(messageBroker).start();
    }
}