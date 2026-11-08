package com.megacraft.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class MainActivity extends Activity {
    private static final String CLIENT = "com.lituty.megacraftpe";
    private static final String SERVER = "latecraft.ru";
    private static final int BG = 0xFF0B1210;
    private static final int PANEL = 0xFF17251F;
    private static final int GREEN = 0xFF9BE56A;
    private EditText nickname;
    private TextView status, statusDot, tipText;

    private final String[] tips = {
        "Совет: придумай ник, который друзьям будет легко узнать.",
        "Совет: перед запуском проверь, что клиент MegaCraft установлен.",
        "Совет: адрес сервера можно скопировать одной кнопкой.",
        "Совет: сохрани свой ник — он останется в лаунчере."
    };
    private int tipIndex = 0;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);
        buildUi();
    }

    private int dp(float v) {
        return (int)(v * getResources().getDisplayMetrics().density + .5f);
    }

    private GradientDrawable shape(int color, int radius) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color);
        d.setCornerRadius(dp(radius));
        return d;
    }

    private GradientDrawable outline(int color, int radius, int strokeColor) {
        GradientDrawable d = shape(color, radius);
        d.setStroke(dp(1), strokeColor);
        return d;
    }

    private TextView label(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }

    private LinearLayout column() {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.VERTICAL);
        return l;
    }

    private LinearLayout.LayoutParams lp(int width, int height) {
        return new LinearLayout.LayoutParams(width < 0 ? width : dp(width), height < 0 ? height : dp(height));
    }

    private void buildUi() {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setClipToPadding(false);

        LinearLayout root = column();
        root.setPadding(dp(20), dp(18), dp(20), dp(28));
        GradientDrawable background = new GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            new int[]{0xFF1B3829, BG, 0xFF101A22}
        );
        root.setBackground(background);
        scroll.addView(root);
        setContentView(scroll);

        LinearLayout top = new LinearLayout(this);
        top.setGravity(Gravity.CENTER_VERTICAL);
        TextView logo = label("✦", 29, GREEN, true);
        top.addView(logo, lp(38, 42));
        LinearLayout titleBox = column();
        TextView brand = label("MEGACRAFT", 22, Color.WHITE, true);
        brand.setLetterSpacing(.12f);
        titleBox.addView(brand, lp(-1, -2));
        TextView edition = label("POCKET EDITION  /  LAUNCHER", 9, 0xFF9BB8A6, true);
        edition.setLetterSpacing(.12f);
        titleBox.addView(edition, lp(-1, -2));
        top.addView(titleBox, new LinearLayout.LayoutParams(0, -2, 1));
        TextView versionBadge = label("v1.1", 11, GREEN, true);
        versionBadge.setGravity(Gravity.CENTER);
        versionBadge.setPadding(dp(12), dp(7), dp(12), dp(7));
        versionBadge.setBackground(outline(0x3324D36B, 30, 0x6655B85D));
        top.addView(versionBadge);
        root.addView(top, lp(-1, -2));

        TextView welcome = label("ТВОЁ ПРИКЛЮЧЕНИЕ\nНАЧИНАЕТСЯ ЗДЕСЬ", 25, Color.WHITE, true);
        welcome.setLineSpacing(dp(2), 1f);
        welcome.setPadding(0, dp(27), 0, dp(5));
        root.addView(welcome, lp(-1, -2));
        TextView sub = label("Запускай MegaCraft и возвращайся в свой мир.", 13, 0xFFB0C2B7, false);
        root.addView(sub, lp(-1, -2));

        LinearLayout hero = column();
        hero.setPadding(dp(18), dp(18), dp(18), dp(18));
        hero.setBackground(outline(0xFF1C3026, 22, 0xFF355640));
        LinearLayout.LayoutParams heroParams = lp(-1, -2);
        heroParams.topMargin = dp(22);
        root.addView(hero, heroParams);

        LinearLayout heroTop = new LinearLayout(this);
        heroTop.setGravity(Gravity.CENTER_VERTICAL);
        TextView cube = label("▦", 47, GREEN, true);
        cube.setGravity(Gravity.CENTER);
        heroTop.addView(cube, lp(65, 65));
        LinearLayout gameInfo = column();
        TextView gameName = label("MEGACRAFT", 17, Color.WHITE, true);
        gameInfo.addView(gameName, lp(-1, -2));
        TextView gameVersion = label("Версия клиента 0.3.0", 12, 0xFFB1C6B8, false);
        gameInfo.addView(gameVersion, lp(-1, -2));
        heroTop.addView(gameInfo, new LinearLayout.LayoutParams(0, -2, 1));
        TextView tag = label("CLIENT", 9, GREEN, true);
        tag.setPadding(dp(9), dp(6), dp(9), dp(6));
        tag.setBackground(shape(0x3325D366, 20));
        heroTop.addView(tag);
        hero.addView(heroTop, lp(-1, -2));

        View line = new View(this);
        line.setBackgroundColor(0x334E765B);
        LinearLayout.LayoutParams lineParams = lp(-1, 1);
        lineParams.topMargin = dp(15);
        lineParams.bottomMargin = dp(13);
        hero.addView(line, lineParams);

        LinearLayout statusRow = new LinearLayout(this);
        statusRow.setGravity(Gravity.CENTER_VERTICAL);
        statusDot = label("●", 12, 0xFFFFC46B, true);
        statusRow.addView(statusDot, lp(20, -2));
        status = label("Проверяем клиент…", 12, 0xFFB8C8BF, true);
        statusRow.addView(status, new LinearLayout.LayoutParams(0, -2, 1));
        TextView refresh = label("↻", 22, GREEN, true);
        refresh.setGravity(Gravity.CENTER);
        refresh.setOnClickListener(v -> refreshStatus());
        statusRow.addView(refresh, lp(35, 35));
        hero.addView(statusRow, lp(-1, -2));

        nickname = new EditText(this);
        nickname.setSingleLine(true);
        nickname.setTextColor(Color.WHITE);
        nickname.setHintTextColor(0xFF81968A);
        nickname.setHint("Введи свой игровой ник");
        nickname.setText(getPreferences(MODE_PRIVATE).getString("nickname", ""));
        nickname.setTextSize(14);
        nickname.setPadding(dp(15), 0, dp(15), 0);
        nickname.setBackground(outline(PANEL, 14, 0xFF30483A));
        LinearLayout.LayoutParams nickParams = lp(-1, 53);
        nickParams.topMargin = dp(17);
        root.addView(nickname, nickParams);

        TextView play = label("▶     ИГРАТЬ", 15, 0xFF142012, true);
        play.setGravity(Gravity.CENTER);
        play.setBackground(shape(GREEN, 16));
        LinearLayout.LayoutParams playParams = lp(-1, 58);
        playParams.topMargin = dp(12);
        root.addView(play, playParams);
        play.setElevation(dp(4));
        play.setOnClickListener(v -> launchClient());

        TextView quickTitle = label("БЫСТРЫЕ ДЕЙСТВИЯ", 10, 0xFF91A99A, true);
        quickTitle.setLetterSpacing(.13f);
        LinearLayout.LayoutParams qTitleParams = lp(-1, -2);
        qTitleParams.topMargin = dp(26);
        qTitleParams.bottomMargin = dp(11);
        root.addView(quickTitle, qTitleParams);

        LinearLayout actions = new LinearLayout(this);
        actions.setOrientation(LinearLayout.HORIZONTAL);
        TextView server = actionTile("⌁", "Сервер", "Копировать адрес");
        TextView help = actionTile("?", "Помощь", "Как начать играть");
        LinearLayout.LayoutParams tile1 = new LinearLayout.LayoutParams(0, dp(92), 1);
        tile1.rightMargin = dp(6);
        LinearLayout.LayoutParams tile2 = new LinearLayout.LayoutParams(0, dp(92), 1);
        tile2.leftMargin = dp(6);
        actions.addView(server, tile1);
        actions.addView(help, tile2);
        root.addView(actions, lp(-1, -2));
        server.setOnClickListener(v -> copyServer());
        help.setOnClickListener(v -> showHelp());

        LinearLayout tipCard = column();
        tipCard.setPadding(dp(15), dp(13), dp(15), dp(13));
        tipCard.setBackground(shape(0xFF18251F, 16));
        LinearLayout.LayoutParams tipParams = lp(-1, -2);
        tipParams.topMargin = dp(13);
        root.addView(tipCard, tipParams);
        TextView tipHead = label("✧  ПОДСКАЗКА", 10, GREEN, true);
        tipCard.addView(tipHead, lp(-1, -2));
        tipText = label(tips[0], 12, 0xFFB9C9BF, false);
        tipText.setPadding(0, dp(6), 0, dp(4));
        tipCard.addView(tipText, lp(-1, -2));
        TextView nextTip = label("ДРУГАЯ ПОДСКАЗКА  ↗", 9, 0xFF91B77F, true);
        nextTip.setPadding(0, dp(7), 0, 0);
        tipCard.addView(nextTip, lp(-1, -2));
        nextTip.setOnClickListener(v -> {
            tipIndex = (tipIndex + 1) % tips.length;
            tipText.setText(tips[tipIndex]);
        });

        TextView footer = label("MEGACRAFT LAUNCHER  •  НЕОФИЦИАЛЬНЫЙ ПРОЕКТ", 9, 0xFF63776C, true);
        footer.setGravity(Gravity.CENTER);
        footer.setPadding(0, dp(24), 0, 0);
        root.addView(footer, lp(-1, -2));

        refreshStatus();
    }

    private TextView actionTile(String icon, String title, String subtitle) {
        LinearLayout box = column();
        box.setGravity(Gravity.CENTER_VERTICAL);
        box.setPadding(dp(14), dp(10), dp(10), dp(10));
        box.setBackground(outline(0xFF192820, 16, 0xFF304B39));
        TextView iconView = label(icon, 21, GREEN, true);
        box.addView(iconView, lp(-1, 25));
        TextView titleView = label(title, 14, Color.WHITE, true);
        box.addView(titleView, lp(-1, -2));
        TextView subView = label(subtitle, 10, 0xFF91A69A, false);
        box.addView(subView, lp(-1, -2));
        return wrapAsTextView(box);
    }

    // A clickable compact tile built from a custom TextView keeps the layout lightweight.
    private TextView wrapAsTextView(LinearLayout source) {
        TextView tile = label("", 12, Color.WHITE, false);
        tile.setVisibility(View.GONE);
        source.setFocusable(true);
        source.setClickable(true);
        source.setOnTouchListener((v, event) -> {
            if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) v.setAlpha(.75f);
            if (event.getAction() == android.view.MotionEvent.ACTION_UP || event.getAction() == android.view.MotionEvent.ACTION_CANCEL) v.setAlpha(1f);
            return false;
        });
        // Return the actual layout through a lightweight subclass is unnecessary; use a FrameLayout.
        android.widget.FrameLayout frame = new android.widget.FrameLayout(this);
        frame.addView(source, new android.widget.FrameLayout.LayoutParams(-1, -1));
        frame.setBackground(source.getBackground());
        frame.setClickable(true);
        frame.setFocusable(true);
        frame.setOnClickListener(v -> source.performClick());
        source.setOnClickListener(v -> frame.performClick());
        return new TileTextView(this, frame);
    }

    private class TileTextView extends TextView {
        private final android.widget.FrameLayout content;
        TileTextView(Context context, android.widget.FrameLayout content) {
            super(context);
            this.content = content;
            setVisibility(View.GONE);
        }
    }

    private boolean isInstalled() {
        try {
            getPackageManager().getPackageInfo(CLIENT, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void refreshStatus() {
        if (status == null) return;
        boolean installed = isInstalled();
        status.setText(installed ? "Клиент установлен — можно запускать" : "Клиент не найден на устройстве");
        status.setTextColor(installed ? GREEN : 0xFFFFC46B);
        statusDot.setTextColor(installed ? GREEN : 0xFFFFC46B);
    }

    private void launchClient() {
        String name = nickname.getText().toString().trim();
        getPreferences(MODE_PRIVATE).edit().putString("nickname", name).apply();
        Intent intent = getPackageManager().getLaunchIntentForPackage(CLIENT);
        if (intent != null) {
            startActivity(intent);
        } else {
            new android.app.AlertDialog.Builder(this)
                .setTitle("MegaCraft пока не установлен")
                .setMessage("Установи MegaCraft 0.3.0 отдельно. Этот лаунчер не содержит игровой клиент. После установки вернись сюда и нажми «Играть».")
                .setPositiveButton("Понятно", null)
                .show();
        }
        refreshStatus();
    }

    private void copyServer() {
        ClipboardManager cm = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText("MegaCraft server", SERVER));
        Toast.makeText(this, "Адрес " + SERVER + " скопирован", Toast.LENGTH_SHORT).show();
    }

    private void showHelp() {
        new android.app.AlertDialog.Builder(this)
            .setTitle("Как начать играть")
            .setMessage("1. Установи MegaCraft 0.3.0 отдельно.\n2. Введи свой ник в лаунчере.\n3. Нажми «Играть».\n4. Кнопка «Сервер» копирует адрес " + SERVER + ".\n\nЛаунчер не устанавливает и не изменяет игровой клиент.")
            .setPositiveButton("Понятно", null)
            .show();
    }
}
