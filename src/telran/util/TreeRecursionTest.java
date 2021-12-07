package telran.util;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class TreeRecursionTest {
	TreeSet<Integer> tree;

	@BeforeEach
	void setUp() throws Exception {
		tree = new TreeSet<>();
		tree.add(30);
		tree.add(40);
		tree.add(35);
		tree.add(80);
		tree.add(60);
		tree.add(100);
		tree.add(90);
		tree.add(15);
		
	}

	@Test
	void widthTest() {
		assertEquals(4, tree.width());
	}
	@Test
	void heightTest() {
		assertEquals(5, tree.height());
	}
	
	@Test
	void displayTreeTest() throws Exception {
		System.out.println("_______");
		tree.displayTree();
		System.out.println("_______");

	}
	
	@Test
	void maxBranchSumTest()   {
		TreeSet<Integer> tree1 = getTreeForTest();
		System.out.println("The tree from slide 38 looks like ");
		tree1.displayTree();
		assertEquals(36, tree1.sumOfMaxBranch());
		
	
	}
	
	
	private TreeSet<Integer> getTreeForTest() {
		int []	arr = {4, 11, 6, 5, 7, 21};
		TreeSet<Integer> treeForTest = new TreeSet<>((a,b) -> sumOfDigits(a) - sumOfDigits(b));
		for(int i = 0; i< arr.length; i++) {
			treeForTest.add(arr[i]);
		}
		return treeForTest;
	}

	private int sumOfDigits(Integer num) {
	if(num<10) {
		return num;
	}
		return num  == 0 ? 0 : num % 10 + sumOfDigits(num / 10);
	}
	
	@Test
	void printForFileSystem() {
		System.out.println("_______Tree for File System looks Like_____");
		tree.displayTreeFileSystem();
		System.out.println("_______");
	}
	
}
