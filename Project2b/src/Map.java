
public class Map {

	private MapObj [][] myMap;
	private int xlen;
	private int ylen;
	
	public Map(int sizex, int sizey) {
		myMap = new MapObj[sizex][sizey];
		
		for (int i = 0; i < sizex; i ++) {
			for (int j = 0; j < sizey; j ++) {
				myMap[i][j] = new MapObj();
			}
		}
		
		xlen = sizex;
		ylen = sizey;
	}
	
	public MapObj getPos(int x,int y) {
		int xshift = x + (xlen / 2);
		int yshift = y + (ylen / 2);
		return myMap[xshift][yshift];
	}
	
	public int getMaxX() {
		return xlen / 2; 
	}
	
	public int getMinX() {
		return - xlen / 2;
	}

	public int getMaxY() {
		return ylen / 2;
	}
	
	public int getMinY() {
		return - ylen / 2;
	}
	
	public class MapObj {
		private byte type;
		private boolean hasExaminedObject;
		private float robotProbability;
		
		public static final byte UNEXPLORED = -1;
		public static final byte EMPTY_SQUARE = 0;
		public static final byte TABLE_EDGE = 1;
		public static final byte OBJECT = 2;
		public static final byte HOME = 3;
	
		public MapObj() {
			type = UNEXPLORED;
			hasExaminedObject = false;
		}

		public synchronized byte getType() {
			return type;
		}

		public synchronized void setType(byte type) {
			this.type = type;
		}

		public synchronized boolean isHasExaminedObject() {
			return hasExaminedObject;
		}

		public synchronized void setHasExaminedObject(boolean hasExaminedObject) {
			this.hasExaminedObject = hasExaminedObject;
		}

		public float getRobotProbability() {
			return robotProbability;
		}

		public void setRobotProbability(float robotProbability) {
			this.robotProbability = robotProbability;
		}
		
		
		
	}
}
