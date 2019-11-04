package com.immomo.mls.fun.lt;

import android.content.Context;

import com.immomo.mls.LuaViewManager;
import com.immomo.mls.annotation.LuaBridge;
import com.immomo.mls.annotation.LuaClass;
import com.immomo.mls.fun.constants.NetworkState;
import com.immomo.mls.receiver.ConnectionStateChangeBroadcastReceiver;
import com.immomo.mls.util.NetworkUtil;
import com.immomo.mls.utils.MainThreadExecutor;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;

/**
 * Created by fanqiang on 2018/9/28.
 */
@LuaClass
public class SINetworkReachability implements ConnectionStateChangeBroadcastReceiver.OnConnectionChangeListener {

    public static final String LUA_CLASS_NAME = "NetworkReachability";

    private LuaFunction networkStateCallback;
    private Globals globals;

    public SINetworkReachability(Globals globals, LuaValue[] init) {
        this.globals = globals;
    }

    public void __onLuaGc() {
        MainThreadExecutor.cancelAllRunnable(getTag());
        NetworkUtil.unregisterConnectionChangeListener(getContext(), this);
        if (networkStateCallback != null) {
            networkStateCallback.destroy();
        }
        networkStateCallback = null;
    }

    protected Context getContext() {
        return ((LuaViewManager) globals.getJavaUserdata()).context;
    }

    //<editor-fold desc="API">
    @LuaBridge
    public void open() {
        NetworkUtil.registerConnectionChangeListener(getContext(), this);
    }

    @LuaBridge
    public void close() {
        NetworkUtil.unregisterConnectionChangeListener(getContext(), this);
    }

    @LuaBridge
    public int networkState() {
        NetworkUtil.NetworkType type = NetworkUtil.getCurrentType(getContext());
        switch (type) {
            case NETWORK_2G:
            case NETWORK_3G:
            case NETWORK_4G:
                return NetworkState.CELLULAR;
            case NETWORK_WIFI:
                return NetworkState.WIFI;
            case NETWORK_UNKNOWN:
                return NetworkState.UNKNOWN;
            default:
                return NetworkState.NO_NETWORK;
        }
    }

    @LuaBridge
    public void setOnNetworkStateChange(LuaFunction callback) {
        if (networkStateCallback != null) {
            networkStateCallback.destroy();
        }
        networkStateCallback = callback;
    }

    //</editor-fold>

    private Object getTag() {
        return "NetworkReachability" + hashCode();
    }

    //<editor-fold desc="OnConnectionChangeListener">
    @Override
    public void onConnectionClosed() {
        if (networkStateCallback != null)
            networkStateCallback.invoke(LuaValue.rNumber(NetworkState.NO_NETWORK));
    }

    @Override
    public void onMobileConnected() {
        if (networkStateCallback != null) {
            networkStateCallback.invoke(LuaValue.rNumber(NetworkState.CELLULAR));
        }
    }

    @Override
    public void onWifiConnected() {
        if (networkStateCallback != null) {
            networkStateCallback.invoke(LuaValue.rNumber(NetworkState.WIFI));
        }
    }
    //</editor-fold>
}
