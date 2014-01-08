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
				Log.d(TAG,"i is: "+i+" and j is: "+j);
				drawHexagon(i,j, 100, canvas);
			}
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
	public void drawHexagon(float i, float j, float radius,Canvas canvas) {
        
		//super(pX, pY, radius, radius);
//      setColor(1, 0, 0);
        //setAlpha(0);
        float x1, x2, y1, y2;
        //float lineWidth = 3;
        this.p.setColor(Color.WHITE);
        float width=(float)Math.sqrt(3)*radius;
        float dX=width/2;
        float dY=radius/2;
		/*Path path = new Path();
        path.moveTo(2*i*dX,dY+radius*j);
        path.lineTo(dX+(2*i*dX),dY*j);
        path.lineTo( 2*(i+1)*dX, dY+radius*j);
        path.lineTo(2*(i+1)*dX, dY+((j+1)*radius));
        path.lineTo(dX+(2*i*dX), radius*(j+2));
        path.lineTo( 2*i*dX,dY+((j+1)*radius));
        path.close();
        canvas.drawPath(path, p);*/
        this.p.setColor(Color.CYAN);
        if(j==0)
        	x1 = i*2*dX;
        else
        	x1 = (i+j)*2*dX-(2.6f*j*radius);
        y1 = (1+(3*j))/2*radius;
        x2 = dX+x1;
        y2 = 1.5f*radius*j;
        
        //y2 = (radius * ((2 - (float)Math.sqrt(3)) / 4));
        canvas.drawLine(x1, y1, x2, y2,p);
        //line.setLineWidth(lineWidth);
        //attachChild();
        
        //second line RED
        x1 = x2; 
        y1 = y2;
        x2 = dX+x1;
        y2 = (1+(3*j))/2*radius;
        p.setColor(Color.RED);
        Log.d(TAG,"x1: "+x1+" y1: "+y1+" x2: "+x2+" y2:"+y2);
        canvas.drawLine(x1, y1, x2, y2,p);
        //line.setLineWidth(lineWidth);
        //attachChild(line);

        //third line BLUE
        x1 = x2; 
        y1 = y2;
        x2 = x1;
        y2 = y1+radius;
        p.setColor(Color.BLUE);
        canvas.drawLine(x1, y1, x2, y2,p);
        //line.setLineWidth(lineWidth);
        //attachChild(line);

        //forth line black
        x1 = x2;
        y1 = y2;
        x2 = x1-dX;
        y2 = y1+dY;
        p.setColor(Color.BLACK);
        canvas.drawLine(x1, y1, x2, y2,p);
        //line.setLineWidth(lineWidth);
        //attachChild(line);

        //fifth line yellow
        x1 = x2;
        y1 = y2;
        x2 = x1-dX;
        y2= y1-dY;
        p.setColor(Color.YELLOW);
        canvas.drawLine(x1, y1, x2, y2,p);
//        line.setLineWidth(lineWidth);
//        attachChild(line);

        //sixth line green 
        x1 = x2;
        y1 = y2;
        x2 = x1;
        y2 = y1-radius;
        p.setColor(Color.GREEN);
        canvas.drawLine(x1, y1, x2, y2,p);
//        line.setLineWidth(lineWidth);
//        attachChild(line);

//        touchableArea = new Rectangle(radius / 4, radius / 4, radius * .75f, radius * .75f);
//        touchableArea.setAlpha(0);
//        attachChild(touchableArea);
    }
        /*
        float h = (float) (Math.sin(DegreesToRadians(30)) * radius);
        float r = (float) (Math.sin(DegreesToRadians(30)) * radius);
        float[] points = new float[24];
        float x=dX+(2*i*radius);
        float y=1.5f*radius*j;
        points=setPoints(x,y,radius,h,r);
        canvas.drawLines(points, p);
        //points[0] = new Point(x, y);
        //points[1] = new Point(x + r, y + h);
        //points[2] = new Point(x + r, y + radius + h);
        //points[3] = new Point(x, y + radius + h + h);
        //points[4] = new Point(x - r, y + radius + h);
        //points[5] = new Point(x - r, y + h);
	}
	private float[]setPoints(float x,float y,float radius,float h,float r){
		float[]ans=new float[24];
		ans[0]=x;
		ans[1]=y;
		ans[2]=x+r;
		ans[3]=y + h;
		
		ans[4]=x+r;
		ans[5]=y + h;
		ans[6]=x+r;
		ans[7]=y + radius + h;
		
		ans[8]=x+r;
		ans[9]=y + radius + h;
		ans[10]=x;
		ans[11]=y + radius + (2*h);
		
		ans[12]=x;
		ans[13]=y + radius + (2*h);
		ans[14]=x - r;
		ans[15]=y + radius + h;

		ans[16]=x - r;
		ans[17]=y + radius + h;
		ans[18]=x - r;
		ans[19]=y + h;
		
		ans[20]=x - r;
		ans[21]=y + h;
		ans[22]=x;
		ans[23]=y ;
		return ans;
	}
	public static double DegreesToRadians(double degrees)
	{
	    return degrees * Math.PI / 180;
	}*/
}
