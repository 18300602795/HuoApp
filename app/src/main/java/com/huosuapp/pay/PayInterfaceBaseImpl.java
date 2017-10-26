package com.huosuapp.pay;

import org.apache.commons.lang.StringUtils;


public class PayInterfaceBaseImpl implements PayInterface {

	@Override
	public Object parseObj(String json) {
		if(StringUtils.isEmpty(json)){
			return null;
		}
		return json;
	}

}
