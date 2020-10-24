package com.site.jwt.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;

public class ShiroUtil {
    //这个后续写到工具类上
    public static String MD5Pwd(String username, String pwd) {
        // 加密算法MD5
        // salt盐 username + salt
        // 迭代次数
        String md5Pwd = new SimpleHash("MD5", pwd,
                ByteSource.Util.bytes(username + "salt"), 2).toHex();
        return md5Pwd;
    }


    //测试用户 username：root; pwd：root 加密后为cf66e4783ae3d28612c4bd49d75181a9 ;
    public static void main(String[] args) {
        System.out.println(ShiroUtil.MD5Pwd("root", "root"));
    }
}
