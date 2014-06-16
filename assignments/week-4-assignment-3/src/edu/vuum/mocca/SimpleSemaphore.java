package edu.vuum.mocca;

import java.util.concurrent.locks.AbstractQueuedSynchronizer.ConditionObject;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @class SimpleSemaphore
 * 
 * @brief This class provides a simple counting semaphore
 *        implementation using Java a ReentrantLock and a
 *        ConditionObject (which is accessed via a Condition). It must
 *        implement both "Fair" and "NonFair" semaphore semantics,
 *        just liked Java Semaphores.
 */
public class SimpleSemaphore {
    /**
     * Define a ReentrantLock to protect the critical section.
     */
    // TODO - you fill in here
	private final ReentrantLock mLock; 	

    /**
     * Define a Condition that waits while the number of permits is 0.
     */
    // TODO - you fill in here
	private final Condition mCond;

    /**
     * Define a count of the number of available permits.
     */
    // TODO - you fill in here.  Make sure that this data member will
    // ensure its values aren't cached by multiple Threads..
	private volatile int mAvailablePermits;

    public SimpleSemaphore(int initialPermits, boolean fair) {
        // TODO - you fill in here to initialize the SimpleSemaphore,
        // making sure to allow both fair and non-fair Semaphore
        // semantics.
    	
    	// Initialize ReentrantLock with fairness
    	mLock = new ReentrantLock(fair);
    	
    	mCond = mLock.newCondition();
    	
    	mAvailablePermits = initialPermits;
    }

    /**
     * Acquire one permit from the semaphore in a manner that can be
     * interrupted.
     */
    public void acquire() throws InterruptedException {
        // TODO - you fill in here.
    	final ReentrantLock l = mLock;
    	
    	l.lockInterruptibly();
    	try {
    		while (mAvailablePermits <= 0) {
    			mCond.await();
    		}
    		mAvailablePermits--;
    	}
    	finally {
    		l.unlock();
    	}
    	
    }

    /**
     * Acquire one permit from the semaphore in a manner that cannot be
     * interrupted.
     */
    public void acquireUninterruptibly() {
        // TODO - you fill in here.
    	final ReentrantLock l = mLock;
    	
    	l.lock();
    	try {
    		while (mAvailablePermits <= 0) {
    		 	mCond.awaitUninterruptibly();
    		}
    		this.mAvailablePermits--;
    	}
    	finally {
    		l.unlock();
    	}
    	
    }

    /**
     * Return one permit to the semaphore.
     */
    public void release() {
        // TODO - you fill in here.
    	final ReentrantLock l = mLock;
    	
    	l.lock();
    	try {
    		mAvailablePermits++;
    		if (mAvailablePermits > 0) {
    			mCond.signal();
    		}
    	}
    	finally {
    		l.unlock();
    	}
    	
    }

    /**
     * Return the number of permits available.
     */
    public int availablePermits() {
        // TODO - you fill in here by changing null to the appropriate
        // return value.
        return mAvailablePermits;
    }
}
