package com.truevalue.dreamappeal.utils;

public class Comm_Param {
    public static final String APP_NAME = "DreamAppal";
    public static final String URL_API = "http://ec2-15-164-168-185.ap-northeast-2.compute.amazonaws.com:8080";
    public static final String URL_API_USERS = URL_API + "/users";

    public static final String URL_API_CHECK = URL_API_USERS + "/check";
    public static final String URL_API_CHECK_EMAIL = URL_API_CHECK + "/email";

    public static final String URL_API_USERS_TOKENS = URL_API_USERS + "/tokens";
    public static final String URL_API_USERS_SIGNUP = URL_API_USERS + "/signup";

    // 내 꿈 소개 등록
    public static final String URL_API_PROFILES = URL_API + "/profiles";

    /**
     * POST : 회원 가입
     * GET : 회원 목록 조회
     * PATCH : 회원 정보 변경
     */
    public static final String URL_API_MEMBERS = URL_API + "/members";
}
