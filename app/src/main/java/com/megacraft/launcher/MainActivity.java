package com.megacraft.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.*;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class MainActivity extends Activity {
    private static final String CLIENT = "com.lituty.megacraftpe";
    private EditText nickname;
    private TextView status;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(16,26,24));
        getWindow().setNavigationBarColor(Color.rgb(16,26,24));
        buildUi();
    }

    private int dp(float v) { return (int)(v * getResources().getDisplayMetrics().density + .5f); }
    private GradientDrawable bg(int color, int radius) {
        GradientDrawable d = new GradientDrawable(); d.setColor(color); d.setCornerRadius(dp(radius)); return d;
    }
    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this); t.setText(value); t.setTextSize(size); t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }
    private void buildUi() {
        ScrollView scroll = new ScrollView(this); scroll.setFillViewport(true);
        LinearLayout root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(22), dp(22), dp(22), dp(26));
        GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.rgb(25,48,39), Color.rgb(16,26,24), Color.rgb(12,19,20)});
        root.setBackground(gradient); scroll.addView(root); setContentView(scroll);

        TextView brand = text("MEGACRAFT", 30, Color.WHITE, true);
        brand.setLetterSpacing(.12f); brand.setGravity(Gravity.CENTER); root.addView(brand, matchWrap());
        TextView subtitle = text("POCKET EDITION  •  LAUNCHER", 11, 0xFF9BB8A6, true);
        subtitle.setGravity(Gravity.CENTER); subtitle.setPadding(0, dp(5), 0, dp(22)); root.addView(subtitle, matchWrap());

        LinearLayout hero = card(); hero.setOrientation(LinearLayout.VERTICAL); hero.setGravity(Gravity.CENTER);
        TextView cube = text("▦", 66, 0xFF9BE56A, true); cube.setGravity(Gravity.CENTER); hero.addView(cube, matchWrap());
        TextView version = text("MEGACRAFT 0.3.0", 18, Color.WHITE, true); version.setGravity(Gravity.CENTER); version.setPadding(0,dp(3),0,dp(4)); hero.addView(version, matchWrap());
        TextView desc = text("Твой мир начинается здесь", 13, 0xFFB6C8BE, false); desc.setGravity(Gravity.CENTER); hero.addView(desc, matchWrap());
        LinearLayout.LayoutParams hp = new LinearLayout.LayoutParams(-1, dp(180)); hp.bottomMargin = dp(18); root.addView(hero, hp);

        nickname = new EditText(this); nickname.setSingleLine(true); nickname.setTextColor(Color.WHITE);
        nickname.setHintTextColor(0xFF8EA198); nickname.setHint("Имя игрока (необязательно)");
        nickname.setText(getPreferences(MODE_PRIVATE).getString("nickname", ""));
        nickname.setPadding(dp(15), 0, dp(15), 0); nickname.setTextSize(14); nickname.setBackground(bg(0xFF20332C, 14));
        LinearLayout.LayoutParams np = new LinearLayout.LayoutParams(-1, dp(52)); np.bottomMargin = dp(12); root.addView(nickname,np);

        Button play = button("▶   ИГРАТЬ", 0xFF91DF5B, 0xFF172316); root.addView(play, buttonParams(58, 12));
        play.setOnClickListener(v -> launchClient());

        status = text("Проверяем установку клиента…", 12, 0xFFB7C9BF, false);
        status.setGravity(Gravity.CENTER); status.setPadding(0,dp(12),0,dp(18)); root.addView(status, matchWrap()); refreshStatus();

        TextView section = text("БЫСТРЫЙ ДОСТУП", 11, 0xFF8FA99A, true); section.setLetterSpacing(.1f); section.setPadding(dp(2),dp(5),0,dp(10)); root.addView(section,matchWrap());
        LinearLayout row = new LinearLayout(this); row.setOrientation(LinearLayout.HORIZONTAL);
        Button server = button("СЕРВЕР",0xFF294438,Color.WHITE); Button settings = button("НАСТРОЙКИ",0xFF294438,Color.WHITE);
        LinearLayout.LayoutParams half = new LinearLayout.LayoutParams(0,dp(48),1); half.rightMargin=dp(6); row.addView(server,half);
        LinearLayout.LayoutParams half2 = new LinearLayout.LayoutParams(0,dp(48),1); half2.leftMargin=dp(6); row.addView(settings,half2); root.addView(row);
        server.setOnClickListener(v -> copyServer());
        settings.setOnClickListener(v -> showSettings());

        TextView footer = text("MegaCraft Launcher 1.0  •  Неофициальный лаунчер", 10, 0xFF71857B, false);
        footer.setGravity(Gravity.CENTER); footer.setPadding(0,dp(24),0,0); root.addView(footer,matchWrap());
    }
    private LinearLayout card() { LinearLayout l = new LinearLayout(this); l.setPadding(dp(16),dp(16),dp(16),dp(16)); l.setBackground(bg(0xFF20382E,20)); return l; }
    private Button button(String label, int color, int fg) {
        Button b = new Button(this); b.setText(label); b.setTextSize(13); b.setTextColor(fg); b.setAllCaps(false);
        b.setTypeface(Typeface.DEFAULT,Typeface.BOLD); b.setBackground(bg(color,14));
        if (android.os.Build.VERSION.SDK_INT >= 21) b.setElevation(dp(2)); return b;
    }
    private LinearLayout.LayoutParams matchWrap() { return new LinearLayout.LayoutParams(-1,-2); }
    private LinearLayout.LayoutParams buttonParams(int h,int bottom) { LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(-1,dp(h)); p.bottomMargin=dp(bottom); return p; }
    private boolean isInstalled() {
        try { getPackageManager().getPackageInfo(CLIENT,0); return true; }
        catch (PackageManager.NameNotFoundException e) { return false; }
    }
    private void refreshStatus() {
        status.setText(isInstalled() ? "● Клиент установлен и готов к запуску" : "○ MegaCraft не найден. Сначала установи MegaCraft 0.3.0.");
        status.setTextColor(isInstalled() ? 0xFF9BE56A : 0xFFFFC46B);
    }
    private void launchClient() {
        getPreferences(MODE_PRIVATE).edit().putString("nickname",nickname.getText().toString().trim()).apply();
        Intent intent = getPackageManager().getLaunchIntentForPackage(CLIENT);
        if (intent != null) { startActivity(intent); }
        else new android.app.AlertDialog.Builder(this).setTitle("Клиент не установлен")
                .setMessage("Установи MegaCraft 0.3.0 отдельно, затем вернись в лаунчер и нажми «Играть».")
                .setPositiveButton("Понятно",null).show();
        refreshStatus();
    }
    private void copyServer() {
        String address = "latecraft.ru";
        ClipboardManager cm=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText("MegaCraft server",address));
        Toast.makeText(this,"Адрес latecraft.ru скопирован",Toast.LENGTH_SHORT).show();
    }
    private void showSettings() {
        new android.app.AlertDialog.Builder(this).setTitle("Настройки лаунчера")
                .setMessage("Версия клиента: 0.3.0\nПакет: " + CLIENT + "\n\nЛаунчер сохраняет только имя игрока на этом устройстве. Изменение игровых настроек выполняется внутри MegaCraft.")
                .setPositiveButton("ОК",null).show();
    }
}
