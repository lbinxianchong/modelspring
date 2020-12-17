package com.lbin.common.config;

public class EncryptConfig {
    /** jwt秘钥 */
    public static String secret = "adf001ce00eb4d809eb3a18511ffd77d";

    /** 权限模式-路径拦截 */
    public static boolean patternPath = false;

    /** 权限模式-注解拦截 */
    public static boolean patternAnno = true;
}
