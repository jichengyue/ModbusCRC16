package net.th.local;

public class CRC_16 {
	static final String HEXES = "0123456789ABCDEF";
/**
 * crc16 for modbus
 * @param buf buffer to be crc
 * @param len buffer length
 * @return crc result word
 */
	static int alex_crc16(byte[] buf, int len) {
		int i, j;
		int c, crc = 0xFFFF;
		for (i = 0; i < len; i++) {
			c = buf[i] & 0x00FF;
			crc ^= c;
			for (j = 0; j < 8; j++) {
				if ((crc & 0x0001) != 0) {
					crc >>= 1;
					crc ^= 0xA001;
				} else
					crc >>= 1;
			}
		}
		return (crc);
	}

	private static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
				.byteValue();
		_b0 = (byte) (_b0 << 4);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
				.byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}
/**
 * convert string of hex to buffer 
 * @param src string to be convert
 * @return buffer
 */
	public static byte[] HexString2Buf(String src) {
		int len = src.length();
		byte[] ret = new byte[len / 2 + 2];
		byte[] tmp = src.getBytes();
		for (int i = 0; i < len; i += 2) {
			ret[i / 2] = uniteBytes(tmp[i], tmp[i + 1]);
		}
		return ret;
	}
/**
 * convert string to buffer and append the crc check word to the end of buffer
 * @param toSend string to be convert
 * @return buffer with crc word, high byte if after low byte according the modbus
 */
	public static byte[] getSendBuf(String toSend) {
		byte[] bb = HexString2Buf(toSend);
		int ri = alex_crc16(bb, bb.length - 2);
		bb[bb.length - 2] = (byte) (0xff & ri);
		bb[bb.length - 1] = (byte) ((0xff00 & ri) >> 8);
		return bb;
	}
/**
 * print buffer to hex string
 * @param raw
 * @return
 */
	public static String getBufHexStr(byte[] raw) {
		if (raw == null) {
			return null;
		}
		final StringBuilder hex = new StringBuilder(2 * raw.length);
		for (final byte b : raw) {
			hex.append(HEXES.charAt((b & 0xF0) >> 4)).append(
					HEXES.charAt((b & 0x0F)));
		}
		return hex.toString();
	}
	/**
	 * 
	 * @param bb buffer to check
	 * @return check result
	 */
	public static boolean checkBuf(byte[] bb){
		int ri = alex_crc16(bb, bb.length-2);
		if(bb[bb.length-1]==(byte)(ri&0xff) 
				&& bb[bb.length-2]==(byte) ((0xff00 & ri) >> 8))
			return true;
		return false;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String str = "010415000004";
		byte[] sbuf2 = CRC_16.getSendBuf(str);
		System.out.println(CRC16M.getBufHexStr(sbuf2));
	}
}
