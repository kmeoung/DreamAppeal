package com.truevalue.dreamappeal.fragment.profile.blueprint;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivityMain;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseTitleBar;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IOBaseTitleBarListener;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;
import com.truevalue.dreamappeal.bean.BeanBlueprintAbilityOpportunity;
import com.truevalue.dreamappeal.http.DAHttpClient;
import com.truevalue.dreamappeal.http.IOServerCallback;
import com.truevalue.dreamappeal.utils.Comm_Param;
import com.truevalue.dreamappeal.utils.Comm_Prefs;
import com.truevalue.dreamappeal.utils.Utils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class FragmentAbilityOpportunity extends BaseFragment implements IOBaseTitleBarListener {

    @BindView(R.id.btb_bar)
    BaseTitleBar mBtbBar;
    @BindView(R.id.iv_add_ability)
    ImageView mIvAddAbility;
    @BindView(R.id.rv_ability)
    RecyclerView mRvAbility;
    @BindView(R.id.iv_add_opportunity)
    ImageView mIvAddOpportunity;
    @BindView(R.id.rv_opportunity)
    RecyclerView mRvOpportunity;

    private BaseRecyclerViewAdapter mAbilityAdapter;
    private BaseRecyclerViewAdapter mOpportunityAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ability_opportunity, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Title Bar 초기화
        mBtbBar.setIOBaseTitleBarListener(this);
        // Adapter 초기화
        initAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        // 서버 초기화
        httpGetAbilities(true);
    }

    @Override
    public void OnClickBack() {
        getActivity().onBackPressed();
    }

    /**
     * 어댑터 초기화
     */
    private void initAdapter() {
        mAbilityAdapter = new BaseRecyclerViewAdapter(getContext(), new IORecyclerViewListener() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return BaseViewHolder.newInstance(R.layout.listitem_ability_opportunity, parent, false);
            }

            @Override
            public void onBindViewHolder(@NonNull BaseViewHolder h, int i) {
                BeanBlueprintAbilityOpportunity bean = (BeanBlueprintAbilityOpportunity) mAbilityAdapter.get(i);
                ImageView ivMore = h.getItemView(R.id.iv_more);
                TextView tvTitle = h.getItemView(R.id.tv_ability_opportunity);
                tvTitle.setText(bean.getContents());
                PopupMenu popupMenu = new PopupMenu(getContext(), ivMore);
                popupMenu.getMenuInflater().inflate(R.menu.menu_achivement_post, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        AlertDialog.Builder builder;
                        AlertDialog dialog;
                        switch (id) {
                            case R.id.menu_edit:
                                ((ActivityMain) getActivity())
                                        .replaceFragmentRight(
                                                FragmentAddAbilityOpportunity.newInstance(FragmentAddAbilityOpportunity.TYPE_ABILITY,
                                                        "갖출 능력 수정",
                                                        bean),
                                                true);
                                break;
                            case R.id.menu_delete:
                                builder = new AlertDialog.Builder(getContext())
                                        .setTitle("갖출 능력 삭제")
                                        .setMessage("능력을 삭제하시겠습니까?")
                                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                httpDeleteAbility(bean.getIdx());
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
                ivMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupMenu.show();
                    }
                });
            }

            @Override
            public int getItemCount() {
                if (mAbilityAdapter != null) {
                    return mAbilityAdapter.size();
                }
                return 0;
            }

            @Override
            public int getItemViewType(int i) {
                return 0;
            }
        });

        mOpportunityAdapter = new BaseRecyclerViewAdapter(getContext(), new IORecyclerViewListener() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return BaseViewHolder.newInstance(R.layout.listitem_ability_opportunity, parent, false);
            }

            @Override
            public void onBindViewHolder(@NonNull BaseViewHolder h, int i) {
                BeanBlueprintAbilityOpportunity bean = (BeanBlueprintAbilityOpportunity) mOpportunityAdapter.get(i);
                ImageView ivMore = h.getItemView(R.id.iv_more);
                TextView tvTitle = h.getItemView(R.id.tv_ability_opportunity);
                tvTitle.setText(bean.getContents());
                PopupMenu popupMenu = new PopupMenu(getContext(), ivMore);
                popupMenu.getMenuInflater().inflate(R.menu.menu_achivement_post, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        AlertDialog.Builder builder;
                        AlertDialog dialog;
                        switch (id) {
                            case R.id.menu_edit:
                                ((ActivityMain) getActivity())
                                        .replaceFragmentRight(FragmentAddAbilityOpportunity
                                                .newInstance(FragmentAddAbilityOpportunity.TYPE_OPPORTUNITY,
                                                        "만들고픈 기회 수정",
                                                        bean), true);
                                break;
                            case R.id.menu_delete:
                                builder = new AlertDialog.Builder(getContext())
                                        .setTitle("만들고픈 기회 삭제")
                                        .setMessage("기회를 삭제하시겠습니까?")
                                        .setPositiveButton("네", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                httpDeleteOpportunity(bean.getIdx());
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


                ivMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupMenu.show();
                    }
                });
            }

            @Override
            public int getItemCount() {
                if (mOpportunityAdapter != null) {
                    return mOpportunityAdapter.size();
                }
                return 0;
            }

            @Override
            public int getItemViewType(int i) {
                return 0;
            }
        });

        mRvAbility.setAdapter(mAbilityAdapter);
        mRvAbility.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvOpportunity.setAdapter(mOpportunityAdapter);
        mRvOpportunity.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    /**
     * http Get
     * 갖출 능력 조회
     *
     * @param isAll 전부 조회시
     */
    private void httpGetAbilities(boolean isAll) {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_BLUEPRINT_ABILITIES;
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));

        HashMap header = Utils.getHttpHeader(prefs.getToken());

        DAHttpClient client = DAHttpClient.getInstance();
        client.Get(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                if (TextUtils.equals(code, SUCCESS)) {
                    JSONObject object = new JSONObject(body);
                    mAbilityAdapter.clear();
                    try {
                        JSONArray array = object.getJSONArray("abilities");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject ability = array.getJSONObject(i);
                            int profile_idx = ability.getInt("profile_idx");
                            int idx = ability.getInt("idx");
                            String contents = ability.getString("ability");
                            BeanBlueprintAbilityOpportunity bean = new BeanBlueprintAbilityOpportunity(profile_idx, idx, contents);
                            mAbilityAdapter.add(bean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (isAll) httpGetOpportunities();
                }
            }
        });
    }

    /**
     * http Get
     * 만들고픈 기회 조회
     */
    private void httpGetOpportunities() {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_BLUEPRINT_OPPORTUNITIES;
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));

        HashMap header = Utils.getHttpHeader(prefs.getToken());

        DAHttpClient client = DAHttpClient.getInstance();
        client.Get(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                if (TextUtils.equals(code, SUCCESS)) {
                    JSONObject object = new JSONObject(body);
                    mOpportunityAdapter.clear();
                    try {
                        JSONArray array = object.getJSONArray("opportunities");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject ability = array.getJSONObject(i);
                            int profile_idx = ability.getInt("profile_idx");
                            int idx = ability.getInt("idx");
                            String contents = ability.getString("opportunity");
                            BeanBlueprintAbilityOpportunity bean = new BeanBlueprintAbilityOpportunity(profile_idx, idx, contents);
                            mOpportunityAdapter.add(bean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    /**
     * http Delete
     * 만들고픈 기회 삭제
     *
     * @param ability_index
     */
    private void httpDeleteAbility(int ability_index) {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_BLUEPRINT_ABILITIES_INDEX;
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        url = url.replace(Comm_Param.ABILITY_INDEX, String.valueOf(ability_index));

        HashMap header = Utils.getHttpHeader(prefs.getToken());

        DAHttpClient client = DAHttpClient.getInstance();
        client.Delete(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                if (TextUtils.equals(code, SUCCESS)) {
                    httpGetAbilities(false);
                }
            }
        });
    }

    /**
     * http Delete
     * 만들고픈 기회 삭제
     *
     * @param opportunity_index
     */
    private void httpDeleteOpportunity(int opportunity_index) {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        String url = Comm_Param.URL_API_BLUEPRINT_OPPORTUNITIES_INDEX;
        url = url.replace(Comm_Param.PROFILES_INDEX, String.valueOf(prefs.getProfileIndex()));
        url = url.replace(Comm_Param.OPPORTUNITY_INDEX, String.valueOf(opportunity_index));

        HashMap header = Utils.getHttpHeader(prefs.getToken());

        DAHttpClient client = DAHttpClient.getInstance();
        client.Delete(url, header, null, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                if (TextUtils.equals(code, SUCCESS)) {
                    httpGetOpportunities();
                }
            }
        });
    }

    @OnClick({R.id.iv_add_ability, R.id.iv_add_opportunity})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add_ability: // 갖출 능력
                ((ActivityMain) getActivity()).replaceFragmentRight(FragmentAddAbilityOpportunity.newInstance(FragmentAddAbilityOpportunity.TYPE_ABILITY, "갖출 능력 등록하기"), true);
                break;
            case R.id.iv_add_opportunity: // 만들고픈 기회
                ((ActivityMain) getActivity()).replaceFragmentRight(FragmentAddAbilityOpportunity.newInstance(FragmentAddAbilityOpportunity.TYPE_OPPORTUNITY, "만들어갈 기회 등록하기"), true);
                break;
        }
    }


}
