/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.text.Editable
 *  android.text.TextUtils
 */
package com.videogo.util;

import android.text.Editable;
import android.text.TextUtils;
import com.videogo.errorlayer.ErrorInfo;
import com.videogo.errorlayer.ErrorLayer;
import com.videogo.exception.BaseException;
import com.videogo.util.LogUtil;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LocalValidate {
    private static final String TAG = "LocalValidate";

    public void localValidateUserName(String userName) throws BaseException {
        if (userName == null || userName.equals("")) {
            LogUtil.e(TAG, "localValidateUserName-> user name is null");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 410001);
            throw new BaseException("validateUserName-> user name is null", errorInfo);
        }
        if (userName.length() < 4) {
            LogUtil.e(TAG, "localValidateUserName->user name smaller than 4");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 410002);
            throw new BaseException("user name smaller than 4", errorInfo);
        }
        if (userName.length() > 20) {
            LogUtil.e(TAG, "localValidateUserName->user name greater than 20");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 410004);
            throw new BaseException("user name greater than 20", errorInfo);
        }
        Pattern pattern1 = Pattern.compile("^[a-zA-Z_0-9]+$");
        if (!pattern1.matcher(userName).matches()) {
            LogUtil.e(TAG, "localValidateUserName->user name contain illegal character");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 410005);
            throw new BaseException("user name contain illegal character", errorInfo);
        }
        if (this.isUserNameAllUnderline(userName)) {
            LogUtil.e(TAG, "localValidateUserName->user name all is under line");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 410003);
            throw new BaseException("user name all is under line", errorInfo);
        }
        if (this.isUserNameAllDigit(userName)) {
            LogUtil.e(TAG, "localValidateUserName->user name all is digit");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 410006);
            throw new BaseException("user name all is digit", errorInfo);
        }
    }

    private boolean isUserNameAllUnderline(String username) {
        char[] chars;
        for (char unit : chars = username.toCharArray()) {
            if (unit == '_') continue;
            return false;
        }
        return true;
    }

    public boolean isUserNameAllDigit(String useranme) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(useranme).matches();
    }

    public void acceptNumberOrChar(Editable edt, String previousText) {
        try {
            String temp = edt.toString();
            int len = temp.length();
            int prevLen = previousText.length();
            if (len == 0 || prevLen >= len) {
                return;
            }
            String tem = temp.substring(prevLen, len);
            char[] temC = tem.toCharArray();
            boolean isDelete = false;
            for (int i = 0; i < temC.length; ++i) {
                char mid = temC[i];
                if (mid > ' ' && mid <= '\u007f') continue;
                isDelete = true;
            }
            if (isDelete) {
                edt.delete(prevLen, len);
            }
        }
        catch (Exception e) {
            LogUtil.printErrStackTrace(TAG, e.fillInStackTrace());
        }
    }

    public void localValidatePsw(String password) throws BaseException {
        if (password == null || password.equals("")) {
            LogUtil.e(TAG, "localValidatePassword->password is null");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 410007);
            throw new BaseException("localValidatePassword->password is null", errorInfo);
        }
        if (password.length() < 6) {
            LogUtil.e(TAG, "localValidatePassword->password smaller than 6");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 410008);
            throw new BaseException("password smaller than 6", errorInfo);
        }
        if (password.length() > 16) {
            LogUtil.e(TAG, "localValidatePassword->password greater than 16");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 410009);
            throw new BaseException("password greater than 16", errorInfo);
        }
        boolean flag = true;
        char a = password.charAt(0);
        for (int i = 0; i < password.length(); ++i) {
            if (a == password.charAt(i)) continue;
            flag = false;
            break;
        }
        if (flag) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 410010);
            throw new BaseException("password is same character", errorInfo);
        }
    }

    public void localValidateNewPsw(String newPsw) throws BaseException {
        if (newPsw == null || newPsw.equals("")) {
            LogUtil.e(TAG, "localValidatePassword->password is null");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 410012);
            throw new BaseException("localValidatePassword->password is null", errorInfo);
        }
        if (newPsw.length() < 6) {
            LogUtil.e(TAG, "localValidatePassword->password smaller than 6");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 410013);
            throw new BaseException("password smaller than 6", errorInfo);
        }
        if (newPsw.length() > 16) {
            LogUtil.e(TAG, "localValidatePassword->password greater than 16");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 410014);
            throw new BaseException("password greater than 16", errorInfo);
        }
        boolean flag = true;
        char a = newPsw.charAt(0);
        for (int i = 0; i < newPsw.length(); ++i) {
            if (a == newPsw.charAt(i)) continue;
            flag = false;
            break;
        }
        if (flag) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 410015);
            throw new BaseException("password is same character", errorInfo);
        }
    }

    public void localValidatePhoneNum(String phoneNum) throws BaseException {
        if (phoneNum == null || phoneNum.equals("")) {
            LogUtil.e(TAG, "localValidatePhoneNum->phone number is null");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 410019);
            throw new BaseException("localValidatePhoneNum->password is null", errorInfo);
        }
        if (phoneNum.length() < 11) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 410020);
            throw new BaseException("camera serial no is null", errorInfo);
        }
    }

    public void localValidateEmail(String email) throws BaseException {
        if (email == null || email.equals("")) {
            LogUtil.e(TAG, "localValidatePhoneNum->phone number is null");
            return;
        }
        String regEx = "^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+\\.[a-zA-Z]{2,3}";
        Pattern p = Pattern.compile(regEx);
        Matcher matcher = p.matcher(email);
        if (!matcher.matches()) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 410031);
            throw new BaseException("camera serial no is null", errorInfo);
        }
    }

    public void localValidateVerifyCode(String verifyCode) throws BaseException {
        if (verifyCode == null || verifyCode.equals("")) {
            LogUtil.e(TAG, "loaclValidateVerifyCode->verify code is null");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 410023);
            throw new BaseException("loaclValidateVerifyCode->verify code is null", errorInfo);
        }
    }

    public void localValidatePsw(String oldPsw, String newPsw, String comfirmPsw) throws BaseException {
        this.localValidatePsw(oldPsw);
        this.localValidatPsw(newPsw, comfirmPsw);
    }

    public void localValidatPsw(String newPsw, String comfirmPsw) throws BaseException {
        this.localValidateNewPsw(newPsw);
        if (comfirmPsw == null || comfirmPsw.equals("")) {
            LogUtil.e(TAG, "loaclValidateModifyPsw->comfirm password is null");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 410017);
            throw new BaseException("comfirm password is null", errorInfo);
        }
        if (!comfirmPsw.equals(newPsw)) {
            LogUtil.e(TAG, "loaclValidateModifyPsw->passwords not equals");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 410018);
            throw new BaseException("passwords not equals", errorInfo);
        }
    }

    public void localValidatResetNewPsw(String newPsw) throws BaseException {
        this.localValidateNewPsw(newPsw);
    }

    public void localValidatCameraName(String cameraName) throws BaseException {
        if (cameraName == null || cameraName.equals("")) {
            LogUtil.e(TAG, "localValidatCameraName->camera name is null");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 410024);
            throw new BaseException("camera name is null", errorInfo);
        }
    }

    public void localValidatSerialNo(String serialNo) throws BaseException {
        if (TextUtils.isEmpty((CharSequence)serialNo)) {
            LogUtil.e(TAG, "localValidatSerialNo->camera serial no is null");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400001);
            throw new BaseException("camera serial no is null", errorInfo);
        }
        if (serialNo.length() != 9) {
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 400002);
            throw new BaseException("camera serial no is null", errorInfo);
        }
    }

    public void localValidatSerialNo(String serialNo, String password) throws BaseException {
        if (serialNo == null || serialNo.equals("")) {
            LogUtil.e(TAG, "localValidatSerialNo->camera serial no is null");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 410026);
            throw new BaseException("camera serial no is null", errorInfo);
        }
        if (password == null) {
            LogUtil.e(TAG, "localValidatSerialNo->camera password is null");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 410029);
            throw new BaseException("camera password is null", errorInfo);
        }
    }

    public void localValidateFixPhoneNum(String fixPhoneNum) throws BaseException {
        if (fixPhoneNum == null) {
            LogUtil.e(TAG, "localValidatePhoneNum->phone number is null");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 410019);
            throw new BaseException("localValidatePhoneNum->password is null", errorInfo);
        }
        if (fixPhoneNum.equals("")) {
            return;
        }
        String regEx = "[0-9]+\\-?[0-9]*+\\-?[0-9]*";
        Pattern pattern0 = Pattern.compile(regEx);
        if (!pattern0.matcher(fixPhoneNum).matches()) {
            LogUtil.e(TAG, "localValidateContact->contact contain illegal character");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 410022);
            throw new BaseException("contact contain illegal character", errorInfo);
        }
    }

    public void localValidateRegisterFixPhoneNum(String fixPhoneNum) throws BaseException {
        if (fixPhoneNum == null || fixPhoneNum.equals("")) {
            return;
        }
        String regEx = "[0-9]+\\-[0-9]+\\-?[0-9]*";
        Pattern pattern0 = Pattern.compile(regEx);
        if (!pattern0.matcher(fixPhoneNum).matches()) {
            LogUtil.e(TAG, "localValidateContact->contact contain illegal character");
            ErrorInfo errorInfo = ErrorLayer.getErrorLayer(2, 410020);
            throw new BaseException("contact contain illegal character", errorInfo);
        }
    }

    public int localValidatDeviceSerial(String deviceSerial) {
        int result = 0;
        if (TextUtils.isEmpty((CharSequence)deviceSerial)) {
            LogUtil.e(TAG, "localValidatSerialNo->camera serial no is null");
            result = 410026;
        }
        return result;
    }

    public int localValidateSmsCode(String smsCode) {
        int result = 0;
        if (TextUtils.isEmpty((CharSequence)smsCode)) {
            result = 400002;
        }
        return result;
    }

    public int localValidateMobileNumber(String mobileNumber) {
        int result = 0;
        if (TextUtils.isEmpty((CharSequence)mobileNumber) || mobileNumber.length() < 4) {
            result = 400002;
        }
        return result;
    }

    public int localValidateParam(String param) {
        int result = 0;
        if (TextUtils.isEmpty((CharSequence)param)) {
            result = 400002;
        }
        return result;
    }
}

