package com.jetty.rpc;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.util.thread.ExecutorThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @project JettyServer服务器，用于RPC调用
 * @file JettyServer.java 创建时间:2017年7月28日下午4:47:45
 * @description 描述（简要描述类的职责、实现方式、使用注意事项等）
 * @author dzn
 * @version 1.0
 *
 */
public class JettyServer {
	private static final Logger logger = LoggerFactory.getLogger(JettyServer.class);

	private Server server;
	private Thread thread;
	public void start(final int port) throws Exception {
		thread = new Thread(new Runnable() {
			@Override
			public void run() {

				// The Server
				server = new Server(new ExecutorThreadPool());  // 非阻塞

				// HTTP connector
				ServerConnector connector = new ServerConnector(server);
				connector.setPort(port);
				server.setConnectors(new Connector[]{connector});

				// Set a handler
				HandlerCollection handlerc =new HandlerCollection();
				handlerc.setHandlers(new Handler[]{new JettyServerHandler()});
				server.setHandler(handlerc);

				try {
					// Start the server
					server.start();
					server.join();	// block until thread stopped
				} catch (Exception e) {
					logger.error("", e);
				} finally {
					destroy();
				}
			}
		});
//		thread.setDaemon(true);	// daemon, service jvm, user thread leave >>> daemon leave >>> jvm leave
		thread.start();
	}

	/**
	 *@description 销毁服务器
	 *@time 创建时间:2017年7月28日下午4:48:10
	 *@author dzn
	 */
	public void destroy() {
		if (server != null) {
			try {
				server.stop();
				server.destroy();
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		if (thread.isAlive()) {
			thread.interrupt();
		}
	}

}
