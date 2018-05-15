package de.rebleyama.lib.utils;

import java.util.EventListener;

@FunctionalInterface
public interface TileReplacedListener extends EventListener {
    void tileChanged(TileReplacedEvent event);
}
