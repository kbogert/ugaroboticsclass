
public class Map {

	private MapObj [][][] myMap;
	private int xlen;
	private int ylen;
	private int hlen;
	
	public Map(int sizex, int sizey, int sizeh) {
		myMap = new MapObj[sizex][sizey][sizeh];
		
		for (int i = 0; i < sizex; i ++) {
			for (int j = 0; j < sizey; j ++) {
				for (int k = 0; k < sizeh; k ++) {
					myMap[i][j][k] = new MapObj();
				}
			}
		}
		
		xlen = sizex;
		ylen = sizey;
		hlen = sizeh;
	}
	
	public MapObj getPos(int x,int y,int h) {
		int xshift = x + (xlen - 1);
		int yshift = y + (ylen - 1);
		int hshift = h + (hlen - 1);
		return myMap[xshift][yshift][hshift];
	}
	
	public int getMaxX() {
		return xlen - 1; 
	}
	
	public int getMinX() {
		return -1;
	}

	public int getMaxY() {
		return ylen - 1 ;
	}
	
	public int getMinY() {
		return -1;
	}

	public int getMaxH() {
		return hlen - 1 ;
	}
	
	public int getMinH() {
		return -1;
	}
		
	public class MapObj {
		private byte type;
		private float robotProbability;
		
		public static final byte UNEXPLORED = -1;
		public static final byte EMPTY_SQUARE = 0;
		public static final byte TABLE_EDGE = 1;
		public static final byte OBJECT = 2;
		public static final byte HOME = 3;
		public static final byte EXAMINED_OBJECT = 4;
	
		public MapObj() {
			type = UNEXPLORED;
		}

		public synchronized byte getType() {
			return type;
		}

		public synchronized void setType(byte type) {
			this.type = type;
		}

		public synchronized boolean isHasExaminedObject() {
			return type == EXAMINED_OBJECT;
		}

		public synchronized void setHasExaminedObject(boolean hasExaminedObject) {
			this.type = EXAMINED_OBJECT;
		}

		public float getRobotProbability() {
			return robotProbability;
		}

		public void setRobotProbability(float robotProbability) {
			this.robotProbability = robotProbability;
		}
		
	}
}
