package com.truevalue.dreamappeal.fragment.profile;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.profile.ActivityBestAchivementDetail;
import com.truevalue.dreamappeal.activity.profile.ActivityAddAchivement;
import com.truevalue.dreamappeal.activity.profile.ActivityCommentDetail;
import com.truevalue.dreamappeal.activity.profile.ActivityRecentAchivementDetail;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseOkHttpClient;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;
import com.truevalue.dreamappeal.base.IOServerCallback;
import com.truevalue.dreamappeal.bean.BeanAchivementPostMain;
import com.truevalue.dreamappeal.utils.Comm_Param;
import com.truevalue.dreamappeal.utils.Comm_Prefs;
import com.truevalue.dreamappeal.utils.Utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class FragmentPerformance extends BaseFragment implements IORecyclerViewListener {

    private static final int TYPE_LIST_HEADER = 0;
    private static final int TYPE_LIST_OTHER = 1;

    @BindView(R.id.rv_dream_description)
    RecyclerView mRvRecycle;

    private BaseRecyclerViewAdapter mAdapter;
    private ViewPagerAdapter mPagerAdapter;

    private int mCurrentPage = 0;
    private int mCurrentIndex = 0;
    private int mTotalPage = 0;
    private String mProfileImage = null;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_performance, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter();
//        bindTempData();
        httpGetAchivementPostMain();
    }

    /**
     * 실현 성과 조회
     */
    private void httpGetAchivementPostMain(){
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_PROFILES_INDEX_ACHIVEMENT_POSTS_MAIN_INDEX;
        url = url.replaceAll(Comm_Param.PROFILES_INDEX,String.valueOf(prefs.getProfileIndex()));
        url = url.replaceAll(Comm_Param.POST_INDEX,String.valueOf(mCurrentIndex));
        HashMap header = Utils.getHttpHeader(prefs.getToken());

        BaseOkHttpClient client = new BaseOkHttpClient();
        mAdapter.clear();
        mAdapter.add("실현성과 테스트");
        client.Get(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

                JSONObject object = new JSONObject(body);
                mProfileImage = object.getString("profile_image");

//                best_posts: {
//                    best_post_1: {
//                        profile_idx: number,
//                                idx: number,
//                                title: string
//                    },
//                    best_post_2: REFERENCE best_post_1,
//                    best_post_3: REFERENCE best_post_1,
//                }

                JSONObject bestPost = object.getJSONObject("best_posts");
                JSONArray array = object.getJSONArray("achievement_posts");
                Gson gson = new Gson();
                for (int i = 0; i < array.length(); i++) {
                    BeanAchivementPostMain bean = gson.fromJson(array.getJSONObject(i).toString(), BeanAchivementPostMain.class);
                    mAdapter.add(bean);
                }
            }
        });

    }

    private void initAdapter() {
        mPagerAdapter = new ViewPagerAdapter(getContext());

        mAdapter = new BaseRecyclerViewAdapter(getContext(), this);
        mRvRecycle.setAdapter(mAdapter);
        mRvRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void bindTempData() {
        mAdapter.add("대표성과");
        for (int i = 0; i < 10; i++) {
            mAdapter.add("");
        }
    }

    /**
     * BaseRecyclerView Interface
     */

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_LIST_HEADER) {
            return BaseViewHolder.newInstance(R.layout.listitem_header_achivement, parent, false);
        } else {
            return BaseViewHolder.newInstance(R.layout.listitem_achivement_post, parent, false);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder h, int i) {
        if (getItemViewType(i) == TYPE_LIST_HEADER) {
            onBindViewHeader(h, i);
        } else if (getItemViewType(i) == TYPE_LIST_OTHER) {
            onBindViewOther(h, i);
        }
    }

    /**
     * Header ViewPager 대표성과
     *
     * @param h
     * @param i
     */
    private void onBindViewHeader(BaseViewHolder h, int i) {
        ViewPager pager = h.getItemView(R.id.vp_pager);
        pager.setAdapter(mPagerAdapter);
        ImageView ivAddAchivement = h.getItemView(R.id.iv_add_achivement);

        ivAddAchivement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityAddAchivement.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 최근 성과
     *
     * @param h
     * @param i
     */
    private void onBindViewOther(BaseViewHolder h, int i) {
        BeanAchivementPostMain bean = (BeanAchivementPostMain) mAdapter.get(i);

        ImageView ivThumbnail = h.getItemView(R.id.iv_thumbnail);
        TextView tvTitle = h.getItemView(R.id.tv_title);
        TextView tvContents = h.getItemView(R.id.tv_contents);
        ImageView ivProfile = h.getItemView(R.id.iv_profile);
        ImageButton ibtnMore = h.getItemView(R.id.ibtn_more);

        // View Resize (화면 크기에 맞춰 정사각형으로 맞춤)
        Point size = Utils.getDisplaySize(getActivity());
        Utils.setResizeView(ivThumbnail,size.x,size.x);

        tvTitle.setText(bean.getTitle());
        tvContents.setText(bean.getContent());

        if(TextUtils.isEmpty(bean.getThumbnail_image())) Glide.with(getContext()).load(R.drawable.blue_box).into(ivThumbnail);
        else Glide.with(getContext()).load(bean.getThumbnail_image()).thumbnail(R.drawable.blue_box).into(ivThumbnail);

        if(TextUtils.isEmpty(mProfileImage)) Glide.with(getContext()).load(R.drawable.drawer_user).into(ivProfile);
        else Glide.with(getContext()).load(bean.getThumbnail_image()).thumbnail(R.drawable.drawer_user).into(ivProfile);

        ibtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo : 임시로 대표성과 등록 (팝업 띄워서 수정 삭제 띄워야 함)
                httpPostAddBestAchivement(1,bean);
            }
        });

        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityRecentAchivementDetail.class);
                startActivity(intent);
            }
        });

        LinearLayout llComment = h.getItemView(R.id.ll_comment);
        llComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityCommentDetail.class);
                startActivity(intent);
            }
        });

    }

    /**
     * 대표성과 등록
     * @param best_achivement_idx
     * @param bean
     */
    private void httpPostAddBestAchivement(int best_achivement_idx,BeanAchivementPostMain bean){

        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());

        String url = Comm_Param.URL_API_PROFILES_INDEX_ACHIVEMENT_BEST_POST_INDEX_POST_INDEX;
        url = url.replaceAll(Comm_Param.BEST_POST_INDEX,String.valueOf(best_achivement_idx));
        url = url.replaceAll(Comm_Param.POST_INDEX,String.valueOf(bean.getIdx()));
        url = url.replaceAll(Comm_Param.PROFILES_INDEX,String.valueOf(prefs.getProfileIndex()));

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        BaseOkHttpClient client = new BaseOkHttpClient();
        client.Post(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAdapter.size();
    }

    @Override
    public int getItemViewType(int i) {
        if (mAdapter.get(i) instanceof String) {
            return TYPE_LIST_HEADER;
        } else return TYPE_LIST_OTHER;
    }

    /**
     * ViewPager Adapter
     */
    public class ViewPagerAdapter extends PagerAdapter {

        private Context mContext;
        private LayoutInflater mInflater;

        public ViewPagerAdapter(Context context) {
            this.mContext = context;
            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            return 3; // 페이지 3개 고정
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @Override

        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = mInflater.inflate(R.layout.layout_achivement, container, false);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ActivityBestAchivementDetail.class);
                    startActivity(intent);
                }
            });
            container.addView(view);
            return view;
        }


    }
}
