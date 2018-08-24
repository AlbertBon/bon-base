package com.bon.common.util;

import com.bon.common.domain.enums.ExceptionType;
import com.bon.common.exception.BusinessException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: bon基础项目
 * @description: 字符串工具类
 * @author: Bon
 * @create: 2018-05-21 17:48
 **/
public class StringUtils extends org.apache.commons.lang3.StringUtils{
    /**
     * 下划线转驼峰法
     * @param line 源字符串
     * @param smallCamel 大小驼峰,是否为小驼峰
     * @return 转换后的字符串
     */
    public static String underline2Camel(String line,boolean smallCamel){
        if(line==null||"".equals(line)){
            return "";
        }
        StringBuffer sb=new StringBuffer();
        Pattern pattern=Pattern.compile("([A-Za-z\\d]+)(_)?");
        Matcher matcher=pattern.matcher(line);
        while(matcher.find()){
            String word=matcher.group();
            sb.append(smallCamel&&matcher.start()==0?Character.toLowerCase(word.charAt(0)):Character.toUpperCase(word.charAt(0)));
            int index=word.lastIndexOf('_');
            if(index>0){
                sb.append(word.substring(1, index).toLowerCase());
            }else{
                sb.append(word.substring(1).toLowerCase());
            }
        }
        return sb.toString();
    }
    /**
     * 驼峰法转下划线
     * @param line 源字符串
     * @return 转换后的字符串
     */
    public static String camel2Underline(String line){
        if(line==null||"".equals(line)){
            return "";
        }
        line=String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuffer sb=new StringBuffer();
        Pattern pattern=Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher=pattern.matcher(line);
        while(matcher.find()){
            String word=matcher.group();
            sb.append(word.toUpperCase());
            sb.append(matcher.end()==line.length()?"":"_");
        }
        return sb.toString().toLowerCase();
    }

    /**
     * 首字母小写转大写
     * @param str
     * @return
     */
    public static String upperCase(String str) {
        if ((str == null) || (str.length() == 0)) return str;
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);
    }

    /**
     * 判断Integer是否是空并大于0
     * @param num
     * @return
     */
    public static boolean isNumNotBlank(Object obj){
        Integer num = (Integer) obj;
        if(null!=num&&num>0){
            return true;
        }
        return false;
    }

    /**
     * 判断byte是否为真 0假 1真
     * @param b
     * @return
     */
    public static boolean isByteTrue(Byte b){
        if(null==b){
            throw new BusinessException(ExceptionType.NULL_ERROR);
        }
        if(b==1){
            return true;
        }
        return false;
    }

    public static StringBuilder replaceTxtEnter(StringBuilder sb){
        String s = sb.toString();
        s = s.replaceAll("\r\n",";");
        sb = new StringBuilder();
        sb.append(s);
        return sb;
    }


}
