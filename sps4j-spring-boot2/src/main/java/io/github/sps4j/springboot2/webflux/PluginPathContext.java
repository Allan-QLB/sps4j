package io.github.sps4j.springboot2.webflux;

import lombok.Builder;
import lombok.Getter;

import java.util.Comparator;

@Getter
@Builder
public class PluginPathContext implements Comparable<PluginPathContext> {
    private final String prefix;
    private final String context;

    @Override
    public int compareTo(PluginPathContext o) {
        return Comparator.comparing(PluginPathContext::getContext).compare(o, this);
    }
}
