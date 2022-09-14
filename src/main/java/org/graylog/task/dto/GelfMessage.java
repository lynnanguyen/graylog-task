package org.graylog.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class GelfMessage extends Message {
    @JsonProperty("host")
    public String host;

    @JsonProperty("facility")
    public String facility;

    @JsonProperty("short_message")
    public String shortMessage;

    @JsonProperty("full_message")
    public String fullMessage;

    @JsonProperty("_status")
    public Integer status;

    @JsonProperty("_source_ip")
    public String sourceIP;

    @JsonProperty("_destination_ip")
    public String targetIP;

    @JsonProperty("_server_ip")
    public String serverIP;

    @JsonProperty("_request_uri")
    public String requestUri;

    @JsonProperty("_device")
    public String device;

    private final Logger logger = LogManager.getLogger(GelfMessage.class.getName());

    public GelfMessage(Message message) {
        this.host = message.clientRequestReferer;
        this.facility = message.clientDeviceType;
        this.shortMessage = String.format("Accessing %s from IP: %s and received a %d.",
                this.host, message.clientIP, message.clientStatus);
        this.fullMessage = message.toString();
        this.status = message.clientStatus;
        this.sourceIP = message.clientIP;
        this.targetIP = message.destinationIP;
        this.serverIP = message.edgeServerIP;
        this.requestUri = message.clientRequestUri;
        this.device = message.clientDeviceType;

    }

    @Override
    public String toString() {
        String gelfMessage = "";
        ObjectMapper mapper = new ObjectMapper();

        try {
            gelfMessage = mapper.writeValueAsString(this);
        } catch (IOException ioe) {
            logger.error("Error writing GELF object as string", ioe.getMessage());
            ioe.printStackTrace();
        }

        return gelfMessage;
    }
}
