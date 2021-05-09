/**
 *
 *
 *
 */
package game.util;


import java.io.*;
import java.net.URLEncoder;
import java.text.*;
import java.util.*;


public class StringUtil {

	
	private static final byte[] UTF_BYTE_EMPTY = {0x00, 0x00};
	private static final byte[] UTF_BYTE_BUG= {0x00, 0x03, 0x42, 0x55, 0x47};

	/**
	 * Returns a specified value while the original value is a 'null' value.
	 * And return the original value if the value is not 'null'.  
	 * 
	 * @param originalValue The original value. 
	 * @param defaultValue The value to be assigned to the null value.
	 * @param isNullString indicate where the original value will be 
	 *                     a string defined as "null" or not.
	 * @return
	 */
	public static String nullValue(String originalValue, String defaultValue,
									boolean isNullString){		
		if(originalValue == null){
			 return defaultValue;
		}
		
		if(isNullString == true && originalValue.equalsIgnoreCase("null")){
			return defaultValue;
		}
		
		return originalValue;
	} 	

	public static String joinVector(Vector list, String sep){
		StringBuffer sb = new StringBuffer();
		
		for(int i=0; i<list.size(); i++){
			Object obj = list.elementAt(i);
			if(sb.length() != 0){
				sb.append(sep);
			}
			if(obj == null){
				sb.append("null");
				continue;
			}
		
			sb.append(obj.toString());
		}
		
		return sb.toString();
	}
	
	public static String joinByteList(List<byte[]> list, String sep){
		StringBuffer sb = new StringBuffer();
		
		for(int i=0; i<list.size(); i++){
			byte[] obj = list.get(i);
			if(obj == null){
				sb.append("(null)");
			}else{
				sb.append(byteToHex(obj));
			}
			sb.append(sep);
		}
		
		return sb.toString();
	}
	
	public static String join(List list, String sep){
		StringBuffer sb = new StringBuffer();
		
		for(int i=0; i<list.size(); i++){
			Object obj = list.get(i);
			if(sb.length() != 0){
				sb.append(sep);
			}
			if(obj == null){
				sb.append("(null)");
			}else{
				sb.append(obj.toString());
			}
		}
		
		return sb.toString();
	}
	
	public static String joinInt(List list, String sep){
		String retval = "";
		
		for(int i=0; i<list.size(); i++){
			Object obj = list.get(i);
			
			if(obj instanceof Integer  == false){
				continue; 
			}
			Integer iobj = (Integer) obj;
			
			if(retval.equals("")){
				retval = "" + iobj.intValue();
			}else{
				retval += sep + iobj.intValue();
			}
		}
		
		return retval;
	}
	
	public static String join(String[] list, String sep, String strForNull){
		if(list == null){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<list.length; i++){			
			if(sb.length() != 0){
				sb.append(sep);
			}
			
			if(list[i] == null){
				sb.append(strForNull);
			}else{
				sb.append(list[i]);
			}
		}
		
		return sb.toString();
	}	
	
	public static String join(String[] list, String sep){
		if(list == null){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<list.length; i++){			
			if(sb.length() == 0){
				sb.append(list[i]);
			}else{
				sb.append(sep);
				sb.append(list[i]);
			}
		}
		
		return sb.toString();
	}	
							
	public static String join(String[] arr, String sep, 
							String beforeStr, String afterStr){
		String retval = "";
		
		if(arr == null){
			return "";
		}

		for(int i=0; i<arr.length ; i++){
			
			if(retval.equals("")){
				retval = beforeStr + arr[i] + afterStr;
			}else{
				retval += sep + beforeStr + arr[i] + afterStr;
			}
		}
		
		return retval;
	}
	

    public static String[] split(String instr, String sep){
        return split(instr, sep, -1);
    }

    // TODO implement the handling for maxToken
	public static String[] split(String instr, String sep, int maxToken){
		ArrayList list;
		
		list = new ArrayList();
		
		// For xxxx sep xxxx sep xxx sep xxxxx
		//          |        |       |
		//          1st idx  2nd idx 3rd idx ......
		
		int start = 0;
		int seplen = sep.length();
		if(seplen == 0){	// Calling BUG!!
			// Invalid !!
			return new String[]{instr};
		}
		
		while(true){
			int idx = instr.indexOf(sep, start);
			
			if(idx < 0){
				break;
			}			            
			
			String str = instr.substring(start, idx);
			
			list.add(str);
			
			start = idx+seplen;
		}
		
		if(start < instr.length()){
			String str = instr.substring(start);
			
			list.add(str);			
		}
		
		String arr[] = new String[list.size()];
		for(int i=0; i<list.size(); i++){
			arr[i] = (String) list.get(i);
		}
		
		return arr;
	}		
    
    public static int[] indexOf(String instr, String[] str, int start){
        int idx = -1;        
        int match = -1;
        for(int i=0; i<str.length; i++){
            int myidx = instr.indexOf(str[i], start);
            // System.out.println("DEBUG: " + instr + " " + str[i] + "[" + myidx + "]");
            if(myidx >= 0 && (myidx < idx || idx == -1)){
                match = i;
                idx = myidx;
            }
        }
        
        return new int[]{match, idx};
    }
	
    public static String[] split(String instr, String[] sep){
        return split(instr, sep, -1, false);
    }
    
    public static String[] split(String instr, String[] sep, int maxToken){
    	return split(instr, sep, maxToken, false);
    }
    
    public static String[] split(String instr, String[] sep, boolean removeEmpty){
        return split(instr, sep, -1, removeEmpty);
    }
    
    
    public static String[] split(String instr, 
    					String[] sep, int maxToken, boolean removeEmpty){
        ArrayList list;
        
        list = new ArrayList();
        
        // For xxxx sep xxxx sep xxx sep xxxxx
        //          |        |       |
        //          1st idx  2nd idx 3rd idx ......
        
        int start = 0;
        
        while(true){
            int[] ret = indexOf(instr, sep, start);
            int match = ret[0];
            int idx = ret[1]; 
            
            if(idx < 0){
                break;
            }
            
            
            String str = instr.substring(start, idx);
            
            if(removeEmpty){
            	// Only add if the string is not null
            	str = str.trim();    
            	if(str.equals("") == false){
            		list.add(str);
            	}
            }else{
            	list.add(str);
            }
            
            start = idx+sep[match].length();
            
            if(maxToken > 0 && list.size() >= maxToken-1){
                break;
            }
        }
        
        if(start < instr.length()){
            String str = instr.substring(start);
            if(removeEmpty){
            	// Only add if the string is not null
            	str = str.trim();    
            	if(str.equals("") == false){
            		list.add(str);
            	}
            }else{
            	list.add(str);
            }
        }
        
        String arr[] = new String[list.size()];
        for(int i=0; i<list.size(); i++){
            arr[i] = (String) list.get(i);
        }
        
        return arr;
    }       

    public static String replace(String instr, String findStr, String replaceStr){
		// Algoithm : split using findStr and join them using replaceStr
		// E.g Replace all "x" with "--" in String "x0x2x3x"
		// After Split:  0 2 3 
		// When joining 
		//  1. Check instr.startsWith(findStr) : YES=> append replaceStr
		//  2. loop the array of splitted string
		//  3. Check instr.endsWith(findStr) : YES=> append replaceStr
	
		String[] list = split(instr, findStr);
		
		String outstr = "";
		
		if(instr.startsWith(findStr)){
			outstr += replaceStr;
		}
		
		for(int i=0; i<list.length; i++){
			if(i != 0){
				outstr += replaceStr;
			}
			
			outstr += list[i];
		}
		
		if(instr.endsWith(findStr)){
			outstr += replaceStr;
		}		
		
		return outstr;
	}

	public static String leftPad(int inval, char chr, int maxlen){	
		return pad(String.valueOf(inval), chr, maxlen, 2);
	}

	public static String leftPad(String instr, char chr, int maxlen){	
		return pad(instr, chr, maxlen, 2);
	}

	public static String rightPad(int inval, char chr, int maxlen){	
		return pad(String.valueOf(inval), chr, maxlen, 1);		
	}
	
	public static String rightPad(String instr, char chr, int maxlen){	
		return pad(instr, chr, maxlen, 1);		
	}
	
	public static String pad(String instr, char chr, 
								int maxlen, int direction){
		if(instr == null){
			instr = "";
		}
		
		int strlen = instr.length();
		
		if(strlen > maxlen){	// No room to do any padding
			return instr;
		}		

		// Left Padding 
		// 1. Find num of char to pad 				
		// 2. Loop the 
		int padsize = maxlen - strlen;
		
		String padstr = repeat(chr, padsize);
				
		if(direction == 1){		// 1 = Right
			return instr + padstr;
		}else{						// Else = Left 
			return padstr + instr;
		}
	}
	
	public static String repeat(char c, int times){
		StringBuffer buf = new StringBuffer();
		
		for(int i=0; i<times; i++){
			buf.append(c);
		}
		
		return buf.toString();		
	}
	
	public static String repeat(String str, int times){
		StringBuffer buf = new StringBuffer();
		
		for(int i=0; i<times; i++){
			buf.append(str);
		}
		
		return buf.toString();		
	}
	
	public static String[] sortStringArray(String[] arr){
		
		if(arr == null || arr.length == 0){
			return new String[0];
		}
		
		// Create the sorted set
	    SortedSet set = new TreeSet();
    
    	// Add elements to the set
    	for(int i=0; i<arr.length; i++){
    		set.add(arr[i]);
    	}
    	
	
	    // Create an array containing the elements in a set (in this case a String array).
	    // The elements in the array are in order.
		return (String[])set.toArray(new String[set.size()]);
	}
	
	public static String[] insertStringToList(String[] list, String str){
		if(list == null){
			return null;
		}
		
		if(str == null){
			return list;
		}
		
		String[] newlist = new String[list.length + 1];
		
		newlist[0] = str;
		
		for(int i=1; i<=list.length; i++){
			newlist[i] = list[i-1];			
		}
		
		return newlist;
	}
	
	public static boolean isLetterDigit(String str){
		if(str.length() == 0){
			return true; 	// kwok: should be true or false?
		}
		return str.matches("^[A-Za-z0-9]+$");
	}
	
	
	
	public static Hashtable stringArrayToHash(String[] data, String[] cols){
		Hashtable h = new Hashtable();
		
		
		if(data == null || cols == null){
			return h;
		}
		
		int dataLen = data.length;
		int colLen = cols.length;
		
		for(int i=0; i<colLen; i++){
			String dataVal = "";
			String col = cols[i];
			if(i >= 0 && i <dataLen){
				dataVal = data[i];
			}
			h.put(col, dataVal);
		}
		
		return h;
	}	
	
	/**
	 * Append one hash table to another
	 * 
	 * 
	 * @param mainHash
	 * @param addHash
	 */
	public static void appendHash(Hashtable mainHash, Hashtable addHash){
		if(mainHash == null || addHash == null){
			return;
		}
		
		for (Enumeration e = addHash.keys(); e.hasMoreElements();) {
			String name = (String) e.nextElement();
			String data = (String) addHash.get(name);
						
			mainHash.put(name, data);			
		}		
	}
	
	public static String toAsciiUnicodeString(String str){
		if(str == null){
			return "";
		}
		
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<str.length(); i++){
			char a = str.charAt(i);
				
			if (a < 0x80) {	// Normal ascii
				if(a == '&'){
					sb.append("&amp;");
				}else{
					sb.append(a);
				}
			}else{
				sb.append("&#x");
				sb.append(Integer.toHexString(a));
				sb.append(";");			
			}
		}			
		return sb.toString(); 
	}
	

	/**
	 * @param parameter
	 * @param i
	 * @return
	 */
	public static long longValue(String str, long defaultVal) {
		if(str == null){
			return defaultVal;
		}
		
		try{
			return Long.parseLong(str);
		}catch(NumberFormatException e){
			return defaultVal;
		}
	}
	
	public static short shortValue(String str, short defaultVal){
		if(str == null){
			return defaultVal;
		}
		
		try{
			return Short.parseShort(str);
		}catch(NumberFormatException e){
			return defaultVal;
		}
	}
	
	public static byte byteValue(String str, byte defaultVal){
		if(str == null){
			return defaultVal;
		}
		
		try{
			return Byte.parseByte(str);
		}catch(NumberFormatException e){
			return defaultVal;
		}
	}

	
	public static int intValue(String str, int defaultVal){
		if(str == null){
			return defaultVal;
		}
		
		try{
			return Integer.parseInt(str);
		}catch(NumberFormatException e){
			return defaultVal;
		}
	}
	
	public static int intValue16(String str, int defaultVal){
		if(str == null){
			return defaultVal;
		}
		
		try{
			return Integer.parseInt(str, 16);
		}catch(NumberFormatException e){
			return defaultVal;
		}
	}
	
	public static float floatValue(String str, float defaultVal){
		if(str == null){
			return defaultVal;
		}
		
		try{
			return Float.parseFloat(str);
		}catch(NumberFormatException e){
			return defaultVal;
		}
	}
	
	public static double doubleValue(String str, double defaultVal){
		if(str == null){
			return defaultVal;
		}
		
		try{
			return Float.parseFloat(str);
		}catch(NumberFormatException e){
			return defaultVal;
		}
	}
	

	public static String toCodedString(String str){
		return toCodedString(str, true);
	}
	
	public static String toCodedString(String str, boolean conv){
		if(str == null){
			return "";
		}
		
		String specialChar = "&\"\\<>";
		String specialStr[] = {"&amp;", "&quot;", "&apos;", "&lt;", "&gt;"};
		
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<str.length(); i++){
			char a = str.charAt(i);
			
				
			if (a < 0x80) {	// Normal ascii
				if(conv == false){
					sb.append(a);
					continue;
				}
				
				int idx = specialChar.indexOf(a);
				if(idx >= 0 && idx < specialStr.length){
					sb.append(specialStr[idx]);
				}else{
					sb.append(a);
				}
			}else{
				sb.append("&#x");
				sb.append(Integer.toHexString((int) a));
				sb.append(";");			
			}
		}			
		return sb.toString(); 
	}

	public static String getMessage(String resourcesClass, 
							Locale locale, String key) {
		return getMessage(resourcesClass, locale, key, (String) null);
	}
	
	public static String getMessage(String resourcesClass, 
							Locale locale, String key, String defaultVal) {
		String message = "";
		try {
			// Load the resource bundle
			ResourceBundle bundle = ResourceBundle.getBundle(resourcesClass,
					locale);
			// Get the message template
			message = bundle.getString(key);
		} catch (Exception e) {
			message = "#" + key + "#";
			message = (defaultVal == null) ? message : defaultVal;
		}

		
		return (message == null) ? "" : message;		
	}

	public static String getMessage(String resourcesClass, Locale locale,
									String key, String[] args) {
		String message = "";
		try {
			// Load the resource bundle
			ResourceBundle bundle = ResourceBundle.getBundle(resourcesClass,
					locale);

			// Get the message template
			message = bundle.getString(key);
		} catch (Exception e) {
			message = "#" + key + "#";
		}

		return message;
	}
	
	
	public static String getProperty(String resClass, String key) {
		String message = "";
		try {
			// Load the resource bundle
			ResourceBundle bundle = ResourceBundle.getBundle(resClass);
			
			// Get the message template
			message = bundle.getString(key);
		} catch (Exception e) {
			return "";
		}
		
		return message;
	}

	public static int getIntProperty(String resClass, String key, int defValue){		
		try {
			// Load the resource bundle
			ResourceBundle bundle = ResourceBundle.getBundle(resClass);
			
			// Get the message template
			String valStr = bundle.getString(key);
			
			try{
				return Integer.parseInt(valStr);
			}catch(NumberFormatException e){
				return defValue;
			}			
		} catch (Exception e) {
			return defValue;
		}		
	}
	
	public static String toBig5String(String str){
		byte[] bytes = new byte[0];

		try {
			bytes = str.getBytes("8859_1");			
		} catch (UnsupportedEncodingException e) {
			return "";
		}
		
		try {
			return new String(bytes, "8859_1");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			return "";
		}
	}
	
	public static String getCurrentMonthStr(String format){
		format = (format == null) ? "yyyyMM" : format;
		
		SimpleDateFormat f = new SimpleDateFormat(format);

        return f.format(new Date());
	}
	
	
	public static String getDateTimeStr(long time, String format){
		SimpleDateFormat f = new SimpleDateFormat(format);

        return f.format(new Date(time));	
	}
	
	public static String getDateTimeStr(Date date){
		String format = "yyyy-MM-dd HH:mm:ss";
		
		SimpleDateFormat f = new SimpleDateFormat(format);

        return f.format(date);
	}
	
	public static String getDateTimeStr(long time){
		String format = "yyyy-MM-dd HH:mm:ss";
		
		SimpleDateFormat f = new SimpleDateFormat(format);

        return f.format(new Date(time));
	}
	
	public static String getCurrentTime(){
		return getDateTimeStr(System.currentTimeMillis());
	}

	public static byte[] stringToUTFByte(String str, String encoding) {
		try {
			return str.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new byte[0];
		}		
	}

	public static byte[] stringToUTFByte(String str) {
		try {
			return str.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new byte[0];
		}		
	}
	
    public static byte[] stringToUTF(String str) {
        // defence for null pointer
        if (str == null || "".equals(str.trim())){
            return null;
        }
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        byte[] array = null;
        
        try {
            dos.writeUTF(str);
            array = baos.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally{
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        
        return array;
    }
    

    /**
     * convert byte array to hex string, negative safe
     * after 2005-06-15, null safe
     */
    public static String byteToHex(byte pixel[]) {
        if (pixel == null){
            return ""; // defensive
        }
        
        StringBuffer sb = new StringBuffer();
        // print the array
        for (int i = 0; i < pixel.length; i++) {
            int value;
            value = pixel[i];
            
            // this is the key point:
            if (value < 0){
                value += 256;
            }
            
            String str = Integer.toHexString(value).toUpperCase();
            if (str.length() < 2){
                str = "0" + str;
            }

            sb.append(str);
        }
        
        return sb.toString();
    }

    public static String byteToHex(byte pixel[], int maxLen) {
        if (pixel == null){
            return ""; // defensive
        }
        
        StringBuffer sb = new StringBuffer();
        // print the array
        for (int i = 0; i < pixel.length && i < maxLen; i++) {
            int value;
            value = pixel[i];
            
            // this is the key point:
            if (value < 0){
                value += 256;
            }
            
            String str = Integer.toHexString(value).toUpperCase();
            if (str.length() < 2){
                str = "0" + str;
            }

            sb.append(str);
        }
        
        return sb.toString();
    }

    
    public static String byteToHex(byte pixel[], String sep) {
        if (pixel == null){
            return ""; // defensive
        }
        
        String result = "";
        // print the array
        for (int i = 0; i < pixel.length; i++) {
            int value;
            value = pixel[i];
            
            // this is the key point:
            if (value < 0){
                value += 256;
            }
            
            String str = Integer.toHexString(value).toUpperCase();
            if (str.length() < 2){
                str = "0" + str;
            }
             
            if(result.length() > 0){
            	result += sep + str;
            }else{
            	result += str;
            }
        }
        
        return result;
    }

    
    public static String ipIntToStr(int val){ 
	    int seg0 = val & 0x000000ff;
	    int seg1 = (val >> 8) & 0x000000ff;
	    int seg2 = (val >> 16) & 0x000000ff;
	    int seg3 = (val >> 24) & 0x000000ff;
	    
	    return "" + seg3 + "." + seg2 + "." + seg1 + "."+ seg0;
	}

	public static int ipStrToInt(String str){
		String[] token = str.split("\\.");
		
		if(token.length < 4){
			//System.out.println("DEBUG!!! Invalid IP Token: " + str);
			return 0;
		}
		
		int seg0 = intValue(token[0], 0) & 0x000000ff;
		int seg1 = intValue(token[1], 0) & 0x000000ff;
		int seg2 = intValue(token[2], 0) & 0x000000ff;
		int seg3 = intValue(token[3], 0) & 0x000000ff;
		
		int retval = (seg0 << 24) | (seg1 << 16) | (seg2 << 8) | seg3;

		// System.out.println("IP: " + str + " >> " + retval);
		
		return retval;
	}
	

	/**
	 * Convert a UTF to String
	 * 
	 * @param inbytes UTF to be converted
	 * @return a String representing the UTF
	 */
	public static String UTFToString(byte[] inbytes){
		if(inbytes == null)
			return null;
		ByteArrayInputStream bais = new ByteArrayInputStream(inbytes);
		DataInputStream dis = new DataInputStream(bais);
		String retStr = null;

		try{
			retStr = dis.readUTF();
		}catch (Exception ex){}finally{
			try{
				dis.close();
				dis = null;
			}catch (Exception ex){}
			try{
				bais.close();
				bais = null;
			}catch (Exception ex){}
		}
		return retStr;
	}
	
	/**
	 * Converts the input hex string to binary. Each character in return string represents to hexadecimal number in input string.
	 * 
	 * @param src the string to be converted
	 */
	public static byte[] hexToByte(String src){
		if(src == null){
			return new byte[0];
		}
		
		int strLen = src.length();

		if(strLen % 2 == 1){ // Odd Case	
			strLen += 1;
			src += "0";		
		}
		
		int byteSize = strLen/2;
		
		byte[] b = new byte[byteSize];
		
		int offset = 0;
		try{
			for(int i=0; i<byteSize; i++, offset +=2){ 
				b[i] = (byte) Integer.parseInt(src.substring(offset, offset+2), 16);			
			}
		}catch(NumberFormatException e){
			e.printStackTrace();
			// LogUtil.getLogger().error("Invalid Format at offset=" + offset);
		}
			
		return b;
	}
	
	public static String hashToString(HashMap hash){
		return hashToString(hash, "\t", "\n");
	}
	
	public static String hashToString(Map hash, String keySep, String listSep){
		StringBuffer sb = new StringBuffer();
		
		if(hash == null){
			return "null hash";
		}
		
		Iterator it = hash.keySet().iterator();
		
		while(it.hasNext()){
			Object key = it.next();
			sb.append(key);
			sb.append(keySep);
			
			Object obj = hash.get(key);
			if(obj instanceof Integer){
				Integer intObj = (Integer) obj;
				sb.append("" + intObj.intValue());
			}else{
				sb.append(obj.toString());
			}
			sb.append(listSep);
		}
		
		return sb.toString();
	}
	
	public static String hashToString(Hashtable hash){
		StringBuffer sb = new StringBuffer();
		
		if(hash == null){
			return "null hash";
		}
		
		Enumeration en = hash.keys();
		
		while(en.hasMoreElements()){			
			Object key = (Object) en.nextElement();
			if(key instanceof Integer){
				sb.append(((Integer) key).intValue());
			}else{
				sb.append(key);
			}
			sb.append("\n");  // peter: change to linefeed
			
			Object obj = hash.get(key);
			if(obj instanceof Integer){
				Integer intObj = (Integer) obj;
				sb.append("" + intObj.intValue());
			}else{
				sb.append(obj.toString());
			}
			sb.append("\n");
		}
		
		return sb.toString();
	}
	
	public static String utfByteToString(byte[] inbytes) {
		if (inbytes == null)
			return null;
		ByteArrayInputStream bais = new ByteArrayInputStream(inbytes);
		DataInputStream dis = new DataInputStream(bais);
		String retStr = null;

		try {
			retStr = dis.readUTF();
		} catch (Exception ex) {
		} finally {
			try {
				dis.close();
				dis = null;
			} catch (Exception ex) {
			}
			try {
				bais.close();
				bais = null;
			} catch (Exception ex) {
			}
		}
		return retStr;
	}

	
    /**
     * Convert byte data to UTF string
     * similar to din = ByteArrayInputStream(data); return din.readUTF();
     * @param data
     * @return
     */
	public static String byteToString(byte[] data){
		return byteToString(data, "utf-8");
	}
	
	public static String byteToString(byte[] data, String encoding){
		try {
			return new String(data, encoding);
		} catch (UnsupportedEncodingException e) {
			return new String(data);
		}
	}

    /**
     * @param cmd
     * @param string
     * @return
     */
    public static String[] specialSplit(String cmd, String sep, int nTokens){
        ArrayList list = new ArrayList();

        String[] tmp = cmd.split(sep);
        for(int i=0; i<tmp.length; i++){
            String str = tmp[i].trim();
            if(str.length() == 0){
                continue;
            }
            
            list.add(str);
        }
        
        for(int i=tmp.length; i<=nTokens; i++){
            list.add("");
        }
        
        String[] splited = new String[list.size()];
        list.toArray(splited);
        
        return splited; 
    }
	
    
    /**
     * Convert decode the UTF string which are encoding by hexstring 
     * Example: 
     * 64656667 => "ABCD" 
     * 
     * @return decoded UTF string
     */
    public static String decodeUTFHexString(String hexStr){

		// Getting the msg from the data (encoded in UTF) 
		byte[] bytedata = StringUtil.hexToByte(hexStr);
		// String msg = "";
		
		
		// Getting the message 
		// Quick exist if there is not softsync upward (client->server) data??
		DataInputStream din = new DataInputStream(
									new ByteArrayInputStream(bytedata));
		try {
			// XXX consider: str = din.readUTF();  din.close(); return str;
			// 
			return din.readUTF();
		} catch (IOException e) {
			return "";
		}finally{
			try {
				din.close();
			} catch (Exception e1) {}
		}
    }
    
    public static int[] strToIntArray(String str, String sep){
    	if(str == null){
    		return null;
    	}
    	
    	String[] tmp = str.split(sep);
    	int[] tmpArr = new int[tmp.length];
    	
    	int numInt = 0;
    	for(int i=0; i<tmp.length; i++){
    		String s = tmp[i].trim();
    		try{
    			int intVal = Integer.parseInt(s);
    			tmpArr[numInt] = intVal;
    			numInt++;
    		}catch(NumberFormatException e){
    			continue;
    		}
    	}
    	
    	int[] retArr = new int[numInt];
    	for(int i=0; i<numInt; i++){
    		retArr[i] = tmpArr[i];
    	}
    	
    	return retArr;
    }
    
    public static String intArrayToString(int[] arr, String sep){ 
    	if(arr == null){
    		return "";    		
    	}
    	
    	StringBuffer sb = new StringBuffer();
    	for(int i=0; i<arr.length; i++){
    		if(i != 0){
    			sb.append(sep);
    		}
    		sb.append(String.valueOf(arr[i]));			
    	}
    	
    	return sb.toString();
    }
    
    public static String valueArrayToString(byte[] arr, String sep){ 
    	if(arr == null){
    		return "";    		
    	}
    	
    	StringBuffer sb = new StringBuffer();
    	for(int i=0; i<arr.length; i++){
    		if(i != 0){
    			sb.append(sep);
    		}
    		sb.append(String.valueOf(arr[i]));			
    	}
    	
    	return sb.toString();
    }


	/**
	 * @param arg
	 * @param defaultVal default value 
	 * @return
	 */
	public static int intValueFromBinary(String arg, int defaultVal) {
		try{
			return Integer.parseInt(arg, 2);
		}catch(NumberFormatException e){
			return defaultVal;
		}		
	}
	
	/**
	 * check String is composed of Character or not
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isDigit(String str){
		if(str == null){
			return false;
		}
		
		char[] chars = str.toCharArray();
		
		if(chars == null && chars.length <= 0){
			return false;
		}
		
		for(int i = 0; i < chars.length; i ++){
			if(Character.isDigit(chars[i]) == false){
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * The byte data return same as what do in writeUTF
	 * Remark: the encoding must be utf-8
	 * 
	 * @return
	 */
	public static byte[] getUTFByte(String input)
	{
		if(input == null || input.length() == 0)
		{
			return UTF_BYTE_EMPTY;
		}
		
		try {
			
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			DataOutputStream dout = new DataOutputStream(bo);
			
			dout.writeUTF(input);
			
			return bo.toByteArray(); 
		} catch (Exception e) {
			System.err.println("getUTFByte: exception: " + e);
			return UTF_BYTE_BUG;
		}
	}

	/**
	 * The byte data return same as what do in writeUTF
	 * Remark: the encoding must be utf-8
	 * 
	 * @return
	 */
	public static byte[] getUTFByteSlow(String input)
	{
		// The following code is working but slow!!
		try
		{
			byte[] data = input.getBytes("utf-8");
			
			byte[] output = new byte[data.length + 2];
			
			int len = data.length;
			
			System.arraycopy(data, 0, output, 2, len);
			
			output[0] = (byte) ((len >> 8) & 0x000000FF);
			output[1] = (byte) (len & 0x000000FF);
			
			return output;
		}catch(Exception e)
		{
			System.err.println("getUTFByte: exception: " + e);
			return UTF_BYTE_BUG;
		}		
	}
	
	public static byte[] getUTF16Byte(String input)
	{
		if(input == null){
			return null;
		}
		
		// The following code is working but slow!!
		try
		{
			return input.getBytes("utf-16");
		}catch(Exception e)
		{
			return null;
		}		
	}
	
	public static String getUTF16Str(byte[] data)
	{
		// The following code is working but slow!!
		try
		{
			return new String(data, "utf-16");
		}catch(Exception e)
		{
			return null;
		}
	}
	
	public static void main(String[] args){

			String inputStr = "tester1";
			byte[] data = StringUtil.stringToUTFByte(inputStr);
			String inputHexStr = StringUtil.byteToHex(data);
			
			System.out.println(inputStr + " inputHexStr=" + inputHexStr);
					
			//inputHexStr = "E5B08FE78CAA";
			inputHexStr = "74657374657231";
			data = StringUtil.hexToByte(inputHexStr.trim());
			String outputStr = StringUtil.byteToString(data, "utf-8");
			
			System.out.println(outputStr);
		
	}

	public static String join(int[] list, String sep)
	{
		StringBuffer sb = new StringBuffer();
		
		if(list == null){
			return "";
		}
		
		for(int i=0; i<list.length; i++){
			if(i != 0){
				sb.append(sep);
			}
			sb.append(list[i]);
		}
		
		return sb.toString();
	}
	
	public static String join(byte[] list, String sep)
	{
		StringBuffer sb = new StringBuffer();
		
		if(list == null){
			return "";
		}
		
		for(int i=0; i<list.length; i++){
			sb.append(list[i]);
			sb.append(sep);
		}
		
		return sb.toString();
	}


	public static String join(short[] list, String sep)
	{
		StringBuffer sb = new StringBuffer();
		
		if(list == null){
			return "";
		}
		
		for(int i=0; i<list.length; i++){
			sb.append(list[i]);
			sb.append(sep);
		}
		
		return sb.toString();
	}

	public static void trimStringArray(String[] col)
	{
		if(col == null){
			return;
		}
		
		for(int i=0; i<col.length; i++){
			col[i] = col[i].trim();
		}
	}

	public static String[] fillArray(String[] vals, int numCol, String defaultVal)
	{
		String[] data = new String[numCol];
		for(int i=0; i<numCol; i++){
			if(i < vals.length){
				data[i] = vals[i];
				continue;
			}
			
			data[i] = defaultVal;
		}
		
		return data;
	}
	
	public static String fillBlankToString(String str , int size){
		if(str == null){
			str = "";
		}
		
		StringBuffer sb = new StringBuffer();
		size =size - str.length();
		for(int i = 0 ; i < size; i++ ){
			sb.append(" ");
		}
		sb.append(str);
		
		return sb.toString(); 
	}
	
	public static String fillBlankToString(int value , int size){
		String str = String.valueOf(value);
		if(str == null){
			str = "";
		}
		StringBuffer sb = new StringBuffer();
		size =size - str.length();
		for(int i = 0 ; i < size; i++ ){
			sb.append(" ");
		}
		sb.append(str);
		
		return sb.toString(); 
	}
	
	public static boolean compareByte(byte[] byte1, byte[] byte2){
		if(byte1 == null || byte2 == null){
			return false;
		}
		
		if(byte1.length != byte2.length){
			return false;
		}
		
		for(int i=0; i<byte1.length; i++){
			if(byte1[i] != byte2[i]){
				return false; 
			}
		}
		
		return true;
	}
	
	/**
	 * split不能正确分割",,"
	 * 
	 * @param inStr
	 * @param sep
	 * @return
	 */
	public static String[] splitNew(String inStr,String sep){
		ArrayList<String> list = new ArrayList<String>();
		int len = sep.length();
		int index = inStr.indexOf(sep);
		while(index >= 0){
			String str = inStr.substring(0,index);
			list.add(str);
			inStr = inStr.substring(index+len);
			index = inStr.indexOf(sep);
		}
		list.add(inStr);
		
		String arr[] = new String[list.size()];
		for(int i=0; i<list.size(); i++){
			arr[i] = (String) list.get(i);
		}
		return arr;
	}
	
	/**
	 * 
	 * str=a,b,c   sep=,
	 * limit=-1 a|b|c
	 * limit=0 a|b|c
	 * limit=1 a,b,c
	 * limit=2 a|b,c
	 * limit=3 a|b|c
	 * limit=4 a|b|c|null
	 * limit=5 a|b|c|null|null
	 * limit=6 a|b|c|null|null|null   
	 * 
	 * @param str
	 * @param sep
	 * @param limit
	 * @return
	 */
	public static String[] splitNew(String str, String sep, int limit){
		if(str == null){
			return null;
		}
		
		if(sep == null){
			return null;
		}
		
		int size = limit > 0 ? limit : 0;
		
		String[] arrayStr = str.split(sep, limit);
		if(limit <= 0){
			return arrayStr;
		}
		
		String[] outStr = new String[limit];
		
		System.arraycopy(arrayStr, 0, outStr, 0, arrayStr.length);
		
		return outStr;
	}
	
	//简单检查一下地址格式
	public static boolean checkEmail(String email){
		if(email == null){
			return false;
		}
		
		int index = email.indexOf("@");
		
		if(index <= 0){
			return false;
		}
		
		int newIndex = email.indexOf(".", index);
		
		if(newIndex <= index){
			return false;
		}
		
		if(email.endsWith(".")){
			return false;
		}
		
		return true;
	}
	
	public static String encodeHexString(String str){
		byte[] data = StringUtil.stringToUTFByte(str);
		return  StringUtil.byteToHex(data);
	}
	
	public static String decodeHexString(String hexStr){
		byte[] data = StringUtil.hexToByte(hexStr);
		return StringUtil.byteToString(data, "utf-8");
	}

	public static Object byteToBinary(byte[] data)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public static String listToString(ArrayList list) {
		if (list == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < list.size(); i++){
			sb.append(list.get(i));
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public static String arrayToString(Object[] array) {
		if (array == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < array.length; i++){
			if(array[i] instanceof int[]){
				sb.append(StringUtil.join(((int[])array[i]), ","));
			}else{
				sb.append(array[i]);
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	

	
//	public static String encodeBase64(String str){
//		if(str == null){
//			return null;
//		}
//		BASE64Encoder encoder = new BASE64Encoder();
//		try {
//			return encoder.encodeBuffer(str.getBytes());
//		}catch(Exception e){
//			return null;
//		}
//	}
//	
//	public static String decodeBase64(String str){
//		if(str == null){
//			return null;
//		}
//		BASE64Decoder decoder = new BASE64Decoder();
//		try {
//			return new String(decoder.decodeBuffer(str));
//		}catch(Exception e){
//			return null;
//		}
//	}
	
//	public static String decodeBase64(String str, String encoding){
//		if(str == null){
//			return null;
//		}
//		BASE64Decoder decoder = new BASE64Decoder();
//		try {
//			return new String(decoder.decodeBuffer(str), encoding);
//		}catch(Exception e){
//			return null;
//		}
//	}
//	
//	public static String encodeBase64(String str, String encoding){
//		if(str == null){
//			return null;
//		}
//		BASE64Encoder encoder = new BASE64Encoder();
//		try {
//			return encoder.encodeBuffer(str.getBytes(encoding));
//		}catch(Exception e){
//			return null;
//		}
//	}
	
	public static String formatMessage(String message, Object[] objs){
		return MessageFormat.format(message, objs);
	}
	
	public static String formatMessage(String message, String objs){
		return MessageFormat.format(message, objs);
	}
	
	public static String URLEncode(String url){
		if(url == null){
			return "";
		}
		
		try {
			return URLEncoder.encode(url, "utf8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	/**
	 * 
	 * 检查str是否在数组array中
	 * 
	 * @param str
	 * @param array
	 * @return
	 */
	public static int indexOfArray(String str, String[] array){
		if(str == null){
			return -1;
		}
		
		if(array == null){
			return -2;
		}
		
		int size = array.length;
		for(int i = 0; i < size ; i++){
			if(str.equals(array[i])){
				return i;
			}
		}
		return -3;
	}
	
	
	public static String getURLParms(String[] parms){
		if(parms == null){
			return "";
		}
		
		return StringUtil.join(parms, "&");
	}
	
	public static String native2ascii(String str){
		String tmp;
		StringBuffer sb = new StringBuffer();
		char c;
		int i, j;
		sb.setLength(0);
		for(i = 0;i<str.length();i++){
			c = str.charAt(i);
			if (c > 255) {
				sb.append("\\u");
				j = (c >>> 8);
				tmp = Integer.toHexString(j);
				if (tmp.length() == 1) sb.append("0");
				sb.append(tmp);
				j = (c & 0xFF);
				tmp = Integer.toHexString(j);
				if (tmp.length() == 1) sb.append("0");
				sb.append(tmp);
			}else {
				sb.append(c);
			}
		}
		return(new String(sb));
	}

//	public static String encodeBase64(String str){
//		return game.util.StringUtil.encodeBase64(str, "utf8");
//	}

//	public static String encodeBase64(String str, String charset){
//		if(str == null){
//			return null;
//		}
//		try {
//			byte[] codeBytes = Base64.encodeBase64(str.getBytes(charset));
//			return new String(codeBytes, charset);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}

//	public static String decodeBase64(String str, String charset){
//		if(str == null){
//			return null;
//		}
//		try {
//			byte[] codeBytes = Base64.decodeBase64(str.getBytes(charset)); 
//			return new String(codeBytes, charset);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	public static String decodeBase64(String str){
//		return decodeBase64(str, "utf8");
//	}
}


