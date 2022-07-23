package me.bemayor.api.common;

import org.apache.commons.lang.Validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum CommonPatterns {
    //字符串的正则判断：数字、十六进制、正整数、Email地址、无线路由SSID和密码
    NUMERIC("-?[0-9]+(\\.[0-9]+)?"),
    HEXADECIMAL("^[0-9A-F]+$"),
    ZEROINTEGER("[+-]{0,1}0"),//"[0-9]*$"
    POSITIVEINTEGER("^\\+{0,1}[1-9]\\d*"),//"^[0-9]*[1-9][0-9]*$"
    NEGATIVEINTEGER("^-[1-9]\\d*"),
    DECIMAL("[-+]{0,1}\\d+\\.\\d*|[-+]{0,1}\\d*\\.\\d+"), //"^[0-9]+(.[0-9]+)?$"
    POSITIVEDECIMAL("\\+{0,1}[0]\\.[1-9]*|\\+{0,1}[1-9]\\d*\\.\\d*"),
    NEGATIVEDECIMAL("^-[0]\\.[1-9]*|^-[1-9]\\d*\\.\\d*"),
    EMAIL("^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$"),
    SSID("^[A-Za-z]+[\\w\\-\\:\\.]*$");

    private final String pattern;

    CommonPatterns(String pattern) {
        Validate.notNull(pattern, "Pattern cannot be null");
        this.pattern = pattern;
    }

    /**
     * This returns the texture hash for this particular head.
     *
     * @return The associated texture hash
     */
    public Matcher matcher(String str) {
        return Pattern.compile(pattern).matcher(str);
    }

}
