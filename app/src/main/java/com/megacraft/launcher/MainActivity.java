package com.megacraft.launcher;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.*;

public class MainActivity extends Activity {
    private static final String CLIENT = "com.lituty.megacraftpe";
    private static final String SERVER = "latecraft.ru";
    private int green = 0xFF43C92F;
    private boolean landscape;

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(0xFF111512);
        getWindow().setNavigationBarColor(0xFF111512);
        landscape = getResources().getDisplayMetrics().widthPixels > getResources().getDisplayMetrics().heightPixels;
        render();
    }

    private int dp(float v) { return (int)(v * getResources().getDisplayMetrics().density + .5f); }
    private GradientDrawable box(int color, int radius, int stroke) {
        GradientDrawable d = new GradientDrawable();
        d.setColor(color); d.setCornerRadius(dp(radius));
        if (stroke != 0) d.setStroke(dp(2), stroke);
        return d;
    }
    private TextView label(String s, int size, int color, boolean bold) {
        TextView t = new TextView(this); t.setText(s); t.setTextSize(size); t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        return t;
    }
    private LinearLayout row() {
        LinearLayout l = new LinearLayout(this); l.setOrientation(LinearLayout.HORIZONTAL);
        l.setGravity(Gravity.CENTER_VERTICAL); return l;
    }
    private LinearLayout col() {
        LinearLayout l = new LinearLayout(this); l.setOrientation(LinearLayout.VERTICAL); return l;
    }
    private LinearLayout.LayoutParams lp(int w, int h) {
        return new LinearLayout.LayoutParams(w < 0 ? w : dp(w), h < 0 ? h : dp(h));
    }
    private TextView tile(String title, String sub, int color, String icon) {
        LinearLayout item = col();
        item.setPadding(dp(10), dp(8), dp(8), dp(8));
        item.setBackground(box(color, 9, 0xFF18251A));
        TextView art = label(icon, landscape ? 24 : 19, Color.WHITE, true);
        art.setGravity(Gravity.CENTER); item.addView(art, lp(-1, landscape ? 38 : 30));
        TextView name = label(title, landscape ? 13 : 11, Color.WHITE, true);
        item.addView(name, lp(-1, -2));
        if (sub != null) { TextView small = label(sub, 8, 0xFFE8F5E5, false); item.addView(small); }
        item.setElevation(dp(4)); item.setOnClickListener(v ->
            Toast.makeText(this, title, Toast.LENGTH_SHORT).show());
        return item;
    }

    private void render() {
        FrameLayout root = new FrameLayout(this);
        root.setBackgroundColor(0xFF247E24);
        root.addView(new SceneBackground(this), new FrameLayout.LayoutParams(-1, -1));

        LinearLayout page = col();
        page.setPadding(dp(8), dp(5), dp(8), dp(8));
        FrameLayout.LayoutParams pageP = new FrameLayout.LayoutParams(-1, -1);
        root.addView(page, pageP);

        LinearLayout top = row();
        top.setPadding(dp(12), dp(4), dp(12), dp(4));
        top.setBackgroundColor(0xEE151A17);
        TextView logo = label("▣  MEGACRAFT", 15, Color.WHITE, true);
        top.addView(logo, new LinearLayout.LayoutParams(0, dp(38), 1));
        TextView online = label("👥  2 В СЕТИ", 10, Color.WHITE, true);
        online.setPadding(dp(10), dp(8), dp(10), dp(8));
        online.setBackground(box(0xFF202820, 8, 0xFF101810));
        top.addView(online);
        TextView profile = label("  ◻  " + getNickname() + "  ▾", 11, Color.WHITE, true);
        profile.setPadding(dp(10), dp(8), dp(10), dp(8));
        profile.setBackground(box(0xFF202820, 8, 0xFF101810));
        LinearLayout.LayoutParams pp = lp(-2, 38); pp.leftMargin = dp(7); top.addView(profile, pp);
        profile.setOnClickListener(v -> editNickname());
        TextView bug = label("⚙", 18, Color.WHITE, true); bug.setGravity(Gravity.CENTER);
        bug.setBackground(box(green, 7, 0xFF1A5A17));
        LinearLayout.LayoutParams bp = lp(40, 38); bp.leftMargin = dp(7); top.addView(bug, bp);
        bug.setOnClickListener(v -> Toast.makeText(this, "Настройки лаунчера", Toast.LENGTH_SHORT).show());
        page.addView(top, lp(-1, 46));

        if (landscape) buildLandscape(page); else buildPortrait(page);
        setContentView(root);
    }

    private void buildLandscape(LinearLayout page) {
        LinearLayout middle = row();
        LinearLayout.LayoutParams mp = new LinearLayout.LayoutParams(-1, 0, 1);
        mp.topMargin = dp(8); page.addView(middle, mp);

        LinearLayout side = col(); side.setGravity(Gravity.CENTER_VERTICAL);
        TextView[] cards = {
            tile("Магазин", "НОВОЕ", 0xFFB51D45, "✦"),
            tile("Тиры PvP", "НОВОЕ", 0xFFC65A24, "⚔"),
            tile("Мой скин", "", 0xFF1766A7, "♟"),
            tile("Играть с друзьями", "", 0xFF286C32, "👥")
        };
        for (TextView c : cards) {
            LinearLayout.LayoutParams cp = lp(112, 78); cp.bottomMargin = dp(9); side.addView(c, cp);
        }
        middle.addView(side, lp(120, -1));

        FrameLayout stage = new FrameLayout(this);
        LinearLayout.LayoutParams stp = new LinearLayout.LayoutParams(0, -1, 1);
        middle.addView(stage, stp);
        Avatar avatar = new Avatar(this);
        FrameLayout.LayoutParams avp = new FrameLayout.LayoutParams(dp(230), -1, Gravity.CENTER);
        stage.addView(avatar, avp);
        TextView discord = label("ВОЙСЫ И ПАТИ\nВ DISCORD\n\n▶  ЗАЙТИ", 15, Color.WHITE, true);
        discord.setPadding(dp(12), dp(12), dp(12), dp(12));
        discord.setBackground(box(0xFF2936A2, 5, 0xFF111A4B));
        FrameLayout.LayoutParams disP = new FrameLayout.LayoutParams(dp(165), dp(118), Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        disP.rightMargin = dp(5); stage.addView(discord, disP);

        LinearLayout bottom = col(); bottom.setPadding(dp(4), dp(4), dp(4), dp(2));
        LinearLayout quick = row(); quick.setGravity(Gravity.CENTER);
        quick.addView(bottomButton("🏆  Топ недели", 0xFF18351B));
        quick.addView(bottomButton("🔎  Найти пати", 0xFF18351B));
        quick.addView(bottomButton("♟  Позвать друга", 0xFF18351B));
        bottom.addView(quick, lp(-1, 42));

        LinearLayout launchPanel = row();
        launchPanel.setPadding(dp(8), dp(7), dp(8), dp(7));
        launchPanel.setBackground(box(0xFF182B20, 7, 0xFF0C2110));
        LinearLayout game = col(); game.setPadding(dp(12), dp(7), dp(8), dp(5));
        game.addView(label("СЕГОДНЯ ИГРАЕМ", 9, 0xFFD5E7D3, true));
        game.addView(label("MegaCraft", 22, Color.WHITE, true));
        game.addView(label("Pocket Edition  •  0.3.0", 9, 0xFFB9D2B5, false));
        launchPanel.addView(game, new LinearLayout.LayoutParams(0, -2, 1));
        LinearLayout actions = col();
        TextView play = label("▶  Играть", 22, 0xFF10210D, true); play.setGravity(Gravity.CENTER);
        play.setBackground(box(0xFF63D842, 6, 0xFF276D1C));
        actions.addView(play, lp(190, 58)); play.setOnClickListener(v -> launchClient());
        TextView chest = label("▣  Открыть сундук", 12, 0xFF382500, true); chest.setGravity(Gravity.CENTER);
        chest.setBackground(box(0xFFF4C34A, 5, 0xFF8C6420));
        LinearLayout.LayoutParams chp = lp(190, 30); chp.topMargin = dp(4); actions.addView(chest, chp);
        launchPanel.addView(actions); bottom.addView(launchPanel, lp(-1, 106));
        page.addView(bottom, lp(-1, 152));
    }

    private void buildPortrait(LinearLayout page) {
        LinearLayout menu = row(); menu.setGravity(Gravity.CENTER);
        menu.addView(tile("Магазин", "", 0xFFB51D45, "✦"), new LinearLayout.LayoutParams(0, dp(65), 1));
        menu.addView(tile("Тиры PvP", "", 0xFFC65A24, "⚔"), new LinearLayout.LayoutParams(0, dp(65), 1));
        menu.addView(tile("Мой скин", "", 0xFF1766A7, "♟"), new LinearLayout.LayoutParams(0, dp(65), 1));
        menu.addView(tile("Друзья", "", 0xFF286C32, "👥"), new LinearLayout.LayoutParams(0, dp(65), 1));
        LinearLayout.LayoutParams menuP = lp(-1, 72); menuP.topMargin = dp(8); page.addView(menu, menuP);

        FrameLayout stage = new FrameLayout(this);
        LinearLayout.LayoutParams stp = new LinearLayout.LayoutParams(-1, 0, 1);
        stp.topMargin = dp(5); page.addView(stage, stp);
        stage.addView(new Avatar(this), new FrameLayout.LayoutParams(dp(180), -1, Gravity.CENTER));
        TextView discord = label("ВОЙСЫ И ПАТИ В DISCORD  •  ЗАЙТИ", 10, Color.WHITE, true);
        discord.setGravity(Gravity.CENTER); discord.setPadding(dp(8), dp(7), dp(8), dp(7));
        discord.setBackground(box(0xFF2936A2, 5, 0xFF111A4B));
        FrameLayout.LayoutParams dp1 = new FrameLayout.LayoutParams(-1, dp(38), Gravity.BOTTOM);
        stage.addView(discord, dp1);

        LinearLayout bottom = col(); bottom.setPadding(dp(5), dp(5), dp(5), dp(5));
        LinearLayout info = row(); info.setPadding(dp(10), dp(7), dp(10), dp(7));
        info.setBackground(box(0xFF214F9A, 6, 0xFF142B55));
        LinearLayout details = col();
        details.addView(label("СЕГОДНЯ ИГРАЕМ", 9, 0xFFE3EDFF, true));
        details.addView(label("MegaCraft  •  0.3.0", 16, Color.WHITE, true));
        info.addView(details, new LinearLayout.LayoutParams(0, -2, 1));
        TextView play = label("▶ ИГРАТЬ", 18, 0xFF10210D, true); play.setGravity(Gravity.CENTER);
        play.setPadding(dp(20), 0, dp(20), 0); play.setBackground(box(0xFF63D842, 6, 0xFF276D1C));
        info.addView(play, lp(-2, 54)); play.setOnClickListener(v -> launchClient());
        bottom.addView(info, lp(-1, 70));
        page.addView(bottom, lp(-1, 80));
    }

    private TextView bottomButton(String text, int color) {
        TextView b = label(text, 10, Color.WHITE, true); b.setGravity(Gravity.CENTER);
        b.setPadding(dp(8), 0, dp(8), 0); b.setBackground(box(color, 5, 0xFF0D1A10));
        LinearLayout.LayoutParams p = lp(-2, 34); p.leftMargin = dp(3); p.rightMargin = dp(3);
        b.setOnClickListener(v -> Toast.makeText(this, text, Toast.LENGTH_SHORT).show()); return b;
    }

    private String getNickname() {
        return getPreferences(MODE_PRIVATE).getString("nickname", "Игрок");
    }
    private void editNickname() {
        EditText input = new EditText(this); input.setSingleLine(true); input.setText(getNickname());
        new android.app.AlertDialog.Builder(this).setTitle("Игровой профиль").setMessage("Твой никнейм")
            .setView(input).setNegativeButton("Отмена", null)
            .setPositiveButton("Сохранить", (d, w) -> {
                String value = input.getText().toString().trim();
                if (!value.isEmpty()) getPreferences(MODE_PRIVATE).edit().putString("nickname", value).apply();
                render();
            }).show();
    }
    private void launchClient() {
        Intent intent = getPackageManager().getLaunchIntentForPackage(CLIENT);
        if (intent != null) {
            try { startActivity(intent); }
            catch (Exception e) { Toast.makeText(this, "Не удалось открыть MegaCraft", Toast.LENGTH_LONG).show(); }
        } else new android.app.AlertDialog.Builder(this).setTitle("MegaCraft не установлен")
            .setMessage("Установи клиент MegaCraft отдельно, затем нажми «Играть» ещё раз.")
            .setPositiveButton("Понятно", null).show();
    }

    private class SceneBackground extends View {
        private Paint p = new Paint(3);
        SceneBackground(Context c) { super(c); }
        @Override protected void onDraw(Canvas c) {
            float w = getWidth(), h = getHeight();
            p.setShader(new RadialGradient(w*.53f, h*.48f, Math.max(w,h)*.75f,
                new int[]{0xFF78E34A,0xFF39A72D,0xFF17651F}, null, Shader.TileMode.CLAMP));
            c.drawRect(0,0,w,h,p); p.setShader(null);
            p.setColor(0x227FEA55);
            for (int i=0;i<26;i++) {
                float x=(i*97)%Math.max(1,(int)w), y=(i*61)%Math.max(1,(int)h);
                c.drawRect(x,y,x+dp(9),y+dp(9),p);
            }
            p.setColor(0x18FFFFFF);
            c.drawCircle(w*.52f,h*.46f,Math.min(w,h)*.38f,p);
        }
    }
    private class Avatar extends View {
        private Paint p = new Paint(3);
        Avatar(Context c) { super(c); }
        private void rect(Canvas c, float x,float y,float w,float h,int color) {
            p.setColor(color); p.setShader(null); c.drawRect(x,y,x+w,y+h,p);
        }
        @Override protected void onDraw(Canvas c) {
            float s=Math.min(getWidth()/180f,getHeight()/330f);
            c.save(); c.translate((getWidth()-180*s)/2f,(getHeight()-330*s)/2f); c.scale(s,s);
            // Stylized original block avatar, not copied from the reference character.
            rect(c,53,104,74,112,0xFFF1D6C7); rect(c,53,104,74,13,0xFF4C4C52);
            rect(c,60,116,60,66,0xFFB7E9D9); rect(c,69,128,42,37,0xFF78CBA7);
            rect(c,82,139,10,7,0xFF285E3B); rect(c,100,139,10,7,0xFF285E3B);
            rect(c,87,158,17,5,0xFF356C48);
            rect(c,56,205,68,66,0xFF4B4149); rect(c,64,205,52,15,0xFF30313A);
            rect(c,64,220,52,42,0xFF7D3B83); rect(c,56,265,30,44,0xFF56316A);
            rect(c,94,265,30,44,0xFF56316A); rect(c,49,303,39,14,0xFF263B27);
            rect(c,93,303,39,14,0xFF263B27);
            rect(c,40,207,17,57,0xFFF0D9CE); rect(c,123,207,17,57,0xFFF0D9CE);
            rect(c,40,258,17,14,0xFF2D6A35); rect(c,123,258,17,14,0xFF2D6A35);
            // pixel cap
            rect(c,39,92,102,12,0xFF626B70); rect(c,51,80,78,12,0xFF818D91);
            rect(c,61,72,58,8,0xFF505A60); rect(c,73,84,15,8,0xFF30383D);
            c.restore();
        }
    }
}
