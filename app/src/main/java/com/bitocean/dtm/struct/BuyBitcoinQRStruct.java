/**
 * 
 */
package com.bitocean.dtm.struct;

import java.io.Serializable;
/**
 * @author bing.liu
 *
 */
public class BuyBitcoinQRStruct implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2399534593635417033L;
	/**
	 * 
	 */
	public String resutlString;
	public int reason;
	public String user_public_key;
	public String bit_type;
	public Double bitcoin_num;
    public String trans_id;
}
