package com.immomo.mls;

import com.immomo.mls.log.DefaultPrintStream;

import org.luaj.vm2.Globals;
import org.luaj.vm2.exception.InvokeError;

import java.io.PrintStream;

/**
 * Created by Xiong.Fangyu on 2019/3/25
 */
public class Environment {
    public static boolean DEBUG = false;

    public static final String LUA_ERROR = "[LUA_ERROR] ";

    public static UncatchExceptionListener uncatchExceptionListener;
    public static boolean hook(Throwable t, Globals globals) {
        if (globals.getState() == Globals.LUA_CALLING)
            return false;
        if (t instanceof InvokeError) {
            InvokeError te = (InvokeError) t;
            if (te.getType() != 0)
                return true;
        }
        if (DEBUG) {
            error(t, globals);
        }
        if (uncatchExceptionListener != null) {
            return uncatchExceptionListener.onUncatch(globals, t);
        }
        return false;
    }

    public static void error(Throwable t, Globals globals) {
        String error = getErrorMsg(t);
        LuaViewManager m = (LuaViewManager) globals.getJavaUserdata();
        if (m != null && m.STDOUT != null) {
            PrintStream ps = m.STDOUT;
            if (ps instanceof DefaultPrintStream) {
                ((DefaultPrintStream) ps).error(LUA_ERROR + error);
            } else {
                ps.print(LUA_ERROR + error);
                ps.println();
            }
            m.showPrinterIfNot();
        }

        HotReloadHelper.onError(t != null ? t.getMessage() : "null");
    }

    public static interface UncatchExceptionListener {
        /**
         * called when some throwable is not caught in lua sdk
         *
         * @param e
         * @return true to handle uncatch throwable, false otherwise.
         */
        boolean onUncatch(Globals globals, Throwable e);
    }


    private static String getErrorMsg(Throwable t) {
        Throwable temp = t != null ? t.getCause() : null;
        if (temp != null) {
            t = temp;
        }
        return t != null ? t.getMessage() : "";
    }
}
