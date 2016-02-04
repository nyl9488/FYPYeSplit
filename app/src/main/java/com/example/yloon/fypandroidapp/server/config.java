package com.example.yloon.fypandroidapp.server;

/**
 * Created by YLoon on 30/12/2015.
 */
public class config {

    public static final String domain_name ="192.168.1.153";

    public static final String GET_IMAGE_URL="http://"+domain_name+":800/fyp_connect/getprofilepic.php?mid=";

    public static final String url_register = "http://"+domain_name+":800/fyp_connect/registermember.php";
    public static final String url_addgroup = "http://"+domain_name+":800/fyp_connect/addgroup.php";
    public static final String url_addbill = "http://"+domain_name+":800/fyp_connect/addbill.php";
    public static final String url_viewAllGroup = "http://"+domain_name+":800/fyp_connect/getAllGroup.php?mid=";
    public static final String url_viewBill = "http://"+domain_name+":800/fyp_connect/getbill.php?gid=";

    public static final String url_viewBillDetails = "http://"+domain_name+":800/fyp_connect/getbilldetail.php?bid=";

    public static final String url_viewGroup = "http://"+domain_name+":800/fyp_connect/getGroup.php?gid=";

    public static final String url_getMember = "http://"+domain_name+":800/fyp_connect/getMember.php?mid=";

    public static final String url_deletebill = "http://"+domain_name+":800/fyp_connect/deletebill.php";
    public static final String UPLOAD_URL = "http://"+domain_name+":800/fyp_connect/uploadpic.php";
    public static final String UPLOAD_KEY = "image";
    //save preference
    public static final String Email = "emailKey";
    public static final String Pass = "passKey";
    public static final String MemberID = "memberIDKey";


    public static final String KEY_GROUP_NAME = "name";
    public static final String KEY_GROUP_ID = "gid";
    public static final String KEY_GROUP_TYPE = "type";

    public static final String KEY_MEMBER_ID = "mid";

    public static final String KEY_BILL_NAME = "name";
    public static final String KEY_BILL_AMOUNT="amount";
    public static final String KEY_BILL_DATE="date";

    public static final String TAG_ID = "mid";
    public static final String TAG_NAME = "username";

    public static final String TAG_JSON_ARRAY="result";
}
