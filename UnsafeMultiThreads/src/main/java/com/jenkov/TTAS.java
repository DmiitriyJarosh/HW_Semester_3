package com.jenkov;

import java.util.concurrent.atomic.AtomicBoolean;

public class TTAS implements Lock {

    protected AtomicBoolean m_locked;

    public TTAS() {
        m_locked = new AtomicBoolean(false);
    }

    public void lock() {
        boolean acquired = false;
        while(!acquired) {
            if(!m_locked.get()) {
                acquired = m_locked.compareAndSet(false, true);
            }
        }
    }

    public boolean tryLock() {
        if(m_locked.get()) {
            return false;
        }

        return m_locked.compareAndSet(false, true);
    }

    public void unlock() {
        m_locked.set(false);
    }
}
