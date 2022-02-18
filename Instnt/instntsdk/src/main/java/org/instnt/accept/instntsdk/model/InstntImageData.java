package org.instnt.accept.instntsdk.model;

public class InstntImageData {

    byte[] imageData;
    String type;
    String documentSide;

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public String getType() {
        return type;
    }

    public void setType(String imageType) {
        this.type = type;
    }

    public String getDocumentSide() {
        return documentSide;
    }

    public void setDocumentSide(String documentSide) {
        this.documentSide = documentSide;
    }
}
