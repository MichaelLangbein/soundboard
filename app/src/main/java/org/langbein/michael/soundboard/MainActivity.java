package org.langbein.michael.soundboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.langbein.michael.soundboard.scenes.BoardScene;
import org.langbein.michael.soundboard.scenes.SceneLogic;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SceneLogic boardScene = new BoardScene();
        MainView mainView = new MainView(this, boardScene);
        setContentView(mainView);
        mainView.startRenderThread();
    }
}
