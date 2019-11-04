package com.immomo.mls.fun.ud;


import com.immomo.mls.annotation.BridgeType;
import com.immomo.mls.annotation.LuaBridge;
import com.immomo.mls.annotation.LuaClass;
import com.immomo.mls.utils.MainThreadExecutor;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.exception.InvokeError;
import org.luaj.vm2.utils.LuaApiUsed;


/**
 * Created by XiongFangyu on 2018/9/5.
 */
@LuaClass(gcByLua = false)
public class Timer {
    public static final String LUA_CLASS_NAME = "Timer";

    private static final byte STATE_IDEL = 0;
    private static final byte STATE_RUNNING = 1;
    private static final byte STATE_PAUSING = 2;
    private static final byte STATE_END = 3;

    private long interval;
    @LuaBridge
    int repeatCount = 0;
    private LuaFunction task;
    private byte state = STATE_IDEL;
    private Task runningTask;
    private final Object tag;

    public Timer(Globals g, LuaValue[] init) {
        tag = new Object();
    }

    @LuaApiUsed
    public void __onLuaGc() {
        if (task != null) {
            task.destroy();
            task = null;
        }
        runningTask = null;
        MainThreadExecutor.cancelAllRunnable(getTag());
    }

    //<editor-fold desc="API">
    //<editor-fold desc="Property">
    @LuaBridge(alias = "interval", type = BridgeType.GETTER)
    public float getInterval() {
        return interval / 1000f;
    }

    @LuaBridge(alias = "interval", type = BridgeType.SETTER)
    public void setInterval(float interval) {
        this.interval = (long) (interval * 1000);
    }
    //</editor-fold>

    //<editor-fold desc="Methods">
    @LuaBridge
    public void start(LuaFunction task) {
        if (state == STATE_RUNNING || state == STATE_PAUSING) {
            return;
        }
        this.task = task;
        state = STATE_RUNNING;
        runningTask = new Task();
        MainThreadExecutor.cancelSpecificRunnable(getTag(), runningTask);
        MainThreadExecutor.postDelayed(getTag(), runningTask, interval);
    }

    @LuaBridge
    public void pause() {
//        if (state == STATE_IDEL)
//            throw new IllegalStateException("no task started!");
        if (state == STATE_RUNNING) {
            state = STATE_PAUSING;
        }
    }

    @LuaBridge
    public void resume() {
//        if (state == STATE_IDEL)
//            throw new IllegalStateException("no task started!");
        if (state == STATE_PAUSING) {
            state = STATE_RUNNING;
            MainThreadExecutor.cancelSpecificRunnable(getTag(), runningTask);

            runningTask.run();
        }
    }

    @LuaBridge
    public void resumeDelay() {
        if (state == STATE_PAUSING) {
            state = STATE_RUNNING;
            if (runningTask != null) {
                MainThreadExecutor.cancelSpecificRunnable(getTag(), runningTask);
                MainThreadExecutor.postDelayed(getTag(), runningTask, interval);
            }
        }
    }

    @LuaBridge
    public void stop() {
//        if (state == STATE_IDEL)
//            throw new IllegalStateException("no task started!");
        state = STATE_END;
        task = null;
        runningTask = null;
    }
    //</editor-fold>
    //</editor-fold>

    private final class Task implements Runnable {
        private int doCount = 0;

        Task() {

        }

        @Override
        public void run() {
            if (task == null || task.getGlobals() == null || task.getGlobals().isDestroyed()) {
                state = STATE_END;
                return;
            }
            if (state != STATE_RUNNING) {
                return;
            }
            if (repeatCount <= 0 || repeatCount > ++doCount) {
                try {
                    task.invoke(LuaValue.rFalse());
                    MainThreadExecutor.cancelSpecificRunnable(getTag(),this);
                    MainThreadExecutor.postDelayed(getTag(), this, interval);
                } catch (InvokeError e) {
                    task = null;
                }
            } else {
                try {
                    task.invoke(LuaValue.rTrue());
                } catch (InvokeError ignore) {}
                state = STATE_END;
                task = null;
            }
        }
    }

    private Object getTag() {
        return tag;
    }
}
