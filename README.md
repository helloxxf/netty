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

## 8、NIO与零拷贝

```
1）零拷贝是网络编程的关键，很多优化都离不开他 
2）在java程序中，常用的零拷贝有 mmap(内存映射）和sendfile。
3）mmap: 通过内存映射，将文件映射到内核缓冲区，同时用户空间可以共享内核的空间数据。这样在网络传输时
        ，就可以减少内核空间到用户控件的拷贝次数。
4）sendFile: Linux2.1版本提供了sendFile函数，原理是：
            数据不经过用户态，直接从内核缓冲区进入到Socket Buffer，同时由于和用户态完全无关，就减少一次上下文切换
5）优势：更少的数据复制，带来其他的性能优势。例如 上下文切换，更少的cpu缓存伪共享以及无cpu校验和计算。
注意：零拷贝不是没有copy，是从操作系统角度看的，是没有cpu拷贝
```

## 8、mmap和sendfile区别

```
1）mmap适合小数据量的读写，sendFile适合大文件传输。
2）mmap需要4次上下文切换，3次数据拷贝；sendFile需要3次山下文切换，最少两次数据拷贝。
3）sendFile可以利用DMA方式，减少cpu拷贝，mmap则不能（必须从内核拷贝到socket缓冲区）
```

## 9、AIO

```
1）在进行I/0 编程中,常用到两种模式：Reactor 和 Proactor。 Java 的NIO 就是Rector,当有事件触发时，服务器端得到通知，进行相应处理
2）AIO 即NIO2.0,叫做异步不阻塞IO。AIO引入异步通道概念，采用Proactor模式，简化了程序编写，有效的请求才启动线程，它的特点是先由操作系统
    完成后才通知服务端程序启动线程去处理，一般适用于连接数较多且连接时间较长的应用。
3）目前AIO还没有广泛的得到应用，Netty是基于NIO,而不是AIO。
```

## 10、原生NIO存在问题
```
1）NIO类库和API复杂，使用麻烦：需要熟练掌握Selector、ServerSocketChannel、SocketChannel、ByteBuffer;
2)需要熟悉多线程，NIO涉及到Reactor模式，需要多线程和网络编程很熟悉才能写出高质量的NIO.
3)开发工作量和难度都很大：例如客户端面临断联重连，网络闪断、半包读写、失败缓存、网络拥塞异常流大处理等。
4）JDK NIO的BUG:例如Epoll Bug,会导致Selector空轮询，最终导致CPU 100%.
```
## 11、Reactor核心组成
```
1）Reactor: Reactor在一个单独的线程中运行，负责监听和分发事件，分发给适当的处理程序来对IO事件作出相应
2）Handlers：处理程序执行I/O事件要完成的实际事件,类似于客户想要与之交谈的公司中的事件人员。Reactor 通过调度
            适当的处理来响应I/O事件，处理程序执行非阻塞操作。
```
## 12、Reactor分类
```
1）单Reactor 单线程
2）单Reactor 多线程
3）主从Reactor 多线程
```
## 12、Netty模型
```
简易版：Netty主要基于主从Reactors 多线程模型做了一定改进，其中主从Reactor多线程模型有多个Reactor。
工作原理： 1)Netty抽象出两组线程池BossGroup专门负责接收客户端的连接，WorkerGroup朱门负责网络的读写。
          2)BossGroup和WorkGroup类型都是NIoEventLoopGroup
          3)NIoEventLoopGroup相当于一个事件循环组，这个组中包含有多个事件循环，每一个事件循环是NioEventLoop
          4)NIOEventLoop表示一个不断循环的执行处理任务的线程。每个NIOEventLoop都有一个Selector,用于监听绑定在其上的socket的网络通讯。
          5)NIoEventLoopGroup可以有多个线程，既可以含有多个NIOEventLoop。
          6)每个Boss NIoEventLoop 循环执行步骤：
            1、轮询accept事件
            2、处理accept事件，与client建立连接，生成NIOSocketChannel,并将其注册到worker NIOEventLoop的selector
            3、处理任务队列的任务，即runAllTasks
          7)每个Worker NIOEventLoop 循环执行步骤
            1、轮询read,write事件
            2、处理I/O事件，即read/write,在对应的NIOSocketChannel进行处理
            3、处理任务队列的任务，即runAllTasks
          8)每个Worker NIOEventLoop 处理业务时，会使用pipeline(管道），pipeline中包含了channel，即通过pipeline可以通过
            获取对应的管道；管道中维护了很多处理器
```