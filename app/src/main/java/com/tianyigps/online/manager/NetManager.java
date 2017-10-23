package com.tianyigps.online.manager;

import android.util.Log;

import com.tianyigps.online.data.Urls;
import com.tianyigps.online.interfaces.OnAddAttentionListener;
import com.tianyigps.online.interfaces.OnAttentionListListener;
import com.tianyigps.online.interfaces.OnCheckUserListener;
import com.tianyigps.online.interfaces.OnCheckVersionListener;
import com.tianyigps.online.interfaces.OnDelAttentionListener;
import com.tianyigps.online.interfaces.OnFeedbackListener;
import com.tianyigps.online.interfaces.OnFindHisPointsListener;
import com.tianyigps.online.interfaces.OnGetGroupListener;
import com.tianyigps.online.interfaces.OnGetNumWithStatusListener;
import com.tianyigps.online.interfaces.OnGetStationInfoListener;
import com.tianyigps.online.interfaces.OnGetTerminalByGroupListener;
import com.tianyigps.online.interfaces.OnGetUnifenceStatusListener;
import com.tianyigps.online.interfaces.OnGetWarnListListener;
import com.tianyigps.online.interfaces.OnRefreshTerminalListListener;
import com.tianyigps.online.interfaces.OnSearchTerminalWithStatusListener;
import com.tianyigps.online.interfaces.OnShowCustomersListener;
import com.tianyigps.online.interfaces.OnShowPointNewListener;
import com.tianyigps.online.interfaces.OnShowTerminalInfoForMapListener;
import com.tianyigps.online.interfaces.OnShowTerminalInfoListener;
import com.tianyigps.online.interfaces.OnUnifenceInfoListener;
import com.tianyigps.online.interfaces.OnUnifenceOprListener;
import com.tianyigps.online.interfaces.OnUnifenceUpsertListener;
import com.tianyigps.online.utils.StringU;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by cookiemouse on 2017/9/4.
 */

public class NetManager {
    private static final String TAG = "NetManager";

    private OkHttpClient mOkHttpClient;
    private Request mRequest;

    private OnCheckUserListener mOnCheckUserListener;
    private OnCheckVersionListener mOnCheckVersionListener;
    private OnShowCustomersListener mOnShowCustomersListener;
    private OnAttentionListListener mOnAttentionListListener;
    private OnAddAttentionListener mOnAddAttentionListener;
    private OnDelAttentionListener mOnDelAttentionListener;
    private OnFeedbackListener mOnFeedbackListener;
    private OnShowTerminalInfoListener mOnShowTerminalInfoListener;
    private OnFindHisPointsListener mOnFindHisPointsListener;
    private OnGetNumWithStatusListener mOnGetNumWithStatusListener;
    private OnGetGroupListener mOnGetGroupListener;
    private OnGetTerminalByGroupListener mOnGetTerminalByGroupListener;
    private OnRefreshTerminalListListener mOnRefreshTerminalListListener;
    private OnShowPointNewListener mOnShowPointNewListener;
    private OnSearchTerminalWithStatusListener mOnSearchTerminalWithStatusListener;
    private OnShowTerminalInfoForMapListener mOnShowTerminalInfoForMapListener;
    private OnGetWarnListListener mOnGetWarnListListener;
    private OnGetUnifenceStatusListener mOnGetUnifenceStatusListener;
    private OnUnifenceOprListener mOnUnifenceOprListener;
    private OnUnifenceInfoListener mOnUnifenceInfoListener;
    private OnUnifenceUpsertListener mOnUnifenceUpsertListener;
    private OnGetStationInfoListener mOnGetStationInfoListener;

    public NetManager() {
        mOkHttpClient = new OkHttpClient
                .Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    /**
     * @param userName 用户名、账户
     * @param password 密码
     */
    public void login(String userName, String password) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.CHECK_USER + "userName=" + userName
                + "&password=" + password);
        mRequest = builder.build();
        Log.i(TAG, "checkUser: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnCheckUserListener) {
                    throw new NullPointerException("OnCheckUserListener is null");
                }
                mOnCheckUserListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnCheckUserListener) {
                    throw new NullPointerException("OnCheckUserListener is null");
                }
                mOnCheckUserListener.onSuccess(response.body().string());
            }
        });
    }

    /**
     * 设置登录回调
     */
    public void setOnCheckUserListener(OnCheckUserListener listener) {
        this.mOnCheckUserListener = listener;
    }

    /**
     * 获取最新版本号
     *
     * @param version 当前版本号
     */
    public void checkVersion(String version) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.CHECK_VERSION + "version=" + version);
        mRequest = builder.build();
        Log.i(TAG, "checkVersion: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnCheckVersionListener) {
                    throw new NullPointerException("OnCheckVersionListener is null");
                }
                mOnCheckVersionListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnCheckVersionListener) {
                    throw new NullPointerException("OnCheckVersionListener is null");
                }
                mOnCheckVersionListener.onSuccess(response.body().string());
            }
        });
    }

    public void setCheckVersionListener(OnCheckVersionListener listener) {
        this.mOnCheckVersionListener = listener;
    }

    /**
     * 获取下级客户
     *
     * @param token 令牌
     * @param cid
     */
    public void showCustomers(String token, int cid) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.SHOW_CUSTOMERS + "token=" + token
                + "&cid=" + cid);
        mRequest = builder.build();
        Log.i(TAG, "showCustomers: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnShowCustomersListener) {
                    throw new NullPointerException("OnShowCustomersListener is null");
                }
                mOnShowCustomersListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnShowCustomersListener) {
                    throw new NullPointerException("OnShowCustomersListener is null");
                }
                mOnShowCustomersListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnShowCustomersListener(OnShowCustomersListener listener) {
        this.mOnShowCustomersListener = listener;
    }

    /**
     * 获取关注列表
     *
     * @param token 令牌
     * @param cid
     */
    public void getAttentionList(String token, int cid) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.ATTENTION_LIST + "token=" + token
                + "&cid=" + cid);
        mRequest = builder.build();
        Log.i(TAG, "getAttentionList: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnAttentionListListener) {
                    throw new NullPointerException("OnAttentionListListener is null");
                }
                mOnAttentionListListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnAttentionListListener) {
                    throw new NullPointerException("OnAttentionListListener is null");
                }
                mOnAttentionListListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnAttentionListListener(OnAttentionListListener listener) {
        this.mOnAttentionListListener = listener;
    }

    /**
     * 新增关注
     *
     * @param token 令牌
     * @param cid
     * @param imei
     */
    public void addAttention(String token, int cid, String imei) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.ADD_ATTENTION + "token=" + token
                + "&cid=" + cid
                + "&imei=" + imei);
        mRequest = builder.build();
        Log.i(TAG, "addAttention: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnAddAttentionListener) {
                    throw new NullPointerException("OnAddAttentionListener is null");
                }
                mOnAddAttentionListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnAddAttentionListener) {
                    throw new NullPointerException("OnAddAttentionListener is null");
                }
                mOnAddAttentionListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnAddAttentionListener(OnAddAttentionListener listener) {
        this.mOnAddAttentionListener = listener;
    }

    /**
     * 取消关注
     *
     * @param token 令牌
     * @param cid
     * @param imei
     */
    public void delAttention(String token, int cid, String imei) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.DEL_ATTENTION + "token=" + token
                + "&cid=" + cid
                + "&imei=" + imei);
        mRequest = builder.build();
        Log.i(TAG, "delAttention: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnDelAttentionListener) {
                    throw new NullPointerException("OnDelAttentionListener is null");
                }
                mOnDelAttentionListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnDelAttentionListener) {
                    throw new NullPointerException("OnDelAttentionListener is null");
                }
                mOnDelAttentionListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnDelAttentionListener(OnDelAttentionListener listener) {
        this.mOnDelAttentionListener = listener;
    }

    /**
     * 意见反馈
     *
     * @param token   令牌
     * @param cid
     * @param content 反馈内容
     */
    public void feedback(String token, int cid, String content) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.FEEDBACK + "token=" + token
                + "&cid=" + cid
                + "&content=" + content);
        mRequest = builder.build();
        Log.i(TAG, "feedback: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnFeedbackListener) {
                    throw new NullPointerException("OnFeedbackListener is null");
                }
                mOnFeedbackListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnFeedbackListener) {
                    throw new NullPointerException("OnFeedbackListener is null");
                }
                mOnFeedbackListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnFeedbackListener(OnFeedbackListener listener) {
        this.mOnFeedbackListener = listener;
    }

    /**
     * 获取设备信息
     *
     * @param token
     * @param imei
     */
    public void showTerminalInfo(String token, String imei) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.SHOW_TERMINAL_INFO + "token=" + token
                + "&imei=" + imei);
        mRequest = builder.build();
        Log.i(TAG, "showTerminalInfo: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnShowTerminalInfoListener) {
                    throw new NullPointerException("OnShowTerminalInfoListener is null");
                }
                mOnShowTerminalInfoListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnShowTerminalInfoListener) {
                    throw new NullPointerException("OnShowTerminalInfoListener is null");
                }
                mOnShowTerminalInfoListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnShowTerminalInfoListener(OnShowTerminalInfoListener listener) {
        this.mOnShowTerminalInfoListener = listener;
    }

    /**
     * 获取设备回放信息
     *
     * @param token
     * @param imei
     * @param startData 开始时间
     * @param endData   结束时间
     */
    public void findHisPoints(String token, String imei, String startData, String endData) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.FIND_HIS_POINTS + "token=" + token
                + "&imei=" + imei
                + "&startDate=" + startData
                + "&endDate=" + endData);
        mRequest = builder.build();
        Log.i(TAG, "findHisPoints: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnFindHisPointsListener) {
                    throw new NullPointerException("OnFindHisPointsListener is null");
                }
                mOnFindHisPointsListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnFindHisPointsListener) {
                    throw new NullPointerException("OnFindHisPointsListener is null");
                }
                mOnFindHisPointsListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnFindHisPointsListener(OnFindHisPointsListener listener) {
        this.mOnFindHisPointsListener = listener;
    }

    /**
     * 查询各个状态的终端数量
     *
     * @param token
     * @param cid
     * @param selectCid
     */
    public void getNumWithStatus(String token, int cid, int selectCid) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.GET_NUM_WITH_STATUS + "token=" + token
                + "&cid=" + cid
                + "&selectCid=" + selectCid);
        mRequest = builder.build();
        Log.i(TAG, "getNumWithStatus: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnGetNumWithStatusListener) {
                    throw new NullPointerException("OnGetNumWithStatusListener is null");
                }
                mOnGetNumWithStatusListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnGetNumWithStatusListener) {
                    throw new NullPointerException("OnGetNumWithStatusListener is null");
                }
                mOnGetNumWithStatusListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnGetNumWithStatusListener(OnGetNumWithStatusListener listener) {
        this.mOnGetNumWithStatusListener = listener;
    }

    /**
     * 根据cid和status获取分组
     *
     * @param token
     * @param cid
     * @param selectCid
     */
    public void getGroup(String token, int cid, int selectCid) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.GET_GROUP + "token=" + token
                + "&cid=" + cid
                + "&selectCid=" + selectCid);
        mRequest = builder.build();
        Log.i(TAG, "getGroup: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnGetGroupListener) {
                    throw new NullPointerException("OnGetGroupListener is null");
                }
                mOnGetGroupListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnGetGroupListener) {
                    throw new NullPointerException("OnGetGroupListener is null");
                }
                mOnGetGroupListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnGetGroupListener(OnGetGroupListener listener) {
        this.mOnGetGroupListener = listener;
    }

    /**
     * 根据groupId、status获取设备
     *
     * @param token
     * @param cid
     * @param selectCid
     * @param gid       分组id
     * @param status    状态（所有、在线、离线）
     */
    public void getTerminalByGroup(String token, int cid, int selectCid, int gid, int status) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.GET_TERMINAL_BY_GROUP_ID + "token=" + token
                + "&cid=" + cid
                + "&selectCid=" + selectCid
                + "&gid=" + gid
                + "&status=" + status);
        mRequest = builder.build();
        Log.i(TAG, "getTerminalByGroup: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnGetTerminalByGroupListener) {
                    throw new NullPointerException("OnGetTerminalByGroupListener is null");
                }
                mOnGetTerminalByGroupListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnGetTerminalByGroupListener) {
                    throw new NullPointerException("OnGetTerminalByGroupListener is null");
                }
                mOnGetTerminalByGroupListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnGetTerminalByGroupListener(OnGetTerminalByGroupListener listener) {
        this.mOnGetTerminalByGroupListener = listener;
    }

    /**
     * 刷新设备状态
     *
     * @param token
     * @param cid
     * @param imeiStr 需要刷新的imei号，用“，”作分隔
     */
    public void refreshTerminalList(String token, int cid, String imeiStr) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.REFRESH_TERMINAL_LIST + "token=" + token
                + "&cid=" + cid
                + "&imeis=" + imeiStr);
        mRequest = builder.build();
        Log.i(TAG, "refreshTerminalList: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnRefreshTerminalListListener) {
                    throw new NullPointerException("OnRefreshTerminalListListener is null");
                }
                mOnRefreshTerminalListListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnRefreshTerminalListListener) {
                    throw new NullPointerException("OnRefreshTerminalListListener is null");
                }
                mOnRefreshTerminalListListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnRefreshTerminalListListener(OnRefreshTerminalListListener listener) {
        this.mOnRefreshTerminalListListener = listener;
    }

    /**
     * 查询终端、绘制地图
     *
     * @param token
     * @param cid
     * @param cidStr
     * @param imeiStr
     * @param attention
     */
    public void showPointNew(String token, int cid, String cidStr, String imeiStr, boolean attention) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.SHOW_POINT_NEW + "token=" + token
                + "&cid=" + cid
                + "&cidStr=" + cidStr
                + "&imeiStr=" + imeiStr
                + "&attention=" + attention);
        mRequest = builder.build();
        Log.i(TAG, "showPointNew: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnShowPointNewListener) {
                    throw new NullPointerException("OnShowPointNewListener is null");
                }
                mOnShowPointNewListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnShowPointNewListener) {
                    throw new NullPointerException("OnShowPointNewListener is null");
                }
                mOnShowPointNewListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnShowPointNewListener(OnShowPointNewListener listener) {
        this.mOnShowPointNewListener = listener;
    }

    /**
     * 查询设备信息
     *
     * @param token
     * @param cid
     * @param condition 搜索条件
     *                  //     * @param pageNum   页码，可不填
     *                  //     * @param pageSize  每页数量，可不填
     */
//    public void searchTerminalWithStatus(String token, int cid, String condition, int pageNum, int pageSize) {
    public void searchTerminalWithStatus(String token, int cid, String condition) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.SEARCH_TERMINAL_WITH_STATUS + "token=" + token
                + "&cid=" + cid
                + "&condition=" + condition
                + "&pageNum="
                + "&pageSize=");
        mRequest = builder.build();
        Log.i(TAG, "searchTerminalWithStatus: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnSearchTerminalWithStatusListener) {
                    throw new NullPointerException("OnSearchTerminalWithStatusListener is null");
                }
                mOnSearchTerminalWithStatusListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnSearchTerminalWithStatusListener) {
                    throw new NullPointerException("OnSearchTerminalWithStatusListener is null");
                }
                mOnSearchTerminalWithStatusListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnSearchTerminalWithStatusListener(OnSearchTerminalWithStatusListener listener) {
        this.mOnSearchTerminalWithStatusListener = listener;
    }

    /**
     * 获取设备定位信息
     *
     * @param token
     * @param imei
     */
    public void showTerminalInfo4Map(String token, String imei) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.SHOW_TERMINAL_INFO_FOR_MAP + "token=" + token
                + "&imei=" + imei);
        mRequest = builder.build();
        Log.i(TAG, "showTerminalInfo4Map: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnShowTerminalInfoForMapListener) {
                    throw new NullPointerException("OnShowTerminalInfoForMapListener is null");
                }
                mOnShowTerminalInfoForMapListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnShowTerminalInfoForMapListener) {
                    throw new NullPointerException("OnShowTerminalInfoForMapListener is null");
                }
                mOnShowTerminalInfoForMapListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnShowTerminalInfoForMapListener(OnShowTerminalInfoForMapListener listener) {
        this.mOnShowTerminalInfoForMapListener = listener;
    }

    /**
     * 查询报警
     *
     * @param token
     * @param cid
     * @param warnTypes
     * @param lastId
     * @param condition
     */
    public void getWarnList(String token, int cid, String warnTypes, String lastId, String condition) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.GET_WARN_LIST + "token=" + token
                + "&cid=" + cid
                + "&warnTypes=" + warnTypes
                + "&lastId=" + lastId
                + "&condition=" + condition);
        mRequest = builder.build();
        Log.i(TAG, "getWarnList: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnGetWarnListListener) {
                    throw new NullPointerException("OnGetWarnListListener is null");
                }
                mOnGetWarnListListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnGetWarnListListener) {
                    throw new NullPointerException("OnGetWarnListListener is null");
                }
                mOnGetWarnListListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnGetWarnListListener(OnGetWarnListListener listener) {
        this.mOnGetWarnListListener = listener;
    }

    /**
     * 查看围栏状态
     *
     * @param imei
     */
    public void getUnifenceStatus(String imei) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.UNIFENCE_STATUS + "imei=" + imei);
        mRequest = builder.build();
        Log.i(TAG, "getUnifenceStatus: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnGetUnifenceStatusListener) {
                    throw new NullPointerException("OnGetUnifenceStatusListener is null");
                }
                mOnGetUnifenceStatusListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnGetUnifenceStatusListener) {
                    throw new NullPointerException("OnGetUnifenceStatusListener is null");
                }
                mOnGetUnifenceStatusListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnGetUnifenceStatusListener(OnGetUnifenceStatusListener listener) {
        this.mOnGetUnifenceStatusListener = listener;
    }

    /**
     * 停启围栏
     *
     * @param imei
     * @param opr  1 为启用，2 为停用
     */
    public void unifenceOpr(String imei, int opr) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.UNIFENCE_OPR + "imei=" + imei
                + "&opr=" + opr);
        mRequest = builder.build();
        Log.i(TAG, "unifenceOpr: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnUnifenceOprListener) {
                    throw new NullPointerException("OnUnifenceOprListener is null");
                }
                mOnUnifenceOprListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnUnifenceOprListener) {
                    throw new NullPointerException("OnUnifenceOprListener is null");
                }
                mOnUnifenceOprListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnUnifenceOprListener(OnUnifenceOprListener listener) {
        this.mOnUnifenceOprListener = listener;
    }

    /**
     * 查看围栏信息
     *
     * @param imei
     */
    public void unifenceInfo(String imei) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.UNIFENCE_INFO + "imei=" + imei);
        mRequest = builder.build();
        Log.i(TAG, "unifenceInfo: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnUnifenceInfoListener) {
                    throw new NullPointerException("OnUnifenceInfoListener is null");
                }
                mOnUnifenceInfoListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnUnifenceInfoListener) {
                    throw new NullPointerException("OnUnifenceInfoListener is null");
                }
                mOnUnifenceInfoListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnUnifenceInfoListener(OnUnifenceInfoListener listener) {
        this.mOnUnifenceInfoListener = listener;
    }

    /**
     * 新增围栏
     *
     * @param imei
     * @param type   围栏类型，1 圆形， 2 多边形
     * @param point  圆心，数组[lng,lat],多边形传[]
     * @param radius 半径，多边形传null
     * @param points 围栏点，首尾相连
     */
    public void unifenceUpsert(String imei, int type, String point, String radius, String points) {
        Request.Builder builder = new Request.Builder();
        builder.url(Urls.UNIFENCE_UPSERT + "imei=" + imei
                + "&type=" + type
                + "&point=" + point
                + "&radius=" + radius
                + "&points=" + points);
        mRequest = builder.build();
        Log.i(TAG, "unifenceUpsert: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnUnifenceUpsertListener) {
                    throw new NullPointerException("OnUnifenceUpsertListener is null");
                }
                mOnUnifenceUpsertListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnUnifenceUpsertListener) {
                    throw new NullPointerException("OnUnifenceUpsertListener is null");
                }
                mOnUnifenceUpsertListener.onSuccess(response.body().string());
            }
        });
    }

    public void unifenceUpsertPost(String json) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody requestBodyJson = RequestBody.create(JSON, json);
        mRequest = new Request.Builder()
                .url(Urls.UNIFENCE_UPSERT)
                .post(requestBodyJson)
                .build();

        Log.i(TAG, "uploadPic: url-->" + mRequest.url());

        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnUnifenceUpsertListener) {
                    throw new NullPointerException("OnUnifenceUpsertListener is null");
                }
                mOnUnifenceUpsertListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnUnifenceUpsertListener) {
                    throw new NullPointerException("OnUnifenceUpsertListener is null");
                }
                mOnUnifenceUpsertListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnUnifenceUpsertListener(OnUnifenceUpsertListener listener) {
        this.mOnUnifenceUpsertListener = listener;
    }

    /**
     * 获取基站
     */
    public void getStationInfo(String stationCode) {
        String lac = stationCode.substring(8, 12);
        String cell_id = stationCode.substring(12);
        String lacNum = StringU.str2HexStr(lac);
        String cellIdNum = StringU.str2HexStr(cell_id);

        Log.i(TAG, "getStationInfo: lac-->" + lac);
        Log.i(TAG, "getStationInfo: cell_id-->" + cell_id);
        Log.i(TAG, "getStationInfo: lacNum-->" + lacNum);
        Log.i(TAG, "getStationInfo: cellIdNum-->" + cellIdNum);

        Request.Builder builder = new Request.Builder();
        builder.url("http://vipapiproxy.haoservice.com/api/getlbs?mcc=460&mnc=0&key=128ade83cb924eb5bdd02ba9bc6e151f&type=1"
                + "&cell_id=" + cell_id
                + "&cellIdNum=" + cellIdNum
                + "&lac=" + lac
                + "&lacNum=" + lacNum);
        mRequest = builder.build();
        Log.i(TAG, "getStationInfo: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnGetStationInfoListener) {
                    throw new NullPointerException("OnGetStationInfoListener is null");
                }
                mOnGetStationInfoListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnGetStationInfoListener) {
                    throw new NullPointerException("OnGetStationInfoListener is null");
                }
                mOnGetStationInfoListener.onSuccess(response.body().string());
            }
        });
    }

    public void setOnGetStationInfoListener(OnGetStationInfoListener listener) {
        this.mOnGetStationInfoListener = listener;
    }

    /**
     * 上传用户信息
     */
    public void sendUserInfo(String imei
            , String phoneBrand
            , String phoneModel
            , String versionCode
            , String latitude
            , String longitude
            , String exception_msg
            , String loginName
            , String operateSystem) {
        Request.Builder builder = new Request.Builder();
        builder.url("http://121.43.178.183:8000/tyzx/main/postLoginInfo?"
                + "&mphone_imei=" + imei
                + "&mphone_brand=" + phoneBrand
                + "&mphone_mode=" + phoneModel
                + "&version=" + versionCode
                + "&latitude=" + latitude
                + "&longitude=" + longitude
                + "&exception_msg=" + exception_msg
                + "&loginName=" + loginName
                + "&appName=1"
                + "&operateSystem=" + operateSystem);
        mRequest = builder.build();
        Log.i(TAG, "getStationInfo: url-->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });
    }
}
