package com.hex;

import com.hex.view.BoardView;
import com.hex.view.HexView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends Activity  implements OnClickListener{
	private HexBoard board;
	private HexView hView;
	private final static String TAG="main";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		View startButton = findViewById(R.id.start_button);
		startButton.setOnClickListener(this);
		View exitButton = findViewById(R.id.exit_button);
		exitButton.setOnClickListener(this);
	}
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_button:
			startGame();
			break;
		case R.id.exit_button:
			finish();
			break;
		}
	}
	
	private void startGame(){
		Log.d(TAG, "clicked on start");
		Intent intent = new Intent(this,HexGame.class);
		Log.d(TAG, "starting intent");
		startActivity(intent);
		}

}
