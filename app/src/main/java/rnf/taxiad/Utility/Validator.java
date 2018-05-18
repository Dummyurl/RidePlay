package rnf.taxiad.Utility;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.EditText;

import rnf.taxiad.R;

/**
 * Created by Rahil on 13/1/16.
 */

public final class Validator {

    public enum Type {
        USERNAME, EMAIL, PASSWORD, NAME, LASTNAME, MOBILE, REFERRAL, PAYPAL, COMMENT, PHONE, CITY, NEWPASS, CONPASS
    }

    public static boolean isValid(EditText edt, Type type) {
        boolean isValid = true;
        switch (type) {
            case USERNAME:
                isValid = isValidUsername(edt);
                break;
            case EMAIL:
                isValid = isValidEmail(edt);
                break;
            case PASSWORD:
                isValid = isValidPassword(edt);
                break;
            case NAME:
                isValid = isValidName(edt);
                break;
            case LASTNAME:
                isValid = isValidLastName(edt);
                break;
            case MOBILE:
                isValid = isValidMobile(edt);
                break;
            case REFERRAL:
                isValid = isValidReferral(edt);
                break;
            case PAYPAL:
                isValid = isValidPayPal(edt);
                break;
            case COMMENT:
//                isValid = isVa(edt);
                break;
            case PHONE:
                isValid = isValidePhone(edt);
                break;
            case CITY:
                isValid = isValidCity(edt);
                break;
        }
        return isValid;
    }

    public static boolean isValidPass(EditText edtnew, EditText edtcon, Type type) {
        boolean isValid = true;
        switch (type) {
            case CONPASS:
                isValid = isValidConPass(edtnew, edtcon);
                break;
        }
        return isValid;
    }

    private static boolean isValidePhone(EditText edt) {
        if (!TextUtils.isEmpty(edt.getText().toString().trim()) &&
                Patterns.PHONE.matcher(edt.getText().toString().trim()).matches())
            return true;
        edt.requestFocus();
        ToastAlert.alertShort(edt.getRootView(), R.string.validate_phone);
        return false;
    }

    private static boolean isValidUsername(EditText edt) {
        if (!TextUtils.isEmpty(edt.getText().toString().trim()))
            return true;
        edt.requestFocus();
        ToastAlert.alertShort(edt.getRootView(), R.string.validate_username);
        return false;
    }

    private static boolean isValidCity(EditText edt) {
        if (!TextUtils.isEmpty(edt.getText().toString().trim()))
            return true;
        edt.requestFocus();
        ToastAlert.alertShort(edt.getRootView(), R.string.validate_city);
        return false;
    }

    private static boolean isValidName(EditText edt) {
        if (!TextUtils.isEmpty(edt.getText().toString().trim()))
            return true;
        edt.requestFocus();
        ToastAlert.alertShort(edt.getRootView(), R.string.validate_name);
        return false;
    }

    private static boolean isValidLastName(EditText edt) {
        if (!TextUtils.isEmpty(edt.getText().toString().trim()))
            return true;
        edt.requestFocus();
        ToastAlert.alertShort(edt.getRootView(), R.string.validate_last_name);
        return false;
    }

    private static boolean isValidPassword(EditText edt) {
        if (!TextUtils.isEmpty(edt.getText().toString().trim()))
            return true;
        edt.requestFocus();
        ToastAlert.alertShort(edt.getRootView(), R.string.validate_password);
        return false;
    }

    private static boolean isValidEmail(EditText edt) {
        if (!TextUtils.isEmpty(edt.getText().toString().trim()) &&
                Patterns.EMAIL_ADDRESS.matcher(edt.getText().toString().trim()).matches())
            return true;
        edt.requestFocus();
        ToastAlert.alertShort(edt.getRootView(), R.string.validate_email);
        return false;
    }

    private static boolean isValidMobile(EditText edt) {
        if (!TextUtils.isEmpty(edt.getText().toString().trim()) &&
                Patterns.PHONE.matcher(edt.getText().toString().trim()).matches())
            return true;
        edt.requestFocus();
        ToastAlert.alertShort(edt.getRootView(), R.string.validate_mobile);
        return false;
    }


    private static boolean isValidConPass(EditText edtnew, EditText edtcon) {
        if ((TextUtils.isEmpty(edtnew.getText().toString().trim())) && (TextUtils.isEmpty(edtcon.getText().toString().trim()))) {
            return true;
        } else if ((!TextUtils.isEmpty(edtnew.getText().toString().trim())) && (TextUtils.isEmpty(edtcon.getText().toString().trim()))) {
            edtcon.requestFocus();
            ToastAlert.alertShort(edtcon.getRootView(), R.string.validate_con_pass);
            return false;
        } else if ((TextUtils.isEmpty(edtnew.getText().toString().trim())) && (!TextUtils.isEmpty(edtcon.getText().toString().trim()))) {
            edtcon.requestFocus();
            ToastAlert.alertShort(edtcon.getRootView(), R.string.validate_new_pass);
            return false;
        } else if ((!TextUtils.isEmpty(edtnew.getText().toString().trim())) && (!TextUtils.isEmpty(edtcon.getText().toString().trim()))) {
            if (edtnew.getText().toString().trim().equals(edtcon.getText().toString().trim())) {
                return true;
            } else {
                edtcon.requestFocus();
                ToastAlert.alertShort(edtcon.getRootView(), R.string.validate_match_pass);
                return false;
            }
        }
        return false;
    }

    private static boolean isValidReferral(EditText edt) {
        if (!TextUtils.isEmpty(edt.getText().toString().trim()))
            return true;
        edt.requestFocus();
        ToastAlert.alertShort(edt.getRootView(), R.string.validate_referral_code);
        return false;
    }

    private static boolean isValidPayPal(EditText edt) {
        if (!TextUtils.isEmpty(edt.getText().toString().trim()))
            return true;
        edt.requestFocus();
        ToastAlert.alertShort(edt.getRootView(), R.string.validate_paypal_id);
        return false;
    }

    public static boolean isValidConfirmPassword(EditText edtPassword, EditText
            edtConfirmPassword) {
        if (edtPassword.getText().toString().equals(edtConfirmPassword.getText().toString()))
            return true;
        edtConfirmPassword.requestFocus();
        ToastAlert.alertShort(edtConfirmPassword.getRootView(), R.string.validate_confirm_password);
        return false;
    }

    private static boolean isNotEmpty(String text) {
        if (text != null && text.trim().length() != 0)
            return true;
        return false;
    }


    private static class ValidationWatcher implements TextWatcher {
        private EditText edt;

        public ValidationWatcher(EditText edt) {
            this.edt = edt;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            edt.setError(null);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
