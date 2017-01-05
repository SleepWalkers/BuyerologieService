package com.buyerologie.topic.enums;

public enum ImageType {

    LIST(1), COLLECT(2);

    private int type;

    private ImageType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static ImageType cast(int type) {
        for (ImageType imageType : ImageType.values()) {
            if (imageType.getType() == type) {
                return imageType;
            }
        }
        return null;
    }

}
