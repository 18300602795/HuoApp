package com.huosuapp.Util;

import android.text.TextUtils;

/**
 * Created by admin on 2016/9/2.
 */
public class getTypeUtils {

    public static String[] getType(String typeStr) {
        String[] typeArr={"","角色","格斗","休闲", "竞速","策略","射击", "其它"};
        String[] typeIndexs = typeStr.split(",");
        String[] returnStr=new String[3];
        try {
            for(int i=0;i<typeIndexs.length;i++){
                int index=Integer.parseInt(typeIndexs[i]);
                returnStr[i]=typeArr[index];

//                textContent.append(typeArr[index]);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if(TextUtils.isEmpty(returnStr[0])){
            returnStr[0]="其它";
        }
        return returnStr;
    }

}
