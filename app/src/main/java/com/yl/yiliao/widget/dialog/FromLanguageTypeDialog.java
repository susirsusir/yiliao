package com.yl.yiliao.widget.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.yl.yiliao.R;

public class FromLanguageTypeDialog extends Dialog {

    private static SelectListener mlistener;
    public static FromLanguageTypeDialog showDialog(Context context, SelectListener listener) {
        mlistener = listener;
        FromLanguageTypeDialog dialog = new FromLanguageTypeDialog(context);
        dialog.show();
        return dialog;
    }

    private FromLanguageTypeDialog(@NonNull Context context) {
        super(context, R.style.CustomTheme_Dialog);
    }


    private static String[] from_type = new String[]{"推荐", "视频", "图片", "段子", "专享", "分类", "女神", "音乐", "涨姿势"};
    private static String[] from_code = new String[]{"推荐", "视频", "图片", "段子", "专享", "分类", "女神", "音乐", "涨姿势"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.from_type_dialog);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1,from_type));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    mlistener.select(from_type[i],from_code[i]);
            }
        });

    }

    public interface SelectListener {
        void select(String type,String code);
    }
}
