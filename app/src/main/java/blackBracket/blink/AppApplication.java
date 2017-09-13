package blackBracket.blink;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by anish on 13-09-2017.
 */

public class AppApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        initStetho();
    }

    private void initStetho() {
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());
    }
}
