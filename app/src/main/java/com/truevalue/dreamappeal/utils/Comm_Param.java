package com.truevalue.dreamappeal.utils;

public class Comm_Param {
    public static final boolean IS_TEST = false;

    public static final String APP_NAME = "DreamAppal";
    public static final String URL_API = "http://ec2-15-164-168-185.ap-northeast-2.compute.amazonaws.com:8080";
    public static final String URL_API_USERS = URL_API + "/users";

    public static final String URL_API_CHECK = URL_API_USERS + "/check";
    public static final String URL_API_CHECK_EMAIL = URL_API_CHECK + "/email";

    public static final String URL_API_USERS_TOKENS = URL_API_USERS + "/tokens";
    public static final String URL_API_USERS_SIGNUP = URL_API_USERS + "/signup";

    // 내 꿈 소개 등록
    public static final String URL_API_PROFILES = URL_API + "/profiles";
    // 프로필 인덱스
    public static final String PROFILES_INDEX = "PROFILES_INDEX";
    // user Index
    public static final String USER_INDEX = "USER_INDEX";
    // post Index
    public static final String POST_INDEX = "POST_INDEX";
    // Best Post Index
    public static final String BEST_POST_INDEX = "BEST_POST_INDEX";

    // 내 꿈 소개 조회 / 내 꿈 소개 업데이트 / 프로필 삭제
    public static final String URL_API_PROFILES_INDEX = URL_API_PROFILES + "/" + PROFILES_INDEX;
    // 내 꿈 목록 조회
    public static final String URL_API_PROFILES_INDEX_LIST = URL_API_PROFILES + "/" + USER_INDEX + "/list";
    // 실현성과 등록
    public static final String URL_API_PROFILES_INDEX_ACHIVEMENT_POSTS = URL_API_PROFILES_INDEX + "/achievement_posts";
    // 실현성과 조회
    public static final String URL_API_PROFILES_INDEX_ACHIVEMENT_POSTS_MAIN = URL_API_PROFILES_INDEX_ACHIVEMENT_POSTS + "/main";
    public static final String URL_API_PROFILES_INDEX_ACHIVEMENT_POSTS_MAIN_INDEX = URL_API_PROFILES_INDEX_ACHIVEMENT_POSTS_MAIN + "/" + POST_INDEX;
    // 실현성과 / 대표성과 상세 조회 / 수정 / 삭제
    public static final String URL_API_PROFILES_INDEX_ACHIVEMENT_POSTS_INDEX = URL_API_PROFILES_INDEX_ACHIVEMENT_POSTS + "/" + POST_INDEX;
    // 대표 성과 등록
    public static final String URL_API_PROFILES_INDEX_ACHIVEMENT_BEST_POST = URL_API_PROFILES_INDEX_ACHIVEMENT_POSTS + "/best_post";
    public static final String URL_API_PROFILES_INDEX_ACHIVEMENT_BEST_POST_INDEX = URL_API_PROFILES_INDEX_ACHIVEMENT_BEST_POST + "/" + BEST_POST_INDEX;
    public static final String URL_API_PROFILES_INDEX_ACHIVEMENT_BEST_POST_INDEX_POST_INDEX = URL_API_PROFILES_INDEX_ACHIVEMENT_BEST_POST_INDEX + "/" + POST_INDEX;
    // 발전 계획 조회
    public static final String URL_API_PROFILES_INDEX_BLUEPRINT = URL_API_PROFILES_INDEX + "/blueprint";

    /**
     * POST : 회원 가입
     * GET : 회원 목록 조회
     * PATCH : 회원 정보 변경
     */
    public static final String URL_API_MEMBERS = URL_API + "/members";
}
