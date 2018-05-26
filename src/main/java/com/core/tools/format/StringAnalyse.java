package com.core.tools.format;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringAnalyse {

    /**
     * match pattern, match filename in a path, may be url,uri,filepath,filename
     */
    private static Pattern p = Pattern.compile("([/\\\\]?)([^/\\\\\\.]*)(\\.)([^/\\\\\\.]*)$");
    /**
     * Matcher
     */
    private static Matcher m = null;
    /**
     * NUMBER表达式
     */
    public static final String NUMBER_PATTERN = "^\\d+$";
    /**
     * INT表达式
     */
    public static final String INTEGER_PATTERN = "^[-]?\\d+$";
    /**
     * 文件格式表达式
     */
    public static final String FILE_NAME_PATTERN = "^\\w+(\\.\\w+)?$";
    /**
     * name表达式
     */
    public static final String NAME_PATTERN = "^\\w*$";
    /**
     * longname表达式
     */
    public static final String LONGNAME_PATTERN = ".*";
    /**
     * ip表达式
     */
    public static final String IP_PATTERN = "^(([01]?[\\d]{1,2})|(2[0-4][\\d])|(25[0-5]))(\\.(([01]?[\\d]{1,2})|(2[0-4][\\d])|(25[0-5]))){3}$";
    /**
     * NEAR_IP表达式
     */
    public static final String NEAR_IP_PATTERN = "\\d{1,3}(?:\\.\\d{1,3}){3}";
    /**
     * EMAIL表达式
     */
    public static final String EMAIL_PATTERN = "^([\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+)$";
    /**
     * 图片格式集合
     */
    public static final String[] IMAGE_LIST = { "bmp", "gif", "jpg", "png" };
    /**
     * AUDIO格式集合
     */
    public static final String[] AUDIO_LIST = { "mid", "mp3", "wav", "wma" };
    /**
     * sql通配符
     */
    private static final String SQL_WILDCARDS = "_%";

    /**
     * get file name prefix. eg: filePath="..\bbb/aaaa.gif" will return "aaaa"
     * 
     * @param filePath filePath may be a URL,URI,Path,fileName...
     * @return file name prefix
     */
    public static String getFileNamePrefix(String filePath) {
        m = p.matcher(filePath);
        if (m.find()) {
            return m.group(2);
        }
        return null;
    }

    /**
     * get file name suffix. eg: filePath="..\bbb/aaaa.gif" will return "gif"
     * 
     * @param filePath filePath may be a URL,URI,Path,fileName...
     * @return file name suffix
     */
    public static String getFileNameSuffix(String filePath) {
        m = p.matcher(filePath);
        if (m.find()) {
            return m.group(4);
        }
        return null;
    }

    /**
     * get file name. eg: filePath="..\bbb/aaaa.gif" will return "aaaa.gif"
     * 
     * @param filePath filePath may be a URL,URI,Path,fileName...
     * @return full file name
     */
    public static String getFileName(String filePath) {
        m = p.matcher(filePath);
        if (m.find()) {
            return m.group(2) + m.group(3) + m.group(4);
        }
        return null;
    }

    /**
     * get file path. eg: filePath="..\bbb/aaaa.gif" will return "..\bbb/"
     * 
     * @param filePath filePath may be a URL,URI,Path,fileName...
     * @return file path, don't file name.
     */
    public static String getPath(String filePath) {
        m = p.matcher(filePath);
        if (m.find()) {
            return m.replaceFirst("$1");
        }
        return null;
    }

    /**
     * String transform to array
     * 
     * @param str eg:"value1;value2"
     * @param separator The separator. eg:";"
     * @param isTrim ;"
     * @return a object array eg:["value1","value2"]
     */
    public static String[] stringToArray(String str, String separator, boolean isTrim) {
        List values = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(str, separator);
        while (tokenizer.hasMoreElements()) {
            if (isTrim) {
                values.add(tokenizer.nextElement().toString().trim());
            } else {
                values.add(tokenizer.nextElement());
            }
        }
        return (String[]) values.toArray(new String[0]);
    }

    /**
     * String transform to array[2][]
     * 
     * @param str eg:"key1=value1;key2=value2"
     * @param ps The primary separator. eg:";"
     * @param as The assistant separator. eg:"="
     * @return an array, array[0] is key array, array[1] is value array
     */
    public static String[][] stringToArray(String str, String ps, String as) {
        String[][] arr = new String[2][];
        List keys = new ArrayList();
        List values = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(str, ps);
        String tmpStr = null;
        String[] tmpArr = null;
        while (tokenizer.hasMoreElements()) {
            tmpStr = (String) tokenizer.nextElement();
            tmpArr = tmpStr.split(as);
            keys.add(tmpArr[0]);
            values.add(tmpArr[1]);
        }
        arr[0] = (String[]) keys.toArray(new String[0]);
        arr[1] = (String[]) values.toArray(new String[0]);
        return arr;
    }

    /**
     * String transform to array, and add left/right bracket
     * 
     * @param str eg:"value1;value2"
     * @param separator The separator. eg:";"
     * @param leftBracket the left bracket. eg:"("
     * @param rightBracket the right bracket. eg:")"
     * @param isTrim isTrim
     * @return a object array eg:["(value1)","(value2)"]
     */
    public static String[] stringToArray(String str, String separator, String leftBracket, String rightBracket,
            boolean isTrim) {
        List values = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(str, separator);
        while (tokenizer.hasMoreElements()) {
            StringBuffer sb = new StringBuffer();
            if (leftBracket != null) {
                sb.append(leftBracket);
            }
            if (isTrim) {
                sb.append(tokenizer.nextElement().toString().trim());
            } else {
                sb.append(tokenizer.nextElement());
            }
            if (rightBracket != null) {
                sb.append(rightBracket);
            }
            values.add(sb.toString());
        }
        return (String[]) values.toArray(new String[0]);
    }

    /**
     * String transform to array, and clip left/right bracket
     * 
     * @param str eg:"(value1);(value2)"
     * @param separator The separator. eg:";"
     * @param leftBracket the left bracket. eg:"("
     * @param rightBracket the right bracket. eg:")"
     * @param isTrim isTrim
     * @return a object array eg:["value1","value2"]
     */
    public static String[] stringToArrayClip(String str, String separator, String leftBracket, String rightBracket,
            boolean isTrim) {
        List values = new ArrayList();
        StringTokenizer tokenizer = new StringTokenizer(str, separator);
        while (tokenizer.hasMoreElements()) {
            String ne;
            if (isTrim) {
                ne = tokenizer.nextElement().toString().trim();
            } else {
                ne = tokenizer.nextElement().toString();
            }
            int start = 0;
            int end = ne.length();
            if (leftBracket != null) {
                if (ne.startsWith(leftBracket)) {
                    start = leftBracket.length();
                }
            }
            if (rightBracket != null) {
                if (ne.endsWith(rightBracket)) {
                    end -= rightBracket.length();
                }
            }
            values.add(ne.substring(start, end));
        }
        return (String[]) values.toArray(new String[0]);
    }

    /**
     * String transform to map eg:"key1=value1;key2=value2"
     * 
     * @param str str
     * @param ps The primary separator. eg:";"
     * @param as The assistant separator. eg:"="
     * @return HashMap
     */
    public static HashMap stringToMap(String str, String ps, String as) {
        HashMap hm = new HashMap();
        StringTokenizer tokenizer = new StringTokenizer(str, ps);
        String tmpStr = null;
        String[] arrStr = null;
        while (tokenizer.hasMoreElements()) {
            tmpStr = (String) tokenizer.nextElement();
            arrStr = tmpStr.split(as);
            hm.put(arrStr[0], arrStr[1]);
        }
        return hm;
    }

    /**
     * String transform to map eg:"key1=value11,value12;key2=value21,value22"
     * 
     * @param str str
     * @param ps The primary separator. eg:";"
     * @param as1 The assistant separator. eg:"="
     * @param as2 The assistant separator. eg:","
     * @return HashMap
     */
    public static HashMap stringToMap(String str, String ps, String as1, String as2) {
        if (as2 == null || as2.equals("")) {
            return stringToMap(str, ps, as1);
        }
        HashMap hm = new HashMap();
        StringTokenizer tokenizer = new StringTokenizer(str, ps);
        String[] arrStr = null;
        while (tokenizer.hasMoreElements()) {
            arrStr = ((String) tokenizer.nextElement()).split(as1);
            if (arrStr != null && arrStr.length == 2 && arrStr[1] != null) {
                hm.put(arrStr[0], stringToArray(arrStr[1], as2, false));
            }
        }
        return hm;
    }

    /**
     * array transform to String
     * 
     * @param values a object array eg:["value1","value2"]
     * @param separator the separator. eg:";"
     * @return a string eg:"value1;value2"
     */
    public static String arrayToString(Object[] values, String separator, boolean isTrim) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < values.length;) {
            if (isTrim) {
                sb.append(values[i].toString().trim());
            } else {
                sb.append(values[i]);
            }
            if (++i < values.length) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    /**
     * array transform to String, and except left/right bracket.
     * 
     * @param values a object array eg:["'value1'","'value2'"]
     * @param separator the separator. eg:";"
     * @param leftBracket the left bracket. eg:"'"
     * @param rightBracket the right bracket. eg:"'"
     * @return a string eg:"value1;value2"
     */
    public static String arrayToString(Object[] values, String separator, String leftBracket, String rightBracket,
            boolean isTrim) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < values.length;) {
            String value = values[i].toString();
            if (leftBracket != null) {
                if (leftBracket.length() <= value.length() && value.startsWith(leftBracket)) {
                    value = value.substring(leftBracket.length());
                }
            }
            if (rightBracket != null) {
                if (rightBracket.length() <= value.length() && value.endsWith(rightBracket)) {
                    value = value.substring(0, value.length() - rightBracket.length());
                }
            }
            if (isTrim) {
                sb.append(value.trim());
            } else {
                sb.append(value);
            }
            if (++i < values.length) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    /**
     * map transform to String
     * 
     * @param map
     * @param ps The primary separator. eg:";"
     * @param as The assistant separator. eg:"="
     * @return A String eg:"key1=value1;key2=value2"
     */
    public static String mapToString(Map map, String ps, String as) {
        StringBuffer sb = new StringBuffer();
        Iterator it = map.keySet().iterator();
        int i = 0;
        int size = map.size();
        while (it.hasNext()) {
            String key = (String) it.next();
            sb.append(key);
            sb.append(as);
            sb.append(map.get(key));
            if (++i < size) {
                sb.append(ps);
            }
        }
        return sb.toString();
    }

    /**
     * Use a pattern match a string from start
     * 
     * @param s The String will be Matched.
     * @param patternStr The string expression of pattern
     * @param start The start index in s
     * @param group The result group number
     * @return The index of result group string start character in s.<br>
     *         If match failure, return -1.
     */
    public static int findPattern(String s, String patternStr, int start, int group) {
        Pattern p = Pattern.compile(patternStr);
        Matcher m = p.matcher(s);
        if (m.find(start)) {
            return m.start(group);
        }
        return -1;
    }

    /**
     * Use a pattern match a string
     * 
     * @param s The String will be Matched.
     * @param patternStr The string expression of pattern
     * @return
     */
    public static boolean testPattern(String s, String patternStr) {
        Pattern p = Pattern.compile(patternStr);
        Matcher m = p.matcher(s);
        return m.find();
    }

    public static boolean matchPattern(String s, String patternStr) {
        return testPattern(s, patternStr);
    }

    /**
     * Test a string whether or not it's a file name. File name must be all
     * alphanumeric or '_'.
     * 
     * @param fileName
     * @return
     */
    public static boolean testFileName(String fileName) {
        return testPattern(fileName, FILE_NAME_PATTERN);
    }

    /**
     * Test a string whether or not it's a name.
     * 
     * @param name
     * @return
     */
    public static boolean testName(String name) {
        return testPattern(name, NAME_PATTERN);
    }

    /**
     * Test a string whether or not it's a long name. include space
     * 
     * @param name
     * @return
     */
    public static boolean testNameL(String name) {
        return testPattern(name, LONGNAME_PATTERN);
    }

    /**
     * Test a string whether or not it's a number.
     * 
     * @param number number>=0
     * @return
     */
    public static boolean testNumber(String number) {
        return testPattern(number, NUMBER_PATTERN);
    }

    /**
     * Test a string whether or not it's a integer.
     * 
     * @param integer eg:-2 34 0 +65
     * @return
     */
    public static boolean testInteger(String integer) {
        return testPattern(integer, INTEGER_PATTERN);
    }

    /**
     * Test a string whether or not it's a IP address.
     * 
     * @param ip
     * @return
     */
    public static boolean testIP(String ip) {
        return testPattern(ip, IP_PATTERN);
    }

    /**
     * Test a string whether or not it's a e_mail address.
     * 
     * @param email
     * @return
     */
    public static boolean testEmail(String email) {
        return testPattern(email, EMAIL_PATTERN);
    }

    /**
     * Test a string whether or not it's a e_mail address sequence. Example :
     * aaa@google.com,bbb@hotmai.com,ccc@sun.com is true
     * 
     * @param emails
     * @param separator The separator, eg:";" or ","
     * @return
     */
    public static boolean testEmails(String emails, String separator) {
        StringBuffer emailsPattern = new StringBuffer("^([\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+)(");
        emailsPattern.append(separator);
        emailsPattern.append("[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+)*$");
        return testPattern(emails, emailsPattern.toString());
    }

    /**
     * Test a string whether or not it's a e_mail address sequence. Example :
     * aaa@google.com,bbb@hotmai.com,ccc@sun.com or
     * aaa@google.com;bbb@hotmai.com;ccc@sun.com is true
     * 
     * @param emails
     * @param separators The separator array, eg:[";",","," "]
     * @return
     */
    public static boolean testEmails(String emails, String[] separators) {
        for (int i = 0; i < separators.length; i++) {
            if (testEmails(emails, separators[i]))
                return true;
        }
        return false;
    }

    /**
     * Test a file path is a image file by file suffix.
     * 
     * @param filePath filePath may be a URL,URI,Path,fileName...
     * @return
     */
    public static boolean testImage(String filePath) {
        String fileSuffix = getFileNameSuffix(filePath);
        if (fileSuffix != null) {
            for (int i = 0; i < IMAGE_LIST.length; i++) {
                if (IMAGE_LIST[i].equalsIgnoreCase(fileSuffix)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Test a file path is a audio file by file suffix.
     * 
     * @param filePath filePath may be a URL,URI,Path,fileName...
     * @return
     */
    public static boolean testAudio(String filePath) {
        String fileSuffix = getFileNameSuffix(filePath);
        if (fileSuffix != null) {
            for (int i = 0; i < AUDIO_LIST.length; i++) {
                if (AUDIO_LIST[i].equalsIgnoreCase(fileSuffix)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * unite two object arrays to one, no repeat element
     * 
     * @param ary1
     * @param ary2
     * @return a united object array
     */
    public static Object[] uniteObjectArray(Object[] ary1, Object[] ary2) {
        ArrayList al = new ArrayList();

        for (int i = 0; i < ary1.length; i++) {
            al.add(ary1[i]);
        }
        for (int j = 0; j < ary2.length; j++) {
            boolean exist = false;
            for (int k = 0; k < ary1.length; k++) {
                if (ary2[j].equals(ary1[k])) {
                    exist = true;
                    break;
                }
            }
            if (!exist)
                al.add(ary2[j]);
        }
        return al.toArray();
    }

    /**
     * unite two string arrays to one, no repeat element
     * 
     * @param ary1
     * @param ary2
     * @return a united string array
     */
    public static String[] uniteStringArray(String[] ary1, String[] ary2) {
        Object[] to = uniteObjectArray(ary1, ary2);
        String[] ts = new String[to.length];
        for (int i = 0; i < ts.length; i++) {
            ts[i] = to[i].toString();
        }
        return ts;
    }

    /**
     * wipe repeat object in objs array.
     * 
     * @param objs
     * @return new object array and no repeat object
     */
    public static Object[] wipeRepeat(Object[] objs) {
        if (objs == null)
            return new Object[0];
        ArrayList al = new ArrayList();
        LinkedHashSet lhs = new LinkedHashSet((int) (objs.length * 1.51));
        for (int i = 0; i < objs.length; i++) {
            lhs.add(objs[i]);
        }
        Iterator iter = lhs.iterator();
        while (iter.hasNext()) {
            al.add(iter.next());
        }
        return al.toArray();
    }

    /**
     * 校验对象在对象数组中是否存在
     * 
     * @param objs
     * @param obj
     * @return
     */
    public static boolean existInArray(Object[] objs, Object obj) {
        if (objs == null || objs.length == 0 || obj == null)
            return false;
        for (int i = 0; i < objs.length; i++) {
            if (objs[i].equals(obj))
                return true;
        }
        return false;
    }

    /**
     * 校验str中是否存在characters中指定的任一字符
     * 
     * @param str eg:"abc_"
     * @param characters eg:"_%"
     * @return 如果str中存在characters中指定的字符集合其中任一字符，返回true。
     */
    public static boolean existCharacter(String str, String characters) {
        String ps = "^.*[" + characters + "].*$";
        return Pattern.matches(ps, str);
    }

    /**
     * 校验sql中是否存在sql通配符（用于检查是否是模糊匹配）
     * 
     * @param sql eg:"abc_"
     * @return 如果sql中存在SQL通配符，返回true（说明启用了模糊匹配）
     */
    public static boolean isSQLFuzzyMatching(String sql) {
        return existCharacter(sql, SQL_WILDCARDS);
    }

    /**
     * 从一个字符串中解析出第一个符合ip地址定义的第一个ip串，如果找不到则返回""。
     * 
     * @param str eg："sljgi2434.625.68.733.345ife324.53.43.624msli"
     * @return 如果存在一个ip地址，返回这个ip串； 如果有多个符合的ip，返回从左向右数，符合条件的第一个ip串；
     *         如果没有符合的ip，返回""。 对于上例将返回"24.53.43.62"
     *         （注：已使用的匹配串中的所有字符将不再用于下次匹配，非贪婪模式）
     */
    public static String getIncludeIP(String str) {
        String ip = "";
        Pattern p1 = Pattern.compile(NEAR_IP_PATTERN);
        Matcher m1 = p1.matcher(str);
        while (ip.equals("") && m1.find()) {
            String tmp = m1.group();
            if (testIP(tmp)) {
                ip = tmp;
            } else {
                String[] tmps = tmp.split("\\.");
                if (Integer.parseInt(tmps[0]) > 255) {
                    tmp = tmp.substring(1);
                    if (testIP(tmp)) {
                        ip = tmp;
                    } else if (Integer.parseInt(tmps[3]) > 255 && testIP(tmp.substring(0, tmp.length() - 1))) {
                        ip = tmp.substring(0, tmp.length() - 1);
                    }
                } else {
                    if (Integer.parseInt(tmps[3]) > 255 && testIP(tmp.substring(0, tmp.length() - 1))) {
                        ip = tmp.substring(0, tmp.length() - 1);
                    }
                }
            }
        }
        return ip;
    }

    /**
     * 从一个字符串中可解析出的所有符合ip地址定义的ip串集，如果找不到则返回一个new String[0]。
     * 
     * @param str eg："sljgi2434.25.68.733.345ife324.53.43.624msli"
     * @return 如果有多个符合的ip，返回一个String[]， String[]中的序号依据，从左向右数，符合条件的ip串；
     *         如果没有符合的ip，返回一个new String[0]。
     *         对于上例将返回["34.25.68.73","24.53.43.62"]。
     *         （注：已使用的匹配串中的所有字符将不再用于下次匹配，非贪婪模式）
     */
    public static String[] getIncludeIPs(String str) {
        ArrayList ips = new ArrayList();
        Pattern p1 = Pattern.compile(NEAR_IP_PATTERN);
        Matcher m1 = p1.matcher(str);
        while (m1.find()) {
            String tmp = m1.group();
            if (testIP(tmp)) {
                ips.add(tmp);
            } else {
                String[] tmps = tmp.split("\\.");
                if (Integer.parseInt(tmps[0]) > 255) {
                    tmp = tmp.substring(1);
                    if (testIP(tmp)) {
                        ips.add(tmp);
                    } else if (Integer.parseInt(tmps[3]) > 255 && testIP(tmp.substring(0, tmp.length() - 1))) {
                        ips.add(tmp.substring(0, tmp.length() - 1));
                    }
                } else {
                    if (Integer.parseInt(tmps[3]) > 255 && testIP(tmp.substring(0, tmp.length() - 1))) {
                        ips.add(tmp.substring(0, tmp.length() - 1));
                    }
                }
            }
        }
        return (String[]) ips.toArray(new String[0]);
    }

    /**
     * 比较两个ip地址的大小
     * 
     * @param ip1，eg:"10.1.1.2"
     * @param ip2，eg:"10.1.2.66"
     * @return 如果ip1小于ip2返回-1，大于返回1，等于则返回0。
     */
    public static int compareIP(String ip1, String ip2) {
        String[] fip = ip1.split("\\.");
        String[] tip = ip2.split("\\.");
        for (int i = 0; i < fip.length; i++) {
            if (Integer.parseInt(fip[i]) < Integer.parseInt(tip[i])) {
                return -1;
            } else if (Integer.parseInt(fip[i]) > Integer.parseInt(tip[i])) {
                return 1;
            } else
                continue;
        }
        return 0;
    }

    /**
     * 比较两个已经转换为int型的ip地址的大小
     * 
     * @param ip1，eg:{10，1，1，2}"
     * @param ip2，eg:{10，1，2，66}"
     * @return 如果ip1小于ip2返回-1，大于返回1，等于则返回0。
     */
    public static int compareIntIP(int ip1[], int ip2[]) {
        for (int i = 0; i < ip1.length; i++) {
            if (ip1[i] < ip2[i]) {
                return -1;
            } else if (ip1[i] > ip2[i]) {
                return 1;
            } else
                continue;
        }
        return 0;
    }

    /**
     * 判断在多个ip地址中是否有符合的ip地址，只要有一个符合就返回true
     * 
     * @param ip
     * @param ips
     * @return boolean
     */
    public static boolean matchIps(String ip, String ips) {
        boolean match = false;
        Object[] ipArray = StringAnalyse.wipeRepeat(StringAnalyse.stringToArray(ips.trim(), ",", "", "", true));
        for (int i = 0; i < ipArray.length; i++) {
            int flag = StringAnalyse.compareIP(ip, ipArray[i].toString());
            if (flag == 0) {
                match = true;
                break;
            }
        }
        return match;
    }

    /**
     * 判断在ip地址中是否属于某个ip范围
     * 
     * @param ip ip
     * @param ipRange ipRange
     * @return boolean
     */
    public static boolean matchIpRange(String ip, Object[] ipRange) {
        boolean match = true;
        int flag = StringAnalyse.compareIP(ip, ipRange[0].toString());
        if (flag == -1) {
            match = false;
        }
        int flag2 = StringAnalyse.compareIP(ip, ipRange[1].toString());
        if (flag2 == 1) {
            match = false;
        }
        return match;
    }

    /**
     * 判断在ip地址中是否属于多个ip地址范围中的一个，只要有一个符合就返回true
     * 
     * @param ip ip
     * @param ipRanges ipRanges
     * @return boolean
     */
    public static boolean matchIpRanges(String ip, String ipRanges) {
        boolean match = false;
        Object[] iprObjs = StringAnalyse.stringToArray(ipRanges.trim(), ",", "", "", true);
        for (int i = 0; i < iprObjs.length; i++) {
            Object[] iprs = StringAnalyse.stringToArray(iprObjs[i].toString(), "-", "", "", true);
            if (matchIpRange(ip, iprs)) {
                match = true;
                break;
            }
        }
        return match;
    }

    /**
     * 判断在多个网元名称中是否有符合的网元名称，只要有一个符合就返回true
     * 
     * @param neName neName
     * @param host host
     * @return boolean
     */
    public static boolean matchNeNames(String neName, String host) {
        boolean match = false;
        Object[] nes = StringAnalyse.wipeRepeat(StringAnalyse.stringToArray(host.trim(), ",", "", "", true));
        for (int i = 0; i < nes.length; i++) {
            if (nes[i].equals(neName) || neName == nes[i]) {
                match = true;
                break;
            }
        }
        return match;
    }

    /**
     * 判断告警消息中是否含有regexp表达式匹配内容
     * 
     * @param regExp regExp
     * @param summary summary
     * @return boolean
     */
    public static boolean matchIncludeMessage(String regExp, String summary) {
        if (regExp == null || summary == null) {
            return false;
        }
        Pattern p = Pattern.compile(regExp, Pattern.DOTALL);
        return p.matcher(summary).find();
    }

    /**
     * 替换字符串
     * 
     * @param regEx 正则表达式
     * @param str 源字符串
     * @param replaceStr 指定字符串被替换成的字符串
     * @return 被替换完成的字符串
     * @throws PatternSyntaxException PatternSyntaxException
     */
    public static String stringReplace(String regEx, String str, String replaceStr) throws PatternSyntaxException {
        // 字母和数字
        // String regEx = "[^a-zA-Z0-9]";
        // 所有特殊字符
        // String regEx =
        // "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll(replaceStr).trim();
    }
}
