package de.rebleyama.server.connection;

import de.rebleyama.server.workPackages.WorkPackage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientManager implements Runnable {
    private byte clientId;
    private BlockingQueue<WorkPackage> clientQueue = new LinkedBlockingQueue<>();

    public ClientManager(byte clientId) {
        this.clientId = clientId;
    }

    public BlockingQueue<WorkPackage> getclientQueue() {
        return  clientQueue;
    }

    @Override
    public void run() {
        // read from queue
        while (true) {
            WorkPackage task = clientQueue.take();
            clientQueue.remove(task);
            // do something
            }
        }
    }
}
