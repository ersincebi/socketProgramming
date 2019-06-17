class serverList{
	public int id;
	public int sts;
	public int[] job;
	public serverList next;

	/**
	 * takes the required values and sets
	 * @param id for finding server in list
	 * @param sts for seeing the status if busy or not
	 * for busy -> 0
	 * for ready -> 1
	 */
	public serverList(int id, int sts){
		this.id = id;
		this.sts = sts;
		next = null;
	}
}