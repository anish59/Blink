package blackBracket.blink.practice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import blackBracket.blink.R;

public class RND extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rnd);
        DemoConstructorClass aClass = new DemoConstructorClass("Kausha", " Patel");
//        aClass.sum(4, 5);
    }
}
