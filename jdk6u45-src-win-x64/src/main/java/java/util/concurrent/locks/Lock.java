/*
 * %W% %E%
 *
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package java.util.concurrent.locks;
import java.util.concurrent.TimeUnit;

/**
 * {@code Lock} implementations provide more extensive locking
 * operations than can be obtained using {@code synchronized} methods
 * and statements.  They allow more flexible structuring, may have
 * quite different properties, and may support multiple associated
 * {@link Condition} objects.
 *  extensive广泛
 *  obtained获得
 *  statements声明
 *  flexible structuring灵活的结构
 *  Lock实现提供了比用synchronized方法和声明更广泛的锁定操作。他们允许更灵活的结构，有不同的属性，支持多个关联对象。
 *
 *
 * <p>A lock is a tool for controlling access to a shared resource by
 * multiple threads. Commonly, a lock provides exclusive access to a
 * shared resource: only one thread at a time can acquire the lock and
 * all access to the shared resource requires that the lock be
 * acquired first. However, some locks may allow concurrent access to
 * a shared resource, such as the read lock of a {@link ReadWriteLock}.
 *
 * exclusive独家,独占
 * 锁是一个控制访问被多个线程共享的资源的工具。
 * 通常，锁提供了对共享资源的独占访问：在同一时间只能有一个线程获得锁，对共享资源的完全访问需要先获得锁。
 * 然而，有些锁是允许并发访问共享资源的，比如读写锁ReadWriteLock。
 *
 * <p>The use of {@code synchronized} methods or statements provides
 * access to the implicit monitor lock associated with every object, but
 * forces all lock acquisition and release to occur in a block-structured way:
 * when multiple locks are acquired they must be released in the opposite
 * order, and all locks must be released in the same lexical scope in which
 * they were acquired.
 *
 * implicit-隐含的
 * acquisition-获得
 * block-structured-块结构
 * occur-发生
 * opposite-相反
 * lexical-词法
 * 使用了synchronized的方法或语句，可以访问关联在每个对象上的隐式监视器锁，但是强制所有锁以块结构的方式去获得和释放：
 * 当多个锁被获得时，他们必须以相反的顺序被释放，所有的锁必须在他们获得锁的相同词法范围释放。
 *
 * <p>While the scoping mechanism for {@code synchronized} methods
 * and statements makes it much easier to program with monitor locks,
 * and helps avoid many common programming errors involving locks,
 * there are occasions where you need to work with locks in a more
 * flexible way. For example, some algorithms for traversing
 * concurrently accessed data structures require the use of
 * &quot;hand-over-hand&quot; or &quot;chain locking&quot;: you
 * acquire the lock of node A, then node B, then release A and acquire
 * C, then release B and acquire D and so on.  Implementations of the
 * {@code Lock} interface enable the use of such techniques by
 * allowing a lock to be acquired and released in different scopes,
 * and allowing multiple locks to be acquired and released in any
 * order.
 *
 * mechanism机制
 * in a more flexible way以更灵活的方式
 * involve涉及
 * involving涉及
 * occasions-场合
 * traversing-遍历
 * 范围机制让编写监视器锁变得更简单，帮助避免很多常见的涉及锁的编程错误，
 * 在某些情况下，您需要以更灵活的方式使用锁。例如，用于遍历并发访问的数据结构的一些算法需要使用手动或链锁定：
 * 获取节点A的锁，然后获取节点B，然后释放A并获取C，然后释放B并获取D和等等。
 * 通过允许在不同范围内获取和释放锁，并允许以任何顺序获取和释放多个锁，接口的实现使得能够使用这种技术。
 *
 * <p>With this increased flexibility comes additional
 * responsibility. The absence of block-structured locking removes the
 * automatic release of locks that occurs with {@code synchronized}
 * methods and statements. In most cases, the following idiom
 * should be used:
 *
 * flexibility灵活性
 * absence缺少
 * idiom习语、惯用语法
 * 随着这种增加的灵活性，附加责任。缺少块结构锁定会消除方法和语句发生的锁的自动释放。在大多数情况下，应使用以下用法，手动获取和释放锁
 *
 * <pre><tt>     Lock l = ...;
 *     l.lock();
 *     try {
 *         // access the resource protected by this lock
 *     } finally {
 *         l.unlock();
 *     }
 * </tt></pre>
 *
 * When locking and unlocking occur in different scopes, care must be
 * taken to ensure that all code that is executed while the lock is
 * held is protected by try-finally or try-catch to ensure that the
 * lock is released when necessary.
 * 当锁定和解锁发生在不同的范围内时，必须注意确保在保持锁定时执行的所有代码都受try-finally或try-catch的保护，以确保在必要时释放锁定
 *
 * <p>{@code Lock} implementations provide additional functionality
 * over the use of {@code synchronized} methods and statements by
 * providing a non-blocking attempt to acquire a lock ({@link
 * #tryLock()}), an attempt to acquire the lock that can be
 * interrupted ({@link #lockInterruptibly}, and an attempt to acquire
 * the lock that can timeout ({@link #tryLock(long, TimeUnit)}).
 *
 * 通过提供非阻塞的方式获取锁，尝试获取(tryLock)可能被中断的锁(lockInterruptibly)以及尝试获取可能超时的锁(tryLock(long, TimeUnit))，
 * Lock的实现提供了比Synchronized方法和语句更多的额外功能。
 *
 * <p>A {@code Lock} class can also provide behavior and semantics
 * that is quite different from that of the implicit monitor lock,
 * such as guaranteed ordering, non-reentrant usage, or deadlock
 * detection. If an implementation provides such specialized semantics
 * then the implementation must document those semantics.
 *
 * behavior行为
 * semantics语义
 * implicit隐式
 * implicit monitor lock隐式监视器锁
 * non-reentrant非重入
 * Lock类还可以提供与隐式监视器锁完全不同的行为和语义，例如保证排序，非重入使用或死锁检测。
 * 如果实现提供了这样的专用语义，那么实现必须记录那些语义。
 *
 * <p>Note that {@code Lock} instances are just normal objects and can
 * themselves be used as the target in a {@code synchronized} statement.
 * Acquiring the
 * monitor lock of a {@code Lock} instance has no specified relationship
 * with invoking any of the {@link #lock} methods of that instance.
 * It is recommended that to avoid confusion you never use {@code Lock}
 * instances in this way, except within their own implementation.
 *
 * 请注意，Lock实例只是普通对象，它们本身可以用作synchronized语句中的目标。
 * 获取Lock实例的监视器锁定与调用该实例的任何锁定方法没有特别的关系。
 * 为避免混淆，建议您不要以这种方式使用Lock实例，除非在他们自己的实现中。
 *
 * <p>Except where noted, passing a {@code null} value for any
 * parameter will result in a {@link NullPointerException} being
 * thrown.
 *
 * 除非另有说明，否则任何参数传递null值将导致抛出NullPointerException。
 *
 * <h3>Memory Synchronization</h3>
 *
 * <p>All {@code Lock} implementations <em>must</em> enforce the same
 * memory synchronization semantics as provided by the built-in monitor
 * lock, as described in <a href="http://java.sun.com/docs/books/jls/">
 * The Java Language Specification, Third Edition (17.4 Memory Model)</a>:
 *
 * 所有Lock实现必须强制执行内置监视器锁提供的相同内存同步语义，
 * 如<a href="http://java.sun.com/docs/books/jls/"> Java语言规范中所述，第三版（17.4内存模型）</a>
 *
 * <ul>
 * <li>A successful {@code lock} operation has the same memory
 * synchronization effects as a successful <em>Lock</em> action.
 * <li>A successful {@code unlock} operation has the same
 * memory synchronization effects as a successful <em>Unlock</em> action.
 * </ul>
 * 一个成功的加锁(指synchronized加锁)操作，与一个成功的Lock动作，有相同的内存同步效果。
 * 一个成功的解锁(指synchronized解锁)操作，与一个成功的Unlock动作，有相同的内存同步效果。
 *
 * Unsuccessful locking and unlocking operations, and reentrant
 * locking/unlocking operations, do not require any memory
 * synchronization effects.
 *
 * 不成功的锁定和解锁操作，以及重入锁定/解锁操作，不需要任何内存同步效果。
 *
 * <h3>Implementation Considerations</h3>
 * 实现注意事项
 *
 * <p> The three forms of lock acquisition (interruptible,
 * non-interruptible, and timed) may differ in their performance
 * characteristics, ordering guarantees, or other implementation
 * qualities.  Further, the ability to interrupt the <em>ongoing</em>
 * acquisition of a lock may not be available in a given {@code Lock}
 * class.  Consequently, an implementation is not required to define
 * exactly the same guarantees or semantics for all three forms of
 * lock acquisition, nor is it required to support interruption of an
 * ongoing lock acquisition.  An implementation is required to clearly
 * document the semantics and guarantees provided by each of the
 * locking methods. It must also obey the interruption semantics as
 * defined in this interface, to the extent that interruption of lock
 * acquisition is supported: which is either totally, or only on
 * method entry.
 *
 * Further此外
 * ongoing正在进行
 * Consequently因此
 * define定义
 * exactly完全
 * clearly清楚地
 * document记录
 * obey遵守
 * extent程度
 * to the extent到…的程度
 * 锁定获取的三种形式（可中断，不可中断和定时）可能在性能特征，排序保证或其他实现质量方面有所不同。
 * 此外，在给定的Lock类中可能无法中断正在进行的锁定获取的能力。
 * 因此，不需要实现为所有三种形式的锁获取定义完全相同的保证或语义，也不需要支持正在进行的锁获取的中断。
 * 需要一种实现来清楚地记录每种锁定方法提供的语义和保证。它还必须遵守此接口中定义的中断语义，以支持锁获取的中断：完全或仅在方法入口上。
 *
 * <p>As interruption generally implies cancellation, and checks for
 * interruption are often infrequent, an implementation can favor responding
 * to an interrupt over normal method return. This is true even if it can be
 * shown that the interrupt occurred after another action may have unblocked
 * the thread. An implementation should document this behavior.
 *
 * imply，implies意味着
 * infrequent不常见
 * favor有利于
 *
 * 由于中断通常意味着取消，并且中断检查通常不常见，因此实现可以有利于响应正常方法返回的中断。
 * 即使可以显示在另一个操作可能已取消阻塞线程之后发生中断，也是如此。实现应记录此行为。
 *
 * @see ReentrantLock
 * @see Condition
 * @see ReadWriteLock
 *
 * @since 1.5
 * @author Doug Lea
 */
public interface Lock {

    /**
     * Acquires the lock.
     * 获得锁
     *
     * <p>If the lock is not available then the current thread becomes
     * disabled for thread scheduling purposes and lies dormant until the
     * lock has been acquired.
     * 如果锁不可用，则当前线程变为出于线程调度目的而禁用并处于休眠状态，直到已获得锁。
     *
     * <p><b>Implementation Considerations</b>
     * 实施注意事项（实现注意事项）
     *
     * <p>A {@code Lock} implementation may be able to detect erroneous use
     * of the lock, such as an invocation that would cause deadlock, and
     * may throw an (unchecked) exception in such circumstances.  The
     * circumstances and the exception type must be documented by that
     * {@code Lock} implementation.
     * Lock 实现可能能够检测到错误使用锁，例如会导致死锁的调用，以及在这种情况下可能会抛出（未经检查的）异常。
     * 这情况和异常类型必须由锁实现来记录。
     *
     * lies dormant-处于休眠状态
     * dormant-休眠的
     * erroneous-错误
     * detect-探测
     * circumstances-情况
     *
     */
    void lock();

    /**
     * Acquires the lock unless the current thread is
     * {@linkplain Thread#interrupt interrupted}.
     * 获取锁，除非当前线程被Thread.interrupt中断。
     *
     * <p>Acquires the lock if it is available and returns immediately.
     * 如果可用，则获取锁并立即返回。
     *
     * <p>If the lock is not available then the current thread becomes
     * disabled for thread scheduling purposes and lies dormant until
     * one of two things happens:
     * 如果锁不可用，则当前线程变为出于线程调度目的而禁用并处于休眠状态，
     * 直到发生以下两种情况之一：
     *
     * <ul>
     * <li>The lock is acquired by the current thread; or
     * <li>Some other thread {@linkplain Thread#interrupt interrupts} the
     * current thread, and interruption of lock acquisition is supported.
     * </ul>
     *      锁被当前线程获取；
     *      或者其他一些线程中断了当前线程，支持中断获取锁。
     *
     * <p>If the current thread:
     * <ul>
     * <li>has its interrupted status set on entry to this method; or
     * <li>is {@linkplain Thread#interrupt interrupted} while acquiring the
     * lock, and interruption of lock acquisition is supported,
     * </ul>
     * then {@link InterruptedException} is thrown and the current thread's
     * interrupted status is cleared.
     * 如果当前线程：
     *    在进入这个方法时设置了中断状态；
     *    或者获取锁的时候被中断，支持中断获取锁。
     *
     * <p><b>Implementation Considerations</b>
     * 实施注意事项
     *
     * <p>The ability to interrupt a lock acquisition in some
     * implementations may not be possible, and if possible may be an
     * expensive operation.  The programmer should be aware that this
     * may be the case. An implementation should document when this is
     * the case.
     * 在某些实现中，中断获取锁的能力可能是不可能的，如果可能的话，这可能是一项昂贵的操作。
     * 程序员应该意识到可能是这种情况。在这种情况下，实现应该记录。
     * aware-意识
     *
     * <p>An implementation can favor responding to an interrupt over
     * normal method return.
     * 一个实现可以倾向于响应中断而不是正常方法返回
     *
     * <p>A {@code Lock} implementation may be able to detect
     * erroneous use of the lock, such as an invocation that would
     * cause deadlock, and may throw an (unchecked) exception in such
     * circumstances.  The circumstances and the exception type must
     * be documented by that {@code Lock} implementation.
     * 一个Lock的实现可能能够检测到错误使用锁，例如一个调用可能导致死锁，并可能在这种情况下抛出（未经检查的）异常情况。
     * 这种情况和异常类型必须由这个Lock的实现来记录。
     *
     * @throws InterruptedException if the current thread is
     *         interrupted while acquiring the lock (and interruption
     *         of lock acquisition is supported).
     * 抛出InterruptedException异常, 如果当前线程获取锁时被中断了（支持中断）
     */
    void lockInterruptibly() throws InterruptedException;

    /**
     * Acquires the lock only if it is free at the time of invocation.
     * 仅在调用的空闲时间才获取锁。
     *
     * <p>Acquires the lock if it is available and returns immediately
     * with the value {@code true}.
     * If the lock is not available then this method will return
     * immediately with the value {@code false}.
     * 获取锁如果锁可用并直接返回true
     * 如果锁不可用则直接返回false
     *
     * <p>A typical usage idiom for this method would be:
     *    这个方法一个典型的使用模板如下：
     * <pre>
     *      Lock lock = ...;
     *      if (lock.tryLock()) {
     *          try {
     *              // manipulate protected state
     *          } finally {
     *              lock.unlock();
     *          }
     *      } else {
     *          // perform alternative actions
     *      }
     * </pre>
     * This usage ensures that the lock is unlocked if it was acquired, and
     * doesn't try to unlock if the lock was not acquired.
     * 这种用法可确保在获得锁时解锁，并且如果未获得锁，则不会尝试解锁。
     *
     * @return {@code true} if the lock was acquired and
     *         {@code false} otherwise
     * 返回true如果获取到锁，否则返回false
     */
    boolean tryLock();

    /**
     * Acquires the lock if it is free within the given waiting time and the
     * current thread has not been {@linkplain Thread#interrupt interrupted}.
     * 如果在给定的等待时间内空闲，则获取锁，当前线程未被Thread.interrupt()中断。
     *
     * <p>If the lock is available this method returns immediately
     * with the value {@code true}.
     * If the lock is not available then
     * the current thread becomes disabled for thread scheduling
     * purposes and lies dormant until one of three things happens:
     * 如果锁可用，则此方法立即返回true。
     * 如果锁不可用，则当前线程被禁用以进行线程调度目的并处于休眠状态，直到发生以下三件事之一：
     *
     * <ul>
     * <li>The lock is acquired by the current thread; or
     * <li>Some other thread {@linkplain Thread#interrupt interrupts} the
     * current thread, and interruption of lock acquisition is supported; or
     * <li>The specified waiting time elapses
     * </ul>
     *     (1)锁被当前线程获取；
     *     (2)一些其他线程中断了当前线程, (获取锁支持中断)
     *     (3)或者经过指定的等待时间。
     *
     * <p>If the lock is acquired then the value {@code true} is returned.
     *   如果锁被获取到了，则返回true。
     *
     * <p>If the current thread:
     * <ul>
     * <li>has its interrupted status set on entry to this method; or
     * <li>is {@linkplain Thread#interrupt interrupted} while acquiring
     * the lock, and interruption of lock acquisition is supported,
     * </ul>
     * then {@link InterruptedException} is thrown and the current thread's
     * interrupted status is cleared.
     * 如果当前线程：
     *    (1)在进入此方法时设置其中断状态；
     *    (2)或者获取锁时被中断（支持中断）
     * 那么将抛出InterruptedException异常, 当前线程的中断状态将被清除。
     *
     * <p>If the specified waiting time elapses then the value {@code false}
     * is returned.
     * If the time is
     * less than or equal to zero, the method will not wait at all.
     * 如果指定的等待时间过去了，则返回false；
     * 如果时间小于或者等于0， 则该方法根本就不会等待。
     *
     * <p><b>Implementation Considerations</b>
     * 实施注意事项
     *
     * <p>The ability to interrupt a lock acquisition in some implementations
     * may not be possible, and if possible may be an expensive operation.
     * The programmer should be aware that this may be the case. An
     * implementation should document when this is the case.
     * 在某些实现中, 中断锁定获取的能力可能是不可能的，如果可能的话也是一个昂贵的操作。
     * 程序员应该意识到可能是这种情况。
     * 如果是这种情况下，实现类应该记录下。
     *
     * <p>An implementation can favor responding to an interrupt over normal
     * method return, or reporting a timeout.
     * 与正常方法返回的情况相比，实现类可以更倾向于响应中断，或者报告超时
     *
     * <p>A {@code Lock} implementation may be able to detect
     * erroneous use of the lock, such as an invocation that would cause
     * deadlock, and may throw an (unchecked) exception in such circumstances.
     * The circumstances and the exception type must be documented by that
     * {@code Lock} implementation.
     * 一个Lock的实现可能需要能够检测到错误使用锁，例如调用会导致死锁，
     * 在这种情况下可能会抛出（未经检查的）异常。
     * 这种情况和异常类型必须该Lock实现记录一下。
     *
     * @param time the maximum time to wait for the lock
     *        最大等待时间
     * @param unit the time unit of the {@code time} argument
     *        时间单位
     * @return {@code true} if the lock was acquired and {@code false}
     *         if the waiting time elapsed before the lock was acquired
     *        如果获取到锁，则返回true；
     *        如果超过等待时间，则返回false。
     *
     * @throws InterruptedException if the current thread is interrupted
     *         while acquiring the lock (and interruption of lock
     *         acquisition is supported)
     * 抛出InterruptedException异常, 如果当前线程被中断的话。（需支持中断）
     */
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;

    /**
     * Releases the lock.
     * 释放锁
     *
     * <p><b>Implementation Considerations</b>
     * 实施注意事项
     *
     * <p>A {@code Lock} implementation will usually impose
     * restrictions on which thread can release a lock (typically only the
     * holder of the lock can release it) and may throw
     * an (unchecked) exception if the restriction is violated.
     * Any restrictions and the exception
     * type must be documented by that {@code Lock} implementation.
     *
     * 一个Lock实现通常会强加限制，限制哪个线程可以释放锁（通常只有锁的持有者可以释放它）
     * 如果违反限制，则抛出（未经检查的）异常。
     * 任何限制和例外类型必须由Lock的实现来记录。
     *
     * impose-强加
     * violate-违反
     *
     */
    void unlock();

    /**
     * Returns a new {@link Condition} instance that is bound to this
     * {@code Lock} instance.
     * 返回一个绑定到这个Lock实例上的新Condition对象
     *
     * <p>Before waiting on the condition the lock must be held by the
     * current thread.
     * A call to {@link Condition#await()} will atomically release the lock
     * before waiting and re-acquire the lock before the wait returns.
     * 在等待条件之前，锁必须由当前线程持有。
     * 一个Condition.await()的调用，在等待之前将自动释放锁
     * 并在等待返回之前重新获取锁。
     *
     * <p><b>Implementation Considerations</b>
     * 实施注意事项
     *
     * <p>The exact operation of the {@link Condition} instance depends on
     * the {@code Lock} implementation and must be documented by that
     * implementation.
     * 这个Condition实例，确切的操作取决于Lock实现。
     * Lock实现必须记录它。
     *
     * @return A new {@link Condition} instance for this {@code Lock} instance
     * @throws UnsupportedOperationException if this {@code Lock}
     *         implementation does not support conditions
     * 返回这个Lock对象的一个新的Condition实例,
     * 如果这个Lock实现不支持conditions, 则抛出UnsupportedOperationException异常
     *
     */
    Condition newCondition();
}
