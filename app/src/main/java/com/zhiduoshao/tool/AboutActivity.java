package com.zhiduoshao.tool;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AboutActivity extends AppCompatActivity {

    private static final String AVATAR_URL = "http://q1.qlogo.cn/g?b=qq&nk=2227235998&s=100";
    private static final String QQ_CARD_URI = "mqqapi://card/show_pslcard?src_type=internal&source=sharecard&version=1&uin=2227235998";
    private static final String GITHUB_URL = "https://github.com/NikoYOYO/sky-decrypt-tool";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        MaterialButton backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(v -> finish());

        ImageView avatarImageView = findViewById(R.id.avatarImageView);
        loadAvatar(avatarImageView);

        MaterialCardView developerCard = findViewById(R.id.developerCard);
        developerCard.setOnClickListener(v -> {
            animateCardClick(v);
            v.postDelayed(this::openQQCard, 150);
        });

        MaterialCardView licenseCard = findViewById(R.id.licenseCard);
        licenseCard.setOnClickListener(v -> {
            animateCardClick(v);
            v.postDelayed(this::openGitHub, 150);
        });
        
        applyCardAnimations();
    }
    
    private void applyCardAnimations() {
        View logoCard = findViewById(R.id.logoCard);
        View aboutCard = findViewById(R.id.aboutCard);
        View disclaimerCard = findViewById(R.id.disclaimerCard);
        View licenseCard = findViewById(R.id.licenseCard);
        View developerCard = findViewById(R.id.developerCard);
        
        animateCardFadeInUpWithBounce(logoCard, 0);
        animateCardFadeInUpWithOvershoot(aboutCard, 150);
        animateCardFadeInWithRotation(disclaimerCard, 300);
        animateCardFadeInUpWithOvershoot(licenseCard, 450);
        animateCardFadeInUpWithOvershoot(developerCard, 600);
        
        addHoverEffect(developerCard);
        addHoverEffect(licenseCard);
    }
    
    private void animateCardFadeInUp(View view, long delay) {
        if (view == null) return;
        
        view.setAlpha(0f);
        view.setTranslationY(50f);
        
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        fadeIn.setDuration(600);
        fadeIn.setInterpolator(new AccelerateDecelerateInterpolator());
        fadeIn.setStartDelay(delay);
        
        ObjectAnimator slideUp = ObjectAnimator.ofFloat(view, "translationY", 50f, 0f);
        slideUp.setDuration(600);
        slideUp.setInterpolator(new AccelerateDecelerateInterpolator());
        slideUp.setStartDelay(delay);
        
        fadeIn.start();
        slideUp.start();
    }
    
    private void animateCardFadeInUpWithBounce(View view, long delay) {
        if (view == null) return;
        
        view.setAlpha(0f);
        view.setTranslationY(80f);
        
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        fadeIn.setDuration(800);
        fadeIn.setStartDelay(delay);
        
        ObjectAnimator slideUp = ObjectAnimator.ofFloat(view, "translationY", 80f, 0f);
        slideUp.setDuration(800);
        slideUp.setInterpolator(new BounceInterpolator());
        slideUp.setStartDelay(delay);
        
        fadeIn.start();
        slideUp.start();
    }
    
    private void animateCardFadeInUpWithOvershoot(View view, long delay) {
        if (view == null) return;
        
        view.setAlpha(0f);
        view.setTranslationY(60f);
        view.setScaleX(0.8f);
        view.setScaleY(0.8f);
        
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        fadeIn.setDuration(600);
        fadeIn.setStartDelay(delay);
        
        ObjectAnimator slideUp = ObjectAnimator.ofFloat(view, "translationY", 60f, 0f);
        slideUp.setDuration(600);
        slideUp.setInterpolator(new OvershootInterpolator(1.5f));
        slideUp.setStartDelay(delay);
        
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1f);
        scaleX.setDuration(600);
        scaleX.setInterpolator(new OvershootInterpolator(1.5f));
        scaleX.setStartDelay(delay);
        
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1f);
        scaleY.setDuration(600);
        scaleY.setInterpolator(new OvershootInterpolator(1.5f));
        scaleY.setStartDelay(delay);
        
        fadeIn.start();
        slideUp.start();
        scaleX.start();
        scaleY.start();
    }
    
    private void animateCardFadeInWithRotation(View view, long delay) {
        if (view == null) return;
        
        view.setAlpha(0f);
        view.setRotationY(-90f);
        
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        fadeIn.setDuration(700);
        fadeIn.setStartDelay(delay);
        
        ObjectAnimator rotateY = ObjectAnimator.ofFloat(view, "rotationY", -90f, 0f);
        rotateY.setDuration(700);
        rotateY.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateY.setStartDelay(delay);
        
        fadeIn.start();
        rotateY.start();
    }
    
    private void addHoverEffect(View view) {
        if (view == null) return;
        
        view.setOnLongClickListener(v -> {
            animatePulse(v);
            return true;
        });
    }
    
    private void animatePulse(View view) {
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 1.05f, 1f);
        scaleUpX.setDuration(400);
        scaleUpX.setRepeatCount(1);
        
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 1.05f, 1f);
        scaleUpY.setDuration(400);
        scaleUpY.setRepeatCount(1);
        
        scaleUpX.start();
        scaleUpY.start();
    }
    
    private void animateArrowPulse(View arrowView) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(arrowView, "scaleX", 1f, 1.2f, 1f);
        scaleX.setDuration(1000);
        scaleX.setRepeatCount(ValueAnimator.INFINITE);
        scaleX.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleX.setStartDelay(2000);
        
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(arrowView, "scaleY", 1f, 1.2f, 1f);
        scaleY.setDuration(1000);
        scaleY.setRepeatCount(ValueAnimator.INFINITE);
        scaleY.setInterpolator(new AccelerateDecelerateInterpolator());
        scaleY.setStartDelay(2000);
        
        ObjectAnimator alpha = ObjectAnimator.ofFloat(arrowView, "alpha", 1f, 0.5f, 1f);
        alpha.setDuration(1000);
        alpha.setRepeatCount(ValueAnimator.INFINITE);
        alpha.setStartDelay(2000);
        
        scaleX.start();
        scaleY.start();
        alpha.start();
    }
    
    private void animateCardClick(View view) {
        ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.95f);
        scaleDownX.setDuration(100);
        
        ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.95f);
        scaleDownY.setDuration(100);
        
        ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 0.95f, 1f);
        scaleUpX.setDuration(100);
        scaleUpX.setStartDelay(100);
        
        ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 0.95f, 1f);
        scaleUpY.setDuration(100);
        scaleUpY.setStartDelay(100);
        
        scaleDownX.start();
        scaleDownY.start();
        scaleUpX.start();
        scaleUpY.start();
    }

    private void loadAvatar(ImageView imageView) {
        new Thread(() -> {
            try {
                URL url = new URL(AVATAR_URL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);
                
                runOnUiThread(() -> imageView.setImageBitmap(bitmap));
            } catch (Exception ignored) {
                runOnUiThread(() -> {
                    imageView.setImageResource(android.R.drawable.ic_menu_gallery);
                });
            }
        }).start();
    }

    private void openQQCard() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(QQ_CARD_URI));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "无法打开QQ名片，请确保已安装QQ", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void openGitHub() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(GITHUB_URL));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "无法打开GitHub项目", Toast.LENGTH_SHORT).show();
        }
    }
}
