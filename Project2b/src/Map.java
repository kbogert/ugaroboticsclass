
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
			return myMap1[x][y][h];
		else 
			return myMap2[x][y][h];		
	}

	public float getNewPos(int x,int y,int h) {
		if (whichMap)
			return myMap2[x][y][h];
		else 
			return myMap1[x][y][h];		
	}
	
	public void setPos(int x,int y,int h, float prob) {
		if (whichMap)
			myMap1[x][y][h] = prob;
		else 
			myMap2[x][y][h] = prob;		
	}

	public void setNewPos(int x,int y,int h, float prob) {
		if (whichMap)
			myMap2[x][y][h] = prob;
		else 
			myMap1[x][y][h] = prob;		
	}
	
	public void switchMaps() {
		whichMap = !whichMap;
	}
	
	public int getMaxX() {
		return xlen; 
	}
	
	public int getMinX() {
		return 0;
	}

	public int getMaxY() {
		return ylen ;
	}
	
	public int getMinY() {
		return 0;
	}

	public int getMaxH() {
		return hlen;
	}
	
	public int getMinH() {
		return 0;
	}
	
	public void setMapObjs(MapObj newhead) {
		head = newhead;
	}
	
	public MapObj getMapObjs() {
		return head;
	}
		
}
