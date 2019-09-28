package com.truevalue.dreamappeal.fragment.search;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.truevalue.dreamappeal.R;
import com.truevalue.dreamappeal.activity.ActivitySearch;
import com.truevalue.dreamappeal.base.BaseFragment;
import com.truevalue.dreamappeal.base.BaseRecyclerViewAdapter;
import com.truevalue.dreamappeal.base.BaseViewHolder;
import com.truevalue.dreamappeal.base.IORecyclerViewListener;
import com.truevalue.dreamappeal.bean.BeanSearchAppealer;
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
import okhttp3.Call;

public class FragmentSearchAppealer extends BaseFragment implements IORecyclerViewListener, ActivitySearch.IOSearchListener {

    @BindView(R.id.rv_popular)
    RecyclerView mRvPopular;

    private BaseRecyclerViewAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_popular, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Init Adapter
        initAdapter();
        // initView
        initView();
    }


    private void initAdapter() {
        mAdapter = new BaseRecyclerViewAdapter(getContext(), this);
        mRvPopular.setAdapter(mAdapter);
        mRvPopular.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initView() {
        ((ActivitySearch) getActivity()).setmListener(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return BaseViewHolder.newInstance(R.layout.listitem_search_popular, parent, false);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder h, int i) {
        BeanSearchAppealer bean = (BeanSearchAppealer) mAdapter.get(i);
        ImageView ivProfile = h.getItemView(R.id.iv_profile);
        TextView tvValueStyle = h.getItemView(R.id.tv_value_style);
        TextView tvJob = h.getItemView(R.id.tv_job);
        TextView tvName = h.getItemView(R.id.tv_name);
        ImageView ivDelete = h.getItemView(R.id.iv_delete);

        if (TextUtils.isEmpty(bean.getImage()))
            Glide.with(this).load(R.drawable.drawer_user).apply(new RequestOptions().circleCrop()).into(ivProfile);
        else
            Glide.with(this).load(bean.getImage()).placeholder(R.drawable.drawer_user).apply(new RequestOptions().circleCrop()).into(ivProfile);

        tvValueStyle.setText(bean.getValue_style());
        tvJob.setText(bean.getJob());
        tvName.setText(bean.getName());

        ivDelete.setOnClickListener(view -> {
            // todo : 검색에서 삭제가 무엇인지 알아보아야 합니다.
            Toast.makeText(getContext().getApplicationContext(), "구현되지 않은 기능입니다.", Toast.LENGTH_SHORT).show();
        });

        h.itemView.setOnClickListener(v -> {
            Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
            prefs.setProfileIndex(bean.getIdx(),false);
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        });
    }

    @Override
    public int getItemCount() {
        return mAdapter.size();
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public void search(String text) {
        if(text.length() > 0) {
            httpPostSearch(text);
        }else{
            mAdapter.clear();
        }
    }

    /**
     * http Post
     * 검색
     *
     * @param keyword
     */
    private void httpPostSearch(String keyword) {
        Comm_Prefs prefs = Comm_Prefs.getInstance(getContext());
        HashMap header = Utils.getHttpHeader(prefs.getToken());
        HashMap<String, String> body = new HashMap<>();
        body.put("keyword", keyword);
        body.put("idx",String.valueOf(prefs.getMyProfileIndex()));
        DAHttpClient.getInstance(getContext()).Post(Comm_Param.URL_SEARCH, header, body, new IOServerCallback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, int serverCode, String body, String code, String message) throws IOException, JSONException {
                Toast.makeText(getContext().getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                if (TextUtils.equals(code, SUCCESS)) {
                    mAdapter.clear();

                    JSONObject json = new JSONObject(body);
                    JSONArray appealers = json.getJSONArray("appealers");
                    Gson gson = new Gson();
                    for (int i = 0; i < appealers.length(); i++) {
                        BeanSearchAppealer bean = gson.fromJson(appealers.getJSONObject(i).toString(), BeanSearchAppealer.class);
                        mAdapter.add(bean);
                    }
                }
            }
        });
    }
}
