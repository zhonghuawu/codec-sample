package com.envisioniot.transport.context;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.SystemPropertyUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Locale;

@SuppressWarnings("unchecked")
@Slf4j
public class NettyContextResolver {
    public static ServerNettyContext createServerContext(int nBossThreads, int nWorkerThreads) {
        return doCreateServerContext(nBossThreads, nWorkerThreads);
    }

    public static ServerNettyContext createServerContext(int nThreads) {
        return doCreateServerContext(nThreads, -1);
    }

    public static ClientNettyContext createClientContext(int nThreads) {
        return doCreateClientContext(nThreads);
    }

    /**
     * Create the client netty context based on a server netty context
     * @param context
     * @return
     */
    public static ClientNettyContext createClientContext(ServerNettyContext context) {
        if (context.getWorkerGroup() instanceof EpollEventLoopGroup) {
            return new ClientNettyContext(context.getWorkerGroup(), EpollSocketChannel.class);
        }
        return new ClientNettyContext(context.getWorkerGroup(), NioSocketChannel.class);
    }

    private static ServerNettyContext doCreateServerContext(int nBossThreads, int nWorkerThreads) {
        ServerNettyContext context = null;

        if (isUsingLinuxAndAMD64()) {
            try {
                log.info("trying to use epoll for server context in Linux ...");
                EventLoopGroup bossGroup = new EpollEventLoopGroup(nBossThreads);
                EventLoopGroup workerGroup = bossGroup;
                if (nWorkerThreads >= 0) {
                    workerGroup = new EpollEventLoopGroup(nWorkerThreads);
                }

                context = new ServerNettyContext(bossGroup, workerGroup, EpollServerSocketChannel.class);
                log.info("initialized epoll successfully");
            } catch (Throwable e) {
                log.info("failed to init epoll, would try NIO", e);
            }
        }

        if (context == null) {
            EventLoopGroup bossGroup = new NioEventLoopGroup(nBossThreads);
            EventLoopGroup workerGroup = bossGroup;
            if (nWorkerThreads >= 0) {
                workerGroup = new NioEventLoopGroup(nWorkerThreads);
            }
            context = new ServerNettyContext(bossGroup, workerGroup, NioServerSocketChannel.class);
        }

        return context;
    }

    private static ClientNettyContext doCreateClientContext(int nThreads) {
        ClientNettyContext context = null;

        if (isUsingLinuxAndAMD64()) {
            try {
                log.info("trying to use epoll for client context in Linux ...");
                EventLoopGroup group = new EpollEventLoopGroup(nThreads);
                context = new ClientNettyContext(group, EpollSocketChannel.class);
                log.info("initialized epoll successfully");
            } catch (Throwable e) {
                log.info("failed to init epoll, would try NIO", e);
            }
        }

        if (context == null) {
            EventLoopGroup group = new NioEventLoopGroup(nThreads);
            context = new ClientNettyContext(group, NioSocketChannel.class);
        }

        return context;
    }

    private static boolean isUsingLinuxAndAMD64() {
        String osName = SystemPropertyUtil.get("os.name").toLowerCase(Locale.ENGLISH).trim();
        String arch = SystemPropertyUtil.get("os.arch").toLowerCase(Locale.ENGLISH).trim();
        return osName.contains("linux") && arch.contains("amd64");
    }
}
