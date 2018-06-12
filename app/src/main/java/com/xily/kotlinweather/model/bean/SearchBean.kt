package com.xily.kotlinweather.model.bean

class SearchBean {

    var heWeather6: List<HeWeather6Bean>? = null

    class HeWeather6Bean {
        /**
         * basic : [{"cid":"CN101010100","location":"北京","parent_city":"北京","admin_area":"北京","cnty":"中国","lat":"39.90498734","lon":"116.4052887","tz":"+8.00","type":"city"},{"cid":"CN101210410","location":"北仑","parent_city":"宁波","admin_area":"浙江","cnty":"中国","lat":"29.90943909","lon":"121.83130646","tz":"+8.00","type":"city"},{"cid":"CN101301301","location":"北海","parent_city":"北海","admin_area":"广西","cnty":"中国","lat":"21.4733429","lon":"109.11925507","tz":"+8.00","type":"city"},{"cid":"CN101040800","location":"北碚","parent_city":"重庆","admin_area":"重庆","cnty":"中国","lat":"29.82542992","lon":"106.43786621","tz":"+8.00","type":"city"},{"cid":"CN101070706","location":"北镇","parent_city":"锦州","admin_area":"辽宁","cnty":"中国","lat":"41.59876251","lon":"121.79595947","tz":"+8.00","type":"city"}]
         * status : ok
         */

        var status: String? = null
        var basic: List<BasicBean>? = null

        class BasicBean {
            /**
             * cid : CN101010100
             * location : 北京
             * parent_city : 北京
             * admin_area : 北京
             * cnty : 中国
             * lat : 39.90498734
             * lon : 116.4052887
             * tz : +8.00
             * type : city
             */

            var cid: String? = null
            var location: String? = null
            var parent_city: String? = null
            var admin_area: String? = null
            var cnty: String? = null
            var lat: String? = null
            var lon: String? = null
            var tz: String? = null
            var type: String? = null
        }
    }
}
