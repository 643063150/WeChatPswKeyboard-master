package com.example.numketboard.widget;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.example.numketboard.R;
import com.example.numketboard.adapter.KeyBoardAdapter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 虚拟键盘
 */
public class VirtualKeyboardView extends RelativeLayout {

    Context context;

    //因为就6个输入框不会变了，用数组内存申请固定空间，比List省空间（自己认为）
    private GridView gridView;    //用GrideView布局键盘，其实并不是真正的键盘，只是模拟键盘的功能

    private ArrayList<Map<String, String>> valueList;    //有人可能有疑问，为何这里不用数组了？
    //因为要用Adapter中适配，用数组不能往adapter中填充

    private RelativeLayout layoutBack;
    EditText editText;

    public VirtualKeyboardView(Context context) {
        this(context, null);
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
        setKeyboard(editText);
    }

    // 设置不调用系统键盘
    public void setKeyboard(EditText editText) {
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            editText.setInputType(InputType.TYPE_NULL);
        } else {
            ((Activity) context).getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus",
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public VirtualKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        View view = View.inflate(context, R.layout.layout_virtual_keyboard, null);

        valueList = new ArrayList<>();

        layoutBack = (RelativeLayout) view.findViewById(R.id.layoutBack);
        gridView = (GridView) view.findViewById(R.id.gv_keybord);

        initValueList();

        setupView();

        addView(view);      //必须要，不然不显示控件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position < 11 && position != 9) {    //点击0~9按钮

                    String amount = editText.getText().toString().trim();
                    amount = amount + valueList.get(position).get("name");
                    int index = editText.getSelectionStart();
                    if (index==amount.length()-1){
                        editText.setText(amount);
                        Editable ea = editText.getText();
                        editText.setSelection(ea.length());
                    }else {
                        Editable editable = editText.getText();
                        editable.insert(index,valueList.get(position).get("name"));
                    }

                } else {

                    if (position == 9) {      //点击退格键
                        String amount = editText.getText().toString().trim();
                        int index = editText.getSelectionStart();
                        if (index==amount.length()-1){
                            amount = amount + valueList.get(position).get("name");
                            editText.setText(amount);
                            Editable ea = editText.getText();
                            editText.setSelection(ea.length());
                        }else {
                            Editable editable = editText.getText();
                            editable.insert(index,valueList.get(position).get("name"));
                        }


                    }

                    if (position == 11) {      //点击退格键
                        String amount = editText.getText().toString().trim();
                        if (amount.length() > 0) {
                            int index = editText.getSelectionStart();
                            if (index==amount.length()){
                                amount = amount.substring(0, amount.length() - 1);
                                editText.setText(amount);
                                Editable ea = editText.getText();
                                editText.setSelection(ea.length());
                            }else {
                              if (index!=0){
                                  Editable editable = editText.getText();
                                  editable.delete(index-1, index);
                              }
                            }

                        }
                    }
                }
            }
        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 11) {      //点击退格键
                    String amount = editText.getText().toString().trim();
                    if (amount.length() > 0) {
                        amount = amount.substring(0, amount.length() - 1);
                        editText.setText("");

                        Editable ea = editText.getText();
                        editText.setSelection(ea.length());
                    }
                }
                return false;
            }
        });
    }

    public RelativeLayout getLayoutBack() {
        return layoutBack;
    }

    public ArrayList<Map<String, String>> getValueList() {
        return valueList;
    }

    private void initValueList() {

        // 初始化按钮上应该显示的数字
        for (int i = 1; i < 13; i++) {
            Map<String, String> map = new HashMap<>();
            if (i < 10) {
                map.put("name", String.valueOf(i));
            } else if (i == 10) {
                map.put("name", ".");
            } else if (i == 11) {
                map.put("name", String.valueOf(0));
            } else if (i == 12) {
                map.put("name", "");
            }
            valueList.add(map);
        }
    }

    public GridView getGridView() {
        return gridView;
    }

    private void setupView() {

        KeyBoardAdapter keyBoardAdapter = new KeyBoardAdapter(context, valueList);
        gridView.setAdapter(keyBoardAdapter);
    }
}
