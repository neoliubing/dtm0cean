/**
 * 
 */
package com.bitocean.dtm.struct;

import java.io.Serializable;
/**
 * @author bing.liu
 *
 */
public class SellBitcoinQRStruct implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7441791379703418621L;
	/**
	 * 
	 */
	public String resutlString;
	public int reason;
	public String bitcoin_qr;
    public String tradeid;
	public int quota_num;
}
