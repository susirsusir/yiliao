package com.yl.yiliao.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.yl.yiliao.R;
import com.yl.yiliao.event.AddSubScribeEvent;
import com.yl.yiliao.event.RefreshFriendEvent;
import com.yl.yiliao.model.BaseData;
import com.yl.yiliao.model.CountryCodeData;
import com.yl.yiliao.model.CountryCodeItem;
import com.yl.yiliao.model.SearchUserData;
import com.yl.yiliao.utils.Constants;
import com.yl.yiliao.utils.GsonUtils;
import com.yl.yiliao.utils.ImageUtiles;
import com.yl.yiliao.utils.RequestUtils;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class SouActivity extends BaseTopActivity {
    private final String TAG = SouActivity.class.getSimpleName();

    private EditText mPhoneInput;
    private TextView mCountryTv;
    private String mdefautCode = "86";
    private RelativeLayout emptyLay;
    private RelativeLayout userLay;
    private SimpleDraweeView userIcon;
    private TextView userName;
    private TextView userNo;
    private TextView addBtn;
    private List<String> codeList = new ArrayList<>();
    private List<CountryCodeItem> items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("搜一搜");
        initView();

    }

    private void initView() {
        mPhoneInput = (EditText) findViewById(R.id.phone_ed);
        mPhoneInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (keyEvent != null && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    // 在这里执行搜索
                    search();
                    return true;
                }
                return false;
            }
        });
        mCountryTv = (TextView) findViewById(R.id.country_code_tv);
        findViewById(R.id.search_cion).setOnClickListener(this);

        emptyLay = (RelativeLayout) findViewById(R.id.empty_lay);
        userLay = (RelativeLayout) findViewById(R.id.user_lay);
        userIcon = (SimpleDraweeView) findViewById(R.id.user_icon);
        userIcon.setOnClickListener(this);
        userName = (TextView) findViewById(R.id.user_name);
        userNo = (TextView) findViewById(R.id.user_phone);
        addBtn = (TextView) findViewById(R.id.add_btn);
        addBtn.setOnClickListener(this);
        findViewById(R.id.country_code_lay).setOnClickListener(this);
        initData();
    }

    private void initData() {
        new RequestUtils(this, TAG, Constants.GET_PHONE_CODE, new RequestUtils.RequestCallback() {
            @Override
            public void success(String result) {

                CountryCodeData resultData = GsonUtils.getResult(result, CountryCodeData.class);
                if (resultData != null) {
                    if (resultData.getCode() != 0) {
                        Toast.makeText(SouActivity.this, resultData.getDescription(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (resultData.getData() != null) {
                        items = resultData.getData();
                        setList(resultData.getData());
                    }
                }
            }

            @Override
            public void fail(VolleyError volleyError) {
                if (volleyError != null) {
                    Toast.makeText(SouActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void setList(List<CountryCodeItem> items) {
        if (items.size() > 0) {
            for (int i = 0; i < items.size(); i++) {
                CountryCodeItem item = items.get(i);
                codeList.add(item.getName() + "  (+" + item.getCode() + ")");
            }
        }

    }


    @Override
    protected int getContentRes() {
        return R.layout.activity_sou;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.search_cion:
                // 执行搜索
                search();
                break;
            case R.id.user_icon:
                Intent intent = new Intent(this, UserInfoActivity.class);
                if (mData != null) {
                    intent.putExtra("phoneNo", mData.getPhoneNo());
                }
                startActivity(intent);
                break;
            case R.id.add_btn:
                // 添加好友
                addFriend(mData.getPhoneNo());
                break;
            case R.id.country_code_lay:
                // 展示国家代码
                showCodeDialog();
                break;
        }
    }

    private AlertDialog.Builder codeBuilder;

    private void showCodeDialog() {
        if (codeList.size() == 0) {
            return;
        }
        if (codeBuilder == null) {
            codeBuilder = new AlertDialog.Builder(this);
            codeBuilder.setItems(codeList.toArray(new String[codeList.size()]), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (items != null) {
                        mdefautCode = items.get(i).getCode();
                    }
                    mCountryTv.setText(codeList.get(i));
                }
            });
        }
        codeBuilder.show();
    }

    private void addFriend(final String phone) {
        new RequestUtils(this, TAG, Constants.ADD_FRIEND + phone, new RequestUtils.RequestCallback() {
            @Override
            public void success(String result) {

                BaseData resultData = GsonUtils.getResult(result, BaseData.class);
                if (resultData != null) {
                    if (resultData.getCode() != 0) {
                        Toast.makeText(SouActivity.this, resultData.getDescription(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    EventBus.getDefault().post(new AddSubScribeEvent(phone));
                    EventBus.getDefault().post(new RefreshFriendEvent());
                    addBtn.setText("已添加");
                    addBtn.setBackgroundResource(R.drawable.btn_get_code_sel);
                    addBtn.setTextColor(getResources().getColor(R.color.edit_hint_color));
                    addBtn.setClickable(false);
                }
            }

            @Override
            public void fail(VolleyError volleyError) {
                if (volleyError != null) {
                    Toast.makeText(SouActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                }
                emptyLay.setVisibility(View.VISIBLE);
                userLay.setVisibility(View.GONE);
            }
        });
    }

    private void search() {
        String phone = mPhoneInput.getText().toString().trim();
        if (!TextUtils.isEmpty(phone)) {
            searchData(mdefautCode, phone);
        }else {
            Toast.makeText(this,"手机号不能为空",Toast.LENGTH_SHORT).show();
        }
    }

    private void searchData(String defaultCode, String phone) {
        new RequestUtils(this, TAG, Constants.GET_OTHER_USER_INFO + defaultCode + phone, new RequestUtils.RequestCallback() {
            @Override
            public void success(String result) {

                SearchUserData resultData = GsonUtils.getResult(result, SearchUserData.class);
                if (resultData != null) {
                    if (resultData.getCode() != 0) {
                        Toast.makeText(SouActivity.this, resultData.getDescription(), Toast.LENGTH_SHORT).show();
                        emptyLay.setVisibility(View.VISIBLE);
                        userLay.setVisibility(View.GONE);
                        return;
                    }
                    if (resultData.getData() != null) {
                        mData = resultData.getData();
                        setData(resultData.getData());
                    } else {
                        emptyLay.setVisibility(View.VISIBLE);
                        userLay.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void fail(VolleyError volleyError) {
                if (volleyError != null) {
                    Toast.makeText(SouActivity.this, "网络异常，请稍后重试", Toast.LENGTH_SHORT).show();
                }
                emptyLay.setVisibility(View.VISIBLE);
                userLay.setVisibility(View.GONE);
            }
        });
    }

    private SearchUserData mData;

    private void setData(SearchUserData data) {
        emptyLay.setVisibility(View.GONE);
        userLay.setVisibility(View.VISIBLE);
        ImageUtiles.load(userIcon, data.getAvatar());
        userName.setText(data.getNickName());
        userNo.setText("(+" + data.getPhoneNo().substring(0, 2) + ") " + data.getPhoneNo().substring(2));
        if (data.isFriend()) {
            addBtn.setText("已添加");
            addBtn.setBackgroundResource(R.drawable.btn_get_code_sel);
            addBtn.setTextColor(getResources().getColor(R.color.edit_hint_color));
            addBtn.setClickable(false);
        } else {
            addBtn.setText("加好友");
            addBtn.setBackgroundResource(R.drawable.btn_get_code);
            addBtn.setTextColor(getResources().getColor(R.color.cursor_color));
            addBtn.setClickable(true);
        }
        addBtn = (TextView) findViewById(R.id.add_btn);
    }


}
