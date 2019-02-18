package shibo.study.rpc.simple;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import shibo.study.proxy.ProxyFactoryManager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author zhangshibo
 */
@Log4j2
public class RpcFramework {

    private static final ExecutorService WORKING_POOL =
            new ThreadPoolExecutor(1, 10, 0L, TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(10), new ThreadFactory() {
                private AtomicInteger count = new AtomicInteger(1);

                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "rpc-framework-working-" + count.getAndIncrement());
                }
            });

    public static void export(Object service, int port) {
        Objects.requireNonNull(service, "service can not be null");
        log.info("Preparing to export service {} on port {}", service.getClass().getName(), port);
        ServerSocket server = createServerSocket(port);
        for (; ; ) {
            try (Socket socket = server.accept()) {
                try (ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                     ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream())) {
                    String methodName = input.readUTF();
                    Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
                    Object[] arguments = (Object[]) input.readObject();
                    Future<Object> future =
                            WORKING_POOL.submit(() -> doInvoke(service, methodName, parameterTypes, arguments));
                    output.writeObject(future.get());
                } catch (IOException | ClassNotFoundException e) {
                    log.error("Error while dealing request from client.", e);
                    throw new IllegalStateException(e);
                } catch (InterruptedException | ExecutionException e) {
                    log.error("Error while doing the work.");
                    throw new IllegalStateException(e);
                }
            } catch (IOException e) {
                log.error("can not accept socket", e);
                throw new IllegalStateException(e);
            }
        }

    }

    public static <T> T refer(Class<T> interfaceClass, String host, int port) {
        Objects.requireNonNull(interfaceClass, "Interface class must not null");
        if (!interfaceClass.isInterface()) {
            throw new IllegalArgumentException("The class " + interfaceClass.getName() + " must be interface!");
        }
        if (StringUtils.isEmpty(host)) {
            throw new IllegalArgumentException("Host must not be empty!");
        }
        log.info("Preparing to get remote service {} from server {}:{}", interfaceClass.getName(), host, port);
        return ProxyFactoryManager.defaultProxyFactory().getProxy(interfaceClass, (proxy, method, args) -> {
            try (Socket socket = new Socket(host, port)) {
                try (ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                     ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {
                    output.writeUTF(method.getName());
                    output.writeObject(method.getParameterTypes());
                    output.writeObject(args);
                    output.flush();

                    Object result = input.readObject();
                    if (result instanceof Throwable) {
                        throw (Throwable) result;
                    }
                    return result;
                } catch (IOException e) {
                    log.error("Error occurred while sending/reading info from server.", e);
                    throw new IllegalStateException(e);
                }
            } catch (IOException e) {
                log.error("Error while communicating with server.", e);
                throw new IllegalStateException(e);
            }
        });
    }

    private static Object doInvoke(Object service, String methodName, Class<?>[] parameterTypes, Object[] arguments) {
        try {
            Method method = service.getClass().getMethod(methodName, parameterTypes);
            if (log.isDebugEnabled()) {
                log.debug("Invoking method: {} with arguments: {}", methodName,
                        Arrays.stream(arguments).collect(Collectors.toList()));
            }
            return method.invoke(service, arguments);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            String msg = "can not invoke method: " + methodName;
            log.error(msg, e);
            throw new IllegalStateException(msg, e);
        }
    }

    private static ServerSocket createServerSocket(int port) {
        try {
            ServerSocket server = new ServerSocket(port);
            log.info("Created server socket on port {}", port);
            return server;
        } catch (IOException e) {
            String msg = "can not open server socket on port: " + port;
            log.error(msg, e);
            throw new IllegalStateException(msg, e);
        } catch (IllegalArgumentException | SecurityException e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }
}
