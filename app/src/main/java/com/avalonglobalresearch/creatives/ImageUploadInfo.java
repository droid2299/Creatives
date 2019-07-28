package com.avalonglobalresearch.creatives;

public class ImageUploadInfo {

        public String imageName;

        public String imageURL;

        public String username;

        public String profile;

        public String uid;

        public ImageUploadInfo() {

        }

        public ImageUploadInfo(String name, String url , String username , String profile , String uid) {

            this.imageName = name;
            this.imageURL= url;
            this.username = username;
            this.profile = profile;
            this.uid = uid;
        }

        public String getImageName() {
            return imageName;
        }

        public String getImageURL() {
            return imageURL;
        }

        public String getUsername() {
        return username;
    }

        public String getProfile() {
        return profile;
    }

        public String getUid() {
        return uid;
    }
}
