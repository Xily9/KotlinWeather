package com.xily.kotlinweather.model.bean

class BaiduLocationBean {

    var status: Int = 0
    var result: ResultBean? = null

    class ResultBean {
        var addressComponent: AddressComponentBean? = null

        class AddressComponentBean {
            /**
             * country : 中国
             * country_code : 0
             * country_code_iso : CHN
             * country_code_iso2 : CN
             * province : 福建省
             * city : 福州市
             * city_level : 2
             * district : 闽侯县
             * town :
             * adcode : 350121
             * street : G1501(福州绕城高速)
             * street_number :
             * direction :
             * distance :
             */

            var country: String? = null
            var country_code: Int = 0
            var country_code_iso: String? = null
            var country_code_iso2: String? = null
            var province: String? = null
            var city: String? = null
            var city_level: Int = 0
            var district: String? = null
            var town: String? = null
            var adcode: String? = null
            var street: String? = null
            var street_number: String? = null
            var direction: String? = null
            var distance: String? = null
        }
    }
}
