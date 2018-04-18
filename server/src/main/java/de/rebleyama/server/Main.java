package de.rebleyama.server;

import de.rebleyama.server.net.MessageManager;

public class Main {

    private Main() {

    }

    public static void main(String[] args) {
        // Initialize the message broker

        MessageManager messageManager = new MessageManager(21012);
        messageManager.begin();

        try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        }
        
        messageManager.end();
    }
}