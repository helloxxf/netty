# Netty

# 1、I/O类型

```
BIO:同步阻塞IO
NIO:同步非阻塞IO
AIO：异步非阻塞IO
```

## 2、BIO说明

```
面向流；服务器实现模式为一个连接一个线程，及客户端有连接请求时候就要启动一个线程进行处理，如果连接不作任何事情，就会造成不必要的浪费，可以使用线程池解决。
BIO适合连接数目比较少且架构比较固定。 实现简单。
```

## 3、NIO说明

```
面向缓冲区，数据读取到一个稍后处理的缓冲区，需要时可在缓冲区前后移动，提高处理的灵活性，使用它可以提供非阻塞式的高伸缩网络。
三核心：Buffer(缓冲区）， Channel（通道）, Selector（选择器）
```

## 4、Buffer、channel、selector三者关系

```
1、每个channel都会对应一个buffer。
2、selector对应一个线程，监控多个channel。
3、程序切换到那个channel是由事件决定的。
4、selector会根据不同的事件会在channel切换。
5、buffer就是一个内存块，底层数组实现。
7、数据的读写都是要通过buffer。bio中的要么是输入流要不是输出流，不能双向。
8、channel是双向的。
```

## 5、Buffer属性

```
1、Capcity:容量
2、Limit：缓冲区当前终点，不能对缓冲区超过的极限位置进行读写操作，且极限位置可以修改。
3、Position：位置，下一个要被读或者写的元素的索引，每次读写缓冲区都会改变这个值
4、mark：标记，基本不会主动修改
```

## 6、通道Channel

```
NIO的通道类似于流，但有些区别：
1）通道可以同时读写，流只可以读或者写。
2）通道可以异步读写数据。
3）通道可以从缓冲区读数据，也可以写到缓冲区中。
4）channel是NIO的一个接口
5）BIO的stream是单向的，例如FileInputStream 只可以读取数据操作， NIO的channel 是双向的，可以读操作也可写操作。
```

## 7、Selector(选择器)

```
1）可以用一个线程，处理多个客户端连接
2）Selector 能够检测多个注册的通道上的是否有事件发生（多个channel以事件的方式可以注册到Selector），
    如果有事情发生，便获区事件然后针对每个事件进行相应处理。这样就可以一个线程处理多个通道，也就是管理多个多个连接和请求。
3）只有在连接真正有读写事件发生时，才会进行读写，这样可以大大介绍系统开销，并且不必为每个连接都去创建一个线程，不用去维护多线程。
4）避免多线程之间上下文之间切换带来的开销
```