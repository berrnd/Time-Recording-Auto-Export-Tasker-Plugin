package de.berrnd.Time_Recording_Auto_Export_Tasker_Plugin;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class MailHelper {

    public static void ComposeTextMail(String to, String subject, String text, Activity callingActivity) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setData(Uri.parse("mailto:" + to));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        callingActivity.startActivity(intent);
    }

}
