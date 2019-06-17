import java.util.Arrays;
class loadBalancerTest{
	private loadBalancer lb;
	public static void main(String[] args) {
		loadBalancerTest lbt = new loadBalancerTest();
		lbt.setUp();
		lbt.toBuffer_test_expectTrue();
		lbt.insert_test_expectTrue();
		lbt.makeArr_test_expectTrue();
		lbt.isArrayFull_test_expectTrue();
		lbt.unFilledIndex_test_expectOne();
		lbt.resize_test_tobeEqual();
		lbt.bufferToJob_test_expectTrue();
		lbt.isoverLimit_test_expectTrue();
		lbt.moveArray_test_tobeEqual();
		lbt.tearDown();
	}

	public void setUp(){ lb = new loadBalancer(); }

	public void toBuffer_test_expectTrue(){
		final int[] expected = {30,0};
		lb.toBuffer(30);
		final int[] actual = lb.buffer;
		if(Arrays.equals(actual, expected)) System.out.println(true);
		else System.out.println(false);
	}

	public void insert_test_expectTrue(){
		final int[] expected = {30,0,0};
		final int[] arr = {0,0,0};
		final int[] actual = lb.insert(arr,30);
		if(Arrays.equals(actual, expected)) System.out.println(true);
		else System.out.println(false);
	}

	public void makeArr_test_expectTrue(){
		final int[] expected = {30,30,20,0};
		final int[] actual = lb.makeArr(80);
		if(Arrays.equals(actual, expected)) System.out.println(true);
		else System.out.println(false);
	}

	public void isArrayFull_test_expectTrue(){
		final int[] arr = {1,0};
		final boolean actual = lb.isArrayFull(arr);
		if(actual) System.out.println(true);
		else System.out.println(false);
	}

	public void unFilledIndex_test_expectOne(){
		final int expected = 1;
		final int[] arr = {1,0};
		final int actual = lb.unFilledIndex(arr);
		if(actual == expected) System.out.println(true);
		else System.out.println(false);
	}

	public void resize_test_tobeEqual(){
		final int[] expected = {1,0,0,0};
		final int[] arr = {1,0};
		final int[] actual = lb.resize(arr);
		if(Arrays.equals(actual, expected)) System.out.println(true);
		else System.out.println(false);
	}

	public void bufferToJob_test_expectTrue(){
		lb.buffer = new int[]{30, 30, 30, 30};
		final int[] expected = {30,0};
		final int[] actual = lb.bufferToJob();
		if(Arrays.equals(actual, expected)) System.out.println(true);
		else System.out.println(false);
	}

	public void isoverLimit_test_expectTrue(){
		final int[] arr = {30,0};
		final int nextItem = 10;
		final boolean actual = lb.isoverLimit(arr,nextItem);
		if(actual) System.out.println(true);
		else System.out.println(false);
	}

	public void moveArray_test_tobeEqual(){
		final int[] expected = {0};
		final int[] arr = {1,0};
		final int[] actual = lb.moveArray(arr);
		if(Arrays.equals(actual, expected)) System.out.println(true);
		else System.out.println(false);
	}

	public void tearDown(){ lb = null; }
}