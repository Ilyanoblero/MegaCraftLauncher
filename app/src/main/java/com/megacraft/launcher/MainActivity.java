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
    private static final int BG = 0xFF090D18;
    private static final int PANEL = 0xFF151D30;
    private static final int PANEL2 = 0xFF1D2942;
    private static final int ACCENT = 0xFF8B7CFF;
    private static final int CYAN = 0xFF63D9E8;
    private int page = 0;
    private EditText nickname;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(BG);
        getWindow().setNavigationBarColor(BG);
        render();
    }

    private int dp(float v) { return (int)(v * getResources().getDisplayMetrics().density + .5f); }
    private GradientDrawable shape(int color, int radius) {
        GradientDrawable d = new GradientDrawable(); d.setColor(color); d.setCornerRadius(dp(radius)); return d;
    }
    private GradientDrawable border(int color, int radius, int stroke) {
        GradientDrawable d = shape(color, radius); d.setStroke(dp(1), stroke); return d;
    }
    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this); t.setText(value); t.setTextSize(size); t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD); return t;
    }
    private LinearLayout column() {
        LinearLayout l = new LinearLayout(this); l.setOrientation(LinearLayout.VERTICAL); return l;
    }
    private LinearLayout.LayoutParams lp(int w, int h) {
        return new LinearLayout.LayoutParams(w < 0 ? w : dp(w), h < 0 ? h : dp(h));
    }

    private void render() {
        LinearLayout shell = column(); shell.setBackgroundColor(BG);
        LinearLayout header = new LinearLayout(this);
        header.setGravity(Gravity.CENTER_VERTICAL); header.setPadding(dp(20), dp(14), dp(20), dp(12));
        header.addView(text("✦", 27, ACCENT, true), lp(38, -2));
        LinearLayout brand = column();
        brand.addView(text("BLOCKHUB", 19, Color.WHITE, true));
        TextView sub = text("GAME LAUNCHER  •  ANDROID", 9, 0xFF91A0BD, true);
        sub.setLetterSpacing(.12f); brand.addView(sub);
        header.addView(brand, new LinearLayout.LayoutParams(0, -2, 1));
        TextView badge = text("BETA", 9, CYAN, true);
        badge.setPadding(dp(10), dp(6), dp(10), dp(6)); badge.setBackground(border(0x222DCEE4, 20, 0x6645C7D8));
        header.addView(badge); shell.addView(header, lp(-1, -2));

        ScrollView scroll = new ScrollView(this); scroll.setFillViewport(true);
        LinearLayout content = column(); content.setPadding(dp(18), dp(8), dp(18), dp(22));
        scroll.addView(content); shell.addView(scroll, new LinearLayout.LayoutParams(-1, 0, 1));
        if (page == 0) home(content); else if (page == 1) modules(content); else if (page == 2) library(content); else settings(content);

        LinearLayout nav = new LinearLayout(this);
        nav.setPadding(dp(8), dp(8), dp(8), dp(10)); nav.setBackgroundColor(0xFF11182A);
        String[] labels = {"⌂\nГлавная", "▦\nМодули", "▣\nБиблиотека", "⚙\nНастройки"};
        for (int i = 0; i < labels.length; i++) {
            final int target = i;
            TextView item = text(labels[i], 10, page == i ? CYAN : 0xFF7886A5, page == i);
            item.setGravity(Gravity.CENTER); item.setLineSpacing(dp(3), 1f); item.setPadding(0, dp(4), 0, dp(4));
            nav.addView(item, new LinearLayout.LayoutParams(0, dp(48), 1));
            item.setOnClickListener(v -> { page = target; render(); });
        }
        shell.addView(nav, lp(-1, -2)); setContentView(shell);
    }

    private void heading(LinearLayout root, String title, String subtitle) {
        TextView h = text(title, 24, Color.WHITE, true); h.setPadding(0, dp(15), 0, dp(4));
        root.addView(h, lp(-1, -2)); root.addView(text(subtitle, 12, 0xFF98A6C2, false), lp(-1, -2));
    }
    private LinearLayout card(LinearLayout root, int top) {
        LinearLayout box = column(); box.setPadding(dp(16), dp(16), dp(16), dp(16));
        box.setBackground(border(PANEL, 19, 0xFF293653));
        LinearLayout.LayoutParams p = lp(-1, -2); p.topMargin = dp(top); root.addView(box, p); return box;
    }
    private TextView action(String title, int color) {
        TextView b = text(title, 14, Color.WHITE, true); b.setGravity(Gravity.CENTER);
        b.setPadding(dp(12), dp(14), dp(12), dp(14)); b.setBackground(shape(color, 14)); b.setClickable(true); return b;
    }

    private void home(LinearLayout root) {
        heading(root, "Игровой центр", "Все твои игры и инструменты — в одном месте.");
        LinearLayout hero = card(root, 20);
        GradientDrawable heroBg = new GradientDrawable(GradientDrawable.Orientation.TL_BR, new int[]{0xFF352C69, 0xFF1B3454, 0xFF17253B});
        heroBg.setCornerRadius(dp(22)); hero.setBackground(heroBg);
        TextView eyebrow = text("РЕКОМЕНДУЕМЫЙ МОДУЛЬ  •  01", 9, 0xFFC4C0FF, true);
        eyebrow.setLetterSpacing(.1f); hero.addView(eyebrow, lp(-1, -2));
        TextView title = text("MEGACRAFT", 27, Color.WHITE, true); title.setPadding(0, dp(12), 0, dp(2));
        hero.addView(title, lp(-1, -2)); hero.addView(text("Pocket Edition  ·  клиент 0.3.0", 12, 0xFFD0D8F0, false), lp(-1, -2));
        TextView state = text(isInstalled() ? "●  УСТАНОВЛЕН" : "○  НЕ НАЙДЕН", 10, isInstalled() ? CYAN : 0xFFFFC477, true);
        state.setPadding(0, dp(15), 0, dp(12)); hero.addView(state, lp(-1, -2));
        TextView play = action("▶     ЗАПУСТИТЬ MEGACRAFT", ACCENT);
        hero.addView(play, lp(-1, 52)); play.setOnClickListener(v -> launchClient());

        LinearLayout quick = card(root, 14);
        quick.addView(text("БЫСТРЫЕ ДЕЙСТВИЯ", 10, 0xFF9BA9C6, true), lp(-1, -2));
        LinearLayout row = new LinearLayout(this);
        TextView server = action("⌁  Сервер", PANEL2), modulesBtn = action("▦  Модули", PANEL2);
        LinearLayout.LayoutParams half = new LinearLayout.LayoutParams(0, dp(48), 1); half.rightMargin = dp(5); row.addView(server, half);
        LinearLayout.LayoutParams half2 = new LinearLayout.LayoutParams(0, dp(48), 1); half2.leftMargin = dp(5); row.addView(modulesBtn, half2);
        LinearLayout.LayoutParams rowP = lp(-1, -2); rowP.topMargin = dp(12); quick.addView(row, rowP);
        server.setOnClickListener(v -> copyServer()); modulesBtn.setOnClickListener(v -> { page = 1; render(); });

        LinearLayout nickCard = card(root, 14);
        nickCard.addView(text("ИГРОВОЙ ПРОФИЛЬ", 10, 0xFF9BA9C6, true), lp(-1, -2));
        nickname = new EditText(this); nickname.setSingleLine(true); nickname.setTextColor(Color.WHITE);
        nickname.setHintTextColor(0xFF7F8BA6); nickname.setHint("Твой никнейм");
        nickname.setText(getPreferences(MODE_PRIVATE).getString("nickname", "")); nickname.setTextSize(14);
        nickname.setPadding(dp(13), 0, dp(13), 0); nickname.setBackground(border(PANEL2, 12, 0xFF354361));
        LinearLayout.LayoutParams np = lp(-1, 49); np.topMargin = dp(10); nickCard.addView(nickname, np);
        TextView save = action("СОХРАНИТЬ НИК", 0xFF263A59);
        LinearLayout.LayoutParams sp = lp(-1, 44); sp.topMargin = dp(8); nickCard.addView(save, sp);
        save.setOnClickListener(v -> {
            getPreferences(MODE_PRIVATE).edit().putString("nickname", nickname.getText().toString().trim()).apply();
            Toast.makeText(this, "Ник сохранён", Toast.LENGTH_SHORT).show();
        });

        LinearLayout news = card(root, 14);
        news.addView(text("✧  ПОДСКАЗКА", 11, CYAN, true), lp(-1, -2));
        TextView tip = text("BLOCKHUB — оболочка для игровых модулей. MegaCraft уже подключён как отдельный модуль.", 12, 0xFFB4C0D8, false);
        tip.setPadding(0, dp(7), 0, 0); news.addView(tip, lp(-1, -2));
    }

    private void modules(LinearLayout root) {
        heading(root, "Модули", "Подключай игры как отдельные модули лаунчера.");
        LinearLayout module = card(root, 18);
        LinearLayout top = new LinearLayout(this); top.setGravity(Gravity.CENTER_VERTICAL);
        TextView icon = text("▦", 36, CYAN, true); icon.setGravity(Gravity.CENTER); top.addView(icon, lp(58, 58));
        LinearLayout info = column(); info.addView(text("MegaCraft", 19, Color.WHITE, true));
        info.addView(text("Minecraft: Pocket Edition · 0.3.0", 11, 0xFF9EACC7, false));
        top.addView(info, new LinearLayout.LayoutParams(0, -2, 1));
        TextView active = text("MODULE", 9, CYAN, true); active.setPadding(dp(9), dp(6), dp(9), dp(6));
        active.setBackground(shape(0x223DD1E2, 20)); top.addView(active); module.addView(top, lp(-1, -2));
        View divider = new View(this); divider.setBackgroundColor(0xFF2A3651);
        LinearLayout.LayoutParams dp1 = lp(-1, 1); dp1.topMargin = dp(14); dp1.bottomMargin = dp(14); module.addView(divider, dp1);
        module.addView(text("СОСТОЯНИЕ", 9, 0xFF8E9DBB, true), lp(-1, -2));
        module.addView(text(isInstalled() ? "Клиент обнаружен на устройстве." : "Клиент не обнаружен. Установи его отдельно.", 12, isInstalled() ? CYAN : 0xFFFFC477, false), lp(-1, -2));
        TextView launch = action("▶  ОТКРЫТЬ МОДУЛЬ", ACCENT);
        LinearLayout.LayoutParams bp = lp(-1, 48); bp.topMargin = dp(14); module.addView(launch, bp);
        launch.setOnClickListener(v -> launchClient());

        LinearLayout note = card(root, 14);
        note.addView(text("КАК РАБОТАЕТ СИСТЕМА МОДУЛЕЙ", 10, CYAN, true), lp(-1, -2));
        note.addView(text("Каждая игра оформляется отдельной карточкой со своей версией, статусом и действиями. Сейчас доступен модуль MegaCraft. Другие игры можно будет подключить позже.", 12, 0xFFB4C0D8, false), lp(-1, -2));
        TextView refresh = action("ОБНОВИТЬ СТАТУС", PANEL2);
        LinearLayout.LayoutParams rp = lp(-1, 44); rp.topMargin = dp(12); note.addView(refresh, rp);
        refresh.setOnClickListener(v -> { Toast.makeText(this, isInstalled() ? "MegaCraft найден" : "MegaCraft не найден", Toast.LENGTH_SHORT).show(); render(); });
    }

    private void library(LinearLayout root) {
        heading(root, "Библиотека", "Твои игровые модули и их состояние.");
        LinearLayout item = card(root, 18);
        LinearLayout row = new LinearLayout(this); row.setGravity(Gravity.CENTER_VERTICAL);
        TextView icon = text("▦", 34, ACCENT, true); row.addView(icon, lp(52, 52));
        LinearLayout info = column(); info.addView(text("MegaCraft", 17, Color.WHITE, true));
        info.addView(text("Версия 0.3.0 · Android", 11, 0xFF9EACC7, false));
        row.addView(info, new LinearLayout.LayoutParams(0, -2, 1)); item.addView(row, lp(-1, -2));
        TextView state = text(isInstalled() ? "●  УСТАНОВЛЕН" : "○  НУЖНО УСТАНОВИТЬ ОТДЕЛЬНО", 10, isInstalled() ? CYAN : 0xFFFFC477, true);
        state.setPadding(0, dp(12), 0, dp(12)); item.addView(state, lp(-1, -2));
        TextView open = action("ОТКРЫТЬ", ACCENT); item.addView(open, lp(-1, 46)); open.setOnClickListener(v -> launchClient());
        LinearLayout infoCard = card(root, 14);
        infoCard.addView(text("ПОКА ОДНА ИГРА", 11, CYAN, true), lp(-1, -2));
        infoCard.addView(text("Список будет расширяться по мере добавления новых модулей. Лаунчер не скачивает игры автоматически и не меняет их файлы.", 12, 0xFFB4C0D8, false), lp(-1, -2));
    }

    private void settings(LinearLayout root) {
        heading(root, "Настройки", "Настрой лаунчер под себя.");
        LinearLayout general = card(root, 18);
        general.addView(text("ОБЩИЕ", 10, CYAN, true), lp(-1, -2));
        TextView nick = action("РЕДАКТИРОВАТЬ НИК НА ГЛАВНОЙ", PANEL2);
        LinearLayout.LayoutParams np = lp(-1, 46); np.topMargin = dp(10); general.addView(nick, np);
        nick.setOnClickListener(v -> { page = 0; render(); });
        TextView server = action("СКОПИРОВАТЬ АДРЕС СЕРВЕРА", PANEL2);
        LinearLayout.LayoutParams sp = lp(-1, 46); sp.topMargin = dp(8); general.addView(server, sp);
        server.setOnClickListener(v -> copyServer());

        LinearLayout about = card(root, 14);
        about.addView(text("О ЛАУНЧЕРЕ", 10, CYAN, true), lp(-1, -2));
        about.addView(text("BLOCKHUB Launcher · версия 1.2 beta\nНеофициальный проект. Игровые клиенты устанавливаются отдельно. Лаунчер не является разработчиком MegaCraft.", 12, 0xFFB4C0D8, false), lp(-1, -2));
        TextView status = action("ПРОВЕРИТЬ MEGACRAFT", PANEL2);
        LinearLayout.LayoutParams stp = lp(-1, 46); stp.topMargin = dp(12); about.addView(status, stp);
        status.setOnClickListener(v -> new android.app.AlertDialog.Builder(this).setTitle("Статус MegaCraft")
            .setMessage(isInstalled() ? "Клиент найден: " + CLIENT : "Клиент не найден. Установи MegaCraft 0.3.0 отдельно.")
            .setPositiveButton("ОК", null).show());
    }

    private boolean isInstalled() {
        try { getPackageManager().getPackageInfo(CLIENT, 0); return true; }
        catch (PackageManager.NameNotFoundException e) { return false; }
    }
    private void launchClient() {
        getPreferences(MODE_PRIVATE).edit().putString("nickname",
            nickname == null ? getPreferences(MODE_PRIVATE).getString("nickname", "") : nickname.getText().toString().trim()).apply();
        Intent intent = getPackageManager().getLaunchIntentForPackage(CLIENT);
        if (intent != null) {
            try { startActivity(intent); }
            catch (Exception e) { Toast.makeText(this, "Не удалось открыть MegaCraft", Toast.LENGTH_LONG).show(); }
        } else {
            new android.app.AlertDialog.Builder(this).setTitle("Модуль не установлен")
                .setMessage("Установи MegaCraft 0.3.0 отдельно. BLOCKHUB — лаунчер и не содержит игровые файлы.")
                .setPositiveButton("Понятно", null).show();
        }
    }
    private void copyServer() {
        ClipboardManager cm = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText("MegaCraft server", SERVER));
        Toast.makeText(this, "Адрес " + SERVER + " скопирован", Toast.LENGTH_SHORT).show();
    }
}
