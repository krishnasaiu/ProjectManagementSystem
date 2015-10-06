package control;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/*
 * Copyright: Copyright © 2015
 *
 * @author Krishna Sai
 * @version 1
 */
public class InternetAvailibility implements RunnableFuture<Boolean>{
	
	/*
	 * (non-Javadoc)
	 * @see java.util.concurrent.Future#cancel(boolean)
	 * 
	 * Checks for internet availibility and return true or false depending on the connectivity 
	 */

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCancelled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isDone() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Boolean get() throws InterruptedException, ExecutionException {
		try {
			//make a URL to a known source
			URL url = new URL("http://www.google.co.in");

			//open a connection to that source
			HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();

			//trying to retrieve data from the source. If there
			//is no connection, this line will fail
			@SuppressWarnings("unused")
			Object objData = urlConnect.getContent();

		} catch (UnknownHostException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	@Override
	public Boolean get(long timeout, TimeUnit unit)
			throws InterruptedException, ExecutionException, TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
