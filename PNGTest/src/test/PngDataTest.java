package test;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;

import game.util.FileUtil;
import main.ImageUtil;
import main.PngData;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class PngDataTest extends TestCase {
	public static String WORK_PATH = System.getProperty("user.dir");
	
	public PngDataTest(String method){
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

	public void test_findPalettePos() throws Exception {
		PngData png = new PngData(null);
		
		byte[] pngByte = FileUtil.readByteDataFromFile(WORK_PATH + "/sample/9.png");
		
		
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(pngByte));
		
		int[] posLen = png.findPalettePosAndLen(dis);
		System.out.println("pos=" + posLen[0] + " len=" + posLen[1]);
	}
	

	public void test_infoPalette() throws Exception {
		byte[] pngByte = FileUtil.readByteDataFromFile(WORK_PATH + "/sample/9.png");
		PngData png = new PngData(pngByte);
		
		System.out.println(png.infoPalette());
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void test_applyPalette() throws Exception {
		byte[] pngByte = FileUtil.readByteDataFromFile(WORK_PATH + "/sample/9.png");
		PngData png = new PngData(pngByte);
		
		//byte[] palByte = FileUtil.readByteDataFromFile(WORK_PATH + "/sample/9_2.pl");
		byte[] palByte = FileUtil.readByteDataFromFile(WORK_PATH + "/sample/9_3.pl");
		System.out.println("DEBUG: " + new String(palByte));
		png.applyPalette(palByte);
		System.out.println(png.infoPalette());
		FileUtil.writeByteDataToFile(WORK_PATH + "/temp/new.png", png.data);
	}
	
	public static void main(String[] args) {
    	TestSuite suite = new TestSuite();
    	Class cls = PngDataTest.class;
    	boolean runAll = false;

    	if(runAll){
    		TestRunner runner = new TestRunner();
    		TestRunner.run(runner.getTest(cls.getName()));
    		return;
    	}
        
        // Selective Run for the test 
    	suite.addTest(new PngDataTest("test_applyPalette"));
    	// suite.addTest(new PngDataTest("test_infoPalette"));
    	// suite.addTest(new PngDataTest("test_findPalettePos"));
    	// suite.addTest(new PngDataTest("test_assert1"));
    	// suite.addTest(new PngDataTest("test_assert2"));
    	
    	TestRunner.run(suite);
	}
}
