class manageServer{
	private int size = 0;
	static serverList first;
	static serverList last;
	public manageServer(){
		first = null;
		last = null;
	}

	/**
	 * initializes the servers
	 * sets the linst inside serverList class
	 * @param hms is determines the count of servers
	 */
	public void __init(int hms){ for(int i=0;i<hms;i++) setServer(i,1); }

	/**
	 * returns status, according to requested id
	 * @param id is the number of server
	 * @return returns status of server, 0 for busy, 1 for ready
	 */
	public int searchSts(int id){
		serverList tmp = first;
		while(tmp!=null){
			if(tmp.id==id)
				return tmp.sts;
			tmp = tmp.next;
		}
		return 0;
	}

	/**
	 * returns queue of servers job list according to requested id
	 * @param id is the number of server
	 * @return returns queue of server
	 */
	public static int[] getJob(int id){
		serverList tmp = first;
		while(tmp!=null){
			if(tmp.id==id)
				return tmp.job;
			tmp = tmp.next;
		}
		return null;
	}

	/**
	 * searches for any available server
	 * @return -1 if can't find any available server
	 */
	public int searchAvailableServer(){
		serverList tmp = first;
		while(tmp!=null){
			if(tmp.sts==1)
				return tmp.id;
			tmp = tmp.next;
		}
		return -1;
	}

	/**
	 * sets the status of server between 0 and 1
	 * @param id for finding server
	 * @param sts the value that will setted for status
	 */
	public static void setSts(int id, int sts){
		serverList tmp = first;
		while(tmp!=null){
			if(tmp.id==id) {
				tmp.sts = sts;
				break;
			}
			tmp = tmp.next;
		}
	}

	/**
	 * sets the queue for defined server
	 * @param id for finding server
	 * @param job the queue list of server
	 */
	public void setJob(int id, int[] job){
		serverList tmp = first;
		while(tmp!=null){
			if(tmp.id==id){
				tmp.job = job;
				break;
			}
			tmp = tmp.next;
		}
	}

	/**
	 * ones server takes and finishes it queue this removes every data in it
	 * @param id for finding server
	 */
	public static void releaseJobQueue(int id){
		serverList tmp = first;
		while(tmp!=null){
			if(tmp.id==id) {
				tmp.job = new int[1];
				break;
			}
			tmp = tmp.next;
		}
	}

	/**
	 * for initializing the servers
	 * @param id for definer
	 * @param sts for initial status
	 */
	public void setServer(int id, int sts){
		serverList dn = new serverList(id, sts);
		if(first==null){
			first=dn;
			last=dn;
		}else{
			last.next=dn;
			last=dn;
		}
		size++;
	}
}