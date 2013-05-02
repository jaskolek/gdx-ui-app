package com.broken_e.test.ui;

import com.broken_e.test.ui.game.GameRoot;
import com.broken_e.ui.BaseScreen;
import com.broken_e.ui.UiApp;

public class GameScreen extends BaseScreen {

	public GameScreen(UiApp app) {
		super(app);
		mainTable.addActor(new GameRoot());
	}

	@Override
	public void onBackPress() {
		app.switchScreens(new MainScreen(app));
	}
	
}
