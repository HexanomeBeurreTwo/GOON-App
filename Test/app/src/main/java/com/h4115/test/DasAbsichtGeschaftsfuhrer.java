package com.h4115.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DasAbsichtGeschaftsfuhrer {

    public static void LaunchLogin(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        ((Activity)context).finish();
    }

    public static void LaunchMain(Context context, User user){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("id", user.getUserId());
        intent.putExtra("username",user.getUsername());
        intent.putExtra("password", user.getPassword());
        intent.putExtra("email", user.getEmail());
        intent.putExtra("age", user.getAge());
        intent.putExtra("citizen", user.getCitizen());
        intent.putExtra("tags", user.getTags());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        ((Activity)context).finish();
    }

    public static void LaunchSubscribeChannels(Context context, String result, User user){
        Intent intent = new Intent(context, SubscribeChannels.class);
        intent.putExtra("result", result);
        intent.putExtra("id", user.getUserId());
        intent.putExtra("username",user.getUsername());
        intent.putExtra("password", user.getPassword());
        intent.putExtra("email", user.getEmail());
        intent.putExtra("age", user.getAge());
        intent.putExtra("citizen", user.getCitizen());
        intent.putExtra("tags", user.getTags());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
