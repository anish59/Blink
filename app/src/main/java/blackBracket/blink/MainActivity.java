package blackBracket.blink;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import org.jetbrains.annotations.NotNull;

import io.ghyeok.stickyswitch.widget.StickySwitch;

public class MainActivity extends AppCompatActivity {

    private StickySwitch stickyswitch;
    private RelativeLayout layoutRelative;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        init();
        initListener();
    }

    private void initListener() {
        stickyswitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(@NotNull StickySwitch.Direction direction, @NotNull String text) {
                int colorFrom = context.getResources().getColor(R.color.white);
                int colorTo = getResources().getColor(R.color.green);
                if (stickyswitch.getDirection() == StickySwitch.Direction.RIGHT) {
                    changeBgColor(colorFrom, colorTo);
                } else {
                    changeBgColor(colorTo, colorFrom);
                }
            }
        });
    }

    private void init() {
        layoutRelative = (RelativeLayout) findViewById(R.id.layoutRelative);
        stickyswitch = (StickySwitch) findViewById(R.id.sticky_switch);
    }


    private void changeBgColor(int colorFrom, int colorTo) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(250); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                layoutRelative.setBackgroundColor((int) animator.getAnimatedValue());
            }

        });
        colorAnimation.start();
    }
}
