package cn.finalteam.toolsfinal;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Desction:String工具类
 * Author:pengjianbo
 * Date:15/9/17 下午4:22
 */
public class StringUtils {

    /**
     * is null or its length is 0 or it is made by space
     *
     * <pre>
     * isBlank(null) = true;
     * isBlank(&quot;&quot;) = true;
     * isBlank(&quot;  &quot;) = true;
     * isBlank(&quot;a&quot;) = false;
     * isBlank(&quot;a &quot;) = false;
     * isBlank(&quot; a&quot;) = false;
     * isBlank(&quot;a b&quot;) = false;
     * </pre>
     *
     * @param str
     * @return if string is null or its size is 0 or it is made by space, return true, else return false.
     */
    public static boolean isBlank(String str) {
        return (str == null || str.trim().length() == 0);
    }

    /**
     * is null or its length is 0
     *
     * <pre>
     * isEmpty(null) = true;
     * isEmpty(&quot;&quot;) = true;
     * isEmpty(&quot;  &quot;) = false;
     * </pre>
     *
     * @param str
     * @return if string is null or its size is 0, return true, else return false.
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0) && isBlank(str);
    }

    /**
     * get length of CharSequence
     *
     * <pre>
     * length(null) = 0;
     * length(\"\") = 0;
     * length(\"abc\") = 3;
     * </pre>
     *
     * @param str
     * @return if str is null or empty, return 0, else return {@link CharSequence#length()}.
     */
    public static int length(CharSequence str) {
        return str == null ? 0 : str.length();
    }

    /**
     * null Object to empty string
     *
     * <pre>
     * nullStrToEmpty(null) = &quot;&quot;;
     * nullStrToEmpty(&quot;&quot;) = &quot;&quot;;
     * nullStrToEmpty(&quot;aa&quot;) = &quot;aa&quot;;
     * </pre>
     *
     * @param str
     * @return
     */
    public static String nullStrToEmpty(Object str) {
        return (str == null ? "" : (str instanceof String ? (String)str : str.toString()));
    }

    /**
     * capitalize first letter
     *
     * <pre>
     * capitalizeFirstLetter(null)     =   null;
     * capitalizeFirstLetter("")       =   "";
     * capitalizeFirstLetter("2ab")    =   "2ab"
     * capitalizeFirstLetter("a")      =   "A"
     * capitalizeFirstLetter("ab")     =   "Ab"
     * capitalizeFirstLetter("Abc")    =   "Abc"
     * </pre>
     *
     * @param str
     * @return
     */
    public static String capitalizeFirstLetter(String str) {
        if (isEmpty(str)) {
            return str;
        }

        char c = str.charAt(0);
        return (!Character.isLetter(c) || Character.isUpperCase(c)) ? str : new StringBuilder(str.length())
                .append(Character.toUpperCase(c)).append(str.substring(1)).toString();
    }

    /**
     * encoded in utf-8
     *
     * <pre>
     * utf8Encode(null)        =   null
     * utf8Encode("")          =   "";
     * utf8Encode("aa")        =   "aa";
     * utf8Encode("啊啊啊啊")   = "%E5%95%8A%E5%95%8A%E5%95%8A%E5%95%8A";
     * </pre>
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException if an error occurs
     */
    public static String utf8Encode(String str) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("UnsupportedEncodingException occurred. ", e);
            }
        }
        return str;
    }

    /**
     * encoded in utf-8, if exception, return defultReturn
     *
     * @param str
     * @param defultReturn
     * @return
     */
    public static String utf8Encode(String str, String defultReturn) {
        if (!isEmpty(str) && str.getBytes().length != str.length()) {
            try {
                return URLEncoder.encode(str, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                return defultReturn;
            }
        }
        return str;
    }

    /**
     * get innerHtml from href
     *
     * <pre>
     * getHrefInnerHtml(null)                                  = ""
     * getHrefInnerHtml("")                                    = ""
     * getHrefInnerHtml("mp3")                                 = "mp3";
     * getHrefInnerHtml("&lt;a innerHtml&lt;/a&gt;")                    = "&lt;a innerHtml&lt;/a&gt;";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a&lt;a&gt;innerHtml&lt;/a&gt;")                    = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com"&gt;innerHtml&lt;/a&gt;")               = "innerHtml";
     * getHrefInnerHtml("&lt;a href="baidu.com" title="baidu"&gt;innerHtml&lt;/a&gt;") = "innerHtml";
     * getHrefInnerHtml("   &lt;a&gt;innerHtml&lt;/a&gt;  ")                           = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                      = "innerHtml";
     * getHrefInnerHtml("jack&lt;a&gt;innerHtml&lt;/a&gt;&lt;/a&gt;")                  = "innerHtml";
     * getHrefInnerHtml("&lt;a&gt;innerHtml1&lt;/a&gt;&lt;a&gt;innerHtml2&lt;/a&gt;")        = "innerHtml2";
     * </pre>
     *
     * @param href
     * @return <ul>
     *         <li>if href is null, return ""</li>
     *         <li>if not match regx, return source</li>
     *         <li>return the last string that match regx</li>
     *         </ul>
     */
    public static String getHrefInnerHtml(String href) {
        if (isEmpty(href)) {
            return "";
        }

        String hrefReg = ".*<[\\s]*a[\\s]*.*>(.+?)<[\\s]*/a[\\s]*>.*";
        Pattern hrefPattern = Pattern.compile(hrefReg, Pattern.CASE_INSENSITIVE);
        Matcher hrefMatcher = hrefPattern.matcher(href);
        if (hrefMatcher.matches()) {
            return hrefMatcher.group(1);
        }
        return href;
    }

    /**
     * process special char in html
     *
     * <pre>
     * htmlEscapeCharsToString(null) = null;
     * htmlEscapeCharsToString("") = "";
     * htmlEscapeCharsToString("mp3") = "mp3";
     * htmlEscapeCharsToString("mp3&lt;") = "mp3<";
     * htmlEscapeCharsToString("mp3&gt;") = "mp3\>";
     * htmlEscapeCharsToString("mp3&amp;mp4") = "mp3&mp4";
     * htmlEscapeCharsToString("mp3&quot;mp4") = "mp3\"mp4";
     * htmlEscapeCharsToString("mp3&lt;&gt;&amp;&quot;mp4") = "mp3\<\>&\"mp4";
     * </pre>
     *
     * @param source
     * @return
     */
    public static String htmlEscapeCharsToString(String source) {
        return StringUtils.isEmpty(source) ? source : source.replaceAll("&lt;", "<").replaceAll("&gt;", ">")
                .replaceAll("&amp;", "&").replaceAll("&quot;", "\"");
    }

    /**
     * transform half width char to full width char
     *
     * <pre>
     * fullWidthToHalfWidth(null) = null;
     * fullWidthToHalfWidth("") = "";
     * fullWidthToHalfWidth(new String(new char[] {12288})) = " ";
     * fullWidthToHalfWidth("！＂＃＄％＆) = "!\"#$%&";
     * </pre>
     *
     * @param s
     * @return
     */
    public static String fullWidthToHalfWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == 12288) {
                source[i] = ' ';
                // } else if (source[i] == 12290) {
                // source[i] = '.';
            } else if (source[i] >= 65281 && source[i] <= 65374) {
                source[i] = (char)(source[i] - 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * transform full width char to half width char
     *
     * <pre>
     * halfWidthToFullWidth(null) = null;
     * halfWidthToFullWidth("") = "";
     * halfWidthToFullWidth(" ") = new String(new char[] {12288});
     * halfWidthToFullWidth("!\"#$%&) = "！＂＃＄％＆";
     * </pre>
     *
     * @param s
     * @return
     */
    public static String halfWidthToFullWidth(String s) {
        if (isEmpty(s)) {
            return s;
        }

        char[] source = s.toCharArray();
        for (int i = 0; i < source.length; i++) {
            if (source[i] == ' ') {
                source[i] = (char)12288;
                // } else if (source[i] == '.') {
                // source[i] = (char)12290;
            } else if (source[i] >= 33 && source[i] <= 126) {
                source[i] = (char)(source[i] + 65248);
            } else {
                source[i] = source[i];
            }
        }
        return new String(source);
    }

    /**
     * 数据库字符转义
     * @param keyWord
     * @return
     */
    public static String sqliteEscape(String keyWord){
        keyWord = keyWord.replace("/", "//");
        keyWord = keyWord.replace("'", "''");
        keyWord = keyWord.replace("[", "/[");
        keyWord = keyWord.replace("]", "/]");
        keyWord = keyWord.replace("%", "/%");
        keyWord = keyWord.replace("&","/&");
        keyWord = keyWord.replace("_", "/_");
        keyWord = keyWord.replace("(", "/(");
        keyWord = keyWord.replace(")", "/)");
        return keyWord;
    }

    /**
     *
     * @param inputText
     * @return
     */
    public static boolean isPhone(String inputText) {
        String mobileRegex = "^1[3|4|5|7|8][0-9]\\d{8}$";
        Pattern p = Pattern.compile(mobileRegex);
        Matcher m = p.matcher(inputText);
        return m.matches();
    }

    /**
     * 中国电信号段 133、149、153、173、177、180、181、189、199
     * 中国联通号段 130、131、132、145、155、156、166、175、176、185、186
     * 中国移动号段 134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188、198
     * 其他号段
     * 14号段以前为上网卡专属号段，如中国联通的是145，中国移动的是147等等。
     * 虚拟运营商
     * 电信：1700、1701、1702
     * 移动：1703、1705、1706
     * 联通：1704、1707、1708、1709、171
     * 卫星通信：1349
     *
     * @param phone
     * @return
     */
    public static boolean isMobilePhone(String phone) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if (phone.length() != 11) {
            return false;
        } else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            if (!isMatch) {
            }
            return isMatch;
        }
    }

    /**
     * 匹配1开头11位的手机号
     * @param phone
     * @return
     */
    public static boolean isMobilePhoneSimple(String phone) {
        Pattern p = Pattern.compile("[1]\\d{10}"); //"[1]"代表第1位为数字1，"\\d{10}"代表后面是可以是0～9的数字，有10位。
        Matcher matcher = p.matcher(phone);
        return matcher.matches();

    }


    public static String getSecretPhone(String phoneNumber) {

        if (isMobilePhoneSimple(phoneNumber)) {
            return phoneNumber.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
        } else {
            return phoneNumber;
        }
    }

    public static boolean hasValue(String str) {
        return str != null && !"".equals(str.trim());
    }

    public static boolean isNumber(String text) {
        if (hasValue(text)) {
            Pattern pattern = Pattern.compile("^[0-9]*$");
            Matcher matcher = pattern.matcher(text);
            return matcher.matches();
        } else {
            return false;
        }
    }

    public static boolean isValidEmail(String mail) {
//       ^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+");
        Matcher mc = pattern.matcher(mail);
        return mc.matches();
    }

    public static <T> Boolean isListEmpty(List<T> objects) {
        return objects != null && (objects == null || objects.size() != 0) ? Boolean.valueOf(false) : Boolean.valueOf(true);
    }

    public static String formatDouble(double d) {

        if (d < 1) {
            int distance = (int) (d * 1000);
            return distance + "m";
        } else {
            return String.format("%.2f", d) + "km";
        }
    }

    public static String getSubString(String str, int subLength) {
        String temp = "";
        if (str.length() > subLength) {
            temp = str.substring(0, subLength - 1) + "...";
        } else {
            temp = str;
        }
        return temp;
    }

    public static void setPositionColor(TextView tv, String content, int start, int end, int color) {
        SpannableStringBuilder style = new SpannableStringBuilder(content);
        //str代表要显示的全部字符串
        style.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //start:代表从第几个字符开始变颜色，注意第一个字符序号是０．
        //end:代表变色到第几个字符．
        tv.setText(style);
    }
    public static void setPositionSizeColor(TextView tv, String content, int start, int end, int color) {
        SpannableStringBuilder style = new SpannableStringBuilder(content);
        //str代表要显示的全部字符串
        style.setSpan(new ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new AbsoluteSizeSpan(18,true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //第二个参数boolean dip，如果为true，表示前面的字体大小单位为dip，否则为像素，同上。
        style.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //粗体
        //start:代表从第几个字符开始变颜色，注意第一个字符序号是０．
        //end:代表变色到第几个字符．
        tv.setText(style);
    }

    public static SpannableString setKeyWords4Color(Context context, SpannableString spannableString, String keywords, int color) {
        if (keywords.startsWith("$")) {
            keywords = "\\" + keywords;
        }
        if (keywords.contains("****")) {
            keywords = keywords.replace("****", "\\*\\*\\*\\*");
        }

        Matcher emotions = Pattern.compile(keywords).matcher(spannableString);

        while (emotions.find()) {
            if (color != 0) {
                spannableString.setSpan(new ForegroundColorSpan(color), emotions.start(), emotions.end(), 33);
            }
        }
        return spannableString;
    }

    public static SpannableString setKeyWords4ColorOne(Context context, SpannableString spannableString, String keywords, int color) {
        if (keywords.startsWith("$")) {
            keywords = "\\" + keywords;
        }
        if (keywords.contains("****")) {
            keywords = keywords.replace("****", "\\*\\*\\*\\*");
        }

        Matcher emotions = Pattern.compile(keywords).matcher(spannableString);
        if (emotions.find()) {
            if (color != 0) {
                spannableString.setSpan(new ForegroundColorSpan(color), emotions.start(), emotions.end(), 33);
            }
        }
        return spannableString;
    }

    /**
     * 设置特殊字符颜色
     *
     * @param tv：TextView控件
     * @param strResource：R.String.register
     * @param keyWord:关键字
     * @param color：getResouce.getColor(R.color.bluse)
     */
    public static void setSpecialText(Context context, TextView tv, int strResource, String keyWord, int color) {
        String content = String.format(context.getResources().getString(strResource), keyWord);
        SpannableString spannable = new SpannableString(content);
        SpannableString resultSpannable = setKeyWords4Color(context, spannable, keyWord, color);
        tv.setText(resultSpannable);
    }

    /**
     * 设置特殊字符颜色
     *
     * @param tv：TextView控件
     * @param keyWord:关键字
     * @param color：getResouce.getColor(R.color.bluse)
     */
    public static void setSpecialText(Context context, TextView tv, String myContent, String keyWord, int color) {
        String content = String.format(myContent, keyWord);
        SpannableString spannable = new SpannableString(content);
        SpannableString resultSpannable = setKeyWords4Color(context, spannable, keyWord, color);
        tv.setText(resultSpannable);
    }

    public static void setSpecialText_Score(Context context, TextView tv, String myContent, String keyWord, int color) {
        String content = String.format(myContent, keyWord);
        SpannableString spannable = new SpannableString(content);
        SpannableString resultSpannable = setKeyWords4ColorOne(context, spannable, keyWord, color);
        tv.setText(resultSpannable);
    }

    /**
     * 获取资源文件格式化的string字符串
     *
     * @param stringResId
     * @param args
     * @return
     */
    public static String getStringFormat(Context context,int stringResId, Object... args) {
        return String.format(context.getString(stringResId), args);
    }

    /**
     * null转化为""
     *
     * @param s
     * @return
     */
    public static String null2String(String s) {
        return TextUtils.isEmpty(s) ? "" : s;
    }

    /**
     * 获得某个子字符串的个数
     *
     * @param src  源字符串
     * @param find 要查找的字符串
     * @return 要查找的字符串个数
     */
    public static int getOccur(String src, String find) {
        int o = 0;
        int index = -1;
        while ((index = src.indexOf(find, index)) > -1) {
            ++index;
            ++o;
        }
        return o;
    }
}
