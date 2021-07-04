#对于不同 GC和堆内存的总结  
## 运行环境
 本人环境  
 jdk8  
 Windows 11系统  
## 运行指令
java -XX:+{} -Xms{} -Xmx{} -XX:+PrintGCDetails GCLogAnalysis
## 不同GC算法对比
### 串行GC-UseSerialGC 
#### 128m堆内存时
java -XX:+UseSerialGC -Xms128m -Xmx128m -XX:+PrintGCDetails GCLogAnalysis  
导致OOM，young gc一次3-5毫秒左右，full gc一次1-13毫秒左右。  
发现有几次full gc只有1-4毫秒，GC后年轻代大小无变化，可能跟代码中对象创建的分配速率有关，也可能是生成的对象太大，老年代也放不下了，而旧的也没有被回收掉，所以保持原大小了。  
第六行的GC日志中，GC过后年轻代的对象大小还多了，应该是分配速率大于回收速率了  
2021-07-04T22:29:56.577+0800: [Full GC (Allocation Failure) 2021-07-04T22:29:56.577+0800: [Tenured: 87307K->87307K(87424K), 0.0014715 secs] 126407K->123810K(126720K), [Metaspace: 2654K->2654K(1056768K)], 0.0016715 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]  
2021-07-04T22:29:56.579+0800: [Full GC (Allocation Failure) 2021-07-04T22:29:56.579+0800: [Tenured: 87379K->87379K(87424K), 0.0033647 secs] 126626K->124917K(126720K), [Metaspace: 2654K->2654K(1056768K)], 0.0034858 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]  
2021-07-04T22:29:56.583+0800: [Full GC (Allocation Failure) 2021-07-04T22:29:56.583+0800: [Tenured: 87379K->87156K(87424K), 0.0118883 secs] 126563K->123077K(126720K), [Metaspace: 2654K->2654K(1056768K)], 0.0125164 secs] [Times: user=0.02 sys=0.00, real=0.01 secs]  
2021-07-04T22:29:56.597+0800: [Full GC (Allocation Failure) 2021-07-04T22:29:56.598+0800: [Tenured: 87288K->87288K(87424K), 0.0041171 secs] 126580K->125127K(126720K), [Metaspace: 2654K->2654K(1056768K)], 0.0053710 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]  
2021-07-04T22:29:56.603+0800: [Full GC (Allocation Failure) 2021-07-04T22:29:56.606+0800: [Tenured: 87288K->87288K(87424K), 0.0014843 secs] 126448K->126300K(126720K), [Metaspace: 2654K->2654K(1056768K)], 0.0055075 secs] [Times: user=0.00 sys=0.00, real=0.01 secs]  
2021-07-04T22:29:56.609+0800: [Full GC (Allocation Failure) 2021-07-04T22:29:56.609+0800: [Tenured: 87288K->87333K(87424K), 0.0126980 secs] 126300K->125724K(126720K), [Metaspace: 2654K->2654K(1056768K)], 0.0134878 secs] [Times: user=0.02 sys=0.00, real=0.01 secs]  
2021-07-04T22:29:56.623+0800: [Full GC (Allocation Failure) 2021-07-04T22:29:56.623+0800: [Tenured: 87333K->87333K(87424K), 0.0014025 secs] 126487K->125855K(126720K), [Metaspace: 2654K->2654K(1056768K)], 0.0017678 secs] [Times: user=0.00 sys=0.00, real=0.00 secs]  
#### 256m堆内存时
java -XX:+UseSerialGC -Xms256m -Xmx256m -XX:+PrintGCDetails GCLogAnalysis  
导致OOM，young gc一次8-11毫秒，full gc一次1-27毫秒  
#### 512m堆内存时
java -XX:+UseSerialGC -Xms512m -Xmx512m -XX:+PrintGCDetails GCLogAnalysis  
生成13.5-13.8万之间的对象。young gc一次15-19毫秒，full gc一次22-35毫秒，没有出现1毫秒的full gc的情况  
#### 1g堆内存时
java -XX:+UseSerialGC -Xms1g -Xmx1g -XX:+PrintGCDetails GCLogAnalysis  
生成约2w个对象。young gc一次24-35毫秒，没有产生full gc,但是有出现老年代被回收的情况
#### 2g堆内存时
java -XX:+UseSerialGC -Xms2g -Xmx2g -XX:+PrintGCDetails GCLogAnalysis  
生成约1.9w个对象。young gc一次42-58毫秒，没有full gc，老年代也没有被回收
#### 总结
1g的时候创建对象数量最多的，gc时间随堆内存增加越来越长  
当堆内存够大时，生产对象的速率低于垃圾回收速率，没有产生full gc，当堆内存达到2g，老年代都没有被回收了，这个时候分配速率低于垃圾回收速率
堆内存较小时，full gc不够稳定，有的时候full gc过后并没有回收掉垃圾
### 并行GC-UseParallelGC 
#### 128m堆内存时
java -XX:+UseParallelGC -Xms128m -Xmx128m -XX:+PrintGCDetails GCLogAnalysis  
oom，young gc一次1-5毫秒，full gc一次1-11毫秒，不够稳定  
#### 256m堆内存时
java -XX:+UseParallelGC -Xms256m -Xmx256m -XX:+PrintGCDetails GCLogAnalysis  
oom，young gc一次2-5毫秒，full gc一次1-31毫秒，不够稳定 
#### 512m堆内存时
java -XX:+UseParallelGC -Xms512m -Xmx512m -XX:+PrintGCDetails GCLogAnalysis  
生成1w个对象，young gc一次2-7毫秒，full gc一次28-32毫秒
#### 1g堆内存时
java -XX:+UseParallelGC -Xms1g -Xmx1g -XX:+PrintGCDetails GCLogAnalysis    
生成2w个对象，young gc一次7-13毫秒，full gc一次36毫秒，full gc次数明显减少
#### 2g堆内存时
java -XX:+UseParallelGC -Xms2g -Xmx2g -XX:+PrintGCDetails GCLogAnalysis    
生成2.3w个对象，young gc一次12-20毫秒，full gc一次46毫秒，full gc每次运行只发生一次  
#### 总结
创建对象数量随堆内存增大而增多，gc时间随堆内存增加越来越长  
堆内存达到2g，还是存在full gc的情况  
堆内存较小时，full gc不够稳定  
### CMS-UseConcMarkSweepGC  
full gc时间为初始标记到reset结束的时间  
#### 128m堆内存时
java -XX:+UseConcMarkSweepGC -Xms128m -Xmx128m -XX:+PrintGCDetails  -XX:+PrintGCDateStamps GCLogAnalysis  
oom，young gc一次3-8毫秒，full gc频繁，一次50-60毫秒
#### 256m堆内存时
java -XX:+UseConcMarkSweepGC -Xms256m -Xmx256m -XX:+PrintGCDetails  -XX:+PrintGCDateStamps GCLogAnalysis  
oom（有几次执行成功了，生成5k对象）。young gc一次4-12毫秒，full gc频繁，一次90-110毫秒
#### 512m堆内存时
java -XX:+UseConcMarkSweepGC -Xms512m -Xmx512m -XX:+PrintGCDetails  -XX:+PrintGCDateStamps GCLogAnalysis  
生成1.4w个对象，young gc一次8-19毫秒，full gc一次100-110毫秒
#### 1g堆内存时
java -XX:+UseConcMarkSweepGC -Xms1g -Xmx1g -XX:+PrintGCDetails  -XX:+PrintGCDateStamps GCLogAnalysis  
生成2w个对象，young gc一次11-33毫秒，full gc一次230毫秒
#### 2g堆内存时
java -XX:+UseConcMarkSweepGC -Xms2g -Xmx2g -XX:+PrintGCDetails  -XX:+PrintGCDateStamps GCLogAnalysis  
生成1.9w个对象，young gc一次17-52毫秒，full gc没有执行完成，到abortable-preclean阶段程序就执行完毕了
#### 总结
堆内存为1g时生成对象最多  
gc时间随堆内存增加有明显增长，尤其是full gc  
除了初始化标记和最终标记阶段，其他并发阶段都有发生young gc的情况，有的时候会导致本次full gc没执行完直接开始下一次full gc  
堆内存较小时，full gc在预清理或者一些并发阶段就oom了，没有执行完毕,或者直接开始了下一次的full gc阶段  
### G1-UseG1GC 
本次不打印detail了，方便看时间
#### 128m堆内存时
java -XX:+UseG1GC -Xms128m -Xmx128m -XX:+PrintGC  -XX:+PrintGCDateStamps GCLogAnalysis  
oom，young gc一次1-4毫秒，full gc一次11-12毫秒
#### 256m堆内存时
java -XX:+UseG1GC -Xms256m -Xmx256m -XX:+PrintGC  -XX:+PrintGCDateStamps GCLogAnalysis  
oom，young gc一次3-6毫秒，full gc一次40-47毫秒
#### 512m堆内存时
java -XX:+UseG1GC -Xms512m -Xmx512m -XX:+PrintGC  -XX:+PrintGCDateStamps GCLogAnalysis  
生成1.2w个对象，young gc一次2-7毫秒，full gc一次50-60毫秒
#### 1g堆内存时
java -XX:+UseG1GC -Xms1g -Xmx1g -XX:+PrintGC  -XX:+PrintGCDateStamps GCLogAnalysis  
生成1.8w个对象，young gc一次4-8毫秒，full gc一次50-70毫秒
#### 2g堆内存时
java -XX:+UseG1GC -Xms2g -Xmx2g -XX:+PrintGC  -XX:+PrintGCDateStamps GCLogAnalysis  
生成2w个对象，young gc一次6-8毫秒，full gc次数明显减少，一次210-240毫秒
#### 总结
有的时候执行到remark阶段之后clean up就结束了，开始下一次initial mark，没有完成一整个gc过程。有的时候会出现concurrent-cleanup-start/end  
full gc中间会穿插young gc  
生成对象数量随堆内存增大而增大  
## 总结
1.堆内存较小时，full gc存在不稳定的情况  
2.128m到256m会导致oom，大量时间在做gc，堆内存过小时，full gc施展不开手脚，旧的清理不完，新的又来了，所以会oom  
3.奇怪的是，并行gc的full gc时间还是比较短的，甚至优于cms和g1，我觉得并行gc的吞吐量下降的会比较多  
4.不同gc算法的生成对象峰值差不多，生成对象最多就2w了  
5.full gc存在被打断的情况，没有执行完就开始了下一次full gc，应该是分配速率过大，在上次full gc的过程中又触发了full gc的条件  
6.仅凭我分析的这几个数据指标来看，并不能说G1 GC就是最优解，我应该再计算一下gc发生的次数才对   
7.只有CMS GC在堆内存大小为256的时候出现不oom的情况   
8.我在想是不是我算错了gc时间。。  
