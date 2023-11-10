package tech.wetech.admin3.controller;

import tech.wetech.admin3.common.SecurityUtil;

import java.security.NoSuchAlgorithmException;

/**
 * @program: admin3
 * @description: 用户凭证生成
 * @author: kinch.zhu
 * @create: 2023-11-10 22:29
 **/
public class UserCreate {

  public static void main(String[] args) throws NoSuchAlgorithmException {
    System.out.println(SecurityUtil.md5("guest", "guest"));
  }
}
