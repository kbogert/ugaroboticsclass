import com.ridgesoft.intellibrain.IntelliBrain;
import com.ridgesoft.io.Speaker;



public class PlayMusic implements Runnable {

	public static final short c = 262;
	public static final short csharp = 277;
	public static final short dflat = csharp;
	public static final short d = 294;
	public static final short dsharp = 311;
	public static final short eflat = dsharp;
	public static final short e = 330;
	public static final short f = 394;
	public static final short fsharp = 370;
	public static final short gflat = fsharp;	
	public static final short g = 392;
	public static final short gsharp = 415;
	public static final short aflat = gsharp;
	public static final short a = 440;
	public static final short asharp = 466;
	public static final short bflat = asharp;
	public static final short b = 494;

	Speaker buzzer;
	
	public void run() {
		buzzer = IntelliBrain.getBuzzer();
		
		while (true) {
			// m1
			play(bflat, 65.0/32.0);
			play(bflat, 1.0/32.0);
			play(bflat, 1.0/6.0);
			play(bflat, 1.0/6.0);
			play(bflat, 1.0/6.0);
			// m2
			play(bflat, 2.0/6.0);
			play(aflat, 1.0/6.0);
			play(bflat, 19.0/16.0);
			play(bflat, 1.0/12.0);
			play(bflat, 1.0/6.0);
			play(bflat, 1.0/6.0);
			play(bflat, 1.0/6.0);
			// m3
			play(bflat, 2.0/6.0);
			play(aflat, 1.0/6.0);
			play(bflat, 19.0/16.0);
			play(bflat, 1.0/32.0);
			play(bflat, 1.0/6.0);
			play(bflat, 1.0/6.0);
			play(bflat, 1.0/6.0);
			// m4
			play(bflat, 1.0/8.0);
			play(f, 1.0/16.0);
			play(f, 1.0/16.0);
			play(f, 1.0/8.0);
			play(f, 1.0/16.0);
			play(f, 1.0/16.0);
			play(f, 1.0/8.0);
			play(f, 1.0/16.0);
			play(f, 1.0/16.0);
			play(f, 1.0/8.0);
			play(f, 1.0/8.0);
			// m5
			play(bflat, 1.0/4.0);
			play(f, 5.0/6.0, true);
/*			play(d, 1.0/6.0, true);*/
//			play(c, 1.0/6.0); 
//			play(d, 2.0/6.0, true);
//			play(bflat, 3.0/16.0);
			play(bflat, 1.0/8.0);
			play((short)(c * 2), 1.0/8.0);
			play((short)(d * 2), 1.0/8.0);
			play((short)(eflat * 2), 1.0/8.0);
			// m6
			play((short)(f * 2), 5.0/8.0);
/*			play(bflat, 1.0/8.0);
			play(bflat, 1.0/16.0);
			play((short)(c * 2), 1.0/16.0);
			play((short)(d * 2), 1.0/16.0);
			play((short)(eflat * 2), 1.0/16.0); */
			play((short)(f * 2), 1.0/8.0);
			play((short)(f * 2), 1.0/8.0);
//			play((short)(f * 2), 1.0/6.0);
			play((short)(gflat * 2), 1.0/6.0);
			play((short)(aflat * 2), 1.0/6.0);
			// m7
			play((short)(b * 2), 5.0/8.0);
/*			play(gflat, 1.0/8.0);
			play(gflat, 1.0/16.0);
			play(aflat, 1.0/16.0);
			play(bflat, 1.0/16.0);
			play((short)(c * 2), 1.0/16.0);
			play((short)(csharp * 2), 1.0/8.0); */
			play((short)(bflat * 2), 1.0/16.0);
//			play((short)(dflat * 2), 1.0/16.0);
			play((short)(bflat * 2), 1.0/6.0);
			play((short)(aflat * 2), 1.0/6.0);
			play((short)(gflat * 2), 1.0/6.0);
			// m8
			play((short)(aflat * 2), 2.0/6.0);
			play((short)(gflat * 2), 1.0/6.0);
			play((short)(f * 2), 4.0/6.0);
/*			play(aflat, 1.0/6.0);
			play(gflat, 1.0/6.0);
			play(aflat, 1.0/6.0); 
			play(aflat, 2.0/6.0);
			play(aflat, 1.0/6.0);*/
			play(aflat, 1.0/6.0);
			play(aflat, 1.0/6.0);
			// m9
			play((short)(eflat * 2), 1.0/8.0);
			play((short)(eflat * 2), 1.0/16.0);
			play((short)(f * 2), 1.0/16.0);
			play((short)(gflat * 2), 3.0/8.0);
			/*play(gflat, 1.0/16.0);
			play(aflat, 1.0/16.0);
			play(bflat, 1.0/4.0);*/
			play((short)(f * 2), 1.0/8.0);
			play((short)(eflat * 2), 1.0/8.0);
			// m10
			play((short)(dflat * 2), 1.0/8.0);
			play((short)(dflat * 2), 1.0/16.0);
			play((short)(eflat * 2), 1.0/16.0);
			play((short)(f * 2), 3.0/8.0);
/*			play(f, 1.0/16.0);
			play(gflat, 1.0/16.0);
			play(aflat, 1.0/4.0); */
			play((short)(eflat * 2), 1.0/8.0);
			play((short)(dflat * 2), 1.0/8.0);
			play((short)(c * 2), 1.0/8.0);
			play((short)(c * 2), 1.0/16.0);
			play((short)(d * 2), 1.0/16.0);
			play((short)(e * 2), 4.0/8.0);
/*			play(e, 1.0/16.0);
			play(f, 1.0/16.0);
			play(g, 1.0/8.0); 
			play(g, 1.0/16.0);
			play(a, 1.0/16.0); */
			play(bflat, 1.0/8.0);
			play((short)(c * 2), 1.0/8.0);
			// m12
			play(f,  1/8.0);
			play(f,  1/16.0);
			play(f,  1/16.0);
			play(f,  1/8.0);
			play(f,  1/16.0);
			play(f,  1/16.0);
			play(f,  1/8.0);
			play(f,  1/16.0);
			play(f,  1/16.0);
			play(f,  1/8.0);
			play(f,  1/8.0);
			//m.13
			play(bflat,  1/4.0);
			play(f,  5/6.0,true);
/*			play(d,  1/6.0,true);
			play(c,  1/6.0,true);
			play(d,  2/6.0,true); 
			play(bflat,  3/6.0); */
			play(bflat,  1/16.0);
			play((short)(c * 2),  1/16.0);
			play((short)(d * 2),  1/16.0);
			play((short)(eflat * 2),   1/16.0);
			//m.14
			play((short)(f * 2),  4/8.0);
/*			play(bflat,  1/8.0);
			play(bflat,  1/16.0);
			play((short)(c * 2),  1/16.0);
			play((short)(d * 2),  1/16.0);
			play((short)(eflat * 2),   1/16.0); */
			play((short)(f * 2),  1/8.0);
			play((short)(f * 2),  1/8.0);
			play((short)(f * 2),  1/6.0);
			play((short)(gflat * 2),   1/6.0);
			play((short)(aflat * 2),   1/6.0);
			//m.15
			play((short)(bflat * 2),  3/2.0);
			play((short)(csharp * 2),  1/2.0);
			//m.16
			play((short)(c * 2),  1/2.0);
			play((short)(a * 2),  1.0);
			play((short)(f * 2),  1/2.0);
			//m.17
			play((short)(fsharp * 2),  3/2.0);
			play((short)(asharp * 2),   1/2.0);
			//m.18
			play((short)(a * 2),  1/2.0);
			play((short)(f * 2),  1.0);
			play((short)(f * 2),  1/2.0);
			//m.19
			play((short)(fsharp * 2),  3/2.0);
			play((short)(asharp * 2),   1.0);
			//m.20
			play((short)(a * 2),  1/2.0);
			play((short)(f * 2),  1.0);
			play((short)(d * 2),  1/2.0);
			//m.21
			play((short)(eflat * 2),   3/2.0);
			play((short)(fsharp * 2),  1.0);
			play((short)(f * 2),  1.0);
			play((short)(dflat * 2),   1.0);
			play(bflat,  1/2.0);
			//m.23
			play((short)(c * 2),  1/8.0);
			play((short)(c * 2),  1/16.0);
			play((short)(d * 2),  1/16.0);
			play((short)(e * 2),  1/8.0);
			play(e,  1/16.0);
			play(f,  1/16.0);
			play(g,  1/8.0);
			play(g,  1/16.0);
			play(a,  1/16.0);
			play(bflat,  1/18.0);
			play((short)(c * 2),  1/8.0);
			//m.24
			play(f,  1/8.0);
			play(f,  1/16.0);
			play(f,  1/16.0);
			play(f,  1/8.0);
			play(f,  1/16.0);
			play(f,  1/16.0);
			play(f,  1/8.0);
			play(f,  1/16.0);
			play(f,  1/16.0);
			play(f,  1/8.0);
			play(f,  1/8.0);
			pause(1.0/2.0);
			
		}
	}
	private void play(short note, double time) {
		play(note, time, false);
	}
	
	private void play(short note, double time, boolean nopause) {
		buzzer.play(note, (int)(time * 1000.0));
		if (nopause)
			return;
		try {
			Thread.sleep(50);
		} catch (InterruptedException e) {
		} 
	}
	
	private void pause(double time) {
		try {
			Thread.sleep((int)(time * 1000));
		} catch (InterruptedException e) {
		}		
	}

}
