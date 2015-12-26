package app.hoai.bkit4u.home4u.constant;

/**
 * Created by hoaipc on 10/22/15.
 */
public class AppConstant
{
    public final static String HOST = "http://bkitsmarthome.esy.es/v1";
//    public final static String HOST = "http://bkitsmarthome.grn.cc/v1/";
//    public final static String HOST = "http://bkitsmarthome.22web.org/v1";
    public final static String GET_ALL_DEVICES = "/device/all";
    public final static String GET_ALL_ROOMS = "/room/all";
    public final static String GET_ALL_EVENTS = "/event/all";
    public final static String GET_ALL_ACTIONS = "/action/all";

    public final static String GET_ALL_DEVICE_ROOMLESS = "/device/roomless";
    public final static String DEVICE_DETAIL = "/device/detail";
    public static final String ADD_EVENT = "/event/add";
    public static final String ADD_ROOM = "/room/add";
    public static final String ADD_ROOM_DEVICE = "/room/add/device";
    public static final String ADD_EVENT_ACTION = "/event/add/action";
    public static final String DEVICE_ACTION = "/device/action";
    public static final String EVENT_ACTION = "/event/action";
    public static final String EVENT_EXCUTE = "/event/excute";

    public final static String SEND_COMMAND = "/command/add";

    public static final String FIRE_BASE_ROOT = "https://shining-fire-4041.firebaseio.com/";
    public static final String DEVICES_REF = "https://shining-fire-4041.firebaseio.com/devices/";
    public static final String ROOMS_REF = "https://shining-fire-4041.firebaseio.com/rooms/";
    public static final String EVENTS_REF = "https://shining-fire-4041.firebaseio.com/events/";
    public static final String NOTIFICATIONS_REF = "https://shining-fire-4041.firebaseio.com/notifications/";
}
