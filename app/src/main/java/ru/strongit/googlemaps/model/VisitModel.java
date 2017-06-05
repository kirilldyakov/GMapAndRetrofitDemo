        package ru.strongit.googlemaps.model;

        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

        public class VisitModel {

            @SerializedName("title")
            @Expose
            private String title;
            @SerializedName("organizationId")
            @Expose
            private String organizationId;

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getOrganizationId() {
                return organizationId;
            }

            public void setOrganizationId(String organizationId) {
                this.organizationId = organizationId;
            }

        }