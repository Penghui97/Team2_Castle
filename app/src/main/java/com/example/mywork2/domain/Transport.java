package com.example.mywork2.domain;
/**
 * @author Jing
 * function: used for store a temp transport object in the program
 */
public class Transport {
    private String transportId;
    private String operator;
    private String type;

    public Transport() {}

    public Transport(String transportId, String operator, String type) {
        this.transportId = transportId;
        this.operator = operator;
        this.type = type;
    }

    public String getTransportId() {
        return transportId;
    }

    public void setTransportId(String transportId) {
        this.transportId = transportId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Transport{" +
                "transportId='" + transportId + '\'' +
                ", operator='" + operator + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
