package main;

public class ImageUtil {
	/**
	 * 更改png palette并输出
	 */
	public static byte[] changePngPal(byte[] pngByte, byte[] palByte){
		// TODO: Error check
		
		byte[] newPng = new byte[pngByte.length];
		System.arraycopy(pngByte, 0, newPng, 0, pngByte.length);
		
		
		return newPng;
	}
	
	
    // Set integer
    public static void setInt(int val, byte [] arr, int idx){
        arr[idx]   = (byte)((val >>> 24) & 0xff);
        arr[idx+1] = (byte)((val >>> 16) & 0xff);
        arr[idx+2] = (byte)((val >>> 8)  & 0xff);
        arr[idx+3] = (byte)((val >>> 0)  & 0xff);
    }

    // Get integer
    public static int getInt(byte [] arr, int idx){
        int v = arr[idx];
        v = (v << 8) | ((int)arr[idx+1]) & 0x000000FF;
        v = (v << 8) | ((int)arr[idx+2]) & 0x000000FF;
        v = (v << 8) | ((int)arr[idx+3]) & 0x000000FF;
        return v;
    }
}
