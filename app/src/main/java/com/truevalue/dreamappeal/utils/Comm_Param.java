package com.truevalue.dreamappeal.utils;

public class Comm_Param {
    public static final String APP_NAME = "DreamAppal";
    public static final String URL_API = "http://ec2-15-164-168-185.ap-northeast-2.compute.amazonaws.com:8080";
    public static final String URL_API_PROCESS = URL_API + "/process";
    public static final String URL_API_PROCESS_SIGNIN = URL_API_PROCESS + "/signIn";
    public static final String URL_API_PROCESS_SIGNUP = URL_API_PROCESS + "/signUp";

    /**
     * POST : 회원 가입
     * GET : 회원 목록 조회
     * PATCH : 회원 정보 변경
     */
    public static final String URL_API_MEMBERS = URL_API + "/members";
}
