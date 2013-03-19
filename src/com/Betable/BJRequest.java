package com.Betable;

/**
 * UI options
 * @author yun-chiao
 *
 */
enum Operation {
	Add("create a game; returns: game id"),
	Join("join a game; param: game id; returns player position, player id"),
	List("list available games"),
	Get("get game state; param: game id"),
	Start("start a game; param: game id"),
	Hit("take another card; param: game id, player id"),
	Stand("yield turn to next player; param: game id, player id"),
	Win("query game winner; param: game id"), 
	Help("this message");
	
	String msg;
	Operation(String msg) {
		this.msg = msg;
	}

	static String help() {
		StringBuilder sb = new StringBuilder();
		for (Operation op : values())
			sb.append(op).append(" -- ").append(op.msg).append('\n');
		return sb.toString();
	}
}

/**
 * Request to Blackjack server
 * @author yun-chiao
 *
 */
class BJRequest {
	private final static long DONT_CARE = 0;
	public final Operation op;
	public final long gid, pid;

	BJRequest(Operation op, long gid, long pid) {
		this.op = op;
		this.gid = gid;
		this.pid = pid;
	}

	BJRequest(Operation op, long gid) {
		this(op, gid, DONT_CARE);
	}

	BJRequest(Operation op) {
		this(op, DONT_CARE, DONT_CARE);
	}
	
	@Override
	public String toString() {
		return String.format("Request(op=%s, gid=%s, pid=%s)", op,gid,pid);
	}
}