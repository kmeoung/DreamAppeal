package com.truevalue.dreamappeal.bean;

public class BeanBlueprintAbilityOpportunity {

    private int profile_idx;
    private int idx;
    private String contents;

//    abilities: [
//    {
//        profile_idx: number,
//                idx: number,
//            ability: string
//    }, ...
//            ],
//    opportunities: [
//    {
//        profile_idx: number,
//                idx: number,
//            opportunity: string
//    }, ...
//            ],


    public BeanBlueprintAbilityOpportunity(int profile_idx, int idx, String contents) {
        this.profile_idx = profile_idx;
        this.idx = idx;
        this.contents = contents;
    }

    public int getProfile_idx() {
        return profile_idx;
    }

    public void setProfile_idx(int profile_idx) {
        this.profile_idx = profile_idx;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
