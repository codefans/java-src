/*
 * %W% %E%
 *
 * Copyright (c) 2006,2010 Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package java.util.concurrent.locks;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;
import sun.misc.Unsafe;

/**
 * Provides a framework for implementing blocking locks and related
 * synchronizers (semaphores, events, etc) that rely on
 * first-in-first-out (FIFO) wait queues.
 * 提供一个框架，用于实现依赖先进先出 (FIFO) 等待队列的阻塞锁和相关同步器（信号量、事件等）。
 *
 * This class is designed to be a useful basis for most kinds of synchronizers that rely on a
 * single atomic <tt>int</tt> value to represent state.
 * 此类被设计成有一个有用的基础类，用以实现大多数依赖单个原子整型值来表示同步状态的锁。
 *
 * Subclasses must define the protected methods that change this state, and which
 * define what that state means in terms of this object being acquired
 * or released.
 * 子类更改此状态的方法必须定义受保护的，并定义该状态在获取或释放此对象方面的含义。
 *
 * Given these, the other methods in this class carry
 * out all queuing and blocking mechanics. Subclasses can maintain
 * other state fields, but only the atomically updated <tt>int</tt>
 * value manipulated using methods {@link #getState}, {@link
 * #setState} and {@link #compareAndSetState} is tracked with respect
 * to synchronization.
 * 鉴于这些，此类中的其他方法执行所有排队和阻塞机制。 子类可以维护其他状态字段，
 * 但只有使用 getState、setState 和 compareAndSetState 方法操作的原子更新的 int 值才会被同步跟踪。
 *
 * rely on-依赖
 * in terms of-就...而言, 在...方面
 * carry out-执行
 * mechanics-机制
 * maintain-维护
 * manipulated-被操纵
 *
 * <p>Subclasses should be defined as non-public internal helper
 * classes that are used to implement the synchronization properties
 * of their enclosing class.  Class
 * <tt>AbstractQueuedSynchronizer</tt> does not implement any
 * synchronization interface.  Instead it defines methods such as
 * {@link #acquireInterruptibly} that can be invoked as
 * appropriate by concrete locks and related synchronizers to
 * implement their public methods.
 * 子类应该定义为非公共的内部工具类，用于实现封闭类的同步属性
 *  类AbstractQueuedSynchronizer没有实现任何同步接口。
 *  相反，它定义了诸如#acquireInterruptably这类方法, 可以被具体的锁和相关同步器适当的调用，
 *  以实现他们的公共方法。
 *
 * appropriate-合适的、适当地
 * concrete-具体的
 *
 * <p>This class supports either or both a default <em>exclusive</em>
 * mode and a <em>shared</em> mode. When acquired in exclusive mode,
 * attempted acquires by other threads cannot succeed. Shared mode
 * acquires by multiple threads may (but need not) succeed.
 * 此类支持一个或两个默认的独占模式和共享模式。在独占模式下获得时，其他线程尝试获取不能成功。
 * 共享模式多线程获取可能（但不需要）成功。
 *
 * This class does not &quot;understand&quot; these differences except in the
 * mechanical sense that when a shared mode acquire succeeds, the next
 * waiting thread (if one exists) must also determine whether it can
 * acquire as well.
 * 这个类不需要理解这些区别，除了在机器意义上。
 * 当共享模式获取成功时，下一个等待线程（如果存在）还必须确定它是否可以获取。
 *
 * Threads waiting in the different modes share the
 * same FIFO queue. Usually, implementation subclasses support only
 * one of these modes, but both can come into play for example in a
 * {@link ReadWriteLock}. Subclasses that support only exclusive or
 * only shared modes need not define the methods supporting the unused mode.
 * 在不同模式下等待的线程共享相同的 FIFO 队列。
 * 通常，实现子类只支持这些模式之一，但两者都可以发挥作用，例如在{@link ReadWriteLock}这个类中。
 * 仅支持独占或只有共享模式之类，不需要定义支持未使用模式的方法。
 *
 * sense-感觉
 *
 * <p>This class defines a nested {@link ConditionObject} class that
 * can be used as a {@link Condition} implementation by subclasses
 * supporting exclusive mode for which method {@link
 * #isHeldExclusively} reports whether synchronization is exclusively
 * held with respect to the current thread, method {@link #release}
 * invoked with the current {@link #getState} value fully releases
 * this object, and {@link #acquire}, given this saved state value,
 * eventually restores this object to its previous acquired state.
 * 这个类定义了一个嵌套的{@link ConditionObject}类，它可以用作{@link Condition}的一个之类实现
 * 支持独占模式, 方法{@link#isHeldExclusively}可以判断相对与当前线程，同步是否是独占的,
 * 使用当前{@link #getState}方法获取到的值去调用{@link #release}方法, 以完全释放这个对象,
 * 调用{@link #acquire}方法，给定这个保存的状态值，
 * 最终将此对象恢复到其先前获取的状态。
 *
 * No <tt>AbstractQueuedSynchronizer</tt> method otherwise creates such a
 * condition, so if this constraint cannot be met, do not use it.  The
 * behavior of {@link ConditionObject} depends of course on the
 * semantics of its synchronizer implementation.
 *
 * 没有AbstractQueuedSynchronizer方法，否则会创建这样一个条件，因此如果无法满足此约束，请不要使用它。
 * 这ConditionObject的行为当然取决于其同步器实现的语义。
 *
 * constraint-约束
 * semantics-语义
 *
 *
 * <p>This class provides inspection, instrumentation, and monitoring
 * methods for the internal queue, as well as similar methods for
 * condition objects. These can be exported as desired into classes
 * using an <tt>AbstractQueuedSynchronizer</tt> for their
 * synchronization mechanics.
 * 此类提供检查、仪表和监控内部队列的方法，和condition objects相类似的方法。
 * 使用AbstractQueuedSynchronizer类, 可以根据需要将这些使用到他们的同步机制。
 *
 * mechanics-机制
 *
 * <p>Serialization of this class stores only the underlying atomic
 * integer maintaining state, so deserialized objects have empty
 * thread queues. Typical subclasses requiring serializability will
 * define a <tt>readObject</tt> method that restores this to a known
 * initial state upon deserialization.
 * 此类的序列化仅存储底层原子整数维护状态，因此反序列化的对象具有空线程队列。
 * 需要可序列化的典型子类将定义一个 <tt>readObject</tt> 方法，该方法在反序列化时将其恢复到已知的初始状态。
 *
 * atomic-原子
 * maintaining-维护
 * define-定义
 * upon deserialization-在反序列化的时候
 * upon-在...的时候
 *
 * <h3>Usage</h3>
 *
 * <p>To use this class as the basis of a synchronizer, redefine the
 * following methods, as applicable, by inspecting and/or modifying
 * the synchronization state using {@link #getState}, {@link
 * #setState} and/or {@link #compareAndSetState}:
 * 要将此类用作同步器的基础，请根据(具体)情况重新定义以下方法，
 * 使用 {@link #getState}方法、{@link #setState}方法 和/或 {@link #compareAndSetState}方法,
 * 检查和/或修改同步状态：
 *
 * as applicable-根据(具体)情况
 * inspecting-检查
 *
 * <ul>
 * <li> {@link #tryAcquire}
 * <li> {@link #tryRelease}
 * <li> {@link #tryAcquireShared}
 * <li> {@link #tryReleaseShared}
 * <li> {@link #isHeldExclusively}
 *</ul>
 *
 * Each of these methods by default throws {@link
 * UnsupportedOperationException}.  Implementations of these methods
 * must be internally thread-safe, and should in general be short and
 * not block. Defining these methods is the <em>only</em> supported
 * means of using this class. All other methods are declared
 * <tt>final</tt> because they cannot be independently varied.
 * 默认情况下，这些方法中的每一个都会抛出 {@link UnsupportedOperationException}。
 * 这些方法的实现必须是内部线程安全的，并且通常应该是简短的而不是阻塞的。
 * 定义这些方法是<em>唯一</em>支持的使用此类的方法。
 * 所有其他方法都声明为final，因为它们不能独立变化。
 *
 * in general-通常
 *
 *
 * <p>You may also find the inherited methods from {@link
 * AbstractOwnableSynchronizer} useful to keep track of the thread
 * owning an exclusive synchronizer.  You are encouraged to use them
 * -- this enables monitoring and diagnostic tools to assist users in
 * determining which threads hold locks.
 * 您可能还会发现 {@link AbstractOwnableSynchronizer} 的继承方法对于跟踪拥有独占同步器的线程很有用。
 * 鼓励您使用它们
 * 这使得监控和诊断工具能够帮助用户确定哪些线程持有锁。
 *
 * inherited-继承
 * diagnostic-诊断
 * diagnostic tools-诊断工具
 * assist-帮助
 *
 * <p>Even though this class is based on an internal FIFO queue, it
 * does not automatically enforce FIFO acquisition policies.  The core
 * of exclusive synchronization takes the form:
 * 尽管此类基于内部 FIFO 队列，但它不会自动执行 FIFO 采集策略。
 * 核心独占同步的形式为：
 *
 * <pre>
 * Acquire-获取:
 *     while (!tryAcquire(arg)) {
 *        <em>enqueue thread if it is not already queued</em>;
 *        如果线程没在队列中, 则加入到队列中
 *        <em>possibly block current thread</em>;
 *        可能阻塞当前线程
 *     }
 *
 * Release-释放:
 *     if (tryRelease(arg))
 *        <em>unblock the first queued thread</em>;
 *        唤醒队列的第一个线程
 * </pre>
 *
 * (Shared mode is similar but may involve cascading signals.)
 * （共享模式类似，但可能涉及级联信号。）
 *
 * <p>Because checks in acquire are invoked before enqueuing, a newly
 * acquiring thread may <em>barge</em> ahead of others that are
 * blocked and queued.
 * However, you can, if desired, define
 * <tt>tryAcquire</tt> and/or <tt>tryAcquireShared</tt> to disable
 * barging by internally invoking one or more of the inspection
 * methods.
 * In particular, a strict FIFO lock can define
 * <tt>tryAcquire</tt> to immediately return <tt>false</tt> if {@link
 * #getFirstQueuedThread} does not return the current thread.
 * A normally preferable non-strict fair version can immediately return
 * <tt>false</tt> only if {@link #hasQueuedThreads} returns
 * <tt>true</tt> and <tt>getFirstQueuedThread</tt> is not the current
 * thread;
 * or equivalently, that <tt>getFirstQueuedThread</tt> is both
 * non-null and not the current thread.  Further variations are
 * possible.
 * 因为在入队之前，在acquire内的检查会被调用，一个新的获取线程可能会<em>插入</em>在其他阻塞和排队的线程之前。
 * 但是，如果需要，您可以定义tryAcquire()方法和/或 tryAcquireShared()方法
 * 通过内部调用一个或多个检查方法来禁止插入
 * 特别是，严格的 FIFO 锁可以定义tryAcquire()方法立即返回false如果 {@link #getFirstQueuedThread} 不返回当前线程。
 * 一种通常更优的非严格公平版本可以立即返回false仅当 {@link #hasQueuedThreads}返回true, 以及getFirstQueuedThread()返回获取的不是当前的线;
 * 或者等效地，getFirstQueuedThread()方法获取的是非空且不是当前线程。 进一步的变化是可能的。
 *
 * barge-插入
 * if desired-如果需要
 * inspection-检查
 *
 * <p>Throughput and scalability are generally highest for the
 * default barging (also known as <em>greedy</em>,
 * <em>renouncement</em>, and <em>convoy-avoidance</em>) strategy.
 * While this is not guaranteed to be fair or starvation-free, earlier
 * queued threads are allowed to recontend before later queued
 * threads, and each recontention has an unbiased chance to succeed
 * against incoming threads.
 * Also, while acquires do not &quot;spin&quot; in the usual sense, they may perform multiple
 * invocations of <tt>tryAcquire</tt> interspersed with other
 * computations before blocking.
 * This gives most of the benefits of spins when exclusive synchronization is only briefly held,
 * without most of the liabilities when it isn't.
 * If so desired, you can augment this by preceding calls to acquire methods with
 * "fast-path" checks, possibly prechecking {@link #hasContended}
 * and/or {@link #hasQueuedThreads} to only do so if the synchronizer is likely not to be contended.
 * 默认插入的吞吐量和可扩展性通常是最高的（也称为<em>贪婪</em>，<em>放弃</em>和<em>护航</em>）策略。
 * 虽然这不能保证公平或无饥饿，但先排队的线程可以在后面排队的线程之前重新竞争。
 * 并且每次重新竞争的线程，跟进入的线程，都有一个公平的机会去获取成功。
 * 此外，虽然获取不“自旋” 在通常意义上，他们可以在阻塞前执行多个tryAcquire()的调用, 并穿插着其他的计算。
 * 这提供了自旋的大部分好处，当独占的同步仅仅是短暂保持的时候，没有多数职责，当它不是。
 * 如果需要，您可以通过前面调用获取“快速路径”检查方法来增强这一点
 * 可能预先检查 {@link #hasContended}方法和/或 {@link #hasQueuedThreads}方法, 仅在同步器很可能不会被竞争。
 *
 * barging-插入
 * renouncement-放弃
 * starvetion-饥饿
 * starvation-free-无饥饿
 * contend-竞争、抗衡
 * recontend-重新竞争
 * unbiased-公正的
 * an unbiased chance-一个公平的机会
 * spin-旋转
 * briefly-短暂的、简要的
 * liability-职责
 * liabilities
 * If so desired-如果需要
 * desired-想要的
 * augment-增加、增强
 * preceding-前
 *
 *
 * <p>This class provides an efficient and scalable basis for
 * synchronization in part by specializing its range of use to
 * synchronizers that can rely on <tt>int</tt> state, acquire, and
 * release parameters, and an internal FIFO wait queue. When this does
 * not suffice, you can build synchronizers from a lower level using
 * {@link java.util.concurrent.atomic atomic} classes, your own custom
 * {@link java.util.Queue} classes, and {@link LockSupport} blocking support.
 * 这个类提供了一个高效和可扩展的部分同步基础
 * 通过指定基于int状态的范围、获取和释放参数, 和内部 FIFO 等待队列。
 * 当这样做时还不够，您可以使用{@link java.util.concurrent.atomic atomic}类，
 * 您自定义的{@link java.util.Queue}类和 {@link LockSupport}阻塞支持, 来从底层构建同步器
 *
 * efficient-高效的
 * scalable-可扩展
 * rely on-依赖，基于
 * suffice-够了、足够
 *
 * <h3>Usage Examples</h3>
 * 使用示例
 *
 * <p>Here is a non-reentrant mutual exclusion lock class that uses
 * the value zero to represent the unlocked state, and one to
 * represent the locked state. While a non-reentrant lock
 * does not strictly require recording of the current owner
 * thread, this class does so anyway to make usage easier to monitor.
 * It also supports conditions and exposes one of the instrumentation methods:
 * 这里是一个不可重入的互斥锁类，它使用值0代表解锁状态，1代表锁定状态。
 * 而不可重入锁不严格要求记录当前所有者线程，这个类这样做是为了使使用更容易监控。
 * 它还支持条件和暴露检测方法之一：
 *
 * mutual-相互
 * exclusion-排斥
 * strictly-严格
 * exposes-暴露
 * instrumentation-检测
 *
 * <pre>
 * class Mutex implements Lock, java.io.Serializable {
 *
 *   // Our internal helper class
 *   private static class Sync extends AbstractQueuedSynchronizer {
 *     // Report whether in locked state
 *     protected boolean isHeldExclusively() {
 *       return getState() == 1;
 *     }
 *
 *     // Acquire the lock if state is zero
 *     public boolean tryAcquire(int acquires) {
 *       assert acquires == 1; // Otherwise unused
 *       if (compareAndSetState(0, 1)) {
 *         setExclusiveOwnerThread(Thread.currentThread());
 *         return true;
 *       }
 *       return false;
 *     }
 *
 *     // Release the lock by setting state to zero
 *     protected boolean tryRelease(int releases) {
 *       assert releases == 1; // Otherwise unused
 *       if (getState() == 0) throw new IllegalMonitorStateException();
 *       setExclusiveOwnerThread(null);
 *       setState(0);
 *       return true;
 *     }
 *
 *     // Provide a Condition
 *     Condition newCondition() { return new ConditionObject(); }
 *
 *     // Deserialize properly
 *     private void readObject(ObjectInputStream s)
 *         throws IOException, ClassNotFoundException {
 *       s.defaultReadObject();
 *       setState(0); // reset to unlocked state
 *     }
 *   }
 *
 *   // The sync object does all the hard work. We just forward to it.
 *   private final Sync sync = new Sync();
 *
 *   public void lock()                { sync.acquire(1); }
 *   public boolean tryLock()          { return sync.tryAcquire(1); }
 *   public void unlock()              { sync.release(1); }
 *   public Condition newCondition()   { return sync.newCondition(); }
 *   public boolean isLocked()         { return sync.isHeldExclusively(); }
 *   public boolean hasQueuedThreads() { return sync.hasQueuedThreads(); }
 *   public void lockInterruptibly() throws InterruptedException {
 *     sync.acquireInterruptibly(1);
 *   }
 *   public boolean tryLock(long timeout, TimeUnit unit)
 *       throws InterruptedException {
 *     return sync.tryAcquireNanos(1, unit.toNanos(timeout));
 *   }
 * }
 * </pre>
 *
 * <p>Here is a latch class that is like a {@link CountDownLatch}
 * except that it only requires a single <tt>signal</tt> to
 * fire. Because a latch is non-exclusive, it uses the <tt>shared</tt>
 * acquire and release methods.
 * 这是一个类似于 {@link CountDownLatch} 的闩锁类，只是它只需要一个signal（信号）即可触发。
 * 因为闩锁是非独占的，所以它使用shared共享模式获取和释放方法。
 *
 * <pre>
 * class BooleanLatch {
 *
 *   private static class Sync extends AbstractQueuedSynchronizer {
 *     boolean isSignalled() { return getState() != 0; }
 *
 *     protected int tryAcquireShared(int ignore) {
 *       return isSignalled()? 1 : -1;
 *     }
 *
 *     protected boolean tryReleaseShared(int ignore) {
 *       setState(1);
 *       return true;
 *     }
 *   }
 *
 *   private final Sync sync = new Sync();
 *   public boolean isSignalled() { return sync.isSignalled(); }
 *   public void signal()         { sync.releaseShared(1); }
 *   public void await() throws InterruptedException {
 *     sync.acquireSharedInterruptibly(1);
 *   }
 * }
 * </pre>
 *
 * @since 1.5
 * @author Doug Lea
 */

public abstract class AbstractQueuedSynchronizer
        extends AbstractOwnableSynchronizer
        implements java.io.Serializable {

    private static final long serialVersionUID = 7373984972572414691L;

    /**
     * Creates a new <tt>AbstractQueuedSynchronizer</tt> instance
     * with initial synchronization state of zero.
     * 创建一个新的AbstractQueuedSynchronizer对象
     * 将同步状态state初始化为0
     */
    protected AbstractQueuedSynchronizer() { }

    /**
     * Wait queue node class.
     * 等待队列node类
     *
     * <p>The wait queue is a variant of a "CLH" (Craig, Landin, and
     * Hagersten) lock queue. CLH locks are normally used for
     * spinlocks.
     * We instead use them for blocking synchronizers, but
     * use the same basic tactic of holding some of the control
     * information about a thread in the predecessor of its node.
     * A "status" field in each node keeps track of whether a thread
     * should block.
     * A node is signalled when its predecessor releases.
     * Each node of the queue otherwise serves as a specific-notification-style monitor holding a single waiting thread.
     * The status field does NOT control whether threads are granted locks etc though.
     * A thread may try to acquire if it is first in the queue. But being first does not guarantee success;
     * it only gives the right to contend.
     * So the currently released contender thread may need to rewait.
     * 等待队列是“CLH”（Craig、Landin 和 Hagersten）锁定队列的变体。 CLH 锁通常用于自旋锁。
     * 我们改为使用它们来作为阻塞同步器，但是使用相同的基本策略来保存在前驱节点中的线程的控制信息。
     * 每个节点中的一个“状态”字段跟踪线程是否需要阻塞。
     * 一个节点被唤醒，当它的前置节点释放。
     * 除此以外, 队列的每个节点都持有一个单独的等待线程，作为一个特定通知式监视器。
     * 状态字段不控制线程是否被授予锁等。
     * 一个线程可能会尝试获取(锁)，如果它是队列中的第一个。 但成为第一并不能保证成功；
     * 它只给予竞争的权利。
     * 所以当前释放的竞争线程可能需要重新等待。
     *
     * tactic-策略
     * predecessor-前驱
     * otherwise-除此以外
     * contend-抗衡、竞争、争夺
     * currently-当前
     * contender-竞争者
     *
     * <p>To enqueue into a CLH lock, you atomically splice it in as new
     * tail. To dequeue, you just set the head field.
     * 要加入 CLH 锁，您可以原子地将其拼接为新的尾部。 要出列，您只需设置 head 字段。
     * atomically-原子地
     * splice it in-拼接起来
     *
     * <pre>
     *      +------+  prev +-----+       +-----+
     * head |      | <---- |     | <---- |     |  tail
     *      +------+       +-----+       +-----+
     * </pre>
     *
     * <p>Insertion into a CLH queue requires only a single atomic
     * operation on "tail", so there is a simple atomic point of
     * demarcation from unqueued to queued. Similarly, dequeing
     * involves only updating the "head". However, it takes a bit
     * more work for nodes to determine who their successors are,
     * in part to deal with possible cancellation due to timeouts
     * and interrupts.
     * 插入 CLH 队列只需要一个尾结点的原子操作，所以有一个简单的原子点来划分未入队和已入队。
     * 同样，出队只涉及更新“头节点”。 然而，确定谁是继任者需要更多的工作，
     * 部分是为了处理可能因超时和中断而取消的问题。
     *
     * demarcation-划分
     * successor-继任者
     *
     * <p>The "prev" links (not used in original CLH locks), are mainly needed to handle cancellation.
     * If a node is cancelled, its successor is (normally) relinked to a non-cancelled predecessor.
     * For explanation of similar mechanics in the case of spin locks, see the papers by Scott and Scherer at
     * http://www.cs.rochester.edu/u/scott/synchronization/
     * “prev”链接（未在原始 CLH 锁中使用），主要是需要处理取消。
     * 如果一个节点被取消，它的继任者（通常）重新链接到未取消的前任。
     * 关于自旋锁类似机制的解释，请参阅 Scott 和 Scherer 的论文，网址为
     * http://www.cs.rochester.edu/u/scott/synchronization/
     *
     * explanation-解释
     * similar mechanics-类似的机制
     * in the case of-关于
     *
     * <p>We also use "next" links to implement blocking mechanics.
     *
     * The thread id for each node is kept in its own node, so a
     * predecessor signals the next node to wake up by traversing
     * next link to determine which thread it is.
     *
     * Determination of successor must avoid races with newly queued nodes to set the "next" fields of their predecessors.
     *
     * This is solved when necessary by checking backwards from the atomically updated "tail" when a node's successor appears to be null.
     * (Or, said differently, the next-links are an optimization so that we don't usually need a backward scan.)
     *
     * 我们还使用“next”链接来实现阻塞机制。
     * 每个节点的线程 id 都保存在它自己的节点中，所以一个前继节点通过遍历下一个链接，来唤醒下一个节点，并确定它是哪个线程。
     * 确定后继者必须避免与新排队的节点竞争以设置他们前节点的“next”字段。
     * 这就解决了，当一个节点的后继节点为null，原子更新tail节点时必要的向后检查.
     * （或者，换句话说，next链接是一个优化，这样我们通常不需要向后扫描。）
     *
     * race-比赛、竞争
     *
     *
     * <p>Cancellation introduces some conservatism to the basic algorithms.
     *
     * Since we must poll for cancellation of other nodes, we can miss noticing whether a cancelled node is ahead or behind us.
     *
     * This is dealt with by always unparking successors upon cancellation, allowing them to stabilize on a new predecessor.
     *
     * 取消为基本算法引入了一些保守性。
     *
     * 由于我们必须轮询其他节点的取消，因此我们可能无法注意到被取消的节点是在我们前面还是在我们后面。
     *
     * 这是通过在取消时总是解除后继者来处理的，使他们能够稳定在新的前任者上。
     *
     * introduce-介绍、引入
     * conservatism-保守主义
     * stabilize-稳定
     *
     * <p>CLH queues need a dummy header node to get started.
     * But we don't create them on construction, because it would be wasted effort if there is never contention.
     * Instead, the node is constructed and head and tail pointers are set upon first contention.
     * CLH 队列需要一个虚拟头节点来启动。
     * 但是我们不会在构建时创建它们，因为如果从不存在争用，那将是浪费精力。
     * 相反，在第一次争用时构造节点并设置头指针和尾指针。
     *
     * dummy-假的、虚拟的
     * construction-建造
     * effort-努力
     * contention-竞争
     *
     * <p>Threads waiting on Conditions use the same nodes, but use an additional link.
     * Conditions only need to link nodes in simple (non-concurrent) linked queues because they are only accessed when exclusively held.
     * Upon await, a node is inserted into a condition queue.
     * Upon signal, the node is transferred to the main queue.
     * A special value of status field is used to mark which queue a node is on.
     *
     * 在Conditions上等待的线程使用相同的节点，但使用额外的链接。
     * Conditions只需要链接简单（非并发）链接队列中的节点，因为它们仅在独占时才被访问。
     * 在等待时，一个节点被插入到条件队列中。
     * 在唤醒时，节点被转移到主队列。
     * status 字段的特殊值用于标记节点所在的队列。
     *
     * <p>Thanks go to Dave Dice, Mark Moir, Victor Luchangco, Bill Scherer and Michael Scott,
     * along with members of JSR-166 expert group, for helpful ideas, discussions, and critiques on the design of this class.
     *
     * <p>感谢 Dave Dice、Mark Moir、Victor Luchangco、Bill Scherer 和 Michael Scott
     * 以及 JSR-166 专家组的成员，感谢他们对这个类的设计提出有益的想法、讨论和批评。
     *
     */
    static final class Node {
        /** waitStatus value to indicate thread has cancelled */
        /** waitStatus的值, 表明线程被取消了 */
        static final int CANCELLED =  1;
        /** waitStatus value to indicate successor's thread needs unparking */
        /** waitStatus的值, 表明后继线程需要被唤醒 */
        static final int SIGNAL    = -1;
        /** waitStatus value to indicate thread is waiting on condition */
        /** waitStatus的值, 表明线程在condition上等待 */
        static final int CONDITION = -2;
        /**
         * waitStatus value to indicate the next acquireShared should
         * unconditionally propagate
         * waitStatus的值, 表明下一个acquireShared应该无条件传播
         * propagate-传播
         */
        static final int PROPAGATE = -3;
        /** Marker to indicate a node is waiting in shared mode */
        /** 表明节点正在以共享模式等待 */
        static final Node SHARED = new Node();
        /** Marker to indicate a node is waiting in exclusive mode */
        /** 表明节点正在以独占模式等待
         * marker-标记
         * */
        static final Node EXCLUSIVE = null;

        /**
         * Status field, taking on only the values:
         * 状态字段，只能取这些值:
         *
         *   SIGNAL:     The successor of this node is (or will soon be) blocked (via park),
         *               so the current node must unpark its successor when it releases or cancels.
         *               To avoid races, acquire methods must first indicate they need a signal,
         *               then retry the atomic acquire, and then, on failure, block.
         *               该节点的后继节点被（或即将）阻塞（通过park），
         *               因此当前节点在释放或取消时必须唤醒其后继节点。
         *               为了避免竞争，获取方法必须首先表明它们需要一个信号，
         *               然后重试原子获取，然后在失败时阻塞。
         *
         *   CANCELLED:  This node is cancelled due to timeout or interrupt.
         *               Nodes never leave this state. In particular,a thread with cancelled node never again blocks.
         *               由于超时或中断，此节点被取消。
         *               节点永远不会离开此状态。 特别是，取消节点的线程永远不会再次阻塞。
         *
         *   CONDITION:  This node is currently on a condition queue.
         *               It will not be used as a sync queue node until transferred, at which time the status will be set to 0.
         *               (Use of this value here has nothing to do with the other uses of the field, but simplifies mechanics.)
         *               该节点当前在条件队列中。
         *               它在传输之前不会用作同步队列节点，此时状态将设置为 0。
         *              （此处使用此值与该字段的其他用途无关，但简化了机制。）
         *
         *   PROPAGATE:  A releaseShared should be propagated to other nodes.
         *               This is set (for head node only) in doReleaseShared to ensure propagation continues,
         *               even if other operations have since intervened.
         *               releaseShared 应该传递到其他节点。
         *               这在 doReleaseShared 中设置（仅适用于头节点）以确保传播继续，
         *               即使此后其他行动进行了干预。
         *
         *   0:          None of the above
         *
         * The values are arranged numerically to simplify use.
         * Non-negative values mean that a node doesn't need to signal.
         * So, most code doesn't need to check for particular values, just for sign.
         * 这些值按数字排列以简化使用。
         * 非负值意味着节点不需要唤醒。
         * 因此，大多数代码不需要检查特定值，只需检查符号。
         *
         * The field is initialized to 0 for normal sync nodes, and CONDITION for condition nodes.
         * It is modified only using CAS.
         * 该字段对于普通同步节点初始化为 0，对于条件节点初始化为 CONDITION。
         * 它仅使用 CAS 进行修改。
         */
        volatile int waitStatus;

        /**
         * Link to predecessor node that current node/thread relies on for checking waitStatus.
         * Assigned during enqueing, and nulled out (for sake of GC) only upon dequeuing.
         * Also, upon cancellation of a predecessor, we short-circuit while finding a non-cancelled one, which will always exist because the head node is never cancelled:
         * A node becomes head only as a result of successful acquire.
         * A cancelled thread never succeeds in acquiring, and a thread only cancels itself, not any other node.
         * 链接到当前节点/线程依赖于检查 waitStatus 的前驱节点。
         * 在入队期间分配，并仅在出队时取消（为了 GC）。
         * 此外，在取消前任时，我们在找到一个未取消的时进行短路，因为头节点永远不会被取消，所以它会一直存在：
         * 一个节点只有在获取成功后才成为头节点。
         * 一个被取消的线程永远不会成功获取，并且一个线程只会取消自己，而不是任何其他节点。
         *
         * nulled out-取消
         * sake of-为了
         *
         */
        volatile Node prev;

        /**
         * Link to the successor node that the current node/thread unparks upon release.
         * Assigned once during enqueuing, and nulled out (for sake of GC) when no longer needed.
         * Upon cancellation, we cannot adjust this field, but can notice status and bypass the node if cancelled.
         * The enq operation does not assign next field of a predecessor until after attachment, so seeing a null next field does not necessarily mean that node is at end of queue.
         * However, if a next field appears to be null, we can scan prev's from the tail to double-check.
         * 链接后继节点, 当前节点/线程在释放时被唤醒。
         * 在排队期间分配一次，并在不再需要时取消（为了 GC）。
         * 取消后，我们无法调整此字段，但可以通知状态并在取消时绕过节点。
         * enq 操作直到连接后才分配前驱(节点)的 next 字段，因此看到 null next 字段并不一定意味着该节点位于队列末尾。
         * 但是，如果下一个字段显示为空，我们可以从尾部扫描上一个字段以进行仔细检查。
         *
         */
        volatile Node next;

        /**
         * The thread that enqueued this node.
         * Initialized on construction and nulled out after use.
         * 在这个节点上排队的线程
         * 在构造时初始化，用完后取消
         *
         */
        volatile Thread thread;

        /**
         * Link to next node waiting on condition, or the special value SHARED.
         * Because condition queues are accessed only when holding in exclusive mode, we just need a simple linked queue to hold nodes while they are waiting on conditions.
         * They are then transferred to the queue to re-acquire.
         * And because conditions can only be exclusive, we save a field by using special value to indicate shared mode.
         * 链接到下一个在condition上等待的节点，或特殊值 SHARED。
         * 因为条件队列只有在独占模式下才会被访问，所以我们只需要一个简单的链接队列来保存节点，因为它们正在等待条件。
         * 然后将它们转移到队列以重新获取。
         * 并且因为条件只能是独占的，所以我们通过使用特殊值来表示共享模式来保存字段。
         *
         */
        Node nextWaiter;

        /**
         * Returns true if node is waiting in shared mode
         * 如果节点在共享模式中等待, 则返回true
         */
        final boolean isShared() {
            return nextWaiter == SHARED;
        }

        /**
         * Returns previous node, or throws NullPointerException if
         * null.  Use when predecessor cannot be null.
         * @return the predecessor of this node
         * 返回前一个节点, 或抛出NullPointerException异常, 如果前一个节点为空的话。
         */
        final Node predecessor() throws NullPointerException {
            Node p = prev;
            if (p == null)
                throw new NullPointerException();
            else
                return p;
        }

        Node() {    // Used to establish initial head or SHARED marker
        }

        Node(Thread thread, Node mode) {     // Used by addWaiter
            this.nextWaiter = mode;
            this.thread = thread;
        }

        Node(Thread thread, int waitStatus) { // Used by Condition
            this.waitStatus = waitStatus;
            this.thread = thread;
        }
    }

    /**
     * Head of the wait queue, lazily initialized.
     * Except for initialization, it is modified only via method setHead.
     * Note:
     * If head exists, its waitStatus is guaranteed not to be
     * CANCELLED.
     * 等待队列的头节点, 延迟初始化,
     * 除初始化外，仅通过 setHead 方法进行修改。
     * 注意：
     * 如果 head 存在，则保证它的 waitStatus 不是CANCELLED(取消)。
     */
    private transient volatile Node head;

    /**
     * Tail of the wait queue, lazily initialized.
     * Modified only via method enq to add new wait node.
     * 等待队列的尾结点, 延迟初始化
     * 仅通过enq方法来修改, 添加新的等待节点
     */
    private transient volatile Node tail;

    /**
     * The synchronization state.
     * 同步状态state
     */
    private volatile int state;

    /**
     * Returns the current value of synchronization state.
     * This operation has memory semantics of a <tt>volatile</tt> read.
     * 返回当前的同步状态state的值
     * 这个操作跟一个volatile变量的读操作, 有相同的内存语义
     * @return current state value
     */
    protected final int getState() {
        return state;
    }

    /**
     * Sets the value of synchronization state.
     * 设置同步状态state的值
     * This operation has memory semantics of a <tt>volatile</tt> write.
     * 这个操作跟一个volatile变量的写操作, 有相同的内存语义
     * @param newState the new state value
     */
    protected final void setState(int newState) {
        state = newState;
    }

    /**
     * Atomically sets synchronization state to the given updated value if the current state value equals the expected value.
     * This operation has memory semantics of a <tt>volatile</tt> read and write.
     * 原子地设置同步状态state为给定的update的值, 如果当前state状态的值与expect值相等的话。
     * 这个操作与一个volatile变量的读写操作, 有相同的内存语义
     *
     * @param expect the expected value
     * @param update the new value
     * @return true if successful. False return indicates that the actual
     *         value was not equal to the expected value.
     */
    protected final boolean compareAndSetState(int expect, int update) {
        // See below for intrinsics setup to support this
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }

    // Queuing utilities

    /**
     * The number of nanoseconds for which it is faster to spin rather than to use timed park.
     * A rough estimate suffices to improve responsiveness with very short timeouts.
     * 纳秒数, 自旋比使用时间睡眠更快。
     * 粗略估计足以在非常短的超时时间内提高响应能力。
     *
     * spin-自旋
     * rough-粗略
     * estimate-估计
     * suffices-足够
     * responsiveness-响应性
     *
     */
    static final long spinForTimeoutThreshold = 1000L;

    /**
     * Inserts node into queue, initializing if necessary. See picture above.
     * 将节点插入队列，必要时进行初始化。 见上图.
     *
     * @param node the node to insert
     * @return node's predecessor
     */
    private Node enq(final Node node) {
        for (;;) {
            Node t = tail;
            if (t == null) { // Must initialize
                Node h = new Node(); // Dummy header
                h.next = node;
                node.prev = h;
                if (compareAndSetHead(h)) {
                    tail = node;
                    return h;
                }
            }
            else {
                node.prev = t;
                if (compareAndSetTail(t, node)) {
                    t.next = node;
                    return t;
                }
            }
        }
    }

    /**
     * Creates and enqueues node for given thread and mode.
     * 为当前线程以给定的模式创建节点, 并将该节点加入到等待队列中。
     *
     * @param current the thread
     * @param mode Node.EXCLUSIVE for exclusive, Node.SHARED for shared
     * @return the new node
     */
    private Node addWaiter(Node mode) {
        Node node = new Node(Thread.currentThread(), mode);
        // Try the fast path of enq; backup to full enq on failure
        Node pred = tail;
        if (pred != null) {
            node.prev = pred;
            if (compareAndSetTail(pred, node)) {
                pred.next = node;
                return node;
            }
        }
        enq(node);
        return node;
    }

    /**
     * Sets head of queue to be node, thus dequeuing.
     * Called only by acquire methods.
     * Also nulls out unused fields for sake of GC and to suppress unnecessary signals and traversals.
     * 将节点设置为队列头，从而出队。
     * 仅由acquire方法调用。
     * 为了 GC 和抑制不必要的唤醒和遍历，还清空了未使用的字段。
     *
     * suppress-抑制
     *
     * @param node the node
     */
    private void setHead(Node node) {
        head = node;
        node.thread = null;
        node.prev = null;
    }

    /**
     * Wakes up node's successor, if one exists.
     * 唤醒当前节点的前置节点, 如果存在的话。
     *
     * @param node the node
     */
    private void unparkSuccessor(Node node) {
        /*
         * If status is negative (i.e., possibly needing signal) try to clear in anticipation of signalling.
         * It is OK if this fails or if status is changed by waiting thread.
         * 如果状态为负（即可能需要唤醒），请尝试清除以期待唤醒。
         * 如果此操作失败或等待线程更改了status状态值，则可以。
         */
        int ws = node.waitStatus;
        if (ws < 0)
            compareAndSetWaitStatus(node, ws, 0);

        /*
         * Thread to unpark is held in successor, which is normally just the next node.
         * But if cancelled or apparently null, traverse backwards from tail to find the actual non-cancelled successor.
         * 需要唤醒的线程保留在后继节点中，通常只是下一个节点。
         * 但如果被取消或明显为空，则从尾部向后遍历以找到实际的未取消后继者。
         */
        Node s = node.next;
        if (s == null || s.waitStatus > 0) {
            s = null;
            for (Node t = tail; t != null && t != node; t = t.prev)
                if (t.waitStatus <= 0)
                    s = t;
        }
        if (s != null)
            LockSupport.unpark(s.thread);
    }

    /**
     * Release action for shared mode -- signal successor and ensure propagation.
     * (Note: For exclusive mode, release just amounts to calling unparkSuccessor of head if it needs signal.)
     * 共享模式的释放动作——唤醒后继者并确保传播。
     * （注意：对于独占模式，如果需要唤醒，释放就相当于调用头部的 unparkSuccessor。）
     */
    private void doReleaseShared() {
        /*
         * Ensure that a release propagates, even if there are other in-progress acquires/releases.
         * This proceeds in the usual way of trying to unparkSuccessor of head if it needs signal.
         * But if it does not, status is set to PROPAGATE to ensure that upon release, propagation continues.
         * Additionally, we must loop in case a new node is added while we are doing this.
         * Also, unlike other uses of unparkSuccessor, we need to know if CAS to reset status fails, if so rechecking.
         * 确保发布传播，即使有其他正在进行的获取/发布。
         * 如果需要唤醒，这会以通常的方式尝试 unparkSuccessor 头部。
         * 但如果不是，则状态设置为 PROPAGATE 以确保在发布时继续传播。
         * 此外，我们必须循环以防在我们这样做时添加了新节点。
         * 此外，与 unparkSuccessor 的其他用途不同，我们需要知道 CAS 重置状态是否失败，如果失败则重新检查。
         *
         */
        for (;;) {
            Node h = head;
            if (h != null && h != tail) {
                int ws = h.waitStatus;
                if (ws == Node.SIGNAL) {
                    if (!compareAndSetWaitStatus(h, Node.SIGNAL, 0))
                        continue; // loop to recheck cases
                    unparkSuccessor(h);
                }
                else if (ws == 0 &&
                        !compareAndSetWaitStatus(h, 0, Node.PROPAGATE))
                    continue; // loop on failed CAS
            }
            if (h == head) // loop if head changed
                break;
        }
    }

    /**
     * in shared mode, if so propagating if either propagate > 0 or
     * PROPAGATE status was set.
     * 在共享模式,如果是传播，如果传播 > 0 或传播状态被设置。
     * @param node the node
     * @param propagate the return value from a tryAcquireShared
     */
    private void setHeadAndPropagate(Node node, int propagate) {
        Node h = head; // Record old head for check below
        setHead(node);
        /*
         * Try to signal next queued node if:
         * Propagation was indicated by caller,or was recorded (as h.waitStatus) by a previous operation
         * (note: this uses sign-check of waitStatus because PROPAGATE status may transition to SIGNAL.)
         * and The next node is waiting in shared mode, or we don't know, because it appears null
         * The conservatism in both of these checks may cause unnecessary wake-ups, but only when there are multiple racing acquires/releases, so most need signals now or soon anyway.
         * 如果出现以下情况，请尝试唤醒下一个排队节点：
         * 传播由调用者指示，或由先前的操作记录（如 h.waitStatus）
         * （注意：这使用 waitStatus 的符号检查，因为 PROPAGATE 状态可能会转换为 SIGNAL。）
         * 而下一个节点在共享模式下等待，或者我们不知道，因为它看起来为空
         *
         * 这两项检查中的保守性可能会导致不必要的唤醒，但仅当有多个竞争获取/释放时，因此大多数现在或很快就需要信号。
         *
         */
        if (propagate > 0 || h == null || h.waitStatus < 0) {
            Node s = node.next;
            if (s == null || s.isShared())
                doReleaseShared();
        }
    }

    // Utilities for various versions of acquire
    // 各种版本的acquire工具

    /**
     * Cancels an ongoing attempt to acquire.
     * 取消正在进行的获取尝试
     *
     * @param node the node
     */
    private void cancelAcquire(Node node) {
        // Ignore if node doesn't exist
        if (node == null)
            return;

        node.thread = null;

        // Skip cancelled predecessors
        // 跳过已经取消的前继节点
        Node pred = node.prev;
        while (pred.waitStatus > 0)
            node.prev = pred = pred.prev;

        // predNext is the apparent node to unsplice.
        // predNext明显是要断开的节点.
        // CASes below will fail if not, in which case, we lost race vs another cancel or signal, so no further action is necessary.
        // 如果没有下面的cas操作将会失败，在这种情况下，我们竞争失败与另一个取消和信号，所以进一步的动作就没有必要了
        Node predNext = pred.next;

        /**
         * Can use unconditional write instead of CAS here.
         * After this atomic step, other Nodes can skip past us.
         * Before, we are free of interference from other threads.
         *
         * 这里可以用无条件写代替CAS。
         * 在这个原子步骤之后，其他节点可以跳过我们。
         * 在此之前，我们不受其他线程的干扰。
         *
         * interference-干扰
         *
         */
        node.waitStatus = Node.CANCELLED;

        // If we are the tail, remove ourselves.
        if (node == tail && compareAndSetTail(node, pred)) {
            compareAndSetNext(pred, predNext, null);
        } else {
            // If successor needs signal, try to set pred's next-link
            // so it will get one. Otherwise wake it up to propagate.
            int ws;
            if (pred != head &&
                    ((ws = pred.waitStatus) == Node.SIGNAL ||
                            (ws <= 0 && compareAndSetWaitStatus(pred, ws, Node.SIGNAL))) &&
                    pred.thread != null) {
                Node next = node.next;
                if (next != null && next.waitStatus <= 0)
                    compareAndSetNext(pred, predNext, next);
            } else {
                unparkSuccessor(node);
            }

            node.next = node; // help GC
        }
    }

    /**
     * Checks and updates status for a node that failed to acquire.
     * Returns true if thread should block.
     * This is the main signal control in all acquire loops.
     * Requires that pred == node.prev
     *
     * 检查并更新获取失败节点的状态。
     * 如果线程阻塞，返回true。
     * 这是所有获取回路中的主要信号控制。
     * 要求pred == node.prev
     *
     * @param pred node's predecessor holding status
     * @param node the node
     * @return {@code true} if thread should block
     */
    private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
        int ws = pred.waitStatus;
        if (ws == Node.SIGNAL)
            /*
             * This node has already set status asking a release
             * to signal it, so it can safely park
             */
            return true;
        if (ws > 0) {
            /*
             * Predecessor was cancelled. Skip over predecessors and
             * indicate retry.
             */
            do {
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);
            pred.next = node;
        } else {
            /*
             * waitStatus must be 0 or PROPAGATE. Indicate that we
             * need a signal, but don't park yet. Caller will need to
             * retry to make sure it cannot acquire before parking.
             */
            compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
        }
        return false;
    }

    /**
     * Convenience method to interrupt current thread.
     * 方便的方法中断当前线程。
     *
     */
    private static void selfInterrupt() {
        Thread.currentThread().interrupt();
    }

    /**
     * Convenience method to park and then check if interrupted
     * 方便方法去阻塞线程，然后检查是否中断
     *
     * @return {@code true} if interrupted
     */
    private final boolean parkAndCheckInterrupt() {
        LockSupport.park(this);
        return Thread.interrupted();
    }

    /*
     * Various flavors of acquire, varying in exclusive/shared and control modes.
     * Each is mostly the same, but annoyingly different.
     * Only a little bit of factoring is possible due to interactions of exception mechanics (including ensuring that we cancel if tryAcquire throws exception) and other control, at least not without hurting performance too much.
     *
     * 不同口味的获取，不同的独占/共享和控制模式。
     * 每一个都差不多，但又有令人讨厌的不同。
     * 由于异常机制(包括确保我们在tryAcquire抛出异常时取消)和其他控制的相互作用，只有少量的分解是可能的，至少不会对性能造成太大的伤害。
     *
     * flavors-口味
     * varying-不同
     * annoyingly-烦人
     * factoring
     * interactions-相互作用
     *
     */

    /**
     * Acquires in exclusive uninterruptible mode for thread already in
     * queue. Used by condition wait methods as well as acquire.
     * 为已经在队列里的线程，以独占不间断的方式获取锁。
     * 被Condition.wait()方法和acquire()方法使用
     *
     * @param node the node
     * @param arg the acquire argument
     * @return {@code true} if interrupted while waiting
     */
    final boolean acquireQueued(final Node node, int arg) {
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    return interrupted;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                    interrupted = true;
            }
        } catch (RuntimeException ex) {
            cancelAcquire(node);
            throw ex;
        }
    }

    /**
     * Acquires in exclusive interruptible mode.
     * 在独占可中断模式下获取
     *
     * @param arg the acquire argument
     */
    private void doAcquireInterruptibly(int arg)
            throws InterruptedException {
        final Node node = addWaiter(Node.EXCLUSIVE);
        try {
            for (;;) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    return;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                    break;
            }
        } catch (RuntimeException ex) {
            cancelAcquire(node);
            throw ex;
        }
        // Arrive here only if interrupted
        cancelAcquire(node);
        throw new InterruptedException();
    }

    /**
     * Acquires in exclusive timed mode.
     * 在独占时间模式下获取
     *
     * @param arg the acquire argument
     * @param nanosTimeout max wait time
     *                     最大等待时间
     * @return {@code true} if acquired
     */
    private boolean doAcquireNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        long lastTime = System.nanoTime();
        final Node node = addWaiter(Node.EXCLUSIVE);
        try {
            for (;;) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    return true;
                }
                if (nanosTimeout <= 0) {
                    cancelAcquire(node);
                    return false;
                }
                if (nanosTimeout > spinForTimeoutThreshold &&
                        shouldParkAfterFailedAcquire(p, node))
                    LockSupport.parkNanos(this, nanosTimeout);
                long now = System.nanoTime();
                nanosTimeout -= now - lastTime;
                lastTime = now;
                if (Thread.interrupted())
                    break;
            }
        } catch (RuntimeException ex) {
            cancelAcquire(node);
            throw ex;
        }
        // Arrive here only if interrupted
        cancelAcquire(node);
        throw new InterruptedException();
    }

    /**
     * Acquires in shared uninterruptible mode.
     * 在共享不可中断模式下获取
     *
     * @param arg the acquire argument
     */
    private void doAcquireShared(int arg) {
        final Node node = addWaiter(Node.SHARED);
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();
                if (p == head) {
                    int r = tryAcquireShared(arg);
                    if (r >= 0) {
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        if (interrupted)
                            selfInterrupt();
                        return;
                    }
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                    interrupted = true;
            }
        } catch (RuntimeException ex) {
            cancelAcquire(node);
            throw ex;
        }
    }

    /**
     * Acquires in shared interruptible mode.
     * 在共享可中断模式下获取
     *
     * @param arg the acquire argument
     */
    private void doAcquireSharedInterruptibly(int arg)
            throws InterruptedException {
        final Node node = addWaiter(Node.SHARED);
        try {
            for (;;) {
                final Node p = node.predecessor();
                if (p == head) {
                    int r = tryAcquireShared(arg);
                    if (r >= 0) {
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        return;
                    }
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                        parkAndCheckInterrupt())
                    break;
            }
        } catch (RuntimeException ex) {
            cancelAcquire(node);
            throw ex;
        }
        // Arrive here only if interrupted
        cancelAcquire(node);
        throw new InterruptedException();
    }

    /**
     * Acquires in shared timed mode.
     * 在共享时间模式下获取
     *
     * @param arg the acquire argument
     * @param nanosTimeout max wait time
     * @return {@code true} if acquired
     */
    private boolean doAcquireSharedNanos(int arg, long nanosTimeout)
            throws InterruptedException {

        long lastTime = System.nanoTime();
        final Node node = addWaiter(Node.SHARED);
        try {
            for (;;) {
                final Node p = node.predecessor();
                if (p == head) {
                    int r = tryAcquireShared(arg);
                    if (r >= 0) {
                        setHeadAndPropagate(node, r);
                        p.next = null; // help GC
                        return true;
                    }
                }
                if (nanosTimeout <= 0) {
                    cancelAcquire(node);
                    return false;
                }
                if (nanosTimeout > spinForTimeoutThreshold &&
                        shouldParkAfterFailedAcquire(p, node))
                    LockSupport.parkNanos(this, nanosTimeout);
                long now = System.nanoTime();
                nanosTimeout -= now - lastTime;
                lastTime = now;
                if (Thread.interrupted())
                    break;
            }
        } catch (RuntimeException ex) {
            cancelAcquire(node);
            throw ex;
        }
        // Arrive here only if interrupted
        cancelAcquire(node);
        throw new InterruptedException();
    }

    // Main exported methods
    // 主要对外暴露的方法

    /**
     * Attempts to acquire in exclusive mode.
     * This method should query if the state of the object permits it to be acquired in the exclusive mode, and if so to acquire it.
     * 尝试以独占的方式获得。
     * 该方法应该查询对象的状态是否允许以独占模式获取它，如果允许，则获取它。
     *
     * This method is always invoked by the thread performing acquire.
     * If this method reports failure, the acquire method may queue the thread, if it is not already queued, until it is signalled by a release from some other thread.
     * This can be used to implement method {@link Lock#tryLock()}.
     * 执行acquire的线程总是调用此方法。
     * 如果该方法调用失败，则acquire方法可能会将尚未排队的线程放入队列，直到其他线程释放为止。
     * 这可以用来实现方法{@link Lock#tryLock()}。
     *
     * <p>The default implementation throws {@link UnsupportedOperationException}.
     *
     * @param arg the acquire argument. This value is always the one
     *        passed to an acquire method, or is the value saved on entry
     *        to a condition wait.  The value is otherwise uninterpreted
     *        and can represent anything you like.
     * @return {@code true} if successful. Upon success, this object has
     *         been acquired.
     * @throws IllegalMonitorStateException if acquiring would place this
     *         synchronizer in an illegal state. This exception must be
     *         thrown in a consistent fashion for synchronization to work
     *         correctly.
     * @throws UnsupportedOperationException if exclusive mode is not supported
     */
    protected boolean tryAcquire(int arg) {
        throw new UnsupportedOperationException();
    }

    /**
     * Attempts to set the state to reflect a release in exclusive mode.
     * 在独占模式下，尝试去设置反应释放状态的state
     *
     * <p>This method is always invoked by the thread performing release.
     * 该方法总是被执行release方法的线程调用。
     *
     * <p>The default implementation throws {@link UnsupportedOperationException}.
     *
     * @param arg the release argument. This value is always the one
     *        passed to a release method, or the current state value upon
     *        entry to a condition wait.  The value is otherwise
     *        uninterpreted and can represent anything you like.
     * @return {@code true} if this object is now in a fully released
     *         state, so that any waiting threads may attempt to acquire;
     *         and {@code false} otherwise.
     * @throws IllegalMonitorStateException if releasing would place this
     *         synchronizer in an illegal state. This exception must be
     *         thrown in a consistent fashion for synchronization to work
     *         correctly.
     * @throws UnsupportedOperationException if exclusive mode is not supported
     */
    protected boolean tryRelease(int arg) {
        throw new UnsupportedOperationException();
    }

    /**
     * Attempts to acquire in shared mode.
     * This method should query if the state of the object permits it to be acquired in the shared mode, and if so to acquire it.
     * 尝试在共享模式下获取。
     * 这个方法应该查询对象的状态是否允许在共享模式下获取它，如果允许，则获取它。
     *
     * This method is always invoked by the thread performing acquire.
     * If this method reports failure, the acquire method may queue the thread, if it is not already queued, until it is signalled by a release from some other thread.
     * 执行acquire的线程总是调用此方法。
     * 如果该方法失败，则acquire方法可能会将尚未排队的线程放入队列，直到其他线程释放该线程为止。
     *
     *
     * <p>The default implementation throws {@link
     * UnsupportedOperationException}.
     *
     * @param arg the acquire argument. This value is always the one
     *        passed to an acquire method, or is the value saved on entry
     *        to a condition wait.  The value is otherwise uninterpreted
     *        and can represent anything you like.
     * @return a negative value on failure; zero if acquisition in shared
     *         mode succeeded but no subsequent shared-mode acquire can
     *         succeed; and a positive value if acquisition in shared
     *         mode succeeded and subsequent shared-mode acquires might
     *         also succeed, in which case a subsequent waiting thread
     *         must check availability. (Support for three different
     *         return values enables this method to be used in contexts
     *         where acquires only sometimes act exclusively.)  Upon
     *         success, this object has been acquired.
     * @throws IllegalMonitorStateException if acquiring would place this
     *         synchronizer in an illegal state. This exception must be
     *         thrown in a consistent fashion for synchronization to work
     *         correctly.
     * @throws UnsupportedOperationException if shared mode is not supported
     */
    protected int tryAcquireShared(int arg) {
        throw new UnsupportedOperationException();
    }

    /**
     * Attempts to set the state to reflect a release in shared mode.
     * 在共享模式下，尝试去设置反应释放状态的state。
     *
     * <p>This method is always invoked by the thread performing release.
     * 该方法总是被执行release方法的线程调用。
     *
     * <p>The default implementation throws
     * {@link UnsupportedOperationException}.
     *
     * @param arg the release argument. This value is always the one
     *        passed to a release method, or the current state value upon
     *        entry to a condition wait.  The value is otherwise
     *        uninterpreted and can represent anything you like.
     * @return {@code true} if this release of shared mode may permit a
     *         waiting acquire (shared or exclusive) to succeed; and
     *         {@code false} otherwise
     * @throws IllegalMonitorStateException if releasing would place this
     *         synchronizer in an illegal state. This exception must be
     *         thrown in a consistent fashion for synchronization to work
     *         correctly.
     * @throws UnsupportedOperationException if shared mode is not supported
     */
    protected boolean tryReleaseShared(int arg) {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns {@code true} if synchronization is held exclusively with respect to the current (calling) thread.
     * This method is invoked upon each call to a non-waiting {@link ConditionObject} method.
     * (Waiting methods instead invoke {@link #release}.)
     * 如果同步被当前（调用）线程独占的持有，则返回true
     * 这个方法在每次调用非等待的{@link ConditionObject}方法时被调用。
     * (等待方法会调用{@link #release}。)
     *
     * The default implementation throws {@link UnsupportedOperationException}.
     * This method is invoked internally only within {@link ConditionObject} methods, so need not be defined if conditions are not used.
     * 默认实现抛出{@link UnsupportedOperationException}。
     * 这个方法只在{@link ConditionObject}方法内部调用，所以如果不使用条件，就不需要定义这个方法。
     *
     * @return {@code true} if synchronization is held exclusively;
     *         {@code false} otherwise
     * @throws UnsupportedOperationException if conditions are not supported
     */
    protected boolean isHeldExclusively() {
        throw new UnsupportedOperationException();
    }

    /**
     * Acquires in exclusive mode, ignoring interrupts.  Implemented
     * by invoking at least once {@link #tryAcquire},
     * returning on success.  Otherwise the thread is queued, possibly
     * repeatedly blocking and unblocking, invoking {@link
     * #tryAcquire} until success.  This method can be used
     * to implement method {@link Lock#lock}.
     * 以独占模式获取, 忽略中断.
     * 实现至少调用一次, 成功即返回。否则这个线程就会被排队，可能反复的阻塞、解除阻塞、调用tryAcquire方法直到成功。
     * 这个方法可以用来实现Lock.lock()方法。
     *
     * @param arg the acquire argument.  This value is conveyed to
     *        {@link #tryAcquire} but is otherwise uninterpreted and
     *        can represent anything you like.
     *  获取参数. 这个值被传递给tryAcquire方法, 但未中断
     *  可以代表任何你喜欢的东西
     *
     */
    public final void acquire(int arg) {
        if (!tryAcquire(arg) &&
                acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            selfInterrupt();
    }

    /**
     * Acquires in exclusive mode, aborting if interrupted.
     * Implemented by first checking interrupt status, then invoking at least once {@link #tryAcquire}, returning on success.
     * Otherwise the thread is queued, possibly repeatedly blocking and unblocking, invoking {@link #tryAcquire} until success or the thread is interrupted.
     * This method can be used to implement method {@link Lock#lockInterruptibly}.
     * 在独占模式下获取，如果被中断则中止。
     * 首先检查中断状态，然后调用至少一次{@link #tryAcquire}，成功返回。
     * 否则，线程将被排队，可能会重复阻塞和解除阻塞，调用{@link #tryAcquire}，直到成功或线程被中断。
     * 这个方法可以用来实现方法{@link Lock# lockinterruptible}。
     *
     * @param arg the acquire argument.  This value is conveyed to
     *        {@link #tryAcquire} but is otherwise uninterpreted and
     *        can represent anything you like.
     * @throws InterruptedException if the current thread is interrupted
     */
    public final void acquireInterruptibly(int arg) throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        if (!tryAcquire(arg))
            doAcquireInterruptibly(arg);
    }

    /**
     * Attempts to acquire in exclusive mode, aborting if interrupted, and failing if the given timeout elapses.  Implemented by first checking interrupt status, then invoking at least once {@link #tryAcquire}, returning on success.
     * Otherwise, the thread is queued, possibly repeatedly blocking and unblocking, invoking {@link #tryAcquire} until success or the thread is interrupted or the timeout elapses.
     * This method can be used to implement method {@link Lock#tryLock(long, TimeUnit)}.
     * 尝试在独占模式下获取，如果被中断将终止，如果给定的超时时间过了将失败。首先检查中断状态，然后调用至少一次{@link #tryAcquire}，成功返回。
     * 否则，线程将进入队列，可能会反复阻塞和解除阻塞，调用{@link #tryAcquire}，直到成功或线程被中断或超时。
     * 这个方法可以用来实现方法{@link Lock#tryLock(long, TimeUnit)}。
     *
     * @param arg the acquire argument.  This value is conveyed to
     *        {@link #tryAcquire} but is otherwise uninterpreted and
     *        can represent anything you like.
     * @param nanosTimeout the maximum number of nanoseconds to wait
     * @return {@code true} if acquired; {@code false} if timed out
     * @throws InterruptedException if the current thread is interrupted
     */
    public final boolean tryAcquireNanos(int arg, long nanosTimeout) throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        return tryAcquire(arg) ||
                doAcquireNanos(arg, nanosTimeout);
    }

    /**
     * Releases in exclusive mode.
     * Implemented by unblocking one or more threads if {@link #tryRelease} returns true.
     * This method can be used to implement method {@link Lock#unlock}.
     * 以独占模式释放。
     * 如果{@link #tryRelease}返回true，则解除阻塞一个或多个线程。
     * 这个方法可以用来实现方法{@link Lock#unlock}。
     *
     * @param arg the release argument.  This value is conveyed to
     *        {@link #tryRelease} but is otherwise uninterpreted and
     *        can represent anything you like.
     * @return the value returned from {@link #tryRelease}
     */
    public final boolean release(int arg) {
        if (tryRelease(arg)) {
            Node h = head;
            if (h != null && h.waitStatus != 0)
                unparkSuccessor(h);
            return true;
        }
        return false;
    }

    /**
     * Acquires in shared mode, ignoring interrupts.
     * Implemented by first invoking at least once {@link #tryAcquireShared}, returning on success.
     * Otherwise the thread is queued, possibly repeatedly blocking and unblocking, invoking {@link #tryAcquireShared} until success.
     * 在共享模式下获取，忽略中断。
     * 通过首先调用至少一次{@link # tryacquirered}实现，成功时返回。
     * 否则，线程将被排队，可能会重复阻塞和解除阻塞，调用{@link # tryacquirered}直到成功。
     *
     * @param arg the acquire argument.  This value is conveyed to
     *        {@link #tryAcquireShared} but is otherwise uninterpreted
     *        and can represent anything you like.
     */
    public final void acquireShared(int arg) {
        if (tryAcquireShared(arg) < 0)
            doAcquireShared(arg);
    }

    /**
     * Acquires in shared mode, aborting if interrupted.
     * Implemented by first checking interrupt status, then invoking at least once {@link #tryAcquireShared}, returning on success.
     * Otherwise the thread is queued, possibly repeatedly blocking and unblocking, invoking {@link #tryAcquireShared} until success or the thread is interrupted.
     * 在共享模式下获取，如果被中断则中止。
     * 通过首先检查中断状态，然后调用至少一次{@link # tryacquirered}，成功返回。
     * 否则，线程将被排队，可能会重复阻塞和解除阻塞，调用{@link # tryacquirered}，直到成功或线程被中断。
     *
     * @param arg the acquire argument.
     * This value is conveyed to {@link #tryAcquireShared} but is
     * otherwise uninterpreted and can represent anything
     * you like.
     * @throws InterruptedException if the current thread is interrupted
     */
    public final void acquireSharedInterruptibly(int arg) throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        if (tryAcquireShared(arg) < 0)
            doAcquireSharedInterruptibly(arg);
    }

    /**
     * Attempts to acquire in shared mode, aborting if interrupted, and failing if the given timeout elapses.
     * Implemented by first checking interrupt status, then invoking at least once {@link #tryAcquireShared}, returning on success.
     * Otherwise, the thread is queued, possibly repeatedly blocking and unblocking, invoking {@link #tryAcquireShared} until success or the thread is interrupted or the timeout elapses.
     * 尝试在共享模式下获取，如果被中断，则中止;如果给定的超时时间过了，则失败。
     * 通过首先检查中断状态，然后调用至少一次{@link # tryacquirered}，成功返回。
     * 否则，线程将被排队，可能会重复阻塞和解除阻塞，调用{@link # tryacquirered}，直到成功或线程被中断或超时结束。
     *
     * @param arg the acquire argument.  This value is conveyed to
     *        {@link #tryAcquireShared} but is otherwise uninterpreted
     *        and can represent anything you like.
     * @param nanosTimeout the maximum number of nanoseconds to wait
     * @return {@code true} if acquired; {@code false} if timed out
     * @throws InterruptedException if the current thread is interrupted
     */
    public final boolean tryAcquireSharedNanos(int arg, long nanosTimeout) throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        return tryAcquireShared(arg) >= 0 ||
                doAcquireSharedNanos(arg, nanosTimeout);
    }

    /**
     * Releases in shared mode.
     * Implemented by unblocking one or more threads if {@link #tryReleaseShared} returns true.
     * 通过共享模式释放。
     * 如果{@link #tryReleaseShared}返回true，则解除阻塞一个或多个线程。
     *
     * @param arg the release argument.  This value is conveyed to
     *        {@link #tryReleaseShared} but is otherwise uninterpreted
     *        and can represent anything you like.
     * @return the value returned from {@link #tryReleaseShared}
     */
    public final boolean releaseShared(int arg) {
        if (tryReleaseShared(arg)) {
            doReleaseShared();
            return true;
        }
        return false;
    }

    // Queue inspection methods
    // 队列检验方法

    /**
     * Queries whether any threads are waiting to acquire.
     * Note that because cancellations due to interrupts and timeouts may occur at any time, a {@code true} return does not guarantee that any other thread will ever acquire.
     * 查询是否有线程正在等待获取。
     * 注意，由于中断和超时导致的取消可能在任何时候发生，{@code true}返回并不保证任何其他线程将会获得。
     *
     * <p>In this implementation, this operation returns in
     * constant time.
     *
     * @return {@code true} if there may be other threads waiting to acquire
     */
    public final boolean hasQueuedThreads() {
        return head != tail;
    }

    /**
     * Queries whether any threads have ever contended to acquire this synchronizer; that is if an acquire method has ever blocked.
     * 查询是否有线程曾经争用过这个同步器;如果一个获取方法曾经阻塞。
     *
     * In this implementation, this operation returns in constant time.
     * 在这个实现中，该操作以常数时间返回。
     *
     * @return {@code true} if there has ever been contention
     */
    public final boolean hasContended() {
        return head != null;
    }

    /**
     * Returns the first (longest-waiting) thread in the queue, or {@code null} if no threads are currently queued.
     * In this implementation, this operation normally returns in constant time, but may iterate upon contention if other threads are concurrently modifying the queue.
     * 返回队列中的第一个(等待时间最长的)线程，如果当前没有线程在队列中，则返回{@code null}。
     * 在此实现中，该操作通常以常量时间返回，但如果其他线程正在并发地修改队列，则可能在争用时进行迭代。
     *
     * @return the first (longest-waiting) thread in the queue, or
     *         {@code null} if no threads are currently queued
     */
    public final Thread getFirstQueuedThread() {
        // handle only fast path, else relay
        return (head == tail)? null : fullGetFirstQueuedThread();
    }

    /**
     * Version of getFirstQueuedThread called when fastpath fails
     * fastpath失败时调用的getFirstQueuedThread的版本
     */
    private Thread fullGetFirstQueuedThread() {
        /*
         * The first node is normally h.next.
         * Try to get its thread field, ensuring consistent reads: If thread field is nulled out or s.prev is no longer head, then some other thread(s) concurrently performed setHead in between some of our reads.
         * We try this twice before resorting to traversal.
         * 第一个节点通常是h.next。
         * 尝试获取它的线程字段，确保一致的读取:如果线程字段为空，或者s.prev不再是head，那么在我们的一些读取之间，其他线程将并发执行setHead。
         * 在进行遍历之前，我们尝试了两次。
         *
         * traversal-遍历
         *
         */
        Node h, s;
        Thread st;
        if (((h = head) != null && (s = h.next) != null &&
                s.prev == head && (st = s.thread) != null) ||
                ((h = head) != null && (s = h.next) != null &&
                        s.prev == head && (st = s.thread) != null))
            return st;

        /*
         * Head's next field might not have been set yet, or may have been unset after setHead.
         * So we must check to see if tail is actually first node.
         * If not, we continue on, safely traversing from tail back to head to find first, guaranteeing termination.
         *
         * Head的下一个字段可能还没有被设置，或者在setHead之后未被设置。
         * 所以我们必须检查尾部是否是第一个节点。
         * 如果没有，我们继续，从尾部到头部安全穿越，找到第一个，保证终止。
         *
         */

        Node t = tail;
        Thread firstThread = null;
        while (t != null && t != head) {
            Thread tt = t.thread;
            if (tt != null)
                firstThread = tt;
            t = t.prev;
        }
        return firstThread;
    }

    /**
     * Returns true if the given thread is currently queued.
     * 如果给定的线程正在排队，则返回true。
     *
     * This implementation traverses the queue to determine presence of the given thread.
     * 这个实现遍历队列以确定给定线程是否存在。
     *
     * @param thread the thread
     * @return {@code true} if the given thread is on the queue
     * @throws NullPointerException if the thread is null
     */
    public final boolean isQueued(Thread thread) {
        if (thread == null)
            throw new NullPointerException();
        for (Node p = tail; p != null; p = p.prev)
            if (p.thread == thread)
                return true;
        return false;
    }

    /**
     * Return {@code true} if the apparent first queued thread, if one exists, is not waiting in exclusive mode.
     * Used only as a heuristic in ReentrantReadWriteLock.
     * 如果明显的第一个排队的线程(如果存在)没有在排他模式中等待，则返回{@code true}。
     * 仅在ReentrantReadWriteLock中用作启发式。
     *
     * apparent-明显的
     *
     */
    final boolean apparentlyFirstQueuedIsExclusive() {
        Node h, s;
        return ((h = head) != null && (s = h.next) != null &&
                s.nextWaiter != Node.SHARED);
    }

    /**
     * Return {@code true} if the queue is empty or if the given thread is at the head of the queue.
     * This is reliable only if <tt>current</tt> is actually Thread.currentThread() of caller.
     * 如果队列是空的，或者给定的线程位于队列的头部，则返回{@code true}。
     * 只有当<tt>current</tt>实际上是Thread.currentThread()的调用者时才可靠。
     *
     */
    final boolean isFirst(Thread current) {
        Node h, s;
        return ((h = head) == null ||
                ((s = h.next) != null && s.thread == current) ||
                fullIsFirst(current));
    }

    final boolean fullIsFirst(Thread current) {
        // same idea as fullGetFirstQueuedThread
        Node h, s;
        Thread firstThread = null;
        if (((h = head) != null && (s = h.next) != null &&
                s.prev == head && (firstThread = s.thread) != null))
            return firstThread == current;
        Node t = tail;
        while (t != null && t != head) {
            Thread tt = t.thread;
            if (tt != null)
                firstThread = tt;
            t = t.prev;
        }
        return firstThread == current || firstThread == null;
    }


    // Instrumentation and monitoring methods
    // 仪器仪表和监测方法

    /**
     * Returns an estimate of the number of threads waiting to acquire.
     * The value is only an estimate because the number of threads may change dynamically while this method traverses internal data structures.
     * This method is designed for use in monitoring system state, not for synchronization control.
     * 返回等待获取(锁)的线程的预估数量
     * 该值只是一个估计值，因为在此方法遍历内部数据结构时，线程的数量可能会发生动态变化。
     * 该方法是用于监控系统状态，而不是用于同步控制。
     *
     * @return the estimated number of threads waiting to acquire
     * @return 等待获取(锁)的线程的预估数量
     *
     * estimate-估计、估量
     *
     */
    public final int getQueueLength() {
        int n = 0;
        for (Node p = tail; p != null; p = p.prev) {
            if (p.thread != null)
                ++n;
        }
        return n;
    }

    /**
     * Returns a collection containing threads that may be waiting to acquire.
     * Because the actual set of threads may change dynamically while constructing this result, the returned collection is only a best-effort estimate.
     * The elements of the returned collection are in no particular order.
     * This method is designed to facilitate construction of subclasses that provide more extensive monitoring facilities.
     * 返回一个包含可能等待获取的线程的集合。
     * 因为实际的线程集在构造这个结果时可能会动态更改，所以返回的集合只是一个最佳估计。
     * 返回集合中的元素没有特定的顺序。
     * 这种方法旨在促进子类的构建，从而提供更广泛的监控设施。
     *
     * particular-特定的
     * facilitate-促进
     * extensive-广泛的
     * facilities-设施
     *
     * @return the collection of threads
     */
    public final Collection<Thread> getQueuedThreads() {
        ArrayList<Thread> list = new ArrayList<Thread>();
        for (Node p = tail; p != null; p = p.prev) {
            Thread t = p.thread;
            if (t != null)
                list.add(t);
        }
        return list;
    }

    /**
     * Returns a collection containing threads that may be waiting to acquire in exclusive mode.
     * This has the same properties as {@link #getQueuedThreads} except that it only returns those threads waiting due to an exclusive acquire.
     * 返回一个集合，该集合包含可能在独占模式下等待获取的线程。
     * 它与{@link #getQueuedThreads}具有相同的属性，只是它只返回那些由于独占获取而等待的线程。
     *
     * @return the collection of threads
     */
    public final Collection<Thread> getExclusiveQueuedThreads() {
        ArrayList<Thread> list = new ArrayList<Thread>();
        for (Node p = tail; p != null; p = p.prev) {
            if (!p.isShared()) {
                Thread t = p.thread;
                if (t != null)
                    list.add(t);
            }
        }
        return list;
    }

    /**
     * Returns a collection containing threads that may be waiting to acquire in shared mode.
     * This has the same properties as {@link #getQueuedThreads} except that it only returns those threads waiting due to a shared acquire.
     * 返回一个集合，其中包含可能在共享模式下等待获取的线程。
     * 它与{@link #getQueuedThreads}具有相同的属性，只是它只返回那些由于共享获取而等待的线程。
     *
     * @return the collection of threads
     */
    public final Collection<Thread> getSharedQueuedThreads() {
        ArrayList<Thread> list = new ArrayList<Thread>();
        for (Node p = tail; p != null; p = p.prev) {
            if (p.isShared()) {
                Thread t = p.thread;
                if (t != null)
                    list.add(t);
            }
        }
        return list;
    }

    /**
     * Returns a string identifying this synchronizer, as well as its state.
     * The state, in brackets, includes the String {@code "State ="} followed by the current value of {@link #getState}, and either {@code "nonempty"} or {@code "empty"} depending on whether the queue is empty.
     * 返回标识此同步器的字符串及其状态。
     * 括号中的状态包括String {@code " state ="}，后跟{@link #getState}的当前值，以及取决于队列是否为空的{@code "nonempty"}或{@code "empty"}。
     *
     * @return a string identifying this synchronizer, as well as its state
     */
    public String toString() {
        int s = getState();
        String q  = hasQueuedThreads()? "non" : "";
        return super.toString() +
                "[State = " + s + ", " + q + "empty queue]";
    }


    // Internal support methods for Conditions
    // Conditions的内部支撑方法

    /**
     * Returns true if a node, always one that was initially placed on
     * a condition queue, is now waiting to reacquire on sync queue.
     * 如果一个节点(总是最初放置的节点)返回true
     * 一个条件队列，现在正在等待重新获取同步队列。
     *
     * @param node the node
     * @return true if is reacquiring
     */
    final boolean isOnSyncQueue(Node node) {
        if (node.waitStatus == Node.CONDITION || node.prev == null)
            return false;
        if (node.next != null) // If has successor, it must be on queue
            return true;
        /*
         * node.prev can be non-null, but not yet on queue because the CAS to place it on queue can fail.
         * So we have to traverse from tail to make sure it actually made it.
         * It will always be near the tail in calls to this method, and unless the CAS failed (which is unlikely), it will be there, so we hardly ever traverse much.
         * node.prev可以是非空值，但还没有进入队列，因为将其放入队列的CAS可能会失败。
         * 所以我们必须从尾部遍历以确保它真的成功了。
         * 在对这个方法的调用中，它总是在尾部附近，除非CAS失败(这是不可能的)，否则它就会在那里，所以我们很少遍历。
         *
         */
        return findNodeFromTail(node);
    }

    /**
     * Returns true if node is on sync queue by searching backwards from tail.
     * Called only when needed by isOnSyncQueue.
     * 如果节点在同步队列上，则通过从尾部向后搜索返回true。
     * 只在isOnSyncQueue需要时调用。
     *
     * @return true if present
     */
    private boolean findNodeFromTail(Node node) {
        Node t = tail;
        for (;;) {
            if (t == node)
                return true;
            if (t == null)
                return false;
            t = t.prev;
        }
    }

    /**
     * Transfers a node from a condition queue onto sync queue.
     * Returns true if successful.
     * 将节点从条件队列转移到同步队列。
     * 如果成功返回true。
     *
     * @param node the node
     * @return true if successfully transferred (else the node was
     * cancelled before signal).
     */
    final boolean transferForSignal(Node node) {
        /*
         * If cannot change waitStatus, the node has been cancelled.
         */
        if (!compareAndSetWaitStatus(node, Node.CONDITION, 0))
            return false;

        /*
         * Splice onto queue and try to set waitStatus of predecessor to indicate that thread is (probably) waiting.
         * If cancelled or attempt to set waitStatus fails, wake up to resync (in which case the waitStatus can be transiently and harmlessly wrong).
         * 拼接到队列上，并尝试设置前任的waitStatus，以表明线程(可能)正在等待。
         * 如果取消或尝试设置waitStatus失败，则唤醒重新同步(在这种情况下，waitStatus可以是暂时的，并且无害的错误)。
         *
         */
        Node p = enq(node);
        int ws = p.waitStatus;
        if (ws > 0 || !compareAndSetWaitStatus(p, ws, Node.SIGNAL))
            LockSupport.unpark(node.thread);
        return true;
    }

    /**
     * Transfers node, if necessary, to sync queue after a cancelled wait.
     * Returns true if thread was cancelled before being signalled.
     *
     * 如果有必要，在取消等待后，将节点传输到同步队列。
     * 如果在发出信号之前线程已被取消，则返回true。
     *
     * @param current the waiting thread
     * @param node its node
     * @return true if cancelled before the node was signalled.
     */
    final boolean transferAfterCancelledWait(Node node) {
        if (compareAndSetWaitStatus(node, Node.CONDITION, 0)) {
            enq(node);
            return true;
        }
        /*
         * If we lost out to a signal(), then we can't proceed until it finishes its enq().
         * Cancelling during an incomplete transfer is both rare and transient, so just spin.
         * 如果我们丢失了一个信号，那么我们不能继续，直到它完成它的enq()方法。
         * 在不完全转移期间的抵消是罕见的和短暂的，所以只是自旋。
         */
        while (!isOnSyncQueue(node))
            Thread.yield();
        return false;
    }

    /**
     * Invokes release with current state value;
     * returns saved state.
     * Cancels node and throws exception on failure.
     *
     * 使用当前状态值调用release;
     * 返回保存的状态。
     * 取消节点并在失败时抛出异常。
     *
     * @param node the condition node for this wait
     * @return previous sync state
     */
    final int fullyRelease(Node node) {
        try {
            int savedState = getState();
            if (release(savedState))
                return savedState;
        } catch (RuntimeException ex) {
            node.waitStatus = Node.CANCELLED;
            throw ex;
        }
        // reach here if release fails
        node.waitStatus = Node.CANCELLED;
        throw new IllegalMonitorStateException();
    }

    // Instrumentation methods for conditions
    // conditions测量方法

    /**
     * Queries whether the given ConditionObject uses this synchronizer as its lock.
     * 查询给定的条件对象是否使用此同步器作为它的锁。
     *
     * @param condition the condition
     * @return <tt>true</tt> if owned
     * @throws NullPointerException if the condition is null
     */
    public final boolean owns(ConditionObject condition) {
        if (condition == null)
            throw new NullPointerException();
        return condition.isOwnedBy(this);
    }

    /**
     * Queries whether any threads are waiting on the given condition associated with this synchronizer.
     * Note that because timeouts and interrupts may occur at any time, a <tt>true</tt> return does not guarantee that a future <tt>signal</tt> will awaken any threads.
     * This method is designed primarily for use in monitoring of the system state.
     * 查询是否有线程正在等待与此同步器相关联的给定条件。
     * 注意，由于超时和中断可能在任何时候发生，<tt>true</tt>返回并不保证未来的<tt>signal</tt>将唤醒任何线程。
     * 这种方法主要是为监控系统状态而设计的。
     *
     * @param condition the condition
     * @return <tt>true</tt> if there are any waiting threads
     * @throws IllegalMonitorStateException if exclusive synchronization
     *         is not held
     * @throws IllegalArgumentException if the given condition is
     *         not associated with this synchronizer
     * @throws NullPointerException if the condition is null
     */
    public final boolean hasWaiters(ConditionObject condition) {
        if (!owns(condition))
            throw new IllegalArgumentException("Not owner");
        return condition.hasWaiters();
    }

    /**
     * Returns an estimate of the number of threads waiting on the given condition associated with this synchronizer.
     * Note that because timeouts and interrupts may occur at any time, the estimate serves only as an upper bound on the actual number of waiters.
     * This method is designed for use in monitoring of the system state, not for synchronization control.
     * 返回在与此同步器相关联的给定条件下等待的线程数的估计。
     * 请注意，由于超时和中断可能随时发生，因此估计仅作为等待者实际数量的上限。
     * 该方法用于监控系统状态，而不是用于同步控制。
     *
     * estimate-估计
     *
     * @param condition the condition
     * @return the estimated number of waiting threads
     * @throws IllegalMonitorStateException if exclusive synchronization
     *         is not held
     * @throws IllegalArgumentException if the given condition is
     *         not associated with this synchronizer
     * @throws NullPointerException if the condition is null
     */
    public final int getWaitQueueLength(ConditionObject condition) {
        if (!owns(condition))
            throw new IllegalArgumentException("Not owner");
        return condition.getWaitQueueLength();
    }

    /**
     * Returns a collection containing those threads that may be waiting on the given condition associated with this synchronizer.
     * Because the actual set of threads may change dynamically while constructing this result, the returned collection is only a best-effort estimate.
     * The elements of the returned collection are in no particular order.
     * 返回一个集合，其中包含可能正在等待与此同步器关联的给定条件的线程。
     * 因为实际的线程集在构造这个结果时可能会动态更改，所以返回的集合只是一个最佳估计。
     * 返回集合中的元素没有特定的顺序。
     *
     * @param condition the condition
     * @return the collection of threads
     * @throws IllegalMonitorStateException if exclusive synchronization
     *         is not held
     * @throws IllegalArgumentException if the given condition is
     *         not associated with this synchronizer
     * @throws NullPointerException if the condition is null
     */
    public final Collection<Thread> getWaitingThreads(ConditionObject condition) {
        if (!owns(condition))
            throw new IllegalArgumentException("Not owner");
        return condition.getWaitingThreads();
    }

    /**
     * Condition implementation for a {@link AbstractQueuedSynchronizer} serving as the basis of a {@link Lock} implementation.
     * {@link AbstractQueuedSynchronizer}的条件实现作为{@link Lock}实现的基础。
     *
     * <p>Method documentation for this class describes mechanics, not behavioral specifications from the point of view of Lock and Condition users.
     * Exported versions of this class will in general need to be accompanied by documentation describing condition semantics that rely on those of the associated
     * <p>该类的方法文档描述的是机制，而不是Lock和Condition用户的行为规范。
     * 这个类的导出版本通常需要与描述条件语义的文档一起使用，这些语义依赖于相关的
     *
     * <tt>AbstractQueuedSynchronizer</tt>.
     *
     * <p>This class is Serializable, but all fields are transient, so deserialized conditions have no waiters.
     * <p>这个类是可序列化的，但所有字段都是瞬态的，所以反序列化的条件没有等待器。
     *
     *
     */
    public class ConditionObject implements Condition, java.io.Serializable {
        private static final long serialVersionUID = 1173984872572414699L;
        /** First node of condition queue. */
        private transient Node firstWaiter;
        /** Last node of condition queue. */
        private transient Node lastWaiter;

        /**
         * Creates a new <tt>ConditionObject</tt> instance.
         */
        public ConditionObject() { }

        // Internal methods

        /**
         * Adds a new waiter to wait queue.
         * @return its new wait node
         */
        private Node addConditionWaiter() {
            Node t = lastWaiter;
            // If lastWaiter is cancelled, clean out.
            if (t != null && t.waitStatus != Node.CONDITION) {
                unlinkCancelledWaiters();
                t = lastWaiter;
            }
            Node node = new Node(Thread.currentThread(), Node.CONDITION);
            if (t == null)
                firstWaiter = node;
            else
                t.nextWaiter = node;
            lastWaiter = node;
            return node;
        }

        /**
         * Removes and transfers nodes until hit non-cancelled one or null.
         * Split out from signal in part to encourage compilers to inline the case of no waiters.
         * 删除和传输节点，直到达到未取消节点或空节点。
         * 从信号中分离出来，部分是为了鼓励编译器内联没有等待器的情况。
         *
         * @param first (non-null) the first node on condition queue
         */
        private void doSignal(Node first) {
            do {
                if ( (firstWaiter = first.nextWaiter) == null)
                    lastWaiter = null;
                first.nextWaiter = null;
            } while (!transferForSignal(first) &&
                    (first = firstWaiter) != null);
        }

        /**
         * Removes and transfers all nodes.
         *
         * @param first (non-null) the first node on condition queue
         */
        private void doSignalAll(Node first) {
            lastWaiter = firstWaiter  = null;
            do {
                Node next = first.nextWaiter;
                first.nextWaiter = null;
                transferForSignal(first);
                first = next;
            } while (first != null);
        }

        /**
         * Unlinks cancelled waiter nodes from condition queue.
         * Called only while holding lock.
         * This is called when cancellation occurred during condition wait, and upon insertion of a new waiter when lastWaiter is seen to have been cancelled.
         * This method is needed to avoid garbage retention in the absence of signals.
         * So even though it may require a full traversal, it comes into play only when timeouts or cancellations occur in the absence of signals.
         * It traverses all nodes rather than stopping at a particular target to unlink all pointers to garbage nodes without requiring many re-traversals during cancellation storms.
         * 解除条件队列中被取消的等待节点的链接。
         * 仅在持有锁时调用。
         * 当条件等待期间发生取消时，当lastWaiter被取消时插入一个新的waiter时，调用此函数。
         * 这种方法是为了避免在没有信号的情况下保留垃圾。
         * 因此，即使它可能需要一个完整的遍历，它也只在没有信号的情况下发生超时或取消时才起作用。
         * 它遍历所有节点，而不是在特定的目标处停止，解除所有指向垃圾节点的指针的链接，而不需要在取消风暴期间多次重新遍历。
         *
         * retention-保留
         * absence-缺席
         *
         */
        private void unlinkCancelledWaiters() {
            Node t = firstWaiter;
            Node trail = null;
            while (t != null) {
                Node next = t.nextWaiter;
                if (t.waitStatus != Node.CONDITION) {
                    t.nextWaiter = null;
                    if (trail == null)
                        firstWaiter = next;
                    else
                        trail.nextWaiter = next;
                    if (next == null)
                        lastWaiter = trail;
                }
                else
                    trail = t;
                t = next;
            }
        }

        // public methods

        /**
         * Moves the longest-waiting thread, if one exists,
         * from the wait queue for this condition to the wait queue for the owning lock.
         * 将等待时间最长的线程(如果存在)从该条件的等待队列移动到拥有锁的等待队列。
         *
         * @throws IllegalMonitorStateException if {@link #isHeldExclusively}
         *         returns {@code false}
         */
        public final void signal() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            Node first = firstWaiter;
            if (first != null)
                doSignal(first);
        }

        /**
         * Moves all threads from the wait queue for this condition to
         * the wait queue for the owning lock.
         * 将所有线程从该条件的等待队列移动到拥有锁的等待队列。
         *
         * @throws IllegalMonitorStateException if {@link #isHeldExclusively}
         *         returns {@code false}
         */
        public final void signalAll() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            Node first = firstWaiter;
            if (first != null)
                doSignalAll(first);
        }

        /**
         * Implements uninterruptible condition wait.
         * Save lock state returned by {@link #getState} Invoke {@link #release} with saved state as argument, throwing  IllegalMonitorStateException if it fails.
         *
         * Block until signalled Reacquire by invoking specialized version of {@link #acquire} with saved state as argument.
         *
         * 实现不可中断条件等待。
         * 调用{@link #release}以saved state为参数，如果失败则抛出IllegalMonitorStateException。
         *
         * 调用{@link #acquire}的特殊版本，以保存的状态作为参数，直到有信号重新获取。
         *
         */
        public final void awaitUninterruptibly() {
            Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            boolean interrupted = false;
            while (!isOnSyncQueue(node)) {
                LockSupport.park(this);
                if (Thread.interrupted())
                    interrupted = true;
            }
            if (acquireQueued(node, savedState) || interrupted)
                selfInterrupt();
        }

        /*
         * For interruptible waits, we need to track whether to throw InterruptedException, if interrupted while blocked on condition, versus reinterrupt current thread, if interrupted while blocked waiting to re-acquire.
         * 对于可中断的等待，我们需要跟踪是否抛出InterruptedException(如果在阻塞的情况下中断，等待重新获取)，还是重新中断当前线程(如果在阻塞的情况下中断，等待重新获取)。
         *
         */

        /** Mode meaning to reinterrupt on exit from wait */
        private static final int REINTERRUPT =  1;
        /** Mode meaning to throw InterruptedException on exit from wait */
        private static final int THROW_IE    = -1;

        /**
         * Checks for interrupt, returning THROW_IE if interrupted  before signalled, REINTERRUPT if after signalled, or 0 if not interrupted.
         * 检查中断，如果在发出信号之前中断，返回THROW_IE，如果在发出信号之后中断，返回REINTERRUPT，如果没有中断，返回0。
         *
         */
        private int checkInterruptWhileWaiting(Node node) {
            return (Thread.interrupted()) ?
                    ((transferAfterCancelledWait(node))? THROW_IE : REINTERRUPT) :
                    0;
        }

        /**
         * Throws InterruptedException, reinterrupts current thread, or does nothing, depending on mode.
         * 根据模式的不同，抛出InterruptedException，重新中断当前线程，或者什么也不做。
         *
         */
        private void reportInterruptAfterWait(int interruptMode)
                throws InterruptedException {
            if (interruptMode == THROW_IE)
                throw new InterruptedException();
            else if (interruptMode == REINTERRUPT)
                selfInterrupt();
        }

        /**
         * Implements interruptible condition wait.
         *
         * If current thread is interrupted, throw InterruptedException Save lock state returned by {@link #getState} Invoke {@link #release} with saved state as argument, throwing IllegalMonitorStateException  if it fails.
         *
         * Block until signalled or interrupted Reacquire by invoking specialized version of {@link #acquire} with saved state as argument.
         *
         * If interrupted while blocked in step 4, throw exception
         *
         * 实现可中断条件等待。
         *
         * 如果当前线程被中断，抛出InterruptedException保存{@link #getState}返回的锁状态。调用{@link #release}以saved state为参数，如果失败则抛出IllegalMonitorStateException。
         *
         * 通过调用专用版本的{@link #acquire}，以保存的状态作为参数，直到有信号发出或被中断重新获取。
         *
         * 如果在第4步阻塞时中断，抛出异常
         *
         */
        public final void await() throws InterruptedException {
            if (Thread.interrupted())
                throw new InterruptedException();
            Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            int interruptMode = 0;
            while (!isOnSyncQueue(node)) {
                LockSupport.park(this);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
            }
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            if (node.nextWaiter != null)
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
        }

        /**
         * Implements timed condition wait.
         *
         * If current thread is interrupted, throw InterruptedException Save lock state returned by {@link #getState} Invoke {@link #release} with saved state as argument, throwing IllegalMonitorStateException  if it fails.
         *
         * Block until signalled, interrupted, or timed out Reacquire by invoking specialized version of {@link #acquire} with saved state as argument.
         *
         * If interrupted while blocked in step 4, throw InterruptedException
         *
         * 实现定时条件等待。
         *
         * 如果当前线程被中断，抛出InterruptedException保存{@link #getState}返回的锁状态。调用{@link #release}以saved state为参数，如果失败则抛出IllegalMonitorStateException。
         *
         * 用已保存的状态作为参数调用{@link #acquire}的特殊版本重新获取。
         *
         * 如果在第4步阻塞时中断，则抛出InterruptedException
         *
         */
        public final long awaitNanos(long nanosTimeout) throws InterruptedException {
            if (Thread.interrupted())
                throw new InterruptedException();
            Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            long lastTime = System.nanoTime();
            int interruptMode = 0;
            while (!isOnSyncQueue(node)) {
                if (nanosTimeout <= 0L) {
                    transferAfterCancelledWait(node);
                    break;
                }
                LockSupport.parkNanos(this, nanosTimeout);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;

                long now = System.nanoTime();
                nanosTimeout -= now - lastTime;
                lastTime = now;
            }
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            if (node.nextWaiter != null)
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
            return nanosTimeout - (System.nanoTime() - lastTime);
        }

        /**
         * Implements absolute timed condition wait.
         *
         * If current thread is interrupted, throw InterruptedException Save lock state returned by {@link #getState} Invoke {@link #release} with saved state as argument, throwing IllegalMonitorStateException  if it fails.
         *
         * Block until signalled, interrupted, or timed out Reacquire by invoking specialized version of {@link #acquire} with saved state as argument.
         *
         * If interrupted while blocked in step 4, throw InterruptedException If timed out while blocked in step 4, return false, else true
         *
         * 实现绝对定时条件等待。
         *
         * 如果当前线程被中断，抛出InterruptedException保存{@link #getState}返回的锁状态。调用{@link #release}以saved state为参数，如果失败则抛出IllegalMonitorStateException。
         *
         * 用已保存的状态作为参数调用{@link #acquire}的特殊版本重新获取。
         *
         * 如果在第4步阻塞时超时，则返回false，否则返回true
         *
         */
        public final boolean awaitUntil(Date deadline) throws InterruptedException {
            if (deadline == null)
                throw new NullPointerException();
            long abstime = deadline.getTime();
            if (Thread.interrupted())
                throw new InterruptedException();
            Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            boolean timedout = false;
            int interruptMode = 0;
            while (!isOnSyncQueue(node)) {
                if (System.currentTimeMillis() > abstime) {
                    timedout = transferAfterCancelledWait(node);
                    break;
                }
                LockSupport.parkUntil(this, abstime);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
            }
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            if (node.nextWaiter != null)
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
            return !timedout;
        }

        /**
         * Implements timed condition wait.
         *
         * If current thread is interrupted, throw InterruptedException Save lock state returned by {@link #getState} Invoke {@link #release} with saved state as argument, throwing IllegalMonitorStateException  if it fails.
         *
         * Block until signalled, interrupted, or timed out Reacquire by invoking specialized version of {@link #acquire} with saved state as argument.
         *
         * If interrupted while blocked in step 4, throw InterruptedException If timed out while blocked in step 4, return false, else true
         *
         * 实现定时条件等待。
         *
         * 如果当前线程被中断，抛出InterruptedException保存{@link #getState}返回的锁状态。调用{@link #release}以saved state为参数，如果失败则抛出IllegalMonitorStateException。
         *
         * 用已保存的状态作为参数调用{@link #acquire}的特殊版本重新获取。
         *
         * 如果在第4步阻塞时超时，则返回false，否则返回true
         *
         */
        public final boolean await(long time, TimeUnit unit) throws InterruptedException {
            if (unit == null)
                throw new NullPointerException();
            long nanosTimeout = unit.toNanos(time);
            if (Thread.interrupted())
                throw new InterruptedException();
            Node node = addConditionWaiter();
            int savedState = fullyRelease(node);
            long lastTime = System.nanoTime();
            boolean timedout = false;
            int interruptMode = 0;
            while (!isOnSyncQueue(node)) {
                if (nanosTimeout <= 0L) {
                    timedout = transferAfterCancelledWait(node);
                    break;
                }
                LockSupport.parkNanos(this, nanosTimeout);
                if ((interruptMode = checkInterruptWhileWaiting(node)) != 0)
                    break;
                long now = System.nanoTime();
                nanosTimeout -= now - lastTime;
                lastTime = now;
            }
            if (acquireQueued(node, savedState) && interruptMode != THROW_IE)
                interruptMode = REINTERRUPT;
            if (node.nextWaiter != null)
                unlinkCancelledWaiters();
            if (interruptMode != 0)
                reportInterruptAfterWait(interruptMode);
            return !timedout;
        }

        //  support for instrumentation
        //  支持工具

        /**
         * Returns true if this condition was created by the given synchronization object.
         *
         * 如果此条件是由给定的同步对象创建的，则返回true。
         *
         * @return {@code true} if owned
         */
        final boolean isOwnedBy(AbstractQueuedSynchronizer sync) {
            return sync == AbstractQueuedSynchronizer.this;
        }

        /**
         * Queries whether any threads are waiting on this condition.
         * Implements {@link AbstractQueuedSynchronizer#hasWaiters}.
         * 查询是否有线程在此条件下等待。
         * 实现了{@link AbstractQueuedSynchronizer # hasWaiters}。
         *
         * @return {@code true} if there are any waiting threads
         * @throws IllegalMonitorStateException if {@link #isHeldExclusively}
         *         returns {@code false}
         */
        protected final boolean hasWaiters() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            for (Node w = firstWaiter; w != null; w = w.nextWaiter) {
                if (w.waitStatus == Node.CONDITION)
                    return true;
            }
            return false;
        }

        /**
         * Returns an estimate of the number of threads waiting on this condition.
         * Implements {@link AbstractQueuedSynchronizer#getWaitQueueLength}.
         * 返回在此条件下等待的线程数的估计。
         * 实现了{@link AbstractQueuedSynchronizer # getWaitQueueLength}。
         *
         * @return the estimated number of waiting threads
         * @throws IllegalMonitorStateException if {@link #isHeldExclusively}
         *         returns {@code false}
         */
        protected final int getWaitQueueLength() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            int n = 0;
            for (Node w = firstWaiter; w != null; w = w.nextWaiter) {
                if (w.waitStatus == Node.CONDITION)
                    ++n;
            }
            return n;
        }

        /**
         * Returns a collection containing those threads that may be waiting on this Condition.
         * Implements {@link AbstractQueuedSynchronizer#getWaitingThreads}.
         * 返回一个集合，其中包含可能正在等待此Condition的线程。
         * 实现了{@link AbstractQueuedSynchronizer # getWaitingThreads}。
         *
         * @return the collection of threads
         * @throws IllegalMonitorStateException if {@link #isHeldExclusively}
         *         returns {@code false}
         */
        protected final Collection<Thread> getWaitingThreads() {
            if (!isHeldExclusively())
                throw new IllegalMonitorStateException();
            ArrayList<Thread> list = new ArrayList<Thread>();
            for (Node w = firstWaiter; w != null; w = w.nextWaiter) {
                if (w.waitStatus == Node.CONDITION) {
                    Thread t = w.thread;
                    if (t != null)
                        list.add(t);
                }
            }
            return list;
        }
    }

    /**
     * Setup to support compareAndSet.
     * We need to natively implement this here:
     * For the sake of permitting future enhancements,
     * we cannot explicitly subclass AtomicInteger,
     * which would be efficient and useful otherwise.
     * So, as the lesser of evils, we natively implement using hotspot intrinsics API.
     * And while we are at it, we do the same for other CASable fields (which could otherwise be done with atomic field updaters).
     * 安装程序支持compareAndSet。
     * 我们需要在这里本地实现它:
     * 为了允许未来的增强，
     * 不能显式子类化AtomicInteger，
     * 否则这将是有效和有用的。
     * 因此，我们使用hotspot intrinsics API来实现。
     * 当我们这样做的时候，我们对其他CASable字段做了同样的事情(否则可以用原子字段更新器来完成)。
     *
     * For the sake of-为了
     * explicitly-明确地
     * evils-邪恶
     * as the lesser of evils-作为较小的邪恶
     *
     */
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long stateOffset;
    private static final long headOffset;
    private static final long tailOffset;
    private static final long waitStatusOffset;
    private static final long nextOffset;

    static {
        try {
            stateOffset = unsafe.objectFieldOffset
                    (AbstractQueuedSynchronizer.class.getDeclaredField("state"));
            headOffset = unsafe.objectFieldOffset
                    (AbstractQueuedSynchronizer.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset
                    (AbstractQueuedSynchronizer.class.getDeclaredField("tail"));
            waitStatusOffset = unsafe.objectFieldOffset
                    (Node.class.getDeclaredField("waitStatus"));
            nextOffset = unsafe.objectFieldOffset
                    (Node.class.getDeclaredField("next"));

        } catch (Exception ex) { throw new Error(ex); }
    }

    /**
     * CAS head field. Used only by enq
     */
    private final boolean compareAndSetHead(Node update) {
        return unsafe.compareAndSwapObject(this, headOffset, null, update);
    }

    /**
     * CAS tail field. Used only by enq
     */
    private final boolean compareAndSetTail(Node expect, Node update) {
        return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
    }

    /**
     * CAS waitStatus field of a node.
     */
    private final static boolean compareAndSetWaitStatus(Node node,
                                                         int expect,
                                                         int update) {
        return unsafe.compareAndSwapInt(node, waitStatusOffset,
                expect, update);
    }

    /**
     * CAS next field of a node.
     */
    private final static boolean compareAndSetNext(Node node,
                                                   Node expect,
                                                   Node update) {
        return unsafe.compareAndSwapObject(node, nextOffset, expect, update);
    }
}
