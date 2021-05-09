package main;

import game.util.StringUtil;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class PngData {
	private final static int HOLD_LENGTH = 8;
	
	public byte[] data = null;
	
	public int firstPalettePos = -1;
	public int firstPaletteLen = -1;
	public int firstImgData = -1;
	
	public PngData(byte[] _data){
		data = _data;
		// data = new byte[_data.length];
		// System.arraycopy(_data, 0, data, 0, _data.length);
		
		init();
	}

	public void init(){ 
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
		int[] posLen;
		try {
			posLen = findPalettePosAndLen(dis);
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		firstPalettePos = posLen[0];
		firstPaletteLen = posLen[1];
	}


	public int[] findPalettePosAndLen(DataInputStream dis) throws IOException {
		// PNG Header
		dis.skipBytes(HOLD_LENGTH);
		int pos = HOLD_LENGTH;
		int len = -1;
		
		for (;;) {
			// information: http://www.3-t.com/pub/png/spec/1.1/PNG-Structure.html
			// 
			// CHUNK
            // CHUNK HEADER 
			// Length 		4-byte 
			// ChunkType	4-byte
			// ChunkData	Depend Chunk Type (can be zero)
			// CRC			4-byte
			int headerLen = dis.readInt();	// No include this len,chunkType,crc
			
			byte[] chunkType = new byte[4];
			dis.read(chunkType);
			
			// pos 
			pos += 8;	// int (4-len) chunkType (4-len)
			
			String str = new String(chunkType);
			// System.out.println("chunkType=" + str + " headerLen=" + headerLen);
			if("PLTE".equals(str)){
				len = headerLen;		// Save the palette Len
				break;
			}
			
			if("IEND".equals(str)){
				break;
			}
			// skip len = headerLen-4
			// ��������chunk data
			dis.skipBytes(headerLen);
			pos += headerLen;
			
			byte[] crcByte = new byte[4];
			dis.read(crcByte);
			pos += 4;
		}
		
		return new int[]{pos, len};
	}
	
	
	
	public String infoPalette(){
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));
		
		try {
			return infoPaletteByDis(dis);
		} catch (IOException e) {
			return "EXCEPTION " + e;
		}
	}
	
	public String infoPaletteByDis(DataInputStream dis) throws IOException {
		StringBuffer sb = new StringBuffer();
		
		// PNG Header
		dis.skipBytes(HOLD_LENGTH);
		int pos = HOLD_LENGTH;
		for (;;) {
			// information: http://www.3-t.com/pub/png/spec/1.1/PNG-Structure.html
			// 
            // CHUNK HEADER
			// Length 		4-byte 
			// ChunkType	4-byte
			// ChunkData	Depend Chunk Type (can be zero)
			// CRC			4-byte
			int headerLen = dis.readInt();	// No include this len,chunkType,crc
			byte[] chunkType = new byte[4];
			dis.read(chunkType);
			
			pos += 8;	// int (4-len) chunkType (4-len) 
			
			String str = new String(chunkType);
			System.out.println("chunkType=" + str + " headerLen=" + headerLen);
			sb.append(str + " len=" + headerLen);
			sb.append(" pos=" + pos);
			if("IEND".equals(str)){
				break;
			}
			// skip len = headerLen-4
			dis.skipBytes(headerLen);
			pos += headerLen;
			
			byte[] crcByte = new byte[4];
			dis.read(crcByte);
			pos += 4;
			
			
			sb.append(" crc=" + StringUtil.byteToHex(crcByte));
			sb.append("\n");
        }
		sb.append("\n");

		
		return sb.toString();
	}
	

    public void applyPalette(byte[] plColorData){
    	if(firstPalettePos < 0){
    		System.out.println("applyPalette: palettePos not ready");
    		return;
    	}
    	
    	if(firstPaletteLen < 0){
    		System.out.println("applyPalette: paletteLen not ready");
    		return;
    	}
    	
    	if(plColorData == null){
    		System.out.println("applyPalette: plColorData is null");
    		return;
    	}
    	System.out.println("DEBUG: plLen=" + plColorData.length
    						+ " firstPalLen=" + firstPaletteLen);
    	
    	// int minLen = (plColorData.length < firstPaletteLen) ? 
    	// 					plColorData.length : firstPaletteLen;
    	
    	System.arraycopy(plColorData, 0, data, firstPalettePos, plColorData.length);
    	
    	// imgConvert(data);
    	
//    	try{
//
//    		int startIndex = m_firstPLTE+8;
//    		
//    		//int length = Util.getInt(dump, m_firstPLTE) + 4;
//    		int length = getInt(dump, m_firstPLTE);
//    		
//    		
//    		//System.out.println("length[BaseImage]: "+(length+4));
//    		
//    		
//    		int counter = 0;
//    		for(int i=0;i<plColorData.length;i++){
//    			
//    			for(int color = 0;color<3;color++){ //r,g,b
//    				dump[startIndex+counter] = plColorData[i][color];
//    				counter ++;
//    			}
//    		}
//    		
//    	}catch(Exception ex){
//    	}
    }
	

	private static void imgConvert(byte content[]) {

		try {
			int start = 0;
			int newcolor = -1;
			for (int idx = 0; idx < content.length - 3; idx++) // PLTE��ʶ
			{
				if (content[idx] == 0x50 && content[idx + 1] == 0x4c
						&& content[idx + 2] == 0x54 && content[idx + 3] == 0x45) {
					start = idx;
					break;
				}
			}
			for (int idx = 0; idx < 4; idx++){
				newcolor = pixelConvert(content[start + idx], newcolor);
			}

			int r, g, b, length;

			length = (content[start - 4] & 0xff) << 24
					| (content[start - 3] & 0xff) << 16 | (content[start - 2])
					& 0xff << 8 | (content[start - 1] & 0xff);

			for (int i = 0; i < length; i += 3) {
				// �µ���ɫ
				newcolor = pixelConvert(content[start + 4 + i], newcolor);
				newcolor = pixelConvert(content[start + 4 + i + 1], newcolor);
				newcolor = pixelConvert(content[start + 4 + i + 2], newcolor);
			}

			newcolor = ~newcolor;
			content[start + 4 + length] = (byte) (newcolor >> 24);
			content[start + 4 + length + 1] = (byte) (newcolor >> 16);
			content[start + 4 + length + 2] = (byte) (newcolor >> 8);
			content[start + 4 + length + 3] = (byte) (newcolor);
		} catch (Exception e) {
		}
	}

	private static int pixelConvert(byte pixel, int color) {

		int tmp = pixel & 0xff;
		color ^= tmp;
		for (int idx = 0; idx < 8; idx++){
			if ((color & 1) != 0) // ��λΪ1
				color = (color >>> 1) ^ 0xedb88320;
			else
				color >>>= 1;
		}
		
		return color;
	} 
    

}
