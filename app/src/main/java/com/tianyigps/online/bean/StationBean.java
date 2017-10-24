package com.tianyigps.online.bean;

/**
 * Created by cookiemouse on 2017/10/24.
 */

public class StationBean {
    /**
     * location : {"address":{"region":"上海市","county":"宝山区","street":"张庙街道","street_number":"胡庄村","city":"上海市","country":"中国"},"addressDescription":"上海市宝山区蕴川路张庙街道胡庄村,呼玛五村东门西240米,南北高架路东15米","longitude":121.4380579,"latitude":31.349635,"accuracy":"1500"}
     * access_token : null
     * ErrCode : 0
     */

    private LocationBean location;
    private Object access_token;
    private String ErrCode;

    public LocationBean getLocation() {
        return location;
    }

    public void setLocation(LocationBean location) {
        this.location = location;
    }

    public Object getAccess_token() {
        return access_token;
    }

    public void setAccess_token(Object access_token) {
        this.access_token = access_token;
    }

    public String getErrCode() {
        return ErrCode;
    }

    public void setErrCode(String ErrCode) {
        this.ErrCode = ErrCode;
    }

    public static class LocationBean {
        /**
         * address : {"region":"上海市","county":"宝山区","street":"张庙街道","street_number":"胡庄村","city":"上海市","country":"中国"}
         * addressDescription : 上海市宝山区蕴川路张庙街道胡庄村,呼玛五村东门西240米,南北高架路东15米
         * longitude : 121.4380579
         * latitude : 31.349635
         * accuracy : 1500
         */

        private AddressBean address;
        private String addressDescription;
        private double longitude;
        private double latitude;
        private String accuracy;

        public AddressBean getAddress() {
            return address;
        }

        public void setAddress(AddressBean address) {
            this.address = address;
        }

        public String getAddressDescription() {
            return addressDescription;
        }

        public void setAddressDescription(String addressDescription) {
            this.addressDescription = addressDescription;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getAccuracy() {
            return accuracy;
        }

        public void setAccuracy(String accuracy) {
            this.accuracy = accuracy;
        }

        public static class AddressBean {
            /**
             * region : 上海市
             * county : 宝山区
             * street : 张庙街道
             * street_number : 胡庄村
             * city : 上海市
             * country : 中国
             */

            private String region;
            private String county;
            private String street;
            private String street_number;
            private String city;
            private String country;

            public String getRegion() {
                return region;
            }

            public void setRegion(String region) {
                this.region = region;
            }

            public String getCounty() {
                return county;
            }

            public void setCounty(String county) {
                this.county = county;
            }

            public String getStreet() {
                return street;
            }

            public void setStreet(String street) {
                this.street = street;
            }

            public String getStreet_number() {
                return street_number;
            }

            public void setStreet_number(String street_number) {
                this.street_number = street_number;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }
        }
    }
}
