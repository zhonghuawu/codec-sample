package com.envisioniot.transport;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
@EqualsAndHashCode
public class Endpoint implements Serializable {
    private final String host;
    private final int port;

    public Endpoint(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Endpoint(String hostAndPort) {
        List<String> splits = Splitter.on(':').trimResults().omitEmptyStrings().splitToList(hostAndPort);
        Preconditions.checkArgument(splits.size() == 2, "invalid host and port: " + hostAndPort);

        this.host = splits.get(0);
        this.port = Integer.parseInt(splits.get(1));
    }

    @Override
    public String toString() {
        return host + ":" + port;
    }
}
