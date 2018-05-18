package rnf.taxiad.models;

import java.util.List;

/**
 * Created by rnf-new on 14/12/16.
 */

public class LocationAddress {


    /**
     * results : [{"address_components":[{"long_name":"Jacob Dekema Freeway","short_name":"I-805","types":["route"]},{"long_name":"North Park","short_name":"North Park","types":["neighborhood","political"]},{"long_name":"San Diego","short_name":"San Diego","types":["locality","political"]},{"long_name":"San Diego County","short_name":"San Diego County","types":["administrative_area_level_2","political"]},{"long_name":"California","short_name":"CA","types":["administrative_area_level_1","political"]},{"long_name":"United States","short_name":"US","types":["country","political"]},{"long_name":"92104","short_name":"92104","types":["postal_code"]}],"formatted_address":"Jacob Dekema Freeway, San Diego, CA 92104, USA","geometry":{"bounds":{"northeast":{"lat":32.7497122,"lng":-117.1204574},"southwest":{"lat":32.7459267,"lng":-117.1231711}},"location":{"lat":32.7477512,"lng":-117.1219531},"location_type":"GEOMETRIC_CENTER","viewport":{"northeast":{"lat":32.7497122,"lng":-117.1204574},"southwest":{"lat":32.7459267,"lng":-117.1231711}}},"place_id":"ChIJg6rsi15U2YARXOVuSxdkUrg","types":["route"]},{"address_components":[{"long_name":"North Park","short_name":"North Park","types":["neighborhood","political"]},{"long_name":"San Diego","short_name":"San Diego","types":["locality","political"]},{"long_name":"San Diego County","short_name":"San Diego County","types":["administrative_area_level_2","political"]},{"long_name":"California","short_name":"CA","types":["administrative_area_level_1","political"]},{"long_name":"United States","short_name":"US","types":["country","political"]}],"formatted_address":"North Park, San Diego, CA, USA","geometry":{"bounds":{"northeast":{"lat":32.769124,"lng":-117.111662},"southwest":{"lat":32.721242,"lng":-117.1475291}},"location":{"lat":32.7456484,"lng":-117.1294166},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":32.769124,"lng":-117.111662},"southwest":{"lat":32.721242,"lng":-117.1475291}}},"place_id":"ChIJd7-0_fRU2YARPrISJ4CrfxE","types":["neighborhood","political"]},{"address_components":[{"long_name":"San Diego","short_name":"San Diego","types":["locality","political"]},{"long_name":"San Diego County","short_name":"San Diego County","types":["administrative_area_level_2","political"]},{"long_name":"California","short_name":"CA","types":["administrative_area_level_1","political"]},{"long_name":"United States","short_name":"US","types":["country","political"]}],"formatted_address":"San Diego, CA, USA","geometry":{"bounds":{"northeast":{"lat":33.114249,"lng":-116.90816},"southwest":{"lat":32.534856,"lng":-117.3097969}},"location":{"lat":32.715738,"lng":-117.1610838},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":33.114249,"lng":-116.90816},"southwest":{"lat":32.534856,"lng":-117.2821666}}},"place_id":"ChIJSx6SrQ9T2YARed8V_f0hOg0","types":["locality","political"]},{"address_components":[{"long_name":"92104","short_name":"92104","types":["postal_code"]},{"long_name":"San Diego","short_name":"San Diego","types":["locality","political"]},{"long_name":"San Diego County","short_name":"San Diego County","types":["administrative_area_level_2","political"]},{"long_name":"California","short_name":"CA","types":["administrative_area_level_1","political"]},{"long_name":"United States","short_name":"US","types":["country","political"]}],"formatted_address":"San Diego, CA 92104, USA","geometry":{"bounds":{"northeast":{"lat":32.7572911,"lng":-117.110284},"southwest":{"lat":32.7161159,"lng":-117.1473959}},"location":{"lat":32.7398671,"lng":-117.1205925},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":32.7572911,"lng":-117.110284},"southwest":{"lat":32.7161159,"lng":-117.1473959}}},"place_id":"ChIJ5SrdeIlU2YAR5_r4Ibu2I_0","types":["postal_code"]},{"address_components":[{"long_name":"San Diego Metropolitan Area","short_name":"San Diego Metropolitan Area","types":["political"]},{"long_name":"San Diego County","short_name":"San Diego County","types":["administrative_area_level_2","political"]},{"long_name":"California","short_name":"CA","types":["administrative_area_level_1","political"]},{"long_name":"United States","short_name":"US","types":["country","political"]}],"formatted_address":"San Diego Metropolitan Area, CA, USA","geometry":{"bounds":{"northeast":{"lat":33.5050284,"lng":-116.0809399},"southwest":{"lat":32.528832,"lng":-117.6110809}},"location":{"lat":33.0933809,"lng":-116.6081653},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":33.5050302,"lng":-116.0809399},"southwest":{"lat":32.528832,"lng":-117.6110809}}},"place_id":"ChIJl3eDNO7c24ARWg5B-UGAInU","types":["political"]},{"address_components":[{"long_name":"San Diego County","short_name":"San Diego County","types":["administrative_area_level_2","political"]},{"long_name":"California","short_name":"CA","types":["administrative_area_level_1","political"]},{"long_name":"United States","short_name":"US","types":["country","political"]}],"formatted_address":"San Diego County, CA, USA","geometry":{"bounds":{"northeast":{"lat":33.5050284,"lng":-116.0809399},"southwest":{"lat":32.528832,"lng":-117.6110809}},"location":{"lat":32.7157305,"lng":-117.1610966},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":33.5050302,"lng":-116.0809399},"southwest":{"lat":32.5342651,"lng":-117.5959108}}},"place_id":"ChIJHWD_IzDr24ARKAeA6yv9DTU","types":["administrative_area_level_2","political"]},{"address_components":[{"long_name":"California","short_name":"CA","types":["administrative_area_level_1","political"]},{"long_name":"United States","short_name":"US","types":["country","political"]}],"formatted_address":"California, USA","geometry":{"bounds":{"northeast":{"lat":42.0095169,"lng":-114.131211},"southwest":{"lat":32.528832,"lng":-124.482003}},"location":{"lat":36.778261,"lng":-119.4179324},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":42.009378,"lng":-114.131211},"southwest":{"lat":32.5344766,"lng":-124.415165}}},"place_id":"ChIJPV4oX_65j4ARVW8IJ6IJUYs","types":["administrative_area_level_1","establishment","point_of_interest","political"]},{"address_components":[{"long_name":"United States","short_name":"US","types":["country","political"]}],"formatted_address":"United States","geometry":{"bounds":{"northeast":{"lat":71.5388001,"lng":-66.885417},"southwest":{"lat":18.7763,"lng":170.5957}},"location":{"lat":37.09024,"lng":-95.712891},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":49.38,"lng":-66.94},"southwest":{"lat":25.82,"lng":-124.39}}},"place_id":"ChIJCzYy5IS16lQRQrfeQ5K5Oxw","types":["country","political"]}]
     * status : OK
     */

    private String status;
    private List<ResultsBean> results;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * address_components : [{"long_name":"Jacob Dekema Freeway","short_name":"I-805","types":["route"]},{"long_name":"North Park","short_name":"North Park","types":["neighborhood","political"]},{"long_name":"San Diego","short_name":"San Diego","types":["locality","political"]},{"long_name":"San Diego County","short_name":"San Diego County","types":["administrative_area_level_2","political"]},{"long_name":"California","short_name":"CA","types":["administrative_area_level_1","political"]},{"long_name":"United States","short_name":"US","types":["country","political"]},{"long_name":"92104","short_name":"92104","types":["postal_code"]}]
         * formatted_address : Jacob Dekema Freeway, San Diego, CA 92104, USA
         * geometry : {"bounds":{"northeast":{"lat":32.7497122,"lng":-117.1204574},"southwest":{"lat":32.7459267,"lng":-117.1231711}},"location":{"lat":32.7477512,"lng":-117.1219531},"location_type":"GEOMETRIC_CENTER","viewport":{"northeast":{"lat":32.7497122,"lng":-117.1204574},"southwest":{"lat":32.7459267,"lng":-117.1231711}}}
         * place_id : ChIJg6rsi15U2YARXOVuSxdkUrg
         * types : ["route"]
         */

        private String formatted_address;
        private GeometryBean geometry;
        private String place_id;
        private List<AddressComponentsBean> address_components;
        private List<String> types;

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public GeometryBean getGeometry() {
            return geometry;
        }

        public void setGeometry(GeometryBean geometry) {
            this.geometry = geometry;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public List<AddressComponentsBean> getAddress_components() {
            return address_components;
        }

        public void setAddress_components(List<AddressComponentsBean> address_components) {
            this.address_components = address_components;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public static class GeometryBean {
            /**
             * bounds : {"northeast":{"lat":32.7497122,"lng":-117.1204574},"southwest":{"lat":32.7459267,"lng":-117.1231711}}
             * location : {"lat":32.7477512,"lng":-117.1219531}
             * location_type : GEOMETRIC_CENTER
             * viewport : {"northeast":{"lat":32.7497122,"lng":-117.1204574},"southwest":{"lat":32.7459267,"lng":-117.1231711}}
             */

            private BoundsBean bounds;
            private LocationBean location;
            private String location_type;
            private ViewportBean viewport;

            public BoundsBean getBounds() {
                return bounds;
            }

            public void setBounds(BoundsBean bounds) {
                this.bounds = bounds;
            }

            public LocationBean getLocation() {
                return location;
            }

            public void setLocation(LocationBean location) {
                this.location = location;
            }

            public String getLocation_type() {
                return location_type;
            }

            public void setLocation_type(String location_type) {
                this.location_type = location_type;
            }

            public ViewportBean getViewport() {
                return viewport;
            }

            public void setViewport(ViewportBean viewport) {
                this.viewport = viewport;
            }

            public static class BoundsBean {
                /**
                 * northeast : {"lat":32.7497122,"lng":-117.1204574}
                 * southwest : {"lat":32.7459267,"lng":-117.1231711}
                 */

                private NortheastBean northeast;
                private SouthwestBean southwest;

                public NortheastBean getNortheast() {
                    return northeast;
                }

                public void setNortheast(NortheastBean northeast) {
                    this.northeast = northeast;
                }

                public SouthwestBean getSouthwest() {
                    return southwest;
                }

                public void setSouthwest(SouthwestBean southwest) {
                    this.southwest = southwest;
                }

                public static class NortheastBean {
                    /**
                     * lat : 32.7497122
                     * lng : -117.1204574
                     */

                    private double lat;
                    private double lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }

                public static class SouthwestBean {
                    /**
                     * lat : 32.7459267
                     * lng : -117.1231711
                     */

                    private double lat;
                    private double lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }
            }

            public static class LocationBean {
                /**
                 * lat : 32.7477512
                 * lng : -117.1219531
                 */

                private double lat;
                private double lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }
            }

            public static class ViewportBean {
                /**
                 * northeast : {"lat":32.7497122,"lng":-117.1204574}
                 * southwest : {"lat":32.7459267,"lng":-117.1231711}
                 */

                private NortheastBeanX northeast;
                private SouthwestBeanX southwest;

                public NortheastBeanX getNortheast() {
                    return northeast;
                }

                public void setNortheast(NortheastBeanX northeast) {
                    this.northeast = northeast;
                }

                public SouthwestBeanX getSouthwest() {
                    return southwest;
                }

                public void setSouthwest(SouthwestBeanX southwest) {
                    this.southwest = southwest;
                }

                public static class NortheastBeanX {
                    /**
                     * lat : 32.7497122
                     * lng : -117.1204574
                     */

                    private double lat;
                    private double lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }

                public static class SouthwestBeanX {
                    /**
                     * lat : 32.7459267
                     * lng : -117.1231711
                     */

                    private double lat;
                    private double lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }
            }
        }

        public static class AddressComponentsBean {
            /**
             * long_name : Jacob Dekema Freeway
             * short_name : I-805
             * types : ["route"]
             */

            private String long_name;
            private String short_name;
            private List<String> types;

            public String getLong_name() {
                return long_name;
            }

            public void setLong_name(String long_name) {
                this.long_name = long_name;
            }

            public String getShort_name() {
                return short_name;
            }

            public void setShort_name(String short_name) {
                this.short_name = short_name;
            }

            public List<String> getTypes() {
                return types;
            }

            public void setTypes(List<String> types) {
                this.types = types;
            }
        }
    }
}
