package org.graylog.task.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {

    private final Logger logger = LogManager.getLogger(Message.class.getName());

    @JsonProperty("ClientDeviceType")
    public String clientDeviceType;

    @JsonProperty("ClientIP")
    public String clientIP;

    @JsonProperty("ClientIPClass")
    public String clientIPClass;

    @JsonProperty("ClientStatus")
    public Integer clientStatus;

    @JsonProperty("ClientRequestBytes")
    public Integer clientRequestBytes;

    @JsonProperty("ClientRequestReferer")
    public String clientRequestReferer;

    @JsonProperty("ClientRequestURI")
    public String clientRequestUri;

    @JsonProperty("ClientRequestUserAgent")
    public String clientRequestUserAgent;

    @JsonProperty("ClientSrcPort")
    public Integer clientSrcPort;

    @JsonProperty("EdgeServerIP")
    public String edgeServerIP;

    @JsonProperty("EdgeStartTimestamp")
    public Integer edgeStartTimestamp;

    @JsonProperty("DestinationIP")
    public String destinationIP;

    @JsonProperty("OriginResponseBytes")
    public Integer originResponseBytes;

    @JsonProperty("OriginResponseTime")
    public Integer originResponseTime;

    @Override
    public String toString() {
        String message = "";
        ObjectMapper mapper = new ObjectMapper();

        try {
            message = mapper.writeValueAsString(this);
        } catch (IOException ioe) {
            logger.error("Error writing message object as string", ioe.getMessage());
            ioe.printStackTrace();
        }

        return message;
    }
}
