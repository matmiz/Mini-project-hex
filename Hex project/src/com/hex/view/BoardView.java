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
		Paint background = new Paint();
		background.setColor(getResources().getColor(R.color.gray));
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);	
		/*
		for (float i=0;i<HexBoard.BOARD_SIZE;i++){
			for (float j=0;j<HexBoard.BOARD_SIZE;j++){
				switch(this.board.getColorAt((int)i, (int)j)){
					case HexBoard.EMPTY:
						p.setColor(Color.WHITE);
						canvas.drawRect(i,i+50,j,j+50,p);
						break;
					case HexBoard.COL_PLAYER:
						p.setColor(Color.RED);
						canvas.drawRect(i,i+50,j,j+50,p);
						break;
					case HexBoard.ROW_PLAYER:
						p.setColor(Color.BLUE);
						canvas.drawRect(i,i+50,j,j+50,p);
						break;
				}
			}
		}
		
		TEST
		Log.d(TAG,"is drawing a new board");
		this.p=new Paint();
		this.p.setColor(Color.RED);
		canvas.drawLine(10, 10, 100, 100, p);*/
					
		

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
		}
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
    private class Hexagon {
        private final int[] polyY, polyX;
        private final int polySides = 6;

        public Hexagon(double x, double y, double r) {
            polyX = getXCoordinates(x, y, r, 6, Math.PI / 2);
            polyY = getYCoordinates(x, y, r, 6, Math.PI / 2);
        }

        public boolean contains(Point p) {
            boolean oddTransitions = false;
            for(int i = 0, j = polySides - 1; i < polySides; j = i++) {
                if((polyY[i] < p.y && polyY[j] >= p.y) || (polyY[j] < p.y && polyY[i] >= p.y)) {
                    if(polyX[i] + (p.y - polyY[i]) / (polyY[j] - polyY[i]) * (polyX[j] - polyX[i]) < p.x) {
                        oddTransitions = !oddTransitions;
                    }
                }
            }
            return oddTransitions;
        }
        protected int[] getXCoordinates(double x, double y, double r, int vertexCount, double startAngle) {
            int res[] = new int[vertexCount];
            double addAngle = 2 * Math.PI / vertexCount;
            double angle = startAngle;
            for(int i = 0; i < vertexCount; i++) {
                res[i] = (int) (Math.round(r * Math.cos(angle)) + x);
                angle += addAngle;
            }
            return res;
        }

        protected int[] getYCoordinates(double x, double y, double r, int vertexCount, double startAngle) {
            int res[] = new int[vertexCount];
            double addAngle = 2 * Math.PI / vertexCount;
            double angle = startAngle;
            for(int i = 0; i < vertexCount; i++) {
                res[i] = (int) (Math.round(r * Math.sin(angle)) + y);
                angle += addAngle;
            }
            return res;
        }
        protected  double radiusCalc(double w, double h, double n) {
            double spaceV = (((n - 1) * 3 / 2) + 2);
            double spaceH = n + (n - 1) / 2; 
            spaceH = (w / (spaceH * Math.sqrt(3)));
            spaceV = (h / spaceV);
            if(spaceV < spaceH) {
                return spaceV;
            }
            return spaceH;
        }
    }
}
