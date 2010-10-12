
	public class MapObj {
		private byte type;
		private MapObj next;
		private int x;
		private int y;
		private int h;
		
		public static final byte EMPTY_SQUARE = 0;
		public static final byte TABLE_EDGE = 1;
		public static final byte OBJECT = 2;
		public static final byte HOME = 3;
		public static final byte EXAMINED_OBJECT = 4;
	
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

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public int getH() {
			return h;
		}

		public void setH(int h) {
			this.h = h;
		}
		
		
	}
