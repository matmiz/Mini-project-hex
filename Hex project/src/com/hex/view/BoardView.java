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
import android.graphics.Path;
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
            for(int j = 0; j < 2; j++) 
                buttons[i][j] = new Button(game);    
        }
    
	@Override
	public void onDraw(Canvas canvas){
		Log.d(TAG,"is drawing a new board");
		background.setColor(getResources().getColor(R.color.gray));
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);	
		for (float i=0;i<HexBoard.BOARD_SIZE;i++){
			for (float j=0;j<HexBoard.BOARD_SIZE && j<=i;j++){
				drawHexagon(i,j, 30, canvas);
			}
		}
//		drawHexagon(11,1, 10, canvas);
//		drawHexagon(11,2, 10, canvas);
//		drawHexagon(11,3, 10, canvas);
//		drawHexagon(12,2, 10, canvas);
//		drawHexagon(12,3, 10, canvas);
//		drawHexagon(12,4, 10, canvas);
		for (float j=HexBoard.BOARD_SIZE;j<2*HexBoard.BOARD_SIZE;j++){
				for(float i=j-10 ;i <= HexBoard.BOARD_SIZE ;i++)
					drawHexagon(j,i, 30, canvas);
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
	
	public void drawHexagon(float i, float j, float radius,Canvas canvas) {
        float x1, x2, y1, y2;
        this.p.setColor(Color.WHITE);
        float width=(float)Math.sqrt(3)*radius;
        float dX=width/2;
        float dY=radius/2;
		Path path = new Path();
		if(j==0)
        	x1 = i*2*dX;
        else
        	x1 = (i+j)*2*dX-(2.6f*j*radius);
        y1 = (1+(3*j))/2*radius;
        x2 = dX+x1;
        y2 = 1.5f*radius*j;
        //fill the shapes
        path.moveTo(x1,y1);  
        path.lineTo(x2,y2);//first and last point
        path.lineTo( x2+dX, (1+(3*j))/2*radius);//2  
        path.lineTo(x2+dX, ((1+(3*j))/2*radius)+radius);//3
        path.lineTo(x2, (((1+(3*j))/2*radius)+radius)+dY);//4
        path.lineTo( x2-dX,((1+(3*j))/2*radius)+radius);//5
        path.close();
        canvas.drawPath(path, p);
        //draw the lines
        this.p.setColor(Color.BLACK);
        canvas.drawLine(x1, y1, x2, y2,p);
        //attachChild();
        x1 = x2; 
        y1 = y2;
        x2 = dX+x1;
        y2 = (1+(3*j))/2*radius;
        canvas.drawLine(x1, y1, x2, y2,p);
        //attachChild(line);
        x1 = x2; 
        y1 = y2;
        x2 = x1;
        y2 = y1+radius;
        canvas.drawLine(x1, y1, x2, y2,p);
        //attachChild(line);
        x1 = x2;
        y1 = y2;
        x2 = x1-dX;
        y2 = y1+dY;
        canvas.drawLine(x1, y1, x2, y2,p);
        //attachChild(line);
        x1 = x2;
        y1 = y2;
        x2 = x1-dX;
        y2= y1-dY;
        canvas.drawLine(x1, y1, x2, y2,p);
//      attachChild(line);
        x1 = x2;
        y1 = y2;
        x2 = x1;
        y2 = y1-radius;
        canvas.drawLine(x1, y1, x2, y2,p);
//      attachChild(line);
    }
}
