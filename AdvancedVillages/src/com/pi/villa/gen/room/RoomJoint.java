package com.pi.villa.gen.room;

import com.pi.villa.gen.Side;

public class RoomJoint {
	public int axis;
	public int y;
	public int type;
	public Side side;
	public boolean required;

	public RoomJoint(Side s, int sAxis, int sY, int sType,
			boolean sReq) {
		this.axis = sAxis;
		this.y = sY;
		this.side = s;
		this.type = sType;
		this.required = sReq;
	}
}
