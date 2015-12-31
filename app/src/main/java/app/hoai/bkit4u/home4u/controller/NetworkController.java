package app.hoai.bkit4u.home4u.controller;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import app.hoai.bkit4u.home4u.constant.AppConstant;
import app.hoai.bkit4u.home4u.listener.ResponseListener;
import app.hoai.bkit4u.home4u.listener.SendActionListener;
import app.hoai.bkit4u.home4u.model.BaseDeviceCollection;
import app.hoai.bkit4u.home4u.model.BaseDeviceModel;
import app.hoai.bkit4u.home4u.model.DeviceActionCollection;
import app.hoai.bkit4u.home4u.model.DeviceActionModel;
import app.hoai.bkit4u.home4u.model.HomeItemCollection;
import app.hoai.bkit4u.home4u.model.SpeechControlModel;
import app.hoai.bkit4u.home4u.model.type.HomeType;
import app.hoai.bkit4u.home4u.model.type.SpeechType;

/**
 * Created by hoaipc on 10/6/15.
 */
public class NetworkController
{
    private static RequestQueue mRequestQueue;
    private static NetworkController mInstance;
    private Context mContext;

    public static void createIntance(Context context)
    {
        if (mInstance == null)
        {
            mInstance = new NetworkController(context);
        }
    }

    public static NetworkController getInstance()
    {
        return mInstance;
    }

    private NetworkController(Context context)
    {
        mRequestQueue = Volley.newRequestQueue(context);
        this.mContext = context;
    }

    public String getMacAddress()
    {
        WifiManager manager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress();
        address = address.replace(":", "");
        return address;
    }

    public void getDeviceDetail(final ResponseListener<BaseDeviceModel> responseListener, String id)
    {
        String url = AppConstant.HOST + AppConstant.DEVICE_DETAIL;

        Log.d("Home4U-Url request", url + "/" + id);

        final Map<String, String> params = new HashMap<>();
        params.put("id", id);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.d("Home4U-Data response", response);
                try
                {
                    JSONObject json = new JSONObject(response);
                    if (json.getString("success").equals("1"))
                    {
                        Log.d("Home4U-Data response", json.toString());
                        BaseDeviceModel model = new BaseDeviceModel(json.getString("id"), json.getString("name"), json.getString("type"), json.getString("room_id"));
                        responseListener.onResponse(model);
                    }
                    else
                    {
                        responseListener.onError();
                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                responseListener.onError();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                return params;
            }
        };

        mRequestQueue.add(request);
    }

    public void addNewEvent(final ResponseListener<String> responseListener, String name)
    {
        String url = AppConstant.HOST + AppConstant.ADD_EVENT;

        Log.d("Home4U-Event request", url + "/" + name);

        final Map<String, String> params = new HashMap<>();
        params.put("name", name);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.d("Home4U-Event response", response);
                try
                {
                    JSONObject json = new JSONObject(response);
                    if (json.getString("success").equals("1"))
                    {
                        responseListener.onResponse(response);
                    }
                    else
                    {
                        responseListener.onError();
                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                responseListener.onError();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                return params;
            }
        };

        mRequestQueue.add(request);
    }

    public void addNewRoom(final ResponseListener<String> responseListener, String name)
    {
        String url = AppConstant.HOST + AppConstant.ADD_ROOM;

        Log.d("Home4U-Room request", url + "/" + name);

        final Map<String, String> params = new HashMap<>();
        params.put("name", name);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.d("Home4U-Room response", response);
                try
                {
                    JSONObject json = new JSONObject(response);
                    if (json.getString("success").equals("1"))
                    {
                        responseListener.onResponse(response);
                    }
                    else
                    {
                        responseListener.onError();
                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                responseListener.onError();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                return params;
            }
        };

        mRequestQueue.add(request);
    }

    public void getRoomCollection(final ResponseListener<HomeItemCollection> responseListener)
    {
        String url = AppConstant.HOST + AppConstant.GET_ALL_ROOMS;
        Log.d("Home4U-get all room", "url: " + url);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {

                JSONArray json;
                final HomeItemCollection collection;
                try
                {
                    json = response.getJSONArray("rooms");
                    collection = HomeItemCollection.makeFromJsonArray(json, HomeType.ROOM);
                    responseListener.onResponse(collection);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

                Log.d("Home4U-Response room", response.toString());
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.d("Home4U-error request", "!");
                error.printStackTrace();
                responseListener.onError();
            }
        });

        mRequestQueue.add(jsonRequest);
    }

    public void getEventCollection(final ResponseListener<HomeItemCollection> responseListener)
    {
        String url = AppConstant.HOST + AppConstant.GET_ALL_EVENTS;
        Log.d("Home4U-get all event", "url: " + url);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {

                JSONArray json;
                final HomeItemCollection collection;
                try
                {
                    json = response.getJSONArray("events");
                    collection = HomeItemCollection.makeFromJsonArray(json, HomeType.EVENT);
                    responseListener.onResponse(collection);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

                Log.d("Home4U-onResponse", response.toString());
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.d("Home4U-on error", "!");
                error.printStackTrace();
                responseListener.onError();
            }
        });

        mRequestQueue.add(jsonRequest);
    }

    public void addCommand(final SendActionListener listener, DeviceActionModel model)
    {
        String url = AppConstant.HOST + AppConstant.SEND_COMMAND;
        Log.d("Home4U-Send command", "url: " + url);


        final Map<String, String> params = new HashMap<>();
        params.put("action_id", model.getId());
        params.put("device_id", model.getDeviceId());

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.d("Home4U-Send command", "respone" + response);
                JSONObject json;
                try
                {
                    json = new JSONObject(response);

                    if (json.getString("success").equals("1")) listener.onSuccess();
                    else listener.onError();

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                listener.onError();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                return params;
            }
        };

        mRequestQueue.add(request);
    }

    public void getDeviceActions(final ResponseListener<DeviceActionCollection> listener, String id)
    {
        String url = AppConstant.HOST + AppConstant.DEVICE_ACTION;

        Log.d("Home4U-Url request", url + "/" + id);

        final Map<String, String> params = new HashMap<>();
        params.put("id", id);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.d("Home4U-Data response", response);
                try
                {
                    JSONObject json = new JSONObject(response);
                    if (json.getString("success").equals("1"))
                    {
                        JSONArray jsonArray = json.getJSONArray("actions");
                        DeviceActionCollection collection = DeviceActionCollection.makeFromJsonArray(jsonArray);
                        listener.onResponse(collection);
                    }
                    else
                    {
                        listener.onError();
                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                listener.onError();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                return params;
            }
        };

        mRequestQueue.add(request);
    }

    public void getDeviceRoomless(final ResponseListener<BaseDeviceCollection> responseListener)
    {
        String url = AppConstant.HOST + AppConstant.GET_ALL_DEVICE_ROOMLESS;
        Log.d("Home4U-request", "url: " + url);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {

                JSONArray json;
                final BaseDeviceCollection collection;
                try
                {
                    json = response.getJSONArray("devices");
                    collection = BaseDeviceCollection.makeFromJsonArray(json);
                    responseListener.onResponse(collection);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

                Log.d("Home4U-onResponse", response.toString());
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.d("Home4U-on error", "!");
                error.printStackTrace();
                responseListener.onError();
            }
        });

        mRequestQueue.add(jsonRequest);
    }

    public void addRoomDevice(final ResponseListener<String> responseListener, String deviceId, String roomId)
    {

        String url = AppConstant.HOST + AppConstant.ADD_ROOM_DEVICE;

        Log.d("Home4U-add room device", url);

        final Map<String, String> params = new HashMap<>();
        params.put("id", roomId);
        params.put("device_id", deviceId);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.d("Home4U-Event response", response);
                try
                {
                    JSONObject json = new JSONObject(response);
                    if (json.getString("success").equals("1"))
                    {
                        responseListener.onResponse(response);
                    }
                    else
                    {
                        responseListener.onError();
                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                responseListener.onError();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                return params;
            }
        };

        mRequestQueue.add(request);
    }

    public void getEventAction(final ResponseListener<DeviceActionCollection> listener, String id)
    {
        String url = AppConstant.HOST + AppConstant.EVENT_ACTION;

        Log.d("Home4U-Url request", url);

        final Map<String, String> params = new HashMap<>();
        params.put("id", id);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.d("Home4U-Data response", response);
                try
                {
                    JSONObject json = new JSONObject(response);
                    if (json.getString("success").equals("1"))
                    {
                        JSONArray jsonArray = json.getJSONArray("actions");
                        DeviceActionCollection collection = DeviceActionCollection.makeFromJsonArray(jsonArray);
                        listener.onResponse(collection);
                    }
                    else
                    {
                        listener.onError();
                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                listener.onError();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                return params;
            }
        };

        mRequestQueue.add(request);
    }

    public void getAllAction(final ResponseListener<DeviceActionCollection> listener)
    {
        String url = AppConstant.HOST + AppConstant.GET_ALL_ACTIONS;
        Log.d("Home4U-get all devices", "url: " + url);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response)
            {

                JSONArray json;
                final DeviceActionCollection collection;
                try
                {
                    if (response.getString("success").equals("1"))
                    {
                        json = response.getJSONArray("actions");
                        collection = DeviceActionCollection.makeFromJsonArray(json);
                        listener.onResponse(collection);
                    } else
                    {
                        listener.onError();
                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

                Log.d("Home4U-onResponse", response.toString());
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.d("Home4U-on error", "!");
                error.printStackTrace();
                listener.onError();
            }
        });

        mRequestQueue.add(jsonRequest);
    }

    public void addEventAction(final ResponseListener<String> responseListener, String actionId, String eventId)
    {

        String url = AppConstant.HOST + AppConstant.ADD_EVENT_ACTION;

        Log.d("Home4U-add event action", url);

        final Map<String, String> params = new HashMap<>();
        params.put("id", eventId);
        params.put("action_id", actionId);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.d("Home4U-Event response", response);
                try
                {
                    JSONObject json = new JSONObject(response);
                    if (json.getString("success").equals("1"))
                    {
                        responseListener.onResponse(response);
                    }
                    else
                    {
                        responseListener.onError();
                    }

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                responseListener.onError();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                return params;
            }
        };

        mRequestQueue.add(request);
    }

    public void excuteEvent(final SendActionListener listener, String eventId)
    {
        String url = AppConstant.HOST + AppConstant.EVENT_EXCUTE;
        Log.d("Home4U-Send event", "url: " + url);


        final Map<String, String> params = new HashMap<>();
        params.put("id", eventId);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Log.d("Home4U-Send command", "respone" + response);
                JSONObject json;
                try
                {
                    json = new JSONObject(response);

                    if (json.getString("success").equals("1")) listener.onSuccess();
                    else listener.onError();

                } catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener()
        {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                listener.onError();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                return params;
            }
        };

        mRequestQueue.add(request);
    }

    public void excuteSpeechControl(final SendActionListener listener, SpeechControlModel model)
    {
        if (model.getType() == SpeechType.EVENT)
        {
            excuteEvent(listener, model.getId());
        }
        else if (model.getType() == SpeechType.ACTION)
        {
            addCommand(listener, model.getActionModel());
        }
    }

    public void deleteEventAction(final ResponseListener<String> responseListener, String eventId, String actionId)
    {

    }

    public String getRequestDataGateWayString()
    {
        JSONObject json = new JSONObject();
        try
        {
            json.put("msgID",72);
            json.put("deviceID",getMacAddress());
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        return json.toString();
    }

    public String getBroadcastString()
    {
        JSONObject Json = new JSONObject();
        try
        {
            Json.put("msgID", 48);
            Json.put("deviceID", getMacAddress());
            Json.put("type", "android");
            Json.put("name", getMacAddress());
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        return Json.toString();
    }

    public String getAddActionString(String deviceId, String actionName)
    {
        JSONObject requestJson = new JSONObject();
        JSONObject targetJson = new JSONObject();
        try
        {
            requestJson.put("msgID",64);
            requestJson.put("deviceID",getMacAddress());

            targetJson.put("deviceID",deviceId);
            targetJson.put("name",actionName);

            requestJson.put("target",targetJson);

        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        return requestJson.toString();
    }

    public String getOfflineCommandString(String deviceId, String actionId)
    {
        JSONObject Json = new JSONObject();
        try
        {
            Json.put("msgID", 96);
            Json.put("deviceID", deviceId);
            Json.put("actionID", actionId);
        } catch (JSONException e)
        {
            e.printStackTrace();
        }

        return Json.toString();
    }
}
