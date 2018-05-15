package de.rebleyama.client.ui;

import com.badlogic.gdx.graphics.Color;


public enum TileColor {

    /**
     * Grassland
     */
    GRASSLANDS {
        private int grasslands = Color.rgba8888(new Color(Color.valueOf("4a8800")));
        @Override
        public int getColor() {
            return grasslands;
        }

    },
    /**
     * Forest, provides Wood
     */
    FOREST {
        private int forest = Color.rgba8888(new Color(Color.valueOf("326100")));

        @Override
        public int getColor() {
            return forest;
        }

    },
    /**
     * Mountain, provides Stone
     */
    MOUNTAINS {
        private int mountains = Color.rgba8888(Color.GRAY);

        @Override
        public int getColor() {
            return mountains;
        }

    },
    /**
     * River
     */
    RIVER {
        private int river = Color.rgba8888(new Color(Color.valueOf("031271")));

        @Override
        public int getColor() {
            return river;
        }

    },
    /**
     * Desert, resembles grasslands, but with a sand texture
     */
    DESERT {
        private int desert = Color.rgba8888(Color.YELLOW);

        @Override
        public int getColor() {
            return desert;
        }

    },
    /**
     * Water that can be passed
     */
    SHALLOW_WATER {
        private int shallow_water = Color.rgba8888(new Color(Color.valueOf("2f71b9")));

        @Override
        public int getColor() {
           return shallow_water;
        }

    },
    COAL {
        private int coal = Color.rgba8888(Color.DARK_GRAY);

        @Override
        public int getColor() {
            return coal;
        }

    },
    IRON {
        private int iron = Color.rgba8888(Color.LIGHT_GRAY);

        @Override
        public int getColor() {
            return iron;
        }
    };

    /**
     * Returns Color of the Terrain for the minimap
     */
    public abstract int getColor();

}
