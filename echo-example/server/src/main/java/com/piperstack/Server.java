package com.piperstack;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {
  private final int port;

  public Server(int port) {
    this.port = port;
  }

  public static void main(String... args) throws Exception {
    if (args.length != 1) {
      System.err.println("Usage: " + Server.class.getSimpleName() + " <port>");
      return;
    }

    new Server(Integer.parseInt(args[0])).start();
  }

  public void start() throws Exception {
    final ServerHandler handler = new ServerHandler();
    EventLoopGroup group = new NioEventLoopGroup();

    try {
      ServerBootstrap b = new ServerBootstrap();
      b.group(group)
        .channel(NioServerSocketChannel.class)
        .localAddress(new InetSocketAddress(this.port))
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          public void initChannel(SocketChannel ch) {
            ch.pipeline().addLast(handler);
          }
        });

      ChannelFuture f = b.bind().sync();
      f.channel().closeFuture().sync();
    } finally {
      group.shutdownGracefully().sync();
    }
  }
}
