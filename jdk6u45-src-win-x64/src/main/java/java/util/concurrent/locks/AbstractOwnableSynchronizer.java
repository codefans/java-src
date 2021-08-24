/*
 * %W% %E%
 *
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package java.util.concurrent.locks;

/**
 * A synchronizer that may be exclusively owned by a thread.  This
 * class provides a basis for creating locks and related synchronizers
 * that may entail a notion of ownership.  The
 * <tt>AbstractOwnableSynchronizer</tt> class itself does not manage or
 * use this information. However, subclasses and tools may use
 * appropriately maintained values to help control and monitor access
 * and provide diagnostics.
 *
 * exclusively
 entail意味着
 notion概念
 appropriately适当
 maintain保持
 diagnostics诊断

 * 同步器只会被一个线程占用。
 * 此类为创建可能需要所有权概念的锁和相关同步器提供了基础。
 * <tt> AbstractOwnableSynchronizer </ tt>类本身不管理或使用此信息。
 * 但是，子类和工具可以使用适当维护的值来帮助控制和监视访问并提供诊断
 *
 * @since 1.6
 * @author Doug Lea
 */
public abstract class AbstractOwnableSynchronizer
        implements java.io.Serializable {

    /** Use serial ID even though all fields transient. */
    /** 用一个serial ID，即使所有字段都不序列化. */
    private static final long serialVersionUID = 3737899427754241961L;

    /**
     * Empty constructor for use by subclasses.
     * 给子类使用的空构造方法
     */
    protected AbstractOwnableSynchronizer() { }

    /**
     * The current owner of exclusive mode synchronization.
     * 独占模式的锁的当前持有者.
     */
    private transient Thread exclusiveOwnerThread;

    /**
     * Sets the thread that currently owns exclusive access. A
     * <tt>null</tt> argument indicates that no thread owns access.
     * This method does not otherwise impose any synchronization or
     * <tt>volatile</tt> field accesses.
     *
     * indicate表示
     * otherwise除此之外、另外
     * impose强制、强加
     * 设置当前拥有独占访问权限的线程。
     * <tt>null</tt>参数表示没有线程拥有访问权限。
     * 此方法不会强制执行任何同步或<tt>volatile</tt>字段访问。
     */
    protected final void setExclusiveOwnerThread(Thread t) {
        exclusiveOwnerThread = t;
    }

    /**
     * Returns the thread last set by
     * <tt>setExclusiveOwnerThread</tt>, or <tt>null</tt> if never
     * set.  This method does not otherwise impose any synchronization
     * or <tt>volatile</tt> field accesses.
     * @return the owner thread
     *
     * 返回由<tt>setExclusiveOwnerThread</tt>设置的最后一个线程，
     * 如果从未设置，则返回<tt>null</tt>。
     * 此方法不会强制执行任何同步或<tt>volatile</tt>字段访问。
     * @return所有者线程
     *
     */
    protected final Thread getExclusiveOwnerThread() {
        return exclusiveOwnerThread;
    }
}
