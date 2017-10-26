package com.huosuapp.Util;

import android.util.Log;

import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 说明：移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
 * 联通：130、131、132、152、155、156、185、186
 * 电信：133、153、180、189
 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
 * 验证号码 手机号 固话均可
 */

public class StringUtils {
    public static  boolean isPhoneNumberValid(String number){
        boolean isValid = false;
        String expression = "((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
        CharSequence inputStr = number;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches() ) {
            isValid = true;
        }
        return isValid;
    }

    /**
     * get请求方法拼接
     */
    public static String getCompUrlFromParams(String url, Map<String, String> params) {
        String url1 = "";
//        if (params != null) {
//            params.put(Constants.AGENT, Constants.agent);
//            params.put(Constants.FROM, Constants.from);
//            params.put(Constants.APP_ID, Constants.appid);
//            params.put(Constants.CLIENT_ID, Constants.clientid);
//        }
        if (null != url && !"".equals(url) && null != params
                && params.size() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("?");
            for (Map.Entry<String, String> entity : params.entrySet()) {
                Logger.msg("entity.getKey()", entity.getKey());
                Logger.msg("entity.getValue()",entity.getValue());
                sb.append(entity.getKey()).append("=")
                        .append(entity.getValue()).append("&");
            }
            Logger.msg("sb--1--",sb.toString());
            sb.deleteCharAt(sb.length() - 1);
            Logger.msg("sb--2--",sb.toString());
            url1 = url + sb.toString();
            Logger.msg("url---1---", url1);
            return url1;
        }
        Logger.msg("url---2---", url);
       return url;
    }
    public static String getUrlContainAppid(String url, Map<String, String> params) {
        String url1 = "";
        if (params != null) {
            params.put(Constants.AGENT, Constants.agent);
            params.put(Constants.FROM, Constants.from);
            params.put(Constants.APP_ID, Constants.appid);
            params.put(Constants.CLIENT_ID, Constants.clientid);
        }
        if (null != url && !"".equals(url) && null != params
                && params.size() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("?");
            for (Map.Entry<String, String> entity : params.entrySet()) {
                sb.append(entity.getKey()).append("=")
                        .append(entity.getValue()).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            url1 = url + sb.toString();
            Logger.msg("url", url1);
            return url1;
        }
        Logger.msg("url", url);
        return url;
    }
    public static String getCode(){
        Random random = new Random();
        StringBuffer stringBuffer= new StringBuffer();
        int I = random.nextInt(10000);
        int B = random.nextInt(10000);
        int R = random.nextInt(10000);
        int U = random.nextInt(10000);
       return stringBuffer.append(I+"").append(B+"").append(R+"").append(U+"").toString();
    }

}
