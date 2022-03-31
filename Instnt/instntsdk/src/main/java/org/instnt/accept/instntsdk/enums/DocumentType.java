package org.instnt.accept.instntsdk.enums;

public enum DocumentType {
    License, PassportCard;

    public static String getAllNames() {
        String names = "";
        for (int i = 0; i < DocumentType.values().length; i++) {
            if(i!=0)
                names+=", ";
            names+=DocumentType.values()[i].name();
        }
        return names;
    }
}
