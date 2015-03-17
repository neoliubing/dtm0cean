package com.bitocean.dtm.service;
/**
 * @author bing.liu
 * 
 */
public class PollingBroadCastEvent {
	public final static int BASE_EVENT = 0;

    //获取汇率===============
    public final static int EVENT_GET_RATE_LIST_SUCCESS = BASE_EVENT + 1;
    public final static int EVENT_GET_RATE_LIST_FAIL = BASE_EVENT + 2;
    //卖币确认======================
    public final static int EVENT_GET_SELL_SUCCESS = BASE_EVENT + 3;
    public final static int EVENT_GET_SELL_FAIL = BASE_EVENT + 4;

    private int type;
	private Object obj;

	public PollingBroadCastEvent(int type) {
		this.type = type;
	}

	public PollingBroadCastEvent(int type, Object obj) {
		this.type = type;
		this.obj = obj;
	}

	public int getType() {
		return type;
	}

	public Object getObject() {
		return obj;
	}
}
