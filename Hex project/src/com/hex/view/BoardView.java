package com.hex.view;
/** Board view class**/
import com.hex.HexBoard;
import com.hex.HexGame;
import com.hex.R;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
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
	private float[] values;
	
	public BoardView(Context context, HexBoard board){
		super(context);
		Log.d(TAG,"building new boardView in constructor");
		this.p=new Paint();
		this.background=new Paint();
		this.board=board;
		this.game=(HexGame) context;
		this.values=HexValues();
		background.setColor(Color.GRAY);
		setButtons(this.game);
		setFocusable(true);
		setFocusableInTouchMode(true);
	}
	
	private float[] HexValues()
	{
		//[  0    |   1    |    2      |     3     |   4   |     5      ]
		//[Radius | Height | RowHeight | HalfWidth | WIdth | ExtraHeight]
		float [] values=new float[6];
		values[0] = HexBoard.RADIUS;
	    values[1] =2* values[0];
	    values[2] =1.5f* values[0];
	    values[3] = (float)Math.sqrt((values[0] * values[0]) - ((values[0] /2) * (values[0] /2)));
	    values[4] =2*values[3];
	    values[5] = values[1] - values[2];
	    return values;
	}
	
    public void setButtons(HexGame game) {
        this.game = game;
        buttons = new Button[HexBoard.BOARD_SIZE][HexBoard.BOARD_SIZE];
        for(int i = 0; i < HexBoard.BOARD_SIZE; i++) 
            for(int j = 0; j < HexBoard.BOARD_SIZE; j++){ 
                buttons[i][j] = new Button(game);  
                buttons[i][j].setPressed(false);
            }
        		
        }
    
	@Override
	public void onDraw(Canvas canvas){
		canvas.drawRect(0, 0, getWidth(), getHeight(), this.background);	
		for (float i=0;i<HexBoard.BOARD_SIZE;i++){
			for (float j=0;j<HexBoard.BOARD_SIZE && j<=i;j++){
				drawHexagon(i,j, HexBoard.RADIUS, canvas);
			}
		}
		for (float j=HexBoard.BOARD_SIZE;j<2*HexBoard.BOARD_SIZE;j++){
				for(float i=j-10 ;i <= HexBoard.BOARD_SIZE-1 ;i++)
					drawHexagon(j,i, HexBoard.RADIUS, canvas);
				}
	  //paintHexagon(6, 5, 30, canvas, Color.BLUE, false);
	}
	
	/**We use onSizeChanged( ) to calculate the size of each tile on the screen**/
	@Override 
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		//TODO change to be compatible with hexagons
		}
	
	public void drawHexagon(float i, float j, float radius,Canvas canvas) {
        float x1, x2, y1, y2;
        //this.p.setColor(Color.WHITE);
        float width=(float)Math.sqrt(3)*radius;
        float dX=width/2;
        float dY=radius/2;
		if(j==0)
        	x1 = i*2*dX;
        else
        	x1 = (i+j)*2*dX-(2.6f*j*radius);
        y1 = (1+(3*j))/2*radius;
        x2 = dX+x1;
        y2 = 1.5f*radius*j;
        //fill the shapes
        if(buttons[(int)i%HexBoard.BOARD_SIZE][(int)j%HexBoard.BOARD_SIZE].isPressed()){
        	Log.d("PressedButton","painting on i="+i%HexBoard.BOARD_SIZE+" and j="+j%HexBoard.BOARD_SIZE);
        	paintHexagon(i,j,HexBoard.RADIUS,canvas,Color.RED,false);
        	}
        else
        	paintHexagon(i,j,HexBoard.RADIUS,canvas,Color.WHITE,true);
        //draw the lines
        this.p.setColor(Color.BLACK);
        canvas.drawLine(x1, y1, x2, y2,p);
        x1 = x2; 
        y1 = y2;
        x2 = dX+x1;
        y2 = (1+(3*j))/2*radius;
        canvas.drawLine(x1, y1, x2, y2,p);
        x1 = x2; 
        y1 = y2;
        x2 = x1;
        y2 = y1+radius;
        canvas.drawLine(x1, y1, x2, y2,p);
        x1 = x2;
        y1 = y2;
        x2 = x1-dX;
        y2 = y1+dY;
        canvas.drawLine(x1, y1, x2, y2,p);
        x1 = x2;
        y1 = y2;
        x2 = x1-dX;
        y2= y1-dY;
        canvas.drawLine(x1, y1, x2, y2,p);
        x1 = x2;
        y1 = y2;
        x2 = x1;
        y2 = y1-radius;
        canvas.drawLine(x1, y1, x2, y2,p);
        
    }
	
	//[  0    |   1    |    2      |     3     |   4   |     5      ]
	//[Radius | Height | RowHeight | HalfWidth | WIdth | ExtraHeight]
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    float px = event.getX() + this.values[3];
	    float py = event.getY() + this.values[1] /2;
		int gridX = (int)px / (int)this.values[4];
		int gridY = (int)py / (int)this.values[2];
		int resultY=gridY;
		int resultX=gridX; 
		int gridModX = (int)px % (int)this.values[4];
		int gridModY = (int)py % (int)this.values[2];
		boolean gridType=false;
		float scale = this.values[5] / this.values[3]; 
		if (((int)gridY &1) ==0)
			gridType =true; 
		if(gridType){
			{
				// middle hexagon
				resultY = gridY;
				resultX = gridX;
				// left hexagon
				if (gridModY < (this.values[5] - gridModX * scale))
				{
					resultY = gridY -1;
					resultX = gridX -1;
				}
				// right hexagon
				if (gridModY < (-this.values[5] + gridModX * scale))
				{
					resultY = gridY -1;
					resultX = gridX;
				}
			}
		}
		else
	    {
	        if (gridModX >= this.values[3])
	        {
	            if (gridModY < (2* this.values[5] - gridModX * scale))
	            {
	                // Top hexagon
	                resultY = gridY -1;
	                resultX = gridX;
	            }
	            else
	            {
	                // Right hexagon
	                resultY = gridY;
	                resultX = gridX;
	            }
	        }
	        if (gridModX < this.values[3])
	        {
	            if (gridModY < (gridModX * scale))
	            {
	                // Top hexagon
	                resultY = gridY -1;
	                resultX = gridX;
	            }
	            else
	            {
	                // Left hexagon
	                resultY = gridY;
	                resultX = gridX -1;
	            }
	        }
	    } 
		Log.d("onTOuchEvent","on touch event x: "+resultX);
		Log.d("onTOuchEvent","on touch event y: "+resultY);
		this.buttons[(int)(resultX % HexBoard.BOARD_SIZE)-1][(int)(resultY % HexBoard.BOARD_SIZE)-1].setPressed(true);
		postInvalidate();
		return true;
	}
	
	public void paintHexagon(float i, float j, float radius,Canvas canvas,int c,Boolean isCreating) {
        float x1, x2, y1, y2;
        float width=(float)Math.sqrt(3)*radius;
        float dX=width/2;
        float dY=radius/2;
		Path path = new Path();
		this.p.setColor(c);
		Log.d("paintHexagon","painting on "+i+","+j+" with color "+c);
        if(!isCreating){
    		j=j+i;
        	float tmp=i;
        	i=j;
        	j=tmp;
        }
		if(j==0)
        	x1 = i*2*dX;
        else
        	x1 = (i+j)*2*dX-(2.6f*j*radius);
        y1 = (1+(3*j))/2*radius;
        x2 = dX+x1;
        y2 = 1.5f*radius*j;
        path.moveTo(x1,y1);  
        path.lineTo(x2,y2);
        path.lineTo( x2+dX, (1+(3*j))/2*radius);
        path.lineTo(x2+dX, ((1+(3*j))/2*radius)+radius);
        path.lineTo(x2, (((1+(3*j))/2*radius)+radius)+dY);
        path.lineTo( x2-dX,((1+(3*j))/2*radius)+radius);
        path.close();
        canvas.drawPath(path, this.p);
	}



}
