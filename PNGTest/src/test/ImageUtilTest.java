package test;

import main.ImageUtil;
import game.util.FileUtil;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class ImageUtilTest extends TestCase {
	public static String WORK_PATH = System.getProperty("user.dir");
	
	public ImageUtilTest(String method){
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

	public void test_changePngPal() throws Exception {
		byte[] pngByte = FileUtil.readByteDataFromFile(WORK_PATH + "/sample/9.png");
		byte[] palByte = FileUtil.readByteDataFromFile(WORK_PATH + "/sample/9_1.pl");
		
		byte[] newPng = ImageUtil.changePngPal(pngByte, palByte);
		
		FileUtil.writeByteDataToFile(WORK_PATH + "/temp/new.png", newPng);
	}
	
	public static void main(String[] args) {
    	TestSuite suite = new TestSuite();
    	Class cls = ImageUtilTest.class;
    	boolean runAll = false;

    	if(runAll){
    		TestRunner runner = new TestRunner();
    		TestRunner.run(runner.getTest(cls.getName()));
    		return;
    	}
        
        // Selective Run for the test 
    	suite.addTest(new ImageUtilTest("test_changePngPal"));
    	// suite.addTest(new ImageUtilTest("test_assert1"));
    	// suite.addTest(new ImageUtilTest("test_assert2"));
    	
    	TestRunner.run(suite);
	}
}
