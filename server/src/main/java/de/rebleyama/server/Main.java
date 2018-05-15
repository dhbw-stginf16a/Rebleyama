package de.rebleyama.server;

import java.util.Scanner;

import de.rebleyama.server.net.MessageManager;

public class Main {

    private Main() {

    }

    public static void main(String[] args) {
        // Initialize the message broker

        MessageManager messageManager = new MessageManager(21012);
        messageManager.begin();

        Scanner scanner = new Scanner(System.in);
        while(!scanner.hasNextLine()){}
        // scanner.nextLine();
        scanner.close();
        
        messageManager.end();
    }
}