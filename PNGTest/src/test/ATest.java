package test;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class ATest extends TestCase {
	public static String WORK_PATH = System.getProperty("user.dir");
	
	public ATest(String method){
		super(method);
		// Common.initLog4J(WORK_PATH);
	}
	
	public void test_assert1(){
		System.out.println("Testing Assert 1");
		assertTrue("show if the input is not true", true);		
	}
	
	public void test_assert2(){
		System.out.println("Testing Assert 2");
		assertTrue("show if the input is not true", true);		
	}

	
	
	public static void main(String[] args) {
    	TestSuite suite = new TestSuite();
    	Class cls = ATest.class;
    	boolean runAll = false;

    	if(runAll){
    		TestRunner runner = new TestRunner();
    		TestRunner.run(runner.getTest(cls.getName()));
    		return;
    	}
        
        // Selective Run for the test 
    	// suite.addTest(new ATest("test_assert1"));
    	// suite.addTest(new ATest("test_assert2"));
    	
    	TestRunner.run(suite);
	}
}
