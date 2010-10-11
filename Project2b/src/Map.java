
public class Map {

	private MapObj [][][] myMap1;
	private MapObj [][][] myMap2;
	private boolean whichMap;
	private int xlen;
	private int ylen;
	private int hlen;
	
	public Map(int sizex, int sizey, int sizeh) {
		myMap1 = new MapObj[sizex][sizey][sizeh];
		
		for (int i = 0; i < sizex; i ++) {
			for (int j = 0; j < sizey; j ++) {
				for (int k = 0; k < sizeh; k ++) {
					myMap1[i][j][k] = new MapObj();
				}
			}
		}
		myMap2 = new MapObj[sizex][sizey][sizeh];
		
		for (int i = 0; i < sizex; i ++) {
			for (int j = 0; j < sizey; j ++) {
				for (int k = 0; k < sizeh; k ++) {
					myMap2[i][j][k] = new MapObj();
				}
			}
		}		
		xlen = sizex;
		ylen = sizey;
		hlen = sizeh;
	}
	
	public synchronized MapObj getPos(int x,int y,int h) {
		if (whichMap)
			return myMap1[x+1][y+1][h+1];
		else 
			return myMap2[x+1][y+1][h+1];		
	}

	public synchronized MapObj getNewPos(int x,int y,int h) {
		if (whichMap)
			return myMap2[x+1][y+1][h+1];
		else 
			return myMap1[x+1][y+1][h+1];		
	}
	
	public synchronized void switchMaps() {
		whichMap = !whichMap;
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
