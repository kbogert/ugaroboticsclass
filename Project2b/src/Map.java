
public class Map {

	private MapObj head;
	private int xlen;
	private int ylen;
	
	public Map(int sizex, int sizey) {
		
		xlen = sizex;
		ylen = sizey;
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
	
	public void setMapObjs(MapObj newhead) {
		head = newhead;
	}
	
	public MapObj getMapObjs() {
		return head;
	}
		
}
