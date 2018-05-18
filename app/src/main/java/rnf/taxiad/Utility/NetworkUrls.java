package rnf.taxiad.Utility;

/**
 * Created by Rahil on 18/1/16.
 */
public final class NetworkUrls {

    /**
     * DOMAIN
     */

//    private static final String DOMAIN = "http://labs.agilistechlabs.com/";


    /*debug url*/
//    private static final String DOMAIN = "http://manage.rideplay.tv/"; //prod Url
//    private static final String DOMAIN = "http://dev.manage.rideplay.tv/"; //dev Url



   /*live url*/
    //private static final String DOMAIN = "http://manage.rideplay.tv/rideplay/";
    private static final String DOMAIN = "http://manage.rideplay.tv/";


    /**
     * BASE URL
     */

//    private static final String BASE = DOMAIN + "Innertube1/api/";

//    private static final String BASE = DOMAIN + "Innertube/api/";

    private static final String BASE = DOMAIN + "api/";

    /**
     * LOGIN URL
     */

    public static final String LOGIN = BASE + "login";

    /**
     * FORGOT PASSWORD URL
     */

    public static final String FORGOT_PASSWORD = BASE + "forgetPassword";

    /**
     * SIGNUP URL
     */

    public static final String SIGNUP = BASE + "addDriver";

    /**
     * UPDATE PROFILE URL
     */

    public static final String UPDATE_PROFILE = BASE + "updateProfile";

    /**
     * GET ADS URL
     */

    public static final String GET_ADS = BASE + "ads";

    /**
     * GET PROFILE DATA URL
     */

    public static final String GET_PROFILE = BASE + "getUserProfile";

    /**
     * ADS STATS URL
     */

//    public static final String DOWNLOADED_STATS = BASE + "adStats";
//    public static final String DOWNLOADED_STATS = BASE + "adStatsTest";

    public static final String DOWNLOADED_STATS = BASE + "updateHourlyDistanceDuration";

    /**
     * DISPLAY ADS URL
     */

    public static final String ADS_STATS = BASE + "updateHourlyState";

//    public static final String ADS_STATS = BASE + "adsDisplayTest";

    /**
     * SEND MAIL TO ADMIN
     */

    public static final String SEND_MAIL = BASE + "sendMailToAdmin";

    /**
     * Send Ghost Car Broadcast
     */

//    public static final String GHOST_CAR = " http://162.243.68.86:7379/PUBLISH/innertube/";

    public static final String GHOST_CAR = "http://manage.rideplay.tv:7379/PUBLISH/rideplay/";


    /**
     * Text To Feature
     */

    public static final String TEXT_TO_FEATURE = BASE + "textToFeature";


    /**
     * Send Checkout Link
     */

    public static final String SEND_CHECKOUT = BASE + "textToCheckout";

}
