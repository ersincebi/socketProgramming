class server implements Runnable{
	int servId;

	/**
	 * for defining initialized thread
	 * @param servId defined server id
	 */
	public server(int servId){ this.servId = servId; }

	/**
	 * ones it initialized, server thread allways tries to take job every half second
	 * if its queue is loaded, takes the values adds them up
	 * then sets server status as 0
	 * then waits as setted in its queue
	 * after releases the server by setting status 1
	 * then repeats
	 */
	public void run(){
		while(true){
			try{
				Thread.sleep(500);
				int val = takeJob();
				manageServer.releaseJobQueue(servId);
				if(val==0){ continue; }
				openServer(val);
				Thread.sleep(val*1000);
				closeServer();
			}catch(Exception e){}
		}
	}

	/**
	 * finds server and returns queue of that array
	 * @param servId required server
	 * @return adds the value inside array and returns integer value
	 */
	public int takeJob(){
		int sum = 0;
		int[] jobList = manageServer.getJob(servId);
		for(int item : jobList) sum+=item;
		return sum;
	}

	/**
	 * coloses server and says how many second it will wait
	 * @param val for seeing the waiting time
	 */
	public void openServer(int val){
		System.out.println((servId+1) + ". Server is running, data processing will take "+ val +" seconds");
		manageServer.setSts(servId,0);
	}

	/**
	 * releases the server
	 * gives ready flag by calling semaphore function in loadBalancer start()
	 */
	public void closeServer(){
		System.out.println((servId+1) + ". Server is finished its process!");
		manageServer.setSts(servId,1);
		loadBalancer.start();
	}
}