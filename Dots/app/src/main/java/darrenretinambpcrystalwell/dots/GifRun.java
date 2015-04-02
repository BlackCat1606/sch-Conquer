package darrenretinambpcrystalwell.dots;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

/**
 *
 * @author Darren Ng 1000568
 *
 * GitRun runs the decoded GIFDecode
 * Canvas redraws infinitely
 *
 *
 */

public class GifRun implements Runnable, Callback {

	
	public Bitmap bmb;
	public GIFDecode decode;
	public int ind;
	public int gifCount;
	public SurfaceHolder mSurfaceHolder ;
	boolean surfaceExists;

    private static Context context;

    public GifRun(Context context) {
        this.context = context;
    }
	
	public void LoadGiff(SurfaceView v, android.content.Context theTHIS, int R_drawable)
	{		
	       mSurfaceHolder = v.getHolder();
	       mSurfaceHolder.addCallback(this);
	       decode = new GIFDecode();
	       decode.read(theTHIS.getResources().openRawResource(R_drawable));
	       ind = 0;
			// decode.
			gifCount = decode.getFrameCount();
			bmb = decode.getFrame(0);
			surfaceExists=true;
			Thread t = new Thread(this);
			t.start();
	}

	public void run() 
	{
		while (surfaceExists) {
			try {
				
					Canvas rCanvas = mSurfaceHolder.lockCanvas();
                    Bitmap b = Bitmap.createScaledBitmap(bmb,ScreenDimensions.getWidth(context),ScreenDimensions.getHeight(context),false);
                    rCanvas.drawBitmap(b,0,0,new Paint());
//					rCanvas.drawBitmap(b, (rCanvas.getWidth()-decode.width)/2,(rCanvas.getHeight()-decode.height)/2 , new Paint());
					//ImageView im = (ImageView) findViewById(R.id.imageView1);
					//im.setImageBitmap(bmb);
					
					mSurfaceHolder.unlockCanvasAndPost(rCanvas);
					bmb = decode.next();
					
				Thread.sleep(5);
			} catch (Exception ex) {

			}
		}
		
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) 
	{
		
		
		
	}

	public void surfaceCreated(SurfaceHolder holder) 
	{
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		
		surfaceExists=false;
	}
	
}
