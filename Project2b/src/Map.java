
public class Map {

	private float [][][] myMap1;
	private float [][][] myMap2;
	private boolean whichMap;
	private MapObj head;
	private int xlen;
	private int ylen;
	private int hlen;
	
	public Map(int sizex, int sizey, int sizeh) {
		myMap1 = new float[sizex][sizey][sizeh];
		myMap2 = new float[sizex][sizey][sizeh];
		
		xlen = sizex;
		ylen = sizey;
		hlen = sizeh;
	}
	
	public float getPos(int x,int y,int h) {
		if (whichMap)
			return myMap1[x+1][y+1][h+1];
		else 
			return myMap2[x+1][y+1][h+1];		
	}

	public float getNewPos(int x,int y,int h) {
		if (whichMap)
			return myMap2[x+1][y+1][h+1];
		else 
			return myMap1[x+1][y+1][h+1];		
	}
	
	public void setPos(int x,int y,int h, float prob) {
		if (whichMap)
			myMap1[x+1][y+1][h+1] = prob;
		else 
			myMap2[x+1][y+1][h+1] = prob;		
	}

	public void setNewPos(int x,int y,int h, float prob) {
		if (whichMap)
			myMap2[x+1][y+1][h+1] = prob;
		else 
			myMap1[x+1][y+1][h+1] = prob;		
	}
	
	public void switchMaps() {
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
	
	public void setMapObjs(MapObj newhead) {
		head = newhead;
	}
	
	public MapObj getMapObjs() {
		return head;
	}
		
	public class MapObj {
		private byte type;
		private MapObj next;
		
		public static final byte UNEXPLORED = -1;
		public static final byte EMPTY_SQUARE = 0;
		public static final byte TABLE_EDGE = 1;
		public static final byte OBJECT = 2;
		public static final byte HOME = 3;
		public static final byte EXAMINED_OBJECT = 4;
	
		public MapObj() {
			type = UNEXPLORED;
		}

		public byte getType() {
			return type;
		}

		public void setType(byte type) {
			this.type = type;
		}

		public boolean isHasExaminedObject() {
			return type == EXAMINED_OBJECT;
		}

		public void setHasExaminedObject(boolean hasExaminedObject) {
			this.type = EXAMINED_OBJECT;
		}
		
		public MapObj getNext() {
			return next;
		}
		
		public void setNext(MapObj newnext) {
			next = newnext;
		}
		
	}
}
