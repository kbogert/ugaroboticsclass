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
			play(f, 1.0/6.0);
			play(d, 1.0/6.0);
			play(c, 1.0/6.0);
			play(d, 2.0/6.0);
			play(bflat, 3.0/16.0);
			play(bflat, 1.0/16.0);
			play(c, 1.0/16.0);
			play(d, 1.0/16.0);
			play(eflat, 1.0/16.0);
			// m6
			play(f, 1.0/8.0);
			play(bflat, 1.0/8.0);
			play(bflat, 1.0/16.0);
			play(c, 1.0/16.0);
			play(d, 1.0/16.0);
			play(eflat, 1.0/16.0);
			play(f, 1.0/8.0);
			play(f, 1.0/8.0);
			play(f, 1.0/6.0);
			play(gflat, 1.0/6.0);
			play(aflat, 1.0/6.0);
			// m7
			play(b, 1.0/8.0);
			play(gflat, 1.0/8.0);
			play(gflat, 1.0/16.0);
			play(aflat, 1.0/16.0);
			play(bflat, 1.0/16.0);
			play(c, 1.0/16.0);
			play(csharp, 1.0/8.0);
			play(bflat, 1.0/16.0);
			play(dflat, 1.0/16.0);
			play(bflat, 1.0/6.0);
			play(aflat, 1.0/6.0);
			play(gflat, 1.0/6.0);
			// m8
			play(aflat, 2.0/6.0);
			play(gflat, 1.0/6.0);
			play(f, 1.0/6.0);
			play(aflat, 1.0/6.0);
			play(gflat, 1.0/6.0);
			play(aflat, 1.0/6.0);
			play(aflat, 2.0/6.0);
			play(aflat, 1.0/6.0);
			play(aflat, 1.0/6.0);
			play(aflat, 1.0/6.0);
			// m9
			play(eflat, 1.0/8.0);
			play(eflat, 1.0/16.0);
			play(f, 1.0/16.0);
			play(gflat, 1.0/8.0);
			play(gflat, 1.0/16.0);
			play(aflat, 1.0/16.0);
			play(bflat, 1.0/4.0);
			play(f, 1.0/8.0);
			play(eflat, 1.0/8.0);
			// m10
			play(dflat, 1.0/8.0);
			play(dflat, 1.0/16.0);
			play(eflat, 1.0/16.0);
			play(f, 1.0/8.0);
			play(f, 1.0/16.0);
			play(gflat, 1.0/16.0);
			play(aflat, 1.0/4.0);
			play(eflat, 1.0/8.0);
			play(dflat, 1.0/8.0);
			play(c, 1.0/8.0);
			play(c, 1.0/16.0);
			play(d, 1.0/16.0);
			play(e, 1.0/8.0);
			play(e, 1.0/16.0);
			play(f, 1.0/16.0);
			play(g, 1.0/8.0);
			play(g, 1.0/16.0);
			play(a, 1.0/16.0);
			play(bflat, 1.0/8.0);
			play(c, 1.0/8.0);
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
			play(f,  1/6.0);
			play(d,  1/6.0);
			play(c,  1/6.0);
			play(d,  2/6.0);
			play(bflat,  3/6.0);
			play(bflat,  1/16.0);
			play(c,  1/16.0);
			play(d,  1/16.0);
			play(eflat,   1/16.0);
			//m.14
			play(f,  1/8.0);
			play(bflat,  1/8.0);
			play(bflat,  1/16.0);
			play(c,  1/16.0);
			play(d,  1/16.0);
			play(eflat,   1/16.0);
			play(f,  1/8.0);
			play(f,  1/8.0);
			play(f,  1/6.0);
			play(gflat,   1/6.0);
			play(aflat,   1/6.0);
			//m.15
			play(bflat,  3/2.0);
			play(csharp,  1/2.0);
			//m.16
			play(c,  1/2.0);
			play(a,  1.0);
			play(f,  1/2.0);
			//m.17
			play(fsharp,  3/2.0);
			play(asharp,   1/2.0);
			//m.18
			play(a,  1/2.0);
			play(f,  1.0);
			play(f,  1/2.0);
			//m.19
			play(fsharp,  3/2.0);
			play(asharp,   1.0);
			//m.20
			play(a,  1/2.0);
			play(f,  1.0);
			play(d,  1/2.0);
			//m.21
			play(eflat,   3/2.0);
			play(fsharp,  1.0);
			play(f,  1.0);
			play(dflat,   1.0);
			play(bflat,  1/2.0);
			//m.23
			play(c,  1/8.0);
			play(c,  1/16.0);
			play(d,  1/16.0);
			play(e,  1/8.0);
			play(e,  1/16.0);
			play(f,  1/16.0);
			play(g,  1/8.0);
			play(g,  1/16.0);
			play(a,  1/16.0);
			play(bflat,  1/18.0);
			play(c,  1/8.0);
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
			
			
		}
	}
	
	private void play(short note, double time) {
		buzzer.play(note, (int)(time * 1000.0));
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
		}
	}

}
