package com.paynetone.counter.utils;

public class Constants {
    public static final String KEY_SHARE_PREFERENCES = "KEY_SHARE_PREFERENCES";
    public static final String KEY_EMPLOYEE = "KEY_EMPLOYEE";
    public static final String KEY_PAYNET = "KEY_PAYNET";
    public static final String KEY_ANDROID_PAYMENT_MODE = "ANDROID_PAYMENT_MODE";

    public static final String ANDROID_PAYMENT_MODE_HIDE = "HIDE";
    public static final String ANDROID_PAYMENT_MODE_SHOW = "SHOW";

    public static final String CHANNEL = "ANDROID";

    public static final String CMD_EMP_LOGIN = "EMP_LOGIN";
    public static final String CMD_ORDER_ADD_NEW = "ORDER_ADD_NEW";
    public static final String CMD_ORDER_GET_BY_CODE = "ORDER_GET_BY_CODE";
    public static final String CMD_ORDER_SEARCH = "ORDER_SEARCH";
    public static final String CMD_MERCHANT_GET_BALANCE = "MERCHANT_GET_BALANCE";
    public static final String CMD_PAYNET_GET_BY_ID = "PAYNET_GET_BY_ID";
    public static final String CMD_TRANS_WITHDRAW = "TRANS_WITHDRAW";
    public static final String CMD_DIC_GET_BANK = "DIC_GET_BANK";
    public static final String DIC_GET_WALLET = "DIC_GET_WALLET";
    public static final String CMD_TRANS_GET_OUTWARD = "TRANS_GET_OUTWARD";
    public static final String CMD_TRANS_WITHDRAW_SEARCH = "TRANS_WITHDRAW_SEARCH";
    public static final String CMD_EMP_ADD_NEW = "EMP_ADD_NEW";
    public static final String CMD_EMP_GET_OTP = "EMP_GET_OTP";
    public static final String CMD_DIC_GET_PARAMS = "DIC_GET_PARAMS";
    public static final String CMD_DIC_BUSINESS_SERVICES = "DIC_BUSINESS_SERVICES";
    public static final String CMD_DIC_GET_PROVINCE = "DIC_GET_PROVINCE";
    public static final String CMD_DIC_GET_DISTRICT = "DIC_GET_DISTRICT";
    public static final String CMD_DIC_GET_WARD = "DIC_GET_WARD";
    public static final String CMD_MERCHANT_ADD_NEW = "MERCHANT_ADD_NEW";
    public static final String CMD_MERCHANT_EDIT = "MERCHANT_EDIT";
    public static final String CMD_MERCHANT_GET_BY_MOBILE_NUMBER = "MERCHANT_GET_BY_MOBILE_NUMBER";
    public static final String CMD_UPDATE_PASSWORD_BY_OTP="EMP_UPDATE_PASSWORD_BY_OTP";

    public static final String WEB_VIEW_URL = "WEB_VIEW_URL";

    public static final String STATUS_W = "W";
    public static final String STATUS_S = "S";
    public static final String STATUS_C = "C";

    public static final String PROVIDER_VIETTEL = "VIETTEL";
    public static final String PROVIDER_ZALO = "ZALO";
    public static final String PROVIDER_SHOPPE = "SHOPEE";
    public static final String PROVIDER_VN_PAY = "VNPAY";
    public static final String PROVIDER_MOCA = "GRAB";
    public static final String PROVIDER_VIETQR = "VIETQR";

    public static final String HOME_QR = "HOME_QR";
    public static final String HOME_HISTORY = "HOME_HISTORY";

    public static final String AMOUNT_OUTWARD = "AMOUNT_OUTWARD";

    public static final String REGISTER_PASS_DATA = "REGISTER_PASS_DATA";
    public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;

    public static final String MERCHANT_MODE_ADD = "MERCHANT_MODE_ADD";
    public static final String MERCHANT_MODE_EDIT = "MERCHANT_MODE_EDIT";
    public static final String MERCHANT_MODE_VIEW = "MERCHANT_MODE_VIEW";

    public static final int PAYMENT_TYPE_SHOPEE=5;
    public static final int PAYMENT_TYPE_VIETTEL=3;
    public static final int PAYMENT_TYPE_ZALO=4;
    public static final int PAYMENT_TYPE_VN_PAY=6;
    public static final int PAYMENT_TYPE_MOCA =7;
    public static final int PAYMENT_TYPE_VIETQR =8;

    public static final String FORMALITY_TYPE_ONLINE = "1";
    public static final String FORMALITY_TYPE_OFFLINE = "2";

    public static final String WAITING_APPROVAL = "A";
    public static final String APPROVED= "P";
    public static final String REFUSE = "R";


    public static final int PNOLEVEL_STALL = 4; // t??i kho???n qu???y
    public static final int PNOLEVEL_MERCHANT = 1; // t??i kho???n merchant

    public static final int   BUSINESS_TYPE_ENTERPRISE = 1; // doanh nghiep
    public static final int   BUSINESS_TYPE_HOUSEHOLD = 2; // h??? kinh doanh
    public static final int   BUSINESS_TYPE_PERSONAL = 3; //  c?? nh??n kinh doanh
    public static final int   BUSINESS_TYPE_VIETLOTT = 4; //c???a h??ng x??? s??? vietlott
    public static final int   BUSINESS_TYPE_SYNTHETIC = 5; // c???a h??ng x??? s??? t???ng h???p

    public static final int THRESHOLD_CLICK_TIME = 500;

    public static final String TYPE_GET_OTP_REGISTER_ACCOUNT="N";
    public static final String TYPE_GET_OTP_FORGOT_PASSWORD="Y";

    public static final String EXTRA_OPT ="EXTRA_OTP";

    public static final int PAYMENT_VIETTEL_PAY = 1;
    public static final int PAYMENT_ZALO_PAY = 2;
    public static final int PAYMENT_SHOPPE_PAY = 3;
    public static final int PAYMENT_VN_PAY = 4;
    public static final int PAYMENT_MOCA = 5;
    public static final int PAYMENT_VIETQR = 6;

    public static final int WITHDRAW_CATEGORY_BANK = 1;  // r??t ti???n ng??n h??ng
    public static final int WITHDRAW_CATEGORY_VIETLOTT = 2;  // r??t ti???n vietlott
    public static final int WITHDRAW_CATEGORY_WALLET = 3; // r??t ti???n v??

    public static final String NOTIFICATION_COUNT = "notification_count";
    public static final String NOTIFICATION_DATA_TRANSFER = "TF_NotificationData";

    // bank
    public static final int BANK_ID_BIDV = 1;
    public static final int BANK_ID_TECK = 2;
    public static final int BANK_ID_VIETCOM = 3;
    public static final int BANK_ID_VIETIN = 4;
    public static final int BANK_ID_AGRI = 5;
    public static final int BANK_MB=6;

    public static final String BANK_NAME_BIDV = "Ng??n h??ng TMCP ?????u t?? v?? Ph??t tri???n Vi???t Nam";
    public static final String BANK_NAME_TECK = "Ng??n h??ng c??? ph???n th????ng m???i k??? th????ng";
    public static final String BANK_NAME_VIETCOM = "Ng??n h??ng TMCP Ngo???i Th????ng Vi???t Nam";
    public static final String BANK_NAME_VIETIN = "Ng??n h??ng TPCP C??ng Th????ng Vi???t Nam";
    public static final String BANK_NAME_AGRI = "Ng??n h??ng N??ng nghi???p v?? Ph??t tri???n Vi???t Nam";
    public static final String BANK_NAME_MB = "Ng??n h??ng Qu??n ?????i";

    // wallet

    public static final int WALLET_VIETTEL = 1;
    public static final int WALLET_ZALO = 2;
    public static final int WALLET_SHOPEE = 3;
    public static final int WALLET_MOMO = 4;



}
