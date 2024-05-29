package me.smartfarm.common;

import static me.smartfarm.common.Constants.PASSWORD_SECRET_KEY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import me.smartfarm.R;
import me.smartfarm.data.models.User;

public class Util {
    private static User currentUser;

    public static Calendar getClearedUtc() {
        Calendar utc = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        utc.clear();
        return utc;
    }


    public static String formatNum(String number) {
        try {
            double value = Double.parseDouble(number);
            DecimalFormat decimalFormat = new DecimalFormat("#.00");
            return decimalFormat.format(value);
        } catch (NumberFormatException e) {
            return "Invalid number";
        }
    }

    public static double formatNum(double number) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return Double.parseDouble(decimalFormat.format(number));
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        Util.currentUser = currentUser;
    }

    public static boolean isValidEmail(String email) {
        // Define the email pattern
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Create a Pattern object
        Pattern pattern = Pattern.compile(emailPattern);

        // Create a Matcher object
        Matcher matcher = pattern.matcher(email);

        // Check if the email matches the pattern
        return matcher.matches();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encrypt(String strToEncrypt) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(PASSWORD_SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(strToEncrypt.getBytes());

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decrypt(String strToDecrypt) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(PASSWORD_SECRET_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = new byte[0];
        decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(strToDecrypt));

        return new String(decryptedBytes);
    }

    public static String encodeToBase64(Bitmap imageBitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return android.util.Base64.encodeToString(byteArray, android.util.Base64.DEFAULT);
    }

    public static Bitmap decodeFromBase64(String base64String) {
        byte[] decodedBytes = android.util.Base64.decode(base64String, android.util.Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


    public static String[] neighborhood(Context context, int position) {
        switch (position) {
            case 0:
                return context.getResources().getStringArray(R.array.amman_districts);
            case 1:
                return context.getResources().getStringArray(R.array.zarqa_districts);

            case 2:
                return context.getResources().getStringArray(R.array.irbid_districts);

            case 3:
                return context.getResources().getStringArray(R.array.aqaba_districts);

            case 4:
                return context.getResources().getStringArray(R.array.mafraq_districts);

            case 5:
                return context.getResources().getStringArray(R.array.madaba_districts);

            case 6:
                return context.getResources().getStringArray(R.array.salt_districts);

            case 7:
                return context.getResources().getStringArray(R.array.jerash_districts);

            case 8:
                return context.getResources().getStringArray(R.array.ajloun_districts);

            case 9:
                return context.getResources().getStringArray(R.array.maan_districts);

            case 10:
                return context.getResources().getStringArray(R.array.tafilah_districts);

            case 11:
                return context.getResources().getStringArray(R.array.karak_districts);

            default:
                return new String[]{};

        }
    }

    public static String timestampToDateTime(long timestamp) {
        // Create a SimpleDateFormat object with the desired format
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        // Create a Date object from the timestamp
        Date date = new Date(timestamp);

        // Use the SimpleDateFormat object to format the Date object as a string
        return sdf.format(date);
    }

    public static String getTimeAgo(long timestamp, Context context) {
        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime - timestamp;

        // Convert time difference to minutes, hours, days, weeks, months, or years
        long minutes = timeDifference / (60 * 1000);
        long hours = minutes / 60;
        long days = hours / 24;
        long weeks = days / 7;
        long months = days / 30;
        long years = days / 365;

        // Format output based on time difference
        if (timeDifference <= 10 * 60 * 1000) { // 10 minutes
            return "now";
        } else if (minutes < 60) {
            return minutes + " minutes ago";
        } else if (hours < 24) {
            return hours + " hours ago";
        } else if (days < 7) {
            return days + " days ago";
        } else if (weeks < 4) {
            return weeks + " weeks ago";
        } else if (months < 12) {
            return months + " months ago";
        } else {
            return years + " years ago";
        }
    }

    public static String getPricePerUnit(Context context, double price) {
        return context.getResources().getString(R.string.ton) + "/" + formatNum(price);
    }

    public static String convertArabicIndicToArabicNumerals(String input) {
        char[] arabicIndicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};
        char[] arabicChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

        StringBuilder builder = new StringBuilder();
        for (char c : input.toCharArray()) {
            boolean isArabicIndic = false;
            for (int i = 0; i < arabicIndicChars.length; i++) {
                if (c == arabicIndicChars[i]) {
                    builder.append(arabicChars[i]);
                    isArabicIndic = true;
                    break;
                }
            }
            if (!isArabicIndic) {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    public static String getAmount(Context context, String amount) {

        return String.format("(%s) %s", amount, context.getResources().getString(R.string.ton));
    }

    public static void makeFarmerView(View view) {
        if (Util.getCurrentUser().getUserTypeId() == Constants.FARMER_ROLE) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public static void makeTraderView(View view) {
        if (Util.getCurrentUser().getUserTypeId() == Constants.TRADER_ROLE) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public static String getUserFullName(String firstName, String lastName) {
        return String.format("%s %s", firstName, lastName);
    }

    public static String convertEnglishToArabicNumerals(String input) {
        char[] englishChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        char[] arabicIndicChars = {'٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩'};

        StringBuilder builder = new StringBuilder();
        for (char c : input.toCharArray()) {
            boolean isEnglish = false;
            for (int i = 0; i < englishChars.length; i++) {
                if (c == englishChars[i]) {
                    builder.append(arabicIndicChars[i]);
                    isEnglish = true;
                    break;
                }
            }
            if (!isEnglish) {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    public static Locale getLocale(Context context) {
        return context.getResources().getConfiguration().locale;
    }
}
