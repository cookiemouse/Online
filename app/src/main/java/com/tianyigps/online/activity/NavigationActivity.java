package com.tianyigps.online.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.google.gson.Gson;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.tianyigps.online.R;
import com.tianyigps.online.bean.TerminalInfo4MapBean;
import com.tianyigps.online.data.Data;
import com.tianyigps.online.interfaces.OnShowTerminalInfoForMapListener;
import com.tianyigps.online.manager.NetManager;
import com.tianyigps.online.manager.SharedManager;
import com.tianyigps.online.utils.ToastU;

import java.util.ArrayList;
import java.util.List;

public class NavigationActivity extends AppCompatActivity implements AMapNaviViewListener, AMapNaviListener, SynthesizerListener {

    private static final String TAG = "NavigationActivity";
    private static final int FLUSH_TIME = 40000;
    private static final int DELAY = 5000;

    private AMapNaviView mAMapNaviView;
    private AMapNavi mAMapNavi;

    //  科大讯飞
    private SpeechSynthesizer mSpeechSynthesizer;

    private NaviLatLng mNaviLatLngEnd;

    private ToastU mToastU;

    private NetManager mNetManager;
    private SharedManager mSharedManager;
    private MyHandler myHandler;

    private String mToken = "", mImei = "";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_navigation);

        Intent intent = getIntent();

        //  初始化科大讯飞
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=575f6945");
        mSpeechSynthesizer = SpeechSynthesizer.createSynthesizer(this, new InitListener() {
            @Override
            public void onInit(int i) {
                Log.i(TAG, "onInit: ");
            }
        });

        //获取 AMapNaviView 实例
        mAMapNaviView = (AMapNaviView) findViewById(R.id.navi_view);
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);

        mToastU = new ToastU(this);

        mNetManager = new NetManager();
        mSharedManager = new SharedManager(this);
        myHandler = new MyHandler();

        mToken = mSharedManager.getToken();
        mImei = intent.getStringExtra(Data.KEY_IMEI);

        mAMapNavi = AMapNavi.getInstance(getApplicationContext());

        mAMapNavi.addAMapNaviListener(this);


        mNetManager.setOnShowTerminalInfoForMapListener(new OnShowTerminalInfoForMapListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                TerminalInfo4MapBean terminalInfo4MapBean = gson.fromJson(result, TerminalInfo4MapBean.class);
                if (!terminalInfo4MapBean.isSuccess()) {
                    return;
                }
                TerminalInfo4MapBean.ObjBean objBean = terminalInfo4MapBean.getObj();
                TerminalInfo4MapBean.ObjBean.RedisobjBean redisobjBean = objBean.getRedisobj();
                mNaviLatLngEnd = new NaviLatLng(redisobjBean.getLatitudeF(), redisobjBean.getLongitudeF());
                myHandler.sendEmptyMessage(Data.MSG_3);
            }

            @Override
            public void onFailure() {
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
        if (null != myHandler) {
            myHandler.sendEmptyMessage(Data.MSG_1);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();
        if (null != mSpeechSynthesizer) {
            mSpeechSynthesizer.stopSpeaking();
        }
        if (null != myHandler) {
            myHandler.removeMessages(Data.MSG_1);
            myHandler.removeMessages(Data.MSG_2);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAMapNaviView.onDestroy();
        mAMapNavi.stopNavi();
        mAMapNavi.destroy();
        if (null != mSpeechSynthesizer) {
            mSpeechSynthesizer.destroy();
        }
    }

    @Override
    public void onInitNaviFailure() {
        Log.i(TAG, "onInitNaviFailure: ");
    }

    @Override
    public void onInitNaviSuccess() {
        Log.i(TAG, "onInitNaviSuccess: ");

        if (null != mNaviLatLngEnd) {
            int strategy = 0;
            try {
                strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            List<NaviLatLng> eList = new ArrayList<>();

            eList.add(mNaviLatLngEnd);
            mAMapNavi.calculateDriveRoute(eList, null, strategy);
        }
    }

    @Override
    public void onStartNavi(int i) {
        Log.i(TAG, "onStartNavi: ");
    }

    @Override
    public void onTrafficStatusUpdate() {
        Log.i(TAG, "onTrafficStatusUpdate: ");
    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
        Log.i(TAG, "onLocationChange: ");
    }

    @Override
    public void onGetNavigationText(int i, String s) {
        Log.i(TAG, "onGetNavigationText: ");
    }

    @Override
    public void onGetNavigationText(String s) {
        Log.i(TAG, "onGetNavigationText: ");
        mSpeechSynthesizer.startSpeaking(s, NavigationActivity.this);
    }

    @Override
    public void onEndEmulatorNavi() {
        Log.i(TAG, "onEndEmulatorNavi: ");
    }

    @Override
    public void onArriveDestination() {
        Log.i(TAG, "onArriveDestination: ");
    }

    @Override
    public void onCalculateRouteFailure(int i) {
        Log.i(TAG, "onCalculateRouteFailure: ");
        mToastU.showToast("未找到导航路径，请重新尝试");
    }

    @Override
    public void onReCalculateRouteForYaw() {
        Log.i(TAG, "onReCalculateRouteForYaw: ");
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {
        Log.i(TAG, "onReCalculateRouteForTrafficJam: ");
    }

    @Override
    public void onArrivedWayPoint(int i) {
        Log.i(TAG, "onArrivedWayPoint: ");
    }

    @Override
    public void onGpsOpenStatus(boolean b) {
        Log.i(TAG, "onGpsOpenStatus: ");
    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {
        Log.i(TAG, "onNaviInfoUpdate: ");
    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {
        Log.i(TAG, "onNaviInfoUpdated: ");
    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {
        Log.i(TAG, "updateCameraInfo: ");
    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {
        Log.i(TAG, "onServiceAreaUpdate: ");
    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
        Log.i(TAG, "showCross: ");
    }

    @Override
    public void hideCross() {
        Log.i(TAG, "hideCross: ");
    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {
        Log.i(TAG, "showLaneInfo: ");
    }

    @Override
    public void hideLaneInfo() {
        Log.i(TAG, "hideLaneInfo: ");
    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        Log.i(TAG, "onCalculateRouteSuccess: ");
        mAMapNavi.startNavi(NaviType.GPS);
//        mAMapNavi.startNavi(NaviType.EMULATOR);
    }

    @Override
    public void notifyParallelRoad(int i) {
        Log.i(TAG, "notifyParallelRoad: ");
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
        Log.i(TAG, "OnUpdateTrafficFacility: ");
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {
        Log.i(TAG, "OnUpdateTrafficFacility: ");
    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {
        Log.i(TAG, "OnUpdateTrafficFacility: ");
    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
        Log.i(TAG, "updateAimlessModeStatistics: ");
    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {
        Log.i(TAG, "updateAimlessModeCongestionInfo: ");
    }

    @Override
    public void onPlayRing(int i) {
        Log.i(TAG, "onPlayRing: ");
    }

    //====================================分割线========================================

    @Override
    public void onNaviSetting() {
        Log.i(TAG, "onNaviSetting: ");
    }

    @Override
    public void onNaviCancel() {
        Log.i(TAG, "onNaviCancel: ");
    }

    @Override
    public boolean onNaviBackClick() {
        Log.i(TAG, "onNaviBackClick: ");
        return false;
    }

    @Override
    public void onNaviMapMode(int i) {
        Log.i(TAG, "onNaviMapMode: ");
    }

    @Override
    public void onNaviTurnClick() {
        Log.i(TAG, "onNaviTurnClick: ");
    }

    @Override
    public void onNextRoadClick() {
        Log.i(TAG, "onNextRoadClick: ");
    }

    @Override
    public void onScanViewButtonClick() {
        Log.i(TAG, "onScanViewButtonClick: ");
    }

    @Override
    public void onLockMap(boolean b) {
        Log.i(TAG, "onLockMap: ");
    }

    @Override
    public void onNaviViewLoaded() {
        Log.i(TAG, "onNaviViewLoaded: ");
    }

    //========================================分割线==========================================
    @Override
    public void onSpeakBegin() {
    }

    @Override
    public void onBufferProgress(int i, int i1, int i2, String s) {
    }

    @Override
    public void onSpeakPaused() {
    }

    @Override
    public void onSpeakResumed() {
    }

    @Override
    public void onSpeakProgress(int i, int i1, int i2) {
    }

    @Override
    public void onCompleted(SpeechError speechError) {
    }

    @Override
    public void onEvent(int i, int i1, int i2, Bundle bundle) {
    }

    private void reRoad(NaviLatLng latLngEnd) {
        int strategy = 0;
        try {
            strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<NaviLatLng> eList = new ArrayList<>();
        eList.add(latLngEnd);
        mAMapNavi.calculateDriveRoute(eList, null, strategy);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Data.MSG_MSG: {
                    break;
                }
                case Data.MSG_NOTHING: {
                    break;
                }
                case Data.MSG_ERO: {
                    break;
                }
                case Data.MSG_1: {
                    //  重新获取终点
                    mNetManager.showTerminalInfo4Map(mToken, mImei);
                    myHandler.sendEmptyMessageDelayed(Data.MSG_1, FLUSH_TIME);
                    break;
                }
                case Data.MSG_3: {
                    //  重新获取终点成功
                    mSpeechSynthesizer.startSpeaking("目标车辆移动，将重新归划路径", NavigationActivity.this);
                    myHandler.sendEmptyMessageDelayed(Data.MSG_2, DELAY);
                    break;
                }
                case Data.MSG_2: {
                    //  延时刷新位置
                    reRoad(mNaviLatLngEnd);
                    break;
                }
            }
        }
    }
}
