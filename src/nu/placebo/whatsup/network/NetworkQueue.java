package nu.placebo.whatsup.network;

import java.util.LinkedList;
import java.util.List;

import nu.placebo.whatsup.constants.Constants;

/**
 * 
 * This class makes sure not to many service calls are running and creates
 * threads to run calls in. The class is a singleton.
 * 
 */

public class NetworkQueue {
	private static NetworkQueue instance;
	private List<NetworkQueueItem> queue = new LinkedList<NetworkQueueItem>();
	private int activeCalls = 0;

	/**
	 * Private constructor to ensure that the class is singleton.
	 */
	private NetworkQueue() {
	}

	/**
	 * Gives access to the instance of the NetworkQueue.
	 * 
	 * @return
	 */
	public static NetworkQueue getInstance() {
		if (instance == null) {
			instance = new NetworkQueue();
		}
		return instance;
	}

	public void add(NetworkQueueItem item){
		this.queue.add(item);
		if(canStartNewCall()) {
			nextCall();
		}
	}

	private void nextCall() {
		if(!this.queue.isEmpty()) {
			this.activeCalls++;
			new Thread(new Runnable() {
				public void run() {        
					queue.remove(0).execute();
					activeCalls--;
				}
			}).start();
		}
	}

	private boolean canStartNewCall() {
		return this.activeCalls <= Constants.ALLOWED_CONCURRENT_CALLS;
	}
}
