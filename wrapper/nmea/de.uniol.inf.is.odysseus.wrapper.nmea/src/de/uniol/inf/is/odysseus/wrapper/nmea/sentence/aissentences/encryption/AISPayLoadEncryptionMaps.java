package de.uniol.inf.is.odysseus.wrapper.nmea.sentence.aissentences.encryption;

import java.util.HashMap;
import java.util.Map;

public class AISPayLoadEncryptionMaps {
	
	public final static Map<Integer, String> sixBitAscii = new HashMap<Integer, String>();
	static {
		sixBitAscii.put(0, "@"); 	
		sixBitAscii.put(1, "A"); 		 
		sixBitAscii.put(2, "B");		
		sixBitAscii.put(3, "C"); 		
		sixBitAscii.put(4, "D"); 		
		sixBitAscii.put(5, "E"); 		
		sixBitAscii.put(6, "F"); 		
		sixBitAscii.put(7, "G"); 		
		sixBitAscii.put(8, "H");		
		sixBitAscii.put(9, "I"); 		
		sixBitAscii.put(10, "J");		
		sixBitAscii.put(11, "K"); 		
		sixBitAscii.put(12, "L"); 		
		sixBitAscii.put(13, "M"); 		
		sixBitAscii.put(14, "N"); 		
		sixBitAscii.put(15, "O"); 		
		sixBitAscii.put(16, "P"); 		
		sixBitAscii.put(17, "Q"); 		
		sixBitAscii.put(18, "R"); 		
		sixBitAscii.put(19, "S"); 		
		sixBitAscii.put(20, "T"); 		
		sixBitAscii.put(21, "U"); 		
		sixBitAscii.put(22, "V"); 		
		sixBitAscii.put(23, "W"); 		 
		sixBitAscii.put(24, "X"); 		
		sixBitAscii.put(25, "Y"); 		
		sixBitAscii.put(26, "Z"); 		 
		sixBitAscii.put(27, "["); 		
		sixBitAscii.put(28, "\\"); 		
		sixBitAscii.put(29, "]"); 		
		sixBitAscii.put(30, "^"); 		
		sixBitAscii.put(31, "_"); 		
		sixBitAscii.put(32, " "); 		
		sixBitAscii.put(33, "!"); 		
		sixBitAscii.put(34, "\""); 		
		sixBitAscii.put(35, "#"); 		
		sixBitAscii.put(36, "$"); 		
		sixBitAscii.put(37, "%"); 		
		sixBitAscii.put(38, "&"); 		
		sixBitAscii.put(39, "'"); 		
		sixBitAscii.put(40, "("); 		
		sixBitAscii.put(41, ")"); 		
		sixBitAscii.put(42, "*"); 		
		sixBitAscii.put(43, "+"); 		 
		sixBitAscii.put(44, ","); 		
		sixBitAscii.put(45, "-"); 		
		sixBitAscii.put(46, "."); 		
		sixBitAscii.put(47, "/"); 		
		sixBitAscii.put(48, "0"); 		
		sixBitAscii.put(49, "1"); 		
		sixBitAscii.put(50, "2"); 		
		sixBitAscii.put(51, "3");		
		sixBitAscii.put(52, "4");		
		sixBitAscii.put(53, "5");		
		sixBitAscii.put(54, "6"); 		
		sixBitAscii.put(55, "7"); 		
		sixBitAscii.put(56, "8"); 		
		sixBitAscii.put(57, "9"); 		
		sixBitAscii.put(58, ":"); 		
		sixBitAscii.put(59, ";"); 		
		sixBitAscii.put(60, "<"); 		
		sixBitAscii.put(61, "="); 		
		sixBitAscii.put(62, ">"); 		
		sixBitAscii.put(63, "?"); 		
	}

	public final static Map<String, String> charToSixBit = new HashMap<String, String>();
	static {
		charToSixBit.put("0", "000000"); // 0
		charToSixBit.put("1", "000001"); // 1
		charToSixBit.put("2", "000010"); // 2
		charToSixBit.put("3", "000011"); // 3
		charToSixBit.put("4", "000100"); // 4
		charToSixBit.put("5", "000101"); // 5
		charToSixBit.put("6", "000110"); // 6
		charToSixBit.put("7", "000111"); // 7
		charToSixBit.put("8", "001000"); // 8
		charToSixBit.put("9", "001001"); // 9
		charToSixBit.put(":", "001010"); // 10
		charToSixBit.put(";", "001011"); // 11
		charToSixBit.put("<", "001100"); // 12
		charToSixBit.put("=", "001101"); // 13
		charToSixBit.put(">", "001110"); // 14
		charToSixBit.put("?", "001111"); // 15
		charToSixBit.put("@", "010000"); // 16
		charToSixBit.put("A", "010001"); // 17
		charToSixBit.put("B", "010010"); // 18
		charToSixBit.put("C", "010011"); // 19
		charToSixBit.put("D", "010100"); // 20
		charToSixBit.put("E", "010101"); // 21
		charToSixBit.put("F", "010110"); // 22
		charToSixBit.put("G", "010111"); // 23 
		charToSixBit.put("H", "011000"); // 24
		charToSixBit.put("I", "011001"); // 25
		charToSixBit.put("J", "011010"); // 26 
		charToSixBit.put("K", "011011"); // 27
		charToSixBit.put("L", "011100"); // 28
		charToSixBit.put("M", "011101"); // 29
		charToSixBit.put("N", "011110"); // 30
		charToSixBit.put("O", "011111"); // 31
		charToSixBit.put("P", "100000"); // 32
		charToSixBit.put("Q", "100001"); // 33
		charToSixBit.put("R", "100010"); // 34
		charToSixBit.put("S", "100011"); // 35
		charToSixBit.put("T", "100100"); // 36
		charToSixBit.put("U", "100101"); // 37
		charToSixBit.put("V", "100110"); // 38
		charToSixBit.put("W", "100111"); // 39
		charToSixBit.put("`", "101000"); // 40
		charToSixBit.put("a", "101001"); // 41
		charToSixBit.put("b", "101010"); // 42
		charToSixBit.put("c", "101011"); // 43 
		charToSixBit.put("d", "101100"); // 44
		charToSixBit.put("e", "101101"); // 45
		charToSixBit.put("f", "101110"); // 46
		charToSixBit.put("g", "101111"); // 47
		charToSixBit.put("h", "110000"); // 48
		charToSixBit.put("i", "110001"); // 49
		charToSixBit.put("j", "110010"); // 50
		charToSixBit.put("k", "110011"); // 51
		charToSixBit.put("l", "110100"); // 52
		charToSixBit.put("m", "110101"); // 53
		charToSixBit.put("n", "110110"); // 54
		charToSixBit.put("o", "110111"); // 55
		charToSixBit.put("p", "111000"); // 56
		charToSixBit.put("q", "111001"); // 57
		charToSixBit.put("r", "111010"); // 58
		charToSixBit.put("s", "111011"); // 59
		charToSixBit.put("t", "111100"); // 60
		charToSixBit.put("u", "111101"); // 61
		charToSixBit.put("v", "111110"); // 62
		charToSixBit.put("w", "111111"); // 63
	}	
}
