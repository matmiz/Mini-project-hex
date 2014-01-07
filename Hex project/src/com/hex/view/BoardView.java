package com.hex.view;
/** Board view class**/
import com.hex.HexBoard;
import com.hex.HexGame;
import com.hex.R;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;

public class BoardView extends View{
	private HexBoard board;
	private ShapeDrawable[][] matrixDraw;
	private ShapeDrawable[][] cellDraw;
	private Button[][] buttons;
	private HexGame game;
	private Paint p;
	private Paint background ;
	private final static String TAG="boardView";
	//**example variables, delete later**//
	private float width; // width of one tile
	private float height; // height of one tile
	private int selX; // X index of selection
	private int selY; // Y index of selection
	private final Rect selRect = new Rect();
	//**example variables, delete later**//
	
	public BoardView(Context context, HexBoard board){
		super(context);
		Log.d(TAG,"building new boardView in constructor");
		this.p=new Paint();
		this.background=new Paint();
		this.board=board;
		this.game=(HexGame) context;
		setButtons(this.game);
		setFocusable(true);
		setFocusableInTouchMode(true);
	}
	
    public void setButtons(HexGame game) {
        this.game = game;
        buttons = new Button[HexBoard.BOARD_SIZE][HexBoard.BOARD_SIZE];
        for(int i = 0; i < HexBoard.BOARD_SIZE; i++) 
            for(int j = 0; j < HexBoard.BOARD_SIZE; j++) 
                buttons[i][j] = new Button(game);    
        }
    
	@Override
	public void onDraw(Canvas canvas){
		Log.d(TAG,"is drawing a new board");
		background.setColor(getResources().getColor(R.color.gray));
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);	
		for (float i=0;i<HexBoard.BOARD_SIZE;i++){
			for (float j=0;j<HexBoard.BOARD_SIZE;j++){
				drawHexagon(i,j, 100, canvas);
			}
		}
		
		/*TEST
		Log.d(TAG,"is drawing a new board");
		this.p=new Paint();
		this.p.setColor(Color.RED);
		canvas.drawLine(10, 10, 100, 100, p);*/
					
		
		/*
		// Draw the board...
		// Define colors for the grid lines
		Paint dark = new Paint();
		dark.setColor(getResources().getColor(R.color.black));
		Paint hilite = new Paint();
		hilite.setColor(getResources().getColor(R.color.red));
		Paint light = new Paint();
		light.setColor(getResources().getColor(R.color.blue));
		// Draw the minor grid lines
		for (int i = 0; i < 9; i++) {
			canvas.drawLine(0, i*height, getWidth(), i*height,light);
			canvas.drawLine(0, i*height + 1, getWidth(), i*height+ 1, hilite);
			canvas.drawLine(i*width, 0, i*width, getHeight(),light);
			canvas.drawLine(i*width + 1, 0, i*width + 1,getHeight(), hilite);
		}
		// Draw the major grid lines
		for (int i = 0; i < 9; i++) {
			if (i % 3 != 0)
			continue;
			canvas.drawLine(0, i*height, getWidth(), i*height,dark);
			canvas.drawLine(0, i*height + 1, getWidth(), i*height+ 1, hilite);
			canvas.drawLine(i*width, 0, i*width, getHeight(), dark);
			canvas.drawLine(i*width + 1, 0, i*width + 1,getHeight(), hilite);
		}*/
	}
	
	/**We use onSizeChanged( ) to calculate the size of each tile on the screen**/
	@Override 
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		//TODO change to be compatible with hexagons
		width = w / 9f;
		height = h / 9f;
		getRect(selX, selY, selRect);
		super.onSizeChanged(w, h, oldw, oldh);
		}
	private void getRect(int x, int y, Rect rect) {
		rect.set((int) (x
		*
		width), (int) (y
		*
		height), (int) (x
		*
		width + width), (int) (y
		*
		height + height));
		}
	
	
	
	/**inner class for representing a hexagon**/
	public void drawHexagon(float x, float y, float radius,Canvas canvas) {
        //super(pX, pY, radius, radius);
//      setColor(1, 0, 0);
        //setAlpha(0);
        float x1, x2, y1, y2;
        //float lineWidth = 3;
        this.p.setColor(Color.WHITE);
        float width=(float)Math.sqrt(3)*radius;
        x1 = 0; 
        y1 = radius / 2;
        x2 = width / 2;
        y2 = 0;
        //y2 = (radius * ((2 - (float)Math.sqrt(3)) / 4));
        canvas.drawLine(x1, y1, x2, y2,p);
        //line.setLineWidth(lineWidth);
        //attachChild();
        
        //second line
        x1 = x2; 
        y1 = y2;
        x2 = width;
        y2 = radius/2;
        p.setColor(Color.RED);
        canvas.drawLine(x1, y1, x2, y2,p);
        //line.setLineWidth(lineWidth);
        //attachChild(line);

        //third line
        x1 = x2; 
        y1 = y2;
        x2 = width;
        y2 = radius * 1.5f;
        canvas.drawLine(x1, y1, x2, y2,p);
        //line.setLineWidth(lineWidth);
        //attachChild(line);

        //forth line
        x1 = x2;
        y1 = y2;
        x2 = width/2;
        y2 = radius * 2;
        canvas.drawLine(x1, y1, x2, y2,p);
        //line.setLineWidth(lineWidth);
        //attachChild(line);

        //fifth line
        x1 = x2;
        y1 = y2;
        x2 = 0;
        y2= radius * 1.5f;
        canvas.drawLine(x1, y1, x2, y2,p);
//        line.setLineWidth(lineWidth);
//        attachChild(line);

        //sixth line
        x1 = x2;
        y1 = y2;
        x2 = 0;
        y2 = radius/2;
        canvas.drawLine(x1, y1, x2, y2,p);
//        line.setLineWidth(lineWidth);
//        attachChild(line);

//        touchableArea = new Rectangle(radius / 4, radius / 4, radius * .75f, radius * .75f);
//        touchableArea.setAlpha(0);
//        attachChild(touchableArea);
    }
}
