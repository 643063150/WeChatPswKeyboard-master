package com.lnyp.pswkeyboard;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ScrollView;


import com.example.numketboard.widget.VirtualKeyboardView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

public class NormalKeyBoardActivity extends AppCompatActivity {

    private VirtualKeyboardView virtualKeyboardView;

    private GridView gridView;

    private ArrayList<Map<String, String>> valueList;

    private EditText textAmount;

    private Animation enterAnim;

    private Animation exitAnim;
    private EditText textAmount2;
    private ScrollView textAmount3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_key_board);

        initAnim();

        initView();

//        valueList = virtualKeyboardView.getValueList();
    }

    /**
     * 数字键盘显示动画
     */
    private void initAnim() {

        enterAnim = AnimationUtils.loadAnimation(this, R.anim.push_bottom_in);
        exitAnim = AnimationUtils.loadAnimation(this, R.anim.push_bottom_out);
    }

    private void initView() {

        textAmount = (EditText) findViewById(R.id.textAmount);
        textAmount2 = (EditText) findViewById(R.id.textAmount2);
        textAmount3 = (ScrollView) findViewById(R.id.textAmount3);
        // 设置不调用系统键盘
//        if (android.os.Build.VERSION.SDK_INT <= 10) {
//            textAmount.setInputType(InputType.TYPE_NULL);
//        } else {
//            this.getWindow().setSoftInputMode(
//                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//            try {
//                Class<EditText> cls = EditText.class;
//                Method setShowSoftInputOnFocus;
//                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus",
//                        boolean.class);
//                setShowSoftInputOnFocus.setAccessible(true);
//                setShowSoftInputOnFocus.invoke(textAmount, false);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

        virtualKeyboardView = (VirtualKeyboardView) findViewById(R.id.virtualKeyboardView);
        virtualKeyboardView.setKeyboard(textAmount);
        virtualKeyboardView.setKeyboard(textAmount2);
        virtualKeyboardView.getLayoutBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                virtualKeyboardView.startAnimation(exitAnim);
                virtualKeyboardView.setVisibility(View.GONE);
            }
        });

//        gridView = virtualKeyboardView.getGridView();
//        gridView.setOnItemClickListener(onItemClickListener);
        textAmount.setOnTouchListener(new View.OnTouchListener() {
            int touch_flag=0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                if(touch_flag==2){
                    virtualKeyboardView.setEditText(textAmount);
                    virtualKeyboardView.setFocusable(true);
                    virtualKeyboardView.setFocusableInTouchMode(true);
                    virtualKeyboardView.startAnimation(enterAnim);
                    virtualKeyboardView.setVisibility(View.VISIBLE);
                    touch_flag=0;
                }
                return false;
            }
        });
        textAmount2.setOnTouchListener(new View.OnTouchListener() {
            int touch_flag=0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                touch_flag++;
                textAmount3.scrollTo(0,-1);
                if(touch_flag==2){
                    virtualKeyboardView.setEditText(textAmount2);
                    virtualKeyboardView.setFocusable(true);
                    virtualKeyboardView.setFocusableInTouchMode(true);
                    virtualKeyboardView.startAnimation(enterAnim);
                    virtualKeyboardView.setVisibility(View.VISIBLE);
                    int location[] = new int[2];
                    textAmount2.getLocationOnScreen(location);
                    System.out.println("editext:"+location[1]);
                   new Handler().postDelayed(new Thread(){
                       @Override
                       public void run() {
                           super.run();
                           int location[] = new int[2];
                           virtualKeyboardView.getLocationOnScreen(location);
                           System.out.println("keyboard:"+location[1]);
                       }
                   },100);
                    touch_flag=0;
                }
                return false;
            }
        });
    }

//    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
//
//        @Override
//        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//
//            if (position < 11 && position != 9) {    //点击0~9按钮
//
//                String amount = textAmount.getText().toString().trim();
//                amount = amount + valueList.get(position).get("name");
//
//                textAmount.setText(amount);
//
//                Editable ea = textAmount.getText();
//                textAmount.setSelection(ea.length());
//            } else {
//
//                if (position == 9) {      //点击退格键
//                    String amount = textAmount.getText().toString().trim();
////                    if (!amount.contains(".")) {
//                        amount = amount + valueList.get(position).get("name");
//                        textAmount.setText(amount);
//
//                        Editable ea = textAmount.getText();
//                        textAmount.setSelection(ea.length());
////                    }
//                }
//
//                if (position == 11) {      //点击退格键
//                    String amount = textAmount.getText().toString().trim();
//                    if (amount.length() > 0) {
//                        amount = amount.substring(0, amount.length() - 1);
//                        textAmount.setText(amount);
//
//                        Editable ea = textAmount.getText();
//                        textAmount.setSelection(ea.length());
//                    }
//                }
//            }
//        }
//    };
}
