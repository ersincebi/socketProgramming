import java.net.*;
import java.math.BigInteger;
import java.util.Arrays;
class loadBalancer implements Runnable{
	/**
	 * defining how many servers will run
	 */
	private int hms = 3;
	/**
	 * semaphore value
	 * for stop -> 0
	 * for signal -> 1
	 */
	private static int trigger=1;
	/**
	 * it determines the every servers loadLimit
	 */
	private int loadLimit = 30;
	/**
	 * buffer queue is for buffering the values that cant be send to server at that time
	 */
	public int[] buffer = new int[1];
	/**
	 * job queue is for determining buffered values before sending, according to loadLimit
	 */
	private int[] job = new int[1];
	private manageServer ms = new manageServer();
	DatagramSocket ss;
	private Thread[] servThread = new Thread[hms];
	public static void main(String[] args){
		loadBalancer lb = new loadBalancer();
		lb.ms.__init(lb.hms);
		lb.setAll();
		lb.serverSet();
		Thread mn = new Thread(lb);
		/**
		 * for balancing buffered data every half second, as paralel to the buffering function
		 */
		Thread mbl = new Thread(()->{
			while(true){
				try{
					Thread.sleep(500);
					lb.balance();
				}catch(Exception e){}
			}
		});
		mn.start();
		mbl.start();
	}

	/**
	 * buffers the values that received as paralel to balance function
	 * catches the sended data and buffers it
	 * ones data received calls start()
	 * taking byte array as array againg and turn it into BigInteger and then take its integer value to buffer
	 */
	public void run(){
		try{
			DatagramPacket rp;
			ss = new DatagramSocket(9999);
			byte[] rd = new byte[1024];
			while(true){
				rp = new DatagramPacket(rd, rd.length);
				ss.receive(rp);
				byte[] data = rp.getData();
				int length = rp.getLength();
				byte[] data2 = new byte[length];
				System.arraycopy(data, 0, data2, 0, length);
				BigInteger bigInteger = new BigInteger(data2);
				int intVal = bigInteger.intValue();
				toBuffer(intVal);
				start();
			}
		}catch(Exception e){}
	}

	/**
	 * balances if semaphore is setted ready and if there is available server
	 */
	public void balance(){
		if(get()==0) return;
		int servId = ms.searchAvailableServer();
		if(servId==-1){
			System.out.println("All servers are busy, data(s) will be buffered!");
			stop();
		}else{
			ms.setJob(servId, bufferToJob());
			job = new int[1];
		}
	}

	/**
	 * buffers the sended data
	 * @param pkt packet to insert into buffer
	 */
	public void toBuffer(int pkt){
		int[] arr = makeArr(pkt);
		for(int item:arr) if(item>0) buffer = insert(buffer,item);
	}

	/**
	 * adds the data last element of array
	 * @param arr array to inserted in
	 * @param pkt the value to insert into queue
	 * @return
	 */
	public int[] insert(int[] arr, int pkt){
		if(isArrayFull(arr)) arr = resize(arr);
		arr[unFilledIndex(arr)] = pkt;
		return arr;
	}

	/**
	 * takes data and split into pieces, according to loadLimit
	 * @param pkt the packet to split into pieces
	 * @return an array of splitted data
	 */
	public int[] makeArr(int pkt){
		int[] arr = new int[2];
		while(true){
			if(pkt<=0) break;
			if(pkt>=loadLimit) arr = insert(arr,loadLimit);
			else arr = insert(arr,pkt);
			pkt -= loadLimit;
		}
		return arr;
	}

	/**
	 * checks if array is loaded or not
	 * @param arr array to check
	 * @return boolean
	 * if loaded -> true
	 * if not loaded -> false
	 */
	public boolean isArrayFull(int[] arr){
		return (unFilledIndex(arr) + 1) == (arr.length);
	}

	/**
	 * finds the last empty index of array
	 * @param arr array to check
	 * @return index of required array
	 */
	public int unFilledIndex(int[] arr){
		if(arr.length!=1) for(int i = 0; i < arr.length; i++) if(arr[i]==0) return i;
		return 0;
	}

	/**
	 * resizes array to double its size
	 * @param arr array to resize
	 * @return an array of same array as double
	 */
	public int[] resize(int[] arr){
		int m = arr.length;
		int[] tmp = new int[m*2];
		System.arraycopy(arr,0,tmp, 0,arr.length);
		return tmp;
	}
	/**
	 * fils job array as much as loadLimit
	 * @return filled array
	 */
	public int[] bufferToJob(){
		for(int item : buffer){
			if(isoverLimit(job,item)||item==0) break;
			job = insert(job, item);
			buffer = moveArray(buffer);
		}
		return job;
	}

	/**
	 * this function simply makes prediction
	 * if job queue reaches its limits with next item in buffer it tells
	 * @param arr the queue to check
	 * @param nextItem the next item in buffer
	 * @return if job queue to be send is over limit returns true or else false
	 */
	public boolean isoverLimit(int[] arr, int nextItem){
		int sum = 0;
		for(int item:arr) sum+=item;
		return (sum+nextItem)>loadLimit;
	}

	/**
	 * deletes first element of required array
	 * @param arr required array
	 * @return same array as moved
	 */
	public int[] moveArray(int[] arr){
		return Arrays.copyOfRange(arr, 1, arr.length);
	}

	/**
	 * initializes threads for required server
	 */
	public void serverSet(){
		for (int i = 0; i < hms; i++){
			servThread[i] = new Thread(new server(i));
			servThread[i].start();
		}
	}

	/**
	 * initializes all servers as hms value setted
	 */
	public void setAll(){ for(int i = 0; i<hms; i++) servThread[i] = new Thread(new server(i)); }

	/**
	 * sets semaphore to 0 for stop
	 */
	public static void stop(){ trigger = 0; }

	/**
	 * sets semaphore to 1 for signal
	 */
	public static void start(){ trigger = 1; }

	/**
	 * @return value of semaphore as required
	 */
	public static int get(){ return trigger; }

	/**
	 * checks for next data in buffer
	 * if it is 0
	 * closes the socket
	 * @param bufferVal next value that will be check in buffer queue
	 */
	public void closeSocket(int bufferVal){
		if(bufferVal==0) {
			System.out.println("All servers are shutting down!");
			ss.close();
			System.exit(0);
		}else{
			return;
		}
	}
}