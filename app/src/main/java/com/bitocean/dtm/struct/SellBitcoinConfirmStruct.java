/**
 * 
 */
package com.bitocean.dtm.struct;

import java.io.Serializable;

/**
 * @author bing.liu
 *
 */
public class SellBitcoinConfirmStruct implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 9204694307884145556L;
	/**
	 * 
	 */
	public String resutlString;
	public int reason;
	public String dtm_currency;
	public String redeem_code;
    public String txid;
	public int currency_num;
    public String trans_id;
}
