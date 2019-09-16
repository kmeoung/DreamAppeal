package com.truevalue.dreamappeal.utils;

public class Comm_Param {
    public static final boolean IS_TEST = true;

    public static final String APP_NAME = "DreamAppal";
    public static final String URL_API = "http://ec2-15-164-168-185.ap-northeast-2.compute.amazonaws.com:8080";
    public static final String URL_API_USERS = URL_API + "/users";

    // 검색
    public static final String URL_SEARCH = URL_API + "/search";

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
    // Ability Index
    public static final String ABILITY_INDEX = "ABILITY_INDEX";
    // Opprotunity Index
    public static final String OPPORTUNITY_INDEX = "OPPORTUNITY_INDEX";
    // Objects Index
    public static final String OBJECT_INDEX = "OBJECT_INDEX";
    // Steps Index
    public static final String STEPS_INDEX = "STEPS_INDEX";


    // 내 꿈 소개 조회 / 내 꿈 소개 업데이트 / 프로필 삭제
    public static final String URL_API_PROFILES_INDEX = URL_API_PROFILES + "/" + PROFILES_INDEX;
    // 내 꿈 목록 조회
    public static final String URL_API_PROFILES_INDEX_LIST = URL_API_PROFILES + "/" + USER_INDEX + "/list";

    // 실현성과 등록
    public static final String URL_API_ACHIVEMENT_POSTS = URL_API_PROFILES_INDEX + "/achievement_posts";
    // 실현성과 조회
    public static final String URL_API_ACHIVEMENT_POSTS_MAIN = URL_API_ACHIVEMENT_POSTS + "/main";
    public static final String URL_API_ACHIVEMENT_POSTS_MAIN_INDEX = URL_API_ACHIVEMENT_POSTS_MAIN + "/" + POST_INDEX;
    // 실현성과 / 대표성과 상세 조회 / 수정 / 삭제
    public static final String URL_API_ACHIVEMENT_POSTS_INDEX = URL_API_ACHIVEMENT_POSTS + "/" + POST_INDEX;

    public static final String URL_API_ACHIVEMENT_BEST_POST = URL_API_ACHIVEMENT_POSTS + "/best_post";
    // 대표 성과 내리기
    public static final String URL_API_ACHIVEMENT_BEST_POST_INDEX = URL_API_ACHIVEMENT_BEST_POST + "/" + BEST_POST_INDEX;
    // 대표 성과 등록
    public static final String URL_API_ACHIVEMENT_BEST_POST_INDEX_POST_INDEX = URL_API_ACHIVEMENT_BEST_POST_INDEX + "/" + POST_INDEX;

    // 발전 계획
    public static final String URL_API_BLUEPRINT = URL_API_PROFILES_INDEX + "/blueprint";
    // 갖출 능력 조회 / 등록
    public static final String URL_API_BLUEPRINT_ABILITIES = URL_API_BLUEPRINT + "/abilities";
    // 갖출 능력 수정 / 삭제
    public static final String URL_API_BLUEPRINT_ABILITIES_INDEX = URL_API_BLUEPRINT_ABILITIES + "/" + ABILITY_INDEX;
    // 만들고픈 기회 조회 / 등록
    public static final String URL_API_BLUEPRINT_OPPORTUNITIES = URL_API_BLUEPRINT + "/opportunities";
    // 만들고픈 기회 수정 / 삭제
    public static final String URL_API_BLUEPRINT_OPPORTUNITIES_INDEX = URL_API_BLUEPRINT_OPPORTUNITIES + "/" + OPPORTUNITY_INDEX;
    // 실천 목표 추가
    public static final String URL_API_BLUEPRINT_OBJECTS = URL_API_BLUEPRINT + "/objects";
    // 실천 목표 조회 / 수정 / 삭제
    public static final String URL_API_BLUEPRINT_OBJECTS_INDEX = URL_API_BLUEPRINT_OBJECTS + "/" + OBJECT_INDEX;
    // 실천 목표 세부단계 추가
    public static final String URL_API_BLUEPRINT_OBJECTS_INDEX_STEPS = URL_API_BLUEPRINT_OBJECTS_INDEX + "/steps";
    // 실천 목표 세부단계 수정 / 삭제
    public static final String URL_API_BLUEPRINT_OBJECTS_INDEX_STEPS_INDEX = URL_API_BLUEPRINT_OBJECTS_INDEX_STEPS + "/" + STEPS_INDEX;


    /**
     * POST : 회원 가입
     * GET : 회원 목록 조회
     * PATCH : 회원 정보 변경
     */
    public static final String URL_API_MEMBERS = URL_API + "/members";
}
