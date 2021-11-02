package com.debezium.utils;

/**
 * Derives type of operation specified in SourceRecord.value
 *
 * @see org.apache.kafka.connect.source.SourceRecord
 * */
public enum Operation {

    READ("r"),
    CREATE("c"),
    UPDATE("u"),
    DELETE("d");

    private final String code;

    private Operation(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    /**
     * Returns the type of operation from the code provided
     * */
    public static Operation forCode(String code) {
        Operation[] values = values();
        int len = values.length;

        //  Iterate through values and return specified operation
        for (int i = 0; i < len; ++i) {
            Operation operation = values[i];
            if (operation.getCode().equalsIgnoreCase(code)) {
                return operation;
            }
        }
        //  When we encounter an unknown operation
        return null;
    }

}








