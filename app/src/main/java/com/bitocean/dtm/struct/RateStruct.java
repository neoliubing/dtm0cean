/**
 * 
 */
package com.bitocean.dtm.struct;

import java.io.Serializable;

/**
 * @author bing.liu
 *
 */
public class RateStruct implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7674589133689704579L;
	public String bit_type;//虚拟货币币类型
	public String dtm_currency;//法币类型
	public Double currency_buy;// 用户数字货币买入 1比特币对应 423.32 //显示给用户看的
	public Double currency_sell;// 用户数字货币卖出 1比特币对应 423.32
	public Double bit_buy;// 用户数字货币买入 钱箱基准现金面额 对应的比特币汇率 1000日元对应 0.02 10000日元对应0.2  //当用户买入时点击叠加
	public Double bit_sell;// 用户数字货币卖出 钱箱基准现金面额 对应的比特币汇率 1000日元对应 0.02 10000日元对应0.2
	public int threshold_buy_min;//本次买最低交易额
    public int threshold_buy_max;//本次买最高交易额
    public int threshold_sell_min;//本次卖最低交易额
    public int threshold_sell_max;//本次卖最高交易额
}
