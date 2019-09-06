package com.truevalue.dreamappeal.fragment.profile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.profile.ActivityAddAchivement;
import com.truevalue.dreamappeal.activity.profile.ActivityBestAchivementDetail;
import com.truevalue.dreamappeal.activity.profile.ActivityCommentDetail;
import com.truevalue.dreamappeal.activity.profile.ActivityRecentAchivementDetail;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseOkHttpClient;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;
import com.truevalue.dreamappeal.base.IOServerCallback;
import com.truevalue.dreamappeal.bean.BeanAchivementPostMain;
import com.truevalue.dreamappeal.bean.BeanBestPost;
import com.truevalue.dreamappeal.utils.Comm_Param;
import com.truevalue.dreamappeal.utils.Comm_Prefs;
import com.truevalue.dreamappeal.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static android.app.Activity.RESULT_OK;

public class FragmentPerformance extends BaseFragment implements IORecyclerViewListener {

    private static final int TYPE_LIST_OTHER = 0;

    private static final int REQUEST_ADD_RECENT_ACHIVEMENT = 1100;
    private static final int REQUEST_EDIT_RECENT_ACHIVEMENT = 1101;
    private static final int REQUEST_RECENT_ACHIVEMENT = 1102;
    private static final int TOP_BANNER_DELAY = 1000 * 4;

    @BindView(R.id.rv_dream_description)
    RecyclerView mRvRecycle;
    @BindView(R.id.vp_pager)
    ViewPager mVpPager;
    @BindView(R.id.iv_add_achivement)
    ImageView mIvAddAchivement;

    private BaseRecyclerViewAdapter mAdapter;
    private ViewPagerAdapter mPagerAdapter;

    private int mCurrentIndex = 0;
    private String mProfileImage = null;
    private boolean isViewCreated = false;
    private ArrayList<BeanBestPost> mBestPostList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_performance, container, false);
        ButterKnife.bind(this, view);
        isViewCreated = true;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mAdapter == null) {
            initAdapter();
        }
    }

    @Override
    public void onViewPaged(boolean isView) {
        if (isViewCreated) {
            if (isView) {
                httpGetAchivementPostMain();
                startPageRolling();
            } else {
                stopPageRolling();
            }
        }
        super.onViewPaged(isView);
    }

    /**
     * 실현 성과 조회
     */
    private void httpGetAchivementPostMain() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_PROFILES_INDEX_ACHIVEMENT_POSTS_MAIN_INDEX;
        url = url.replaceAll(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        url = url.replaceAll(Comm_Param.POST_INDEX, String.valueOf(mCurrentIndex));
        HashMap header = Utils.getHttpHeader(prefs.getToken());

        BaseOkHttpClient client = new BaseOkHttpClient();
        mAdapter.clear();
        mBestPostList.clear();
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

                JSONArray array = object.getJSONArray("achievement_posts");
                Gson gson = new Gson();
                for (int i = 0; i < array.length(); i++) {
                    BeanAchivementPostMain bean = gson.fromJson(array.getJSONObject(i).toString(), BeanAchivementPostMain.class);
                    mAdapter.add(bean);
                }

                JSONObject bestPosts = object.getJSONObject("best_posts");
                try {
                    JSONObject bestPost = bestPosts.getJSONObject("best_post_1");
                    BeanBestPost bean = gson.fromJson(bestPost.toString(), BeanBestPost.class);
                    mBestPostList.add(bean);
                } catch (JSONException e) {
                    e.printStackTrace();
                    mBestPostList.add(null);
                }

                try {
                    JSONObject bestPost = bestPosts.getJSONObject("best_post_2");
                    BeanBestPost bean = gson.fromJson(bestPost.toString(), BeanBestPost.class);
                    mBestPostList.add(bean);
                } catch (JSONException e) {
                    e.printStackTrace();
                    mBestPostList.add(null);
                }

                try {
                    JSONObject bestPost = bestPosts.getJSONObject("best_post_3");
                    BeanBestPost bean = gson.fromJson(bestPost.toString(), BeanBestPost.class);
                    mBestPostList.add(bean);
                } catch (JSONException e) {
                    e.printStackTrace();
                    mBestPostList.add(null);
                }
                mPagerAdapter.notifyDataSetChanged();
            }
        });

    }

    private void initAdapter() {

        mPagerAdapter = new ViewPagerAdapter(getContext());
        mAdapter = new BaseRecyclerViewAdapter(getContext(), this);
        mRvRecycle.setAdapter(mAdapter);
        mRvRecycle.setLayoutManager(new LinearLayoutManager(getContext()));

        mVpPager.setAdapter(mPagerAdapter);
        mVpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mVpPager.getAdapter().getCount() > 1) {
                    switch (state) {
                        case ViewPager.SCROLL_STATE_DRAGGING:
                            stopPageRolling();
                            break;
                        case ViewPager.SCROLL_STATE_SETTLING:
                            startPageRolling();
                            break;
                    }
                }
            }
        });
    }


    /**
     * Pager Cnotroll Handler
     */
    private Handler handlerPager = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (mVpPager != null) {
                int position = mVpPager.getCurrentItem();
                if (mPagerAdapter != null) {
                    if (position >= (mPagerAdapter.getCount() - 1)) {
                        mVpPager.setCurrentItem(0);
                    } else {
                        mVpPager.setCurrentItem(position + 1);
                    }
                }
            }
            return false;
        }
    });

    private void startPageRolling() {
        if (!handlerPager.hasMessages(0)) {
            handlerPager.sendEmptyMessageDelayed(0, TOP_BANNER_DELAY);
        }
    }

    private void stopPageRolling() {
        handlerPager.removeMessages(0);
    }


    /**
     * BaseRecyclerView Interface
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return BaseViewHolder.newInstance(R.layout.listitem_achivement_post, parent, false);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder h, int i) {
        onBindViewOther(h, i);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ADD_RECENT_ACHIVEMENT:
                case REQUEST_EDIT_RECENT_ACHIVEMENT:
                case REQUEST_RECENT_ACHIVEMENT:
                    httpGetAchivementPostMain();
                    break;
            }
        }
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
        Utils.setResizeView(ivThumbnail, size.x, size.x);

        tvTitle.setText(bean.getTitle());
        tvContents.setText(bean.getContent());

        if (TextUtils.isEmpty(bean.getThumbnail_image()))
            Glide.with(getContext()).load(R.drawable.user).into(ivThumbnail);
        else
            Glide.with(getContext()).load(bean.getThumbnail_image()).thumbnail(R.drawable.user).into(ivThumbnail);

        if (TextUtils.isEmpty(mProfileImage))
            Glide.with(getContext()).load(R.drawable.drawer_user).into(ivProfile);
        else
            Glide.with(getContext()).load(bean.getThumbnail_image()).thumbnail(R.drawable.drawer_user).into(ivProfile);

        PopupMenu popupMenu = new PopupMenu(getContext(), ibtnMore);
        popupMenu.getMenuInflater().inflate(R.menu.menu_achivement_post, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                AlertDialog.Builder builder;
                AlertDialog dialog;
                switch (id) {
                    case R.id.menu_edit:
                        Intent intent = new Intent(getContext(), ActivityAddAchivement.class);
                        intent.putExtra(ActivityAddAchivement.EXTRA_EDIT_ACHIVEMENT_POST, bean);
                        startActivityForResult(intent, REQUEST_EDIT_RECENT_ACHIVEMENT);
                        break;
                    case R.id.menu_delete:
                        builder = new AlertDialog.Builder(getContext())
                                .setTitle("프로필 삭제")
                                .setMessage("프로필을 삭제하시겠습니까?")
                                .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        httpDeletePostAchivement(bean.getIdx());
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        dialog = builder.create();
                        dialog.show();
                        break;
                }
                return false;
            }
        });

        ibtnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.show();
            }
        });


        h.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityRecentAchivementDetail.class);
                intent.putExtra(ActivityRecentAchivementDetail.EXTRA_RECENT_ACHIVEMENT_INDEX, bean.getIdx());
                startActivityForResult(intent,REQUEST_RECENT_ACHIVEMENT);
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
     * 최근 성과 삭제
     *
     * @param index
     */
    private void httpDeletePostAchivement(int index) {
        // todo : 제대로 된 설정 필요
        if (index == -1) return;
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_PROFILES_INDEX_ACHIVEMENT_POSTS_INDEX;
        url = url.replaceAll(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        url = url.replaceAll(Comm_Param.POST_INDEX, String.valueOf(index));

        HashMap header = Utils.getHttpHeader(prefs.getToken());
        BaseOkHttpClient client = new BaseOkHttpClient();
        client.Delete(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                if (TextUtils.equals(code, SUCCESS)) {
                    httpGetAchivementPostMain();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAdapter.size();
    }

    @Override
    public int getItemViewType(int i) {
        return TYPE_LIST_OTHER;
    }

    @OnClick(R.id.iv_add_achivement)
    public void onViewClicked() {
        Intent intent = new Intent(getContext(), ActivityAddAchivement.class);
        startActivityForResult(intent, REQUEST_ADD_RECENT_ACHIVEMENT);
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

        @Override
        public int getItemPosition(@NonNull Object object) {
            // todo : 추후 제거 바람
            return POSITION_NONE;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = mInflater.inflate(R.layout.layout_achivement, container, false);
            TextView tvTitle = view.findViewById(R.id.tv_title);
            tvTitle.setText("대표 성과 " + (position + 1));
            if (mBestPostList.size() > 0 && mBestPostList.get(position) != null) {
                BeanBestPost bean = mBestPostList.get(position);
                TextView tvBestPostAchivement = view.findViewById(R.id.tv_best_achivement);
                tvBestPostAchivement.setText(bean.getTitle());
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), ActivityBestAchivementDetail.class);
                        intent.putExtra(ActivityBestAchivementDetail.EXTRA_BEST_ACHIVEMENT_INDEX, bean.getIdx());
                        startActivity(intent);
                    }
                });
            }
            container.addView(view);
            return view;
        }
    }

    @Override
    public void onDestroy() {
        if (handlerPager != null) stopPageRolling();
        super.onDestroy();
    }
}
