package com.diyuewang.m.tools;

import com.tencent.bugly.crashreport.CrashReport;

public class BuglyErrorManager {

    public static void postIMEIError(String content) {
        CrashReport.postCatchedException(new BuglyError("IMEI:"+content));
    }
}
