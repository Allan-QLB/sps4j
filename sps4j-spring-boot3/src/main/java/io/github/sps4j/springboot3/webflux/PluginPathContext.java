package io.github.sps4j.springboot3.webflux;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PluginPathContext implements Comparable<PluginPathContext> {
    private final String prefix;
    private final String context;

    @Override
    public int compareTo(PluginPathContext o) {
        return java.util.Comparator.comparing(PluginPathContext::getContext).compare(o, this);
    }
}
