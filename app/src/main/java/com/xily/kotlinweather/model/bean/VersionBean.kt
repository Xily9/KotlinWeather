package com.xily.kotlinweather.model.bean

class VersionBean {

    /**
     * status : 0
     * data : {"version":5,"version_name":"Ver.2.0 beta2","download_url":"https://xilym.tk/apk/fzujwc.apk","version_force_update_under":0,"text":"测试更新","time":"2018-04-20"}
     */

    var status: Int = 0
    var data: DataBean? = null

    class DataBean {
        /**
         * version : 5
         * version_name : Ver.2.0 beta2
         * download_url : https://xilym.tk/apk/fzujwc.apk
         * version_force_update_under : 0
         * text : 测试更新
         * time : 2018-04-20
         */

        var version: Int = 0
        var version_name: String? = null
        var download_url: String? = null
        var version_force_update_under: Int = 0
        var text: String? = null
        var time: String? = null
    }
}
