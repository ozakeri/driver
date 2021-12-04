package gap.com.driver.fragment;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetView;

import gap.com.driver.R;
import gap.com.driver.app.DriverApplication;
import gap.com.driver.common.Constants;
import gap.com.driver.util.Constant;
import gap.com.driver.util.Globals;
import gap.com.driver.util.Util;
import gap.com.driver.util.Utils;

public class PrivacyFragment extends Fragment {

    EditText password_edt, confirm_edt;
    SwitchCompat switch_Button, switchChange_Button;
    CardView inputLocalPassCardView;
    RelativeLayout localPassButton, activeLocalPass, changeLocalPass;
    int getConfirmLocalPass = 0;
    String localPassStr;
    String localPassConfirmStr;
    private Globals globals = Globals.getInstance();
    private boolean showCaseViewFirFirst = false;


    public PrivacyFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_creation_local_password, container, false);
        if (!DriverApplication.getInstance().getSharedPreferences().getBoolean(Constants.PREF_GOTO_SETTING, false)) {
            //showGuideDialog();
        }

        SharedPreferences.Editor editor = DriverApplication.getInstance().getSharedPreferences().edit();
        editor.putBoolean(Constants.PREF_GOTO_SETTING, true);
        editor.apply();
        showCaseViewFirFirst = DriverApplication.getInstance().getSharedPreferences().getBoolean(Constants.PRIVACY_SHOW_CASE_VIEW_FOR_FIRST, false);
        getConfirmLocalPass = DriverApplication.getInstance().getSharedPreferences().getInt(Constant.SELECT_CONFIRM_LOCALPASS, -1);
        localPassStr = DriverApplication.getInstance().getSharedPreferences().getString(Constant.LOCALPASS, "");
        localPassConfirmStr = DriverApplication.getInstance().getSharedPreferences().getString(Constant.LOCALPASSCONFIRM, "");
        init(view);

        if (showCaseViewFirFirst) {
            SharedPreferences.Editor editor1 = DriverApplication.getInstance().getSharedPreferences().edit();
            editor1.putBoolean(Constants.PRIVACY_SHOW_CASE_VIEW_FOR_FIRST, false);
            editor1.apply();
            privacyShowcaseView(getActivity(), switch_Button);
        }

        Util.setTypeFace(password_edt);
        Util.setTypeFace(confirm_edt);

        password_edt.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
        confirm_edt.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);

        if (localPassStr.trim().isEmpty() && localPassConfirmStr.trim().isEmpty()) {
            Util.recognizeSwitchLocalPassword(Constant.SELECT_CONFIRM_LOCALPASS, 0);
            System.out.println("getConfirmLocalPass1==" + getConfirmLocalPass);
        }

        /*new ShowcaseView.Builder(getActivity())
                .setTarget(new ViewTarget(view.findViewById(R.id.switch_Button)))
                .setContentTitle(R.string.label_login_active_localPass)
                .setContentText(R.string.label_login_active_description)
                .hideOnTouchOutside()
                .build();*/

        getConfirmLocalPass = DriverApplication.getInstance().getSharedPreferences().getInt(Constant.SELECT_CONFIRM_LOCALPASS, -1);
        if (getConfirmLocalPass == 0) {
            switch_Button.setChecked(false);
            System.out.println("getConfirmLocalPass3==" + getConfirmLocalPass);
        } else if (getConfirmLocalPass == 1) {
            switch_Button.setChecked(true);
            changeLocalPass.setVisibility(View.VISIBLE);
            System.out.println("getConfirmLocalPass4==" + getConfirmLocalPass);
        }

     /*   switch (getConfirmLocalPass) {
            case 0:
                switch_Button.setChecked(false);
                break;

            case 1:
                switch_Button.setChecked(true);
                changeLocalPass.setVisibility(View.VISIBLE);
                break;
        }*/

        switch_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                localPassStr = DriverApplication.getInstance().getSharedPreferences().getString(Constant.LOCALPASS, "");
                localPassConfirmStr = DriverApplication.getInstance().getSharedPreferences().getString(Constant.LOCALPASSCONFIRM, "");

                if (b) {
                    if (localPassStr.trim().isEmpty() && localPassConfirmStr.trim().isEmpty()) {
                        inputLocalPassCardView.setVisibility(View.VISIBLE);
                        changeLocalPass.setVisibility(View.GONE);
                    } else {
                        inputLocalPassCardView.setVisibility(View.GONE);
                        changeLocalPass.setVisibility(View.VISIBLE);
                    }
                    Util.recognizeSwitchLocalPassword(Constant.SELECT_CONFIRM_LOCALPASS, 1);
                    //Util.loginIs(Constant.LOGIN_IS, true);
                    System.out.println("switch_Button11");

                    InputMethodManager inputMethodManager =  (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInputFromWindow(password_edt.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                    password_edt.requestFocus();
                } else {
                    Util.recognizeSwitchLocalPassword(Constant.SELECT_CONFIRM_LOCALPASS, 0);
                    //Util.loginIs(Constant.LOGIN_IS, false);
                    System.out.println("switch_Button22");
                    changeLocalPass.setVisibility(View.GONE);
                    switchChange_Button.setChecked(false);
                    inputLocalPassCardView.setVisibility(View.GONE);
                }
            }
        });

        switchChange_Button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    inputLocalPassCardView.setVisibility(View.VISIBLE);

                    InputMethodManager inputMethodManager =  (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.toggleSoftInputFromWindow(password_edt.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
                    password_edt.requestFocus();

                } else {
                    inputLocalPassCardView.setVisibility(View.GONE);
                }
            }
        });

        /*password_edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                password_edt.post(new Runnable() {
                    @Override
                    public void run() {
                        password_edt.setSelection(password_edt.length());
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(password_edt, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });

        confirm_edt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                confirm_edt.post(new Runnable() {
                    @Override
                    public void run() {
                        confirm_edt.setSelection(confirm_edt.length());
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(confirm_edt, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });*/

        localPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String passwordStr = Utils.farsiNumberReplacement(password_edt.getText().toString());
                String confirmStr = Utils.farsiNumberReplacement(confirm_edt.getText().toString());

                if (TextUtils.isEmpty(passwordStr)) {
                    password_edt.setError(getResources().getString(R.string.label_login_editText_warning));
                    Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.label_login_editText_warning), Toast.LENGTH_SHORT);
                    Util.showToast1(toast, false);
                    toast.show();
                } else if (TextUtils.isEmpty(confirmStr)) {
                    confirm_edt.setError(getResources().getString(R.string.label_login_editText_warning));
                    Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.label_login_editText_warning), Toast.LENGTH_SHORT);
                    Util.showToast1(toast, false);
                    toast.show();
                } else if (passwordStr.equals(confirmStr)) {
                    Util.recognizeSwitchLocalPassword(Constant.SELECT_CONFIRM_LOCALPASS, 1);
                    System.out.println("passwordStr.equals(confirmStr)");
                    Util.recognizeInputLocalPassword(Constant.LOCALPASS, passwordStr);
                    Util.recognizeInputLocalPassword(Constant.LOCALPASSCONFIRM, confirmStr);
                    Toast.makeText(getActivity(), getResources().getString(R.string.label_login_editText_ok), Toast.LENGTH_SHORT).show();
                    goToSettingsFragment();
                } else {
                    Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.label_login_clickOk_warning), Toast.LENGTH_SHORT);
                    Util.showToast1(toast, false);
                    toast.show();
                }
            }
        });

        password_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (password_edt.getText().toString().length() == 0) {
                    password_edt.setGravity(Gravity.RIGHT);
                } else {
                    password_edt.setGravity(Gravity.NO_GRAVITY);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String result = editable.toString().replaceAll(" ", "");
                if (!editable.toString().equals(result)) {
                    password_edt.setText(result);
                    password_edt.setSelection(result.length());
                }
            }
        });


        confirm_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (confirm_edt.getText().toString().length() == 0) {
                    confirm_edt.setGravity(Gravity.RIGHT);
                } else {
                    confirm_edt.setGravity(Gravity.NO_GRAVITY);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String result = editable.toString().replaceAll(" ", "");
                if (!editable.toString().equals(result)) {
                    confirm_edt.setText(result);
                    confirm_edt.setSelection(result.length());
                }
            }
        });

        return view;
    }

    public void init(View view) {
        inputLocalPassCardView = (CardView) view.findViewById(R.id.cardView_inputLocalPass);
        localPassButton = (RelativeLayout) view.findViewById(R.id.layout_localPassButton);
        activeLocalPass = (RelativeLayout) view.findViewById(R.id.layout_activeLocalPass);
        changeLocalPass = (RelativeLayout) view.findViewById(R.id.layout_changeLocalPass);
        switch_Button = (SwitchCompat) view.findViewById(R.id.switch_Button);
        switchChange_Button = (SwitchCompat) view.findViewById(R.id.switchChange_Button);
        password_edt = (EditText) view.findViewById(R.id.input_pass);
        confirm_edt = (EditText) view.findViewById(R.id.input_confirm);
    }

    public void showGuideDialog() {
        final Dialog alert = new Dialog(getActivity());
        alert.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alert.setContentView(R.layout.custom_dialog_guide);
        TextView buttonTextView = alert.findViewById(R.id.txt_action);
        buttonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    public void goToSettingsFragment() {
        Fragment fragment = new SettingsFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        Util.recognizeSelectedItems(Constant.RECOGNIZE, "SettingsFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        localPassStr = DriverApplication.getInstance().getSharedPreferences().getString(Constant.LOCALPASS, "");
        localPassConfirmStr = DriverApplication.getInstance().getSharedPreferences().getString(Constant.LOCALPASSCONFIRM, "");
        if (localPassStr.trim().isEmpty() && localPassConfirmStr.trim().isEmpty()) {
            Util.recognizeSwitchLocalPassword(Constant.SELECT_CONFIRM_LOCALPASS, 0);
            System.out.println("onPause");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        localPassStr = DriverApplication.getInstance().getSharedPreferences().getString(Constant.LOCALPASS, "");
        localPassConfirmStr = DriverApplication.getInstance().getSharedPreferences().getString(Constant.LOCALPASSCONFIRM, "");
        if (localPassStr.trim().isEmpty() && localPassConfirmStr.trim().isEmpty()) {
            Util.recognizeSwitchLocalPassword(Constant.SELECT_CONFIRM_LOCALPASS, 0);
            System.out.println("onDestroy");
        }
    }

    public static void privacyShowcaseView(final Context context, View view) {

        String title = "در صورت تمایل می توانید گذرواژه محلی خود را فعال کنید";
        String description = "";

        TapTargetView.showFor((Activity) context, TapTarget.forView(view, title, description)
                .cancelable(false)
                .drawShadow(true)
                .targetCircleColor(R.color.colorPrimary_transparent)
                .outerCircleColor(R.color.light_green_transparent)
                .textColor(R.color.colorPrimary)
                .titleTextDimen(R.dimen.title_text_size)
                .tintTarget(false), new TapTargetView.Listener() {
            @Override
            public void onTargetClick(TapTargetView view) {
                super.onTargetClick(view);
            }

            @Override
            public void onOuterCircleClick(TapTargetView view) {
                super.onOuterCircleClick(view);
                Toast.makeText(view.getContext(), "You clicked the outer circle!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTargetDismissed(TapTargetView view, boolean userInitiated) {
                Log.d("TapTargetViewSample", "You dismissed me :(");
            }
        });
    }
}
