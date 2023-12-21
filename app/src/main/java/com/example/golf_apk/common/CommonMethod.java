package com.example.golf_apk.common;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class CommonMethod {
    private static final String KEY_ALIAS = "ilmselsfsefppqlooawl12301233";
    private static final String PREFERENCES_ALIAS = "com.example.golf";

    // 프래그먼트 내에 멤버 변수로 ProgressDialog 추가
    private static ProgressDialog progressDialog;

    // 로딩 표시
    public static void showLoading(Context context) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("로딩 중...");
            progressDialog.setCancelable(false); // 사용자가 취소할 수 없도록 설정
            progressDialog.show();
        }
    }

    // 로딩 숨기기
    public static void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


    public static void showAlert(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("알림")
                .setMessage(message)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // 확인 버튼을 눌렀을 때 수행할 작업
                        dialog.dismiss(); // 다이얼로그 닫기
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public static void saveAccessToken(Context context, String refreshToken) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_ALIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KeyType.ACCESS_TOKEN.getValue(), refreshToken);
        editor.apply();
    }

    public static String getAccessToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_ALIAS, Context.MODE_PRIVATE);
        return preferences.getString(KeyType.ACCESS_TOKEN.getValue(), null);
    }

    public static void saveRefreshToken(Context context, String accessToken) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            if (!keyStore.containsAlias(KEY_ALIAS)) {
                KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
                KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(
                        KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT
                )
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .setUserAuthenticationRequired(false)
                        .setRandomizedEncryptionRequired(false)
                        .build();

                keyGenerator.init(keyGenParameterSpec);
                keyGenerator.generateKey();
            }

            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" +
                    KeyProperties.BLOCK_MODE_CBC + "/" +
                    KeyProperties.ENCRYPTION_PADDING_PKCS7);

            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_ALIAS, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(accessToken.getBytes());

            String encryptedRefreshToken = Base64.encodeToString(encryptedBytes, Base64.DEFAULT);

            SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_ALIAS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(KeyType.REFRESH_TOKEN.getValue(), encryptedRefreshToken);
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getRefreshToken(Context context) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            Cipher cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" +
                    KeyProperties.BLOCK_MODE_CBC + "/" +
                    KeyProperties.ENCRYPTION_PADDING_PKCS7);

            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_ALIAS, null);
            cipher.init(Cipher.DECRYPT_MODE, key);

            SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_ALIAS, Context.MODE_PRIVATE);
            String encryptedRefreshToken = preferences.getString(KeyType.REFRESH_TOKEN.getValue(), null);

            byte[] encryptedBytes = Base64.decode(encryptedRefreshToken, Base64.DEFAULT);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getInfoFromToken(String key,String jwt) {
        Claims claims = Jwts.parser()
                .parseClaimsJws(jwt)
                .getBody();
        if (claims != null) {
            return claims.get(key, String.class);
        }
        return null;
    }

    public static void saveInfotoStorage(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_ALIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getInfoFromStorage(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_ALIAS, Context.MODE_PRIVATE);
        return preferences.getString(key, null);
    }

    public static void clearValueFromSharedPreferences(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_ALIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void clearAll(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_ALIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

}
