package domain;

public class RoomVO {
	private String roomMaster;
	private String roomName;
	private int rid;
	private int count;
	private int maxCount;
	private String pwd;
	private String created;
	private String server;
	
	
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getRoomMaster() {
		return roomMaster;
	}
	public void setRoomMaster(String roomMaster) {
		this.roomMaster = roomMaster;
	}
	public String getRoomName() {
		return roomName;
	}
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	@Override
	public String toString() {
		return "RoomVO [roomMaster=" + roomMaster + ", roomName=" + roomName + ", rid=" + rid + ", count=" + count
				+ ", maxCount=" + maxCount + ", pwd=" + pwd + ", created=" + created + ", server=" + server + "]";
	}
	
}
