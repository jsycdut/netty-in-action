package com.piperstack;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
  private final String host;
  private final int port;

  public Client(String host, int port) {
    this.host = host;
    this.port = port;
  }

  public void start() throws Exception {
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap b = new Bootstrap();
      b.group(group)
        .channel(NioSocketChannel.class)
        .remoteAddress(new InetSocketAddress(this.host, this.port))
        .handler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new ClientHandler());
          }
        });
      ChannelFuture f = b.connect().sync();
      f.channel().closeFuture().sync();

    } finally {
      group.shutdownGracefully().sync();
    }
  }

  public static void main(String... args) throws Exception {
    if (args.length != 2) {
      System.err.println("Usage: " + Client.class.getSimpleName() + " <host> <port>");
      return;
    }

    new Client(args[0], Integer.parseInt(args[1])).start();
  }
}
