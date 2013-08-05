package com.pi.villa.gen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.pi.Filter;
import com.pi.villa.gen.room.Room;
import com.pi.villa.gen.room.RoomJoint;
import com.pi.villa.gen.room.RoomType;
import com.pi.villa.gen.room.Rooms;
import com.pi.villa.gen.room.Rooms.RoomData;

public class PathConnector {
	public static List<PositionedRoom> getConnectableRooms(
			PositionedRoom r, RoomJoint nodeEntry,
			Filter<PositionedRoom> roomFilter) {
		RoomData myData = Rooms.getRoomData(r.getRoom());
		List<PositionedRoom> rooms =
				new ArrayList<PositionedRoom>();
		for (RoomData rD : Rooms.iterator()) {
			if ((nodeEntry.type & rD.getRoomType()) > 0) {
				for (Orientation o : rD.getOrientations()) {
					Room temp = rD.createInstance(o);
					if (temp != null) {
						Iterator<RoomJoint> roomNodes =
								temp.getPathNodes(nodeEntry.side
										.getInverse());
						RoomJoint otherNode;
						while ((otherNode = roomNodes.next()) != null) {
							if (myData != null
									&& (otherNode.type & myData
											.getRoomType()) == 0) {
								continue;
							}
							// Computes!
							int nodeDiff =
									nodeEntry.axis
											- otherNode.axis;
							int yDiff =
									nodeEntry.y - otherNode.y;
							PositionedRoom pR = null;
							switch (nodeEntry.side) {
							case Z_MINUS:
								pR =
										new PositionedRoom(
												temp,
												r.getX()
														+ nodeDiff,
												r.getY() + yDiff,
												r.getZ()
														- temp.getDepth());
								break;
							case Z_PLUS:
								pR =
										new PositionedRoom(
												temp,
												r.getX()
														+ nodeDiff,
												r.getY() + yDiff,
												r.getZ()
														+ r.getRoom()
																.getDepth());
								break;
							case X_PLUS:
								pR =
										new PositionedRoom(
												temp,
												r.getX()
														+ r.getRoom()
																.getWidth(),
												r.getY() + yDiff,
												r.getZ()
														+ nodeDiff);
								break;
							case X_MINUS:
								pR =
										new PositionedRoom(
												temp,
												r.getX()
														- temp.getWidth(),
												r.getY() + yDiff,
												r.getZ()
														+ nodeDiff);
								break;
							}
							if (pR != null) {
								int count =
										roomFilter.accept(pR);
								for (int i = 0; i < count; i++) {
									if (rD == null
											|| Math.random() <= rD
													.getRarity()) {
										rooms.add(pR);
									}
								}
							}
						}
					}
				}
			}
		}
		return rooms;
	}
}
