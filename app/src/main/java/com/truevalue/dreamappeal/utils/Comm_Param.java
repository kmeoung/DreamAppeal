package com.truevalue.dreamappeal.utils;

public class Comm_Param {
    // todo : 테스트 용
    public static final boolean IS_TEST = false;

    public static final String APP_NAME = "DreamAppal";
    public static final String URL_API = "http://ec2-15-164-168-185.ap-northeast-2.compute.amazonaws.com:8080";

    // 프로필 인덱스
    public static final String PROFILES_INDEX = "PROFILES_INDEX";
    // 내 프로필 인덱스
    public static final String MY_PROFILES_INDEX = "PROFILES_MY_INDEX";
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
    // Comment Index
    public static final String COMMENTS_INDEX = "COMMENTS_INDEX";

    public static final String URL_API_USERS = URL_API + "/users";
    // 회원 조회
    public static final String URL_API_USERS_INDEX = URL_API_USERS + "/" + PROFILES_INDEX;

    // 검색
    public static final String URL_SEARCH = URL_API + "/search";

    public static final String URL_API_CHECK = URL_API_USERS + "/check";
    public static final String URL_API_CHECK_EMAIL = URL_API_CHECK + "/email";

    public static final String URL_API_USERS_TOKENS = URL_API_USERS + "/tokens";
    public static final String URL_API_USERS_SIGNUP = URL_API_USERS + "/signup";
    // todo : 그냥 호출
    // 내 꿈 소개 등록
    public static final String URL_API_PROFILES = URL_API + "/profiles";
    // todo : 내 프로필도 호출
    // 내 꿈 소개 조회
    public static final String URL_API_PROFILES_INDEX_INDEX = URL_API_PROFILES + "/" + MY_PROFILES_INDEX + "/" + PROFILES_INDEX;
    // 내 꿈 소개 업데이트 / 프로필 삭제
    public static final String URL_API_PROFILES_INDEX = URL_API_PROFILES + "/" + PROFILES_INDEX;
    // 내 꿈 목록 조회
    public static final String URL_API_PROFILES_INDEX_LIST = URL_API_PROFILES + "/" + USER_INDEX + "/list";

    // 이미지 업로드 는 패치
    public static final String URL_API_PROFILE_INDEX_IMAGE  = URL_API_PROFILES_INDEX + "/image";

    // 실현 성과
    public static final String URL_API_MYPROFILEINDEX_ACHIVEMENT_POSTS = URL_API_PROFILES_INDEX_INDEX + "/achievement_posts";
    // 실현성과 조회
    public static final String URL_API_ACHIVEMENT_POSTS_MAIN = URL_API_MYPROFILEINDEX_ACHIVEMENT_POSTS + "/main";
    public static final String URL_API_INDEX_ACHIVEMENT_POSTS_MAIN_INDEX = URL_API_ACHIVEMENT_POSTS_MAIN + "/" + POST_INDEX;

    // 발전 계획 조회
    public static final String URL_API_PROFILE_BLUEPRINT = URL_API_PROFILES_INDEX_INDEX + "/blueprint";

    // 실천 인증 조회
    public static final String URL_API_PROFILES_INDEX_INDEX_ACTIONPOSTS = URL_API_PROFILES_INDEX_INDEX + "/action_posts";
    public static final String URL_API_PROFILES_INDEX_INDEX_ACTIONPOSTS_INDEX = URL_API_PROFILES_INDEX_INDEX_ACTIONPOSTS + "/" + POST_INDEX;

    // 내 꿈 소개 댓글 조회
    public static final String URL_API_PROFILES_INDEX_INDEX_PRESENTCOMMENTS = URL_API_PROFILES_INDEX_INDEX + "/present_comments";
    // 발전 댓글 조회
    public static final String URL_API_PROFILES_INDEX_INDEX_BLUEPRINTCOMMENTS = URL_API_PROFILES_INDEX_INDEX + "/blueprint_comments";
    // 실현 성과 댓글 조회
    public static final String URL_API_PROFILES_INDEX_INDEX_PERFORMANCE = URL_API_PROFILES_INDEX_INDEX + "/achievement_posts";
    public static final String URL_API_PROFILES_INDEX_INDEX_PERFORMANCE_INDEX_COMMENTS = URL_API_PROFILES_INDEX_INDEX_PERFORMANCE + "/" + POST_INDEX + "/comments";

    // 실천 인증 댓글 조회
    public static final String URL_API_PROFILES_INDEX_INDEX_ACTIONPOST = URL_API_PROFILES_INDEX_INDEX + "/action_posts";
    public static final String URL_API_PROFILES_INDEX_INDEX_ACTIONPOST_INDEX_COMMENTS = URL_API_PROFILES_INDEX_INDEX_ACTIONPOST + "/" + POST_INDEX + "/comments";

    // 실현성과 등록
    public static final String URL_API_ACHIVEMENT_POSTS = URL_API_PROFILES_INDEX + "/achievement_posts";
    public static final String URL_API_ACHIVEMENT_POST = URL_API_PROFILES_INDEX + "/achievement_post";
    // 실현성과 / 대표성과 상세 조회
    public static final String URL_API_MYPROFILEINDEX_ACHIVEMENT_POSTS_INDEX = URL_API_MYPROFILEINDEX_ACHIVEMENT_POSTS + "/" + POST_INDEX;

    // 실현성과 수정 / 삭제
    public static final String URL_API_ACHIVEMENT_POSTS_INDEX = URL_API_ACHIVEMENT_POSTS + "/" + POST_INDEX;
    public static final String URL_API_ACHIVEMENT_POST_INDEX = URL_API_ACHIVEMENT_POST + "/" + POST_INDEX;
    // 실현성과 좋아요
    public static final String URL_API_PROFILES_INDEX_PERFORMANCE_INDEX_LIKE_INDEX = URL_API_ACHIVEMENT_POST_INDEX + "/like/" + MY_PROFILES_INDEX;



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
    public static final String URL_API_PROFILE_BLUEPRINT_OBJECTS = URL_API_PROFILE_BLUEPRINT + "/objects";
    // 실천 목표 조회
    public static final String URL_API_PROFILE_BLUEPRINT_OBJECTS_INDEX = URL_API_PROFILE_BLUEPRINT_OBJECTS + "/" + OBJECT_INDEX;
    // 실천 목표 수정 / 삭제
    public static final String URL_API_BLUEPRINT_OBJECTS_INDEX = URL_API_BLUEPRINT_OBJECTS + "/" + OBJECT_INDEX;
    // 실천 목표 세부단계 추가
    public static final String URL_API_BLUEPRINT_OBJECTS_INDEX_STEPS = URL_API_BLUEPRINT_OBJECTS_INDEX + "/steps";
    // 실천 목표 세부단계 수정 / 삭제
    public static final String URL_API_BLUEPRINT_OBJECTS_INDEX_STEPS_INDEX = URL_API_BLUEPRINT_OBJECTS_INDEX_STEPS + "/" + STEPS_INDEX;

    // 실천 인증 추가
    public static final String URL_API_PROFILES_INDEX_ACTIONPOSTS = URL_API_PROFILES_INDEX + "/action_posts";
    public static final String URL_API_PROFILES_INDEX_ACTIONPOST = URL_API_PROFILES_INDEX + "/action_post";
    // 실천 인증 카테고리 조회
    public static final String URL_API_PROFILES_INDEX_ACTIONPOSTS_CATEGORY = URL_API_PROFILES_INDEX_ACTIONPOSTS + "/category";
    // 실천 인증 카테고리 세부사항 조회
    public static final String URL_API_PROFILES_INDEX_ACTIONPOSTS_CATEGORY_INDEX = URL_API_PROFILES_INDEX_ACTIONPOSTS_CATEGORY + "/" + OBJECT_INDEX;
    // 실천 인증 상세 수정 / 삭제
    public static final String URL_API_PROFILES_INDEX_ACTIONPOSTS_INDEX = URL_API_PROFILES_INDEX_ACTIONPOSTS + "/" + POST_INDEX;
    public static final String URL_API_PROFILES_INDEX_ACTIONPOST_INDEX = URL_API_PROFILES_INDEX_ACTIONPOST + "/" + POST_INDEX;
    // 실천인증 좋아요
    public static final String URL_API_PROFILES_INDEX_ACTIONPOST_INDEX_LIKE_INDEX = URL_API_PROFILES_INDEX_ACTIONPOST_INDEX + "/like/" + MY_PROFILES_INDEX;

    // 내 꿈 소개 댓글 추가
    public static final String URL_API_PROFILES_INDEX_PRESENTCOMMENTS = URL_API_PROFILES_INDEX + "/present_comments";
    // 내 꿈 소개 댓글 수정 / 삭제
    public static final String URL_API_PROFILES_INDEX_PRESENTCOMMENTS_INDEX = URL_API_PROFILES_INDEX_PRESENTCOMMENTS + "/" + COMMENTS_INDEX;
    // 내 꿈 소개 댓글 좋아요
    public static final String URL_API_PROFILES_INDEX_PRESENTCOMMENTS_INDEX_MYINDEX = URL_API_PROFILES_INDEX_PRESENTCOMMENTS_INDEX + "/like/" + MY_PROFILES_INDEX;

    // 발전계획 댓글 추가
    public static final String URL_API_PROFILES_INDEX_BLUEPRINTCOMMENTS = URL_API_PROFILES_INDEX + "/blueprint_comments";
    // 발전계획 댓글 수정 / 삭제
    public static final String URL_API_PROFILES_INDEX_BLUEPRINTCOMMENTS_INDEX = URL_API_PROFILES_INDEX_BLUEPRINTCOMMENTS + "/" + COMMENTS_INDEX;
    // 발전계획 댓글 좋아요
    public static final String URL_API_PROFILES_INDEX_BLUEPRINTCOMMENTS_INDEX_MYINDEX = URL_API_PROFILES_INDEX_BLUEPRINTCOMMENTS_INDEX + "/like/" + MY_PROFILES_INDEX;

    // 실현성과 댓글 추가
    public static final String URL_API_PROFILES_INDEX_PERFORMANCE= URL_API_PROFILES_INDEX + "/achievement_posts/" + POST_INDEX + "/comments";
    // 실현성과 댓글 수정 / 삭제
    public static final String URL_API_PROFILES_INDEX_PERFORMANCE_INDEX = URL_API_PROFILES_INDEX_PERFORMANCE + "/" + COMMENTS_INDEX;

    // 실천인증 댓글 추가
    public static final String URL_API_PROFILES_INDEX_ACTIONPOST_INDEX_COMMENTS = URL_API_PROFILES_INDEX + "/action_posts/" + POST_INDEX + "/comments";
    // 실천인증 댓글 수정 / 삭제
    public static final String URL_API_PROFILES_INDEX_ACTIONPOST_INDEX_COMMENTS_INDEX = URL_API_PROFILES_INDEX_ACTIONPOST + "/" + COMMENTS_INDEX;

    // 실현성과 댓글 좋아요
    public static final String URL_API_PROFILES_INDEX_PERFORMANCECOMMENTS= URL_API_PROFILES_INDEX + "/achievement_post_comments";
    public static final String URL_API_PROFILES_INDEX_PERFORMANCECOMMENTS_INDEX = URL_API_PROFILES_INDEX_PERFORMANCECOMMENTS + "/" + COMMENTS_INDEX;
    public static final String URL_API_PROFILES_INDEX_PERFORMANCECOMMENTS_INDEX_MYINDEX = URL_API_PROFILES_INDEX_PERFORMANCECOMMENTS_INDEX + "/like/" + MY_PROFILES_INDEX;

    // 실천인증 댓글 좋아요
    public static final String URL_API_PROFILES_INDEX_ACTIONPOSTCOMMENTS= URL_API_PROFILES_INDEX + "/action_post_comments";
    public static final String URL_API_PROFILES_INDEX_ACTIONPOSTCOMMENTS_INDEX = URL_API_PROFILES_INDEX_ACTIONPOSTCOMMENTS + "/" + COMMENTS_INDEX;
    public static final String URL_API_PROFILES_INDEX_ACTIONPOSTCOMMENTS_INDEX_MYINDEX = URL_API_PROFILES_INDEX_ACTIONPOSTCOMMENTS_INDEX + "/like/" + MY_PROFILES_INDEX;

    // 프로필 응원하기 / 응원하기 조회
    public static final String URL_API_PROFILES_INDEX_LIKE = URL_API_PROFILES_INDEX + "/like";
    public static final String URL_API_PROFILES_INDEX_LIKE_MYPROFILEINDEX = URL_API_PROFILES_INDEX_LIKE + "/" + MY_PROFILES_INDEX;

    /**
     * POST : 회원 가입
     * GET : 회원 목록 조회
     * PATCH : 회원 정보 변경
     */
    public static final String URL_API_MEMBERS = URL_API + "/members";
}
