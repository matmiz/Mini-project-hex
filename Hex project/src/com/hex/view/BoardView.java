package com.hex.view;
/** Board view class**/
import com.hex.HexBoard;
import com.hex.HexGame;
import com.hex.MainActivity;
import com.hex.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
	//private HexBoard board;
	private Button[][] buttons;
	private HexGame game;
	private Paint background,redPlayer,bluePlayer,p ;
	private final static String TAG="boardView";
	private float[] hexValues;
	
	public BoardView(Context context, HexBoard board){
		super(context);
		Log.d(TAG,"building new boardView in constructor");
		this.p=new Paint();
		this.background=new Paint();
		this.redPlayer=new Paint();
		this.bluePlayer=new Paint();
		this.game=(HexGame) context;
		this.hexValues=HexValues();
		background.setColor(Color.GRAY);
		redPlayer.setColor(Color.RED);
		bluePlayer.setColor(Color.BLUE); 
		setButtons(this.game);
		setFocusable(true);
		setFocusableInTouchMode(true);
	}
	
	
    
	@Override
	public void onDraw(Canvas canvas){
		canvas.drawRect(0, 0, getWidth(), getHeight(), this.background);
		if(HexGame.PLAYING)
			canvas.drawRect(0, getHeight()/1.5f,getWidth(), getWidth()/2, this.redPlayer);
		else
			canvas.drawRect(getHeight()/1.5f,0, getWidth()/2, getWidth(), this.bluePlayer);
		for (float i=0;i<HexBoard.BOARD_SIZE;i++){
			for (float j=0;j<HexBoard.BOARD_SIZE && j<=i;j++){
				paintHexagon(i,j,HexBoard.RADIUS,canvas,Color.WHITE,true);
			}
		}
		for (float j=HexBoard.BOARD_SIZE;j<2*HexBoard.BOARD_SIZE;j++){
				for(float i=j-(HexBoard.BOARD_SIZE-1) ;i < HexBoard.BOARD_SIZE ;i++){
					paintHexagon(j,i,HexBoard.RADIUS,canvas,Color.WHITE,true);
					}
				}
		checkButtons(canvas);
		drawLines(canvas);
	}
	
	
	/**We use onSizeChanged to calculate the size of each tile on the screen**/
	@Override 
	public void onSizeChanged(int w, int h, int oldw, int oldh) {
		//TODO change to be compatible with hexagons
		}
	
	private void checkButtons(Canvas canvas) { 
		for(int i=0;i<HexBoard.BOARD_SIZE;i++)
			for(int j=0;j<HexBoard.BOARD_SIZE;j++){
				if(buttons[i][j].isPressed() && buttons[i][j].getId()==HexGame.ME)
						paintHexagon(i,j,HexBoard.RADIUS,canvas,Color.RED,false);
					else if(buttons[i][j].getId()==HexGame.OPPONENT)
						paintHexagon(i,j,HexBoard.RADIUS,canvas,Color.BLUE,false);
			}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction()==MotionEvent.ACTION_DOWN){
			double results[]=pixelToHex(event);
			//Log.d("onTOuchEvent","on touch event x: "+hexX+". y:"+hexY);
			if((results[1]<=HexBoard.BOARD_SIZE-1) && (results[1]>=0) && (results[0]<=HexBoard.BOARD_SIZE-1) && (results[0]>=0) ){
				pressButton((int)results[0],(int)results[1]);
				//if(moved((int)results[0],(int)results[1]))
					//endGameDialog("Congratulations! You won!")
				//else{
					//int[] next=getNextMove();
					//pressButton(next[0],next[1]);
				//if(next[2]==1)
            		//endGameDialog("Oh darn! you lost!");
			//}
			}
			return super.onTouchEvent(event);
		}
		return false;
	}
	
	private void endGameDialog(String msg){
		AlertDialog.Builder builder = new AlertDialog.Builder(this.game);
        builder.setMessage(msg);
        builder.setCancelable(true);
        builder.setPositiveButton("Start a new game",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	Intent intent = new Intent (game,  game.getClass() );            	getContext().startActivity(intent);
            }
        });
        builder.setNegativeButton("Exit",
                new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	((Activity) getContext()).finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
	}
	
	public void pressButton(int i,int j){
		Button b=this.buttons[i][j];
		if(b.isEnabled()){
			b.setPressed(true);
			b.setEnabled(false);
			if(HexGame.PLAYING)
				b.setId(HexGame.ME);
			else
				b.setId(HexGame.OPPONENT);
			postInvalidate();
			HexGame.PLAYING=!HexGame.PLAYING;
		}
	}
	
	private float[] HexValues()
	{	// some useful values for representing hex shape
		//[  0    |   1    |    2      |     3     |   4   |     5      ]
		//[Radius | Height | RowHeight | HalfWidth | Width | ExtraHeight]
		float [] values=new float[6];
		values[0] = HexBoard.RADIUS;
	    values[1] =2* values[0];
	    values[2] =1.5f* values[0];
	    values[3] = (float)Math.sqrt((values[0] * values[0]) - ((values[0] /2) * (values[0] /2)));
	    values[4] =2*values[3];
	    values[5] = values[1] - values[2];
	    return values;
	}
	
    private void setButtons(HexGame game) {
        this.game = game;
        buttons = new Button[HexBoard.BOARD_SIZE][HexBoard.BOARD_SIZE];
        for(int i = 0; i < HexBoard.BOARD_SIZE; i++) 
            for(int j = 0; j < HexBoard.BOARD_SIZE; j++){ 
                buttons[i][j] = new Button(game);  
            }
        }
    
	//converts the pixels that were clicked to the hexagon that those pixels are in it.
	private double[] pixelToHex(MotionEvent event){
		double results[]=new double[2];
		float x = event.getX() + this.hexValues[3];
		float y = event.getY() + this.hexValues[1] /2;
		int matrixX = (int)x / (int)this.hexValues[4];
		int matrixY = (int)y / (int)this.hexValues[2]; 
		int mX = (int)x % (int)this.hexValues[4];
		int mY = (int)y % (int)this.hexValues[2];
		double hexY=matrixY;
		double hexX=matrixX;
		boolean gridType=false;
		float scale = this.hexValues[5] / this.hexValues[3]; 
		if (((int)matrixY & 1) == 0)
			gridType = true; 
		if(!gridType){
			if (mX >= this.hexValues[3])
			{
				if (mY < (2* this.hexValues[5] - mX * scale))
				{
					// Top hexagon
					hexY = matrixY -1;
					hexX = matrixX;
				}
				else
				{
					// Right hexagon
					hexY = matrixY;
					hexX = matrixX;
				}
			}
			if (mX < this.hexValues[3])
			{
				if (mY < (mX * scale))
				{
					// Top hexagon
					hexY = matrixY -1;
					hexX = matrixX;
				}
				else
				{
					// Left hexagon
					hexY = matrixY;
					hexX = matrixX -1;
				}
			}
		}
		else{
			{
				// middle hexagon
				hexY = matrixY;
				hexX = matrixX;
				// left hexagon
				if (mY < (this.hexValues[5] - mX * scale))
				{
					hexY = matrixY -1;
					hexX = matrixX -1;
				}
				// right hexagon
				if (mY < (-this.hexValues[5] + mX * scale))
				{
					hexY = matrixY -1;
					hexX = matrixX;
				}
			}
		}
		hexY--;
		hexX-=(Math.ceil(hexY/2));
		results[0]=hexY;
		results[1]=hexX;
		return results;
	}
	
	private void drawLines(Canvas canvas){
		for (float i=0;i<HexBoard.BOARD_SIZE;i++){
			for (float j=0;j<HexBoard.BOARD_SIZE && j<=i;j++){
				drawHexLines(i,j,canvas,HexBoard.RADIUS);
			}
		}
		for (float j=HexBoard.BOARD_SIZE;j<2*HexBoard.BOARD_SIZE;j++){
				for(float i=j-(HexBoard.BOARD_SIZE-1) ;i < HexBoard.BOARD_SIZE ;i++){
					drawHexLines(j,i,canvas,HexBoard.RADIUS);
					}
				}
	}
	private void drawHexLines(float i,float j,Canvas canvas,float radius){
        float x1,x2,y1,y2;
        Paint p=new Paint();
        p.setColor(Color.BLACK);
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
	
	private void paintHexagon(float i, float j, float radius,Canvas canvas,int c,boolean isCreating) {
        float x1, x2, y1, y2;
        float width=(float)Math.sqrt(3)*radius;
        float dX=width/2;
        float dY=radius/2;
		Path path = new Path();
		this.p.setColor(c);
		//Log.d("paintHexagon","painting on "+i+","+j+" with color "+c);
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
