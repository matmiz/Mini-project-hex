package com.hex.view;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

public class HexView extends View{
	private BoardView bv;
	public HexView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public void setView(BoardView bView){
		this.bv=bView;
	}
	@Override
	public void onDraw(Canvas canvas){
		this.bv.onDraw(canvas);
	}

}
