package com.Betable;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

/**
 * Http request handler.
 * @author yun-chiao
 *
 */
class BJHandler extends AbstractHandler {
	private static final String PID = "pid";
	private static final String GID = "gid";
	private static final String OP = "op";
	private final GameAdmin admin;

	BJHandler(GameAdmin admin) {
		this.admin = admin;
	}

	public void handle(String target, Request baseRequest,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String opStr = request.getParameter(OP);
		if (opStr == null)
			return;
		
		baseRequest.setHandled(true);
		response.setContentType("text/plain;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);

		BJRequest req;
		Operation op = Operation.valueOf(opStr);
		String gid = request.getParameter(GID);
		String pid = request.getParameter(PID);
		if (!gid.isEmpty() && !pid.isEmpty())
			req = new BJRequest(op, Long.valueOf(gid), Long.valueOf(pid));
		else if (!gid.isEmpty())
			req = new BJRequest(op, Long.valueOf(gid));
		else
			req = new BJRequest(op);
		response.getWriter().println(admin.serve(req));
	}
}

/**
 * Main driver.
 * @author yun-chiao
 *
 */
class BJServer {

	public static void main(String[] args) throws Exception {
		GameAdmin admin = new GameAdmin(new Room());
		
		/* simple UI */
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setResourceBase("src/web");
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[]{new BJHandler(admin),resourceHandler});
		
		Server server = new Server(8080);
		server.setHandler(handlers);

		server.start();
		server.join();
	}
}
