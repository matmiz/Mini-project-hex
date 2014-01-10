package com.hex.view;
/** game piece view class**/

import com.hex.GamePiece;

import android.content.Context;
import android.view.View;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


public class GamePieceView extends View{
	private GamePiece peon;
	
	public GamePieceView(Context context) {
		super(context);
	}
	private Paint getPeonColor(){
		Paint paint= new Paint();
		paint.setStrokeWidth(2);
		if(peon.getID()==0)
			paint.setColor(Color.WHITE);
			
		else
			paint.setColor(Color.BLACK);
		return paint;
	}
	//drawing the newly created peon, and placing it on the board
	@Override 
	public void onDraw(Canvas canvas){
		canvas.drawCircle(peon.getX(),peon.getY(),20,getPeonColor());
	}

}
